package com.zjft.usp.zj.device.atm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 维护PM记录DTO类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-28 17:06
 **/
@ApiModel(value = "维护PM记录DTO类")
@Data
public class MaintainPmDto implements Serializable {

    private static final long serialVersionUID = -4281178867721295398L;

    @ApiModelProperty(value = "记录编号")
    private String recordId;

    @ApiModelProperty(value = "维修类型", notes = "maintain-维护，pm-PM")
    private String type;

    @ApiModelProperty(value = "记录时间")
    private String time;

    @ApiModelProperty(value = "故障描述")
    private String faultDesc;
}
