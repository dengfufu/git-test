package com.zjft.usp.anyfix.work.request.composite;

import com.zjft.usp.anyfix.work.request.dto.CaseDto;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

import java.util.Map;

/**
 * 金融工单请求聚合服务类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-02-25 13:48
 **/
public interface CaseRequestCompoService {
    /**
     * 根据条件分页查询金融设备类工单
     *
     * @author Qiugm
     * @date 2020-02-22
     * @param workFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    ListWrapper<CaseDto> queryAtmCase(WorkFilter workFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 获得单个金融设备类工单详情
     *
     * @author Qiugm
     * @date 2020-02-23
     * @param workFilter
     * @param userInfo
     * @return
     */
    CaseDto findAtmCaseDetail(WorkFilter workFilter, UserInfo userInfo);

    /**
     * 获得下拉数据源
     *
     * @author Qiugm
     * @date 2020-03-02
     * @param workFilter
     * @param userInfo
     * @return
     */
    Map<String, Object> findAtmCaseOption(WorkFilter workFilter, UserInfo userInfo);
}
