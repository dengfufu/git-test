package com.zjft.usp.zj.device.atm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备预警信息DTO类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-28 15:39
 **/
@ApiModel(value = "设备预警信息DTO类")
@Data
public class EarlyWarnInfoDto implements Serializable {
    private static final long serialVersionUID = -6795783352206455170L;

    @ApiModelProperty(value = "照片存在情况", notes = "true-已上传，false-未上传")
    private String hasPhoto;

    @ApiModelProperty(value = "上次PM")
    private String lastPm;

    @ApiModelProperty(value = "最新运行日期")
    private String latestStartDate;

    @ApiModelProperty(value = "预设条码确认情况")
    private String defaultBarcodeConfirm;

    @ApiModelProperty(value = "预设二维码确认情况")
    private String defaultBar2StatusConfirm;

    @ApiModelProperty(value = "软件版本")
    private String softVersion;

    @ApiModelProperty(value = "软件最新版本")
    private String softVersionNew;

    @ApiModelProperty(value = "SP软件版本")
    private String spSoftVersion;

    @ApiModelProperty(value = "SP软件最新版本")
    private String spSoftVersionNew;

    @ApiModelProperty(value = "BV软件版本")
    private String bvSoftVersion;

    @ApiModelProperty(value = "BV软件最新版本")
    private String bvSoftVersionNew;

    @ApiModelProperty(value = "其他软件版本")
    private String otherSoftVersion;

}
