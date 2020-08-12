package com.zjft.usp.uas.right.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.feign.service.UserFeignService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.uas.corp.model.CorpRegistry;
import com.zjft.usp.uas.corp.service.CorpRegistryService;
import com.zjft.usp.uas.corp.service.CorpVerifyService;
import com.zjft.usp.uas.right.dto.SysTenantDto;
import com.zjft.usp.uas.right.enums.TenantLevelEnum;
import com.zjft.usp.uas.right.filter.SysTenantFilter;
import com.zjft.usp.uas.right.mapper.SysTenantMapper;
import com.zjft.usp.uas.right.model.SysTenant;
import com.zjft.usp.uas.right.service.SysTenantService;
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
 * <p>
 * 租户设置表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements SysTenantService {

    @Autowired
    private CorpRegistryService corpRegistryService;
    @Autowired
    private CorpVerifyService corpVerifyService;

    @Resource
    private UserFeignService userFeignService;
    /**
     * 分页查询系统租户
     *
     * @param sysTenantFilter
     * @return
     * @author zgpi
     * @date 2019/11/26 19:17
     **/
    @Override
    public ListWrapper<SysTenantDto> query(SysTenantFilter sysTenantFilter) {
        QueryWrapper<SysTenant> queryWrapper = new QueryWrapper<>();
        if (LongUtil.isNotZero(sysTenantFilter.getCorpId())) {
            queryWrapper.eq("corp_id", sysTenantFilter.getCorpId());
        }
        if (StrUtil.isNotBlank(sysTenantFilter.getServiceDemander())) {
            queryWrapper.eq("service_demander", sysTenantFilter.getServiceDemander());
        }
        if (StrUtil.isNotBlank(sysTenantFilter.getServiceProvider())) {
            queryWrapper.eq("service_provider", sysTenantFilter.getServiceProvider());
        }
        if (StrUtil.isNotBlank(sysTenantFilter.getDeviceUser())) {
            queryWrapper.eq("device_user", sysTenantFilter.getDeviceUser());
        }
        if (StrUtil.isNotBlank(sysTenantFilter.getCloudManager())) {
            queryWrapper.eq("cloud_manager", sysTenantFilter.getCloudManager());
        }
        Page page = new Page(sysTenantFilter.getPageNum(), sysTenantFilter.getPageSize());
        IPage<SysTenant> sysTenantIPage = this.page(page, queryWrapper);
        List<SysTenant> sysTenantList = sysTenantIPage.getRecords();
        List<SysTenantDto> sysTenantDtoList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(sysTenantList)) {
            SysTenantDto sysTenantDto;
            List<Long> corpIdList = sysTenantList.stream().map(e -> e.getCorpId()).collect(Collectors.toList());
            Map<Long, String> corpMap = corpRegistryService.mapCorpIdAndShortName(corpIdList);
            for(SysTenant sysTenant : sysTenantList){
                sysTenantDto = new SysTenantDto();
                BeanUtils.copyProperties(sysTenant, sysTenantDto);
                sysTenantDto.setCorpName(StrUtil.trimToEmpty(corpMap.get(sysTenant.getCorpId())));
                sysTenantDtoList.add(sysTenantDto);
            }
        }
        return ListWrapper.<SysTenantDto>builder().list(sysTenantDtoList).total(sysTenantIPage.getTotal()).build();
    }

    /**
     * 添加系统租户
     *
     * @param sysTenant
     * @param userInfo
     * @return
     * @author zgpi
     * @date 2019/11/26 19:18
     **/
    @Override
    public void addSysTenant(SysTenant sysTenant, UserInfo userInfo) {
        SysTenant entity = this.getById(sysTenant.getCorpId());
        if (entity != null) {
            throw new AppException("该企业已存在!");
        }
        corpRegistryService.setMaxCorpCode(sysTenant.getCorpId());
        this.testLevel(sysTenant);
        sysTenant.setOperator(userInfo.getUserId());
        sysTenant.setOperateTime(DateUtil.date());
        this.save(sysTenant);

        //添加租户，自动认证
        CorpRegistry corpRegistry = corpRegistryService.getCorpInfoById(sysTenant.getCorpId());
        corpRegistry.setVerify(1);
        corpRegistryService.saveOrUpdate(corpRegistry);
        //添加自动认证信息
        corpVerifyService.createVerifyByCorpId(sysTenant.getCorpId());
    }

    /**
     * 修改系统租户
     *
     * @param sysTenant
     * @param userInfo
     * @return
     * @author zgpi
     * @date 2019/11/26 19:18
     **/
    @Override
    public void updateSysTenant(SysTenant sysTenant, UserInfo userInfo) {
        if( sysTenant == null) {
            throw new AppException("编辑租户不能为空");
        }
        this.testLevel(sysTenant);
        sysTenant.setOperator(userInfo.getUserId());
        sysTenant.setOperateTime(DateUtil.date());
        this.updateById(sysTenant);
    }

    /**
     * 删除系统租户
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/11/26 19:18
     **/
    @Override
    public void delSysTenant(Long corpId) {
        this.removeById(corpId);
    }

    @Override
    public List<SysTenantDto> list(SysTenantFilter sysTenantFilter) {
        return this.baseMapper.selectSysTenant(sysTenantFilter);
    }

    @Override
    public void setDemander(Long corpId, Long userId) {
        if(LongUtil.isZero(corpId)) {
            throw new AppException("企业不能为空");
        }
        if(LongUtil.isZero(userId)) {
            throw new AppException("用户编号不能为空");
        }
        SysTenant foundSysTenant = this.getById(corpId);
        if(foundSysTenant == null) {
            SysTenant sysTenant = new SysTenant();
            sysTenant.setCorpId(corpId);
            sysTenant.setServiceDemander("Y");
            sysTenant.setDemanderLevel(TenantLevelEnum.ORDINARY.getCode());
            sysTenant.setServiceProvider("N");
            sysTenant.setDeviceUser("N");
            sysTenant.setOperator(userId);
            sysTenant.setOperateTime(DateUtil.date());
            corpRegistryService.setMaxCorpCode(corpId);
            this.save(sysTenant);
        } else {
            if(!"Y".equals(foundSysTenant.getServiceDemander())) {
                foundSysTenant.setServiceDemander("Y");
                this.updateById(foundSysTenant);
            }
        }
    }

    public void testLevel(SysTenant sysTenant) {
        if("Y".equals(sysTenant.getServiceProvider())) {
            if(sysTenant.getServiceLevel() == null || sysTenant.getServiceLevel() == 0 ){
                throw new AppException("需要设置服务提供商级别");
            }
        }
        if("Y".equals(sysTenant.getServiceDemander())) {
            if(sysTenant.getDemanderLevel() == null || sysTenant.getDemanderLevel() == 0 ){
                throw new AppException("需要设置委托商级别");
            }
        }
    }
}
