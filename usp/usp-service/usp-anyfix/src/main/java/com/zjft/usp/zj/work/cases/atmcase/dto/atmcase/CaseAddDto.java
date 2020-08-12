package com.zjft.usp.zj.work.cases.atmcase.dto.atmcase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 新建ATM机CASE DTO类
 *
 * @author zgpi
 * @date 2020/3/24 15:42
 */
@ApiModel(value = "新建ATM机CASE DTO类")
@Data
public class CaseAddDto implements Serializable {

    private static final long serialVersionUID = -5050390470871926664L;

    @ApiModelProperty(value = "CASE编号")
    private String workCode;

    @ApiModelProperty(value = "派工单编号")
    private String workOrderId;

    @ApiModelProperty(value = "CASE类型")
    private String workType;

    @ApiModelProperty(value = "CASE子类编号")
    private Integer workSubType;

    @ApiModelProperty(value = "工单状态")
    private String workStatus;

    @ApiModelProperty(value = "机器型号编号")
    private String modelId;

    @ApiModelProperty(value = "机器型号")
    private String modelName;

    @ApiModelProperty(value = "服务请求")
    private String serviceRequest;

    @ApiModelProperty(value = "交通工具编号")
    private String traffic;

    @ApiModelProperty(value = "交通工具说明")
    private String trafficNote;

    @ApiModelProperty(value = "客户编号，即银行编号")
    private String customId;

    @ApiModelProperty(value = "客户名称，即银行名称")
    private String customName;

    @ApiModelProperty(value = "设备网点编号")
    private String deviceBranch;

    @ApiModelProperty(value = "设备网点名称")
    private String deviceBranchName;

    @ApiModelProperty(value = "服务商网点编号")
    private String serviceBranch;

    @ApiModelProperty(value = "服务商网点名称")
    private String serviceBranchName;

    @ApiModelProperty(value = "是否预约")
    private String ifBook;

    @ApiModelProperty(value = "预约时间")
    private String bookTime;

    @ApiModelProperty(value = "设备编号")
    private String deviceCodes;

    @ApiModelProperty(value = "出厂序列号")
    private String serials;

    @ApiModelProperty(value = "保修状态名称")
    private String warrantyName;

    @ApiModelProperty(value = "故障时间")
    private String faultTime;

    @ApiModelProperty(value = "报修时间")
    private String reportTime;

    @ApiModelProperty(value = "创建人ID")
    private String creator;

    @ApiModelProperty(value = "创建人姓名")
    private String creatorName;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "预计出发时间")
    private String planGoTime;

    @ApiModelProperty(value = "预计到达时间")
    private String planArriveTime;

    @ApiModelProperty(value = "实际到达时间")
    private String actualArriveTime;

    @ApiModelProperty(value = "服务工程师ID拼接，以英文逗号分隔")
    private String engineers;

    @ApiModelProperty(value = "服务工程师姓名拼接，以英文逗号分隔")
    private String engineerNames;

    @ApiModelProperty(value = "是否APP创建，对应ISMOBILE")
    private String createByApp;

    @ApiModelProperty(value = "是否有软件升级", notes = "Y或N")
    private String softUpdate;

    @ApiModelProperty(value = "软件版本")
    private String softVersion;

    @ApiModelProperty(value = "SP软件版本")
    private String spSoftVersion;

    @ApiModelProperty(value = "BV软件版本")
    private String bvSoftVersion;

    @ApiModelProperty(value = "其他软件版本")
    private String otherSoftVersion;

    @ApiModelProperty(value = "当前时间")
    private String currentTime;

    @ApiModelProperty(value = "是否新网点")
    private String isNewBranch;

    @ApiModelProperty(value = "是否同时做巡检")
    private String inspectionRequired;

}
