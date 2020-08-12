package com.zjft.usp.anyfix.oa.cost.filter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: JFZOU
 * @Date: 2020-02-29 14:01
 */

@ApiModel(value = "查询工单报销的查询条件参数信息")
@Data
public class WorkCostFilter {

    @ApiModelProperty(value = "工程师手机号，需要核对手机号两个平台中是否完全一致")
    private String mobile;

    @ApiModelProperty(value = "工程师姓名，需要核对姓名与新平台是否相等或者包含")
    private String userName;

    @ApiModelProperty(value = "报销所属月份")
    private String month;
}
