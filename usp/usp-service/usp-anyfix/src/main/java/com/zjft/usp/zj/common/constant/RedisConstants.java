package com.zjft.usp.zj.common.constant;

/**
 * Redis缓存常量类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-14 13:53
 **/
public class RedisConstants {

    /**
     * 获取当前用户WO应用sessionId
     *
     * @author Qiugm
     * @date 2020-03-14
     * @param corpId
     * @param userId
     * @return
     */
    public static String getWoUserSessionId(Long corpId, Long userId) {
        return "wo-user-sessionid:" + corpId + ":" + userId;
    }

    /**
     * 获取老平台用户Id
     *
     * @author Qiugm
     * @date 2020-03-17
     * @param corpId
     * @param userId
     * @return
     */
    public static String getOldPlatformUserId(Long corpId, Long userId) {
        return "old-platform-userid:" + corpId + ":" + userId;
    }

}
