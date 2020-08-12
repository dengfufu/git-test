package com.zjft.usp.uas.right.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.uas.right.dto.SysRightDto;
import com.zjft.usp.uas.right.filter.SysRightFilter;
import com.zjft.usp.uas.right.model.SysRight;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
public interface SysRightMapper extends BaseMapper<SysRight> {

    /**
     * 模糊查询系统权限
     *
     * @param sysRightFilter
     * @return
     * @author zgpi
     * @date 2019/11/26 17:16
     **/
    List<SysRight> matchSysRight(SysRightFilter sysRightFilter);


    List<SysRight> listUserRightByApp(SysRightFilter sysRightFilter);

    /**
     * 获得最大权限编号
     *
     * @param
     * @return
     * @author zgpi
     * @date 2019/11/26 18:45
     **/
    Long findMaxRightId();

    /**
     * 查询系统权限列表树
     *
     * @param sysRightFilter
     * @return
     * @author zgpi
     * @date 2019/12/9 15:03
     **/
    List<SysRightDto> listSysRightTree(SysRightFilter sysRightFilter);

    /**
     * 查询系统权限列表树
     *
     * @param sysRightFilter
     * @return
     * @author zgpi
     * @date 2019/12/9 15:03
     **/
    List<SysRightDto> listSysAuthRightTree(SysRightFilter sysRightFilter);

}
