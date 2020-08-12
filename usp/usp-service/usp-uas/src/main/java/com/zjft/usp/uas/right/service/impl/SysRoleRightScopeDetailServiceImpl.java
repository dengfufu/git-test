package com.zjft.usp.uas.right.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.uas.right.mapper.SysRoleRightScopeDetailMapper;
import com.zjft.usp.uas.right.model.SysRoleRightScope;
import com.zjft.usp.uas.right.model.SysRoleRightScopeDetail;
import com.zjft.usp.uas.right.service.SysRoleRightScopeDetailService;
import com.zjft.usp.uas.right.service.SysRoleRightScopeService;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @since 2020-06-03
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleRightScopeDetailServiceImpl extends ServiceImpl<SysRoleRightScopeDetailMapper, SysRoleRightScopeDetail> implements SysRoleRightScopeDetailService {

    @Autowired
    private SysRoleRightScopeService sysRoleRightScopeService;

    /**
     * 根据角色编号删除角色权限范围
     *
     * @param roleId
     * @return
     * @author zgpi
     * @date 2020/6/3 16:25
     **/
    @Override
    public void delRoleRightScopeByRoleId(Long roleId) {
        List<SysRoleRightScope> sysRoleRightScopeList = sysRoleRightScopeService.list(
                new QueryWrapper<SysRoleRightScope>().eq("role_id", roleId)
        );
        List<Long> idList = sysRoleRightScopeList.stream().map(e -> e.getId()).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(idList)) {
            this.removeByIds(idList);
        }
    }
}
