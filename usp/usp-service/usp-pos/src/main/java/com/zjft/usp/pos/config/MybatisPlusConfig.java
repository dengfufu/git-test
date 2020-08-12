package com.zjft.usp.pos.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis-Plus配置
 *
 * @author Qiugm
 * @date 2019-08-14 16:39
 * @version 1.0.0
 **/
@Configuration
@MapperScan(basePackages = "com.zjft.usp.pos.mapper")
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
