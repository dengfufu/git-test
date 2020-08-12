package com.zjft.usp.wms.business.consign.strategy.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.consign.dto.ConsignMainDto;
import com.zjft.usp.wms.business.consign.external.ConsignStockExternalService;
import com.zjft.usp.wms.business.consign.model.ConsignDetail;
import com.zjft.usp.wms.business.consign.service.ConsignDetailService;
import com.zjft.usp.wms.business.consign.strategy.ConsignStrategy;
import com.zjft.usp.wms.business.outcome.composite.OutcomeCompoService;
import com.zjft.usp.wms.business.outcome.dto.OutcomeWareCommonDto;
import com.zjft.usp.wms.business.outcome.model.OutcomeWareCommon;
import com.zjft.usp.wms.business.outcome.service.OutcomeWareCommonService;
import com.zjft.usp.wms.flow.composite.FlowInstanceCompoService;
import com.zjft.usp.wms.flow.enums.NodeEndTypeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 物料出库发货实现类
 * @author zphu
 * @date 2019/12/4 9:53
**/
@Component(ConsignStrategy.OUTCOME)
public class OutcomeWareConsignStrategy extends AbstractConsignStrategy {

    @Autowired
    private OutcomeCompoService outcomeCompoService;
    @Autowired
    private OutcomeWareCommonService outcomeWareCommonService;
    @Autowired
    private ConsignDetailService consignDetailService;
    @Resource
    private ConsignStockExternalService consignStockExternalService;
    @Autowired
    private FlowInstanceCompoService flowInstanceCompoService;

    @Override
    public void add(ConsignMainDto consignMainDto, UserInfo userInfo, ReqParam reqParam) {
        super.add(consignMainDto,userInfo,reqParam);
        //设置详情表信息
        List<ConsignDetail> consignDetailList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(consignMainDto.getConsignDetailDtoList())){
            List<Long> detailList = consignMainDto.getConsignDetailDtoList().stream().map(e -> e.getId()).distinct().collect(Collectors.toList());
            Map<Long, OutcomeWareCommon> mapIdAndObject = this.outcomeWareCommonService.mapIdAndObject(detailList);
            // 统计每个出库记录是否都发货完成
            Map<Long,Integer> mapIdAndQuan = new HashMap<>();
            if(CollectionUtil.isNotEmpty(detailList)){
                consignMainDto.getConsignDetailDtoList().forEach(consignDetailDto -> {
                    if(IntUtil.isNotZero(mapIdAndQuan.get(consignDetailDto.getId()))) {
                        mapIdAndQuan.put(consignDetailDto.getId(),mapIdAndQuan.get(consignDetailDto.getId()) + consignDetailDto.getQuantity());
                    }else {
                        mapIdAndQuan.put(consignDetailDto.getId(),consignDetailDto.getQuantity());
                    }
                });
                // 判断是否拆单
                List<OutcomeWareCommon> outcomeWareCommonList = new ArrayList<>();
                detailList.forEach(detailId ->{
                    OutcomeWareCommon outcomeWareCommon = mapIdAndObject.get(detailId);
                    if(outcomeWareCommon != null){
                        // 拆单
                        if(outcomeWareCommon.getQuantity() > mapIdAndQuan.get(detailId)){
                            OutcomeWareCommon outcomeWareCommonSplit = new OutcomeWareCommon();
                            Long instanceBySplit = this.flowInstanceCompoService.createInstanceBySplit(outcomeWareCommon.getFlowInstanceId(), userInfo);
                            BeanUtils.copyProperties(outcomeWareCommon,outcomeWareCommonSplit);
                            outcomeWareCommonSplit.setQuantity(outcomeWareCommon.getQuantity() - mapIdAndQuan.get(detailId));
                            outcomeWareCommonSplit.setId(KeyUtil.getId());
                            outcomeWareCommonSplit.setFlowInstanceId(instanceBySplit);
                            outcomeWareCommonList.add(outcomeWareCommonSplit);

                            outcomeWareCommon.setQuantity(mapIdAndQuan.get(detailId));
                            outcomeWareCommonList.add(outcomeWareCommon);
                        }else if(outcomeWareCommon.getQuantity() < mapIdAndQuan.get(detailId)){
                            throw new AppException("出库单（" + outcomeWareCommon.getOutcomeCode() + "）发货数量超过出库数量");
                        }
                    }
                });
                // 更新出库主表信息
                if(CollectionUtil.isNotEmpty(outcomeWareCommonList)){
                    this.outcomeWareCommonService.saveOrUpdateBatch(outcomeWareCommonList);
                }
            }

            consignMainDto.getConsignDetailDtoList().forEach(consignDetailDto -> {
            ConsignDetail consignDetail = new ConsignDetail();
            BeanUtils.copyProperties(consignDetailDto, consignDetail);
            OutcomeWareCommon outcomeWareCommon = mapIdAndObject.get(consignDetailDto.getId());
            // 复制出库信息
            BeanUtils.copyProperties(outcomeWareCommon, consignDetail);
            consignDetail.setId(KeyUtil.getId());
            consignDetail.setFromDepotId(outcomeWareCommon.getDepotId());
            consignDetail.setFormDetailId(outcomeWareCommon.getId());
            consignDetail.setConsignMainId(consignMainDto.getId());
            consignDetail.setQuantity(consignDetailDto.getQuantity());
            consignDetailList.add(consignDetail);
            // 调用调库方法
            this.consignStockExternalService.adjustStockConsign(consignDetail,userInfo);
            });

            this.consignDetailService.saveBatch(consignDetailList);
            //结束节点并审核节点
            OutcomeWareCommonDto outcomeWareCommonDto = new OutcomeWareCommonDto();
            outcomeWareCommonDto.setIdList(detailList);
            outcomeWareCommonDto.setAuditNote(consignMainDto.getDescription());
            outcomeWareCommonDto.setNodeEndTypeId(NodeEndTypeEnum.PASS.getCode());
            outcomeWareCommonDto.setIsAdjust(false);
            this.outcomeCompoService.batchAudit(outcomeWareCommonDto,userInfo,reqParam);
        }
    }

    @Override
    public void receive(ConsignMainDto consignMainDto, UserInfo userInfo, ReqParam reqParam) {
        super.receive(consignMainDto,userInfo,reqParam);
        //结束节点并审核节点
        OutcomeWareCommonDto outcomeWareCommonDto = new OutcomeWareCommonDto();
        List<Long> formDetailIdList = consignMainDto.getConsignDetailDtoList().stream().map(e -> e.getFormDetailId()).distinct().collect(Collectors.toList());
        outcomeWareCommonDto.setIdList(formDetailIdList);
        outcomeWareCommonDto.setAuditNote(consignMainDto.getDescription());
        outcomeWareCommonDto.setNodeEndTypeId(NodeEndTypeEnum.PASS.getCode());
        outcomeWareCommonDto.setIsAdjust(false);
        this.outcomeCompoService.batchAudit(outcomeWareCommonDto,userInfo,reqParam);
    }
}
