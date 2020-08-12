package com.zjft.usp.auth.authentication;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 密码登录前置拦截器
 *
 * @author: CK
 * @create: 2020-04-01 09:37
 */
@Component
public class ImageCaptchaSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Resource
    private ImageCaptchaFilter imageCaptchaFilter;

    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(imageCaptchaFilter, AbstractPreAuthenticatedProcessingFilter.class);
    }
}
