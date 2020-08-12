package com.zjft.usp.auth.authentication;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.zjft.usp.auth.business.enums.LogonTypeEnum;
import com.zjft.usp.auth.business.service.UserLogService;
import com.zjft.usp.auth.business.service.UspUserDetailsService;
import com.zjft.usp.common.model.LoginAppUser;
import com.zjft.usp.common.oauth2.token.MobileSmsCodeAuthenticationToken;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @author CK
 * @date 2019-08-02 14:10
 */
@Slf4j
@Setter
public class MobileSmsCodeAuthenticationProvider implements AuthenticationProvider {

    private UspUserDetailsService userDetailsService;
    private UserLogService userLogService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MobileSmsCodeAuthenticationToken authenticationToken = (MobileSmsCodeAuthenticationToken) authentication;
        String mobile = (String) authenticationToken.getPrincipal();
        String smsCode = (String) authenticationToken.getCredentials();
        String extraParams = (String) authenticationToken.getExtraParams();
        // 校验手机号
        LoginAppUser user = (LoginAppUser) userDetailsService.loadUserByMobile(mobile);
        if (user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
        this.saveLogonSuccess(user, extraParams);
        MobileSmsCodeAuthenticationToken authenticationResult = new MobileSmsCodeAuthenticationToken(user, smsCode, extraParams, user.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MobileSmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private void saveLogonSuccess(LoginAppUser user, String extraParams) {
        try {
            JSONObject jsonObject = new JSONObject(extraParams);
            jsonObject.put("logonType", LogonTypeEnum.MOBILE_PWD.getCode());
            String json = JSON.toJSONString(jsonObject);
            log.info("保存手机号+短信验证码登录成功日志，用户ID:{}，手机号:{}，附加信息:{}",
                    user.getUserId(), user.getMobile(), extraParams);
            userLogService.saveLogonSuccess(user.getUserId(), user.getMobile(), json);
        } catch (Exception e) {
            log.error("保存手机号+短信验证码登录成功日志发生错误:{}", e);
        }
    }

}
