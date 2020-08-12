package com.zjft.usp.anyfix.work.finish.composite;

import com.zjft.usp.anyfix.work.finish.dto.WorkFinishDto;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

import java.util.List;

/**
 * 工单完成聚合服务类
 *
 * @author zgpi
 * @version 1.0
 * @date 2020/1/21 14:55
 */
public interface WorkFinishCompoService {

    /**
     * 工程师现场服务工单
     *
     * @param workFinishDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/1/21 14:57
     **/
    void localeServiceWork(WorkFinishDto workFinishDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 工程师补单
     *
     * @param workDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    List<Long> supplementWork(WorkDto workDto, UserInfo userInfo, ReqParam reqParam);


    /**
     * 工程师补单
     * @param workDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    List<Long> supplementCreateWork(WorkDto workDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 工程师修改现场服务完成信息
     *
     * @param workFinishDto
     * @param userInfo
     * @param reqParam
     */
    void updateLocaleFinish(WorkFinishDto workFinishDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 修改工单费用
     *
     * @param workFinishDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/5/17 10:39
     **/
    void updateWorkFee(WorkFinishDto workFinishDto, Long curUserId, Long curCorpId);

    /**
     * 修改工单服务完成信息
     *
     * @param workDto
     * @param userInfo
     * @param reqParam
     * @return java.lang.Long
     * @date 2020/3/31
     */
    void modFinish(WorkDto workDto, String type, UserInfo userInfo, ReqParam reqParam);

    /**
     * 补充附件
     * @param workFinishDto
     * @param userInfo
     * @param reqParam
     */
    void supplementFiles(WorkFinishDto workFinishDto, UserInfo userInfo, ReqParam reqParam);
}
