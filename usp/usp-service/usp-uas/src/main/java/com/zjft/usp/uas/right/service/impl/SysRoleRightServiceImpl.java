package com.zjft.usp.uas.right.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.uas.right.dto.SysRoleRightDto;
import com.zjft.usp.uas.right.filter.SysRightFilter;
import com.zjft.usp.uas.right.mapper.SysRoleRightMapper;
import com.zjft.usp.uas.right.model.SysRoleRight;
import com.zjft.usp.uas.right.service.SysRoleRightService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色权限表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleRightServiceImpl extends ServiceImpl<SysRoleRightMapper, SysRoleRight> implements SysRoleRightService {

    /**
     * 获得角色权限
     *
     * @param roleIdList
     * @return
     * @author zgpi
     * @date 2019/11/28 15:32
     **/
    @Override
    public List<SysRoleRightDto> listRoleRightByRoleIdList(List<Long> roleIdList) {
        return this.baseMapper.listRoleRightByRoleIdList(roleIdList);
    }

    /**
     * 获得角色权限
     *
     * @param roleId
     * @return
     * @author zgpi
     * @date 2020/6/8 15:51
     **/
    @Override
    public List<SysRoleRightDto> listRoleRightByRoleId(Long roleId) {
        return this.baseMapper.listRoleRightByRoleId(roleId);
    }

    /**
     * 获得角色权限
     *
     * @return
     * @author zgpi
     * @date 2020/6/8 14:09
     **/
    @Override
    public List<SysRoleRightDto> listRoleRight() {
        return this.baseMapper.listRoleRight();
    }

    /**
     * 获得角色编号与权限列表映射
     *
     * @param roleIdList
     * @return
     * @author zgpi
     * @date 2019/12/2 11:06
     **/
    @Override
    public Map<Long, List<SysRoleRightDto>> mapRoleIdAndRightList(List<Long> roleIdList) {
        Map<Long, List<SysRoleRightDto>> map = new HashMap<>();
        List<SysRoleRightDto> corpRoleRightDtoList = this.listRoleRightByRoleIdList(roleIdList);
        if (CollectionUtil.isNotEmpty(corpRoleRightDtoList)) {
            List<SysRoleRightDto> list;
            for (SysRoleRightDto sysRoleRightDto : corpRoleRightDtoList) {
                list = new ArrayList<>();
                if (map.containsKey(sysRoleRightDto.getRoleId())) {
                    list = map.get(sysRoleRightDto.getRoleId());
                }
                list.add(sysRoleRightDto);
                map.put(sysRoleRightDto.getRoleId(), list);
            }
        }
        return map;
    }

    /**
     * 角色对应的权限编号列表
     *
     * @param roleId
     * @return
     * @author zgpi
     * @date 2019/12/7 14:17
     **/
    @Override
    public List<Long> listRoleRightId(Long roleId) {
        List<Long> rightIdList = new ArrayList<>();
        List<SysRoleRight> list = this.list(new QueryWrapper<SysRoleRight>().eq("role_id", roleId));
        if (CollectionUtil.isNotEmpty(list)) {
            rightIdList = list.stream().map(e -> e.getRightId()).collect(Collectors.toList());
        }
        return rightIdList;
    }

    /**
     * 查询用户权限列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<SysRoleRightDto> listByUserId(Long userId) {
        return this.baseMapper.listUserRight(userId);
    }

    @Override
    public List<SysRoleRight> listByRoleId(Long roleId) {
        Assert.notNull(roleId, "角色编号不能为空！");
        QueryWrapper<SysRoleRight> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        List<SysRoleRight> sysRoleRightList = this.list(queryWrapper);
        return sysRoleRightList;
    }

    @Override
    public List<Long> listRightIdByRoleId(Long roleId) {
        List<SysRoleRight> sysRoleRightList = this.listByRoleId(roleId);
        List<Long> rightIdList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(sysRoleRightList)) {
            for (SysRoleRight sysRoleRight : sysRoleRightList) {
                rightIdList.add(sysRoleRight.getRightId());
            }
        }
        return rightIdList;
    }

    /**
     * 查询用户权限，返回权限编号列表
     *
     * @param userId
     * @param corpId
     * @return
     */
    @Override
    public List<Long> listRightIdByUserId(Long userId, Long corpId) {
        return this.baseMapper.listUserRightId(userId, corpId);
    }

    @Override
    public void deleteByRoleId(Long roleId) {
        Assert.notNull(roleId, "角色编号不能为空");
        UpdateWrapper<SysRoleRight> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("role_id", roleId);
        this.remove(updateWrapper);
    }

    /**
     * 获得用户权限列表
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/12/16 15:56
     **/
    @Override
    public List<SysRoleRightDto> listUserRight(Long userId) {
        return this.baseMapper.listUserRight(userId);
    }

    /**
     * 获得系统权限列表
     *
     * @param rightIdList
     * @return
     * @author zgpi
     * @date 2020/2/5 16:55
     **/
    @Override
    public List<SysRoleRightDto> listSysRight(List<Long> rightIdList) {
        if (CollectionUtil.isEmpty(rightIdList)) {
            return null;
        }
        return this.baseMapper.listSysRight(rightIdList);
    }

    /**
     * 获得某权限的用户编号列表
     *
     * @param rightId
     * @param corpId
     * @return
     **/
    @Override
    public List<Long> listUserByRightId(Long rightId, Long corpId) {
        return this.baseMapper.listUserByRightId(rightId, corpId);
    }

    /**
     * 查询系统权限列表
     *
     * @param sysRightFilter
     * @return
     * @author zgpi
     * @date 2020/6/17 16:55
     **/
    @Override
    public List<SysRoleRightDto> listSysAuthRight(SysRightFilter sysRightFilter) {
        return this.baseMapper.listSysAuthRight(sysRightFilter);
    }


}
