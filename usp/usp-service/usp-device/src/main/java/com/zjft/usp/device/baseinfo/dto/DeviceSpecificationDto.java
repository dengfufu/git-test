package com.zjft.usp.device.baseinfo.dto;

import com.zjft.usp.device.baseinfo.model.DeviceSpecification;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备规格Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2020-04-23 15:43
 **/
@Data
@ApiModel("设备规格Dto")
public class DeviceSpecificationDto extends DeviceSpecification {

    @ApiModelProperty("设备类型名称")
    private String smallClassName;

}
