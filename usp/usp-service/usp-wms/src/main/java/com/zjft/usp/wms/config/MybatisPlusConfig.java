package com.zjft.usp.wms.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis-Plus配置
 * @author zgpi
 * @version 1.0
 * @date 2019-10-09 09:35
 **/
@Configuration
@MapperScan(basePackages = "com.zjft.usp.wms.**.mapper")
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
