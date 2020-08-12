package com.zjft.usp.uas.right.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.uas.right.dto.SysRoleRightDto;
import com.zjft.usp.uas.right.filter.SysRightFilter;
import com.zjft.usp.uas.right.model.SysRoleRight;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色权限表 Mapper 接口
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
public interface SysRoleRightMapper extends BaseMapper<SysRoleRight> {

    /**
     * 获得角色权限列表
     *
     * @param roleIdList
     * @return
     * @author zgpi
     * @date 2019/11/28 15:35
     **/
    List<SysRoleRightDto> listRoleRightByRoleIdList(@Param("roleIdList") List<Long> roleIdList);

    /**
     * 获得角色权限列表
     *
     * @param roleId
     * @return
     * @author zgpi
     * @date 2020/6/8 15:52
     **/
    List<SysRoleRightDto> listRoleRightByRoleId(@Param("roleId") Long roleId);

    /**
     * 获得角色权限列表
     *
     * @param
     * @return
     * @author zgpi
     * @date 2020/6/8 14:09
     **/
    List<SysRoleRightDto> listRoleRight();

    /**
     * 获得用户的权限列表
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/12/4 15:29
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
    List<SysRoleRightDto> listSysRight(@Param("rightIdList") List<Long> rightIdList);

    /**
     * 获得用户的权限编号列表
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/12/6 09:24
     **/
    List<Long> listUserRightId(@Param("userId") Long userId,
                               @Param("corpId") Long corpId);

    /**
     * 获得某权限的用户编号列表
     *
     * @param rightId
     * @param corpId
     * @return
     **/
    List<Long> listUserByRightId(@Param("rightId") Long rightId, @Param("corpId") Long corpId);

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
