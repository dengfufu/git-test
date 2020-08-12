package com.zjft.usp.anyfix.oa.cost.composite;

import com.zjft.usp.anyfix.oa.cost.dto.WorkCostDto;
import com.zjft.usp.anyfix.oa.cost.filter.WorkCostFilter;

import java.util.List;

/**
 * @Author: JFZOU
 * @Date: 2020-02-29 13:59
 */
public interface WorkCostCompoService {

    /**
     * 根据手机号、报销所属月份查找工单信息用于报销工单交通费用
     * @param workCostFilter
     * @return
     */
    List<WorkCostDto> queryByMobile(WorkCostFilter workCostFilter) ;
}
