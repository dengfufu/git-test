package com.zjft.usp.uas.corp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.uas.corp.dto.CorpVerifyDto;
import com.zjft.usp.uas.corp.model.CorpVerify;
import com.zjft.usp.uas.corp.model.CorpVerifyApp;

import java.util.List;
import java.util.Map;

/**
 * @author canlei
 * @date 2019-08-04
 */
public interface CorpVerifyService extends IService<CorpVerify> {

    /**
     * 根据公司编号获取公司认证信息的MAP
     * @param corpIdList
     * @return boolean
     */
    Map<Long, CorpVerify> mapCorpIdAndVerify(List<Long> corpIdList);

    /**
     * 根据企业认证审核生成企业认证信息
     * @param corpVerifyApp
     * @return
     */
    int createVerifyByCheck(CorpVerifyApp corpVerifyApp);

    /**
     * 添加租户，自动认证
     * @param corpId
     * @return
     */
    int createVerifyByCorpId(Long corpId);

    /**
     * 获取企业认证详细信息
     * @date 2020/6/14
     * @param corpId
     * @param userInfo
     * @return com.zjft.usp.uas.corp.dto.CorpVerifyDto
     */
    CorpVerifyDto queryCorpVerify(Long corpId, UserInfo userInfo);

    /**
     * 更改企业认证信息
     * @date 2020/6/14
     * @param CorpVerifyDto
     * @param user
     * @return void
     */
    void updateCorpVerify(CorpVerifyDto CorpVerifyDto,@LoginUser UserInfo user);
}
