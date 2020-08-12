package com.zjft.usp.msg.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author : dcyu
 * @Date : 2019年8月12日
 * @Desc : 短信服务配置类
 */
@Component
@Data
public class SmsConfig {

    /**访问ID*/
    @Value("${msg.access-key-id}")
    private  String accessKeyId;

    /**授权秘钥*/
    @Value("${msg.access-key-secret}")
    private  String accessKeySecret;

    /**地区*/
    @Value("${msg.region-id}")
    private  String regionId;

    /**域名*/
    @Value("${msg.domain}")
    private  String domain;

    /**版本*/
    @Value("${msg.version}")
    private  String version;
}
