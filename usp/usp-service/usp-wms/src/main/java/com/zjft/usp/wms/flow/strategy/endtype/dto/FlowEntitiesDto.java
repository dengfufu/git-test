package com.zjft.usp.wms.flow.strategy.endtype.dto;

import com.zjft.usp.wms.flow.model.FlowInstance;
import com.zjft.usp.wms.flow.model.FlowInstanceNode;
import lombok.Data;

/**
 * 仅用于封装flowInstance、currentInstanceNode，没有其他用途
 *
 * @Author: JFZOU
 * @Date: 2019-11-14 10:14
 */
@Data
public class FlowEntitiesDto {

    /**
     * 流程实例
     */
    private FlowInstance flowInstance;

    /**
     * 当前节点实例
     */
    private FlowInstanceNode currentInstanceNode;
}
