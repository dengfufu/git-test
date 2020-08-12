package com.zjft.usp.wms.business.consign.strategy.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.consign.dto.ConsignMainDto;
import com.zjft.usp.wms.business.consign.external.ConsignStockExternalService;
import com.zjft.usp.wms.business.consign.model.ConsignDetail;
import com.zjft.usp.wms.business.consign.service.ConsignDetailService;
import com.zjft.usp.wms.business.consign.strategy.ConsignStrategy;
import com.zjft.usp.wms.business.stock.dto.StockCommonDto;
import com.zjft.usp.wms.business.stock.model.StockCommon;
import com.zjft.usp.wms.business.stock.service.StockCommonService;
import com.zjft.usp.wms.business.trans.composite.TransCompoService;
import com.zjft.usp.wms.business.trans.dto.TransWareCommonDto;
import com.zjft.usp.wms.business.trans.enums.TransStatusEnum;
import com.zjft.usp.wms.business.trans.model.TransWareCommon;
import com.zjft.usp.wms.business.trans.service.TransWareCommonService;
import com.zjft.usp.wms.flow.composite.FlowInstanceCompoService;
import com.zjft.usp.wms.flow.enums.NodeEndTypeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 调拨发货实现类
 * @author zphu
 * @date 2019/12/4 9:53
**/
@Component(ConsignStrategy.TRANS)
public class TransWareConsignStrategy extends AbstractConsignStrategy {

    @Autowired
    private TransCompoService transCompoService;
    @Autowired
    private TransWareCommonService transWareCommonService;
    @Autowired
    private StockCommonService stockCommonService;
    @Autowired
    private FlowInstanceCompoService flowInstanceCompoService;
    @Resource
    private ConsignStockExternalService consignStockExternalService;
    @Resource
    private ConsignDetailService consignDetailService;

    @Override
    public void add(ConsignMainDto consignMainDto, UserInfo userInfo, ReqParam reqParam) {
        super.add(consignMainDto,userInfo,reqParam);
        //设置详情表信息
        List<ConsignDetail> consignDetailList = new ArrayList<>();

        if(CollectionUtil.isNotEmpty(consignMainDto.getTransWareCommonDtoList())){
            List<Long> detailList = new ArrayList<>();
            List<Long> stockIdList = new ArrayList<>();
            consignMainDto.getTransWareCommonDtoList().forEach(transWareCommonDto -> {
                stockIdList.addAll(transWareCommonDto.getStockCommonDtoList().stream().map(e -> e.getId()).distinct().collect(Collectors.toList()));
                detailList.add(transWareCommonDto.getId());
            });
            // 获取调拨map
            Map<Long, TransWareCommon> mapIdAndTrans = this.transWareCommonService.mapIdAndObject(detailList);
            Map<Long, StockCommon> mapIdAndStock = this.stockCommonService.mapIdAndObject(stockIdList);

            // 需要拆分和添加的流程
            List<TransWareCommon> transWareCommonList = new ArrayList<>();
            consignMainDto.getTransWareCommonDtoList().forEach(transWareCommonDto -> {
                TransWareCommon transWareCommon = mapIdAndTrans.get(transWareCommonDto.getId());
                //该单发货数量
                Integer consignQuantity = 0;
                if(CollectionUtil.isNotEmpty(transWareCommonDto.getStockCommonDtoList())) {
                    for(StockCommonDto stockCommonDto : transWareCommonDto.getStockCommonDtoList()){
                        ConsignDetail consignDetail = new ConsignDetail();
                        StockCommon stockCommon = mapIdAndStock.get(stockCommonDto.getId());
                        // 备件信息复制
                        BeanUtils.copyProperties(stockCommon, consignDetail);
                        // 复制调拨信息
                        BeanUtils.copyProperties(transWareCommon, consignDetail);
                        consignDetail.setId(KeyUtil.getId());
                        consignDetail.setStockId(stockCommonDto.getId());
                        consignDetail.setFormDetailId(transWareCommonDto.getId());
                        consignDetail.setConsignMainId(consignMainDto.getId());
                        // 设置数量和分箱号
                        consignDetail.setQuantity(stockCommonDto.getQuantity());
                        consignDetail.setSubBoxNum(stockCommonDto.getSubBoxNum());
                        consignDetailList.add(consignDetail);
                        // 调整库存
                        this.consignStockExternalService.adjustStockConsign(consignDetail,userInfo);
                        // 累计发货数量
                        consignQuantity += stockCommonDto.getQuantity();
                    }
                }
                //发货数量不足，需要拆单
                if(consignQuantity < transWareCommon.getPassedQuantity()){
                    // 拆单
                    Long instanceBySplit = this.flowInstanceCompoService.createInstanceBySplit(transWareCommon.getFlowInstanceId(), userInfo);
                    TransWareCommon transWareCommonSplit = new TransWareCommon();
                    BeanUtils.copyProperties(transWareCommon,transWareCommonSplit);
                    transWareCommonSplit.setId(KeyUtil.getId());
                    // 该值可能存在负值，需要后续修改处理一下
                    transWareCommonSplit.setApplyQuantity(transWareCommon.getApplyQuantity() - consignQuantity);
                    transWareCommonSplit.setPassedQuantity(transWareCommon.getPassedQuantity() - consignQuantity);
                    transWareCommonSplit.setFlowInstanceId(instanceBySplit);
                    transWareCommonList.add(transWareCommonSplit);

                    transWareCommon.setApplyQuantity(consignQuantity);
                    transWareCommon.setPassedQuantity(consignQuantity);
                    transWareCommonList.add(transWareCommon);
                }else if(consignQuantity > transWareCommon.getPassedQuantity()){
                    throw new AppException("调拨单（" + transWareCommon.getTransCode() + "）发货数量超过审批通过数量");
                }
            });

            // 拆单后更新主表
            if(CollectionUtil.isNotEmpty(transWareCommonList)){
                transWareCommonService.saveOrUpdateBatch(transWareCommonList);
            }

            this.consignDetailService.saveBatch(consignDetailList);
            //结束节点并审核节点
            TransWareCommonDto transWareCommonDto = new TransWareCommonDto();
            transWareCommonDto.setIdList(detailList);
            transWareCommonDto.setSmallClassId(consignMainDto.getSmallClassId());
            transWareCommonDto.setDoDescried(consignMainDto.getDescription());
            transWareCommonDto.setNodeTypeId(NodeEndTypeEnum.PASS.getCode());
            transWareCommonDto.setIsAdjust(false);
            this.transCompoService.batchAudit(transWareCommonDto, userInfo, reqParam, TransStatusEnum.IN_ALLOCATION.getCode());
        }
    }

    @Override
    public void receive(ConsignMainDto consignMainDto, UserInfo userInfo, ReqParam reqParam) {
        super.receive(consignMainDto,userInfo,reqParam);
        //结束节点并审核节点
        List<Long> formDetailIdList = consignMainDto.getConsignDetailDtoList().stream()
                .map(e -> e.getFormDetailId()).distinct().collect(Collectors.toList());
        TransWareCommonDto transWareCommonDto = new TransWareCommonDto();
        transWareCommonDto.setIdList(formDetailIdList);
        transWareCommonDto.setDoDescried(consignMainDto.getDescription());
        transWareCommonDto.setNodeTypeId(NodeEndTypeEnum.PASS.getCode());
        transWareCommonDto.setSmallClassId(consignMainDto.getSmallClassId());
        transWareCommonDto.setIsAdjust(false);
        this.transCompoService.batchAudit(transWareCommonDto, userInfo, reqParam,TransStatusEnum.COMPLETE_ALLOCATION.getCode());
    }

}
