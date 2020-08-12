package com.zjft.usp.auth.authentication;

import com.zjft.usp.auth.business.service.LockAccountService;
import com.zjft.usp.auth.business.service.UserLogService;
import com.zjft.usp.auth.business.service.UspUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 手机号密码登录配置入口
 *
 * @author CK
 * @date 2019-08-02 14:11
 */
@Component
public class MobilePasswordAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private UspUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationSuccessHandler uspAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler uspAuthenticationFailureHandler;

    @Autowired
    private LockAccountService lockAccountService;

    @Autowired
    private UserLogService userLogService;

    @Override
    public void configure(HttpSecurity http) {
        MobilePasswordAuthenticationFilter mobilePasswordAuthenticationFilter = new MobilePasswordAuthenticationFilter();
        mobilePasswordAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        mobilePasswordAuthenticationFilter.setAuthenticationSuccessHandler(uspAuthenticationSuccessHandler);
        mobilePasswordAuthenticationFilter.setAuthenticationFailureHandler(uspAuthenticationFailureHandler);
        //mobile provider
        MobilePasswordAuthenticationProvider provider = new MobilePasswordAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setLockAccountService(lockAccountService);
        provider.setUserLogService(userLogService);
        http.authenticationProvider(provider)
                .addFilterAfter(mobilePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
