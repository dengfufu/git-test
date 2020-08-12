package com.zjft.usp.zj.work.cases.atmcase.filter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: JFZOU
 * @Date: 2020-03-25 14:21
 * @Version 1.0
 */
@ApiModel(value = "ATM机CASE延期查询条件类")
@Data
public class CaseDelayFilter {

    @ApiModelProperty(value = "CASE编号")
    private String workCode;
    @ApiModelProperty(value = "重新预约时间")
    private String reBookTime;

}
