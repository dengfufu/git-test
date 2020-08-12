package com.zjft.usp.anyfix.corp.config.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("配置项Dto")
public class ServiceConfigDto {

    @ApiModelProperty(value = "配置编号")
    private Integer itemId;
    @ApiModelProperty(value = "配置名")
    private String itemName;
    @ApiModelProperty(value = "默认值")
    private String defaultValue;
    @ApiModelProperty(value = "配置值")
    private String itemValue;
    @ApiModelProperty(value = "描述")
    private String description;
}
