package com.zjft.usp.wms.config;

import com.zjft.usp.redis.template.RedisRepository;
import com.zjft.usp.wms.generator.BusinessCodeGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zgpi
 * @version 1.0
 * @date 2019/9/24 1:57 下午
 **/
@Configuration
public class BusinessCodeConfig {

    @Value("${startCode: 0}")
    private int startCode;

    @Bean
    public BusinessCodeGenerator codeGenerator(RedisRepository redisRepository) {
        return new BusinessCodeGenerator(redisRepository, startCode);
    }
}
