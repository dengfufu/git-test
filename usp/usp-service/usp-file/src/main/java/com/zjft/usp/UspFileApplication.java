package com.zjft.usp;

import com.zjft.usp.common.annotation.EnableLoginArgResolver;
import com.zjft.usp.common.annotation.EnableReqParamResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @description: spring boot的启动类
 * @author chenxiaod
 * @date 2019/8/9 16:17
 */
@EnableAsync
@EnableLoginArgResolver
@EnableReqParamResolver
@EnableTransactionManagement
@SpringBootApplication
public class UspFileApplication {
    public static void main(String[] args) {
        SpringApplication.run(UspFileApplication.class, args);
    }
}
