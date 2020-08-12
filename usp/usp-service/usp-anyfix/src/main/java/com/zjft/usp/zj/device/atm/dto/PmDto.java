package com.zjft.usp.zj.device.atm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * PM记录DTO类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-28 17:06
 **/
@ApiModel(value = "PM记录DTO类")
@Data
public class PmDto implements Serializable {
    private static final long serialVersionUID = 2316352274395417953L;

    @ApiModelProperty(value = "记录编号")
    private String pmId;

    @ApiModelProperty(value = "保修状态")
    private String warrantyStatus;

    @ApiModelProperty(value = "PM标准")
    private String pmNorm;

    @ApiModelProperty(value = "开始时间")
    private String beginTime;

    @ApiModelProperty(value = "完成时间")
    private String endTime;

    @ApiModelProperty(value = "软件版本")
    private String softVersion;

    @ApiModelProperty(value = "SP软件版本")
    private String spSoftVersion;

    @ApiModelProperty(value = "BV软件版本")
    private String bvSoftVersion;

    @ApiModelProperty(value = "其他软件版本")
    private String otherSoftVersion;

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
