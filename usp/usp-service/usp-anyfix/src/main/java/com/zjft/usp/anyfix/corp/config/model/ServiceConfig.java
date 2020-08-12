package com.zjft.usp.anyfix.corp.config.model;

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
 * 委托商服务商关系配置表
 * </p>
 *
 * @author zrlin
 * @since 2020-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("service_config")
@ApiModel(value="ServiceConfig对象", description="委托商服务商关系配置表")
public class ServiceConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "配置关系编号")
    @TableId
    private Long id;

    @ApiModelProperty(value = "配置字段编号，引用定义配置表主键")
    private Integer itemId;

    @ApiModelProperty(value = "配置值")
    private String itemValue;

    @ApiModelProperty(value = "引用类型，1为设置委托商，2为设置当前企业")
    private String type;
}
