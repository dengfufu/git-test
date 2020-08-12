package com.zjft.usp.anyfix.work.auto.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.model.WorkCondition;
import com.zjft.usp.anyfix.baseinfo.service.WorkConditionService;
import com.zjft.usp.anyfix.baseinfo.service.WorkTypeService;
import com.zjft.usp.anyfix.common.feign.dto.DeviceSmallClassDto;
import com.zjft.usp.anyfix.corp.branch.model.ServiceBranch;
import com.zjft.usp.anyfix.corp.branch.service.DeviceBranchService;
import com.zjft.usp.anyfix.corp.branch.service.ServiceBranchService;
import com.zjft.usp.anyfix.work.auto.dto.WorkDispatchServiceBranchDto;
import com.zjft.usp.anyfix.work.auto.filter.WorkDispatchServiceBranchFilter;
import com.zjft.usp.anyfix.work.auto.mapper.WorkDispatchServiceBranchMapper;
import com.zjft.usp.anyfix.work.auto.model.WorkDispatchServiceBranch;
import com.zjft.usp.anyfix.work.auto.service.WorkDispatchServiceBranchService;
import com.zjft.usp.anyfix.work.auto.service.WorkDispatchServiceCorpService;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.operate.enums.WorkOperateTypeEnum;
import com.zjft.usp.anyfix.work.operate.model.WorkOperate;
import com.zjft.usp.anyfix.work.operate.service.WorkOperateService;
import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
import com.zjft.usp.anyfix.work.request.model.WorkRequest;
import com.zjft.usp.anyfix.work.transfer.enums.WorkHandleTypeEnum;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.device.dto.DeviceInfoDto;
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
 * @author ljzhu
 * @date 2019-09-26 18:21
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkDispatchServiceBranchServiceImpl extends ServiceImpl<WorkDispatchServiceBranchMapper, WorkDispatchServiceBranch> implements WorkDispatchServiceBranchService {

    @Resource
    private WorkDispatchServiceBranchMapper workDispatchServiceBranchMapper;

    @Resource
    private WorkConditionService workConditionService;

    @Resource
    private WorkDealService workDealService;

    @Resource
    private WorkDispatchServiceCorpService workDispatchServiceCorpService;

    @Resource
    private UasFeignService uasFeignService;

    @Resource
    private DeviceFeignService deviceFeignService;

    @Resource
    private DeviceBranchService deviceBranchService;

    @Resource
    private ServiceBranchService serviceBranchService;

    @Autowired
    private WorkOperateService workOperateService;

    @Resource
    private WorkTypeService workTypeService;

    /**
     * 分页查询自动分配服务商网点规则列表
     *
     * @param workDispatchServiceBranchFilter
     * @return
     * @author zgpi
     * @date 2019/11/14 09:43
     **/
    @Override
    public ListWrapper<WorkDispatchServiceBranchDto> query(WorkDispatchServiceBranchFilter workDispatchServiceBranchFilter) {
        Page<WorkDispatchServiceBranchDto> page = new Page(workDispatchServiceBranchFilter.getPageNum(), workDispatchServiceBranchFilter.getPageSize());
        List<WorkDispatchServiceBranchDto> workDispatchServiceBranchDtoList = workDispatchServiceBranchMapper.query(workDispatchServiceBranchFilter, page);
        if (CollectionUtil.isNotEmpty(workDispatchServiceBranchDtoList)) {
            List<Long> corpIdList = new ArrayList<>();
            for (WorkDispatchServiceBranchDto dto : workDispatchServiceBranchDtoList) {
                corpIdList.add(dto.getServiceCorp());
                corpIdList.add(dto.getCustomCorp());
                corpIdList.add(dto.getDemanderCorp());
            }
            Map<Long, String> corpMap = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList)).getData();
            Map<String, String> areaMap = JsonUtil.parseObject(JsonUtil.toJson(uasFeignService.mapAreaCodeAndName().getData()), Map.class);
            Map<Long, String> modelMap = deviceFeignService.mapDeviceModel().getData();

            Map<Long, String> largeClassMap = deviceFeignService.mapLargeClassByCorpIdList(corpIdList).getData();
            Map<Long, String> smallClassMap = deviceFeignService.mapSmallClassByCorpIdList(corpIdList).getData();
            Map<Long, String> brandMap = deviceFeignService.mapDeviceBrandByCorpIdList(corpIdList).getData();
            Map<Long, String> deviceBranchMap = deviceBranchService.mapDeviceBranchByCorpIdList(corpIdList);
            Map<Long, String> serviceBranchMap = serviceBranchService.mapServiceBranchByCorpIdList(corpIdList);

            Map<Integer, String> workTypeMap = this.workTypeService.mapWorkTypeByCorpIdList(corpIdList);

            for (WorkDispatchServiceBranchDto dto : workDispatchServiceBranchDtoList) {
                dto.setServiceBranchName(StrUtil.trimToEmpty(serviceBranchMap.get(dto.getServiceBranch())));
                dto.setDemanderCorpName(StrUtil.trimToEmpty(corpMap.get(dto.getDemanderCorp())));
                dto.setCustomCorpName(StrUtil.trimToEmpty(corpMap.get(dto.getCustomCorp())));
                dto.setServiceCorpName(StrUtil.trimToEmpty(corpMap.get(dto.getServiceCorp())));
                dto.setLargeClassName(StrUtil.trimToEmpty(largeClassMap.get(dto.getLargeClassId())));
                dto.setHandleTypeName(StrUtil.trimToEmpty(WorkHandleTypeEnum.getNameByCode(dto.getHandleType())));

                dto.setSmallClassName(workDispatchServiceCorpService.getName(dto.getSmallClassId(), smallClassMap, ","));
                dto.setBrandName(workDispatchServiceCorpService.getName(dto.getBrandId(), brandMap, ","));
                dto.setModelName(workDispatchServiceCorpService.getName(dto.getModelId(), modelMap, ","));
                dto.setDeviceBranchName(workDispatchServiceCorpService.getName(dto.getDeviceBranch(), deviceBranchMap, ","));
                dto.setDistrictName(workDispatchServiceCorpService.getDistrictName(dto.getDistrict(), areaMap, "-"));

                dto.setWorkTypeName(workDispatchServiceCorpService.getIntegerName(dto.getWorkType(), workTypeMap, ","));

            }
        }
        return ListWrapper.<WorkDispatchServiceBranchDto>builder()
                .list(workDispatchServiceBranchDtoList)
                .total(page.getTotal())
                .build();
    }


    @Override
    public WorkDispatchServiceBranchDto getById(Long id) {

        WorkDispatchServiceBranch workDispatchServiceBranch = workDispatchServiceBranchMapper.selectById(id);

        WorkCondition workCondition = workConditionService.getById(workDispatchServiceBranch.getConditionId());
        WorkDispatchServiceBranchDto workDispatchServiceBranchDto = new WorkDispatchServiceBranchDto();

        BeanUtils.copyProperties(workDispatchServiceBranch, workDispatchServiceBranchDto);
        BeanUtils.copyProperties(workCondition, workDispatchServiceBranchDto);
        return workDispatchServiceBranchDto;
    }

    @Override
    public void add(WorkDispatchServiceBranchDto workDispatchServiceBranch) {
        workDispatchServiceBranch.setId(KeyUtil.getId());
        workDispatchServiceBranch.setConditionId(KeyUtil.getId());
        workConditionService.addWorkCondition(workDispatchServiceBranch, workDispatchServiceBranch.getConditionId());
        workDispatchServiceBranchMapper.insert(workDispatchServiceBranch);
    }

    @Override
    public void mod(WorkDispatchServiceBranchDto workDispatchServiceBranch) {
        workConditionService.delById(workDispatchServiceBranch.getConditionId());
        workConditionService.addWorkCondition(workDispatchServiceBranch, workDispatchServiceBranch.getConditionId());
        workDispatchServiceBranchMapper.updateById(workDispatchServiceBranch);
    }

    @Override
    public void delById(Long id) {
        WorkDispatchServiceBranch workDispatchServiceBranch = workDispatchServiceBranchMapper.selectById(id);
        workConditionService.delById(workDispatchServiceBranch.getConditionId());
        workDispatchServiceBranchMapper.deleteById(id);
    }

    /**
     * 根据工单请求信息得到匹配的服务商网点
     *
     * @param workRequest
     * @param workDeal
     * @return
     * @author zgpi
     * @date 2019/11/14 15:43
     **/
    public WorkDispatchServiceBranchDto matchServiceBranch(WorkRequest workRequest, WorkDeal workDeal) {
        WorkDispatchServiceBranchDto workDispatchServiceBranchDto = new WorkDispatchServiceBranchDto();
        workDispatchServiceBranchDto.setDemanderCorp(workDeal.getDemanderCorp());
        workDispatchServiceBranchDto.setServiceCorp(workDeal.getServiceCorp());
        workDispatchServiceBranchDto.setCustomCorp(workRequest.getCustomCorp());
        workDispatchServiceBranchDto.setWorkType(String.valueOf(workRequest.getWorkType()));

        Long smallClassId = workRequest.getSmallClass();
        Result smallClassResult = deviceFeignService.findDeviceSmallClass(smallClassId);
        DeviceSmallClassDto deviceSmallClassDto = JsonUtil.parseObject(JsonUtil.toJson(smallClassResult.getData()), DeviceSmallClassDto.class);
        workDispatchServiceBranchDto.setLargeClassId(deviceSmallClassDto.getLargeClassId());
        workDispatchServiceBranchDto.setSmallClassId(String.valueOf(smallClassId));
        workDispatchServiceBranchDto.setBrandId(String.valueOf(workRequest.getBrand()));
        workDispatchServiceBranchDto.setModelId(String.valueOf(workRequest.getModel()));
        workDispatchServiceBranchDto.setDistrict(workRequest.getDistrict());
        workDispatchServiceBranchDto.setServiceBranch(workDeal.getServiceBranch());
        workDispatchServiceBranchDto.setDeviceBranch(String.valueOf(workRequest.getDeviceBranch()));

        List<WorkDispatchServiceBranchDto> list = workDispatchServiceBranchMapper
                .matchServiceBranch(workDispatchServiceBranchDto);

        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 自动分配服务商网点
     *
     * @param workRequest
     * @param workDeal
     * @return
     * @author zgpi
     * @date 2019/11/14 15:41
     **/
    @Override
    public Integer autoDispatchServiceBranch(WorkRequest workRequest, WorkDeal workDeal) {
        // 先获得设备档案中的服务网点
        Long serviceBranch = 0L;
        Integer handleType = null;
        if (LongUtil.isNotZero(workDeal.getDeviceId())) {
            Result deviceInfoResult = deviceFeignService.findDeviceInfo(workDeal.getDeviceId());
            DeviceInfoDto deviceInfoDto = JsonUtil.parseObject(JsonUtil.toJson(deviceInfoResult.getData()), DeviceInfoDto.class);
            if (deviceInfoDto != null && LongUtil.isNotZero(deviceInfoDto.getServiceBranch())) {
                serviceBranch = deviceInfoDto.getServiceBranch();
                handleType = WorkHandleTypeEnum.AUTO.getCode();
            }
        }
        Integer serviceMode = null;
        // 若设备档案中没有服务网点，则从规则中获取
        if (LongUtil.isZero(serviceBranch)) {
            WorkDispatchServiceBranchDto dto = this.matchServiceBranch(workRequest, workDeal);
            if (dto != null && LongUtil.isNotZero(dto.getId())) {
                serviceBranch = dto.getServiceBranch();
                handleType = dto.getHandleType();
                serviceMode = dto.getServiceMode();
            }
        }

        if (LongUtil.isNotZero(serviceBranch) && handleType != null) {
            workDeal.setServiceBranch(serviceBranch);
            // 若是自动受理，则需要修改工单状态
            if (WorkHandleTypeEnum.AUTO.getCode() == handleType) {
                workDeal.setWorkStatus(WorkStatusEnum.TO_ASSIGN.getCode());
                workDeal.setServiceMode(serviceMode);
                workDeal.setHandleTime(DateUtil.date().toTimestamp());
            }
            workDealService.updateById(workDeal);

            WorkOperate workOperate = new WorkOperate();
            workOperate.setWorkId(workDeal.getWorkId());
            workOperate.setWorkStatus(workDeal.getWorkStatus());
            workOperate.setOperateType(WorkOperateTypeEnum.AUTO_DISPATCH_BRANCH.getCode());

            ServiceBranch serviceBranchEntity = serviceBranchService.getById(serviceBranch);
            workOperate.setSummary("自动分配服务商网点");
            if (serviceBranchEntity != null) {
                workOperate.setSummary(workOperate.getSummary() + " " + serviceBranchEntity.getBranchName());
            }
            if (WorkHandleTypeEnum.AUTO.getCode() == handleType) {
                workOperate.setSummary(workOperate.getSummary() + " 并自动受理");
            }
            workOperateService.addAutoWorkOperate(workOperate);
            return handleType;
        }
        return null;
    }

}
