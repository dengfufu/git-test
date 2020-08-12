package com.zjft.usp.anyfix.common.service;

/**
 * 权限聚合类
 *
 * @author zgpi
 * @date 2020/6/17 09:55
 */
public interface RightCompoService {

    /**
     * 是否有权限
     *
     * @param corpId
     * @param userId
     * @param rightId
     * @return
     * @author zgpi
     * @date 2020/6/17 09:56
     **/
    boolean hasRight(Long corpId, Long userId, Long rightId);
}
