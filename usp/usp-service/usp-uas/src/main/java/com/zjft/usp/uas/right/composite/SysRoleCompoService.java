package com.zjft.usp.uas.right.composite;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.right.dto.SysRoleDto;
import com.zjft.usp.uas.right.filter.SysRoleFilter;
import com.zjft.usp.uas.right.model.SysRole;

import java.util.List;
import java.util.Map;

/**
 * 角色聚合服务类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/12/13 15:43
 */
public interface SysRoleCompoService {

    /**
     * 分页查询角色
     *
     * @param sysRoleFilter
     * @return
     * @author zgpi
     * @date 2019/12/14 14:32
     **/
    ListWrapper<SysRoleDto> query(SysRoleFilter sysRoleFilter, ReqParam reqParam);

    /**
     * 根据角色id查询
     *
     * @param roleId
     * @return
     * @author zgpi
     * @date 2019/12/13 11:44
     **/
    SysRoleDto findByRoleId(Long roleId);

    /**
     * 添加角色
     *
     * @param sysRoleDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/12/13 11:44
     **/
    void addRole(SysRoleDto sysRoleDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 修改角色
     *
     * @param sysRoleDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/12/13 11:45
     **/
    SysRole updateRole(SysRoleDto sysRoleDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 删除角色
     *
     * @param roleId
     * @return
     * @author zgpi
     * @date 2019/12/13 11:45
     **/
    void delRole(Long roleId);

    /**
     * 人员编号与角色列表映射
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/12/16 09:26
     **/
    Map<Long, List<SysRole>> mapUserIdAndRoleList(Long corpId, List<Long> userIdList);

    /**
     * 人员编号与角色名称映射
     *
     * @param corpId
     * @param userIdList
     * @return
     * @author zgpi
     * @date 2019/12/17 14:57
     **/
    Map<Long, String> mapUserIdAndRoleNames(Long corpId, List<Long> userIdList);

    /**
     * 添加系统角色
     *
     * @param corpId
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/12/24 10:58
     **/
    Long addSysRole(Long corpId, Long userId);

    /**
     * 用户角色列表
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/12/24 18:06
     **/
    List<SysRole> listUserRole(Long userId);

    /**
     * 对企业人员设置为管理员
     *
     * @param sysRoleDto
     */
    int setAdmin(SysRoleDto sysRoleDto);
}
