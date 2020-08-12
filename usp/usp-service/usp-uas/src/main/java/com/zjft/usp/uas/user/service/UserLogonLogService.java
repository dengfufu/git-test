package com.zjft.usp.uas.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.user.dto.UserLogonLogDto;
import com.zjft.usp.uas.user.filter.UserLogonLogFilter;
import com.zjft.usp.uas.user.model.UserLogonLog;

/**
 * <p>
 * 用户登录日志表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2020-05-25
 */
public interface UserLogonLogService extends IService<UserLogonLog> {

    /**
     * 根据条件分页查询用户登录日志
     * @date 2020/5/26
     * @param userLogonLogFilter
     * @param userInfo
     * @param reqParam
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.uas.user.dto.UserLogonLogDto>
     */
    ListWrapper<UserLogonLogDto> queryUserLogonLog(UserLogonLogFilter userLogonLogFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 保存登录成功日志
     *
     * @param userLogonLogDto
     * @return
     * @author zgpi
     * @date 2020/5/25 15:12
     **/
    void saveLogonSuccessLog(UserLogonLogDto userLogonLogDto);

    /**
     * 保存自动登录成功日志
     *
     * @param userLogonLogDto
     * @return
     * @author zgpi
     * @date 2020/5/26 15:30
     **/
    void saveAutoLogonSuccessLog(UserLogonLogDto userLogonLogDto);

    /**
     * 保存密码失败日志
     *
     * @param userLogonLogDto
     * @return
     * @author zgpi
     * @date 2020/5/25 15:12
     **/
    void saveLogonPwdFailLog(UserLogonLogDto userLogonLogDto);

    /**
     * 保存登出日志
     *
     * @param userLogonLogDto
     * @return
     * @author zgpi
     * @date 2020/5/25 20:01
     **/
    void saveLogoutLog(UserLogonLogDto userLogonLogDto);
}
