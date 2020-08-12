package com.zjft.usp.anyfix.corp.branch.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.corp.branch.dto.ServiceBranchDto;
import com.zjft.usp.anyfix.corp.branch.enums.ServiceBranchTypeEnum;
import com.zjft.usp.anyfix.corp.branch.filter.ServiceBranchFilter;
import com.zjft.usp.anyfix.corp.branch.mapper.ServiceBranchMapper;
import com.zjft.usp.anyfix.corp.branch.model.ServiceBranch;
import com.zjft.usp.anyfix.corp.branch.service.ServiceBranchService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 服务网点表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-26
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ServiceBranchServiceImpl extends ServiceImpl<ServiceBranchMapper, ServiceBranch> implements ServiceBranchService {

    @Resource
    private UasFeignService uasFeignService;

    @Override
    public ListWrapper<ServiceBranchDto> query(ServiceBranchFilter serviceBranchFilter, UserInfo userInfo) {
        Page page = new Page(serviceBranchFilter.getPageNum(), serviceBranchFilter.getPageSize());
        QueryWrapper<ServiceBranch> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("service_corp", serviceBranchFilter.getServiceCorp());

        if (LongUtil.isNotZero(serviceBranchFilter.getBranchId())) {
            queryWrapper.eq("branch_id", serviceBranchFilter.getBranchId());
        }
        if (StrUtil.isNotBlank(serviceBranchFilter.getBranchName())) {
            queryWrapper.like("branch_name", StrUtil.trim(serviceBranchFilter.getBranchName()));
        }
        if (StrUtil.isNotBlank(serviceBranchFilter.getAreaCode())) {
            /*queryWrapper.and(wrapper -> wrapper.eq("province", serviceBranchFilter.getAreaCode())
                    .or().eq("city", serviceBranchFilter.getAreaCode())
                    .or().eq("district", serviceBranchFilter.getAreaCode()));*/
            char[] area = serviceBranchFilter.getAreaCode().toCharArray();
            if(area.length > 4){
                queryWrapper.and(wrapper -> wrapper.eq("city", serviceBranchFilter.getAreaCode().substring(0, 4))
                        .or().eq("district", serviceBranchFilter.getAreaCode()));
            }else {
                queryWrapper.and(wrapper -> wrapper.eq("province", serviceBranchFilter.getAreaCode())
                        .or().eq("city", serviceBranchFilter.getAreaCode()));
            }
        }
        if (StrUtil.isNotBlank(serviceBranchFilter.getAddress())) {
            queryWrapper.like("address", serviceBranchFilter.getAddress());
        }
        if (IntUtil.isNotZero(serviceBranchFilter.getType())) {
            queryWrapper.eq("type", serviceBranchFilter.getType());
        }
        if (StrUtil.isNotBlank(serviceBranchFilter.getEnabled())) {
            queryWrapper.eq("enabled", serviceBranchFilter.getEnabled());
        }
        if (StrUtil.isNotBlank(serviceBranchFilter.getMobileFilter())) {
            queryWrapper.and(wrapper -> wrapper.like("address", serviceBranchFilter.getMobileFilter().trim())
                    .or().like("contact_name", serviceBranchFilter.getMobileFilter().trim())
                    .or().like("contact_phone", serviceBranchFilter.getMobileFilter().trim())
                    .or().like("branch_name", serviceBranchFilter.getMobileFilter().trim()));
        }
        if ("Y".equalsIgnoreCase(StrUtil.trimToEmpty(serviceBranchFilter.getIfFirstLevel()))) {
            queryWrapper.eq("upper_branch_id", 0L);
        }
        queryWrapper.orderByAsc("branch_name", "branch_id");
        IPage<ServiceBranch> serviceBranchIPage = this.page(page, queryWrapper);
        List<String> areaCodeList = new ArrayList<>();
        ListWrapper<ServiceBranchDto> listWrapper = new ListWrapper<>();
        List<ServiceBranchDto> dtoList = new ArrayList<>();
        if (serviceBranchIPage != null && CollectionUtil.isNotEmpty(serviceBranchIPage.getRecords())) {
            for (ServiceBranch serviceBranch : serviceBranchIPage.getRecords()) {
                areaCodeList.add(serviceBranch.getProvince());
                areaCodeList.add(serviceBranch.getCity());
                areaCodeList.add(serviceBranch.getDistrict());
            }
            Result<Map<String, String>> result = this.uasFeignService.mapAreaCodeAndNameByCodeList(JsonUtil.toJson(areaCodeList));
            Map<String, String> areaMap = result.getData() == null ? new HashMap<>() : result.getData();
            for (ServiceBranch serviceBranch : serviceBranchIPage.getRecords()) {
                ServiceBranchDto serviceBranchDto = new ServiceBranchDto();
                BeanUtils.copyProperties(serviceBranch, serviceBranchDto);
                String provinceName = StrUtil.trimToEmpty(areaMap.get(serviceBranch.getProvince()));
                String cityName = StrUtil.trimToEmpty(areaMap.get(serviceBranch.getCity()));
                String districtName = StrUtil.trimToEmpty(areaMap.get(serviceBranch.getDistrict()));
                serviceBranchDto.setRegion(provinceName + cityName + districtName);
                dtoList.add(serviceBranchDto);
            }
        }
        for (ServiceBranchDto serviceBranchDto : dtoList) {
            if (serviceBranchDto.getType() == 1) {
                serviceBranchDto.setTypeName(ServiceBranchTypeEnum.OWN_BRANCH.getName());
            } else {
                serviceBranchDto.setTypeName(ServiceBranchTypeEnum.THIRD_PARTY_BRANCH.getName());
            }
        }
        listWrapper.setList(dtoList);
        listWrapper.setTotal(serviceBranchIPage.getTotal());
        return listWrapper;
    }

    @Override
    public ServiceBranchDto findByBranchId(Long branchId) {
        Assert.notNull(branchId, "服务网点编号不能为空！");
        ServiceBranch serviceBranch = this.getById(branchId);
        if (serviceBranch == null) {
            throw new AppException("服务网点不存在！");
        }
        List<String> areaCodeList = new ArrayList<>();
        areaCodeList.add(serviceBranch.getProvince());
        areaCodeList.add(serviceBranch.getCity());
        areaCodeList.add(serviceBranch.getDistrict());
        Result<Map<String, String>> areaResult = uasFeignService.mapAreaCodeAndNameByCodeList(JsonUtil.toJson(areaCodeList));
        Map<String, String> areaMap = areaResult.getData();
        areaMap = areaMap == null ? new HashMap<>() : areaMap;
        ServiceBranchDto serviceBranchDto = new ServiceBranchDto();
        BeanUtils.copyProperties(serviceBranch, serviceBranchDto);
        String region = "";
        region += StrUtil.isBlank(areaMap.get(serviceBranch.getProvince())) ? "" : areaMap.get(serviceBranch.getProvince());
        region += StrUtil.isBlank(areaMap.get(serviceBranch.getCity())) ? "" : areaMap.get(serviceBranch.getCity());
        region += StrUtil.isBlank(areaMap.get(serviceBranch.getDistrict())) ? "" : areaMap.get(serviceBranch.getDistrict());
        serviceBranchDto.setRegion(region);
        if (serviceBranchDto.getType() == 1) {
            serviceBranchDto.setTypeName(ServiceBranchTypeEnum.OWN_BRANCH.getName());
        } else {
            serviceBranchDto.setTypeName(ServiceBranchTypeEnum.THIRD_PARTY_BRANCH.getName());
        }
        if (LongUtil.isNotZero(serviceBranch.getUpperBranchId())) {
            ServiceBranch upperBranch = this.getById(serviceBranch.getUpperBranchId());
            if (upperBranch != null) {
                serviceBranchDto.setUpperBranchName(upperBranch.getBranchName());
            }
        }
        return serviceBranchDto;
    }

    @Override
    public Long addBranch(ServiceBranchDto serviceBranchDto) {
        QueryWrapper<ServiceBranch> wrapper = new QueryWrapper<>();
        wrapper.eq("branch_name", serviceBranchDto.getBranchName());
        wrapper.eq("service_corp", serviceBranchDto.getServiceCorp());
        List<ServiceBranch> list = this.baseMapper.selectList(wrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            throw new AppException("服务网点名称已经存在！");
        }
        ServiceBranch serviceBranch = new ServiceBranch();
        BeanUtils.copyProperties(serviceBranchDto, serviceBranch);
        serviceBranch.setBranchId(KeyUtil.getId());
        serviceBranch.setOperateTime(DateUtil.date());
        this.save(serviceBranch);
        return serviceBranch.getBranchId();
    }

    @Override
    public void updateBranch(ServiceBranchDto serviceBranchDto) {
        if (LongUtil.isZero(serviceBranchDto.getBranchId())) {
            throw new AppException("网点编号不能为空！");
        }
        ServiceBranch serviceBranch = this.getById(serviceBranchDto.getBranchId());
        if (serviceBranch == null) {
            throw new AppException("该服务网点不存在！");
        }
        QueryWrapper<ServiceBranch> wrapper = new QueryWrapper<>();
        wrapper.eq("branch_name", serviceBranchDto.getBranchName());
        wrapper.eq("service_corp", serviceBranchDto.getServiceCorp());
        wrapper.ne("branch_id", serviceBranchDto.getBranchId());
        List<ServiceBranch> list = this.list(wrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            throw new AppException("该服务网点名称已经存在！");
        }
        BeanUtils.copyProperties(serviceBranchDto, serviceBranch, "service_corp");
        serviceBranch.setOperateTime(DateUtil.date());
        if (StrUtil.isBlank(serviceBranch.getDistrict())) {
            serviceBranch.setDistrict("");
        }
        if (StrUtil.isBlank(serviceBranch.getCity())) {
            serviceBranch.setCity("");
        }
        if (StrUtil.isBlank(serviceBranch.getProvince())) {
            serviceBranch.setProvince("");
        }
        this.updateById(serviceBranch);
    }

    @Override
    public Map<Long, ServiceBranch> mapServiceBranchByCorp(Long serviceCorp) {
        Map<Long, ServiceBranch> map = new HashMap<>();
        if (serviceCorp == null || serviceCorp == 0L) {
            return map;
        }
        List<ServiceBranch> list = this.list(new QueryWrapper<ServiceBranch>().eq("service_corp", serviceCorp));
        if (list != null && list.size() > 0) {
            for (ServiceBranch serviceBranch : list) {
                map.put(serviceBranch.getBranchId(), serviceBranch);
            }
        }
        return map;
    }

    @Override
    public List<ServiceBranch> listServiceBranch(ServiceBranchFilter serviceBranchFilter) {
        QueryWrapper<ServiceBranch> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("service_corp", serviceBranchFilter.getServiceCorp());
        if (LongUtil.isNotZero(serviceBranchFilter.getUpperBranchId())) {
            queryWrapper.eq("upper_branch_id", serviceBranchFilter.getUpperBranchId());
        }
        if (StrUtil.isNotBlank(serviceBranchFilter.getEnabled())) {
            queryWrapper.eq("enabled", StrUtil.trimToEmpty(serviceBranchFilter.getEnabled()).toUpperCase());
        }
        queryWrapper.orderByAsc("branch_name", "branch_id");
        return this.list(queryWrapper);
    }

    @Override
    public List<ServiceBranch> listServiceBranchByServiceUserId(Long userId) {
        List<Long> serviceCorpIds = uasFeignService.getCorpIdList(userId).getData();
        if (serviceCorpIds != null && serviceCorpIds.size() > 0) {
            List<ServiceBranch> list = this.list(new QueryWrapper<ServiceBranch>().in("service_corp", serviceCorpIds));
            return list;
        }
        return new ArrayList<>();
    }

    @Override
    public Map<Long, String> mapServiceBranchByCorpIdList(List<Long> corpIdList) {
        Map<Long, String> map = new HashMap<>();
        if (corpIdList == null || corpIdList.size() == 0) {
            return map;
        }
        List<ServiceBranch> list = this.list(new QueryWrapper<ServiceBranch>().in("service_corp", corpIdList));
        if (list != null && list.size() > 0) {
            for (ServiceBranch serviceBranch : list) {
                map.put(serviceBranch.getBranchId(), serviceBranch.getBranchName());
            }
        }
        return map;
    }

    /**
     * 模糊查询服务网点
     *
     * @param serviceBranchFilter
     * @return
     * @author zgpi
     * @date 2019/11/19 10:23
     **/
    @Override
    public List<ServiceBranch> matchServiceBranch(ServiceBranchFilter serviceBranchFilter) {
        return this.baseMapper.matchServiceBranch(serviceBranchFilter);
    }

    /**
     * 获得所有上级网点列表（包括自己）
     *
     * @param branchId
     * @return
     * @author zgpi
     * @date 2020/3/9 15:59
     */
    @Override
    public List<ServiceBranch> listAllUpperServiceBranch(Long branchId) {
        return this.baseMapper.listAllUpperServiceBranch(branchId);
    }

    /**
     * 分页查询服务网点下级列表
     *
     * @param serviceBranchFilter
     * @return
     * @author zgpi
     * @date 2020/3/9 14:34
     */
    @Override
    public ListWrapper<ServiceBranchDto> queryAllLowerServiceBranch(ServiceBranchFilter serviceBranchFilter) {
        Page<ServiceBranch> page = new Page(serviceBranchFilter.getPageNum(), serviceBranchFilter.getPageSize());
        List<ServiceBranch> serviceBranchList = this.baseMapper.queryAllLowerServiceBranch(page, serviceBranchFilter);
        List<String> areaCodeList = new ArrayList<>();
        ListWrapper<ServiceBranchDto> listWrapper = new ListWrapper<>();
        List<ServiceBranchDto> dtoList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(serviceBranchList)) {
            for (ServiceBranch serviceBranch : serviceBranchList) {
                areaCodeList.add(serviceBranch.getProvince());
                areaCodeList.add(serviceBranch.getCity());
                areaCodeList.add(serviceBranch.getDistrict());
            }
            Result<Map<String, String>> result = uasFeignService.mapAreaCodeAndNameByCodeList(JsonUtil.toJson(areaCodeList));
            Map<String, String> areaMap = result.getData() == null ? new HashMap<>() : result.getData();
            for (ServiceBranch serviceBranch : serviceBranchList) {
                ServiceBranchDto serviceBranchDto = new ServiceBranchDto();
                BeanUtils.copyProperties(serviceBranch, serviceBranchDto);
                String provinceName = StrUtil.trimToEmpty(areaMap.get(serviceBranch.getProvince()));
                String cityName = StrUtil.trimToEmpty(areaMap.get(serviceBranch.getCity()));
                String districtName = StrUtil.trimToEmpty(areaMap.get(serviceBranch.getDistrict()));
                serviceBranchDto.setRegion(provinceName + cityName + districtName);
                dtoList.add(serviceBranchDto);
            }
        }
        for (ServiceBranchDto serviceBranchDto : dtoList) {
            if (serviceBranchDto.getType() == 1) {
                serviceBranchDto.setTypeName(ServiceBranchTypeEnum.OWN_BRANCH.getName());
            } else {
                serviceBranchDto.setTypeName(ServiceBranchTypeEnum.THIRD_PARTY_BRANCH.getName());
            }
        }
        listWrapper.setList(dtoList);
        listWrapper.setTotal(page.getTotal());
        return listWrapper;
    }

    /**
     * 分页查询服务网点同级列表
     *
     * @param serviceBranchFilter
     * @return
     * @author zgpi
     * @date 2020/3/9 14:34
     */
    @Override
    public ListWrapper<ServiceBranchDto> querySameServiceBranch(ServiceBranchFilter serviceBranchFilter) {
        if (!LongUtil.isZero(serviceBranchFilter.getBranchId())) {
            ServiceBranch serviceBranch = this.getById(serviceBranchFilter.getBranchId());
            if (serviceBranch != null) {
                serviceBranchFilter.setUpperBranchId(serviceBranch.getUpperBranchId());
            }
        }
        Page<ServiceBranch> page = new Page(serviceBranchFilter.getPageNum(), serviceBranchFilter.getPageSize());
        List<ServiceBranch> serviceBranchList = this.baseMapper.querySameServiceBranch(page, serviceBranchFilter);
        List<String> areaCodeList = new ArrayList<>();
        ListWrapper<ServiceBranchDto> listWrapper = new ListWrapper<>();
        List<ServiceBranchDto> dtoList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(serviceBranchList)) {
            for (ServiceBranch serviceBranch : serviceBranchList) {
                areaCodeList.add(serviceBranch.getProvince());
                areaCodeList.add(serviceBranch.getCity());
                areaCodeList.add(serviceBranch.getDistrict());
            }
            Result<Map<String, String>> result = uasFeignService.mapAreaCodeAndNameByCodeList(JsonUtil.toJson(areaCodeList));
            Map<String, String> areaMap = result.getData() == null ? new HashMap<>() : result.getData();
            for (ServiceBranch serviceBranch : serviceBranchList) {
                ServiceBranchDto serviceBranchDto = new ServiceBranchDto();
                BeanUtils.copyProperties(serviceBranch, serviceBranchDto);
                String provinceName = StrUtil.trimToEmpty(areaMap.get(serviceBranch.getProvince()));
                String cityName = StrUtil.trimToEmpty(areaMap.get(serviceBranch.getCity()));
                String districtName = StrUtil.trimToEmpty(areaMap.get(serviceBranch.getDistrict()));
                serviceBranchDto.setRegion(provinceName + cityName + districtName);
                dtoList.add(serviceBranchDto);
            }
        }
        for (ServiceBranchDto serviceBranchDto : dtoList) {
            if (serviceBranchDto.getType() == 1) {
                serviceBranchDto.setTypeName(ServiceBranchTypeEnum.OWN_BRANCH.getName());
            } else {
                serviceBranchDto.setTypeName(ServiceBranchTypeEnum.THIRD_PARTY_BRANCH.getName());
            }
        }
        listWrapper.setList(dtoList);
        listWrapper.setTotal(page.getTotal());
        return listWrapper;
    }

    @Override
    public Map<Long,String> mapIdAndNameByBranchIdList(List<Long> serviceBranchIdList) {
        Map<Long, String> map = new HashMap<>();
        List<ServiceBranch> list = this.list();
        if (list != null && list.size() > 0) {
            for (ServiceBranch serviceBranch : list) {
                map.put(serviceBranch.getBranchId(), serviceBranch.getBranchName());
            }
        }
        return map;
    }

    /**
     * 根据网点ID列表获得服务网点ID与名称映射
     *
     * @param branchIdList
     * @return
     * @author zgpi
     * @date 2020/4/21 10:23
     **/
    @Override
    public Map<Long, String> mapServiceBranchByBranchIdList(Collection<Long> branchIdList) {
        Map<Long, String> map = new HashMap<>();
        if (CollectionUtil.isEmpty(branchIdList)) {
            return map;
        }
        List<ServiceBranch> list = this.list(new QueryWrapper<ServiceBranch>().in("branch_id", branchIdList));
        if (CollectionUtil.isNotEmpty(list)) {
            for (ServiceBranch serviceBranch : list) {
                map.put(serviceBranch.getBranchId(), serviceBranch.getBranchName());
            }
        }
        return map;
    }

    @Override
    public List<ServiceBranch> getBranchByUserId(Long userId) {
        if(LongUtil.isZero(userId)) {
            throw new AppException("用户编号不能为空");
        }
        List<ServiceBranch> serviceBranches = this.baseMapper.selectBranchByUserId(userId);
        return serviceBranches;
    }

}
