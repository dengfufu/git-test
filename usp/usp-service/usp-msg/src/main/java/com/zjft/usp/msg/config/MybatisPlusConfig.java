package com.zjft.usp.msg.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis-Plus配置
 * @author zgpi
 * @version 1.0
 * @date 2019-08-05 18:38
 **/
@Configuration
@MapperScan(basePackages = "com.zjft.usp.msg.mapper")
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
