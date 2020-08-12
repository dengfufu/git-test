package com.zjft.usp.uas.right.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.common.constant.RedisRightConstants;
import com.zjft.usp.common.model.Right;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.redis.template.RedisRepository;
import com.zjft.usp.uas.right.composite.*;
import com.zjft.usp.uas.right.dto.SysRoleRightDto;
import com.zjft.usp.uas.right.model.SysRight;
import com.zjft.usp.uas.right.model.SysRole;
import com.zjft.usp.uas.right.model.SysRoleUser;
import com.zjft.usp.uas.right.service.SysRightService;
import com.zjft.usp.uas.right.service.SysRoleRightService;
import com.zjft.usp.uas.right.service.SysRoleService;
import com.zjft.usp.uas.right.service.SysRoleUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 权限异步聚合实现类
 * 异步方法的类不能加上事务控制
 *
 * @author zgpi
 * @date 2020/6/5 20:19
 */
@Slf4j
@Service
public class RightRedisCompoServiceImpl implements RightRedisCompoService {

    private static int USER_ROLE_REDIS_EXPIRE = 60 * 60 * 24 * 15; // 15天，人员角色存放redis过期时间

    @Autowired
    private SysRoleUserService sysRoleUserService;
    @Autowired
    private SysRoleRightService sysRoleRightService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRightService sysRightService;
    @Autowired
    private SysRightCompoService sysRightCompoService;
    @Autowired
    private SysUserRightScopeCompoService sysUserRightScopeCompoService;
    @Autowired
    private SysRoleUserCompoService sysRoleUserCompoService;
    @Autowired
    private SysRoleRightCompoService sysRoleRightCompoService;
    @Autowired
    private SysTenantCompoService sysTenantCompoService;

    @Resource
    private RedisRepository redisRepository;

    /**
     * 初始化角色权限
     *
     * @param roleId
     * @return
     * @author zgpi
     * @date 2020/6/15 19:19
     **/
    @Override
    public void initRoleRightRedis(Long roleId) {
        Map<String, List<Right>> rightMap = new HashMap<>();
        List<SysRoleRightDto> sysRoleRightDtoList = sysRoleRightService.listRoleRightByRoleId(roleId);
        if (CollectionUtil.isNotEmpty(sysRoleRightDtoList)) {
            Right right;
            for (SysRoleRightDto sysRoleRightDto : sysRoleRightDtoList) {
                String uri = StrUtil.trimToEmpty(sysRoleRightDto.getUri());
                right = new Right();
                right.setRightId(sysRoleRightDto.getRightId());
                right.setRightType(sysRoleRightDto.getRightType());
                right.setPathMethod(sysRoleRightDto.getPathMethod());
                List<Right> rightList = new ArrayList<>();
                if (rightMap.containsKey(uri)) {
                    rightList = rightMap.get(uri);
                }
                rightList.add(right);
                rightMap.put(uri, rightList);
            }
        }
        redisRepository.del(RedisRightConstants.getRoleRightKey(roleId));
        redisRepository.set(RedisRightConstants.getRoleRightKey(roleId), rightMap);
    }

    /**
     * 异步删除角色权限
     *
     * @param roleId
     * @return
     * @author zgpi
     * @date 2020/6/15 19:25
     **/
    @Async
    @Override
    public void delRoleRightRedis(Long roleId) {
        redisRepository.del(RedisRightConstants.getRoleRightKey(roleId));
    }

    /**
     * 异步删除redis中用户的范围权限
     *
     * @param corpId
     * @param userIdList
     * @param rightIdList
     * @return
     * @author zgpi
     * @date 2020/6/15 17:11
     **/
    @Async
    @Override
    public void delRoleUserScopeRightKey(Long corpId, List<Long> userIdList, List<Long> rightIdList) {
        sysUserRightScopeCompoService.delUserRightScope(userIdList, rightIdList, corpId);
    }

