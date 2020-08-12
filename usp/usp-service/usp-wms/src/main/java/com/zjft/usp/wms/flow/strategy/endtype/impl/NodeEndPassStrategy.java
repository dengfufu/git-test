package com.zjft.usp.wms.flow.strategy.endtype.impl;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.wms.flow.dto.FlowInstanceNodeDealResultDto;
;
import com.zjft.usp.wms.flow.model.FlowInstance;
import com.zjft.usp.wms.flow.model.FlowInstanceNode;
import com.zjft.usp.wms.flow.service.FlowInstanceNodeService;
import com.zjft.usp.wms.flow.service.FlowInstanceService;
import com.zjft.usp.wms.flow.strategy.endtype.INodeEndStrategy;
import com.zjft.usp.wms.flow.strategy.endtype.dto.FlowEntitiesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 节点结束类型处理
 * 节点处理通过后，自动查找下一个待处理节点，如果查找OK，则进入下一个节点；
 * 如果查找不到，说明已到终止节点，此时可以结束流程。
 *
 * @Author: JFZOU
 * @Date: 2019-11-11 11:15
 */

@Service(INodeEndStrategy.PASS)
public class NodeEndPassStrategy extends AbstractNodeEndCommonStrategy {

    @Autowired
    private FlowInstanceService flowInstanceService;

    @Autowired
    private FlowInstanceNodeService flowInstanceNodeService;

    @Override
    public FlowInstanceNode endNode(FlowInstanceNodeDealResultDto dealResultDto, UserInfo userInfo) {
        FlowEntitiesDto flowEntitiesDto = this.getFlowEntities(dealResultDto.getNodeHandlerId());
        FlowInstance findFlowInstance = flowEntitiesDto.getFlowInstance();
        FlowInstanceNode currentNode = flowEntitiesDto.getCurrentInstanceNode();

        Long flowInstanceId = findFlowInstance.getId();


        /**自动查找下一个待处理节点，如果查找到，则进入下一个节点；如果查找不到，说明已到终止节点，此时可以结束流程*/
        FlowInstanceNode nextNode = this.flowInstanceNodeService.getTodoNextNode(currentNode);
        if (nextNode != null) {
            /**进入下一个节点*/
            this.flowInstanceService.updateCurrentNodeId(flowInstanceId, nextNode.getId());
            return nextNode;
        } else {
            /**结束流程*/
            this.flowInstanceService.endFlow(flowInstanceId, userInfo);
            return null;
        }
    }
}
