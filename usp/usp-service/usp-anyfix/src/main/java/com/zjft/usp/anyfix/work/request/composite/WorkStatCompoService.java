package com.zjft.usp.anyfix.work.request.composite;

import com.zjft.usp.anyfix.work.request.dto.WorkStatAreaDto;
import com.zjft.usp.anyfix.work.request.dto.WorkStatDto;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

import java.util.List;
import java.util.Map;

/**
 * 工单统计接口类
 *
 * @author zgpi
 * @version 1.0
 * @date 2020/2/1620:23
 */
public interface WorkStatCompoService {

    /**
     * 按状态统计工单
     *
     * @param workFilter 查询条件
     * @param userInfo   当前用户
     * @param reqParam   公共参数
     * @return
     * @author zgpi
     * @date 2019/10/23 11:20 上午
     **/
    List<WorkStatDto> countWorkStatus(WorkFilter workFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 按状态统计当前用户的工单
     *
     * @param workFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/2/16 20:21
     */
    List<WorkStatDto> countUserWorkStatus(WorkFilter workFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 统计当前用户的待录入费用工单数
     *
     * @param workFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/5/17 11:56
     **/
    Integer countUserWorkFee(WorkFilter workFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 统计当前用户审核不通过的工单数
     *
     * @param workFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/5/19 10:12
     **/
    Integer countUserReject(WorkFilter workFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 按区域统计工单
     *
     * @param workFilter 查询条件
     * @param userInfo   当前用户
     * @param reqParam   公共参数
     * @return
     * @author zgpi
     * @date 2019/12/10 10:48
     **/
    List<WorkStatAreaDto> countWorkArea(WorkFilter workFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 统计待处理工单
     *
     * @param workFilter 查询条件
     * @param userInfo   当前用户
     * @param reqParam   公共参数
     * @return
     * @author zgpi
     * @date 2019/12/10 11:31
     **/
    Map<String, Object> countWorkDeal(WorkFilter workFilter, UserInfo userInfo, ReqParam reqParam);

}
