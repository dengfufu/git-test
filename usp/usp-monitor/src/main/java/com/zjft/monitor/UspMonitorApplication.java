package com.zjft.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author: CK
 * @create: 2020-01-13 15:40
 */
@EnableAdminServer
@EnableDiscoveryClient
@SpringBootApplication
public class UspMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(UspMonitorApplication.class, args);
    }
}
