package com.zjft.usp.auth.config;

import com.zjft.usp.auth.authentication.*;
import com.zjft.usp.auth.consts.SecurityConstants;
import com.zjft.usp.common.config.DefaultPasswordConfig;
import com.zjft.usp.common.oauth2.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.annotation.Resource;

/**
 * spring security配置
 * 在WebSecurityConfigurerAdapter不拦截oauth要开放的资源
 *
 * @author CK
 * @date 2019-08-01 09:32
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
@EnableWebSecurity
@Import(DefaultPasswordConfig.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource
    private LogoutHandler oauthLogoutHandler;

    @Resource
    private UserDetailsService userDetailsService;

    @Autowired
    private MobilePasswordAuthenticationSecurityConfig mobilePasswordAuthenticationSecurityConfig;

    @Autowired
    MobileSmsCodeAuthenticationSecurityConfig mobileSmsCodeAuthenticationSecurityConfig;

    @Autowired
    WxCodeStatusAuthenticationSecurityConfig wxCodeStatusAuthenticationSecurityConfig;

    @Autowired
    MobileSmsCodeSecurityConfig mobileSmsCodeSecurityConfig;

    @Autowired
    ImageCaptchaSecurityConfig imageCaptchaSecurityConfig;

    /**
     * 这一步的配置是必不可少的，否则SpringBoot会自动配置一个AuthenticationManager,覆盖掉内存中的用户
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // @formatter:off
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers(  "/oauth/authorize").fullyAuthenticated()
                .anyRequest()
                //授权服务器关闭basic认证
                .permitAll()
                .and()
            .formLogin()
                .and()
            .logout()
                .logoutUrl(SecurityConstants.LOGOUT_URL)
                .logoutSuccessUrl("/login.html")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .addLogoutHandler(oauthLogoutHandler)
                .clearAuthentication(true)
                .and()
            .apply(mobileSmsCodeSecurityConfig)
                .and()
            .apply(imageCaptchaSecurityConfig)
                .and()
            .apply(mobilePasswordAuthenticationSecurityConfig)
                .and()
            .apply(mobileSmsCodeAuthenticationSecurityConfig)
                .and()
            .apply(wxCodeStatusAuthenticationSecurityConfig)
                .and()
            .csrf()
                .disable()
            .headers() // 解决不允许显示在iframe的问题 https://blog.csdn.net/whiteForever/article/details/73201586
                .frameOptions()
                .disable()
                .cacheControl();
    }
    // @formatter:on


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

}
