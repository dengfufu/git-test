package com.zjft.usp.wms.flow.dto;

import com.zjft.usp.wms.flow.model.FlowClassNode;
import lombok.Data;

import java.util.List;

/**
 * @Author: JFZOU
 * @Date: 2019-11-08 10:41
 */
@Data
public class FlowClassNodeDto extends FlowClassNode {
    /**
     * 获得业务类型节点列表
     */
    private List<FlowClassNode> flowClassNodeList;
}
