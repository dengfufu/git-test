package com.zjft.usp.uas.redis.service;

/**
 * Redis服务类
 *
 * @author zgpi
 * @date 2020/6/8 16:52
 */
public interface RedisService {

    /**
     * 获得Redis的值
     *
     * @param key
     * @return
     * @author zgpi
     * @date 2020/6/8 16:53
     **/
    Object findRedisValue(String key);

    /**
     * 删除Redis的值
     *
     * @param key
     * @return
     * @author zgpi
     * @date 2020/6/10 15:45
     **/
    void deleteRedisValue(String key);
}
