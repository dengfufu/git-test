package com.zjft.usp.wms.flow.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * 流程实例节点处理记录表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("flow_instance_node")
@ApiModel(value="FlowInstanceNode对象", description="流程实例节点处理记录表")
public class FlowInstanceNode implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "流程节点ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "流程实例ID")
    private Long flowInstanceId;

    @ApiModelProperty(value = "流程模板节点ID(为了方便查询，增加此冗余字段)")
    private Long templateNodeId;

    @ApiModelProperty(value = "流程模板节点名称(多余字段)")
    private String templateNodeName;

    @ApiModelProperty(value = "流程节点顺序号")
    private Integer sortNo;

    @ApiModelProperty(value = "流程节点结束类型(实际结束时填写)")
    private Integer completedEndTypeId;

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
