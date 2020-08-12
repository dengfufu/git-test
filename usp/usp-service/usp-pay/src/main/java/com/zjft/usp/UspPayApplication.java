package com.zjft.usp;

import com.spring4all.swagger.EnableSwagger2Doc;
import com.zjft.usp.common.annotation.EnableLoginArgResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: CK
 * @create: 2020-05-19 15:27
 */
@EnableSwagger2Doc
@EnableFeignClients
@EnableDiscoveryClient
@EnableLoginArgResolver
@EnableTransactionManagement
@SpringBootApplication
@RefreshScope
public class UspPayApplication {

    public static void main(String[] args) {
        SpringApplication.run(UspPayApplication.class, args);
    }
}
