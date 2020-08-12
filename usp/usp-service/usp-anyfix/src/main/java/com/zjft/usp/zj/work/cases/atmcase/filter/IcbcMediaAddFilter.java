package com.zjft.usp.zj.work.cases.atmcase.filter;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: JFZOU
 * @Date: 2020-04-01 8:57
 * @Version 1.0
 */
@Data
public class IcbcMediaAddFilter {
    @ApiModelProperty(value = "CASE编号")
    private String workCode;
    @ApiModelProperty(value = "是否工行模式")
    private String bankModeFlag;
}
