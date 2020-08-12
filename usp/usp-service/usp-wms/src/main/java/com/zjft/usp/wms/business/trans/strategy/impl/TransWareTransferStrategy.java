package com.zjft.usp.wms.business.trans.strategy.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.baseinfo.enums.SmallClassEnum;
import com.zjft.usp.wms.business.trans.dto.TransConsignAuditDto;
import com.zjft.usp.wms.business.trans.dto.TransWareCommonDto;
import com.zjft.usp.wms.business.trans.enums.TransStatusEnum;
import com.zjft.usp.wms.business.trans.external.TransConvertToStockService;
import com.zjft.usp.wms.business.trans.model.TransWareCommon;
import com.zjft.usp.wms.business.trans.service.TransWareCommonService;
import com.zjft.usp.wms.flow.composite.FlowInstanceCompoService;
import com.zjft.usp.wms.flow.enums.NodeEndTypeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物料库存调度策略实现类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2019-11-21 18:52
 **/
@Component(AbstractTransStrategy.TRANS_WARE_TRANSFER)
public class TransWareTransferStrategy extends AbstractTransStrategy {




    /**
     * 保存申请单
     *
     * @author Qiugm
     * @date 2019-11-21
     * @param transWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    @Override
    public void save(TransWareCommonDto transWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        super.save(transWareCommonDto, userInfo, reqParam);
    }

    /**
     * 添加调拨单申请信息并自动拆单
     *
     * @author Qiugm
     * @date 2019-11-21
     * @param transWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    @Override
    public void add(TransWareCommonDto transWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        super.add(transWareCommonDto, userInfo, reqParam);
    }

    /**
     * 审批申请单
     *
     * @author Qiugm
     * @date 2019-11-21
     * @param transWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    @Override
    public void batchAudit(TransWareCommonDto transWareCommonDto, UserInfo userInfo, ReqParam reqParam,Integer status) {
        if(transWareCommonDto.getIdList() != null ){
            super.batchAudit(transWareCommonDto,userInfo,reqParam,status);
            return;
        }
        // 用于调拨的审批
        if (CollectionUtil.isEmpty(transWareCommonDto.getTransConsignAuditDtoList())) {
            throw new AppException("请选择需要审批的入库单");
        }
        List<Long> idList = new ArrayList<>();
        Map<Long,TransConsignAuditDto> idAndAuditMap = new HashMap<>();
        for(TransConsignAuditDto dto : transWareCommonDto.getTransConsignAuditDtoList()) {
            idList.add(dto.getId());
            idAndAuditMap.put(dto.getId(),dto);
        }

        Map<Long, TransWareCommon> idAndWareMap = this.transWareCommonService.mapIdAndObject(idList);
        List<TransWareCommon> updateStatusList = new ArrayList<>();
        //TODO
        for(Long id : idList){
            TransWareCommon transWareCommon = idAndWareMap.get(id);
            TransConsignAuditDto transConsignAuditDto = idAndAuditMap.get(id);
            TransWareCommonDto transWareCommonDtoSingle = new TransWareCommonDto();
            BeanUtils.copyProperties(transWareCommon,transWareCommonDtoSingle);
            // 设置审批发货数量以及发货库房
            transWareCommon.setPassedQuantity(transConsignAuditDto.getPassedQuantity());
            transWareCommon.setFromDepotId(transConsignAuditDto.getFromDepotId());
            transWareCommon.setTransStatus(status);

            transWareCommonDtoSingle.setNodeTypeId(NodeEndTypeEnum.PASS.getCode());
            transWareCommonDto.setDoDescried(transWareCommonDto.getDescription());

            // 结束当前结点
            this.endCurrentNode(transWareCommonDtoSingle, userInfo);
            updateStatusList.add(transWareCommon);
            // 流程结束，调整库存
            if (this.flowInstanceCompoService.isEndFlow(transWareCommon.getFlowInstanceId())) {
                // 判读是否进行调库
                if(transWareCommonDto.getIsAdjust()){
                    this.stockAndLogTxExternalService.adjustStock(transWareCommonDto);
                }
                if(transWareCommonDto.getSmallClassId() == SmallClassEnum.TRANS_WARE_SHIFT.getCode()){
                    transWareCommon.setTransStatus(TransStatusEnum.COMPLETE_ALLOCATION.getCode());
                }
            }
        }
        if(CollectionUtil.isNotEmpty(updateStatusList)) {
            this.transWareCommonService.updateBatchById(updateStatusList);
        }
    }

}
