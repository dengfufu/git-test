package com.zjft.usp.wms.flow.dto;

import com.zjft.usp.wms.flow.model.FlowInstanceNodeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程实例节点处理人Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2019-12-04 19:51
 **/
@ApiModel(value = "流程实例节点处理人Dto")
@Getter
@Setter
public class FlowInstanceNodeHandlerDto extends FlowInstanceNodeHandler {

    @ApiModelProperty(value = "流程实例ID")
    private Long flowInstanceId;

}
