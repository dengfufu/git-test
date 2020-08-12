package com.zjft.usp.uas.right.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.uas.right.dto.SysRightTreeDto;
import com.zjft.usp.uas.right.filter.SysRightFilter;
import com.zjft.usp.uas.right.model.SysRight;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
public interface SysRightService extends IService<SysRight> {

    List<SysRight> listUserRightByApp(SysRightFilter sysRightFilter);

    /**
     * 分页查询系统权限
     *
     * @param sysRightFilter
     * @return
     * @author zgpi
     * @date 2019/11/26 17:13
     **/
    ListWrapper<SysRight> query(SysRightFilter sysRightFilter);

    /**
     * 查询权限列表
     *
     * @param sysRightFilter
     * @return
     * @author zgpi
     * @date 2019/11/27 09:08
     **/
    List<SysRight> listByCorpId(SysRightFilter sysRightFilter);

    /**
     * 查询企业公共权限
     *
     * @param corpId
     * @return
     */
    List<SysRight> listCommonByCorpId(Long corpId);

    /**
     * 模糊查询系统权限
     *
     * @param sysRightFilter
     * @return
     * @author zgpi
     * @date 2019/11/26 17:13
     **/
    List<SysRight> matchSysRight(SysRightFilter sysRightFilter);

    /**
     * 添加系统权限
     *
     * @param sysRight
     * @return
     * @author zgpi
     * @date 2019/11/26 18:31
     **/
    void addSysRight(SysRight sysRight);

    /**
     * 修改系统权限
     *
     * @param sysRight
     * @return
     * @author zgpi
     * @date 2019/11/26 18:31
     **/
    void updateSysRight(SysRight sysRight);

    /**
     * 获得最大权限编号
     *
     * @param
     * @return
     * @author zgpi
     * @date 2019/11/26 18:43
     **/
    Long findMaxRightId();

    /**
     * 权限编号与名称映射
     *
     * @param rightIdList
     * @return
     * @author zgpi
     * @date 2019/11/27 14:10
     **/
    Map<Long, String> mapIdAndName(List<Long> rightIdList);

    /**
     * 权限编号与应用编号映射
     *
     * @param rightIdList
     * @return
     * @author zgpi
     * @date 2019/11/27 14:10
     **/
    Map<Long, Integer> mapIdAndAppId(List<Long> rightIdList);

    /**
     * 查询系统权限树
     *
     * @param sysRightFilter
     * @return
     * @author zgpi
     * @date 2019/12/9 14:50
     **/
    List<SysRightTreeDto> listSysRightTree(SysRightFilter sysRightFilter);

    /**
     * 查询系统权限树
     *
     * @param sysRightFilter
     * @return
     * @author zgpi
     * @date 2019/12/9 14:50
     **/
    List<SysRightTreeDto> listSysAuthRightTree(SysRightFilter sysRightFilter);

    /**
     * 查询系统范围权限树，只列出范围权限及其上级
     *
     * @param sysRightFilter
     * @return
     * @author zgpi
     * @date 2020/6/4 10:31
     **/
    List<SysRightTreeDto> listSysAuthScopeRightTree(SysRightFilter sysRightFilter);

}
