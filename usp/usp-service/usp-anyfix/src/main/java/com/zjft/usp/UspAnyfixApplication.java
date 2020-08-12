package com.zjft.usp;

import com.spring4all.swagger.EnableSwagger2Doc;
import com.zjft.usp.common.annotation.EnableLoginArgResolver;
import com.zjft.usp.common.annotation.EnableReqParamResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableSwagger2Doc
@EnableFeignClients
@EnableDiscoveryClient
@EnableLoginArgResolver
@EnableReqParamResolver
@EnableTransactionManagement
@SpringBootApplication
@RefreshScope
public class UspAnyfixApplication {

    public static void main(String[] args) {
        SpringApplication.run(UspAnyfixApplication.class, args);
    }

}
