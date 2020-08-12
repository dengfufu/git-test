package com.zjft.usp;

import com.zjft.usp.common.annotation.EnableLoginArgResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 应用启动主类
 *
 * @author Qiugm
 * @date 2019-08-14 18:52
 * @version 1.0.0
 **/
@EnableDiscoveryClient
@SpringBootApplication
@EnableLoginArgResolver
@EnableTransactionManagement
public class UspPosApplication {
    public static void main(String[] args) {
        SpringApplication.run(UspPosApplication.class, args);
    }
}
