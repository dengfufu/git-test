package com.zjft.usp.anyfix.atm.appraise.composite;

import com.zjft.usp.anyfix.atm.appraise.dto.EngineerAppraiseDto;
import com.zjft.usp.anyfix.atm.appraise.filter.EngineerAppraiseFilter;

import java.util.List;

/**
 * @Author: JFZOU
 * @Date: 2020-03-09 8:58
 * @Version 1.0
 */
public interface EngineerAppraiseCompoService {

    /**
     * 根据企业ID、考核所属月份查找工单信息用于工程师绩效考核
     * @param engineerAppraiseFilter
     * @return
     */
    List<EngineerAppraiseDto> queryByMonth(EngineerAppraiseFilter engineerAppraiseFilter) ;
}
