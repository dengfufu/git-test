package com.zjft.usp.msg;

import com.zjft.usp.msg.service.PushService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;


/**
 * Push配置类
 *
 * @author zrLin
 * @date 2019-08-16 10:04
 * @version 1.0.0
 **/
@EnableConfigurationProperties
@EnableCaching
public class PushAutoConfigure {
    @Bean
    public PushService pushService() {
        return new PushService();
    }


}
