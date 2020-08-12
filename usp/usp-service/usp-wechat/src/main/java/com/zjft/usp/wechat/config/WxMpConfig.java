package com.zjft.usp.wechat.config;

import com.binarywang.spring.starter.wxjava.mp.properties.WxMpProperties;
import com.zjft.usp.redis.template.RedisRepository;
import com.zjft.usp.wechat.handle.LogHandler;
import com.zjft.usp.wechat.handle.MsgHandler;
import com.zjft.usp.wechat.storage.WxMpRedisClusterConfigImpl;
import javax.annotation.Resource;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author: CK
 * @create: 2019-12-06 11:31
 */
@Configuration
public class WxMpConfig {

    @Autowired
    private LogHandler logHandler;
    @Autowired
    private MsgHandler msgHandler;

    @Autowired
    private WxMpProperties properties;

    @Resource
    private RedisRepository redisRepository;

    @Bean
    public WxMpMessageRouter messageRouter(WxMpService wxMpService) {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

        // 记录所有事件的日志 （异步执行）
        newRouter.rule().handler(this.logHandler).next();

        // 默认
        newRouter.rule().async(false).handler(this.msgHandler).end();

        return newRouter;
    }

    @Bean
    public WxMpConfigStorage wxMpConfigStorage() {
        // 使用redis集群进行存储
        WxMpDefaultConfigImpl config = new WxMpRedisClusterConfigImpl(this.redisRepository);
        this.setWxMpInfo(config);
        return config;
    }

    private void setWxMpInfo(WxMpDefaultConfigImpl config) {
        config.setAppId(properties.getAppId());
        config.setSecret(properties.getSecret());
        config.setToken(properties.getToken());
        config.setAesKey(properties.getAesKey());
    }
}
