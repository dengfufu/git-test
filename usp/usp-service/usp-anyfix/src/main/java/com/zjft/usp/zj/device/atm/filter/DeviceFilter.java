package com.zjft.usp.zj.device.atm.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备filter类
 *
 * @author zgpi
 * @date 2020/3/25 10:55
 */
@ApiModel(value = "设备filter类")
@Data
public class DeviceFilter extends Page {

    @ApiModelProperty(value = "机器型号")
    private String deviceModel;

    @ApiModelProperty(value = "制造号")
    private String serial;

    @ApiModelProperty(value = "终端号")
    private String deviceCode;

    @ApiModelProperty(value = "模糊查询", notes = "机器型号/制造号/终端号")
    private String matchName;

    @ApiModelProperty(value = "设备网点")
    private String deviceBranch;

    @ApiModelProperty(value = "客户编号")
    private String customId;

    @ApiModelProperty(value = "设备网点名称")
    private String deviceBranchName;

    @ApiModelProperty(value = "记录编号")
    private String recordId;

    @ApiModelProperty(value = "照明灯管情况")
    private String lightingLamp;
}
