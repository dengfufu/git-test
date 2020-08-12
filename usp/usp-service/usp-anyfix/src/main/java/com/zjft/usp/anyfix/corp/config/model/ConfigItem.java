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
 * 配置项表
 * </p>
 *
 * @author zrlin
 * @since 2020-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("config_item")
@ApiModel(value="ConfigItem对象", description="配置项表")
public class ConfigItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "配置名")
    @TableId
    private Integer id;

    @ApiModelProperty(value = "配置名")
    private String itemName;

    @ApiModelProperty(value = "默认值")
    private String defaultValue;

    @ApiModelProperty(value = "描述")
    private String description;


}
