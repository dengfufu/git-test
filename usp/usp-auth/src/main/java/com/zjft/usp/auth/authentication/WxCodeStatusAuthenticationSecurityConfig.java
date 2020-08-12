package com.zjft.usp.auth.authentication;

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
 * WX登录配置入口
 *
 * @author CK
 * @date 2019-08-02 14:11
 */
@Component
public class WxCodeStatusAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private UspUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationSuccessHandler uspAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler uspAuthenticationFailureHandler;

    @Override
    public void configure(HttpSecurity http) {
        WxCodeStatusAuthenticationFilter wxCodeStatusAuthenticationFilter = new WxCodeStatusAuthenticationFilter();
        wxCodeStatusAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        wxCodeStatusAuthenticationFilter.setAuthenticationSuccessHandler(uspAuthenticationSuccessHandler);
        wxCodeStatusAuthenticationFilter.setAuthenticationFailureHandler(uspAuthenticationFailureHandler);
        //wx provider
        WxCodeStatusAuthenticationProvider provider = new WxCodeStatusAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        http.authenticationProvider(provider)
                .addFilterAfter(wxCodeStatusAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
