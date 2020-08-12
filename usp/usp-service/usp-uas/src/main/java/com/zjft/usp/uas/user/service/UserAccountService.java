package com.zjft.usp.uas.user.service;

import com.zjft.usp.common.model.LoginAppUser;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.baseinfo.model.CfgArea;
import com.zjft.usp.uas.user.dto.*;
import com.zjft.usp.uas.user.model.UserAddr;

import java.util.List;

/**
 * @author zphu
 * @date 2019/8/16 9:18
 * @Version 1.0
 **/
public interface UserAccountService {

    /**
     * 通过mobile查询当前登录用户
     *
     * @param mobile
     * @return
     */
    LoginAppUser logonByMobile(String mobile);

    /**
     * 通过mobile查询当前登录用户
     *
     * @param logonId
     * @return
     */
    LoginAppUser logonByLogonId(String logonId);

    LoginAppUser logonByWx(String openId);

    /**
     * 获得用户编号
     *
     * @param mobile
     * @return
     * @author zgpi
     * @date 2019/11/28 11:06
     **/
    Long findUserIdByMobile(String mobile);

    /**
     * 获取用户完整信息
     *
     * @return com.zjft.usp.common.model.User2
     * @throws
     * @author zphu
     * @date 2019/8/16 11:25
     **/
    UserInfoDto getUserInfoDtoById(Long userId);

    /**
     * 查询用户高清头像
     *
     * @param userInfo
     * @return java.lang.String
     * @throws
     * @author zphu
     * @date 2019/8/16 9:37
     **/
    UserInfoDto queryFaceImgBig(UserInfo userInfo);

    /**
     * 修改昵称
     *
     * @param userInfoDto
     * @param userId
     * @param reqParam
     * @param appId
     * @return void
     * @throws
     * @author zphu
     * @date 2019/9/10 15:09
     **/
    void updateNickname(UserInfoDto userInfoDto, Long userId, ReqParam reqParam, String appId);

    /**
     * 修改用户性别
     *
     * @param userInfoDto
     * @param userId
     * @param reqParam
     * @param appId
     * @return void
     * @date 2020/2/17
     */
    void updateSex(UserInfoDto userInfoDto, Long userId, ReqParam reqParam, String appId);

    /**
     * 修改登录名
     *
     * @param userInfoDto
     * @param userId
     * @param reqParam
     * @param appId
     * @return void
     * @throws
     * @author zphu
     * @date 2019/9/10 14:46
     **/
    void updateLoginId(UserInfoDto userInfoDto, Long userId, ReqParam reqParam, String appId);


    void updateUserInfo(UserInfoDto userInfoDto);


    /**
     * 修改手机号
     *
     * @param verifyMessageDto
     * @param userInfo
     * @param reqParam
     * @param appId
     * @return java.lang.String
     * @throws
     * @author zphu
     * @date 2019/9/10 15:13
     **/
    void updateMobile(VerifyMessageDto verifyMessageDto, UserInfo userInfo, ReqParam reqParam, String appId);

    /**
     * 实名认证
     *
     * @param userRealDto
     * @param userInfo
     * @return java.lang.String
     * @throws
     * @author zphu
     * @date 2019/8/16 9:39
     **/
    void idCardCertify(UserRealDto userRealDto, UserInfo userInfo, ReqParam reqParam, String appId);

    /**
     * 更新基本用户表
     *
     * @param userInfoDto 用户信息
     * @param userId      用户Id
     * @return void 无返回值
     * @author zphu
     * @date 2019/8/13 11:16
     **/
    Boolean setUserInfo(UserInfoDto userInfoDto, Long userId);

    /**
     * 更新签名
     *
     * @param newSignature
     * @param userInfo
     * @param reqParam
     * @param appId
     * @author dcyu
     */
    void setSignature(String newSignature, UserInfo userInfo, ReqParam reqParam, String appId);

    /**
     * 更新邮箱地址
     *
     * @param mailAddress
     * @param userInfo
     * @param reqParam
     * @param appId
     * @return void
     * @author dcyu
     */
    void setEmailAddress(String mailAddress, UserInfo userInfo, ReqParam reqParam, String appId);

    /**
     * 获取用户信息根据用户ID
     *
     * @param userId
     * @return 用户信息类
     * @author dcyu
     */
    String getQRCodeInfo(Long userId);

    /**
     * 获取地区列表
     *
     * @return 地区列表
     * @author dcyu
     * @author dcyu
     */
    List<CfgArea> getRegions();

    /**
     * 设置个人地区信息
     *
     * @param dto
     * @author dcyu
     */
    UserRegionDto setUserRegion(UserRegionDto dto, UserInfo userInfo, ReqParam reqParam, String appId);

    /**
     * 获取地址信息
     *
     * @param userId
     * @return
     * @author dcyu
     */
    List<UserAddr> getUserInfoAddresses(String userId);

    /**
     * 上传用户头像
     *
     * @param userInfoDto
     * @return java.lang.Boolean
     * @throws
     * @author zphu
     * @date 2019/8/22 14:35
     **/
    void uploadFaceImage(UserInfoDto userInfoDto, Long userId);


    void changePassword(String oldPassword, String newPassword, ReqParam reqParam, Long curUserId, String clientId);

    void changePasswordBySms(String mobile, String smsCode, String newPassword, ReqParam reqParam, String clientId);
}
