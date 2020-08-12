package com.zjft.usp.wechat.storage;

import com.zjft.usp.redis.template.RedisRepository;
import java.util.concurrent.TimeUnit;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import me.chanjar.weixin.mp.enums.TicketType;

public class WxMpRedisClusterConfigImpl extends WxMpDefaultConfigImpl {
    private static final String ACCESS_TOKEN_KEY = "wx:access_token:";

    /**
     * 使用连接池保证线程安全.
     */
    private RedisRepository redisRepository;

    private String accessTokenKey;

    public WxMpRedisClusterConfigImpl(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    /**
     * 每个公众号生成独有的存储key.
     */
    @Override
    public void setAppId(String appId) {
        super.setAppId(appId);
        this.accessTokenKey = ACCESS_TOKEN_KEY.concat(appId);
    }

    private String getTicketRedisKey(TicketType type) {
        return String.format("wx:ticket:key:%s:%s", this.appId, type.getCode());
    }

    @Override
    public String getAccessToken() {
        return this.redisRepository.get(this.accessTokenKey).toString();
    }

    @Override
    public boolean isAccessTokenExpired() {
        return this.redisRepository.getRedisTemplate().getExpire(accessTokenKey, TimeUnit.SECONDS) < 2;
    }

    @Override
    public synchronized void updateAccessToken(String accessToken, int expiresInSeconds) {
        this.redisRepository.setExpire(this.accessTokenKey, accessToken, expiresInSeconds -200);
    }

    @Override
    public void expireAccessToken() {
        this.redisRepository.setExpire(this.accessTokenKey, 0);
    }

    @Override
    public String getTicket(TicketType type) {
        return this.redisRepository.get(this.getTicketRedisKey(type)).toString();
    }

    @Override
    public boolean isTicketExpired(TicketType type) {
        return this.redisRepository.getRedisTemplate().getExpire(accessTokenKey, TimeUnit.SECONDS) < 2;
    }

    @Override
    public synchronized void updateTicket(TicketType type, String jsapiTicket, int expiresInSeconds) {
        this.redisRepository.setExpire(this.getTicketRedisKey(type), jsapiTicket, expiresInSeconds -200);
    }

    @Override
    public void expireTicket(TicketType type) {
        this.redisRepository.setExpire(this.getTicketRedisKey(type), 0);
    }
}
