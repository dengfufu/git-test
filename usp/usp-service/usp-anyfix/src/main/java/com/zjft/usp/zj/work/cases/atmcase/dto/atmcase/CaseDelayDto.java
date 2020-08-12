package com.zjft.usp.zj.work.cases.atmcase.dto.atmcase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: JFZOU
 * @Date: 2020-03-25 14:21
 * @Version 1.0
 */
@ApiModel(value = "ATM机CASE延期页面对象类")
@Data
public class CaseDelayDto {

    @ApiModelProperty(value = "CASE编号")
    private String workCode;
    @ApiModelProperty(value = "CASE类型")
    private String workTypeName;
    @ApiModelProperty(value = "工单类型")
    private String appType;
    @ApiModelProperty(value = "银行名称")
    private String customName;
    @ApiModelProperty(value = "网点名称")
    private String deviceBranchName;
    @ApiModelProperty(value = "服务站名称")
    private String serviceBranchName;
    @ApiModelProperty(value = "厂商")
    private String brandName;
    @ApiModelProperty(value = "机器型号名称")
    private String modelName;
    @ApiModelProperty(value = "机器制造号")
    private String serials;
    @ApiModelProperty(value = "当前日期")
    private String today;
    @ApiModelProperty(value = "预计出发时间")
    private String planGoTime;
    @ApiModelProperty(value = "预计到达时间")
    private String planArriveTime;
    @ApiModelProperty(value = "预计完成时间")
    private String planEndTime;
    @ApiModelProperty(value = "实际出发时间")
    private String goTime;
    @ApiModelProperty(value = "实际到达时间")
    private String signTime;
    @ApiModelProperty(value = "实际完成时间")
    private String endTime;
    @ApiModelProperty(value = "是否手机修改")
    private String isMobile;
    @ApiModelProperty(value = "重新预约时间")
    private String reBookTime;
    @ApiModelProperty(value = "预计完成时间")
    private String newPlanEndTime;
    @ApiModelProperty(value = "实际出发时间")
    private String newGoTime;
    @ApiModelProperty(value = "实际到达时间")
    private String newSignTime;
    @ApiModelProperty(value = "报修内容")
    private String serviceRequest;
    @ApiModelProperty(value = "具体内容")
    private String traceDesc;
    @ApiModelProperty(value = "是否在业务跟踪信息显示")
    private String isDisplay;
    @ApiModelProperty(value = "延期情况")
    private String isDelay;
    @ApiModelProperty(value = "CASE跟踪日志列表")
    private List<CaseTraceDto> caseTraceDtoList;
    @ApiModelProperty(value = "前端隐藏字段，原预计完成日期")
    private String oldywdate;
    @ApiModelProperty(value = "前端隐藏字段，原预计完成时间")
    private String oldywdatetime;
    @ApiModelProperty(value = "前端隐藏字段，原实际到达时间")
    private String oldsddatetime;
}
