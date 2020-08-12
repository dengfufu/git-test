package com.zjft.usp.wms.flow.strategy.endtype.impl;

import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.wms.flow.dto.FlowInstanceNodeDealResultDto;
import com.zjft.usp.wms.flow.model.FlowInstance;
import com.zjft.usp.wms.flow.model.FlowInstanceNode;
import com.zjft.usp.wms.flow.service.FlowInstanceNodeService;
import com.zjft.usp.wms.flow.service.FlowInstanceService;
import com.zjft.usp.wms.flow.strategy.endtype.INodeEndStrategy;
import com.zjft.usp.wms.flow.strategy.endtype.dto.FlowEntitiesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 节点结束类型处理
 * 退回开始节点，把所有节点数据重置到初始状态
 *
 * @Author: JFZOU
 * @Date: 2019-11-11 11:15
 */

@Service(INodeEndStrategy.RETURN_START)
public class NodeEndReturnStartStrategy extends AbstractNodeEndCommonStrategy {

    @Autowired
    private FlowInstanceService flowInstanceService;

    @Autowired
    private FlowInstanceNodeService flowInstanceNodeService;

    @Override
    public FlowInstanceNode endNode(FlowInstanceNodeDealResultDto dealResultDto, UserInfo userInfo) {
        FlowEntitiesDto flowInstanceStrategyDto = this.getFlowEntities(dealResultDto.getNodeHandlerId());
        FlowInstance findFlowInstance = flowInstanceStrategyDto.getFlowInstance();

        Long flowInstanceId = findFlowInstance.getId();

        /**重置所有节点*/
        List<FlowInstanceNode> listNodesBy = this.flowInstanceNodeService.listAllBy(flowInstanceId);
        if (listNodesBy == null || listNodesBy.isEmpty()) {
            throw new AppException("该流程节点列表已不存在，无法处理流程流转，请检查！");
        }

        for (FlowInstanceNode instanceNode : listNodesBy) {
            this.flowInstanceNodeService.clearCompleted(instanceNode.getId());
        }

        /**重新设置流程currentNodeId*/
        this.flowInstanceService.updateCurrentNodeId(flowInstanceId, listNodesBy.get(0).getId());

        return listNodesBy.get(0);
    }
}
