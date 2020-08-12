package com.zjft.usp.uas.right.composite;

import com.zjft.usp.uas.right.model.SysRightUrl;

/**
 * 系统权限聚合类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/12/26 15:13
 */
public interface SysRightUrlCompoService {

    /**
     * 添加系统权限
     *
     * @param sysRightUrl
     * @return
     * @author zgpi
     * @date 2019/12/26 15:29
     **/
    void addSysRightUrl(SysRightUrl sysRightUrl);

    /**
     * 修改系统权限
     *
     * @param sysRightUrl
     * @return
     * @author zgpi
     * @date 2019/12/26 15:57
     **/
    void updateSysRightUrl(SysRightUrl sysRightUrl);

    /**
     * 删除系统权限
     *
     * @param id
     * @return
     * @author zgpi
     * @date 2019/12/26 17:27
     **/
    void delSysRightUrl(Long id);
}
