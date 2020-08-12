package com.zjft.usp.uas.right.composite;

import com.zjft.usp.uas.right.dto.SysRoleRightDto;

import java.util.List;

/**
 * 角色权限聚合类
 *
 * @author zgpi
 * @date 2020/5/28 19:47
 */
public interface SysRoleRightCompoService {

    /**
     * 初始化用户对应的角色权限redis
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2020/5/28 19:54
     **/
    void initUserRoleRightRedis(Long userId);

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
     * 获得用户的角色权限列表
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2020/5/28 19:46
     **/
    List<SysRoleRightDto> listRoleRightByUserId(Long userId);

    /**
     * 初始化角色权限redis
     *
     * @param roleId
     * @return
     * @author zgpi
     * @date 2020/6/15 19:40
     **/
    void initRoleRightRedis(Long roleId);
}
