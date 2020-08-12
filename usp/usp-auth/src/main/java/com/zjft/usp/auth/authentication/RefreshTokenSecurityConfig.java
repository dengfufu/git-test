package com.zjft.usp.auth.authentication;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 自动刷新token后置拦截器
 *
 * @author zgpi
 * @date 2020/5/26 09:22
 */
@Component
public class RefreshTokenSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Resource
    private RefreshTokenFilter refreshTokenFilter;

    @Override
    public void configure(HttpSecurity http) {
        http.addFilterAfter(refreshTokenFilter, AbstractPreAuthenticatedProcessingFilter.class);
    }
}
