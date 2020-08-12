package com.zjft.usp.wms.flow.strategy.endtype.impl;

import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
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
 * 返回指定节点，把指定节点及以后的都清空
 *
 * @Author: JFZOU
 * @Date: 2019-11-11 11:15
 */

@Service(INodeEndStrategy.RETURN_ASSIGN_NODE)
public class NodeEndReturnAssignStrategy extends AbstractNodeEndCommonStrategy {

    @Autowired
    private FlowInstanceNodeService flowInstanceNodeService;

    @Override
    public FlowInstanceNode endNode(FlowInstanceNodeDealResultDto dealResultDto, UserInfo userInfo) {

        FlowEntitiesDto flowEntitiesDto = this.getFlowEntities(dealResultDto.getNodeHandlerId());
        FlowInstance flowInstance = flowEntitiesDto.getFlowInstance();
        FlowInstanceNode currentNode = flowEntitiesDto.getCurrentInstanceNode();

        if (LongUtil.isZero(dealResultDto.getAssignNodeId())) {
            throw new AppException("请指定需要退回的节点，请检查！");
        }

        FlowInstanceNode assignNode = this.flowInstanceNodeService.getById(dealResultDto.getAssignNodeId());
        if (assignNode == null) {
            throw new AppException("系统查找该流程的指定退回节点信息失败，请检查！");
        }

        if (currentNode.getSortNo().intValue() < assignNode.getSortNo().intValue()) {
            throw new AppException("指定的退回节点逻辑不对，退回节点不能排在当前节点的后面，请检查！");
        }

        super.resetFrontNodeData(flowInstance.getId(), assignNode);
        return assignNode;
    }
}
