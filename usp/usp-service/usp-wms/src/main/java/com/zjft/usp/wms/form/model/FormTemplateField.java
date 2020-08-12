package com.zjft.usp.wms.form.model;

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
 * 表单模板字段表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("form_template_field")
@ApiModel(value="FormTemplateField对象", description="表单模板字段表")
public class FormTemplateField implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "字段ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "表单模板ID")
    private Long formTemplateId;

    @ApiModelProperty(value = "数据库表名")
    private String tableName;

    @ApiModelProperty(value = "真实的数据库字段编码")
    private String fieldCode;

    @ApiModelProperty(value = "字段分类(10=系统必备字段20=系统预留字段30=用户自定义字段)")
    private Integer fieldClass;

    @ApiModelProperty(value = "字段名称(可修改)")
    private String fieldName;

    @ApiModelProperty(value = "字段类型(10=字符串20=系统列表30=自定义列表40=日期50=数字)")
    private Integer fieldType;

    @ApiModelProperty(value = "字段长度")
    private Integer fieldLength;

    @ApiModelProperty(value = "系统列表ID")
    private Integer sysListId;

    @ApiModelProperty(value = "自定义列表主表ID")
    private Long customListMainId;

    @ApiModelProperty(value = "是否必填 (Y=是,N=否)")
    private String notnull;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "顺序号")
    private Integer sortNo;

    @ApiModelProperty(value = "是否可用 (Y=是,N=否)")
    private String enabled;


}
