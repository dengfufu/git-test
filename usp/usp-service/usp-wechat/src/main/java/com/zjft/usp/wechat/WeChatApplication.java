package com.zjft.usp.wechat;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author: CK
 * @create: 2019-12-06 13:39
 */
@EnableSwagger2Doc
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class WeChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeChatApplication.class, args);
    }
}

