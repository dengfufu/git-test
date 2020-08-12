package com.zjft.usp.uas.corp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.corp.dto.CorpVerifyAppDto;
import com.zjft.usp.uas.corp.filter.CorpVerifyAppFilter;
import com.zjft.usp.uas.corp.model.CorpVerifyApp;

import java.util.List;
import java.util.Map;

/**
 * 企业认证申请服务
 *
 * @author canlei
 * @version 1.0
 * @date 2019-08-22 19:00
 **/
public interface CorpVerifyAppService extends IService<CorpVerifyApp> {

    /**
     * 企业认证申请
     * @param corpVerifyAppDto
     * @param reqParam
     * @param curUserId
     */
    void corpVerifyApply(CorpVerifyAppDto corpVerifyAppDto, ReqParam reqParam, Long curUserId,String clientId);

    /**
     * 企业认证审核
     * @param corpVerifyAppDto
     * @param reqParam
     * @param curUserId
     */
    Result corpVerifyCheck(CorpVerifyAppDto corpVerifyAppDto, ReqParam reqParam, Long curUserId,String clientId);

    /**
     * 查询申请中的企业认证
     * @param corpId
     * @return
     */
    CorpVerifyApp selectApplying(Long corpId);

    /**
     * 查询申请中的企业认证
     * @param corpIdList
     * @return
     */
    List<CorpVerifyApp> selectApplyingList(List<Long> corpIdList);

    /**
     * 获取申请中的企业认证映射
     * @param corpIdList
     * @return
     */
    Map<Long, CorpVerifyApp> mapApplying(List<Long> corpIdList);

    /**
     * 分页查询企业认证申请
     * @param corpVerifyAppFilter
     * @return
     */
    ListWrapper<CorpVerifyAppDto> pageByFilter(CorpVerifyAppFilter corpVerifyAppFilter);

}
