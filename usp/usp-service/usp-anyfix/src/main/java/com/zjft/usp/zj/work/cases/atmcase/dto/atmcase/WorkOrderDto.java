package com.zjft.usp.zj.work.cases.atmcase.dto.atmcase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 派工单DTO类
 *
 * @author zgpi
 * @date 2020/4/7 9:10
 */
@ApiModel(value = "派工单DTO类")
@Data
public class WorkOrderDto {

    @ApiModelProperty(value = "派工单编号")
    private String workOrderId;

    @ApiModelProperty(value = "CASE类型")
    private String workType;

    @ApiModelProperty(value = "CASE子类编号")
    private Integer workSubType;

    @ApiModelProperty(value = "服务站")
    private String serviceBranch;

    @ApiModelProperty(value = "服务站名称")
    private String serviceBranchName;

    @ApiModelProperty(value = "客户编号")
    private String customId;

    @ApiModelProperty(value = "客户名称")
    private String customName;

    @ApiModelProperty(value = "设备网点名称")
    private String deviceBranchName;

    @ApiModelProperty(value = "机器型号编号")
    private String modelId;

    @ApiModelProperty(value = "机器型号")
    private String modelName;

    @ApiModelProperty(value = "终端号")
    private String deviceCode;

    @ApiModelProperty(value = "制造号")
    private String serial;

    @ApiModelProperty(value = "服务请求")
    private String serviceRequest;

    @ApiModelProperty(value = "是否预约")
    private String ifBook;

    @ApiModelProperty(value = "预约时间")
    private String bookTime;

    @ApiModelProperty(value = "当前时间")
    private String currentTime;

    @ApiModelProperty(value = "是否新网点")
    private String isNewBranch;

    @ApiModelProperty(value = "故障时间")
    private String faultTime;

    @ApiModelProperty(value = "报修时间")
    private String reportTime;

}