    /**
     * 删除redis中用户的范围权限
     *
     * @param corpId
     * @param userId
     * @param rightIdList
     * @return
     * @author zgpi
     * @date 2020/6/15 20:16
     **/
    @Async
    @Override
    public void delRoleUserScopeRightKey(Long corpId, Long userId, List<Long> rightIdList) {
        if (LongUtil.isZero(corpId) || LongUtil.isZero(userId) || CollectionUtil.isEmpty(rightIdList)) {
            for (Long rightId : rightIdList) {
                redisRepository.del(RedisRightConstants.getRightUserScope(corpId, userId, rightId));
            }
        }
    }

    /**
     * 异步删除redis中用户的范围权限
     *
     * @param corpId
     * @param roleId
     * @return
     * @author zgpi
     * @date 2020/6/15 17:11
     **/
    @Async
    @Override
    public void delRoleUserScopeRightKey(Long corpId, Long roleId) {
        List<SysRoleUser> sysRoleUserList = sysRoleUserService.list(new QueryWrapper<SysRoleUser>()
                .eq("role_id", roleId));
        List<SysRight> sysRightList = sysRightService.list(new QueryWrapper<SysRight>()
                .eq("has_scope", "Y"));
        List<Long> userIdList = sysRoleUserList.stream().map(e -> e.getUserId()).collect(Collectors.toList());
        List<Long> rightIdList = sysRightList.stream().map(e -> e.getRightId()).collect(Collectors.toList());
        sysUserRightScopeCompoService.delUserRightScope(userIdList, rightIdList, corpId);
    }

    /**
     * 异步删除redis中用户的角色
     *
     * @param corpId
     * @param roleId
     * @param userIdList
     * @return
     * @author zgpi
     * @date 2020/6/15 20:09
     **/
    @Async
    @Override
    public void delUserRoleRedis(Long corpId, Long roleId, List<Long> userIdList) {
        if (LongUtil.isZero(corpId) || LongUtil.isZero(roleId) || CollectionUtil.isEmpty(userIdList)) {
            return;
        }
        if (CollectionUtil.isNotEmpty(userIdList)) {
            for (Long userId : userIdList) {
                List<Long> roleIdList = (List<Long>) redisRepository.get(RedisRightConstants.getUserRoleKey(userId, corpId));
                if (CollectionUtil.isNotEmpty(roleIdList)) {
                    roleIdList.remove(roleId);
                }
                redisRepository.setExpire(RedisRightConstants.getUserRoleKey(userId, corpId), roleIdList,
                        USER_ROLE_REDIS_EXPIRE);
            }
        }
    }

    /**
     * 删除redis中用户的角色
     *
     * @param corpId
     * @param userId
     * @return
     * @author zgpi
     * @date 2020/6/15 20:09
     **/
    @Override
    public void delUserRoleRedis(Long corpId, Long userId) {
        redisRepository.del(RedisRightConstants.getUserRoleKey(userId, corpId));
    }

    /**
     * 添加redis中用户的角色
     *
     * @param corpId
     * @param userId
     * @param roleId
     * @return
     * @author zgpi
     * @date 2020/6/15 19:28
     **/
    @Override
    public void addUserRoleRedis(Long corpId, Long userId, Long roleId) {
        if (LongUtil.isZero(corpId) || LongUtil.isZero(userId) || LongUtil.isZero(roleId)) {
            return;
        }
        List<Long> roleIdList = (List<Long>) redisRepository.get(RedisRightConstants.getUserRoleKey(userId, corpId));
        if (CollectionUtil.isEmpty(roleIdList)) {
            roleIdList = new ArrayList<>();
        }
        roleIdList.add(roleId);
        redisRepository.setExpire(RedisRightConstants.getUserRoleKey(userId, corpId), roleIdList,
                USER_ROLE_REDIS_EXPIRE);
    }

