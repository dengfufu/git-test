package com.zjft.usp.uas.right.composite;

import com.zjft.usp.uas.right.dto.SysUserFullRightDto;
import com.zjft.usp.uas.right.filter.SysRightFilter;

import java.util.List;

/**
 * 人员权限聚合服务类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/12/13 15:43
 */
public interface SysUserRightCompoService {

    /**
     * 获得用户权限编号列表
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/12/16 11:29
     **/
    List<Long> listUserRightId(Long userId, Long corpId);

    /**
     * 获得某权限的用户编号列表
     *
     * @param rightId
     * @param corpId
     * @return
     **/
    List<Long> listUserByRightId(Long rightId, Long corpId);

    /**
     * 获得某权限的用户编号列表（不包含系统管理员用户）
     *
     * @param rightId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/7/6 18:47
     **/
    List<Long> listUserByRightIdNoSysUser(Long rightId, Long corpId);

    /**
     * 查询权限，包含范围权限
     *
     * @param sysRightFilter
     * @return
     * @author zgpi
     * @date 2020/6/12 10:17
     **/
    List<SysUserFullRightDto> listFullUserRight(SysRightFilter sysRightFilter);

}
