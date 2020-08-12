package com.zjft.usp.uas.right.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.uas.right.model.SysRoleApp;

import java.util.Map;

/**
 * <p>
 * 角色应用表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-12-13
 */
public interface SysRoleAppService extends IService<SysRoleApp> {

    /**
     * 删除角色应用
     *
     * @param roleId
     * @return
     * @author zgpi
     * @date 2019/12/14 13:54
     **/
    void deleteByRoleId(Long roleId);

    /**
     * 获得企业角色与应用映射
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/12/14 14:12
     **/
    Map<Long, String> mapRoleAndApps(Long corpId);
}
