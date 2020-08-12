package com.zjft.usp.uas.right.composite;

/**
 * 租户聚合类
 *
 * @author zgpi
 * @date 2020/6/17 16:36
 */
public interface SysTenantCompoService {

    /**
     * 初始化租户redis
     *
     * @param
     * @return
     * @author zgpi
     * @date 2020/6/17 16:37
     **/
    void initSysTenantRedis();

    /**
     * 初始化租户权限redis
     *
     * @return
     * @author zgpi
     * @date 2020/6/17 16:44
     **/
    void initTenantRightRedis();

    /**
     * 新增租户redis
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/19 14:24
     **/
    void addSysTenantRedis(Long corpId);

    /**
     * 修改租户redis
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/19 14:25
     **/
    void updateSysTenantRedis(Long corpId);

    /**
     * 删除租户redis
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/18 08:59
     **/
    void delSysTenantRedis(Long corpId);

}
