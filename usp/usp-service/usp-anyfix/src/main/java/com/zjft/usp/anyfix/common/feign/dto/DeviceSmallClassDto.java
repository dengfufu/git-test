package com.zjft.usp.anyfix.common.feign.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 设备小类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/15 8:41 上午
 **/
@ApiModel("设备小类")
@Getter
@Setter
public class DeviceSmallClassDto {

    @ApiModelProperty(value = "小类ID")
    private Long id;

    @ApiModelProperty(value = "大类ID")
    private Long largeClassId;

    @ApiModelProperty(value = "大类名称")
    private String largeClassName;

    @ApiModelProperty(value = "小类名称")
    private String name;

    @ApiModelProperty(value = "顺序号")
    private Integer sortNo;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "是否可用，1=可用，2=不可用")
    private String enabled;

    @ApiModelProperty(value = "客户企业ID")
    private Long customCorp;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    private Date operateTime;
}
