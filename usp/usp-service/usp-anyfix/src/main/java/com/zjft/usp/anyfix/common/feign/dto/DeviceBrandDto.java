package com.zjft.usp.anyfix.common.feign.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 设备品牌
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/15 8:43 上午
 **/
@ApiModel("设备品牌")
@Data
public class DeviceBrandDto {

    @ApiModelProperty(value = "品牌ID")
    private Long id;

    @ApiModelProperty(value = "品牌logo")
    private Long logo;

    @ApiModelProperty(value = "品牌名称")
    private String name;

    @ApiModelProperty(value = "品牌简称")
    private String shortName;

    @ApiModelProperty(value = "网址")
    private String website;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "是否可用，1=是 2=否")
    private String enabled;

    @ApiModelProperty(value = "客户企业ID")
    private Long customCorp;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    private Date operateTime;
}
