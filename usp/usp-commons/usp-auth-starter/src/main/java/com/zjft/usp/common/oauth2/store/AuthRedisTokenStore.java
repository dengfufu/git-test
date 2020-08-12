package com.zjft.usp.common.oauth2.store;

import com.zjft.usp.common.oauth2.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * 认证服务器使用Redis存取令牌
 * 注意: 需要配置redis参数
 *
 * @author CK
 * @date 2019-08-01 09:02
 */
@Slf4j
public class AuthRedisTokenStore {

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore redisTokenStore = new RedisTokenStore(connectionFactory);
        return redisTokenStore;
    }
}

