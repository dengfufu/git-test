package com.zjft.usp.uas.right.composite;

import java.util.List;

/**
 * 角色用户聚合类
 *
 * @author zgpi
 * @date 2020/5/28 20:47
 */
public interface SysRoleUserCompoService {

    /**
     * 初始化用户角色的redis
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2020/5/28 20:48
     **/
    void initUserRoleRedis(Long userId);

    /**
     * 初始化用户在某个企业的角色redis
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/8 11:11
     **/
    List<Long> initUserCorpRoleRedis(Long userId, Long corpId);

    /**
     * 初始化企业系统角色的用户列表redis
     *
     * @return
     * @author zgpi
     * @date 2020/6/17 19:34
     **/
    void initCorpSysRoleUserListRedis();

    /**
     * 初始化企业系统角色的用户列表redis
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/17 20:45
     **/
    void initCorpSysRoleUserListRedis(Long corpId);

    /**
     * 添加角色人员
     *
     * @param corpId
     * @param userId
     * @param roleIdList
     * @return
     * @author zgpi
     * @date 2020/6/8 16:05
     **/
    void addRoleUser(Long corpId, Long userId, List<Long> roleIdList);

    /**
     * 修改角色人员
     *
     * @param userId
     * @param corpId
     * @param roleIdList
     * @return
     * @author zgpi
     * @date 2020/6/8 16:08
     **/
    void updateRoleUser(Long userId, Long corpId, List<Long> roleIdList);

    /**
     * 删除角色人员
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/8 16:08
     **/
    void delRoleUser(Long userId, Long corpId);
}
