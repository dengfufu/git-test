package com.zjft.usp.anyfix.work.sign.filter;

import com.zjft.usp.anyfix.work.sign.model.WorkSign;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Set;

/**
 * @Author: JFZOU
 * @Date: 2020-03-02 9:26
 * @Version 1.0
 */

@ApiModel(value = "工单签到表过滤条件")
@Data
public class WorkSignFilter extends WorkSign {

    /**工单ID列表*/
    private Set<Long> workIdSet;

    /**签到月份*/
    private String signMonth;
}
