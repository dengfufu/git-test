package com.zjft.usp.device.baseinfo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zgpi
 * @version 1.0
 * @date 2019/12/4 18:50
 */
@ApiModel("设备分类")
@Data
public class DeviceClassDto {

    @ApiModelProperty(value = "企业编号")
    private Long corpId;

    /**
     * 委托商名称
     */
    private String corpName;
}
