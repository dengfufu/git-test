package com.zjft.usp.device.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis-Plus配置
 * @author zgpi
 * @version 1.0
 * @date 2019-09-19 09:35
 **/
@Configuration
@MapperScan(basePackages = "com.zjft.usp.device.**.mapper")
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
