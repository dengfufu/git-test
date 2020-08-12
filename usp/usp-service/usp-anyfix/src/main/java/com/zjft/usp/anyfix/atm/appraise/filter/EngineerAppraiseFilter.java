package com.zjft.usp.anyfix.atm.appraise.filter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: JFZOU
 * @Date: 2020-03-09 8:50
 * @Version 1.0
 */
@ApiModel(value = "查询工程师工单查询条件参数信息")
@Data
public class EngineerAppraiseFilter {

    @ApiModelProperty(value = "公司ID，使用oauth2配置表中指定的企业")
    private Long corpId;
    @ApiModelProperty(value = "考核月份")
    private String month;
    @ApiModelProperty(value = "考核具体哪一天")
    private String date;
}
