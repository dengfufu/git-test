package com.zjft.usp.gateway.config;

import com.zjft.usp.common.oauth2.store.AuthRedisTokenStore;
import com.zjft.usp.gateway.authentication.AuthenticationConverter;
import com.zjft.usp.gateway.authentication.AuthenticationFailHandler;
import com.zjft.usp.gateway.authentication.OAuth2AuthenticationManagerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

import javax.annotation.Resource;

/**
 * 资源服务器配置，在网关验证token以及请求权限过滤
 *
 * @author CK
 * @date 2019-08-10 23:56
 */
@Slf4j
@Configuration
@Import(AuthRedisTokenStore.class)
public class ResourceServerConfig {

    @Autowired
    private OAuth2AuthenticationManagerAdapter oAuth2AuthenticationManagerAdapter;

    @Resource
    private TokenStore tokenStore;


    /**
     * 自定义资源服务器filter
     *
     * @return
     */
    @Bean
    public AuthenticationWebFilter authenticationWebFilter() {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(oAuth2AuthenticationManagerAdapter);
        filter.setServerAuthenticationConverter(new AuthenticationConverter());
        filter.setAuthenticationFailureHandler(new AuthenticationFailHandler());
        return filter;
    }

    @Bean
    public OAuth2AuthenticationManager oAuth2AuthenticationManager() {
        OAuth2AuthenticationManager oauthAuthenticationManager = new OAuth2AuthenticationManager();
        oauthAuthenticationManager.setResourceId("");
        oauthAuthenticationManager.setTokenServices(tokenServices());
        return oauthAuthenticationManager;
    }

    @Bean
    public ResourceServerTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenStore(tokenStore);
        return tokenServices;
    }
}
