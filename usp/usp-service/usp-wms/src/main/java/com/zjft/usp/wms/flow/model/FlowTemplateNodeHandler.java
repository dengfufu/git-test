package com.zjft.usp.wms.flow.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流程模板节点处理人表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("flow_template_node_handler")
@ApiModel(value = "FlowTemplateNodeHandler对象", description = "流程模板节点处理人表")
public class FlowTemplateNodeHandler implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "随机主键ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "流程模板ID(冗多字段，方便查询）")
    private Long templateId;

    @ApiModelProperty(value = "流程模板节点ID")
    private Long templateNodeId;

    @ApiModelProperty(value = "处理人类型(10=发起人20=指定角色30=指定用户)")
    private Integer handlerTypeId;

    @ApiModelProperty(value = "处理人ID(如果是发起人，则为当前用户ID；如果是角色，则为角色ID；如果是指定处理人，则为指定处理人ID)")
    private Long handlerId;

    @ApiModelProperty(value = "是否为主处理人 (其他非会签节点，默认都是主处理人；如果是会签节点则需要手工指定主处理人，主处理人操作才能结束当前节点，Y=是,N=否)")
    private String isMainDirector;


}
