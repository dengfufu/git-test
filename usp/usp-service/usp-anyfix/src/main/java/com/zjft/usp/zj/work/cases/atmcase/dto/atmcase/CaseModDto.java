package com.zjft.usp.zj.work.cases.atmcase.dto.atmcase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 修改CASEDto
 *
 * @author canlei
 * @version 1.0
 * @date 2020-03-25 15:51
 **/
@ApiModel(value = "CASE修改Dto")
@Data
public class CaseModDto implements Serializable {

    private static final long serialVersionUID = 6029194163601596910L;

    @ApiModelProperty(value = "case编号")
    private String workCode;

    @ApiModelProperty(value = "标记手机修改")
    private String createByApp;

    @ApiModelProperty(value = "是否需要巡检")
    private String inspectionRequired;

    @ApiModelProperty(value = "case类型")
    private String workTypeName;

    @ApiModelProperty(value = "case子类")
    private String workSubType;

    @ApiModelProperty(value = "服务网点")
    private String serviceBranch;

    @ApiModelProperty(value = "服务网点名称")
    private String serviceBranchName;

    @ApiModelProperty(value = "客户编号")
    private String customId;

    @ApiModelProperty(value = "客户名称")
    private String customName;

    @ApiModelProperty(value = "设备网点编号")
    private String deviceBranch;

    @ApiModelProperty(value = "设备网点名称")
    private String deviceBranchName;

    @ApiModelProperty(value = "保修情况")
    private String warrantyName;

    @ApiModelProperty(value = "机器型号")
    private String modelId;

    @ApiModelProperty(value = "机器型号名称")
    private String modelName;

    @ApiModelProperty(value = "制造号")
    private String serial;

    @ApiModelProperty(value = "终端号")
    private String deviceCode;

    @ApiModelProperty(value = "新终端号")
    private String newDeviceCode;

    @ApiModelProperty(value = "故障时间")
    private String faultTime;

    @ApiModelProperty(value = "保修时间")
    private String reportTime;

    @ApiModelProperty(value = "交通工具")
    private String traffic;

    @ApiModelProperty(value = "交通工具名称")
    private String trafficName;

    @ApiModelProperty(value = "交通说明")
    private String trafficNote;

    @ApiModelProperty(value = "预计出发时间")
    private String planGoTime;

    @ApiModelProperty(value = "预计到达时间")
    private String planArriveTime;

    @ApiModelProperty(value = "实际出发时间")
    private String goTime;

    @ApiModelProperty(value = "实际到达时间")
    private String signTime;

    @ApiModelProperty(value = "有无软件升级")
    private String softUpdate;

    @ApiModelProperty(value = "软件版本")
    private String softVersion;

    @ApiModelProperty(value = "SP版本")
    private String spSoftVersion;

    @ApiModelProperty(value = "BV版本")
    private String bvSoftVersion;

    @ApiModelProperty(value = "其他软件版本")
    private String otherSoftVersion;

    @ApiModelProperty(value = "工程师")
    private String engineers;

    @ApiModelProperty(value = "工程师姓名")
    private String engineerNames;

    @ApiModelProperty(value = "报修内容")
    private String serviceRequest;

    @ApiModelProperty(value = "修改备注")
    private String modNote;

    @ApiModelProperty(value = "修改时间")
    private String modTime;

}
