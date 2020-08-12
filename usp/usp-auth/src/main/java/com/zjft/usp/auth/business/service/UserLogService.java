package com.zjft.usp.auth.business.service;

/**
 * 用户操作日志
 *
 * @author zgpi
 * @date 2020/5/25 14:35
 */
public interface UserLogService {

    /**
     * 保存登录成功日志
     *
     * @param userId
     * @param mobile
     * @param extraParams
     * @return
     * @author zgpi
     * @date 2020/5/25 14:37
     **/
    void saveLogonSuccess(Long userId, String mobile, String extraParams);

    /**
     * 保存自动登录成功日志
     *
     * @param userId
     * @param mobile
     * @param extraParams
     * @return
     * @author zgpi
     * @date 2020/5/26 15:23
     **/
    void saveAutoLogonSuccess(Long userId, String mobile, String extraParams);

    /**
     * 保存登录密码错误日志
     *
     * @param userId
     * @param mobile
     * @param extraParams
     * @return
     * @author zgpi
     * @date 2020/5/25 14:38
     **/
    void saveLogonPwdFail(Long userId, String mobile, String extraParams);

    /**
     * 保存登出日志
     *
     * @param userId
     * @param mobile
     * @param extraParams
     * @return
     * @author zgpi
     * @date 2020/5/26 15:22
     **/
    void saveLogoutLog(Long userId, String mobile, String extraParams);

}
