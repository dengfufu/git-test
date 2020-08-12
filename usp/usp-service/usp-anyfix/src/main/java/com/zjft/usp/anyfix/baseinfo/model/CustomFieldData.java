package com.zjft.usp.anyfix.baseinfo.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 自定义字段数据表
 * </p>
 *
 * @author cxd
 * @since 2020-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("custom_field_data")
@ApiModel(value="CustomFieldData对象", description="自定义字段数据表")
public class CustomFieldData extends Model<CustomFieldData>  implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数据ID")
    private Long dataId;

    @ApiModelProperty(value = "业务表单类型")
    private Integer formType;

    @ApiModelProperty(value = "业务表单ID")
    private Long formId;

    @ApiModelProperty(value = "字段ID")
    private Long fieldId;

    @ApiModelProperty(value = "字段名称")
    private String fieldName;

    @ApiModelProperty(value = "字段类型")
    private Integer fieldType;

    @ApiModelProperty(value = "字段值")
    private String fieldValue;


}
