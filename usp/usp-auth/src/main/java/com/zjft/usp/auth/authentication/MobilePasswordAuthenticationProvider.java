package com.zjft.usp.auth.authentication;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.zjft.usp.auth.business.enums.LogonTypeEnum;
import com.zjft.usp.auth.business.service.LockAccountService;
import com.zjft.usp.auth.business.service.UserLogService;
import com.zjft.usp.auth.business.service.UspUserDetailsService;
import com.zjft.usp.common.model.LoginAppUser;
import com.zjft.usp.common.oauth2.token.MobilePasswordAuthenticationToken;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自定义手机认证,手机号登录校验逻辑
 * 拓展spring security 用户名密码模式
 *
 * @author CK
 * @date 2019-08-02 14:10
 */
@Slf4j
@Setter
public class MobilePasswordAuthenticationProvider implements AuthenticationProvider {

    private UspUserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;
    private LockAccountService lockAccountService;
    private UserLogService userLogService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MobilePasswordAuthenticationToken authenticationToken = (MobilePasswordAuthenticationToken) authentication;
        String mobile = (String) authenticationToken.getPrincipal();
        String password = (String) authenticationToken.getCredentials();
        String extraParams = (String) authenticationToken.getExtraParams();
        LoginAppUser user = (LoginAppUser) userDetailsService.loadUserByMobile(mobile);

        if (user == null) {
            throw new InternalAuthenticationServiceException("手机号不存在");
        }

        if (StrUtil.isBlank(user.getPassword())) {
            throw new InternalAuthenticationServiceException("您还未设置账户密码，请使用短信登录");
        }

        if (lockAccountService.checkAccountIsLocked(user.getMobile()) == true) {
            throw new InternalAuthenticationServiceException("账号被锁定，请找回密码或者120分钟后重试");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            // 密码错误4次后，账号锁定120分钟
            lockAccountService.addPasswordErrorNumber(user.getMobile());
            this.saveLogonPwdFail(user, extraParams);
            throw new BadCredentialsException("密码错误,密码输入错误4次则账户会被锁定");
        }

        // 登录成功，重置密码输入错误
        lockAccountService.resetPasswordErrorNumber(user.getMobile());
        this.saveLogonSuccess(user, extraParams);
        MobilePasswordAuthenticationToken authenticationResult = new MobilePasswordAuthenticationToken(user, password, extraParams, user.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MobilePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private void saveLogonSuccess(LoginAppUser user, String extraParams) {
        try {
            JSONObject jsonObject = new JSONObject(extraParams);
            jsonObject.put("logonType", LogonTypeEnum.MOBILE_PWD.getCode());
            String json = JSON.toJSONString(jsonObject);
            log.info("保存手机号+密码登录成功日志，用户ID:{}，手机号:{}，附加信息:{}",
                    user.getUserId(), user.getMobile(), extraParams);
            userLogService.saveLogonSuccess(user.getUserId(), user.getMobile(), json);
        } catch (Exception e) {
            log.error("保存手机号+密码登录成功日志发生错误:{}", e);
        }
    }

    private void saveLogonPwdFail(LoginAppUser user, String extraParams) {
        try {
            JSONObject jsonObject = new JSONObject(extraParams);
            jsonObject.put("logonType", LogonTypeEnum.MOBILE_PWD.getCode());
            String json = JSON.toJSONString(jsonObject);
            log.info("保存手机号+密码登录失败日志，用户ID:{}，手机号:{}，附加信息:{}",
                    user.getUserId(), user.getMobile(), extraParams);
            userLogService.saveLogonPwdFail(user.getUserId(), user.getMobile(), json);
        } catch (Exception e) {
            log.error("保存手机号+密码登录失败日志发生错误:{}", e);
        }
    }
}
