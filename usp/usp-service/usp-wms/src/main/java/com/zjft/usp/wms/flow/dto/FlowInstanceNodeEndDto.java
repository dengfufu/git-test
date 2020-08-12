package com.zjft.usp.wms.flow.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.wms.config.DateToLongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: JFZOU
 * @Date: 2019-11-18 15:30
 */
@Data
public class FlowInstanceNodeEndDto {

    @ApiModelProperty(value = "流程节点ID")
    @TableId("id")
    private Long currentNodeId;

    @ApiModelProperty(value = "流程节点结束类型(实际结束时填写)")
    private Integer completedNodeEndTypeId;

    @ApiModelProperty(value = "实际处理人ID(实际结束时填写)")
    private Long completedBy;

    @ApiModelProperty(value = "实际处理时间(实际结束时填写)")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date completedTime;

    @ApiModelProperty(value = "实际处理描述(实际结束时填写)")
    private String completedDescription;

    @ApiModelProperty(value = "是否已完成 (Y=是,N=否,实际结束时填写)")
    private String completed;
}
