package com.zjft.usp.zj.work.repair.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 工行对接故障模块信息
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-18 15:47
 **/
@Data
public class BxReceiveBugDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "交易序号")
    private String tranID;
    @ApiModelProperty(value = "顺序号，从0开始")
    private int sortno;
    @ApiModelProperty(value = "模块/事件（故障模块）")
    private String moduleEvent;
    @ApiModelProperty(value = "模块信息类型 1 报障 2 实际故障 5 巡检")
    private String moduleType;
    @ApiModelProperty(value = "故障类型")
    private String bugType;
    @ApiModelProperty(value = "故障代码")
    private String bugCode;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "设备小类和模块/事件")
    private String smallClassAndModuleEvent;
    @ApiModelProperty(value = "设备小类和模块/事件对应名称")
    private String smallClassAndModuleEventName;
    @ApiModelProperty(value = "模块信息类型对应名称")
    private String moduleTypeName;
}
