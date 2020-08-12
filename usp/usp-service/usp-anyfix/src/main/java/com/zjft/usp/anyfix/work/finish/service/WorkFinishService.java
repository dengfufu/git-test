package com.zjft.usp.anyfix.work.finish.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.finish.dto.WorkFinishDto;
import com.zjft.usp.anyfix.work.finish.model.WorkFinish;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.anyfix.work.request.dto.WorkRequestDto;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工单服务完成表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-10-14
 */
public interface WorkFinishService extends IService<WorkFinish> {

    /**
     * 工程师现场服务工单
     *
     * @author zgpi
     * @date 2019/10/14 1:55 下午
     * @param workFinishDto
     * @param userInfo
     * @param reqParam
     * @return
     **/
    void localeServiceWork(WorkFinishDto workFinishDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 工程师远程服务工单
     *
     * @author zgpi
     * @date 2019/10/14 1:55 下午
     * @param workFinishDto
     * @param userInfo
     * @param reqParam
     * @return
     **/
    void remoteServiceWork(WorkFinishDto workFinishDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 修改现场服务完成信息
     *
     * @param workFinishDto
     * @param userInfo
     * @param reqParam
     */
    void updateLocaleFinish(WorkFinishDto workFinishDto, UserInfo userInfo, ReqParam reqParam);

    void setCustomFieldDateList(WorkFinishDto workFinishDto);

    void updateRomoteWorkFee(WorkFinishDto workFinishDto, UserInfo userInfo, WorkDto workDto);

    void supplementWork(WorkDto workDto, WorkFinishDto workFinishDto, WorkRequestDto workRequestDto, WorkDeal workDeal, UserInfo userInfo, ReqParam reqParam);

    void addWorkOperateBySupplementWork(WorkRequestDto workRequestDto, Integer serviceMode, UserInfo userInfo, ReqParam reqParam);

    Map<Long,WorkFinish> mapWorkIdAndWorkFinish(List<Long> workIdList);
}
