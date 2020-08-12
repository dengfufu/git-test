package com.zjft.usp.wms.flow.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.wms.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 流程实例表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("flow_instance")
@ApiModel(value="FlowInstance对象", description="流程实例表")
public class FlowInstance implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "流程实例ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "企业ID")
    private Long corpId;

    @ApiModelProperty(value = "流程模板ID")
    private Long templateId;

    @ApiModelProperty(value = "当前结点ID(冗余字段)")
    private Long currentNodeId;

    @ApiModelProperty(value = "流程是否结束 (Y=是,N=否)")
    private String isEnd;

    @ApiModelProperty(value = "流程结束人")
    private Long endBy;

    @ApiModelProperty(value = "流程结束时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date endTime;

    @ApiModelProperty(value = "创建人")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date createTime;

    @ApiModelProperty(value = "流程实例拆单前的源流程实例ID")
    private Long sourceInstanceId;
}
