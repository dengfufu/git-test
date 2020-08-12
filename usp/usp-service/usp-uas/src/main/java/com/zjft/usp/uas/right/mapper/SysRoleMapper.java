package com.zjft.usp.uas.right.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.uas.right.dto.SysRoleDto;
import com.zjft.usp.uas.right.filter.SysRoleFilter;
import com.zjft.usp.uas.right.model.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 模糊查询角色
     *
     * @param sysRoleFilter
     * @return
     * @author zgpi
     * @date 2019/11/26 16:36
     **/
    List<SysRole> matchRole(SysRoleFilter sysRoleFilter);

    /**
     * 根据用户编号与企业编号获得角色
     *
     * @param corpId
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/12/24 17:23
     **/
    List<SysRole> findByUserAndCorp(@Param("corpId") Long corpId,
                                    @Param("userId") Long userId);

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
