package com.zjft.usp.auth.authentication;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * MobileSmsCodeAuthenticationFilter
 * <p>
 * 短信登录前置拦截器
 *
 * @author CK
 * @date 2019-08-23 16:23
 */
@Component
public class MobileSmsCodeSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Resource
    private MobileSmsCodeFilter mobileSmsCodeFilter;

    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(mobileSmsCodeFilter, AbstractPreAuthenticatedProcessingFilter.class);
    }
}
