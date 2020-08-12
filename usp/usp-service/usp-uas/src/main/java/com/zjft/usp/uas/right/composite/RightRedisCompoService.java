package com.zjft.usp.uas.right.composite;

import java.util.List;

/**
 * 权限异步聚合类
 *
 * @author zgpi
 * @date 2020/6/5 20:18
 */
public interface RightRedisCompoService {

    /**
     * 初始化角色权限
     *
     * @param roleId
     * @return
     * @author zgpi
     * @date 2020/6/15 19:19
     **/
    void initRoleRightRedis(Long roleId);

    /**
     * 删除角色权限
     *
     * @param roleId
     * @return
     * @author zgpi
     * @date 2020/6/15 19:25
     **/
    void delRoleRightRedis(Long roleId);

    /**
     * 异步删除redis中用户的范围权限
     *
     * @param corpId
     * @param userIdList
     * @param rightIdList
     * @return
     * @author zgpi
     * @date 2020/6/15 17:11
     **/
    void delRoleUserScopeRightKey(Long corpId, List<Long> userIdList, List<Long> rightIdList);

    /**
     * 删除redis中用户的范围权限
     *
     * @param corpId
     * @param userId
     * @param rightIdList
     * @return
     * @author zgpi
     * @date 2020/6/15 20:16
     **/
    void delRoleUserScopeRightKey(Long corpId, Long userId, List<Long> rightIdList);

    /**
     * 异步删除redis中用户的范围权限
     *
     * @param corpId
     * @param roleId
     * @return
     * @author zgpi
     * @date 2020/6/15 17:11
     **/
    void delRoleUserScopeRightKey(Long corpId, Long roleId);

    /**
     * 异步删除redis中用户的角色
     *
     * @param corpId
     * @param roleId
     * @param userIdList
     * @return
     * @author zgpi
     * @date 2020/6/15 20:09
     **/
    void delUserRoleRedis(Long corpId, Long roleId, List<Long> userIdList);

    /**
     * 删除redis中用户的角色
     *
     * @param corpId
     * @param userId
     * @return
     * @author zgpi
     * @date 2020/6/15 20:09
     **/
    void delUserRoleRedis(Long corpId, Long userId);

    /**
     * 添加redis中用户的角色
     *
     * @param corpId
     * @param userId
     * @param roleId
     * @return
     * @author zgpi
     * @date 2020/6/15 19:28
     **/
    void addUserRoleRedis(Long corpId, Long userId, Long roleId);

    /**
     * 添加redis中用户的角色列表
     *
     * @param corpId
     * @param userId
     * @param userIdList
     * @return
     * @author zgpi
     * @date 2020/6/15 19:58
     **/
    void addUserRoleRedis(Long corpId, Long userId, List<Long> userIdList);

    /**
     * 初始化redis用户企业角色
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/15 20:04
     **/
    void initUserCorpRoleRedis(Long userId, Long corpId);

    /**
     * 初始化系统公共权限
     *
     * @param
     * @return
     * @author zgpi
     * @date 2020/6/8 15:22
     **/
    void initSysCommonRightRedis();

    /**
     * 初始化角色权限redis
     *
     * @param
     * @return
     * @author zgpi
     * @date 2020/6/8 12:19
     **/
    void initRoleRightRedis();

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
     * 新增租户redis
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/19 14:26
     **/
    void addSysTenantRedis(Long corpId);

    /**
     * 修改租户redis
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/19 14:27
     **/
    void updateSysTenantRedis(Long corpId);

    /**
     * 初始化租户权限redis
     *
     * @return
     * @author zgpi
     * @date 2020/6/17 16:44
     **/
    void initTenantRightRedis();

    /**
     * 初始化企业系统管理员角色人员列表redis
     *
     * @param
     * @return
     * @author zgpi
     * @date 2020/6/17 19:43
     **/
    void initCorpSysRoleUserListRedis();

    /**
     * 初始化企业系统管理员角色人员列表redis
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/17 20:44
     **/
    void initCorpSysRoleUserListRedis(Long corpId);

    /**
     * 删除企业系统管理员角色人员redis
     *
     * @param corpId
     * @param userId
     * @return
     * @author zgpi
     * @date 2020/6/17 20:52
     **/
    void delCorpSysRoleUserListRedis(Long corpId, Long userId);
}
