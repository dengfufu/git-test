package com.zjft.usp.anyfix.common.feign.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 设备型号
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/15 8:45 上午
 **/
@ApiModel("设备型号")
@Data
public class DeviceModelDto {

    @ApiModelProperty(value = "型号ID")
    private Long id;

    @ApiModelProperty(value = "型号名称")
    private String name;

    @ApiModelProperty(value = "设备品牌")
    private Long brandId;

    @ApiModelProperty(value = "设备小类")
    private Long smallClassId;

    @ApiModelProperty(value = "是否可用，1=可用，2=不可用")
    private String enabled;

    @ApiModelProperty(value = "客户企业ID")
    private Long customCorp;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    private Date operateTime;
}
