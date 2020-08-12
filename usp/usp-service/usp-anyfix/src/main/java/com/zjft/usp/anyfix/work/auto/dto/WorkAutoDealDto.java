package com.zjft.usp.anyfix.work.auto.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zgpi
 * @version 1.0
 * @date 2019/11/13 19:14
 */
@ApiModel("工单自动处理")
@Data
public class WorkAutoDealDto {

    @ApiModelProperty(value = "服务商企业")
    private Long serviceCorp;

    @ApiModelProperty(value = "客户企业")
    private Long customCorp;

    @ApiModelProperty(value = "供应商企业")
    private Long demanderCorp;

    @ApiModelProperty(value = "服务网点")
    private Long deviceBranch;

    @ApiModelProperty(value = "服务模式")
    private Integer serviceMode;

    @ApiModelProperty(value = "工单类型")
    private Integer workType;

    @ApiModelProperty(value = "设备大类")
    private Long largeClassId;

    @ApiModelProperty(value = "设备小类")
    private Long smallClassId;

    @ApiModelProperty(value = "设备品牌")
    private Long brandId;

    @ApiModelProperty(value = "设备型号")
    private Long modelId;

    @ApiModelProperty(value = "行政区划")
    private String district;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "设备号")
    private String deviceCode;

    @ApiModelProperty(value = "自动派单模式")
    private Integer autoMode;

    @ApiModelProperty(value = "手动派单模式")
    private Integer manualMode;

}
