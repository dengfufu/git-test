package com.zjft.usp.uas.right.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.common.constant.RedisRightConstants;
import com.zjft.usp.common.dto.RightScopeDto;
import com.zjft.usp.common.enums.RightScopeTypeEnum;
import com.zjft.usp.common.model.Right;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.redis.template.RedisRepository;
import com.zjft.usp.uas.right.composite.SysUserRightCompoService;
import com.zjft.usp.uas.right.composite.SysUserRightScopeCompoService;
import com.zjft.usp.uas.right.dto.SysRoleRightDto;
import com.zjft.usp.uas.right.dto.SysUserFullRightDto;
import com.zjft.usp.uas.right.dto.SysUserRightScopeDto;
import com.zjft.usp.uas.right.filter.SysRightFilter;
import com.zjft.usp.uas.right.model.SysRight;
import com.zjft.usp.uas.right.model.SysRole;
import com.zjft.usp.uas.right.model.SysRoleUser;
import com.zjft.usp.uas.right.model.SysTenant;
import com.zjft.usp.uas.right.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 人员权限聚合服务实现类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/12/13 15:44
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserRightCompoServiceImpl implements SysUserRightCompoService {

    @Autowired
    private SysRoleRightService sysRoleRightService;
    @Autowired
    private SysRightService sysRightService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRoleUserService sysRoleUserService;
    @Autowired
    private SysTenantService sysTenantService;
    @Autowired
    private SysUserRightScopeCompoService sysUserRightScopeCompoService;

    @Resource
    private RedisRepository redisRepository;

    /**
     * 获得用户权限编号列表
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/12/16 11:29
     **/
    @Override
    public List<Long> listUserRightId(Long userId, Long corpId) {
        if (LongUtil.isZero(userId) || LongUtil.isZero(corpId)) {
            return new ArrayList<>();
        }
        // 1. 获取用户已经配置的权限
        List<Long> rightIdList = sysRoleRightService.listRightIdByUserId(userId, corpId);
        // 2. 加上企业的公共权限
        List<SysRight> corpCommonRightList = sysRightService.listCommonByCorpId(corpId);
        if (corpCommonRightList.size() > 0) {
            List<Long> corpCommonRightIdList = corpCommonRightList.stream().map(e -> e.getRightId()).collect(Collectors.toList());
            rightIdList.addAll(corpCommonRightIdList);
        }
        // 3. 判断角色是企业的系统管理员，自动加上系统权限
        SysTenant sysTenant = sysTenantService.getById(corpId);
        List<SysRole> sysRoleList = sysRoleService.findByUserAndCorp(corpId, userId);
        for (SysRole sysRole : sysRoleList) {
            if (sysRole != null && sysRole.getSysType() == 1) {
                SysRightFilter sysRightFilter = new SysRightFilter();
                if (sysTenant != null) {
                    sysRightFilter.setServiceDemander(sysTenant.getServiceDemander());
                    sysRightFilter.setServiceProvider(sysTenant.getServiceProvider());
                    sysRightFilter.setDeviceUser(sysTenant.getDeviceUser());
                    sysRightFilter.setCloudManager(sysTenant.getCloudManager());
                }
                List<SysRoleRightDto> sysRoleRightDtoList = sysRoleRightService.listSysAuthRight(sysRightFilter);
                List<Long> sysRightIdList = sysRoleRightDtoList.stream().map(e -> e.getRightId())
                        .collect(Collectors.toList());
                rightIdList.addAll(sysRightIdList);
                break;
            }
        }
        // 去重返回
        rightIdList.stream().distinct().collect(Collectors.toList());
        return rightIdList;
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
        List<Long> userIdList = new ArrayList<>();
        List<Long> rightIdList = this.listTenantRightId(corpId);
        // 该企业没有这个权限
        if (CollectionUtil.isEmpty(rightIdList) || !rightIdList.contains(rightId)) {
            return userIdList;
        }
        // 系统管理员用户列表
        List<SysRole> sysRoleList = sysRoleService.list(new QueryWrapper<SysRole>()
                .eq("corp_id", corpId).eq("sys_type", 1));
        List<Long> roleIdList = sysRoleList.stream().map(e -> e.getRoleId()).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(sysRoleList)) {
            List<SysRoleUser> sysRoleUserList = sysRoleUserService.list(new QueryWrapper<SysRoleUser>()
                    .in("role_id", roleIdList));
            List<Long> sysRoleUserIdList = sysRoleUserList.stream().map(e -> e.getUserId()).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(sysRoleUserIdList)) {
                userIdList.addAll(sysRoleUserIdList);
            }
        }
        // 授权的用户列表
        List<Long> rightUserIdList = this.sysRoleRightService.listUserByRightId(rightId, corpId);
        if (CollectionUtil.isNotEmpty(rightUserIdList)) {
            userIdList.addAll(rightUserIdList);
        }
        return userIdList;
    }

    /**
     * 获得某权限的用户编号列表（不包含系统管理员用户）
     *
     * @param rightId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/7/6 18:47
     **/
    @Override
    public List<Long> listUserByRightIdNoSysUser(Long rightId, Long corpId) {
        List<Long> userIdList = new ArrayList<>();
        List<Long> rightIdList = this.listTenantRightId(corpId);
        // 该企业没有这个权限
        if (CollectionUtil.isEmpty(rightIdList) || !rightIdList.contains(rightId)) {
            return userIdList;
        }
        // 授权的用户列表
        List<Long> rightUserIdList = this.sysRoleRightService.listUserByRightId(rightId, corpId);
        if (CollectionUtil.isNotEmpty(rightUserIdList)) {
            userIdList.addAll(rightUserIdList);
        }
        return userIdList;
    }

    /**
     * 查询权限，包含范围权限
     *
     * @param sysRightFilter
     * @return
     * @author zgpi
     * @date 2020/6/12 10:17
     **/
    @Override
    public List<SysUserFullRightDto> listFullUserRight(SysRightFilter sysRightFilter) {
        List<SysUserFullRightDto> list = new ArrayList<>();
        Long corpId = sysRightFilter.getCorpId();
        Long userId = sysRightFilter.getUserId();
        if (LongUtil.isZero(corpId) || LongUtil.isZero(userId)) {
            return list;
        }
        List<Long> userIdList = (List<Long>) redisRepository.get(RedisRightConstants.getCorpSysRoleUserKey(corpId));
        // 是系统管理员
        if (CollectionUtil.isNotEmpty(userIdList) && userIdList.contains(userId)) {
            SysTenant sysTenant = sysTenantService.getById(corpId);
            if (sysTenant != null) {
                sysRightFilter.setServiceDemander(sysTenant.getServiceDemander());
                sysRightFilter.setServiceProvider(sysTenant.getServiceProvider());
                sysRightFilter.setDeviceUser(sysTenant.getDeviceUser());
                sysRightFilter.setCloudManager(sysTenant.getCloudManager());
            }
            List<SysRoleRightDto> sysRoleRightDtoList = sysRoleRightService.listSysAuthRight(sysRightFilter);
            if (CollectionUtil.isNotEmpty(sysRoleRightDtoList)) {
                for (SysRoleRightDto sysRoleRightDto : sysRoleRightDtoList) {
                    Long rightId = sysRoleRightDto.getRightId();
                    SysUserFullRightDto sysUserFullRightDto = new SysUserFullRightDto();
                    BeanUtils.copyProperties(sysRoleRightDto, sysUserFullRightDto);
                    if ("Y".equalsIgnoreCase(sysRoleRightDto.getHasScope())) {
                        List<RightScopeDto> rightScopeDtoList = sysUserRightScopeCompoService
                                .listFinalUserRightScope(userId, corpId, rightId);
                        SysUserRightScopeDto sysUserRightScopeDto;
                        List<SysUserRightScopeDto> sysUserScopeRightDtoList = new ArrayList<>();
                        if (CollectionUtil.isNotEmpty(rightScopeDtoList)) {
                            for (RightScopeDto rightScopeDto : rightScopeDtoList) {
                                sysUserRightScopeDto = new SysUserRightScopeDto();
                                sysUserRightScopeDto.setUserId(userId);
                                sysUserRightScopeDto.setCorpId(corpId);
                                sysUserRightScopeDto.setRightId(rightId);
                                sysUserRightScopeDto.setScopeType(rightScopeDto.getScopeType());
                                if (RightScopeTypeEnum.SERVICE_BRANCH.getCode().equals(rightScopeDto.getScopeType())) {
                                    // 兼容数据报表，如果是服务网点的范围，则返回1
                                    sysUserRightScopeDto.setScopeType(1);
                                }
                                sysUserRightScopeDto.setHasAllScope(StrUtil.trimToEmpty(rightScopeDto.getHasAllScope()));
                                sysUserRightScopeDto.setHasOwnScope(StrUtil.trimToEmpty(rightScopeDto.getHasOwnScope()));
                                sysUserRightScopeDto.setHasOwnLowerScope(StrUtil.trimToEmpty(rightScopeDto.getHasOwnLowerScope()));
                                sysUserRightScopeDto.setIdList(CollectionUtil.newArrayList(rightScopeDto.getOrgIdList()));
                                if ("Y".equalsIgnoreCase(sysUserRightScopeDto.getHasAllScope())) {
                                    sysUserRightScopeDto.setScopeRightType(1);
                                } else if (CollectionUtil.isNotEmpty(sysUserRightScopeDto.getIdList())) {
                                    sysUserRightScopeDto.setScopeRightType(2);
                                } else {
                                    sysUserRightScopeDto.setScopeRightType(3);
                                }
                                sysUserRightScopeDto.setContainLower("");
                                sysUserRightScopeDto.setOrgNames("");
                                sysUserRightScopeDto.setOrgIdList(new ArrayList<>());
                                sysUserScopeRightDtoList.add(sysUserRightScopeDto);
                            }
                        }
                        sysUserFullRightDto.setScopeTypeList(sysUserScopeRightDtoList);
                    }
                    list.add(sysUserFullRightDto);
                }
            }
            return list;
        }
        // 非系统管理员
        // 获取权限点
        List<SysRight> sysRightList = sysRightService.listUserRightByApp(sysRightFilter);
        // 获取范围权限
        for (SysRight sysRight : sysRightList) {
            Long rightId = sysRight.getRightId();
            SysUserFullRightDto sysUserFullRightDto = new SysUserFullRightDto();
            BeanUtils.copyProperties(sysRight, sysUserFullRightDto);
            if ("Y".equalsIgnoreCase(sysRight.getHasScope())) {
                List<RightScopeDto> rightScopeDtoList = sysUserRightScopeCompoService
                        .listFinalUserRightScope(userId, corpId, rightId);
                SysUserRightScopeDto sysUserRightScopeDto;
                List<SysUserRightScopeDto> sysUserScopeRightDtoList = new ArrayList<>();
                if (CollectionUtil.isNotEmpty(rightScopeDtoList)) {
                    for (RightScopeDto rightScopeDto : rightScopeDtoList) {
                        sysUserRightScopeDto = new SysUserRightScopeDto();
                        sysUserRightScopeDto.setUserId(userId);
                        sysUserRightScopeDto.setCorpId(corpId);
                        sysUserRightScopeDto.setRightId(rightId);
                        sysUserRightScopeDto.setScopeType(rightScopeDto.getScopeType());
                        if (RightScopeTypeEnum.SERVICE_BRANCH.getCode().equals(rightScopeDto.getScopeType())) {
                            // 兼容数据报表，如果是服务网点的范围，则返回1
                            sysUserRightScopeDto.setScopeType(1);
                        }
                        sysUserRightScopeDto.setHasAllScope(StrUtil.trimToEmpty(rightScopeDto.getHasAllScope()));
                        sysUserRightScopeDto.setHasOwnScope(StrUtil.trimToEmpty(rightScopeDto.getHasOwnScope()));
                        sysUserRightScopeDto.setHasOwnLowerScope(StrUtil.trimToEmpty(rightScopeDto.getHasOwnLowerScope()));
                        sysUserRightScopeDto.setIdList(CollectionUtil.newArrayList(rightScopeDto.getOrgIdList()));

                        if ("Y".equalsIgnoreCase(sysUserRightScopeDto.getHasAllScope())) {
                            sysUserRightScopeDto.setScopeRightType(1);
                        } else if (CollectionUtil.isNotEmpty(sysUserRightScopeDto.getIdList())) {
                            sysUserRightScopeDto.setScopeRightType(2);
                        } else {
                            sysUserRightScopeDto.setScopeRightType(3);
                        }
                        sysUserRightScopeDto.setContainLower("");
                        sysUserRightScopeDto.setOrgNames("");
                        sysUserRightScopeDto.setOrgIdList(new ArrayList<>());
                        sysUserScopeRightDtoList.add(sysUserRightScopeDto);
                    }
                }
                sysUserFullRightDto.setScopeTypeList(sysUserScopeRightDtoList);
            }
            list.add(sysUserFullRightDto);
        }
        return list;
    }

    /**
     * 获得租户权限编号列表
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/7/6 18:48
     **/
    private List<Long> listTenantRightId(Long corpId) {
        List<Long> rightIdList = new ArrayList<>();
        if (LongUtil.isZero(corpId)) {
            return rightIdList;
        }
        String tenant = (String) redisRepository.get(RedisRightConstants.getCorpTenantKey(corpId));
        if (StrUtil.isNotBlank(tenant)) {
            if ("Y".equalsIgnoreCase(JsonUtil.parseString(tenant, "serviceDemander"))) {
                Map<String, List<Right>> rightMap = (Map<String, List<Right>>)
                        redisRepository.get(RedisRightConstants.getTenantRightKey("service_demander"));
                for (Map.Entry<String, List<Right>> entry : rightMap.entrySet()) {
                    if (CollectionUtil.isNotEmpty(entry.getValue())) {
                        rightIdList.addAll(entry.getValue().stream().map(e -> e.getRightId()).collect(Collectors.toList()));
                    }
                }
            }
            if ("Y".equalsIgnoreCase(JsonUtil.parseString(tenant, "serviceProvider"))) {
                Map<String, List<Right>> rightMap = (Map<String, List<Right>>)
                        redisRepository.get(RedisRightConstants.getTenantRightKey("service_provider"));
                for (Map.Entry<String, List<Right>> entry : rightMap.entrySet()) {
                    if (CollectionUtil.isNotEmpty(entry.getValue())) {
                        rightIdList.addAll(entry.getValue().stream().map(e -> e.getRightId()).collect(Collectors.toList()));
                    }
                }
            }
            if ("Y".equalsIgnoreCase(JsonUtil.parseString(tenant, "deviceUser"))) {
                Map<String, List<Right>> rightMap = (Map<String, List<Right>>)
                        redisRepository.get(RedisRightConstants.getTenantRightKey("device_user"));
                for (Map.Entry<String, List<Right>> entry : rightMap.entrySet()) {
                    if (CollectionUtil.isNotEmpty(entry.getValue())) {
                        rightIdList.addAll(entry.getValue().stream().map(e -> e.getRightId()).collect(Collectors.toList()));
                    }
                }
            }
            if ("Y".equalsIgnoreCase(JsonUtil.parseString(tenant, "cloudManager"))) {
                Map<String, List<Right>> rightMap = (Map<String, List<Right>>)
                        redisRepository.get(RedisRightConstants.getTenantRightKey("cloud_manager"));
                for (Map.Entry<String, List<Right>> entry : rightMap.entrySet()) {
                    if (CollectionUtil.isNotEmpty(entry.getValue())) {
                        rightIdList.addAll(entry.getValue().stream().map(e -> e.getRightId()).collect(Collectors.toList()));
                    }
                }
            }
        }
        return rightIdList;
    }
}
