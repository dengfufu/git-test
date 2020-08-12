package com.zjft.usp.uas.right.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.uas.right.model.SysRoleRightScopeDetail;

/**
 * <p>
 * 角色范围权限表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2020-06-03
 */
public interface SysRoleRightScopeDetailService extends IService<SysRoleRightScopeDetail> {

    /**
     * 根据角色编号删除角色权限范围
     *
     * @param roleId
     * @return
     * @author zgpi
     * @date 2020/6/3 16:25
     **/
    void delRoleRightScopeByRoleId(Long roleId);
}
