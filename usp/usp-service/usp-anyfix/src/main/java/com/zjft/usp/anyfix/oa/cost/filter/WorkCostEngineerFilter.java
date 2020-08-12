package com.zjft.usp.anyfix.oa.cost.filter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: JFZOU
 * @Date: 2020-02-29 15:39
 */
@ApiModel(value = "已完成、已评价工单报销的查询条件参数信息")
@Data
public class WorkCostEngineerFilter {

    @ApiModelProperty(value = "工程师用户名")
    private Long engineer;

    @ApiModelProperty(value = "工单完成状态列表")
    private List<Long> closeStatusList;

    @ApiModelProperty(value = "报销所属月份")
    private String costBelongMonth;
}
