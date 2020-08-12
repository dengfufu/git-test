package com.zjft.usp.uas.right.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.uas.right.model.SysRoleApp;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色应用表 Mapper 接口
 * </p>
 *
 * @author zgpi
 * @since 2019-12-13
 */
public interface SysRoleAppMapper extends BaseMapper<SysRoleApp> {

    /**
     * 角色编号与应用映射列表
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/12/14 14:25
     **/
    List<Map<String, Object>> listRoleAndAppsMap(Long corpId);
}
