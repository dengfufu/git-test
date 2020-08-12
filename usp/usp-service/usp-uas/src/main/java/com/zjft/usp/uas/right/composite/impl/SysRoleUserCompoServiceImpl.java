package com.zjft.usp.uas.right.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjft.usp.common.constant.RedisRightConstants;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.redis.template.RedisRepository;
import com.zjft.usp.uas.right.composite.RightRedisCompoService;
import com.zjft.usp.uas.right.composite.SysRoleUserCompoService;
import com.zjft.usp.uas.right.dto.SysRoleUserDto;
import com.zjft.usp.uas.right.filter.SysRoleFilter;
import com.zjft.usp.uas.right.model.SysRight;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色用户聚合实现类
 *
 * @author zgpi
 * @date 2020/5/28 20:49
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleUserCompoServiceImpl implements SysRoleUserCompoService {

    private static int USER_ROLE_REDIS_EXPIRE = 60 * 60 * 24 * 15; // 15天，人员角色存放redis过期时间

    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRightService sysRightService;
    @Autowired
    private SysRoleUserService sysRoleUserService;
    @Autowired
    private RightRedisCompoService rightRedisCompoService;

    @Resource
    private RedisRepository redisRepository;

    /**
     * 初始化用户角色的redis
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2020/5/28 20:48
     **/
    @Override
    public void initUserRoleRedis(Long userId) {
        List<SysRole> sysRoleList = sysRoleService.listUserRole(userId);
        Map<Long, List<Long>> map = new HashMap<>();
        for (SysRole sysRole : sysRoleList) {
            Long corpId = sysRole.getCorpId();
            List<Long> roleIdList = new ArrayList<>();
            if (map.containsKey(corpId)) {
                roleIdList = map.get(corpId);
            }
            roleIdList.add(sysRole.getRoleId());
            map.put(corpId, roleIdList);
        }
        if (CollectionUtil.isNotEmpty(map)) {
            for (Long corpId : map.keySet()) {
                redisRepository.setExpire(RedisRightConstants.getUserRoleKey(userId, corpId), map.get(corpId),
                        USER_ROLE_REDIS_EXPIRE);
            }
        }
    }

    /**
     * 初始化用户在某个企业的角色redis
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/8 11:11
     **/
    @Override
    public List<Long> initUserCorpRoleRedis(Long userId, Long corpId) {
        List<SysRole> sysRoleList = sysRoleService.findByUserAndCorp(corpId, userId);
        List<Long> roleIdList = sysRoleList.stream().map(e -> e.getRoleId()).collect(Collectors.toList());
        redisRepository.setExpire(RedisRightConstants.getUserRoleKey(userId, corpId), roleIdList,
                USER_ROLE_REDIS_EXPIRE);
        return roleIdList;
    }

    /**
     * 初始化企业系统角色的用户列表redis
     *
     * @return
     * @author zgpi
     * @date 2020/6/17 19:34
     **/
    @Override
    public void initCorpSysRoleUserListRedis() {
        String flag = (String) redisRepository.get(RedisRightConstants.getCorpSysRoleUserInit());
        if ("Y".equalsIgnoreCase(StrUtil.trimToEmpty(flag))) {
            return;
        }
        Map<Long, List<Long>> corpIdAndUserIdListMap = new HashMap<>();
        List<SysRoleUserDto> sysRoleUserDtoList = sysRoleUserService.listCorpSysRoleUser(0L);
        for (SysRoleUserDto sysRoleUserDto : sysRoleUserDtoList) {
            List<Long> userIdList = new ArrayList<>();
            if (corpIdAndUserIdListMap.containsKey(sysRoleUserDto.getCorpId())) {
                userIdList = corpIdAndUserIdListMap.get(sysRoleUserDto.getCorpId());
            }
            userIdList.add(sysRoleUserDto.getUserId());
            corpIdAndUserIdListMap.put(sysRoleUserDto.getCorpId(), userIdList);
        }
        if (CollectionUtil.isNotEmpty(corpIdAndUserIdListMap)) {
            for (Long corpId : corpIdAndUserIdListMap.keySet()) {
                List<Long> userIdList = corpIdAndUserIdListMap.get(corpId);
                redisRepository.set(RedisRightConstants.getCorpSysRoleUserKey(corpId), userIdList);
            }
        }
        redisRepository.set(RedisRightConstants.getCorpSysRoleUserInit(), "Y");
    }

    /**
     * 初始化企业系统角色的用户列表redis
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/17 20:45
     **/
    @Override
    public void initCorpSysRoleUserListRedis(Long corpId) {
        List<SysRoleUserDto> sysRoleUserDtoList = sysRoleUserService.listCorpSysRoleUser(corpId);
        List<Long> userIdList = new ArrayList<>();
        for (SysRoleUserDto sysRoleUserDto : sysRoleUserDtoList) {
            userIdList.add(sysRoleUserDto.getUserId());
        }
        if (CollectionUtil.isNotEmpty(userIdList)) {
            redisRepository.set(RedisRightConstants.getCorpSysRoleUserKey(corpId), userIdList);
        }
    }

    /**
     * 添加角色人员
     *
     * @param corpId
     * @param userId
     * @param roleIdList
     * @return
     * @author zgpi
     * @date 2020/6/8 16:05
     **/
    @Override
    public void addRoleUser(Long corpId, Long userId, List<Long> roleIdList) {
        this.delRoleUserScopeRightKey(corpId, userId);
        if (CollectionUtil.isNotEmpty(roleIdList) && LongUtil.isNotZero(userId)) {
            List<SysRoleUser> list = new ArrayList<>(roleIdList.size());
            SysRoleUser sysRoleUser;
            for (Long roleId : roleIdList) {
                sysRoleUser = new SysRoleUser();
                sysRoleUser.setRoleId(roleId);
                sysRoleUser.setUserId(userId);
                list.add(sysRoleUser);
            }
            sysRoleUserService.saveBatch(list);
            // 新增人员角色redis
            rightRedisCompoService.addUserRoleRedis(corpId, userId, roleIdList);
            // 初始化系统管理员人员列表redis
            rightRedisCompoService.initCorpSysRoleUserListRedis(corpId);
        }
    }

    /**
     * 修改角色人员
     *
     * @param userId
     * @param corpId
     * @param roleIdList
     * @return
     * @author zgpi
     * @date 2020/6/8 16:08
     **/
    @Override
    public void updateRoleUser(Long userId, Long corpId, List<Long> roleIdList) {
        this.delRoleUserScopeRightKey(corpId, userId);
        if (LongUtil.isNotZero(userId)) {
            // 先删除旧角色人员
            SysRoleFilter sysRoleFilter = new SysRoleFilter();
            sysRoleFilter.setCorpId(corpId);
            List<SysRole> oldSysRoleList = sysRoleService.list(sysRoleFilter);
            List<Long> oldRoleIdList = oldSysRoleList.stream().map(SysRole::getRoleId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(oldRoleIdList)) {
                UpdateWrapper<SysRoleUser> updateWrapper = new UpdateWrapper<SysRoleUser>()
                        .eq("user_id", userId)
                        .in("role_id", oldRoleIdList);
                sysRoleUserService.remove(updateWrapper);
            }
            // 新增新角色人员
            if (CollectionUtil.isNotEmpty(roleIdList)) {
                List<SysRoleUser> list = new ArrayList<>();
                SysRoleUser sysRoleUser;
                for (Long roleId : roleIdList) {
                    sysRoleUser = new SysRoleUser();
                    sysRoleUser.setRoleId(roleId);
                    sysRoleUser.setUserId(userId);
                    list.add(sysRoleUser);
                }
                sysRoleUserService.saveBatch(list);
            }
        }
        this.checkSysRoleManager(corpId);
        // 初始化人员角色redis
        rightRedisCompoService.initUserCorpRoleRedis(userId, corpId);
        // 初始化系统管理员人员列表redis
        rightRedisCompoService.initCorpSysRoleUserListRedis(corpId);
    }

    /**
     * 删除角色人员
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/8 16:08
     **/
    @Override
    public void delRoleUser(Long userId, Long corpId) {
        this.delRoleUserScopeRightKey(corpId, userId);
        SysRoleFilter sysRoleFilter = new SysRoleFilter();
        sysRoleFilter.setCorpId(corpId);
        List<SysRole> list = sysRoleService.list(sysRoleFilter);
        List<Long> roleIdList = list.stream().map(SysRole::getRoleId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(roleIdList)) {
            UpdateWrapper<SysRoleUser> updateWrapper = new UpdateWrapper<SysRoleUser>()
                    .eq("user_id", userId)
                    .in("role_id", roleIdList);
            sysRoleUserService.remove(updateWrapper);
        }
        this.checkSysRoleManager(corpId);
        // 删除人员角色redis
        rightRedisCompoService.delUserRoleRedis(corpId, userId);
        // 删除系统管理员人员列表redis
        rightRedisCompoService.delCorpSysRoleUserListRedis(corpId, userId);
    }

    /**
     * 删除redis中用户的范围权限
     *
     * @param corpId
     * @param userId
     * @return
     * @author zgpi
     * @date 2020/3/15 13:13
     */
    private void delRoleUserScopeRightKey(Long corpId, Long userId) {
        List<SysRight> sysRightList = sysRightService.list(new QueryWrapper<SysRight>()
                .eq("has_scope", "Y"));
        List<Long> rightIdList = sysRightList.stream().map(e -> e.getRightId()).collect(Collectors.toList());
        rightRedisCompoService.delRoleUserScopeRightKey(corpId, userId, rightIdList);
    }

    /**
     * 检查系统管理员角色
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/8 16:09
     **/
    private void checkSysRoleManager(Long corpId) {
        SysRole sysRole = this.sysRoleService.getOne(new QueryWrapper<SysRole>().eq("corp_id", corpId)
                .eq("sys_type", 1));
        if (sysRole != null) {
            List<SysRoleUser> sysRoleUserList = sysRoleUserService.list(new QueryWrapper<SysRoleUser>()
                    .eq("role_id", sysRole.getRoleId()));
            if (CollectionUtil.isEmpty(sysRoleUserList)) {
                throw new AppException("当前用户是企业唯一的系统管理员，不能去除系统管理员角色！");
            }
        }
    }
}
