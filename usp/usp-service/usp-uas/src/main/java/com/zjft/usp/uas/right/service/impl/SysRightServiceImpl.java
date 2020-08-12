package com.zjft.usp.uas.right.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.enums.RightScopeTypeEnum;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.uas.right.dto.SysRightDto;
import com.zjft.usp.uas.right.dto.SysRightTreeDto;
import com.zjft.usp.uas.right.filter.SysRightFilter;
import com.zjft.usp.uas.right.mapper.SysRightMapper;
import com.zjft.usp.uas.right.model.SysRight;
import com.zjft.usp.uas.right.model.SysRightExtraCorp;
import com.zjft.usp.uas.right.model.SysRightScopeType;
import com.zjft.usp.uas.right.model.SysTenant;
import com.zjft.usp.uas.right.service.SysRightExtraCorpService;
import com.zjft.usp.uas.right.service.SysRightScopeTypeService;
import com.zjft.usp.uas.right.service.SysRightService;
import com.zjft.usp.uas.right.service.SysTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRightServiceImpl extends ServiceImpl<SysRightMapper, SysRight> implements SysRightService {

    @Autowired
    private SysTenantService sysTenantService;
    @Autowired
    private SysRightScopeTypeService sysRightScopeTypeService;

    @Autowired
    private SysRightExtraCorpService sysRightExtraCorpService;

    @Override
    public List<SysRight> listUserRightByApp(SysRightFilter sysRightFilter) {
        return this.baseMapper.listUserRightByApp(sysRightFilter);
    }

    /**
     * 分页查询系统权限
     *
     * @param sysRightFilter
     * @return
     * @author zgpi
     * @date 2019/11/26 17:14
     **/
    @Override
    public ListWrapper<SysRight> query(SysRightFilter sysRightFilter) {
        QueryWrapper<SysRight> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(sysRightFilter.getRightName())) {
            queryWrapper.eq("right_name", StrUtil.trimToEmpty(sysRightFilter.getRightName()));
        }
        if (StrUtil.isNotBlank(sysRightFilter.getServiceDemander())) {
            queryWrapper.eq("service_demander", sysRightFilter.getServiceDemander());
        }
        if (StrUtil.isNotBlank(sysRightFilter.getServiceProvider())) {
            queryWrapper.eq("service_provider", sysRightFilter.getServiceProvider());
        }
        if (StrUtil.isNotBlank(sysRightFilter.getDeviceUser())) {
            queryWrapper.eq("device_user", sysRightFilter.getDeviceUser());
        }
        if (StrUtil.isNotBlank(sysRightFilter.getCloudManager())) {
            queryWrapper.eq("cloud_manager", sysRightFilter.getCloudManager());
        }
        Page page = new Page(sysRightFilter.getPageNum(), sysRightFilter.getPageSize());
        IPage<SysRight> sysRightIPage = this.page(page, queryWrapper);
        return ListWrapper.<SysRight>builder().list(sysRightIPage.getRecords()).total(sysRightIPage.getTotal()).build();
    }

    /**
     * 查询权限列表
     *
     * @param sysRightFilter
     * @return
     * @author zgpi
     * @date 2019/11/27 09:09
     **/
    @Override
    public List<SysRight> listByCorpId(SysRightFilter sysRightFilter) {
        QueryWrapper<SysRight> queryWrapper = new QueryWrapper<>();
        if (LongUtil.isNotZero(sysRightFilter.getCorpId())) {
            SysTenant sysTenant = sysTenantService.getById(sysRightFilter.getCorpId());
            if (sysTenant == null) {
                throw new AppException("请联系管理员设置企业类型！");
            }
            if (StrUtil.isNotBlank(sysTenant.getServiceDemander())) {
                queryWrapper.eq("service_demander", StrUtil.trimToEmpty(sysTenant.getServiceDemander()).toUpperCase());
            }
            queryWrapper.or();
            if (StrUtil.isNotBlank(sysTenant.getServiceProvider())) {
                queryWrapper.eq("service_provider", StrUtil.trimToEmpty(sysTenant.getServiceProvider()).toUpperCase());
            }
            queryWrapper.or();
            if (StrUtil.isNotBlank(sysTenant.getDeviceUser())) {
                queryWrapper.eq("device_user", StrUtil.trimToEmpty(sysTenant.getDeviceUser()).toUpperCase());
            }
        }
        queryWrapper.orderByAsc("right_id");
        return this.list(queryWrapper);
    }

    @Override
    public List<SysRight> listCommonByCorpId(Long corpId) {
        List<SysRight> sysRightList = new ArrayList<>();
        if (LongUtil.isNotZero(corpId)) {
            QueryWrapper<SysRight> queryWrapper = new QueryWrapper<>();
            SysTenant sysTenant = sysTenantService.getById(corpId);
            if (sysTenant == null) {
                throw new AppException("请联系管理员设置企业类型！");
            }
            // 1. 根据企业类型
            if (sysTenant.getServiceDemander().toUpperCase().equals("Y")) {
                queryWrapper.or(i -> i.eq("service_demander", "Y").eq("service_demander_common", "Y"));
            }
            if (sysTenant.getServiceProvider().toUpperCase().equals("Y")) {
                queryWrapper.or(i -> i.eq("service_provider", "Y").eq("service_provider_common", "Y"));
            }
            if (sysTenant.getDeviceUser().toUpperCase().equals("Y")) {
                queryWrapper.or(i -> i.eq("device_user", "Y").eq("device_user_common", "Y"));
            }
            if (sysTenant.getCloudManager().toUpperCase().equals("Y")) {
                queryWrapper.or(i -> i.eq("cloud_manager", "Y").eq("cloud_manager_common", "Y"));
            }
            // 2. 根据企业id查询rightExtraCorp
            List<SysRightExtraCorp> sysRightExtraCorpList = sysRightExtraCorpService.list(
                    new QueryWrapper<SysRightExtraCorp>().eq("corp_id", corpId).eq("common", "Y")
            );
            if (sysRightExtraCorpList.size() > 0) {
                List<Long> rightIdList = sysRightExtraCorpList.stream().map(e -> e.getRightId()).collect(Collectors.toList());
                if (rightIdList.size() > 0) {
                    queryWrapper.or(i -> i.in("right_id", rightIdList));
                }
            }
            queryWrapper.orderByAsc("right_id");
            sysRightList = this.list(queryWrapper);
            sysRightList.stream().distinct().collect(Collectors.toList());
        }
        return sysRightList;
    }

    /**
     * 模糊查询系统权限
     *
     * @param sysRightFilter
     * @return
     * @author zgpi
     * @date 2019/11/26 17:14
     **/
    @Override
    public List<SysRight> matchSysRight(SysRightFilter sysRightFilter) {
        if (LongUtil.isZero(sysRightFilter.getParentId())) {
            sysRightFilter.setRightId(null);
        }
        return this.baseMapper.matchSysRight(sysRightFilter);
    }

    /**
     * 添加系统权限
     *
     * @param sysRight
     * @return
     * @author zgpi
     * @date 2019/11/26 18:32
     **/
    @Override
    public void addSysRight(SysRight sysRight) {
        SysRight entity = this.getById(sysRight.getRightId());
        if (entity != null) {
            throw new AppException("权限编号已存在！");
        }
        this.save(sysRight);
    }

    /**
     * 修改系统权限
     *
     * @param sysRight
     * @return
     * @author zgpi
     * @date 2019/11/26 18:33
     **/
    @Override
    public void updateSysRight(SysRight sysRight) {
        this.updateById(sysRight);
    }

    /**
     * 获得最大权限编号
     *
     * @param
     * @return
     * @author zgpi
     * @date 2019/11/26 18:43
     **/
    @Override
    public Long findMaxRightId() {
        Long rightId = this.baseMapper.findMaxRightId();
        if (LongUtil.isZero(rightId)) {
            rightId = 1L;
        } else {
            rightId = rightId + 1;
        }
        return rightId;
    }

    /**
     * 权限编号与名称映射
     *
     * @param rightIdList
     * @return
     * @author zgpi
     * @date 2019/11/27 14:10
     **/
    @Override
    public Map<Long, String> mapIdAndName(List<Long> rightIdList) {
        Map<Long, String> map = new HashMap<>();
        if (rightIdList != null && rightIdList.size() > 0) {
            List<SysRight> list = this.list(new QueryWrapper<SysRight>()
                    .in("right_id", rightIdList));
            if (CollectionUtil.isNotEmpty(list)) {
                map = list.stream().collect(Collectors.toMap(SysRight::getRightId, SysRight::getRightName));
            }
        }
        return map;
    }

    /**
     * 权限编号与应用编号映射
     *
     * @param rightIdList
     * @return
     * @author zgpi
     * @date 2019/11/27 14:10
     **/
    @Override
    public Map<Long, Integer> mapIdAndAppId(List<Long> rightIdList) {
        Map<Long, Integer> map = new HashMap<>();
        if (rightIdList != null && rightIdList.size() > 0) {
            List<SysRight> list = this.list(new QueryWrapper<SysRight>()
                    .in("right_id", rightIdList));
            if (CollectionUtil.isNotEmpty(list)) {
                map = list.stream().collect(Collectors.toMap(SysRight::getRightId, SysRight::getAppId));
            }
        }
        return map;
    }

    /**
     * 查询系统权限树
     *
     * @param sysRightFilter
     * @return
     * @author zgpi
     * @date 2019/12/9 14:50
     **/
    @Override
    public List<SysRightTreeDto> listSysRightTree(SysRightFilter sysRightFilter) {
        List<SysRightDto> sysRightDtoList = this.baseMapper.listSysRightTree(sysRightFilter);
        // 获取权限范围信息
        List<Long> rightIdList = sysRightDtoList.stream().map(e -> e.getRightId()).collect(Collectors.toList());
        List<SysRightScopeType> sysRightScopeTypeList = sysRightScopeTypeService.list(new QueryWrapper<SysRightScopeType>().in("right_id", rightIdList));
        Map<Long, List<Integer>> map = new HashMap<>(); // { 203:[1,2,3], 204:[1,2]}
        if (CollectionUtil.isNotEmpty(sysRightScopeTypeList)) {
            for (SysRightScopeType sysRightScopeType : sysRightScopeTypeList) {
                List<Integer> list = new ArrayList<>();
                if (map.containsKey(sysRightScopeType.getRightId())) {
                    list = map.get(sysRightScopeType.getRightId());
                }
                list.add(sysRightScopeType.getScopeType());
                map.put(sysRightScopeType.getRightId(), list);
            }
        }
        // 额外企业信息
        List<SysRightExtraCorp> sysRightExtraCorpList = sysRightExtraCorpService.list(new QueryWrapper<SysRightExtraCorp>().in("right_id", rightIdList));
        Map<Long, List<SysRightExtraCorp>> rightIdAndExtraCorpMap = new HashMap<>();
        for (SysRightExtraCorp extraCorp : sysRightExtraCorpList) {
            rightIdAndExtraCorpMap.computeIfAbsent(extraCorp.getRightId(), k -> new ArrayList<>()).add(extraCorp);
        }

        // 组装
        for (SysRightDto sysRightDto : sysRightDtoList) {
            List<Integer> typeList = map.get(sysRightDto.getRightId());
            if (CollectionUtil.isNotEmpty(typeList)) {
                sysRightDto.setScopeTypeList(typeList);
                List<String> typeNameList = typeList.stream().map(e -> RightScopeTypeEnum.getNameByCode(e)).collect(Collectors.toList());
                sysRightDto.setScopeTypeNames(StrUtil.join(",", typeNameList));
            }
            List<SysRightExtraCorp> extraCorpList = rightIdAndExtraCorpMap.get(sysRightDto.getRightId());
            if (CollectionUtil.isNotEmpty(extraCorpList)) {
                sysRightDto.setExtraCorpList(extraCorpList);
            }
        }
        return this.transferTreeList(sysRightDtoList);
    }

    /**
     * 查询系统权限树
     *
     * @param sysRightFilter
     * @return
     * @author zgpi
     * @date 2019/12/9 14:50
     **/
    @Override
    public List<SysRightTreeDto> listSysAuthRightTree(SysRightFilter sysRightFilter) {
        SysTenant sysTenant = sysTenantService.getById(sysRightFilter.getCorpId());
        if (sysTenant != null) {
            sysRightFilter.setServiceDemander(sysTenant.getServiceDemander());
            sysRightFilter.setServiceProvider(sysTenant.getServiceProvider());
            sysRightFilter.setDeviceUser(sysTenant.getDeviceUser());
            sysRightFilter.setCloudManager(sysTenant.getCloudManager());

            // 获取该企业的权限
            List<SysRightDto> sysRightDtoList = this.baseMapper.listSysAuthRightTree(sysRightFilter);

            // 查询条件的范围权限
            List<Long> rightIdList = sysRightDtoList.stream().map(e -> e.getRightId()).collect(Collectors.toList());
            if(CollectionUtil.isEmpty(rightIdList)){
                throw new AppException("未获取到权限!");
            }
            List<SysRightScopeType> sysRightScopeTypeList = sysRightScopeTypeService.list(new QueryWrapper<SysRightScopeType>().in("right_id", rightIdList));

            Map<Long, List<Integer>> map = new HashMap<>();
            if (CollectionUtil.isNotEmpty(sysRightScopeTypeList)) {
                for (SysRightScopeType sysRightScopeType : sysRightScopeTypeList) {
                    List<Integer> list = new ArrayList<>();
                    if (map.containsKey(sysRightScopeType.getRightId())) {
                        list = map.get(sysRightScopeType.getRightId());
                    }
                    list.add(sysRightScopeType.getScopeType());
                    map.put(sysRightScopeType.getRightId(), list);
                }
            }
            for (SysRightDto sysRightDto : sysRightDtoList) {
                List<Integer> typeList = map.get(sysRightDto.getRightId());
                if (CollectionUtil.isNotEmpty(typeList)) {
                    sysRightDto.setScopeTypeList(typeList);
                }
            }
            return this.transferTreeList(sysRightDtoList);
        }
        return null;
    }

    /**
     * 查询系统范围权限树，只列出范围权限及其上级
     *
     * @param sysRightFilter
     * @return
     * @author zgpi
     * @date 2020/6/4 10:31
     **/
    @Override
    public List<SysRightTreeDto> listSysAuthScopeRightTree(SysRightFilter sysRightFilter) {
        SysTenant sysTenant = sysTenantService.getById(sysRightFilter.getCorpId());
        if (sysTenant != null) {
            sysRightFilter.setServiceDemander(sysTenant.getServiceDemander());
            sysRightFilter.setServiceProvider(sysTenant.getServiceProvider());
            sysRightFilter.setDeviceUser(sysTenant.getDeviceUser());
            sysRightFilter.setCloudManager(sysTenant.getCloudManager());

            // 获取该企业的权限
            List<SysRightDto> sysRightDtoList = this.baseMapper.listSysAuthRightTree(sysRightFilter);

            // 查询条件的范围权限
            List<Long> rightIdList = sysRightDtoList.stream().map(e -> e.getRightId()).collect(Collectors.toList());
            List<SysRightScopeType> sysRightScopeTypeList = sysRightScopeTypeService.list(new QueryWrapper<SysRightScopeType>().in("right_id", rightIdList));

            Map<Long, List<Integer>> map = new HashMap<>();
            if (CollectionUtil.isNotEmpty(sysRightScopeTypeList)) {
                for (SysRightScopeType sysRightScopeType : sysRightScopeTypeList) {
                    List<Integer> list = new ArrayList<>();
                    if (map.containsKey(sysRightScopeType.getRightId())) {
                        list = map.get(sysRightScopeType.getRightId());
                    }
                    list.add(sysRightScopeType.getScopeType());
                    map.put(sysRightScopeType.getRightId(), list);
                }
            }
            for (SysRightDto sysRightDto : sysRightDtoList) {
                List<Integer> typeList = map.get(sysRightDto.getRightId());
                if (CollectionUtil.isNotEmpty(typeList)) {
                    sysRightDto.setScopeTypeList(typeList);
                }
            }
            List<SysRightTreeDto> sysRightTreeDtoList = this.transferTreeList(sysRightDtoList);

            return sysRightTreeDtoList;
        }
        return null;
    }

    /**
     * 组装树状结构列表
     *
     * @param sysRightDtoList
     * @return
     * @author zgpi, CK
     * @date 2019/12/12 18:40
     **/
    private List<SysRightTreeDto> transferTreeList(List<SysRightDto> sysRightDtoList) {
        List<SysRightTreeDto> sysRightTreeDtoList = new ArrayList<>();
        SysRightTreeDto sysRightTreeDto;
        if (sysRightDtoList.size() == 1) {
            SysRightDto dto = sysRightDtoList.get(0);
            if (LongUtil.isNotZero(dto.getParentId())) {
                sysRightTreeDto = new SysRightTreeDto();
                sysRightTreeDto.setKey(dto.getRightId());
                sysRightTreeDto.setRightName(dto.getRightName());
                sysRightTreeDto.setRightCode(dto.getRightCode());
                sysRightTreeDto.setAppId(dto.getAppId());
                sysRightTreeDto.setHasScope(dto.getHasScope());
                sysRightTreeDto.setScopeTypeList(dto.getScopeTypeList());
                sysRightTreeDto.setScopeTypeNames(dto.getScopeTypeNames());
                sysRightTreeDto.setServiceDemander(dto.getServiceDemander());
                sysRightTreeDto.setServiceDemanderCommon(dto.getServiceDemanderCommon());
                sysRightTreeDto.setServiceProvider(dto.getServiceProvider());
                sysRightTreeDto.setServiceProviderCommon(dto.getServiceProviderCommon());
                sysRightTreeDto.setDeviceUser(dto.getDeviceUser());
                sysRightTreeDto.setDeviceUserCommon(dto.getDeviceUserCommon());
                sysRightTreeDto.setCloudManager(dto.getCloudManager());
                sysRightTreeDto.setCloudManagerCommon(dto.getCloudManagerCommon());
                sysRightTreeDto.setHasExtraCorp(dto.getHasExtraCorp());
                sysRightTreeDto.setExtraCorpList(dto.getExtraCorpList());
                sysRightTreeDto.setExtraCorpCommon(dto.getExtraCorpCommon());
                sysRightTreeDto.setIsLeaf(false);
                sysRightTreeDtoList.add(sysRightTreeDto);
                return sysRightTreeDtoList;
            }
        }
        // 一级菜单
        for (SysRightDto sysRightDto : sysRightDtoList) {
            if (LongUtil.isZero(sysRightDto.getParentId())) {
                sysRightTreeDto = new SysRightTreeDto();
                sysRightTreeDto.setKey(sysRightDto.getRightId());
                sysRightTreeDto.setTitle(sysRightDto.getRightName());
                sysRightTreeDto.setRightName(sysRightDto.getRightName());
                sysRightTreeDto.setRightCode(sysRightDto.getRightCode());
                sysRightTreeDto.setAppId(sysRightDto.getAppId());
                sysRightTreeDto.setHasScope(sysRightDto.getHasScope());
                sysRightTreeDto.setScopeTypeList(sysRightDto.getScopeTypeList());
                sysRightTreeDto.setScopeTypeNames(sysRightDto.getScopeTypeNames());
                sysRightTreeDto.setServiceDemander(sysRightDto.getServiceDemander());
                sysRightTreeDto.setServiceDemanderCommon(sysRightDto.getServiceDemanderCommon());
                sysRightTreeDto.setServiceProvider(sysRightDto.getServiceProvider());
                sysRightTreeDto.setServiceProviderCommon(sysRightDto.getServiceProviderCommon());
                sysRightTreeDto.setDeviceUser(sysRightDto.getDeviceUser());
                sysRightTreeDto.setDeviceUserCommon(sysRightDto.getDeviceUserCommon());
                sysRightTreeDto.setCloudManager(sysRightDto.getCloudManager());
                sysRightTreeDto.setCloudManagerCommon(sysRightDto.getCloudManagerCommon());
                sysRightTreeDto.setHasExtraCorp(sysRightDto.getHasExtraCorp());
                sysRightTreeDto.setExtraCorpList(sysRightDto.getExtraCorpList());
                sysRightTreeDto.setExtraCorpCommon(sysRightDto.getExtraCorpCommon());
                sysRightTreeDto.setIsLeaf(true);
                sysRightTreeDtoList.add(sysRightTreeDto);
            }
        }
        // 为一级菜单设置子菜单，getChild是递归调用的
        for (SysRightTreeDto tree : sysRightTreeDtoList) {
            tree.setChildren(getChild(tree, sysRightDtoList));
        }
        return sysRightTreeDtoList;
    }

    /**
     * 递归查找子节点
     *
     * @param rightTree 当前菜单id
     * @param rootMenu  要查找的列表
     * @return
     */
    private List<SysRightTreeDto> getChild(SysRightTreeDto rightTree, List<SysRightDto> rootMenu) {
        // 子菜单
        List<SysRightTreeDto> childList = new ArrayList<>();
        SysRightTreeDto sysRightTreeDto;
        for (SysRightDto sysRightDto : rootMenu) {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (LongUtil.isNotZero(sysRightDto.getParentId())) {
                if (sysRightDto.getParentId().equals(rightTree.getKey())) {
                    rightTree.setIsLeaf(false);
                    sysRightTreeDto = new SysRightTreeDto();
                    sysRightTreeDto.setParentId(sysRightDto.getParentId());
                    sysRightTreeDto.setKey(sysRightDto.getRightId());
                    sysRightTreeDto.setTitle(sysRightDto.getRightName());
                    sysRightTreeDto.setRightName(sysRightDto.getRightName());
                    sysRightTreeDto.setRightCode(sysRightDto.getRightCode());
                    sysRightTreeDto.setAppId(sysRightDto.getAppId());
                    sysRightTreeDto.setHasScope(sysRightDto.getHasScope());
                    sysRightTreeDto.setScopeTypeList(sysRightDto.getScopeTypeList());
                    sysRightTreeDto.setScopeTypeNames(sysRightDto.getScopeTypeNames());
                    sysRightTreeDto.setServiceDemander(sysRightDto.getServiceDemander());
                    sysRightTreeDto.setServiceDemanderCommon(sysRightDto.getServiceDemanderCommon());
                    sysRightTreeDto.setServiceProvider(sysRightDto.getServiceProvider());
                    sysRightTreeDto.setServiceProviderCommon(sysRightDto.getServiceProviderCommon());
                    sysRightTreeDto.setDeviceUser(sysRightDto.getDeviceUser());
                    sysRightTreeDto.setDeviceUserCommon(sysRightDto.getDeviceUserCommon());
                    sysRightTreeDto.setCloudManager(sysRightDto.getCloudManager());
                    sysRightTreeDto.setCloudManagerCommon(sysRightDto.getCloudManagerCommon());
                    sysRightTreeDto.setHasExtraCorp(sysRightDto.getHasExtraCorp());
                    sysRightTreeDto.setExtraCorpList(sysRightDto.getExtraCorpList());
                    sysRightTreeDto.setExtraCorpCommon(sysRightDto.getExtraCorpCommon());
                    sysRightTreeDto.setIsLeaf(true);
                    childList.add(sysRightTreeDto);
                }
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (SysRightTreeDto menu : childList) {
            // 递归
            menu.setChildren(getChild(menu, rootMenu));
        }
        // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }

}
