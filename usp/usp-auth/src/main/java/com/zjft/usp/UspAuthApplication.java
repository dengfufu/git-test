package com.zjft.usp;

import com.zjft.usp.common.annotation.EnableLoginArgResolver;
import com.zjft.usp.common.annotation.EnableReqParamResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableFeignClients
@EnableDiscoveryClient
@RefreshScope
@EnableAsync
@EnableLoginArgResolver
@EnableReqParamResolver
@SpringBootApplication
public class UspAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(UspAuthApplication.class, args);
    }

}
