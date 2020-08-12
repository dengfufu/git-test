package com.zjft.usp.wms.flow.dto;

import com.zjft.usp.wms.flow.model.FlowInstance;
import com.zjft.usp.wms.flow.model.FlowInstanceNode;
import lombok.Data;

import java.util.List;

/**
 * @Author: JFZOU
 * @Date: 2019-11-11 9:09
 */
@Data
public class FlowInstanceDto extends FlowInstance {

    /**
     * 流程实例对象
     */
    private FlowInstance flowInstance;

    /**
     * 流程实例节点对象列表
     */
    private List<FlowInstanceNode> flowInstanceNodeList;
}
