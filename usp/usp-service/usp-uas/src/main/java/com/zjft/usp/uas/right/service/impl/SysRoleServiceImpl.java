package com.zjft.usp.uas.right.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.uas.right.dto.SysRoleDto;
import com.zjft.usp.uas.right.filter.SysRoleFilter;
import com.zjft.usp.uas.right.mapper.SysRoleMapper;
import com.zjft.usp.uas.right.model.SysRole;
import com.zjft.usp.uas.right.service.SysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Override
    public Map<Long, SysRole> mapSysRoleByCorp(Long corpId) {
        Map<Long, SysRole> map = new HashMap<>();
        if (corpId == null || corpId == 0L) {
            return map;
        }
        List<SysRole> list = this.list(new QueryWrapper<SysRole>().eq("corp_id", corpId));
        if (list != null && list.size() > 0) {
            for (SysRole sysRole : list) {
                map.put(sysRole.getRoleId(), sysRole);
            }
        }
        return map;
    }

    @Override
    public List<SysRole> list(SysRoleFilter sysRoleFilter) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("corp_id", sysRoleFilter.getCorpId());
        if (StrUtil.isNotBlank(sysRoleFilter.getEnabled())) {
            queryWrapper.eq("enabled", StrUtil.trimToEmpty(sysRoleFilter.getEnabled()).toUpperCase());
        }
        List<SysRole> list = this.list(queryWrapper);
        return list;
    }

    /**
     * 模糊查询角色列表
     *
     * @param sysRoleFilter
     * @return
     * @author zgpi
     * @date 2019/11/26 16:39
     **/
    @Override
    public List<SysRole> matchRole(SysRoleFilter sysRoleFilter) {
        return this.baseMapper.matchRole(sysRoleFilter);
    }

    /**
     * 根据用户编号与企业编号获得角色
     *
     * @param corpId
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/12/24 17:24
     **/
    @Override
    public List<SysRole> findByUserAndCorp(Long corpId, Long userId) {
        return this.baseMapper.findByUserAndCorp(corpId, userId);
    }

    /**
     * 用户权限列表
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/12/24 18:07
     **/
    @Override
    public List<SysRole> listUserRole(Long userId) {
        return this.baseMapper.listUserRole(userId);
    }
}
