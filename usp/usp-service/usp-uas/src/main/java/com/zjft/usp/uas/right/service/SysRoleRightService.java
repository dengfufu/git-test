package com.zjft.usp.uas.right.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.uas.right.dto.SysRoleRightDto;
import com.zjft.usp.uas.right.filter.SysRightFilter;
import com.zjft.usp.uas.right.model.SysRoleRight;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色权限表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
public interface SysRoleRightService extends IService<SysRoleRight> {

    /**
     * 获得角色权限
     *
     * @param roleIdList
     * @return
     * @author zgpi
     * @date 2019/11/28 15:32
     **/
    List<SysRoleRightDto> listRoleRightByRoleIdList(List<Long> roleIdList);

    /**
     * 获得角色权限
     *
     * @param roleId
     * @return
     * @author zgpi
     * @date 2020/6/8 15:51
     **/
    List<SysRoleRightDto> listRoleRightByRoleId(Long roleId);

    /**
     * 获得角色权限
     *
     * @param
     * @return
     * @author zgpi
     * @date 2020/6/8 14:09
     **/
    List<SysRoleRightDto> listRoleRight();

    /**
     * 获得角色编号与权限列表映射
     *
     * @param roleIdList
     * @return
     * @author zgpi
     * @date 2019/12/2 11:06
     **/
    Map<Long, List<SysRoleRightDto>> mapRoleIdAndRightList(List<Long> roleIdList);

    /**
     * 角色对应的权限编号列表
     *
     * @param roleId
     * @return
     * @author zgpi
     * @date 2019/12/7 14:17
     **/
    List<Long> listRoleRightId(Long roleId);

    /**
     * 查询用户权限列表
     *
     * @param userId
     * @return
     */
    List<SysRoleRightDto> listByUserId(Long userId);

    /**
     * 查询角色权限列表
     *
     * @param roleId
     * @return
     */
    List<SysRoleRight> listByRoleId(Long roleId);

    /**
     * 查询角色权限，返回权限编号列表
     *
     * @param roleId
     * @return
     */
    List<Long> listRightIdByRoleId(Long roleId);

    /**
     * 查询用户权限，返回权限编号列表
     *
     * @param userId
     * @param corpId
     * @return
     */
    List<Long> listRightIdByUserId(Long userId, Long corpId);

    /**
     * 根据角色删除权限
     *
     * @param roleId
     */
    void deleteByRoleId(Long roleId);

    /**
     * 获得用户权限列表
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/12/16 15:56
     **/
    List<SysRoleRightDto> listUserRight(Long userId);

    /**
     * 获得系统权限列表
     *
     * @param rightIdList
     * @return
     * @author zgpi
     * @date 2020/2/5 16:55
     **/
    List<SysRoleRightDto> listSysRight(List<Long> rightIdList);


    /**
     * 获得某权限的用户编号列表
     *
     * @param rightId
     * @param corpId
     * @return
     **/
    List<Long> listUserByRightId(Long rightId, Long corpId);

    /**
     * 查询系统权限列表
     *
     * @param sysRightFilter
     * @return
     * @author zgpi
     * @date 2020/6/17 16:55
     **/
    List<SysRoleRightDto> listSysAuthRight(SysRightFilter sysRightFilter);
}
