package com.zjft.usp.uas.right.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.anyfix.service.AnyfixFeignService;
import com.zjft.usp.common.constant.RedisRightConstants;
import com.zjft.usp.common.dto.RightScopeDto;
import com.zjft.usp.common.enums.RightScopeTypeEnum;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.redis.template.RedisRepository;
import com.zjft.usp.uas.baseinfo.service.CfgAreaService;
import com.zjft.usp.uas.corp.service.CorpRegistryService;
import com.zjft.usp.uas.right.composite.SysUserRightScopeCompoService;
import com.zjft.usp.uas.right.dto.SysRoleUserDto;
import com.zjft.usp.uas.right.dto.SysUserDto;
import com.zjft.usp.uas.right.dto.SysUserRightScopeDto;
import com.zjft.usp.uas.right.filter.SysUserScopeRightFilter;
import com.zjft.usp.uas.right.model.*;
import com.zjft.usp.uas.right.service.*;
import org.springframework.beans.BeanUtils;
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
 * 人员范围权限聚合实现类
 *
 * @author zgpi
 * @date 2020/6/4 14:07
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserRightScopeCompoServiceImpl implements SysUserRightScopeCompoService {

    @Autowired
    private SysUserRightScopeService sysUserRightScopeService;
    @Autowired
    private SysUserRightScopeDetailService sysUserRightScopeDetailService;
    @Autowired
    private SysRoleUserService sysRoleUserService;
    @Autowired
    private SysRoleRightScopeService sysRoleRightScopeService;
    @Autowired
    private SysRoleRightScopeDetailService sysRoleRightScopeDetailService;

    @Autowired
    private SysTenantService sysTenantService;

    @Autowired
    private CfgAreaService cfgAreaService;
    @Autowired
    private CorpRegistryService corpRegistryService;

    @Resource
    private AnyfixFeignService anyfixFeignService;
    @Resource
    private RedisRepository redisRepository;

    /**
     * 人员范围权限列表
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/4 14:08
     **/
    @Override
    public List<SysUserRightScopeDto> listUserRightScope(Long userId, Long corpId) {
        List<SysUserRightScopeDto> sysUserRightScopeDtoList = new ArrayList<>();
        List<SysUserRightScope> sysUserRightScopeList = sysUserRightScopeService.list(new QueryWrapper<SysUserRightScope>()
                .eq("user_id", userId)
                .eq("corp_id", corpId));
        List<Long> idList = sysUserRightScopeList.stream().map(e -> e.getId()).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(idList)) {
            List<SysUserRightScopeDetail> sysUserRightScopeDetailList = sysUserRightScopeDetailService.list(
                    new QueryWrapper<SysUserRightScopeDetail>().in("id", idList));
            Map<Long, List<SysUserRightScopeDetail>> map = this.findIdAndScopeDetailMap(sysUserRightScopeDetailList);
            SysUserRightScopeDto sysUserRightScopeDto;
            for (SysUserRightScope entity : sysUserRightScopeList) {
                sysUserRightScopeDto = new SysUserRightScopeDto();
                BeanUtils.copyProperties(entity, sysUserRightScopeDto);
                List<SysUserRightScopeDetail> list = map.get(entity.getId());
                if (CollectionUtil.isNotEmpty(list)) {
                    List<String> orgIdList = list.stream().map(e -> e.getOrgId()).collect(Collectors.toList());
                    sysUserRightScopeDto.setOrgIdList(orgIdList);
                    sysUserRightScopeDto.setOrgNames(this.findOrgNames(orgIdList, sysUserRightScopeDto.getScopeType()));
                    sysUserRightScopeDto.setContainLower(list.get(0).getContainLower());
                }
                sysUserRightScopeDtoList.add(sysUserRightScopeDto);
            }
        }
        return sysUserRightScopeDtoList;
    }

    /**
     * 设置人员角色范围权限
     *
     * @param sysUserDto
     * @return
     * @author zgpi
     * @date 2020/6/4 14:33
     **/
    @Override
    public void setUserRightScope(SysUserDto sysUserDto) {
        // 删除redis的范围权限
        List<Long> userIdList = new ArrayList<>();
        userIdList.add(sysUserDto.getUserId());
        this.delUserRightScope(userIdList, sysUserDto.getCorpId());

        sysUserRightScopeService.delUserRightScopeByUserId(sysUserDto.getUserId(), sysUserDto.getCorpId());
        sysUserRightScopeDetailService.delUserRightScopeDetail(sysUserDto.getUserId(), sysUserDto.getCorpId());

        if (CollectionUtil.isNotEmpty(sysUserDto.getUserRightScopeList())) {
            SysUserRightScope sysUserRightScope;
            List<SysUserRightScope> sysUserRightScopeList = new ArrayList<>();
            SysUserRightScopeDetail sysUserRightScopeDetail;
            List<SysUserRightScopeDetail> sysUserRightScopeDetailList = new ArrayList<>();
            for (SysUserRightScopeDto sysUserRightScopeDto : sysUserDto.getUserRightScopeList()) {
                sysUserRightScope = new SysUserRightScope();
                sysUserRightScopeDto.setUserId(sysUserDto.getUserId());
                sysUserRightScopeDto.setCorpId(sysUserDto.getCorpId());
                BeanUtils.copyProperties(sysUserRightScopeDto, sysUserRightScope);
                sysUserRightScope.setId(KeyUtil.getId());
                sysUserRightScopeList.add(sysUserRightScope);
                // 指定组织
                if (CollectionUtil.isNotEmpty(sysUserRightScopeDto.getOrgIdList())) {
                    for (String orgId : sysUserRightScopeDto.getOrgIdList()) {
                        if (StrUtil.isNotBlank(orgId)) {
                            sysUserRightScopeDetail = new SysUserRightScopeDetail();
                            sysUserRightScopeDetail.setId(sysUserRightScope.getId());
                            sysUserRightScopeDetail.setOrgId(StrUtil.trimToEmpty(orgId));
                            sysUserRightScopeDetail.setContainLower(sysUserRightScopeDto.getContainLower());
                            sysUserRightScopeDetailList.add(sysUserRightScopeDetail);
                        }
                    }
                }
            }
            sysUserRightScopeService.saveBatch(sysUserRightScopeList);
            sysUserRightScopeDetailService.saveBatch(sysUserRightScopeDetailList);
        }
    }

    /**
     * 获得人员范围权限
     * 检查redis，若redis存在，从redis取，若不存在，则新增
     *
     * @param sysUserScopeRightFilter
     * @return
     * @author zgpi
     * @date 2020/6/5 10:07
     **/
    @Override
    public List<RightScopeDto> listUserRightScope(SysUserScopeRightFilter sysUserScopeRightFilter) {
        Long userId = sysUserScopeRightFilter.getUserId();
        Long corpId = sysUserScopeRightFilter.getCorpId();
        Long rightId = sysUserScopeRightFilter.getRightId();
        if (LongUtil.isZero(userId) || LongUtil.isZero(corpId) ||
                LongUtil.isZero(rightId)) {
            return null;
        }
        List<RightScopeDto> rightScopeDtoList;
        Object obj = redisRepository.get(RedisRightConstants.getRightUserScope(corpId, userId, rightId));
        if (obj != null && obj instanceof List) {
            rightScopeDtoList = (List<RightScopeDto>) obj;
        } else {
            rightScopeDtoList = this.initUserRightScope(sysUserScopeRightFilter);
            if (CollectionUtil.isNotEmpty(rightScopeDtoList)) {
                redisRepository.set(RedisRightConstants.getRightUserScope(corpId, userId, rightId), rightScopeDtoList);
            }
        }
        return rightScopeDtoList;
    }

    /**
     * 删除人员范围权限
     *
     * @param userIdList
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/5 11:05
     **/
    @Override
    public void delUserRightScope(List<Long> userIdList, Long corpId) {
        if (CollectionUtil.isEmpty(userIdList) || LongUtil.isZero(corpId)) {
            return;
        }
        List<Long> rightIdList = new ArrayList<>();
        List<SysRoleUserDto> sysRoleUserDtoList = sysRoleUserService.listRoleUserByUserIdList(corpId, userIdList);
        List<Long> roleIdList = sysRoleUserDtoList.stream().map(e -> e.getRoleId()).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(roleIdList)) {
            List<SysRoleRightScope> sysRoleRightScopeList = sysRoleRightScopeService
                    .list(new QueryWrapper<SysRoleRightScope>().in("role_id", roleIdList));
            if (CollectionUtil.isNotEmpty(sysRoleRightScopeList)) {
                rightIdList.addAll(sysRoleRightScopeList.stream().map(e -> e.getRightId()).collect(Collectors.toList()));
            }
        }
        List<SysUserRightScope> sysUserRightScopeList = sysUserRightScopeService
                .list(new QueryWrapper<SysUserRightScope>().in("user_id", userIdList)
                        .eq("corp_id", corpId));
        if (CollectionUtil.isNotEmpty(sysUserRightScopeList)) {
            rightIdList.addAll(sysUserRightScopeList.stream().map(e -> e.getRightId()).collect(Collectors.toList()));
        }
        for (Long userId : userIdList) {
            for (Long rightId : rightIdList) {
                redisRepository.del(RedisRightConstants.getRightUserScope(corpId, userId, rightId));
            }
        }
    }

    /**
     * 删除人员范围权限
     *
     * @param userIdList
     * @param rightIdList
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/5 11:16
     **/
    @Override
    public void delUserRightScope(List<Long> userIdList, List<Long> rightIdList, Long corpId) {
        if (CollectionUtil.isEmpty(userIdList) || CollectionUtil.isEmpty(rightIdList)) {
            return;
        }
        for (Long userId : userIdList) {
            for (Long rightId : rightIdList) {
                redisRepository.del(RedisRightConstants.getRightUserScope(corpId, userId, rightId));
            }
        }
    }

    /**
     * 初始化人员范围权限
     *
     * @param sysUserScopeRightFilter
     * @return
     * @author zgpi
     * @date 2020/6/4 19:48
     **/
    @Override
    public List<RightScopeDto> initUserRightScope(SysUserScopeRightFilter sysUserScopeRightFilter) {
        Long userId = sysUserScopeRightFilter.getUserId();
        Long corpId = sysUserScopeRightFilter.getCorpId();
        Long rightId = sysUserScopeRightFilter.getRightId();
        List<RightScopeDto> rightScopeDtoList = this.listFinalUserRightScope(userId, corpId, rightId);
        return rightScopeDtoList;
    }

    /**
     * 获得指定组织名称
     *
     * @param orgIdList
     * @param scopeType
     * @return
     * @author zgpi
     * @date 2020/6/3 19:54
     **/
    private String findOrgNames(List<String> orgIdList, Integer scopeType) {
        List<String> nameList = new ArrayList<>();
        if (RightScopeTypeEnum.SERVICE_BRANCH.getCode().equals(scopeType)) {
            Result<Map<Long, String>> serviceBranchMapResult = anyfixFeignService
                    .mapServiceBranchIdAndName(JsonUtil.toJson(orgIdList));
            Map<Long, String> serviceBranchMap = new HashMap<>();
            if (Result.isSucceed(serviceBranchMapResult)) {
                serviceBranchMap = serviceBranchMapResult.getData();
            }
            for (String orgId : orgIdList) {
                nameList.add(StrUtil.trimToEmpty(serviceBranchMap.get(Long.parseLong(orgId))));
            }
        } else if (RightScopeTypeEnum.PROVINCE.getCode().equals(scopeType)) {
            Map<String, String> areaMap = cfgAreaService.mapCodeAndNameByCodeList(orgIdList);
            for (String orgId : orgIdList) {
                nameList.add(StrUtil.trimToEmpty(areaMap.get(orgId)));
            }
        } else if (RightScopeTypeEnum.DEMANDER.getCode().equals(scopeType)) {
            List<Long> corpIdList = orgIdList.stream().map(e -> Long.parseLong(e)).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(corpIdList)) {
                Map<Long, String> corpMap = corpRegistryService.mapCorpIdAndShortName(corpIdList);
                for (String orgId : orgIdList) {
                    nameList.add(StrUtil.trimToEmpty(corpMap.get(Long.parseLong(orgId))));
                }
            }
        }
        return CollectionUtil.join(nameList, ",");
    }

    private Map<Long, List<SysUserRightScopeDetail>> findIdAndScopeDetailMap(List<SysUserRightScopeDetail> sysUserRightScopeDetailList) {
        Map<Long, List<SysUserRightScopeDetail>> map = new HashMap<>();
        if (CollectionUtil.isNotEmpty(sysUserRightScopeDetailList)) {
            for (SysUserRightScopeDetail sysUserRightScopeDetail : sysUserRightScopeDetailList) {
                List<SysUserRightScopeDetail> list = new ArrayList<>();
                if (map.containsKey(sysUserRightScopeDetail.getId())) {
                    list = map.get(sysUserRightScopeDetail.getId());
                }
                list.add(sysUserRightScopeDetail);
                map.put(sysUserRightScopeDetail.getId(), list);
            }
        }
        return map;
    }

    /**
     * 获得人员范围权限（角色范围+个人范围）
     *
     * @param userId
     * @param corpId
     * @param rightId
     * @return
     * @author zgpi
     * @date 2020/6/11 19:33
     **/
    @Override
    public List<RightScopeDto> listFinalUserRightScope(Long userId, Long corpId, Long rightId) {
        if (LongUtil.isZero(userId) || LongUtil.isZero(corpId) ||
                LongUtil.isZero(rightId)) {
            return null;
        }

        List<RightScopeDto> finalRightScopeList = new ArrayList<>();
        // 是系统管理员，可以查询所有数据
        List<Long> userIdList = (List<Long>) redisRepository.get(RedisRightConstants.getCorpSysRoleUserKey(corpId));
        if (CollectionUtil.isNotEmpty(userIdList) && userIdList.contains(userId)) {
            RightScopeDto rightScopeDto = new RightScopeDto();
            // 直接写的服务网点，后续要优化
            rightScopeDto.setScopeType(RightScopeTypeEnum.SERVICE_BRANCH.getCode());
            rightScopeDto.setHasAllScope("Y");
            finalRightScopeList.add(rightScopeDto);
            return finalRightScopeList;
        }
        String serviceProvider = "N";
        SysTenant sysTenant = sysTenantService.getById(corpId);
        if (sysTenant != null) {
            serviceProvider = StrUtil.trimToEmpty(sysTenant.getServiceProvider());
        }
        // 如果不是服务商，则可以查询所有数据
        if (!"Y".equalsIgnoreCase(serviceProvider)) {
            RightScopeDto rightScopeDto = new RightScopeDto();
            // 直接写的服务网点，后续要优化
            rightScopeDto.setScopeType(RightScopeTypeEnum.SERVICE_BRANCH.getCode());
            rightScopeDto.setHasAllScope("Y");
            finalRightScopeList.add(rightScopeDto);
            return finalRightScopeList;
        }

        List<RightScopeDto> rightScopeDtoList = new ArrayList<>();
        // 角色范围权限列表
        rightScopeDtoList.addAll(this.listRoleRightScope(userId, corpId, rightId));
        // 人员范围权限列表
        rightScopeDtoList.addAll(this.listUserRightScope(userId, corpId, rightId));

        // 范围类型与范围权限列表映射
        Map<Integer, List<RightScopeDto>> idAndRightScopeListMap = new HashMap<>();
        for (RightScopeDto rightScopeDto : rightScopeDtoList) {
            idAndRightScopeListMap.computeIfAbsent(rightScopeDto.getScopeType(),
                    k -> new ArrayList<>()).add(rightScopeDto);
        }

        RightScopeDto finalRightScope;
        for (Map.Entry<Integer, List<RightScopeDto>> entry : idAndRightScopeListMap.entrySet()) {
            Integer scopeType = entry.getKey();
            List<RightScopeDto> scopeList = entry.getValue();
            String hasAllScope = ""; // 是否有全部权限
            String hasOwnScope = ""; // 是否有所在组织权限
            String hasOwnLowerScope = ""; // 是否有所在组织下级权限
            List<String> orgIdList = new ArrayList<>(); // 指定组织ID列表（所有设置的范围）
            List<String> needLowerOrgIdList = new ArrayList<>(); // 需要查询下级的组织ID列表

            for (RightScopeDto rightScopeDto : scopeList) {
                hasAllScope = "Y".equalsIgnoreCase(rightScopeDto.getHasAllScope()) ? "Y" : hasAllScope;
                hasOwnScope = "Y".equalsIgnoreCase(rightScopeDto.getHasOwnScope()) ? "Y" : hasOwnScope;
                hasOwnLowerScope = "Y".equalsIgnoreCase(rightScopeDto.getHasOwnLowerScope()) ? "Y" : hasOwnLowerScope;
                if (CollectionUtil.isNotEmpty(rightScopeDto.getOrgIdList())) {
                    orgIdList.addAll(rightScopeDto.getOrgIdList());
                }
                // 包含下级
                if ("Y".equalsIgnoreCase(rightScopeDto.getContainLower())
                        && CollectionUtil.isNotEmpty(rightScopeDto.getOrgIdList())) {
                    needLowerOrgIdList.addAll(rightScopeDto.getOrgIdList());
                }
            }
            finalRightScope = new RightScopeDto();
            finalRightScope.setScopeType(scopeType);
            finalRightScope.setHasAllScope(hasAllScope);
            finalRightScope.setHasOwnScope(hasOwnScope);
            finalRightScope.setHasOwnLowerScope(hasOwnLowerScope);
            // 服务网点范围，有上下级关系
            if (RightScopeTypeEnum.SERVICE_BRANCH.getCode().equals(scopeType)) {
                Result<List<Long>> serviceBranchResult = Result.succeed();
                if ("Y".equalsIgnoreCase(hasOwnScope) && "Y".equalsIgnoreCase(hasOwnLowerScope)) {
                    // 所在网点及下级网点
                    serviceBranchResult = anyfixFeignService.listOwnAndLowerBranchId(userId, corpId);
                } else if ("Y".equalsIgnoreCase(hasOwnScope) && !"Y".equalsIgnoreCase(hasOwnLowerScope)) {
                    // 所在网点
                    serviceBranchResult = anyfixFeignService.listOwnBranchId(userId, corpId);
                } else if (!"Y".equalsIgnoreCase(hasOwnScope) && "Y".equalsIgnoreCase(hasOwnLowerScope)) {
                    // 下级网点
                    serviceBranchResult = anyfixFeignService.listOwnLowerBranchId(userId, corpId);
                } else {
                    // 无范围
                }
                if (Result.isSucceed(serviceBranchResult)) {
                    List<Long> idList = serviceBranchResult.getData();
                    if (CollectionUtil.isNotEmpty(idList)) {
                        orgIdList.addAll(idList.stream().map(e -> String.valueOf(e)).collect(Collectors.toList()));
                    }
                }
                if (CollectionUtil.isNotEmpty(needLowerOrgIdList)) {
                    Result<List<Long>> lowerOrgIdListResult = anyfixFeignService.listLowerBranchId(JsonUtil.toJson(needLowerOrgIdList));
                    if (Result.isSucceed(lowerOrgIdListResult)) {
                        List<Long> branchIdList = lowerOrgIdListResult.getData();
                        if (CollectionUtil.isNotEmpty(branchIdList)) {
                            orgIdList.addAll(branchIdList.stream().map(e -> String.valueOf(e)).collect(Collectors.toList()));
                        }
                    }
                }
            }
            finalRightScope.setOrgIdList(orgIdList);
            finalRightScopeList.add(finalRightScope);
        }
        return finalRightScopeList;
    }

    private List<RightScopeDto> listRoleRightScope(Long userId, Long corpId, Long rightId) {
        List<RightScopeDto> rightScopeDtoList = new ArrayList<>();
        // 处理一个用户多角色的范围权限问题
        List<Long> roleIdList = sysRoleUserService.listRoleIdByUserAndCorp(userId, corpId);
        Map<Long, List<SysRoleRightScopeDetail>> idAndRoleRightListMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(roleIdList)) {
            List<SysRoleRightScope> sysRoleRightScopeList = sysRoleRightScopeService
                    .list(new QueryWrapper<SysRoleRightScope>()
                            .in("role_id", roleIdList)
                            .eq("right_id", rightId));
            List<Long> idList = sysRoleRightScopeList.stream().map(e -> e.getId()).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(idList)) {
                List<SysRoleRightScopeDetail> sysRoleRightScopeDetailList = sysRoleRightScopeDetailService.list(
                        new QueryWrapper<SysRoleRightScopeDetail>().in("id", idList));
                for (SysRoleRightScopeDetail sysRoleRightScopeDetail : sysRoleRightScopeDetailList) {
                    idAndRoleRightListMap.computeIfAbsent(sysRoleRightScopeDetail.getId(),
                            k -> new ArrayList<>()).add(sysRoleRightScopeDetail);
                }
            }
            RightScopeDto rightScopeDto;
            for (SysRoleRightScope sysRoleRightScope : sysRoleRightScopeList) {
                rightScopeDto = new RightScopeDto();
                BeanUtils.copyProperties(sysRoleRightScope, rightScopeDto);
                List<SysRoleRightScopeDetail> detailList = idAndRoleRightListMap.get(sysRoleRightScope.getId());
                if (CollectionUtil.isNotEmpty(detailList)) {
                    rightScopeDto.setOrgIdList(detailList.stream().map(e -> e.getOrgId()).collect(Collectors.toList()));
                    rightScopeDto.setContainLower(detailList.get(0).getContainLower());
                }
                rightScopeDtoList.add(rightScopeDto);
            }
        }
        return rightScopeDtoList;
    }

    private List<RightScopeDto> listUserRightScope(Long userId, Long corpId, Long rightId) {
        List<SysUserRightScope> sysUserRightScopeList = sysUserRightScopeService
                .list(new QueryWrapper<SysUserRightScope>()
                        .eq("user_id", userId)
                        .eq("corp_id", corpId)
                        .eq("right_id", rightId));
        List<Long> idList = sysUserRightScopeList.stream().map(e -> e.getId()).collect(Collectors.toList());
        Map<Long, List<SysUserRightScopeDetail>> idAndUserRightListMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(idList)) {
            List<SysUserRightScopeDetail> sysUserRightScopeDetailList = sysUserRightScopeDetailService.list(
                    new QueryWrapper<SysUserRightScopeDetail>().in("id", idList));
            for (SysUserRightScopeDetail sysUserRightScopeDetail : sysUserRightScopeDetailList) {
                idAndUserRightListMap.computeIfAbsent(sysUserRightScopeDetail.getId(),
                        k -> new ArrayList<>()).add(sysUserRightScopeDetail);
            }
        }

        List<String> demanderCorpList = new ArrayList<>();
        Result<List<Long>> corpIdListResult = anyfixFeignService.listDemanderCorpByManager(corpId, userId);
        if (Result.isSucceed(corpIdListResult) && CollectionUtil.isNotEmpty(corpIdListResult.getData())) {
            demanderCorpList = corpIdListResult.getData().stream().map(e -> String.valueOf(e)).collect(Collectors.toList());
        }

        RightScopeDto rightScopeDto;
        List<RightScopeDto> rightScopeDtoList = new ArrayList<>();
        boolean hasDemander = false;
        for (SysUserRightScope sysUserRightScope : sysUserRightScopeList) {
            rightScopeDto = new RightScopeDto();
            BeanUtils.copyProperties(sysUserRightScope, rightScopeDto);
            List<SysUserRightScopeDetail> detailList = idAndUserRightListMap.get(sysUserRightScope.getId());
            if (CollectionUtil.isNotEmpty(detailList)) {
                rightScopeDto.setOrgIdList(detailList.stream().map(e -> e.getOrgId()).collect(Collectors.toList()));
                rightScopeDto.setContainLower(detailList.get(0).getContainLower());
            }
            rightScopeDtoList.add(rightScopeDto);
            // 有委托商范围
            if (RightScopeTypeEnum.DEMANDER.getCode().equals(rightScopeDto.getScopeType())) {
                List<String> orgIdList = CollectionUtil.newArrayList(rightScopeDto.getOrgIdList());
                orgIdList.addAll(demanderCorpList);
                rightScopeDto.setOrgIdList(orgIdList.stream().distinct().collect(Collectors.toList()));
                hasDemander = true;
            }
        }
        // 没有委托商范围，重新添加
        if (!hasDemander && CollectionUtil.isNotEmpty(demanderCorpList)) {
            rightScopeDto = new RightScopeDto();
            rightScopeDto.setHasAllScope("N");
            rightScopeDto.setScopeType(RightScopeTypeEnum.DEMANDER.getCode());
            rightScopeDto.setOrgIdList(demanderCorpList);
            rightScopeDtoList.add(rightScopeDto);
        }
        return rightScopeDtoList;
    }
}
