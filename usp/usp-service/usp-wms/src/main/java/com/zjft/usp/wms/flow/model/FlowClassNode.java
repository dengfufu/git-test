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
 * 流程类型节点表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("flow_class_node")
@ApiModel(value="FlowClassNode对象", description="流程类型节点表")
public class FlowClassNode implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "随机主键ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "企业ID")
    private Long corpId;

    @ApiModelProperty(value = "业务大类ID")
    private Integer largeClassId;

    @ApiModelProperty(value = "业务小类ID")
    private Integer smallClassId;

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

    @ApiModelProperty(value = "节点顺序号")
    private Integer sortNo;

    @ApiModelProperty(value = "是否核心节点 (Y=是,N=否,为Y则不能删除)")
    private String isCore;


}
