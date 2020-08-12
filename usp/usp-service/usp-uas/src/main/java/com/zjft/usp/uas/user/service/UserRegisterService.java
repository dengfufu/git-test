package com.zjft.usp.uas.user.service;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.user.dto.UserInfoDto;
import com.zjft.usp.uas.user.dto.UserWxDto;

/**
 * @author zphu
 * @date 2019/8/16 10:14
 * @Version 1.0
 **/
public interface UserRegisterService {


    /**
     * 手机号注册用户
     *
     * @param userInfoDto
     * @param reqParam
     * @return java.lang.String
     * @author zphu
     * @date 2019/9/10 11:20
     * **/
    UserInfo registerUserByMobile(UserInfoDto userInfoDto, ReqParam reqParam, String client);

    /**
     * 获取公钥
     *
     * @return java.lang.String
     * @author zphu
     * @date 2019/8/16 10:23
     * @throws
    **/
    String getPublicKey() ;

    /**
     * 检查手机号是否注册，且用户姓名是否正确
     *
     * @param mobile
     * @return java.lang.Boolean
     * @author zphu
     * @date 2019/8/27 18:26
     * @throws
    **/
    Boolean checkIsRegister(String mobile);

    /**
     * 企业管理员注册用户
     * @param mobile
     * @param userName
     * @return
     */
    Long registryByCorpAdmin(String mobile, String userName);

    /**
     * 企业管理员修改用户
     *
     * @param userId
     * @param userName
     * @return
     * @author zgpi
     * @date 2019/11/13 16:47
     **/
    void updateByCorpAdmin(Long userId, String userName);

    /**
     * 微信注册
     * @param userWxDto
     * @param reqParam
     * @param clientId
     */
    void registerUserByWX(UserWxDto userWxDto, ReqParam reqParam, String clientId);
}
