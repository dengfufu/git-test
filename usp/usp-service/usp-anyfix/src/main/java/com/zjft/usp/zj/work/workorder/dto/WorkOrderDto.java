package com.zjft.usp.zj.work.workorder.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 老平台派工单信息
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-18 15:21
 **/
@Data
public class WorkOrderDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "派工单号")
    private long workOrderId;
    @ApiModelProperty(value = "派工单状态")
    private int workStatus;
    @ApiModelProperty(value = "服务站编码")
    private String bureauCode;
    @ApiModelProperty(value = "客户编码")
    private String bankCode;
    @ApiModelProperty(value = "网点名称")
    private String branchName;
    @ApiModelProperty(value = "执行人ID")
    private String executor;
    @ApiModelProperty(value = "任务描述")
    private String taskNote;
    @ApiModelProperty(value = "派遣人ID")
    private String sender;
    @ApiModelProperty(value = "sendTime")
    private Timestamp sendTime;
    @ApiModelProperty(value = "接受时间")
    private Timestamp acceptTime;
    @ApiModelProperty(value = "不接受理由")
    private String refuseNote;
    @ApiModelProperty(value = "完工时间")
    private Timestamp finishTime;
    @ApiModelProperty(value = "是否预约标志")
    private String isReserve;
    @ApiModelProperty(value = "预约日期")
    private String reserveTime;
    @ApiModelProperty(value = "预约时间")
    private String reserveDate;
    @ApiModelProperty(value = "预约日期时间")
    private String reserveDateTime;
    @ApiModelProperty("时间戳，用于控制并发")
    private long modTime;
    @ApiModelProperty(value = "重新预约说明")
    private String reNewPreNote;

    @ApiModelProperty(value = "银行名称")
    private String bankName;
    @ApiModelProperty(value = "服务站名称")
    private String bureauName;
    @ApiModelProperty(value = "执行人名称")
    private String executorName;
    @ApiModelProperty(value = "派遣人名称")
    private String senderName;
    @ApiModelProperty(value = "行方标识【1=工行】")
    private String serviceId;
    @ApiModelProperty(value = "OME工单号【工单在OME应用的唯一标识】")
    private String appNo;
    @ApiModelProperty(value = " 报修单号")
    private String faultRepairId;
    @ApiModelProperty(value = "工单类型")
    private String appType;
    @ApiModelProperty(value = "工单类型名称")
    private String appTypeName;
    @ApiModelProperty(value = "终端号")
    private String atmCode;
    @ApiModelProperty(value = "服务类型【录服务单时，此字段必输。参考OME服务单类型表】")
    private String serType;
    @ApiModelProperty(value = "服务类型名称")
    private String serTypeName;
    @ApiModelProperty(value = "紫金工单状态")
    private String bxAppStatus;
    @ApiModelProperty(value = "紫金工单状态名称")
    private String bxAppStatusName;
    @ApiModelProperty(value = "1=工行模式")
    private String bankMode;
    @ApiModelProperty(value = "设备序列号")
    private String deviceCode;
    @ApiModelProperty(value = "产品类型")
    private long productTypeId;
    @ApiModelProperty(value = "委托方编号")
    private String vendorId;
    @ApiModelProperty("产品类型名称")
    private String productTypeName;
    @ApiModelProperty(value = "委托方")
    private String vendorName;
}
