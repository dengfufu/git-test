package com.zjft.usp.auth.config;

import com.zjft.usp.auth.business.service.impl.RedisAuthorizationCodeServices;
import com.zjft.usp.auth.business.service.impl.RedisClientDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author CK
 * @date 2019-08-01 08:54
 */
@Configuration
public class ClientDetailsConfig {

    @Resource
    private DataSource dataSource;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 声明 ClientDetails实现
     */
    @Bean
    public RedisClientDetailsService redisClientDetailsService() {
        RedisClientDetailsService redisClientDetailsService = new RedisClientDetailsService(dataSource);
        redisClientDetailsService.setRedisTemplate(redisTemplate);
        return redisClientDetailsService;
    }

    @Bean
    public RandomValueAuthorizationCodeServices authorizationCodeServices() {
        RedisAuthorizationCodeServices redisAuthorizationCodeServices = new RedisAuthorizationCodeServices();
        redisAuthorizationCodeServices.setRedisTemplate(redisTemplate);
        return redisAuthorizationCodeServices;
    }
}
