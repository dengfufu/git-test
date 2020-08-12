package com.zjft.usp.zj.device.atm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 维护记录DTO类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-28 17:06
 **/
@ApiModel(value = "维护记录DTO类")
@Data
public class MaintainDto implements Serializable {
    private static final long serialVersionUID = -2139097327779504644L;

    @ApiModelProperty(value = "记录编号")
    private String recordId;

    @ApiModelProperty(value = "故障日期")
    private String faultDate;

    @ApiModelProperty(value = "保修状态")
    private String warrantyStatus;

    @ApiModelProperty(value = "报修时间")
    private String reportDatetime;

    @ApiModelProperty(value = "现场时间")
    private String localeDateTime;

    @ApiModelProperty(value = "修复时间")
    private String repairDateTime;

    @ApiModelProperty(value = "故障类型")
    private String faultType;

    @ApiModelProperty(value = "故障模块")
    private String faultModeName;

    @ApiModelProperty(value = "故障代码")
    private String faultCode;

    @ApiModelProperty(value = "故障描述")
    private String faultDesc;

    @ApiModelProperty(value = "软件版本")
    private String softVersion;

    @ApiModelProperty(value = "SP软件版本")
    private String spSoftVersion;

    @ApiModelProperty(value = "BV软件版本")
    private String bvSoftVersion;

    @ApiModelProperty(value = "其他软件版本")
    private String otherSoftVersion;

    @ApiModelProperty(value = "纸币环流功能状态")
    private String moneyCirculate;

    @ApiModelProperty(value = "处理步骤")
    private String processStep;

    @ApiModelProperty(value = "处理结果")
    private String processResult;

    @ApiModelProperty(value = "工程师名称")
    private String engineerName;

    @ApiModelProperty(value = "工程师签名日期")
    private String engineerSignDate;

    @ApiModelProperty(value = "用户意见")
    private String userComment;

    @ApiModelProperty(value = "用户签名")
    private String userSign;

    @ApiModelProperty(value = "用户签名日期")
    private String userSignDate;

    @ApiModelProperty(value = "联系电话")
    private String userTel;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "备件更换数量")
    private int partNum;
}
