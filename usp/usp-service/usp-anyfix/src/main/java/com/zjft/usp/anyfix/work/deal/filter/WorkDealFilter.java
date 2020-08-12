package com.zjft.usp.anyfix.work.deal.filter;

import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: JFZOU
 * @Date: 2020-03-02 8:18
 * @Version 1.0
 */

@ApiModel(value = "工单处理信息表过滤条件")
@Data
public class WorkDealFilter extends WorkDeal {


    @ApiModelProperty(value = "工单状态列表")
    private List<Integer> workStatusList;

    @ApiModelProperty(value = "服务完成时间所属月份")
    private String finishMonth;
}
