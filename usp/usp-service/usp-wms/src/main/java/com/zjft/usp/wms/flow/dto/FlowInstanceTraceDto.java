package com.zjft.usp.wms.flow.dto;

import com.zjft.usp.wms.flow.model.FlowInstanceTrace;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: JFZOU
 * @Date: 2019-11-14 16:52
 */
@Getter
@Setter
public class FlowInstanceTraceDto extends FlowInstanceTrace {

    @ApiModelProperty(value = "处理人姓名")
    private String completedByName;

    @ApiModelProperty(value = "节点名称")
    private String templateNodeName;

}
