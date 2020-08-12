package com.zjft.usp.uas.right.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.redis.template.RedisRepository;
import com.zjft.usp.uas.right.dto.SysRoleUserDto;
import com.zjft.usp.uas.right.mapper.SysRoleUserMapper;
import com.zjft.usp.uas.right.model.SysRole;
import com.zjft.usp.uas.right.model.SysRoleUser;
import com.zjft.usp.uas.right.service.SysRightService;
import com.zjft.usp.uas.right.service.SysRoleService;
import com.zjft.usp.uas.right.service.SysRoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色人员权限表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleUserServiceImpl extends ServiceImpl<SysRoleUserMapper, SysRoleUser> implements SysRoleUserService {

    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRightService sysRightService;

    @Resource
    private RedisRepository redisRepository;

    /**
     * 角色人员列表
     *
     * @param corpId 企业编号
     * @return 角色人员列表
     * @author zgpi
     * @date 2019/12/16 09:36
     **/
    @Override
    public List<SysRoleUserDto> listRoleUser(Long corpId) {
        return this.baseMapper.listRoleUser(corpId);
    }

    /**
     * 删除角色人员
     *
     * @param roleId
     * @return
     * @author zgpi
     * @date 2020/3/15 15:13
     */
    @Override
    public void delRoleUser(Long roleId) {
        UpdateWrapper<SysRoleUser> updateWrapper = new UpdateWrapper<SysRoleUser>()
                .eq("role_id", roleId);
        this.remove(updateWrapper);
    }

    /**
     * 根据人员列表获得角色人员列表
     *
     * @param corpId     企业编号
     * @param userIdList 人员编号列表
     * @return 角色人员列表
     * @author zgpi
     * @date 2019/12/17 15:00
     **/
    @Override
    public List<SysRoleUserDto> listRoleUserByUserIdList(Long corpId, List<Long> userIdList) {
        return this.baseMapper.listRoleUserByUserIdList(corpId, userIdList);
    }

    /**
     * 根据人员编号与企业编号获得角色编号
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/3/13 17:16
     */
    @Override
    public List<Long> listRoleIdByUserAndCorp(Long userId, Long corpId) {
        List<SysRole> sysRoleList = sysRoleService.list(new QueryWrapper<SysRole>()
                .eq("corp_id", corpId));
        if (CollectionUtil.isEmpty(sysRoleList)) {
            return new ArrayList<>();
        }
        List<Long> roleIdList = sysRoleList.stream().map(e -> e.getRoleId()).collect(Collectors.toList());
        List<SysRoleUser> sysRoleUserList = this.list(new QueryWrapper<SysRoleUser>()
                .eq("user_id", userId)
                .in("role_id", roleIdList));
        return sysRoleUserList.stream().map(e -> e.getRoleId()).collect(Collectors.toList());
    }

    @Override
    public SysRoleUser querySysRoleUser(Long roleId, Long userId) {
        QueryWrapper<SysRoleUser> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleId);
        wrapper.eq("user_id", userId);
        return this.getOne(wrapper);
    }

    /**
     * 获得企业系统管理员人员列表
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/17 20:47
     **/
    @Override
    public List<SysRoleUserDto> listCorpSysRoleUser(Long corpId) {
        return this.baseMapper.listCorpSysRoleUser(corpId);
    }

}
