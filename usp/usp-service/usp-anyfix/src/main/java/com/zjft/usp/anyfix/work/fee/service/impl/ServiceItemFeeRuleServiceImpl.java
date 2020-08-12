package com.zjft.usp.anyfix.work.fee.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.model.WorkCondition;
import com.zjft.usp.anyfix.baseinfo.service.ServiceItemService;
import com.zjft.usp.anyfix.baseinfo.service.WorkConditionService;
import com.zjft.usp.anyfix.baseinfo.service.WorkTypeService;
import com.zjft.usp.anyfix.corp.branch.service.DeviceBranchService;
import com.zjft.usp.anyfix.corp.manage.model.DemanderCustom;
import com.zjft.usp.anyfix.corp.manage.service.DemanderCustomService;
import com.zjft.usp.anyfix.work.auto.service.WorkDispatchServiceCorpService;
import com.zjft.usp.anyfix.work.fee.dto.ServiceItemFeeRuleDto;
import com.zjft.usp.anyfix.work.fee.filter.ServiceItemFeeRuleFilter;
import com.zjft.usp.anyfix.work.fee.mapper.ServiceItemFeeRuleMapper;
import com.zjft.usp.anyfix.work.fee.model.ServiceItemFeeRule;
import com.zjft.usp.anyfix.work.fee.service.ServiceItemFeeRuleService;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.device.service.DeviceFeignService;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工单服务项目结算规则 服务实现类
 * </p>
 *
 * @author canlei
 * @since 2020-01-07
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ServiceItemFeeRuleServiceImpl extends ServiceImpl<ServiceItemFeeRuleMapper, ServiceItemFeeRule> implements ServiceItemFeeRuleService {

    @Autowired
    private WorkConditionService workConditionService;
    @Autowired
    private DeviceBranchService deviceBranchService;
    @Autowired
    private WorkTypeService workTypeService;
    @Autowired
    private WorkDispatchServiceCorpService workDispatchServiceCorpService;
    @Autowired
    private ServiceItemService serviceItemService;
    @Autowired
    private DemanderCustomService demanderCustomService;

    @Resource
    private UasFeignService uasFeignService;
    @Resource
    private DeviceFeignService deviceFeignService;

    /**
     * 分页条件查询
     *
     * @author canlei
     * @param serviceItemFeeRuleFilter
     * @return
     */
    @Override
    public ListWrapper<ServiceItemFeeRuleDto> query(ServiceItemFeeRuleFilter serviceItemFeeRuleFilter) {
        ListWrapper<ServiceItemFeeRuleDto> listWrapper = new ListWrapper<>();
        if (serviceItemFeeRuleFilter == null || LongUtil.isZero(serviceItemFeeRuleFilter.getServiceCorp())) {
            return listWrapper;
        }

        Page page = new Page(serviceItemFeeRuleFilter.getPageNum(), serviceItemFeeRuleFilter.getPageSize());
        List<ServiceItemFeeRuleDto> dtoList = this.baseMapper.pageByFilter(page, serviceItemFeeRuleFilter);
        if (CollectionUtil.isNotEmpty(dtoList)) {
            List<Long> corpIdList = new ArrayList<>();
            List<Long> customList = new ArrayList<>();
            for (ServiceItemFeeRuleDto dto : dtoList) {
                corpIdList.add(dto.getServiceCorp());
                corpIdList.add(dto.getCustomCorp());
                corpIdList.add(dto.getDemanderCorp());
                customList.add(dto.getCustomId());
            }
            Map<Long, String> corpMap = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList)).getData();
            Map<Long, String> customMap = this.demanderCustomService.mapIdAndCustomNameByIdList(customList);
            Map<String, String> areaMap = JsonUtil.parseObject(JsonUtil.toJson(uasFeignService.mapAreaCodeAndName().getData()), Map.class);
            Map<Long, String> modelMap = deviceFeignService.mapDeviceModel().getData();
            Map<Integer, String> serviceItemMap = this.serviceItemService.mapIdAndNameByCorp(serviceItemFeeRuleFilter.getServiceCorp());

            Map<Long, String> largeClassMap = deviceFeignService.mapLargeClassByCorpIdList(corpIdList).getData();
            Map<Long, String> smallClassMap = deviceFeignService.mapSmallClassByCorpIdList(corpIdList).getData();
            Map<Long, String> brandMap = deviceFeignService.mapDeviceBrandByCorpIdList(corpIdList).getData();
            Map<Long, String> deviceBranchMap = deviceBranchService.mapDeviceBranchByCorpIdList(corpIdList);
            Map<Integer, String> workTypeMap = this.workTypeService.mapWorkTypeByCorpIdList(corpIdList);
            // 补充额外属性
            dtoList.forEach(dto -> {
                dto.setWorkTypeName(StrUtil.trimToEmpty(workTypeMap.get(dto.getWorkType())));
                dto.setDemanderCorpName(StrUtil.trimToEmpty(corpMap.get(dto.getDemanderCorp())));
                dto.setCustomCorpName(StrUtil.trimToEmpty(customMap.get(dto.getCustomId())));
                if (LongUtil.isNotZero(dto.getCustomCorp())) {
                    dto.setCustomCorpName(StrUtil.trimToEmpty(corpMap.get(dto.getCustomCorp())));
                }
                dto.setServiceCorpName(StrUtil.trimToEmpty(corpMap.get(dto.getServiceCorp())));
                dto.setServiceItemName(StrUtil.trimToEmpty(serviceItemMap.get(dto.getServiceItemId())));
                dto.setLargeClassName(StrUtil.trimToEmpty(largeClassMap.get(dto.getLargeClassId())));

                dto.setSmallClassName(workDispatchServiceCorpService.getName(dto.getSmallClassId(), smallClassMap, ","));
                dto.setBrandName(workDispatchServiceCorpService.getName(dto.getBrandId(), brandMap, ","));
                dto.setModelName(workDispatchServiceCorpService.getName(dto.getModelId(), modelMap, ","));
                dto.setDeviceBranchName(workDispatchServiceCorpService.getName(dto.getDeviceBranch(), deviceBranchMap, ","));
                dto.setDistrictName(workDispatchServiceCorpService.getDistrictName(dto.getDistrict(), areaMap, "-"));
            });
        }
        listWrapper.setList(dtoList);
        listWrapper.setTotal(page.getTotal());
        return listWrapper;
    }

    /**
     * 添加
     *
     * @author canlei
     * @param serviceItemFeeRuleDto
     */
    @Override
    public void add(ServiceItemFeeRuleDto serviceItemFeeRuleDto) {
        Long conditionId = KeyUtil.getId();
        ServiceItemFeeRule serviceItemFeeRule = new ServiceItemFeeRule();
        if (LongUtil.isNotZero(serviceItemFeeRuleDto.getCustomId())) {
            DemanderCustom demanderCustom = this.demanderCustomService.getById(serviceItemFeeRuleDto.getCustomId());
            if (demanderCustom != null) {
                serviceItemFeeRuleDto.setCustomCorp(demanderCustom.getCustomCorp());
            }
        }
        BeanUtils.copyProperties(serviceItemFeeRuleDto, serviceItemFeeRule);
        serviceItemFeeRule.setRuleId(KeyUtil.getId());
        serviceItemFeeRule.setConditionId(conditionId);
        this.save(serviceItemFeeRule);

        WorkCondition workCondition = new WorkCondition();
        BeanUtils.copyProperties(serviceItemFeeRuleDto, workCondition);
        workCondition.setId(conditionId);
        workConditionService.save(workCondition);
    }

    /**
     * 更新
     *
     * @author canlei
     * @param serviceItemFeeRuleDto
     */
    @Override
    public void update(ServiceItemFeeRuleDto serviceItemFeeRuleDto) {
        // 先删除条件配置
        this.workConditionService.delById(serviceItemFeeRuleDto.getConditionId());
        ServiceItemFeeRule serviceItemFeeRule = new ServiceItemFeeRule();
        if (LongUtil.isNotZero(serviceItemFeeRuleDto.getCustomId())) {
            DemanderCustom demanderCustom = this.demanderCustomService.getById(serviceItemFeeRuleDto.getCustomId());
            if (demanderCustom != null) {
                serviceItemFeeRuleDto.setCustomCorp(demanderCustom.getCustomCorp());
            }
        }
        BeanUtils.copyProperties(serviceItemFeeRuleDto, serviceItemFeeRule);
        this.updateById(serviceItemFeeRule);

        WorkCondition workCondition = new WorkCondition();
        BeanUtils.copyProperties(serviceItemFeeRuleDto, workCondition);
        workCondition.setId(serviceItemFeeRuleDto.getConditionId());
        // 再添加条件配置
        workConditionService.save(workCondition);
    }

    /**
     * 删除
     *
     * @author canlei
     * @param ruleId
     */
    @Override
    public void deleteById(Long ruleId) {
        if (LongUtil.isZero(ruleId)) {
            throw new AppException("规则编号不能为空，请检查！");
        }
       ServiceItemFeeRule serviceItemFeeRule = this.getById(ruleId);
        if (serviceItemFeeRule == null) {
            throw new AppException("规则不存在，请检查！");
        }
        this.removeById(ruleId);
        // 删除条件
        this.workConditionService.removeById(serviceItemFeeRule.getConditionId());
    }

    /**
     * 匹配服务项目费用规则
     *
     * @author canlei
     * @param workDto
     * @return
     */
    @Override
    public List<ServiceItemFeeRuleDto> matchServiceItemFeeRule(WorkDto workDto) {
        if (workDto == null || LongUtil.isZero(workDto.getServiceCorp())) {
            throw new AppException("服务商不能为空");
        }
        List<ServiceItemFeeRuleDto> list = this.baseMapper.matchServiceItemFeeRule(workDto);
        return list;
    }

}