    /**
     * 添加redis中用户的角色列表
     *
     * @param corpId
     * @param userId
     * @param roleIdList
     * @return
     * @author zgpi
     * @date 2020/6/15 19:58
     **/
    @Override
    public void addUserRoleRedis(Long corpId, Long userId, List<Long> roleIdList) {
        if (LongUtil.isZero(corpId) || LongUtil.isZero(userId) || CollectionUtil.isEmpty(roleIdList)) {
            return;
        }
        List<Long> list = (List<Long>) redisRepository.get(RedisRightConstants.getUserRoleKey(userId, corpId));
        if (CollectionUtil.isEmpty(list)) {
            list = new ArrayList<>();
        }
        list.addAll(roleIdList);
        redisRepository.setExpire(RedisRightConstants.getUserRoleKey(userId, corpId), roleIdList,
                USER_ROLE_REDIS_EXPIRE);
    }

    /**
     * 初始化redis用户企业角色
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/15 20:04
     **/
    @Override
    public void initUserCorpRoleRedis(Long userId, Long corpId) {
        List<SysRole> roleList = sysRoleService.findByUserAndCorp(corpId, userId);
        List<Long> roleIdList = roleList.stream().map(e -> e.getRoleId()).collect(Collectors.toList());
        redisRepository.del(RedisRightConstants.getUserRoleKey(userId, corpId));
        redisRepository.setExpire(RedisRightConstants.getUserRoleKey(userId, corpId), roleIdList,
                USER_ROLE_REDIS_EXPIRE);
    }

    /**
     * 初始化系统公共权限
     *
     * @param
     * @return
     * @author zgpi
     * @date 2020/6/8 15:22
     **/
    @Async
    @Override
    public void initSysCommonRightRedis() {
        sysRightCompoService.initSysCommonRightRedis();
    }

    /**
     * 初始化角色权限redis
     *
     * @return
     * @author zgpi
     * @date 2020/6/8 12:19
     **/
    @Async
    @Override
    public void initRoleRightRedis() {
        sysRoleRightCompoService.initRoleRightRedis();
    }

    /**
     * 初始化租户redis
     *
     * @return
     * @author zgpi
     * @date 2020/6/17 16:37
     **/
    @Async
    @Override
    public void initSysTenantRedis() {
        sysTenantCompoService.initSysTenantRedis();
    }

    /**
     * 新增租户redis
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/19 14:26
     **/
    @Override
    public void addSysTenantRedis(Long corpId) {
        sysTenantCompoService.addSysTenantRedis(corpId);
    }

    /**
     * 修改租户redis
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/19 14:27
     **/
    @Override
    public void updateSysTenantRedis(Long corpId) {
        sysTenantCompoService.updateSysTenantRedis(corpId);
    }

    /**
     * 初始化租户权限redis
     *
     * @return
     * @author zgpi
     * @date 2020/6/17 16:44
     **/
    @Async
    @Override
    public void initTenantRightRedis() {
        sysTenantCompoService.initTenantRightRedis();
    }

    /**
     * 初始化企业系统管理员角色人员列表redis
     *
     * @return
     * @author zgpi
     * @date 2020/6/17 19:43
     **/
    @Async
    @Override
    public void initCorpSysRoleUserListRedis() {
        sysRoleUserCompoService.initCorpSysRoleUserListRedis();
    }

    /**
     * 初始化企业系统管理员角色人员列表redis
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/17 20:44
     **/
    @Override
    public void initCorpSysRoleUserListRedis(Long corpId) {
        sysRoleUserCompoService.initCorpSysRoleUserListRedis(corpId);
    }

    /**
     * 删除企业系统管理员角色人员redis
     *
     * @param corpId
     * @param userId
     * @return
     * @author zgpi
     * @date 2020/6/17 20:52
     **/
    @Override
    public void delCorpSysRoleUserListRedis(Long corpId, Long userId) {
        List<Long> userIdList = (List<Long>) redisRepository.get(RedisRightConstants.getCorpSysRoleUserKey(corpId));
        if (CollectionUtil.isNotEmpty(userIdList)) {
            userIdList.remove(userId);
            redisRepository.set(RedisRightConstants.getCorpSysRoleUserKey(corpId), userIdList);
        }
    }

}
