package com.zjft.usp.anyfix.work.fee.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.model.WorkCondition;
import com.zjft.usp.anyfix.baseinfo.service.WorkConditionService;
import com.zjft.usp.anyfix.baseinfo.service.WorkTypeService;
import com.zjft.usp.anyfix.corp.branch.service.DeviceBranchService;
import com.zjft.usp.anyfix.corp.manage.model.DemanderCustom;
import com.zjft.usp.anyfix.corp.manage.service.DemanderCustomService;
import com.zjft.usp.anyfix.work.auto.service.WorkDispatchServiceCorpService;
import com.zjft.usp.anyfix.work.fee.dto.WorkBasicFeeRuleDto;
import com.zjft.usp.anyfix.work.fee.filter.WorkBasicFeeRuleFilter;
import com.zjft.usp.anyfix.work.fee.mapper.WorkBasicFeeRuleMapper;
import com.zjft.usp.anyfix.work.fee.model.WorkBasicFeeRule;
import com.zjft.usp.anyfix.work.fee.service.WorkBasicFeeRuleService;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.anyfix.work.request.enums.ServiceModeEnum;
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
 * 工单基础服务费规则 服务实现类
 * </p>
 *
 * @author canlei
 * @since 2020-01-07
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkBasicFeeRuleServiceImpl extends ServiceImpl<WorkBasicFeeRuleMapper, WorkBasicFeeRule> implements WorkBasicFeeRuleService {

    @Autowired
    private WorkConditionService workConditionService;
    @Autowired
    private DeviceBranchService deviceBranchService;
    @Autowired
    private WorkDispatchServiceCorpService workDispatchServiceCorpService;
    @Autowired
    private WorkTypeService workTypeService;
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
     * @param workBasicFeeRuleFilter
     * @return
     */
    @Override
    public ListWrapper<WorkBasicFeeRuleDto> query(WorkBasicFeeRuleFilter workBasicFeeRuleFilter) {
        ListWrapper<WorkBasicFeeRuleDto> listWrapper = new ListWrapper<>();
        if (workBasicFeeRuleFilter == null) {
            return listWrapper;
        }

        Page page = new Page(workBasicFeeRuleFilter.getPageNum(), workBasicFeeRuleFilter.getPageSize());
        List<WorkBasicFeeRuleDto> dtoList = this.baseMapper.pageByFilter(page, workBasicFeeRuleFilter);
        if (CollectionUtil.isNotEmpty(dtoList)) {
            List<Long> corpIdList = new ArrayList<>();
            List<Long> customIdList = new ArrayList<>();
            for (WorkBasicFeeRuleDto dto : dtoList) {
                corpIdList.add(dto.getServiceCorp());
                corpIdList.add(dto.getCustomCorp());
                corpIdList.add(dto.getDemanderCorp());
                customIdList.add(dto.getCustomId());
            }
            Map<Long, String> corpMap = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList)).getData();
            Map<Long, String> customMap = this.demanderCustomService.mapIdAndCustomNameByIdList(customIdList);
            Map<String, String> areaMap = JsonUtil.parseObject(JsonUtil.toJson(uasFeignService.mapAreaCodeAndName().getData()), Map.class);
            Map<Long, String> modelMap = deviceFeignService.mapDeviceModel().getData();

            Map<Long, String> largeClassMap = deviceFeignService.mapLargeClassByCorpIdList(corpIdList).getData();
            Map<Long, String> smallClassMap = deviceFeignService.mapSmallClassByCorpIdList(corpIdList).getData();
            Map<Long, String> brandMap = deviceFeignService.mapDeviceBrandByCorpIdList(corpIdList).getData();
            Map<Long, String> deviceBranchMap = deviceBranchService.mapDeviceBranchByCorpIdList(corpIdList);
            Map<Integer, String> workTypeMap = this.workTypeService.mapWorkTypeByCorpIdList(corpIdList);
            dtoList.forEach(dto -> {
                dto.setWorkTypeName(StrUtil.trimToEmpty(workTypeMap.get(dto.getWorkType())));
                dto.setDemanderCorpName(StrUtil.trimToEmpty(corpMap.get(dto.getDemanderCorp())));
                dto.setCustomCorpName(StrUtil.trimToEmpty(customMap.get(dto.getCustomId())));
                if (LongUtil.isNotZero(dto.getCustomCorp())) {
                    dto.setCustomCorpName(StrUtil.trimToEmpty(corpMap.get(dto.getCustomCorp())));
                }
                dto.setServiceCorpName(StrUtil.trimToEmpty(corpMap.get(dto.getServiceCorp())));
                dto.setLargeClassName(StrUtil.trimToEmpty(largeClassMap.get(dto.getLargeClassId())));

                dto.setSmallClassName(workDispatchServiceCorpService.getName(dto.getSmallClassId(), smallClassMap, ","));
                dto.setBrandName(workDispatchServiceCorpService.getName(dto.getBrandId(), brandMap, ","));
                dto.setModelName(workDispatchServiceCorpService.getName(dto.getModelId(), modelMap, ","));
                dto.setDeviceBranchName(workDispatchServiceCorpService.getName(dto.getDeviceBranch(), deviceBranchMap, ","));
                dto.setDistrictName(workDispatchServiceCorpService.getDistrictName(dto.getDistrict(), areaMap, "-"));
                dto.setServiceModeName(ServiceModeEnum.getNameByCode(dto.getServiceMode()));
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
     * @param workBasicFeeRuleDto
     */
    @Override
    public void add(WorkBasicFeeRuleDto workBasicFeeRuleDto) {
        Long conditionId = KeyUtil.getId();
        WorkBasicFeeRule workBasicFeeRule = new WorkBasicFeeRule();
        if (LongUtil.isNotZero(workBasicFeeRuleDto.getCustomId())) {
            DemanderCustom demanderCustom = this.demanderCustomService.getById(workBasicFeeRuleDto.getCustomId());
            if (demanderCustom != null) {
                workBasicFeeRuleDto.setCustomCorp(demanderCustom.getCustomCorp());
            }
        }
        BeanUtils.copyProperties(workBasicFeeRuleDto, workBasicFeeRule);
        workBasicFeeRule.setRuleId(KeyUtil.getId());
        workBasicFeeRule.setConditionId(conditionId);
        this.save(workBasicFeeRule);

        WorkCondition workCondition = new WorkCondition();
        BeanUtils.copyProperties(workBasicFeeRuleDto, workCondition);
        workCondition.setId(conditionId);
        workConditionService.save(workCondition);
    }

    /**
     * 更新
     *
     * @author canlei
     * @param workBasicFeeRuleDto
     */
    @Override
    public void update(WorkBasicFeeRuleDto workBasicFeeRuleDto) {
        // 先删除条件配置
        this.workConditionService.delById(workBasicFeeRuleDto.getConditionId());
        WorkBasicFeeRule workBasicFeeRule = new WorkBasicFeeRule();
        if (LongUtil.isNotZero(workBasicFeeRuleDto.getCustomId())) {
            DemanderCustom demanderCustom = this.demanderCustomService.getById(workBasicFeeRuleDto.getCustomId());
            if (demanderCustom != null) {
                workBasicFeeRuleDto.setCustomCorp(demanderCustom.getCustomCorp());
            }
        }
        BeanUtils.copyProperties(workBasicFeeRuleDto, workBasicFeeRule);
        this.updateById(workBasicFeeRule);

        WorkCondition workCondition = new WorkCondition();
        BeanUtils.copyProperties(workBasicFeeRuleDto, workCondition);
        workCondition.setId(workBasicFeeRuleDto.getConditionId());
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
        WorkBasicFeeRule workBasicFeeRule = this.getById(ruleId);
        if (workBasicFeeRule == null) {
            throw new AppException("规则不存在，请检查！");
        }
        this.removeById(ruleId);
        // 同时删除条件
        this.workConditionService.removeById(workBasicFeeRule.getConditionId());
    }

    /**
     * 匹配工单基础服务费用规则
     *
     * @author canlei
     * @param workDto
     * @return
     */
    @Override
    public WorkBasicFeeRuleDto matchWorkBasicFeeRule(WorkDto workDto) {
        if (workDto == null || LongUtil.isZero(workDto.getServiceCorp())) {
            return null;
        }
        List<WorkBasicFeeRuleDto> list = this.baseMapper.matchWorkBasicFeeRule(workDto);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

}
