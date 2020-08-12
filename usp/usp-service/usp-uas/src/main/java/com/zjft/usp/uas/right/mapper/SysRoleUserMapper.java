package com.zjft.usp.uas.right.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.uas.right.dto.SysRoleUserDto;
import com.zjft.usp.uas.right.model.SysRoleUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色人员表 Mapper 接口
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
public interface SysRoleUserMapper extends BaseMapper<SysRoleUser> {

    /**
     * 角色人员列表
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/12/16 09:35
     **/
    List<SysRoleUserDto> listRoleUser(Long corpId);

    /**
     * 根据人员列表获得角色人员列表
     *
     * @param corpId
     * @param userIdList
     * @return
     * @author zgpi
     * @date 2019/12/17 15:00
     **/
    List<SysRoleUserDto> listRoleUserByUserIdList(@Param("corpId") Long corpId,
                                                  @Param("userIdList") List<Long> userIdList);

    /**
     * 获得企业系统管理员人员列表
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/17 20:46
     **/
    List<SysRoleUserDto> listCorpSysRoleUser(@Param("corpId") Long corpId);
}
