package com.zjft.usp.uas.right.composite;

import com.zjft.usp.uas.right.dto.SysRightDto;

/**
 * 系统权限聚合接口类
 *
 * @author zgpi
 * @version 1.0
 * @date 2020/3/11 16:52
 */
public interface SysRightCompoService {

    /**
     * 添加系统权限
     *
     * @param sysRightDto
     * @return
     * @author zgpi
     * @date 2020/3/11 16:52
     */
    void addSysRight(SysRightDto sysRightDto);

    /**
     * 修改系统权限
     *
     * @param sysRightDto
     * @return
     * @author zgpi
     * @date 2020/3/11 16:53
     */
    void updateSysRight(SysRightDto sysRightDto);

    /**
     * 删除系统权限
     *
     * @param rightId
     * @return
     * @author zgpi
     * @date 2020/3/11 16:53
     */
    void delSysRight(Long rightId);

    /**
     * 初始化系统公共权限
     *
     * @param
     * @return
     * @author zgpi
     * @date 2020/6/8 15:22
     **/
    void initSysCommonRightRedis();
}
