package com.zjft.usp;

import com.spring4all.swagger.EnableSwagger2Doc;
import com.zjft.usp.common.annotation.EnableLoginArgResolver;
import com.zjft.usp.common.annotation.EnableReqParamResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableSwagger2Doc
@EnableFeignClients
@EnableDiscoveryClient
@EnableLoginArgResolver
@EnableReqParamResolver
@SpringBootApplication
public class UspDeviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UspDeviceApplication.class, args);
    }

}
