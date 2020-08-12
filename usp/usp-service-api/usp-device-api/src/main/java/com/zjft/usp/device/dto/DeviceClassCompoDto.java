package com.zjft.usp.device.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备分类组合DTO,包含大类，小类主要信息，属性后续可扩展
 * @Author: JFZOU
 * @Date: 2020-03-10 21:19
 * @Version 1.0
 */
@Data
public class DeviceClassCompoDto {

    @ApiModelProperty(value = "设备小类ID")
    private Long smallClassId;
    @ApiModelProperty(value = "设备小类名称")
    private String smallClassName;
    @ApiModelProperty(value = "设备大类ID")
    private Long largeClassId;
    @ApiModelProperty(value = "设备大类名称")
    private String largeClassName;
}
