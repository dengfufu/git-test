package com.zjft.usp.anyfix.baseinfo.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 自定义字段数据源表
 * </p>
 *
 * @author cxd
 * @since 2020-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("custom_field_data_source")
@ApiModel(value="CustomFieldDataSource对象", description="自定义字段数据源表")
public class CustomFieldDataSource extends Model<CustomFieldDataSource>  implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数据源id")
    private Long sourceId;

    @ApiModelProperty(value = "自定义字段id")
    private Long fieldId;

    @ApiModelProperty(value = "是否可用")
    private String enabled;

    @ApiModelProperty(value = "数据源值")
    private String sourceValue;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间(先默认当前时间，修改再刷新)")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;

}
