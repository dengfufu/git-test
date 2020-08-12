package com.zjft.usp.wms.flow.strategy.endtype.impl;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.wms.flow.dto.FlowInstanceNodeDealResultDto;
import com.zjft.usp.wms.flow.model.FlowInstance;
import com.zjft.usp.wms.flow.model.FlowInstanceNode;
import com.zjft.usp.wms.flow.service.FlowInstanceNodeService;
import com.zjft.usp.wms.flow.strategy.endtype.INodeEndStrategy;
import com.zjft.usp.wms.flow.strategy.endtype.dto.FlowEntitiesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 节点结束类型处理
 * 返回上一个节点，把当前节点内容清空
 *
 * @Author: JFZOU
 * @Date: 2019-11-11 11:15
 */

@Service(INodeEndStrategy.RETURN_PREVIOUS_NODE)
public class NodeEndReturnPreviousStrategy extends AbstractNodeEndCommonStrategy {

    @Autowired
    private FlowInstanceNodeService flowInstanceNodeService;

    @Override
    public FlowInstanceNode endNode(FlowInstanceNodeDealResultDto dealResultDto, UserInfo userInfo) {
        FlowEntitiesDto flowInstanceStrategyDto = this.getFlowEntities(dealResultDto.getNodeHandlerId());
        FlowInstance flowInstance = flowInstanceStrategyDto.getFlowInstance();
        FlowInstanceNode currentNode = flowInstanceStrategyDto.getCurrentInstanceNode();

        Long flowInstanceId = flowInstance.getId();

        FlowInstanceNode previousNode = this.flowInstanceNodeService.getTodoPreviousNode(currentNode);
        if (previousNode != null) {
            super.resetFrontNodeData(flowInstanceId, previousNode);
        }
        return previousNode;
    }


}
