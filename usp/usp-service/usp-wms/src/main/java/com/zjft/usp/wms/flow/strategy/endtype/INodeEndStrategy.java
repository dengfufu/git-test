package com.zjft.usp.wms.flow.strategy.endtype;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.wms.flow.dto.FlowInstanceNodeDealResultDto;
import com.zjft.usp.wms.flow.model.FlowInstanceNode;

/**
 * 定义节点结束类型策略接口
 *
 * @Author: JFZOU
 * @Date: 2019-11-11 11:12
 */
public interface INodeEndStrategy {

    String PASS = "com.zjft.usp.flow.EndNodeStrategy_10";
    String RETURN_PREVIOUS_NODE = "com.zjft.usp.flow.EndNodeStrategy_20";
    String RETURN_START = "com.zjft.usp.flow.EndNodeStrategy_30";
    String NOT_PASS = "com.zjft.usp.flow.EndNodeStrategy_40";
    String RETURN_ASSIGN_NODE = "com.zjft.usp.flow.EndNodeStrategy_50";


    /**
     * 节点结束处理接口并返回下一个需要处理的节点对象
     * @param dto
     * @param userInfo
     * @return FlowInstanceNode
     */
    FlowInstanceNode updateDealResult(FlowInstanceNodeDealResultDto dto, UserInfo userInfo);
}
