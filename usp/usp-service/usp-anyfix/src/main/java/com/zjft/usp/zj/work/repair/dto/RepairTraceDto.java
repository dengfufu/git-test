package com.zjft.usp.zj.work.repair.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 老平台报修跟踪记录
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-19 09:19
 **/
@Data
public class RepairTraceDto implements Serializable {
    @ApiModelProperty(value = "序号")
    private String id;

    @ApiModelProperty(value = "报修编号")
    private String repairId;

    @ApiModelProperty(value = "跟踪时间")
    private String traceTime;

    @ApiModelProperty(value = "操作类型")
    private String type;

    @ApiModelProperty(value = "具体内容")
    private String content;

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "操作时间")
    private String operateTime;
    
    @ApiModelProperty(value = "操作人名称")
    private String operatorName;
}
