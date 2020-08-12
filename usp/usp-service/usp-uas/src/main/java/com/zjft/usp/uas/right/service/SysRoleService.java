package com.zjft.usp.uas.right.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.uas.right.dto.SysRoleDto;
import com.zjft.usp.uas.right.filter.SysRoleFilter;
import com.zjft.usp.uas.right.model.SysRole;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 根据企业编号获取角色映射
     * @param corpId
     * @return
     */
    Map<Long, SysRole> mapSysRoleByCorp(Long corpId);

    /**
     * 根据条件查询角色列表
     * @param sysRoleFilter
     * @return
     */
    List<SysRole> list(SysRoleFilter sysRoleFilter);

    /**
     * 模糊查询角色列表
     *
     * @param sysRoleFilter
     * @return
     * @author zgpi
     * @date 2019/11/26 16:39
     **/
    List<SysRole> matchRole(SysRoleFilter sysRoleFilter);

    /**
     * 根据用户编号与企业编号获得角色
     *
     * @param corpId
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/12/24 17:24
     **/
    List<SysRole> findByUserAndCorp(Long corpId, Long userId);

    /**
     * 用户权限列表
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/12/24 18:07
     **/
    List<SysRole> listUserRole(Long userId);
}
