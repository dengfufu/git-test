package com.zjft.usp.uas.corp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.corp.dto.CorpUserAppDto;
import com.zjft.usp.uas.corp.filter.CorpUserAppFilter;
import com.zjft.usp.uas.corp.model.CorpUserApp;

/**
 * TODO
 *
 * @author canlei
 * @version 1.0
 * @date 2019-09-29 09:47
 **/
public interface CorpUserAppService extends IService<CorpUserApp> {

    /**
     * 根据查询条件查询加入企业申请
     * @param corpUserAppFilter
     * @param reqParam
     * @return
     */
    ListWrapper<CorpUserAppDto> pageCorpUserApp(CorpUserAppFilter corpUserAppFilter, ReqParam reqParam);

    /**
     * 加入企业申请
     * @param corpUserAppDto
     * @param reqParam
     * @param curUserId
     * @return
     */
    void joinCorpApply(CorpUserAppDto corpUserAppDto, ReqParam reqParam, Long curUserId,String clientId);

    void corpUserJoinListener(long userId, long corpId);

    /**
     * 加入企业审核
     * @param corpUserAppDto
     * @param reqParam
     * @param curUserId
     */
    void joinCorpCheck(CorpUserAppDto corpUserAppDto, ReqParam reqParam, Long curUserId,String clientId);

    /**
     * 用户是否已申请加入企业
     * @param userId
     * @param corpId
     * @return
     */
    boolean ifUserHasApply(long userId, long corpId);

    /**
     * 查询申请中的记录
     * @param userId
     * @param corpId
     * @return
     */
    CorpUserApp getApplyingApp(Long userId, Long corpId);

}
