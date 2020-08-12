package com.zjft.usp.anyfix.baseinfo.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 自定义字段配置表
 * </p>
 *
 * @author chenxiaod
 * @since 2019-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("custom_field")
@ApiModel(value="CustomField对象", description="自定义字段配置表")
public class CustomField extends Model<CustomField> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "field_id")
    @ApiModelProperty(value = "字段ID")
    private Long fieldId;

    @ApiModelProperty(value = "字段名称")
    private String fieldName;

    @ApiModelProperty(value = "字段类型")
    private Integer fieldType;

    @ApiModelProperty(value = "是否必填")
    private String required;

    @ApiModelProperty(value = "业务表单类型")
    private Integer formType;

    @ApiModelProperty(value = "是否公共")
    private String common;

    @ApiModelProperty(value = "企业编号")
    private Long corpId;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;

}
