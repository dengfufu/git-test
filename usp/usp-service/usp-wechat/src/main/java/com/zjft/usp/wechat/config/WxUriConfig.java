package com.zjft.usp.wechat.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "gongzhonghao")
@Data
public class WxUriConfig {

    public static String uri;


    /** uri **/
    @Value("${gongzhonghao.uri}")
    public void setUri(String uri) {
        WxUriConfig.uri = uri;
    }
}
