package com.zjft.usp.uas.right.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.uas.right.model.SysRoleRightScope;
import com.zjft.usp.uas.right.mapper.SysRoleRightScopeMapper;
import com.zjft.usp.uas.right.service.SysRoleRightScopeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色范围权限表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2020-03-12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleRightScopeServiceImpl extends ServiceImpl<SysRoleRightScopeMapper, SysRoleRightScope> implements SysRoleRightScopeService {

    /**
     * 角色范围权限列表
     *
     * @param sysRoleRightScope
     * @return
     * @author zgpi
     * @date 2020/3/12 19:51
     */
    @Override
    public List<SysRoleRightScope> listRoleRightScope(SysRoleRightScope sysRoleRightScope) {
        return this.list(new QueryWrapper<SysRoleRightScope>()
                .eq("role_id", sysRoleRightScope.getRoleId()));
    }

    /**
     * 根据角色编号列表获得对应权限编号列表
     *
     * @param roleIdList
     * @return
     * @author zgpi
     * @date 2020/3/20 09:34
     */
    @Override
    public List<Long> listRightIdByRoleIdList(List<Long> roleIdList) {
        List<SysRoleRightScope> list = this.list(new QueryWrapper<SysRoleRightScope>()
                .in("role_id", roleIdList));
        List<Long> rightIdList = list.stream().map(e -> e.getRightId()).collect(Collectors.toList());
        return rightIdList.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 添加角色权限范围
     *
     * @param sysRoleRightScope
     * @return
     * @author zgpi
     * @date 2020/3/12 14:51
     */
    @Override
    public void addRoleRightScope(SysRoleRightScope sysRoleRightScope) {
        sysRoleRightScope.setId(KeyUtil.getId());
        this.save(sysRoleRightScope);
    }

    /**
     * 根据角色编号删除角色权限范围
     *
     * @param roleId
     * @return
     * @author zgpi
     * @date 2020/3/12 19:18
     */
    @Override
    public void delRoleRightScopeByRoleId(Long roleId) {
        this.remove(new UpdateWrapper<SysRoleRightScope>()
                .eq("role_id", roleId));
    }
}
