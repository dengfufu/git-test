package com.zjft.usp.uas.redis.service.impl;

import com.zjft.usp.redis.template.RedisRepository;
import com.zjft.usp.uas.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Redis服务实现类
 *
 * @author zgpi
 * @date 2020/6/8 16:53
 */
@Slf4j
@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    private RedisRepository redisRepository;

    /**
     * 获得Redis的值
     *
     * @param key
     * @return
     * @author zgpi
     * @date 2020/6/8 16:53
     **/
    @Override
    public Object findRedisValue(String key) {
        Object obj = null;
        try {
            obj = redisRepository.get(key);
        } catch (Exception e) {
            log.error("获得redis值失败：{}", e);
        }
        return obj;
    }

    /**
     * 删除Redis的值
     *
     * @param key
     * @return
     * @author zgpi
     * @date 2020/6/10 15:45
     **/
    @Override
    public void deleteRedisValue(String key) {
        redisRepository.del(key);
    }
}
