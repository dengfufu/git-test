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
 * 流程模板节点表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("flow_template_node")
@ApiModel(value="FlowTemplateNode对象", description="流程模板节点表")
public class FlowTemplateNode implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "流程模板节点ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "流程模板ID")
    private Long templateId;

    @ApiModelProperty(value = "节点名称")
    private String name;

    @ApiModelProperty(value = "节点类型(10=填写节点20=普通审批节点30=会签审批节点40=发货节点50=收货节点60=确认节点)")
    private Integer nodeType;

    @ApiModelProperty(value = "表单基本信息模板ID")
    private Long formMainId;

    @ApiModelProperty(value = "表单明细信息模板ID")
    private Long formDetailId;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "顺序号，顺序号在前端调整，即顺序号在前端为可输入项")
    private Integer sortNo;

    @ApiModelProperty(value = "是否可用 (Y=是,N=否)")
    private String enabled;

    @ApiModelProperty(value = "是否核心节点 (Y=是,N=否,为Y则不能删除)")
    private String isCore;
}
