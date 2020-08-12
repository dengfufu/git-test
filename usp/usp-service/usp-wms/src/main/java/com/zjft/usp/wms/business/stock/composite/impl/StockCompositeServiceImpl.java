package com.zjft.usp.wms.business.stock.composite.impl;

import cn.hutool.core.date.DateUtil;
import com.zengtengpeng.annotation.Lock;
import com.zengtengpeng.enums.LockModel;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.wms.business.stock.composite.StockCompositeService;
import com.zjft.usp.wms.business.stock.composite.dto.StockNormalAdjustDto;
import com.zjft.usp.wms.business.stock.composite.dto.StockNormalUnAdjustDto;
import com.zjft.usp.wms.business.stock.model.StockCommon;
import com.zjft.usp.wms.business.stock.service.StockCommonService;
import com.zjft.usp.wms.flow.enums.EnabledEnum;
import com.zjft.usp.wms.flow.enums.StockTypeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 库存服务聚合类
 * 处理库存信息变更、添加交易流水记录
 * 对于库存信息变更，需要使用redission分布式锁处理，防止数据在高并发时出现修改错误。
 * 分布式锁使用原则：存在同时修改某一行记录的业务，如果同时修改不加锁会导致数据修改错误。
 * 原则上，插入、删除操作不需要使用分布式锁。
 * 为了提升并发性能，分布式锁请加在最小方法中。
 * 使用@Lock注解(lockModel = LockModel.REENTRANT, keys = "#stockNormalAdjustDto.sourceStockId")
 * 联调时需要重点测试事务@Transactional与锁@Lock会不会有冲突
 *
 * @Author: JFZOU
 * @Date: 2019-11-22 17:07
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StockCompositeServiceImpl implements StockCompositeService {

    @Autowired
    private StockCommonService stockCommonService;

    @Override
    public void unAdjustSituationQty(StockNormalUnAdjustDto stockNormalUnAdjustDto) {
        //TODO 待实现
    }

    /**
     * 每次调整时，都需要把原来的库存明细记录做全量拆分即1生1.1，1.2，1.1再拆分成1.1.1，1.1.2
     * 对于有号有码的stockCommon，原来的置为不可用，新增1条新的stockCommon
     * 对于无号无码的stockCommon，假如原来有10个，现在调拨出2个，原来的置为不可用，新增2条新的stockCommon，1条为10-2，一条为2
     *
     * @param stockNormalAdjustDto 正常调整专用DTO对象
     */
    @Override
    @Lock(lockModel = LockModel.REENTRANT, keys = "#stockNormalAdjustDto.sourceStockId")
    public void adjustSituationQty(StockNormalAdjustDto stockNormalAdjustDto) {

        StockCommon sourceStockCommon = stockCommonService.getById(stockNormalAdjustDto.getSourceStockId());
        if (sourceStockCommon == null) {
            throw new AppException("库存记录已不存在，请检查！");
        }

        if (sourceStockCommon.isExistsBarcode() || sourceStockCommon.isExistsSN()) {
            adjustPartIdOrBar(stockNormalAdjustDto, sourceStockCommon);
        } else {
            adjustNoPartIdNoBar(stockNormalAdjustDto, sourceStockCommon);
        }
    }

    /**
     * 调整有号或者有码
     *
     * @param stockNormalAdjustDto
     * @param sourceStockCommon
     */
    private void adjustPartIdOrBar(StockNormalAdjustDto stockNormalAdjustDto, StockCommon sourceStockCommon) {

        updateOldStockCommonOut(stockNormalAdjustDto, sourceStockCommon);

        /**新添加2条新的库存明细（如在途、出库）*/
        StockCommon getNewTargetStockCommon = getNewTargetStockCommon(stockNormalAdjustDto, sourceStockCommon);
        stockCommonService.save(getNewTargetStockCommon);
    }

    /**
     * 调整无号或者无码
     *
     * @param stockNormalAdjustDto
     * @param sourceStockCommon
     */
    private void adjustNoPartIdNoBar(StockNormalAdjustDto stockNormalAdjustDto, StockCommon sourceStockCommon) {

        if (sourceStockCommon.getIncomeQty().compareTo(stockNormalAdjustDto.getAdjustQty()) >= 0) {

            updateOldStockCommonOut(stockNormalAdjustDto, sourceStockCommon);

            if (sourceStockCommon.getIncomeQty().compareTo(stockNormalAdjustDto.getAdjustQty()) > 0) {
                StockCommon getNewSourceStockCommon = getNewSourceStockCommon(stockNormalAdjustDto, sourceStockCommon);
                stockCommonService.save(getNewSourceStockCommon);
            }

            StockCommon getNewTargetStockCommon = getNewTargetStockCommon(stockNormalAdjustDto, sourceStockCommon);
            stockCommonService.save(getNewTargetStockCommon);

        } else {
            throw new AppException("库存数量不足，当前库存只有【" + sourceStockCommon.getIncomeQty() + "】，请检查！");
        }
    }

    /**
     * 将原来的记录置为不可用，记录时间，业务单号等
     *
     * @param stockNormalAdjustDto
     * @param sourceStockCommon
     */
    private void updateOldStockCommonOut(StockNormalAdjustDto stockNormalAdjustDto, StockCommon sourceStockCommon) {
        StockCommon oldTempStockCommon = new StockCommon();

        oldTempStockCommon.setId(sourceStockCommon.getId());
        oldTempStockCommon.setEnabled(EnabledEnum.NO.getCode());
        oldTempStockCommon.setOutFormDetailId(stockNormalAdjustDto.getFormDetailId());
        oldTempStockCommon.setOutLargeClassId(stockNormalAdjustDto.getLargeClassId());
        oldTempStockCommon.setOutSmallClassId(stockNormalAdjustDto.getSmallClassId());
        oldTempStockCommon.setOutCreateBy(stockNormalAdjustDto.getDoBy());
        oldTempStockCommon.setOutCreateTime(DateUtil.date().toTimestamp());

        stockCommonService.updateById(oldTempStockCommon);
    }

    private StockCommon getNewSourceStockCommon(StockNormalAdjustDto stockNormalAdjustDto, StockCommon oldStockCommon) {
        StockCommon getNewSourceStockCommon = getNewTargetStockCommon(stockNormalAdjustDto, oldStockCommon);
        getNewSourceStockCommon.setDepotId(oldStockCommon.getDepotId());
        getNewSourceStockCommon.setSituation(oldStockCommon.getSituation());
        getNewSourceStockCommon.setIncomeQty(oldStockCommon.getIncomeQty() - stockNormalAdjustDto.getAdjustQty());
        return getNewSourceStockCommon;
    }

    /**
     * 按照数据库表字段顺序分别设置属性，不使用beanUtil工具，直接看到哪些设置属性，哪些没有设置属性
     *
     * @param stockNormalAdjustDto
     * @param oldStockCommon
     * @return
     */
    private StockCommon getNewTargetStockCommon(StockNormalAdjustDto stockNormalAdjustDto, StockCommon oldStockCommon) {
        StockCommon newStockCommon = new StockCommon();
        newStockCommon.setId(KeyUtil.getId());
        newStockCommon.setCorpId(oldStockCommon.getCorpId());
        newStockCommon.setStockType(StockTypeEnum.STOCK_DEPOT.getCode());
        newStockCommon.setInFormDetailId(stockNormalAdjustDto.getFormDetailId());
        newStockCommon.setInLargeClassId(stockNormalAdjustDto.getLargeClassId());
        newStockCommon.setInSmallClassId(stockNormalAdjustDto.getSmallClassId());
        newStockCommon.setDepotId(stockNormalAdjustDto.getNewDepotId());
        newStockCommon.setModelId(oldStockCommon.getModelId());
        newStockCommon.setNormsValue(oldStockCommon.getNormsValue());
        newStockCommon.setSituation(stockNormalAdjustDto.getNewSituation());
        newStockCommon.setStatus(oldStockCommon.getStatus());
        newStockCommon.setPropertyRight(oldStockCommon.getPropertyRight());
        newStockCommon.setSn(oldStockCommon.getSn());
        newStockCommon.setBarcode(oldStockCommon.getBarcode());
        newStockCommon.setIncomeQty(stockNormalAdjustDto.getAdjustQty());
        newStockCommon.setDirectSourceStockId(oldStockCommon.getId());

        if (oldStockCommon.getRootSourceStockId() > 0) {
            newStockCommon.setRootSourceStockId(oldStockCommon.getRootSourceStockId());
        } else {
            newStockCommon.setRootSourceStockId(oldStockCommon.getId());
        }
        newStockCommon.setServiceEndDate(oldStockCommon.getServiceEndDate());
        newStockCommon.setUnitPrice(oldStockCommon.getUnitPrice());
        newStockCommon.setTaxRate(oldStockCommon.getTaxRate());
        newStockCommon.setPurchaseType(oldStockCommon.getPurchaseType());
        newStockCommon.setInCreateBy(stockNormalAdjustDto.getDoBy());
        newStockCommon.setInCreateTime(DateUtil.date().toTimestamp());
        newStockCommon.setEnabled(EnabledEnum.YES.getCode());
        return newStockCommon;
    }

}
