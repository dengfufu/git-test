package com.zjft.usp;

import com.zjft.usp.common.annotation.EnableLoginArgResolver;
import com.zjft.usp.common.annotation.EnableReqParamResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableFeignClients
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableAsync
@SpringBootApplication
@EnableLoginArgResolver
@EnableReqParamResolver
public class UspUasApplication {

    public static void main(String[] args) {
        SpringApplication.run(UspUasApplication.class, args);
    }

}

