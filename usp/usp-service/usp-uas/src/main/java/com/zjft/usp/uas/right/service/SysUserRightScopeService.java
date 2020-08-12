package com.zjft.usp.uas.right.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.uas.right.model.SysUserRightScope;

/**
 * <p>
 * 人员范围权限表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2020-06-04
 */
public interface SysUserRightScopeService extends IService<SysUserRightScope> {

    /**
     * 删除人员范围权限
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/4 15:59
     **/
    void delUserRightScopeByUserId(Long userId, Long corpId);
}
