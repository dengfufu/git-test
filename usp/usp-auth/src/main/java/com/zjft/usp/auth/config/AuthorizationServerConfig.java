package com.zjft.usp.auth.config;

import com.zjft.usp.auth.business.service.impl.RedisClientDetailsService;
import com.zjft.usp.common.oauth2.store.AuthRedisTokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.annotation.Resource;

/**
 * OAuth2 授权服务器配置
 *
 * @author CK
 * @date 2019-08-01 08:45
 */
@Configuration
@EnableAuthorizationServer
@Import(AuthRedisTokenStore.class)
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 注入authenticationManager 来支持 password grant type
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * UserDetailsServiceImpl
     */
    @Resource
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private WebResponseExceptionTranslator webResponseExceptionTranslator;

    @Autowired
    private RedisClientDetailsService redisClientDetailsService;

    @Autowired
    private RandomValueAuthorizationCodeServices authorizationCodeServices;


    /**
     * 配置身份认证器，配置认证方式，TokenStore，TokenGranter，OAuth2RequestFactory
     *
     * @param endpoints
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(tokenStore)
//                .reuseRefreshTokens(false)
                // 认证管理器
                .authenticationManager(authenticationManager)
                // 刷新令牌授权类型模式会包含一个检查
                .userDetailsService(userDetailsService)
                // 授权码模式使用redis
                .authorizationCodeServices(authorizationCodeServices)
                // 自定义错误处理
                .exceptionTranslator(webResponseExceptionTranslator);
    }

    /**
     * 暂时使用内存模式
     * 配置应用名称 应用id
     * 配置OAuth2的客户端相关信息
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(redisClientDetailsService);
        redisClientDetailsService.loadAllClientToCache();
    }

    /**
     * 对应于配置AuthorizationServer安全认证的相关信息，创建ClientCredentialsTokenEndpointFilter核心过滤器
     * https://blog.csdn.net/u012040869/article/details/80140515
     *
     * @param security
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                .tokenKeyAccess("isAuthenticated()")
                .checkTokenAccess("permitAll()")
                //让/oauth/token支持client_id以及client_secret作登录认证
                .allowFormAuthenticationForClients();
    }
}
