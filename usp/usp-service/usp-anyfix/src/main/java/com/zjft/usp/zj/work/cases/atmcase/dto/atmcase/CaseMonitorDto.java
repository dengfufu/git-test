package com.zjft.usp.zj.work.cases.atmcase.dto.atmcase;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: JFZOU
 * @Date: 2020-04-07 9:27
 * @Version 1.0
 */
@Data
public class CaseMonitorDto {

    @ApiModelProperty(value = "CASE编号")
    private String workCode;
    @ApiModelProperty(value = "监控状态编码")
    private int monitorState;
}
