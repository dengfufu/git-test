package com.zjft.usp.uas.right.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.uas.right.dto.SysRoleUserDto;
import com.zjft.usp.uas.right.model.SysRoleUser;

import java.util.List;

/**
 * <p>
 * 角色人员表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
public interface SysRoleUserService extends IService<SysRoleUser> {

    /**
     * 角色人员列表
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/12/16 09:36
     **/
    List<SysRoleUserDto> listRoleUser(Long corpId);

    /**
     * 删除角色人员
     *
     * @param roleId
     * @return
     * @author zgpi
     * @date 2020/3/15 15:13
     */
    void delRoleUser(Long roleId);

    /**
     * 根据人员列表获得角色人员列表
     *
     * @param corpId
     * @param userIdList
     * @return
     * @author zgpi
     * @date 2019/12/17 15:00
     **/
    List<SysRoleUserDto> listRoleUserByUserIdList(Long corpId, List<Long> userIdList);

    /**
     * 根据人员编号与企业编号获得角色编号
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/3/13 17:16
     */
    List<Long> listRoleIdByUserAndCorp(Long userId, Long corpId);

    /**
     * 根据角色编号和用户编号
     *
     * @param roleId
     * @param userId
     * @return
     */
    SysRoleUser querySysRoleUser(Long roleId, Long userId);

    /**
     * 获得企业系统管理员人员列表
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/17 20:47
     **/
    List<SysRoleUserDto> listCorpSysRoleUser(Long corpId);
}
