package com.zjft.usp.zj.device.atm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 升级记录DTO类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-28 17:07
 **/
@ApiModel(value = "升级记录DTO类")
@Data
public class UpdateRecordDto implements Serializable {

    private static final long serialVersionUID = -2254894703011350282L;

    @ApiModelProperty(value = "记录编号")
    private String recordId;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "更新来源")
    private String updateType;

    @ApiModelProperty(value = "软件版本")
    private String softVersion;

    @ApiModelProperty(value = "SP软件版本")
    private String spSoftVersion;

    @ApiModelProperty(value = "BV软件版本")
    private String bvSoftVersion;

    @ApiModelProperty(value = "其他软件版本")
    private String otherSoftVersion;

}
