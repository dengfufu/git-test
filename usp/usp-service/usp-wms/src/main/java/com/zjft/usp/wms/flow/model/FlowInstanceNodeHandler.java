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
 * 流程实例节点处理人表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("flow_instance_node_handler")
@ApiModel(value = "FlowInstanceNodeHandler对象", description = "流程实例节点处理人表")
public class FlowInstanceNodeHandler implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "节点处理人ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "流程节点ID")
    private Long instanceNodeId;

    @ApiModelProperty(value = "是否主要处理人(Y=是,N=否，此主要处理人用于控制流程节点流转)")
    private String mainDirector;

    @ApiModelProperty(value = "流程节点结束类型 (is_main_director='Y'，专员操作项，专员保存时即可填写)")
    private Integer mainDirectorEndTypeId;

    @ApiModelProperty(value = "是否通过(Y=是,N=否 会签节点非专员操作项，保存时即可填写)")
    private String countersignPassed;

    @ApiModelProperty(value = "安排处理人ID")
    private Long assignedToBy;

    @ApiModelProperty(value = "实际处理描述(保存时即可填写)")
    private String doDescription;

    @ApiModelProperty(value = "实际处理人ID(保存时即可填写)")
    private Long doBy;

    @ApiModelProperty(value = "实际处理时间(保存时即可填写)")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date doTime;

    @ApiModelProperty(value = "是否已提交 (Y=是,N=否,保存时即可填写)")
    private String isSubmit;


}
