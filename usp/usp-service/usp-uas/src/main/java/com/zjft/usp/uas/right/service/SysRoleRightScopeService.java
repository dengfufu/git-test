package com.zjft.usp.uas.right.service;

import com.zjft.usp.uas.right.model.SysRoleRightScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色范围权限表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2020-03-12
 */
public interface SysRoleRightScopeService extends IService<SysRoleRightScope> {

    /**
     * 角色范围权限列表
     *
     * @param sysRoleRightScope
     * @return
     * @author zgpi
     * @date 2020/3/12 19:51
     */
    List<SysRoleRightScope> listRoleRightScope(SysRoleRightScope sysRoleRightScope);

    /**
     * 根据角色编号列表获得对应权限编号列表
     *
     * @param roleIdList
     * @return
     * @author zgpi
     * @date 2020/3/20 09:34
     */
    List<Long> listRightIdByRoleIdList(List<Long> roleIdList);

    /**
     * 添加角色权限范围
     *
     * @param sysRoleRightScope
     * @return
     * @author zgpi
     * @date 2020/3/12 14:51
     */
    void addRoleRightScope(SysRoleRightScope sysRoleRightScope);

    /**
     * 根据角色编号删除角色权限范围
     *
     * @param roleId
     * @return
     * @author zgpi
     * @date 2020/3/12 19:18
     */
    void delRoleRightScopeByRoleId(Long roleId);
}
