package com.zjft.usp.uas.right.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.uas.right.dto.SysRightUrlDto;
import com.zjft.usp.uas.right.filter.SysRightUrlFilter;
import com.zjft.usp.uas.right.model.SysRightUrl;

import java.util.List;

/**
 * <p>
 * 权限映射表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
public interface SysRightUrlService extends IService<SysRightUrl> {

    /**
     * 分页查询系统权限
     *
     * @param sysRightUrlFilter
     * @return
     * @author zgpi
     * @date 2019/11/26 17:13
     **/
    ListWrapper<SysRightUrlDto> query(SysRightUrlFilter sysRightUrlFilter);

    /**
     * 公共权限列表
     *
     * @param
     * @return
     * @author zgpi
     * @date 2019/12/2 10:05
     **/
    List<SysRightUrl> listCommon();

    /**
     * 添加系统权限
     *
     * @param sysRightUrl
     * @return
     * @author zgpi
     * @date 2019/11/26 18:31
     **/
    void addSysRightUrl(SysRightUrl sysRightUrl);

    /**
     * 修改系统权限
     *
     * @param sysRightUrl
     * @return
     * @author zgpi
     * @date 2019/11/26 18:31
     **/
    void updateSysRightUrl(SysRightUrl sysRightUrl);

    /**
     * 删除鉴权
     *
     * @param id
     * @return
     * @author zgpi
     * @date 2019/11/26 18:32
     **/
    void delSysRightUrl(Long id);

    /**
     * 权限列表
     *
     * @param
     * @return
     * @author zgpi
     * @date 2019/12/3 09:16
     **/
    List<SysRightUrl> listAuthRight();

    /**
     * 根据url获得权限列表
     *
     * @param url
     * @return
     * @author zgpi
     * @date 2019/12/26 16:25
     **/
    List<SysRightUrl> listByUrl(String url);
}
