package com.zjft.usp;

import com.spring4all.swagger.EnableSwagger2Doc;
import com.zjft.usp.common.annotation.EnableLoginArgResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableSwagger2Doc
@EnableDiscoveryClient
@EnableTransactionManagement
@SpringBootApplication
@EnableLoginArgResolver
public class UspMsgApplication {

    public static void main(String[] args) {
        SpringApplication.run(UspMsgApplication.class, args);
    }

}
