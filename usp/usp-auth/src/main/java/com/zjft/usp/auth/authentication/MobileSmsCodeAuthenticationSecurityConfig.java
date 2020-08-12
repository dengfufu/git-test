package com.zjft.usp.auth.authentication;

import com.zjft.usp.auth.business.service.UserLogService;
import com.zjft.usp.auth.business.service.UspUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @author CK
 * @date 2019-08-02 14:11
 */
@Component
public class MobileSmsCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private UspUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationSuccessHandler uspAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler uspAuthenticationFailureHandler;

    @Autowired
    private UserLogService userLogService;

    @Override
    public void configure(HttpSecurity http) {
        MobileSmsCodeAuthenticationFilter mobileSmsCodeAuthenticationFilter = new MobileSmsCodeAuthenticationFilter();
        mobileSmsCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        mobileSmsCodeAuthenticationFilter.setAuthenticationSuccessHandler(uspAuthenticationSuccessHandler);
        mobileSmsCodeAuthenticationFilter.setAuthenticationFailureHandler(uspAuthenticationFailureHandler);
        //mobile provider
        MobileSmsCodeAuthenticationProvider provider = new MobileSmsCodeAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setUserLogService(userLogService);
        http.authenticationProvider(provider)
                .addFilterAfter(mobileSmsCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
