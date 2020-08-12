package com.zjft.usp.uas.right.composite;

import com.zjft.usp.uas.right.dto.SysRoleRightScopeDto;

import java.util.List;

/**
 * 角色范围权限聚合类
 *
 * @author zgpi
 * @date 2020/6/3 16:42
 */
public interface SysRoleRightScopeCompoService {

    /**
     * 角色范围权限列表
     *
     * @param roleId
     * @return
     * @author zgpi
     * @date 2020/6/3 16:44
     **/
    List<SysRoleRightScopeDto> listRoleRightScope(Long roleId);

}
