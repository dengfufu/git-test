package com.zjft.usp.wms.flow.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.wms.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流程实例处理过程表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("flow_instance_trace")
@ApiModel(value = "FlowInstanceTrace对象", description = "流程实例处理过程表")
public class FlowInstanceTrace implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "随机主键ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "流程实例ID")
    private Long flowInstanceId;

    @ApiModelProperty(value = "当前结点ID(冗余字段)")
    private Long currentNodeId;

    @ApiModelProperty(value = "处理结论中文名称")
    private String dealResultName;

    @ApiModelProperty(value = "处理描述")
    private String completedDescription;

    @ApiModelProperty(value = "处理人")
    private Long completedBy;

    @ApiModelProperty(value = "处理时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date completedTime;

}
