package com.zjft.usp.uas.right.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.common.constant.RedisRightConstants;
import com.zjft.usp.common.model.Right;
import com.zjft.usp.redis.template.RedisRepository;
import com.zjft.usp.uas.right.composite.SysRoleRightCompoService;
import com.zjft.usp.uas.right.dto.SysRoleRightDto;
import com.zjft.usp.uas.right.model.SysRoleUser;
import com.zjft.usp.uas.right.service.SysRoleRightService;
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
 * 角色权限聚合实现类
 *
 * @author zgpi
 * @date 2020/5/28 19:47
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleRightCompoServiceImpl implements SysRoleRightCompoService {

    @Autowired
    private SysRoleRightService sysRoleRightService;
    @Autowired
    private SysRoleUserService sysRoleUserService;

    @Resource
    private RedisRepository redisRepository;

    /**
     * 初始化用户对应的角色权限redis
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2020/5/28 19:54
     **/
    @Override
    public void initUserRoleRightRedis(Long userId) {
        List<SysRoleRightDto> sysRoleRightDtoList = this.listRoleRightByUserId(userId);
        if (CollectionUtil.isNotEmpty(sysRoleRightDtoList)) {
            Map<Long, Map<String, List<Right>>> roleIdAndRightListMap = new HashMap<>();
            Right right;
            for (SysRoleRightDto sysRoleRightDto : sysRoleRightDtoList) {
                Long roleId = sysRoleRightDto.getRoleId();
                String uri = StrUtil.trimToEmpty(sysRoleRightDto.getUri());
                right = new Right();
                right.setRightId(sysRoleRightDto.getRightId());
                right.setRightType(sysRoleRightDto.getRightType());
                right.setPathMethod(sysRoleRightDto.getPathMethod());
                Map<String, List<Right>> rightMap = new HashMap<>();
                if (roleIdAndRightListMap.containsKey(roleId)) {
                    rightMap = roleIdAndRightListMap.get(roleId);
                    List<Right> rightList = new ArrayList<>();
                    if (rightMap.containsKey(uri)) {
                        rightList = rightMap.get(uri);
                    }
                    rightList.add(right);
                    rightMap.put(uri, rightList);
                }
                roleIdAndRightListMap.put(roleId, rightMap);
            }
            for (Long roleId : roleIdAndRightListMap.keySet()) {
                // 不存在，则新增
                Object rightValue = redisRepository.get(RedisRightConstants.getRoleRightKey(roleId));
                if (rightValue == null) {
                    redisRepository.set(RedisRightConstants.getRoleRightKey(roleId), roleIdAndRightListMap.get(roleId));
                }
            }
        }
    }

    /**
     * 初始化角色权限redis
     *
     * @return
     * @author zgpi
     * @date 2020/6/8 12:19
     **/
    @Override
    public void initRoleRightRedis() {
        String flag = (String) redisRepository.get(RedisRightConstants.getRightRoleInit());
        if ("Y".equalsIgnoreCase(StrUtil.trimToEmpty(flag))) {
            return;
        }
        List<SysRoleRightDto> sysRoleRightDtoList = sysRoleRightService.listRoleRight();
        if (CollectionUtil.isNotEmpty(sysRoleRightDtoList)) {
            Map<Long, Map<String, List<Right>>> roleIdAndRightListMap = new HashMap<>();
            Right right;
            for (SysRoleRightDto sysRoleRightDto : sysRoleRightDtoList) {
                Long roleId = sysRoleRightDto.getRoleId();
                String uri = StrUtil.trimToEmpty(sysRoleRightDto.getUri());
                right = new Right();
                right.setRightId(sysRoleRightDto.getRightId());
                right.setRightType(sysRoleRightDto.getRightType());
                right.setPathMethod(sysRoleRightDto.getPathMethod());
                Map<String, List<Right>> rightMap = new HashMap<>();
                if (roleIdAndRightListMap.containsKey(roleId)) {
                    rightMap = roleIdAndRightListMap.get(roleId);
                    List<Right> rightList = new ArrayList<>();
                    if (rightMap.containsKey(uri)) {
                        rightList = rightMap.get(uri);
                    }
                    rightList.add(right);
                    rightMap.put(uri, rightList);
                }
                roleIdAndRightListMap.put(roleId, rightMap);
            }
            for (Long roleId : roleIdAndRightListMap.keySet()) {
                redisRepository.set(RedisRightConstants.getRoleRightKey(roleId), roleIdAndRightListMap.get(roleId));
            }
        }
        redisRepository.set(RedisRightConstants.getRightRoleInit(), "Y");
    }

    /**
     * 获得用户的角色权限列表
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2020/5/28 19:46
     **/
    @Override
    public List<SysRoleRightDto> listRoleRightByUserId(Long userId) {
        List<SysRoleUser> sysRoleUserList = sysRoleUserService
                .list(new QueryWrapper<SysRoleUser>().eq("user_id", userId));
        List<Long> roleIdList = sysRoleUserList.stream().map(e -> e.getRoleId()).collect(Collectors.toList());
        List<SysRoleRightDto> sysRoleRightDtoList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(roleIdList)) {
            sysRoleRightDtoList = sysRoleRightService.listRoleRightByRoleIdList(roleIdList);
        }
        return sysRoleRightDtoList;
    }

    /**
     * 初始化角色权限redis
     *
     * @param roleId
     * @return
     * @author zgpi
     * @date 2020/6/15 19:40
     **/
    @Override
    public void initRoleRightRedis(Long roleId) {
        List<SysRoleRightDto> sysRoleRightDtoList = sysRoleRightService.listRoleRightByRoleId(roleId);
        if (CollectionUtil.isNotEmpty(sysRoleRightDtoList)) {
            Right right;
            Map<String, List<Right>> rightMap = new HashMap<>();
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
            redisRepository.set(RedisRightConstants.getRoleRightKey(roleId), rightMap);
        }
    }
}
