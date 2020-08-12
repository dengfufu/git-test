package com.zjft.usp.zj.work.cases.atmcase.composite;

import com.zjft.usp.anyfix.work.request.dto.WorkStatDto;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.work.cases.atmcase.filter.AtmCaseFilter;

import java.util.List;

/**
 * ATM机CASE统计聚合接口
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-31 14:46
 **/
public interface AtmCaseStatCompoService {

    /**
     * 统计各个状态的工单数量
     *
     * @author Qiugm
     * @date 2020-03-31
     * @param atmCaseFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    List<WorkStatDto> countCaseStatus(AtmCaseFilter atmCaseFilter, UserInfo userInfo, ReqParam reqParam);
}
