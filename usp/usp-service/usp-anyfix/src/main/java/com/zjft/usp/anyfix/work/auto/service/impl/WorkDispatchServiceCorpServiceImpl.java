package com.zjft.usp.anyfix.work.auto.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.model.WorkCondition;
import com.zjft.usp.anyfix.baseinfo.service.WorkConditionService;
import com.zjft.usp.anyfix.baseinfo.service.WorkTypeService;
import com.zjft.usp.anyfix.common.feign.dto.DeviceSmallClassDto;
import com.zjft.usp.anyfix.corp.branch.service.DeviceBranchService;
import com.zjft.usp.anyfix.corp.manage.model.DemanderService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderServiceService;
import com.zjft.usp.anyfix.work.auto.dto.WorkDispatchServiceCorpDto;
import com.zjft.usp.anyfix.work.auto.filter.WorkDispatchServiceCorpFilter;
import com.zjft.usp.anyfix.work.auto.mapper.WorkDispatchServiceCorpMapper;
import com.zjft.usp.anyfix.work.auto.model.WorkDispatchServiceCorp;
import com.zjft.usp.anyfix.work.auto.service.WorkDispatchServiceCorpService;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.operate.enums.WorkOperateTypeEnum;
import com.zjft.usp.anyfix.work.operate.model.WorkOperate;
import com.zjft.usp.anyfix.work.operate.service.WorkOperateService;
import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
import com.zjft.usp.anyfix.work.request.model.WorkRequest;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.device.dto.DeviceInfoDto;
import com.zjft.usp.device.service.DeviceFeignService;
import com.zjft.usp.uas.dto.CorpDto;
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
 * @author ljzhu
 * @date 2019-09-26 18:21
 * @note
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkDispatchServiceCorpServiceImpl extends ServiceImpl<WorkDispatchServiceCorpMapper, WorkDispatchServiceCorp> implements WorkDispatchServiceCorpService {

    @Resource
    private WorkDispatchServiceCorpMapper workDispatchServiceCorpMapper;


    @Resource
    private WorkConditionService workConditionService;

    @Resource
    private UasFeignService uasFeignService;

    @Resource
    private DeviceFeignService deviceFeignService;

    @Resource
    private DeviceBranchService deviceBranchService;

    @Resource
    private WorkDealService workDealService;

    @Autowired
    private WorkOperateService workOperateService;

    @Resource
    private WorkTypeService workTypeService;
    @Resource
    private DemanderServiceService demanderServiceService;
    @Override
    public ListWrapper<WorkDispatchServiceCorpDto> query(WorkDispatchServiceCorpFilter workDispatchServiceCorpFilter) {
        Page<WorkDispatchServiceCorpDto> page = new Page(workDispatchServiceCorpFilter.getPageNum(), workDispatchServiceCorpFilter.getPageSize());
        List<WorkDispatchServiceCorpDto> workDispatchServiceCorpDtoList = workDispatchServiceCorpMapper.query(workDispatchServiceCorpFilter, page);
        if (CollectionUtil.isNotEmpty(workDispatchServiceCorpDtoList)) {
            List<Long> corpIdList = new ArrayList<>();
            for (WorkDispatchServiceCorpDto dto : workDispatchServiceCorpDtoList) {
                corpIdList.add(dto.getServiceCorp());
                corpIdList.add(dto.getCustomCorp());
                corpIdList.add(dto.getDemanderCorp());
            }
            Map<Long, String> corpMap = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList)).getData();
            Map<String, String> areaMap = JsonUtil.parseObject(JsonUtil.toJson(uasFeignService.mapAreaCodeAndName().getData()), Map.class);
            Map<Long, String> modelMap = deviceFeignService.mapDeviceModel().getData();
            Map<Long, String> largeClassMap = this.deviceFeignService.mapLargeClassByCorpIdList(corpIdList).getData();
            Map<Long, String> smallClassMap = this.deviceFeignService.mapSmallClassByCorpIdList(corpIdList).getData();
            Map<Long, String> brandMap = this.deviceFeignService.mapDeviceBrandByCorpIdList(corpIdList).getData();
            Map<Long, String> deviceBranchMap = this.deviceBranchService.mapDeviceBranchByCorpIdList(corpIdList);
            Map<Integer, String> workTypeMap = this.workTypeService.mapWorkTypeByCorpIdList(corpIdList);
            for (WorkDispatchServiceCorpDto dto : workDispatchServiceCorpDtoList) {
                dto.setDemanderCorpName(StrUtil.trimToEmpty(corpMap.get(dto.getDemanderCorp())));
                dto.setCustomCorpName(StrUtil.trimToEmpty(corpMap.get(dto.getCustomCorp())));
                dto.setServiceCorpName(StrUtil.trimToEmpty(corpMap.get(dto.getServiceCorp())));
                dto.setLargeClassName(StrUtil.trimToEmpty(largeClassMap.get(dto.getLargeClassId())));
                dto.setSmallClassName(this.getName(dto.getSmallClassId(), smallClassMap, ","));
                dto.setBrandName(this.getName(dto.getBrandId(), brandMap, ","));
                dto.setModelName(this.getName(dto.getModelId(), modelMap, ","));
                dto.setDeviceBranchName(this.getName(String.valueOf(dto.getDeviceBranch()), deviceBranchMap, ","));
                dto.setDistrictName(this.getDistrictName(dto.getDistrict(), areaMap, "-"));
                dto.setWorkTypeName(this.getIntegerName(dto.getWorkType(), workTypeMap, ","));
            }
        }
        return ListWrapper.<WorkDispatchServiceCorpDto>builder()
                .list(workDispatchServiceCorpDtoList)
                .total(page.getTotal())
                .build();
    }


    /**
     * 根据编号以逗号分隔的字符串id获得对应的name字符串，以指定的连接符连接
     *
     * @param id
     * @param map
     * @param connector
     * @return
     */
    @Override
    public String getName(String id, Map<Long, String> map, String connector) {
        String value = "";
        if (id != null && id.trim().length() > 0) {
            String[] ids = id.split(",");

            for (int i = 0; i < ids.length; i++) {
                if (ids[i] != null && ids[i].trim().length() > 0) {
                    String name = StrUtil.trimToEmpty(map.get(Long.parseLong(ids[i])));
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
     * 获取位置信息名称
     *
     * @param district
     * @param areaMap
     * @param connector
     * @return
     */
    @Override
    public String getDistrictName(String district, Map<String, String> areaMap, String connector) {
        String names = "";
        if (district != null && district.trim().length() > 0) {
            String[] ids = district.split(",");

            for (int i = 0; i < ids.length; i++) {
                if (ids[i] != null && ids[i].trim().length() > 0) {
                    String area = StrUtil.trimToEmpty(ids[i]);
                    String name = "";
                    if (area.length() >= 2) {
                        String provinceName = StrUtil.trimToEmpty(areaMap.get(area.substring(0, 2)));
                        name = StrUtil.isNotBlank(name) ? name + provinceName : provinceName;
                    }
                    if (area.length() >= 4) {
                        String cityName = StrUtil.trimToEmpty(areaMap.get(area.substring(0, 4)));
                        name = StrUtil.isNotBlank(name) ? name + cityName : cityName;
                    }
                    if (area.length() >= 6) {
                        String districtName = StrUtil.trimToEmpty(areaMap.get(area.substring(0, 6)));
                        name = StrUtil.isNotBlank(name) ? name + districtName : districtName;
                    }
                    if (names.length() > 0) {
                        names += ",";
                    }
                    names += name;
                }
            }
        }
        return names;
    }

    @Override
    public WorkDispatchServiceCorpDto getById(Long id) {
        WorkDispatchServiceCorp workDispatchServiceCorp = workDispatchServiceCorpMapper.selectById(id);
        WorkCondition workCondition = workConditionService.getById(workDispatchServiceCorp.getConditionId());
        WorkDispatchServiceCorpDto workDispatchServiceCorpDto = new WorkDispatchServiceCorpDto();
        BeanUtils.copyProperties(workDispatchServiceCorp, workDispatchServiceCorpDto);
        BeanUtils.copyProperties(workCondition, workDispatchServiceCorpDto);
        return workDispatchServiceCorpDto;
    }

    @Override
    public void add(WorkDispatchServiceCorpDto workDispatchServiceCorp) {
        workDispatchServiceCorp.setId(KeyUtil.getId());
        // 条件配置主键
        workDispatchServiceCorp.setConditionId(KeyUtil.getId());
        workDispatchServiceCorpMapper.insert(workDispatchServiceCorp);
        workConditionService.addWorkCondition(workDispatchServiceCorp, workDispatchServiceCorp.getConditionId());
    }

    @Override
    public void mod(WorkDispatchServiceCorpDto workDispatchServiceCorp) {
        // 先删除条件配置
        workConditionService.delById(workDispatchServiceCorp.getConditionId());
        workDispatchServiceCorpMapper.updateById(workDispatchServiceCorp);
        workConditionService.addWorkCondition(workDispatchServiceCorp, workDispatchServiceCorp.getConditionId());
    }

    @Override
    public void delById(Long id) {
        WorkDispatchServiceCorp workDispatchServiceCorp = workDispatchServiceCorpMapper.selectById(id);
        // 先删除条件配置
        workConditionService.delById(workDispatchServiceCorp.getConditionId());
        workDispatchServiceCorpMapper.deleteById(id);
    }

    /**
     * 根据工单请求信息得到匹配的服务商
     *
     * @param workRequest
     * @param workDeal
     * @return
     * @author zgpi
     * @date 2019/11/14 13:59
     **/
    private WorkDispatchServiceCorpDto matchServiceCorp(WorkRequest workRequest, WorkDeal workDeal) {
        WorkDispatchServiceCorpDto workDispatchServiceCorpDto = new WorkDispatchServiceCorpDto();
        workDispatchServiceCorpDto.setDemanderCorp(workDeal.getDemanderCorp());
        workDispatchServiceCorpDto.setServiceCorp(workDeal.getServiceCorp());
        workDispatchServiceCorpDto.setCustomCorp(workRequest.getCustomCorp());
        workDispatchServiceCorpDto.setWorkType(String.valueOf(workRequest.getWorkType()));

        Long smallClassId = workRequest.getSmallClass();
        Result smallClassResult = deviceFeignService.findDeviceSmallClass(smallClassId);
        DeviceSmallClassDto deviceSmallClassDto = JsonUtil.parseObject(JsonUtil.toJson(smallClassResult.getData()), DeviceSmallClassDto.class);
        workDispatchServiceCorpDto.setLargeClassId(deviceSmallClassDto.getLargeClassId());
        workDispatchServiceCorpDto.setSmallClassId(String.valueOf(smallClassId));
        workDispatchServiceCorpDto.setBrandId(String.valueOf(workRequest.getBrand()));
        workDispatchServiceCorpDto.setModelId(String.valueOf(workRequest.getModel()));
        workDispatchServiceCorpDto.setDistrict(workRequest.getDistrict());
        workDispatchServiceCorpDto.setDeviceBranch(String.valueOf(workRequest.getDeviceBranch()));

        List<WorkDispatchServiceCorpDto> list = workDispatchServiceCorpMapper
                .matchServiceCorp(workDispatchServiceCorpDto);

        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 自动分配服务商
     *
     * @param workRequest
     * @param workDeal
     * @return
     * @author zgpi
     * @date 2019/11/13 18:41
     **/
    @Override
    public Long autoGetServiceCorp(WorkRequest workRequest, WorkDeal workDeal) {
        Long serviceCorp = 0L;
        if(LongUtil.isNotZero(workRequest.getDemanderCorp())) {
            if(!workRequest.getDemanderCorp().equals(workRequest.getCreatorCorpId())) {
                QueryWrapper<DemanderService> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("demander_corp",workRequest.getDemanderCorp())
                        .eq("service_corp",workRequest.getCreatorCorpId());
                DemanderService demanderService = demanderServiceService.getOne(queryWrapper);
                if(ObjectUtil.isNotNull(demanderService)) {
                    serviceCorp = workRequest.getCreatorCorpId();
                }
            }
        }
        // 先获得设备档案中的服务商
        if(LongUtil.isZero(serviceCorp)) {
            if (LongUtil.isNotZero(workDeal.getDeviceId())) {
                Result deviceInfoResult = deviceFeignService.findDeviceInfo(workDeal.getDeviceId());
                DeviceInfoDto deviceInfoDto = JsonUtil.parseObject(JsonUtil.toJson(deviceInfoResult.getData()), DeviceInfoDto.class);
                if (deviceInfoDto != null && LongUtil.isNotZero(deviceInfoDto.getServiceCorp())) {
                    serviceCorp = deviceInfoDto.getServiceCorp();
                }
            }
            // 若设备档案中没有服务商，则从规则中获取
            if (LongUtil.isZero(serviceCorp)) {
                WorkDispatchServiceCorpDto dto = this.matchServiceCorp(workRequest, workDeal);
                if (dto != null) {
                    serviceCorp = dto.getServiceCorp();
                }
            }
        }
        if (LongUtil.isNotZero(serviceCorp)) {
            workDeal.setServiceCorp(serviceCorp);
            workDeal.setWorkStatus(WorkStatusEnum.TO_HANDLE.getCode());
            workDeal.setDispatchTime(DateUtil.date());
            workDealService.updateById(workDeal);

            WorkOperate workOperate = new WorkOperate();
            workOperate.setWorkId(workDeal.getWorkId());
            workOperate.setWorkStatus(WorkStatusEnum.TO_HANDLE.getCode());
            workOperate.setOperateType(WorkOperateTypeEnum.AUTO_DISPATCH.getCode());

            Result corpResult = uasFeignService.findCorpById(workDeal.getServiceCorp());
            CorpDto corpDto = JsonUtil.parseObject(JsonUtil.toJson(corpResult.getData()), CorpDto.class);
            workOperate.setSummary("自动提交服务商");
            if (StrUtil.isNotBlank(corpDto.getCorpName())) {
                workOperate.setSummary(workOperate.getSummary() + " " + corpDto.getCorpName());
            }
            workOperateService.addAutoWorkOperate(workOperate);
            return serviceCorp;
        }
        return null;
    }

    /**
     * 根据map<Integer, String>和逗号隔开的id获取id对应的名称
     *
     * @param id
     * @param map
     * @param connector
     * @return
     */
    @Override
    public String getIntegerName(String id, Map<Integer, String> map, String connector) {
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

}
