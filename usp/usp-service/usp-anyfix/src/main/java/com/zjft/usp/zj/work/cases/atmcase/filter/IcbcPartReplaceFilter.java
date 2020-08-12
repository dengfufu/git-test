package com.zjft.usp.zj.work.cases.atmcase.filter;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: JFZOU
 * @Date: 2020-04-01 8:36
 * @Version 1.0
 */
@Data
public class IcbcPartReplaceFilter {

    @ApiModelProperty(value = "CASE编号")
    private String caseId;
    @ApiModelProperty(value = "更换ID")
    private Long replaceId;
    @ApiModelProperty(value = "机器型号ID")
    private String deviceType;
}
