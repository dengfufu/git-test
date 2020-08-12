package com.zjft.usp.wms.flow.strategy.endtype.impl;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.wms.flow.dto.FlowInstanceNodeDealResultDto;
import com.zjft.usp.wms.flow.model.FlowInstanceNode;
import com.zjft.usp.wms.flow.service.FlowInstanceService;
import com.zjft.usp.wms.flow.strategy.endtype.INodeEndStrategy;
import com.zjft.usp.wms.flow.strategy.endtype.dto.FlowEntitiesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 节点结束类型处理
 * 节点不通过，结束流程的策略处理。
 *
 * @Author: JFZOU
 * @Date: 2019-11-11 11:15
 */

@Service(INodeEndStrategy.NOT_PASS)
public class NodeEndNotPassStrategy extends AbstractNodeEndCommonStrategy {

    @Autowired
    private FlowInstanceService flowInstanceService;

    @Override
    public FlowInstanceNode endNode(FlowInstanceNodeDealResultDto dealResultDto, UserInfo userInfo) {
        FlowEntitiesDto flowEntices = this.getFlowEntities(dealResultDto.getNodeHandlerId());
        Long flowInstanceId = flowEntices.getFlowInstance().getId();
        /**结束流程*/
        this.flowInstanceService.endFlow(flowInstanceId, userInfo);

        return null;
    }
}
