package com.zjft.usp.anyfix.work.fee.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.enums.EnabledEnum;
import com.zjft.usp.anyfix.baseinfo.model.ServiceItem;
import com.zjft.usp.anyfix.baseinfo.model.WorkCondition;
import com.zjft.usp.anyfix.baseinfo.service.ServiceItemService;
import com.zjft.usp.anyfix.baseinfo.service.WorkConditionService;
import com.zjft.usp.anyfix.baseinfo.service.WorkTypeService;
import com.zjft.usp.anyfix.corp.branch.service.DeviceBranchService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderCustomService;
import com.zjft.usp.anyfix.work.auto.service.WorkDispatchServiceCorpService;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeAssortDto;
import com.zjft.usp.anyfix.work.fee.filter.WorkFeeAssortFilter;
import com.zjft.usp.anyfix.work.fee.mapper.WorkFeeAssortDefineMapper;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeAssortDefine;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeDetail;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeAssortDefineService;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeDetailService;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.anyfix.work.request.enums.ServiceModeEnum;
import com.zjft.usp.anyfix.work.request.enums.ZoneEnum;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.IntUtil;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 分类费用定义 服务实现类
 * </p>
 *
 * @author canlei
 * @since 2020-04-17
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkFeeAssortDefineServiceImpl extends ServiceImpl<WorkFeeAssortDefineMapper, WorkFeeAssortDefine> implements WorkFeeAssortDefineService {

    @Autowired
    private DemanderCustomService demanderCustomService;
    @Autowired
    private DeviceBranchService deviceBranchService;
    @Autowired
    private WorkTypeService workTypeService;
    @Autowired
    private WorkDispatchServiceCorpService workDispatchServiceCorpService;
    @Autowired
    private WorkConditionService workConditionService;
    @Autowired
    private WorkFeeDetailService workFeeDetailService;
    @Autowired
    private ServiceItemService serviceItemService;

    @Resource
    private UasFeignService uasFeignService;
    @Resource
    private DeviceFeignService deviceFeignService;

    /**
     * 分页查询
     *
     * @param workFeeAssortFilter
     * @return
     */
    @Override
    public ListWrapper<WorkFeeAssortDto> query(WorkFeeAssortFilter workFeeAssortFilter) {
        ListWrapper<WorkFeeAssortDto> listWrapper = new ListWrapper<>();
        Page page = new Page(workFeeAssortFilter.getPageNum(), workFeeAssortFilter.getPageSize());
        List<WorkFeeAssortDto> dtoList = this.baseMapper.pageByFilter(page, workFeeAssortFilter);

        if (CollectionUtil.isNotEmpty(dtoList)) {
            // 获取相关企业编号列表
            List<Long> corpIdList = new ArrayList<>();
            List<Long> customIdList = new ArrayList<>();
            Set<Long> specificationSet = new HashSet<>();
            for (WorkFeeAssortDto dto : dtoList) {
                corpIdList.add(dto.getServiceCorp());
                corpIdList.add(dto.getCustomCorp());
                corpIdList.add(dto.getDemanderCorp());
                customIdList.add(dto.getCustomId());
                if (StrUtil.isNotBlank(dto.getSpecification())) {
                    List<Long> specifications = Arrays.asList(dto.getSpecification().split(","))
                            .stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                    specificationSet.addAll(specifications);
                }
            }
            List<Long> specificationList = new ArrayList<>(specificationSet);
            // 企业映射
            Result<Map<Long, String>> corpMapResult = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList));
            Map<Long, String> corpMap = new HashMap<>();
            if (corpMapResult != null && corpMapResult.getCode() == 0) {
                corpMap = corpMapResult.getData();
            }
            // 客户映射
            Map<Long, String> customMap = this.demanderCustomService.mapIdAndCustomNameByIdList(customIdList);
            // 地区映射
            Result areaMapResult = uasFeignService.mapAreaCodeAndName();
            Map<String, String> areaMap = new HashMap<>();
            if (areaMapResult != null && areaMapResult.getCode() == 0) {
                areaMap = JsonUtil.parseObject(JsonUtil.toJson(areaMapResult.getData()), Map.class);
            }
            // 设备型号映射
            Result<Map<Long, String>> modelMapResult = deviceFeignService.mapDeviceModel();
            Map<Long, String> modelMap = new HashMap<>();
            if (modelMapResult != null && modelMapResult.getCode() == 0) {
                modelMap = modelMapResult.getData();
            }
            // 设备规格映射
            String specificationJson = JsonUtil.toJson(specificationList);
            Result<Map<Long, String>> specificationMapResult = deviceFeignService.mapSpecIdAndSmallClassSpecName(specificationJson);
            Map<Long, String> specificationMap = new HashMap<>();
            if (specificationMapResult != null && specificationMapResult.getCode() == 0) {
                specificationMap = specificationMapResult.getData();
            }
            // 设备大类映射
            Result<Map<Long, String>> largeClassMapResult = deviceFeignService.mapLargeClassByCorpIdList(corpIdList);
            Map<Long, String> largeClassMap = new HashMap<>();
            if (largeClassMapResult != null && largeClassMapResult.getCode() == 0) {
                largeClassMap = largeClassMapResult.getData();
            }
            // 设备小类映射
            Result<Map<Long, String>> smallClassMapResult = deviceFeignService.mapSmallClassByCorpIdList(corpIdList);
            Map<Long, String> smallClassMap = new HashMap<>();
            if (smallClassMapResult != null && smallClassMapResult.getCode() == 0) {
                smallClassMap = smallClassMapResult.getData();
            }
            // 设备品牌映射
            Result<Map<Long, String>> brandMapResult = deviceFeignService.mapDeviceBrandByCorpIdList(corpIdList);
            Map<Long, String> brandMap = new HashMap<>();
            if (brandMapResult != null && brandMapResult.getCode() == 0) {
                brandMap = brandMapResult.getData();
            }
            // 工单类型映射
            Map<Integer, String> workTypeMap = this.workTypeService.mapWorkTypeByCorpIdList(corpIdList);
            // 服务项目映射
            Map<Integer, String> serviceItemMap = this.serviceItemService.mapIdAndNameByCorp(workFeeAssortFilter.getServiceCorp());

            for (WorkFeeAssortDto dto: dtoList) {
                dto.setWorkTypeName(StrUtil.trimToEmpty(workTypeMap.get(dto.getWorkType())));
                dto.setDemanderCorpName(StrUtil.trimToEmpty(corpMap.get(dto.getDemanderCorp())));
                dto.setCustomCorpName(StrUtil.trimToEmpty(customMap.get(dto.getCustomId())));
                if (LongUtil.isNotZero(dto.getCustomCorp())) {
                    dto.setCustomCorpName(StrUtil.trimToEmpty(corpMap.get(dto.getCustomCorp())));
                }
                dto.setServiceCorpName(StrUtil.trimToEmpty(corpMap.get(dto.getServiceCorp())));
                dto.setLargeClassName(StrUtil.trimToEmpty(largeClassMap.get(dto.getLargeClassId())));
                dto.setWorkTypeName(getName(dto.getWorkType(), workTypeMap, ","));
                dto.setSmallClassName(workDispatchServiceCorpService.getName(dto.getSmallClassId(), smallClassMap, ","));
                dto.setSpecificationName(workDispatchServiceCorpService.getName(dto.getSpecification(), specificationMap, ","));
                dto.setBrandName(workDispatchServiceCorpService.getName(dto.getBrandId(), brandMap, ","));
                dto.setModelName(workDispatchServiceCorpService.getName(dto.getModelId(), modelMap, ","));
                dto.setDistrictName(workDispatchServiceCorpService.getDistrictName(dto.getDistrict(), areaMap, "-"));
                dto.setServiceModeName(ServiceModeEnum.getNameByCode(dto.getServiceMode()));
                dto.setZoneName(ZoneEnum.getNameByCode(dto.getZone()));
                dto.setServiceItemName(StrUtil.trimToEmpty(serviceItemMap.get(dto.getServiceItem())));
            }
        }
        listWrapper.setList(dtoList);
        listWrapper.setTotal(page.getTotal());
        return listWrapper;
    }

    /**
     * 添加
     *
     * @param workFeeAssortDto
     * @param userId
     * @param serviceCorp
     */
    @Override
    public void add(WorkFeeAssortDto workFeeAssortDto, Long userId, Long serviceCorp) {
        // 默认服务商为当前企业
        if (LongUtil.isZero(workFeeAssortDto.getServiceCorp())) {
            workFeeAssortDto.setServiceCorp(serviceCorp);
        }
        // 保存条件
        WorkCondition workCondition = new WorkCondition();
        BeanUtils.copyProperties(workFeeAssortDto, workCondition);
        workCondition.setId(KeyUtil.getId());
        this.workConditionService.save(workCondition);
        // 保存分类费用定义
        Long assortId = KeyUtil.getId();
        WorkFeeAssortDefine workFeeAssortDefine = new WorkFeeAssortDefine();
        BeanUtils.copyProperties(workFeeAssortDto, workFeeAssortDefine);
        workFeeAssortDefine.setConditionId(workCondition.getId());
        workFeeAssortDefine.setAssortId(assortId);
        workFeeAssortDefine.setOperator(userId);
        workFeeAssortDefine.setOperateTime(DateTime.now());
        this.save(workFeeAssortDefine);
        // 可用状态，则初始化满足条件的未审核工单的该项费用
        if (EnabledEnum.YES.getCode().equals(workFeeAssortDto.getEnabled())) {
            workFeeAssortDto.setAssortId(workFeeAssortDefine.getAssortId());
            this.workFeeDetailService.addDetailByAssort(workFeeAssortDto, userId);
        }
    }

    /**
     * 更新分类费用
     *
     * @param workFeeAssortDto
     * @param userId
     * @param serviceCorp
     */
    @Override
    public void update(WorkFeeAssortDto workFeeAssortDto, Long userId, Long serviceCorp) {
        if (LongUtil.isZero(workFeeAssortDto.getAssortId())) {
            throw new AppException("分类编号不能为空");
        }
        if (LongUtil.isZero(workFeeAssortDto.getServiceCorp())) {
            workFeeAssortDto.setServiceCorp(serviceCorp);
        }
        WorkFeeAssortDefine workFeeAssortDefineOld = this.getById(workFeeAssortDto.getAssortId());
        if (workFeeAssortDefineOld == null) {
            throw new AppException("分类费用不存在，请检查");
        }
        // 仅更新是否可用
        if (EnabledEnum.YES.getCode().equals(workFeeAssortDto.getUpdateEnabled())) {
            workFeeAssortDefineOld.setEnabled(workFeeAssortDto.getEnabled());
            this.updateById(workFeeAssortDefineOld);
            // 可用状态，则初始化满足条件的未审核工单的该项费用
            if (EnabledEnum.YES.getCode().equals(workFeeAssortDto.getEnabled())) {
                this.workFeeDetailService.updateDetailByAssort(workFeeAssortDto, userId);
                // 不可用状态，则删除未审核工单的该项费用
            } else if (EnabledEnum.NO.getCode().equals(workFeeAssortDto.getEnabled())) {
                this.workFeeDetailService.delUncheckDetailByAssort(workFeeAssortDto.getAssortId());
            }
            return;
        }
        List<WorkFeeDetail> checkedFeeDetailList = this.workFeeDetailService.listCheckedByAssortId(workFeeAssortDto.getAssortId());
        if (CollectionUtil.isNotEmpty(checkedFeeDetailList)) {
            throw new AppException("该费用已有已审核工单引用，不能修改，请检查！");
        }

        WorkCondition workConditionOld = this.workConditionService.getById(workFeeAssortDto.getConditionId());
        // 将null值更新为默认值，避免更新时忽略
        this.updateNullValues(workFeeAssortDto);
        BeanUtils.copyProperties(workFeeAssortDto, workFeeAssortDefineOld);
        BeanUtils.copyProperties(workFeeAssortDto, workConditionOld);
        this.updateById(workFeeAssortDefineOld);
        this.workConditionService.updateById(workConditionOld);
        // 先删除有未审核工单的该项费用，再进行添加操作
        this.workFeeDetailService.delUncheckDetailByAssort(workFeeAssortDto.getAssortId());
        // 可用状态，则初始化满足条件的未审核工单的该项费用
        if (EnabledEnum.YES.getCode().equals(workFeeAssortDto.getEnabled())) {
            this.workFeeDetailService.updateDetailByAssort(workFeeAssortDto, userId);
        }
    }

    /**
     * 匹配工单分类费用
     *
     * @param workDto
     * @return
     */
    @Override
    public List<WorkFeeAssortDto> matchWorkFeeAssortDefine(WorkDto workDto) {
        if (workDto == null || LongUtil.isZero(workDto.getWorkId())) {
            return null;
        }
        return this.baseMapper.matchWorkFeeAssortDefine(workDto);
    }

    /**
     * 根据服务商企业编号获取费用名称编号与名称的映射
     *
     * @param serviceCorp
     * @return
     */
    @Override
    public Map<Long, String> mapIdAndNameByServiceCorp(Long serviceCorp) {
        Map<Long, String> map = new HashMap<>();
        if (LongUtil.isZero(serviceCorp)) {
            return map;
        }
        List<WorkFeeAssortDefine> list = this.list(new QueryWrapper<WorkFeeAssortDefine>().eq("service_corp", serviceCorp));
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(workFeeAssortDefine -> {
                map.put(workFeeAssortDefine.getAssortId(), workFeeAssortDefine.getAssortName());
            });
        }
        return map;
    }

    /**
     * 查询满足分类费用定义的未审核工单
     *
     * @param workFeeAssortDto
     * @return
     */
    @Override
    public List<WorkDto> selectMatchWork(WorkFeeAssortDto workFeeAssortDto) {
        return this.baseMapper.selectMatchWork(workFeeAssortDto);
    }

    /**
     * 获取分类费用规则明细
     *
     * @param assortId
     * @return
     */
    @Override
    public WorkFeeAssortDto getDtoById(Long assortId) {
        if (LongUtil.isZero(assortId)) {
            throw new AppException("分类费用编号不存在，请检查");
        }
        WorkFeeAssortDefine workFeeAssortDefine = this.getById(assortId);
        if (workFeeAssortDefine == null) {
            throw new AppException("分类费用规则不存在，请检查");
        }
        WorkCondition workCondition = this.workConditionService.getById(workFeeAssortDefine.getConditionId());
        WorkFeeAssortDto workFeeAssortDto = new WorkFeeAssortDto();
        BeanUtils.copyProperties(workFeeAssortDefine, workFeeAssortDto);
        BeanUtils.copyProperties(workCondition, workFeeAssortDto);
        // 获取相关企业编号列表
        List<Long> corpIdList = new ArrayList<>();
        List<Long> customIdList = new ArrayList<>();
        Set<Long> specificationSet = new HashSet<>();
        corpIdList.add(workFeeAssortDto.getServiceCorp());
        corpIdList.add(workFeeAssortDto.getCustomCorp());
        corpIdList.add(workFeeAssortDto.getDemanderCorp());
        customIdList.add(workFeeAssortDto.getCustomId());
        if (StrUtil.isNotBlank(workFeeAssortDto.getSpecification())) {
            List<Long> specifications = Arrays.asList(workFeeAssortDto.getSpecification().split(","))
                    .stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            specificationSet.addAll(specifications);
        }
        List<Long> specificationList = new ArrayList<>(specificationSet);
        // 企业映射
        Result<Map<Long, String>> corpMapResult = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList));
        Map<Long, String> corpMap = new HashMap<>();
        if (corpMapResult != null && corpMapResult.getCode() == 0) {
            corpMap = corpMapResult.getData();
        }
        // 客户映射
        Map<Long, String> customMap = this.demanderCustomService.mapIdAndCustomNameByIdList(customIdList);
        // 地区映射
        Result areaMapResult = uasFeignService.mapAreaCodeAndName();
        Map<String, String> areaMap = new HashMap<>();
        if (areaMapResult != null && areaMapResult.getCode() == 0) {
            areaMap = JsonUtil.parseObject(JsonUtil.toJson(areaMapResult.getData()), Map.class);
        }
        // 设备型号映射
        Result<Map<Long, String>> modelMapResult = deviceFeignService.mapDeviceModel();
        Map<Long, String> modelMap = new HashMap<>();
        if (modelMapResult != null && modelMapResult.getCode() == 0) {
            modelMap = modelMapResult.getData();
        }
        // 设备规格映射
        String specificationJson = JsonUtil.toJson(specificationList);
        Result<Map<Long, String>> specificationMapResult = deviceFeignService.mapSpecIdAndSmallClassSpecName(specificationJson);
        Map<Long, String> specificationMap = new HashMap<>();
        if (specificationMapResult != null && specificationMapResult.getCode() == 0) {
            specificationMap = specificationMapResult.getData();
        }
        // 设备大类映射
        Result<Map<Long, String>> largeClassMapResult = deviceFeignService.mapLargeClassByCorpIdList(corpIdList);
        Map<Long, String> largeClassMap = new HashMap<>();
        if (largeClassMapResult != null && largeClassMapResult.getCode() == 0) {
            largeClassMap = largeClassMapResult.getData();
        }
        // 设备小类映射
        Result<Map<Long, String>> smallClassMapResult = deviceFeignService.mapSmallClassByCorpIdList(corpIdList);
        Map<Long, String> smallClassMap = new HashMap<>();
        if (smallClassMapResult != null && smallClassMapResult.getCode() == 0) {
            smallClassMap = smallClassMapResult.getData();
        }
        // 设备品牌映射
        Result<Map<Long, String>> brandMapResult = deviceFeignService.mapDeviceBrandByCorpIdList(corpIdList);
        Map<Long, String> brandMap = new HashMap<>();
        if (brandMapResult != null && brandMapResult.getCode() == 0) {
            brandMap = brandMapResult.getData();
        }
        // 工单类型映射
        Map<Integer, String> workTypeMap = this.workTypeService.mapWorkTypeByCorpIdList(corpIdList);
        workFeeAssortDto.setWorkTypeName(StrUtil.trimToEmpty(workTypeMap.get(workFeeAssortDto.getWorkType())));
        workFeeAssortDto.setDemanderCorpName(StrUtil.trimToEmpty(corpMap.get(workFeeAssortDto.getDemanderCorp())));
        workFeeAssortDto.setCustomCorpName(StrUtil.trimToEmpty(customMap.get(workFeeAssortDto.getCustomId())));
        if (LongUtil.isNotZero(workFeeAssortDto.getCustomCorp())) {
            workFeeAssortDto.setCustomCorpName(StrUtil.trimToEmpty(corpMap.get(workFeeAssortDto.getCustomCorp())));
        }
        workFeeAssortDto.setServiceCorpName(StrUtil.trimToEmpty(corpMap.get(workFeeAssortDto.getServiceCorp())));
        workFeeAssortDto.setLargeClassName(StrUtil.trimToEmpty(largeClassMap.get(workFeeAssortDto.getLargeClassId())));
        workFeeAssortDto.setWorkTypeName(getName(workFeeAssortDto.getWorkType(), workTypeMap, ","));
        workFeeAssortDto.setSmallClassName(workDispatchServiceCorpService.getName(workFeeAssortDto.getSmallClassId(), smallClassMap, ","));
        workFeeAssortDto.setSpecificationName(workDispatchServiceCorpService.getName(workFeeAssortDto.getSpecification(), specificationMap, ","));
        workFeeAssortDto.setBrandName(workDispatchServiceCorpService.getName(workFeeAssortDto.getBrandId(), brandMap, ","));
        workFeeAssortDto.setModelName(workDispatchServiceCorpService.getName(workFeeAssortDto.getModelId(), modelMap, ","));
        workFeeAssortDto.setDistrictName(workDispatchServiceCorpService.getDistrictName(workFeeAssortDto.getDistrict(), areaMap, "-"));
        workFeeAssortDto.setServiceModeName(ServiceModeEnum.getNameByCode(workFeeAssortDto.getServiceMode()));
        workFeeAssortDto.setZoneName(ZoneEnum.getNameByCode(workFeeAssortDto.getZone()));
        // 获取服务项目
        if (IntUtil.isNotZero(workFeeAssortDto.getServiceItem())) {
            ServiceItem serviceItem = this.serviceItemService.getById(workFeeAssortDto.getServiceItem());
            if (serviceItem != null) {
                workFeeAssortDto.setServiceItemName(serviceItem.getName());
            }
        }
        List<WorkFeeDetail> workFeeDetailList = this.workFeeDetailService.listCheckedByAssortId(assortId);
        if (CollectionUtil.isNotEmpty(workFeeDetailList)) {
            workFeeAssortDto.setUsed(true);
        }
        return workFeeAssortDto;
    }

    /**
     * 根据map<Integer, String>和逗号隔开的id获取id对应的名称
     *
     * @param id
     * @param map
     * @param connector
     * @return
     */
    private String getName(String id, Map<Integer, String> map, String connector) {
        String value = "";
        if (id != null && id.trim().length() > 0) {
            String[] ids = id.split(",");

            for (int i = 0; i < ids.length; i++) {
                if (ids[i] != null && ids[i].trim().length() > 0) {
                    String name = StrUtil.trimToEmpty(map.get(Integer.parseInt(ids[i])));
                    if (i > 0) {
                        value += connector + name;
                    } else {
                        value += name;
                    }
                }
            }
        }
        return value;
    }

    /**
     * 将null值更改为默认值，以便更新时不会忽略
     *
     * @param workFeeAssortDto
     */
    private void updateNullValues(WorkFeeAssortDto workFeeAssortDto) {
        if (workFeeAssortDto.getServiceMode() == null) {
            workFeeAssortDto.setServiceMode(0);
        }
        if (workFeeAssortDto.getTogether() == null) {
            workFeeAssortDto.setTogether(EnabledEnum.NO.getCode());
        }
        if (workFeeAssortDto.getCustomId() == null) {
            workFeeAssortDto.setCustomId(0L);
        }
        if (workFeeAssortDto.getCustomCorp() == null) {
            workFeeAssortDto.setCustomCorp(0L);
        }
        if (workFeeAssortDto.getWorkType() == null) {
            workFeeAssortDto.setWorkType("");
        }
        if (workFeeAssortDto.getLargeClassId() == null) {
            workFeeAssortDto.setLargeClassId(0L);
        }
        if (workFeeAssortDto.getSmallClassId() == null) {
            workFeeAssortDto.setSmallClassId("");
        }
        if (workFeeAssortDto.getSpecification() == null) {
            workFeeAssortDto.setSpecification("");
        }
        if (workFeeAssortDto.getBrandId() == null) {
            workFeeAssortDto.setBrandId("");
        }
        if (workFeeAssortDto.getModelId() == null) {
            workFeeAssortDto.setModelId("");
        }
        if (workFeeAssortDto.getZone() == null) {
            workFeeAssortDto.setZone(0);
        }
        if (workFeeAssortDto.getServiceItem() == null) {
            workFeeAssortDto.setServiceItem(0);
        }
        if (workFeeAssortDto.getDistrict() == null) {
            workFeeAssortDto.setDistrict("");
        }
        if (workFeeAssortDto.getDistrictNegate() == null) {
            workFeeAssortDto.setDistrictNegate(EnabledEnum.NO.getCode());
        }
    }

}
