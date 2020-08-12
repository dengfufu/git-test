package com.zjft.usp.uas.right.composite;

import com.zjft.usp.common.dto.RightScopeDto;
import com.zjft.usp.uas.right.dto.SysUserDto;
import com.zjft.usp.uas.right.dto.SysUserRightScopeDto;
import com.zjft.usp.uas.right.filter.SysUserScopeRightFilter;

import java.util.List;

/**
 * 人员范围权限聚合类
 *
 * @author zgpi
 * @date 2020/6/4 14:06
 */
public interface SysUserRightScopeCompoService {

    /**
     * 人员范围权限列表
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/4 14:08
     **/
    List<SysUserRightScopeDto> listUserRightScope(Long userId, Long corpId);

    /**
     * 设置人员角色范围权限
     *
     * @param sysUserDto
     * @return
     * @author zgpi
     * @date 2020/6/4 14:33
     **/
    void setUserRightScope(SysUserDto sysUserDto);

    /**
     * 获得人员范围权限
     * 检查redis，若redis存在，从redis取，若不存在，则新增
     *
     * @param sysUserScopeRightFilter
     * @return
     * @author zgpi
     * @date 2020/6/5 10:07
     **/
    List<RightScopeDto> listUserRightScope(SysUserScopeRightFilter sysUserScopeRightFilter);

    /**
     * 删除人员范围权限
     *
     * @param userIdList
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/5 11:05
     **/
    void delUserRightScope(List<Long> userIdList, Long corpId);

    /**
     * 删除人员范围权限
     *
     * @param userIdList
     * @param rightIdList
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/5 11:15
     **/
    void delUserRightScope(List<Long> userIdList, List<Long> rightIdList, Long corpId);

    /**
     * 初始化人员范围权限
     *
     * @param sysUserScopeRightFilter
     * @return
     * @author zgpi
     * @date 2020/6/4 19:48
     **/
    List<RightScopeDto> initUserRightScope(SysUserScopeRightFilter sysUserScopeRightFilter);

    /**
     * 获得人员范围权限（角色范围+个人范围）
     *
     * @param userId
     * @param corpId
     * @param rightId
     * @return
     * @author zgpi
     * @date 2020/6/11 19:33
     **/
    List<RightScopeDto> listFinalUserRightScope(Long userId, Long corpId, Long rightId);
}
