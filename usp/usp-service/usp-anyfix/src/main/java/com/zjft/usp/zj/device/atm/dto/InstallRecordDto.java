package com.zjft.usp.zj.device.atm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 安装记录DTO类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-28 17:04
 **/
@ApiModel(value = "安装记录DTO类")
@Data
public class InstallRecordDto implements Serializable {
    private static final long serialVersionUID = -8569202611389312938L;

    @ApiModelProperty(value = "记录编号")
    private String recordId;

    @ApiModelProperty(value = "初验日期")
    private String initialDate;

    @ApiModelProperty(value = "安装日期")
    private String installDate;

    @ApiModelProperty(value = "网点名称")
    private String branchName;

    @ApiModelProperty(value = "网点地址")
    private String branchAddress;

    @ApiModelProperty(value = "网点编号")
    private String branchCode;

    @ApiModelProperty(value = "分行编号")
    private String zone;

    @ApiModelProperty(value = "子网掩码")
    private String netmask;

    @ApiModelProperty(value = "柜员机IP")
    private String atmIp;

    @ApiModelProperty(value = "前置机IP")
    private String hostIp;

    @ApiModelProperty(value = "网关IP")
    private String gateway;

    @ApiModelProperty(value = "前置机端口")
    private String hostPort;

    @ApiModelProperty(value = "ATM端口")
    private String atmPort;

    @ApiModelProperty(value = "安装结果")
    private String installResult;

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

}
