package com.zjft.usp.anyfix.config;

import com.zjft.usp.anyfix.utils.BusinessCodeGenerator;
import com.zjft.usp.redis.template.RedisRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author canlei
 * @version 1.0
 * @date 2020-02-14 19:38
 **/
@Configuration
public class BusinessCodeConfig {

    @Value("${startCode: 0}")
    private int startCode;

    @Bean
    public BusinessCodeGenerator businessCodeGenerator(RedisRepository redisRepository){
        return new BusinessCodeGenerator(redisRepository, startCode);
    }

}
