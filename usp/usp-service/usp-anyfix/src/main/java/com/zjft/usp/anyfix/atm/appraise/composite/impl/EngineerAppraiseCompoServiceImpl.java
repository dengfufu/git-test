package com.zjft.usp.anyfix.atm.appraise.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.zjft.usp.anyfix.atm.appraise.composite.EngineerAppraiseCompoService;
import com.zjft.usp.anyfix.atm.appraise.dto.EngineerAppraiseDto;
import com.zjft.usp.anyfix.atm.appraise.filter.EngineerAppraiseFilter;
import com.zjft.usp.anyfix.baseinfo.enums.WorkSysTypeEnum;
import com.zjft.usp.anyfix.baseinfo.service.WorkTypeService;
import com.zjft.usp.anyfix.corp.branch.service.DeviceBranchService;
import com.zjft.usp.anyfix.corp.branch.service.ServiceBranchService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderCustomService;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.anyfix.work.request.enums.ServiceModeEnum;
import com.zjft.usp.anyfix.work.request.enums.TrafficEnum;
import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import com.zjft.usp.anyfix.work.request.mapper.WorkRequestMapper;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.device.dto.DeviceClassCompoDto;
import com.zjft.usp.device.service.DeviceFeignService;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: JFZOU
 * @Date: 2020-03-09 9:03
 * @Version 1.0
 */
@Service
public class EngineerAppraiseCompoServiceImpl implements EngineerAppraiseCompoService {

    @Resource
    private UasFeignService uasFeignService;

    @Autowired
    private DeviceBranchService deviceBranchService;

    @Autowired
    private ServiceBranchService serviceBranchService;

    @Resource
    private DeviceFeignService deviceFeignService;

    @Resource
    private WorkRequestMapper workRequestMapper;

    @Autowired
    private WorkTypeService workTypeService;

    @Autowired
    private DemanderCustomService demanderCustomService;

    private Map<Long, String> demanderCustomCorpNameMap;
    private Map<Integer, String> workTypeMap;
    private Map<Long, String> corpMap;
    private Map<Long, DeviceClassCompoDto> smallClassAndClassCompoDtoMap;
    private Map<Long, String> userMobileMap;
    private Map<Long, String> deviceBrandMap;
    private Map<Long, String> deviceModelMap;
    private Map<Long, String> serviceBranchMap;
    private Map<Long, String> deviceBranchMap;
    private Map<Long, String> customDeviceBranchMap;

    @Override
    public List<EngineerAppraiseDto> queryByMonth(EngineerAppraiseFilter engineerAppraiseFilter) {
        checkDataNotNull(engineerAppraiseFilter);

        /**为了提升性能，每个月抽取31天，循环31次，按天抽取*/
        WorkFilter workFilter = new WorkFilter();
        workFilter.setServiceCorp(engineerAppraiseFilter.getCorpId());
        workFilter.setFinishDayString(engineerAppraiseFilter.getDate());

        workFilter.setServiceMode(ServiceModeEnum.LOCALE_SERVICE.getCode());

        List<Integer> workTypeNoInList = new ArrayList<>();
        workTypeNoInList.add(WorkSysTypeEnum.COMPLAINED.getCode());
        workFilter.setWorkTypeNoInList(workTypeNoInList);

        workFilter.setWorkStatusList(WorkStatusEnum.getAppraiseNormalCloseList());

        List<WorkDto> workDtoList = workRequestMapper.queryEngineerWorkAppraise(workFilter);
        if (CollectionUtil.isEmpty(workDtoList)) {
            return new ArrayList<>();
        }

        List<EngineerAppraiseDto> engineerAppraiseDtoList = new ArrayList<>();
        this.initData(workDtoList);

        for (WorkDto workDto : workDtoList) {
            EngineerAppraiseDto engineerAppraiseDto = new EngineerAppraiseDto();

            engineerAppraiseDto.setWorkId(StrUtil.toString(workDto.getWorkId()));
            engineerAppraiseDto.setWorkCode(workDto.getWorkCode());
            engineerAppraiseDto.setDemandedCorpId(workDto.getDemanderCorp());
            engineerAppraiseDto.setDemandedCorpName(StrUtil.trimToEmpty(corpMap.get(workDto.getDemanderCorp())));
            engineerAppraiseDto.setWorkTypeId(workDto.getWorkType());
            engineerAppraiseDto.setWorkTypeName(StrUtil.trimToEmpty(workTypeMap.get(workDto.getWorkType())));
            engineerAppraiseDto.setWorkStatusId(workDto.getWorkStatus());
            engineerAppraiseDto.setWorkStatusName(WorkStatusEnum.getNameByCode(workDto.getWorkStatus()));
            engineerAppraiseDto.setServiceModeId(workDto.getServiceMode());
            engineerAppraiseDto.setServiceModeName(ServiceModeEnum.getNameByCode(workDto.getServiceMode()));
            engineerAppraiseDto.setCustomName(StrUtil.trimToEmpty(demanderCustomCorpNameMap.get(workDto.getCustomId())));

            DeviceClassCompoDto deviceClassCompoDto = smallClassAndClassCompoDtoMap.get(workDto.getSmallClass());
            if(deviceClassCompoDto  != null){
                engineerAppraiseDto.setSmallClassId(deviceClassCompoDto.getSmallClassId());
                engineerAppraiseDto.setSmallClassName(deviceClassCompoDto.getSmallClassName());
                engineerAppraiseDto.setLargeClassId(deviceClassCompoDto.getLargeClassId());
                engineerAppraiseDto.setLargeClassName(deviceClassCompoDto.getLargeClassName());

            }

            if (LongUtil.isZero(workDto.getDeviceBranch())) {
                engineerAppraiseDto.setDeviceBranchName("");
            }

            if (LongUtil.isNotZero(workDto.getDeviceBranch())) {
                engineerAppraiseDto.setDeviceBranchName(StrUtil.trimToEmpty(deviceBranchMap.get(workDto.getDeviceBranch())));
            }

            if (StrUtil.isBlank(engineerAppraiseDto.getDeviceBranchName()) && LongUtil.isNotZero(workDto.getDeviceBranch())) {
                engineerAppraiseDto.setDeviceBranchName(StrUtil.trimToEmpty(customDeviceBranchMap.get(workDto.getDeviceBranch())));
            }

            engineerAppraiseDto.setDeviceBranchAddress(StrUtil.trimToEmpty(workDto.getAddress()));
            engineerAppraiseDto.setServiceBranchId(workDto.getServiceBranch());
            engineerAppraiseDto.setServiceBranchName(StrUtil.trimToEmpty(serviceBranchMap.get(workDto.getServiceBranch())));
            engineerAppraiseDto.setTrafficName(StrUtil.trimToEmpty(TrafficEnum.getNameByCode(workDto.getTraffic())));
            String signTime = "";
            if (workDto.getSignTime() != null) {
                signTime = DateUtil.format(workDto.getSignTime(), "yyyy-MM-dd HH:mm");
            }
            engineerAppraiseDto.setSignTime(signTime);

            String finishTime = "";
            if (workDto.getFinishTime() != null) {
                finishTime = DateUtil.format(workDto.getFinishTime(), "yyyy-MM-dd HH:mm");
            }
            engineerAppraiseDto.setFinishTime(finishTime);

            /**服务工程师与协同工程师*/
            engineerAppraiseDto.setEngineerId(workDto.getEngineer());
            engineerAppraiseDto.setEngineerMobile(StrUtil.trimToEmpty(this.userMobileMap.get(workDto.getEngineer())));
            engineerAppraiseDto.setTogetherEngineers(workDto.getTogetherEngineers());
            if (StrUtil.isNotEmpty(workDto.getTogetherEngineers())) {

                String togetherEngineers = StrUtil.trimToEmpty(workDto.getTogetherEngineers());
                List<String> togetherEngineersList = Arrays.stream(togetherEngineers.split(","))
                        .filter(StrUtil::isNotBlank).collect(Collectors.toList());

                StringBuffer sbMobile = new StringBuffer(128);
                for (String userId : togetherEngineersList) {
                    Long tmpUserId = Long.parseLong(userId, 10);
                    if (sbMobile.toString().length() > 0) {
                        sbMobile.append(",");
                    }
                    sbMobile.append(StrUtil.trimToEmpty(this.userMobileMap.get(tmpUserId)));
                }
                engineerAppraiseDto.setTogetherEngineerMobiles(sbMobile.toString());

            } else {
                engineerAppraiseDto.setTogetherEngineerMobiles("");
            }

            engineerAppraiseDtoList.add(engineerAppraiseDto);

        }


        return engineerAppraiseDtoList;
    }

    /**
     * 检查请求数据是否为空
     *
     * @param engineerAppraiseFilter
     */
    private void checkDataNotNull(EngineerAppraiseFilter engineerAppraiseFilter) {
        if (engineerAppraiseFilter == null) {
            throw new AppException("请求参数传输错误，请重试");
        }

        if (StrUtil.isEmpty(engineerAppraiseFilter.getDate())) {
            throw new AppException("绩效考核所属日期不能为空，请重试");
        }

        if (LongUtil.isZero(engineerAppraiseFilter.getCorpId())) {
            throw new AppException("绩效考核所属企业不能为空，请重试");
        }
    }

    /**
     * 初始化数据
     *
     * @param workDtoList
     */
    private void initData(List<WorkDto> workDtoList) {
        if (CollectionUtil.isNotEmpty(workDtoList)) {

            /**企业列表*/
            Set<Long>  corpIdSet = new TreeSet<>();
            List<Long> corpIdList = new ArrayList<>();

            Set<Long>  customIdSet = new TreeSet<>();
            List<Long> customIdList = new ArrayList<>();

            Set<Long> userIdSet = new TreeSet<>();
            List<Long> userIdList = new ArrayList<>();

            for (WorkDto workDto : workDtoList) {
                if(LongUtil.isNotZero(workDto.getDemanderCorp())){
                    corpIdSet.add(workDto.getDemanderCorp());
                }

                if(LongUtil.isNotZero(workDto.getServiceCorp())){
                    corpIdSet.add(workDto.getServiceCorp());
                }

                if(LongUtil.isNotZero(workDto.getCustomCorp())){
                    corpIdSet.add(workDto.getCustomCorp());
                }

                if(LongUtil.isNotZero(workDto.getCustomId())) {
                    customIdSet.add(workDto.getCustomId());
                }

                if (LongUtil.isNotZero(workDto.getEngineer())) {
                    userIdSet.add(workDto.getEngineer());
                }

                String togetherEngineers = StrUtil.trimToEmpty(workDto.getTogetherEngineers());
                List<String> togetherEngineersList = Arrays.stream(togetherEngineers.split(","))
                        .filter(StrUtil::isNotBlank).collect(Collectors.toList());
                for(String together:togetherEngineersList){
                    userIdSet.add(Long.parseLong(together,10));
                }
            }

            for(Long corpId:corpIdSet){
                corpIdList.add(corpId);
            }

            for(Long customId:customIdSet){
                customIdList.add(customId);
            }

            for(Long userId:userIdSet){
                userIdList.add(userId);
            }

            /**工单类型映射，可以获得全部映射，此数据不会影响性能*/
            workTypeMap = workTypeService.mapWorkType();

            /**企业ID与名称映射*/
            Result<Map<Long, String>> corpResult = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList));
            if (corpResult.getCode() == Result.SUCCESS) {
                corpMap = corpResult.getData();
                corpMap = corpMap == null ? new HashMap<>(0) : corpMap;
            }

            /**委托商客户企业与名称映射*/
            demanderCustomCorpNameMap = demanderCustomService.mapIdAndCustomNameByIdList(customIdList);
            demanderCustomCorpNameMap = demanderCustomCorpNameMap == null ? new HashMap<>(0) : demanderCustomCorpNameMap;

            /**客户设备品牌与名称映射*/
            Result<Map<Long, String>> deviceBrandResult = deviceFeignService.mapDeviceBrandByJsonCorpIds(JsonUtil.toJson(corpIdList));
            if (deviceBrandResult.getCode() == Result.SUCCESS) {
                deviceBrandMap = deviceBrandResult.getData();
                deviceBrandMap = deviceBrandMap == null ? new HashMap<>(0) : deviceBrandMap;
            }

            /**客户设备型号与名称映射*/
            Result<Map<Long, String>> deviceModelResult = deviceFeignService.mapDeviceModelByJsonCorpIds(JsonUtil.toJson(corpIdList));
            if (deviceModelResult.getCode() == Result.SUCCESS) {
                deviceModelMap = deviceModelResult.getData();
                deviceModelMap = deviceModelMap == null ? new HashMap<>(0) : deviceModelMap;
            }

            /**设备小类ID与设备分类组合对象映射*/
            Result<Map<Long, DeviceClassCompoDto>> deviceClassResult = deviceFeignService.mapDeviceClassCompoByCorpIds(JsonUtil.toJson(corpIdList));
            if (deviceClassResult.getCode() == Result.SUCCESS) {
                smallClassAndClassCompoDtoMap = deviceClassResult.getData();
                smallClassAndClassCompoDtoMap = smallClassAndClassCompoDtoMap == null ?  new HashMap<>(0) : smallClassAndClassCompoDtoMap;
            }

            /**服务网点与名称映射*/
            serviceBranchMap = serviceBranchService.mapServiceBranchByCorpIdList(corpIdList);
            /**设备网点与名称映射*/
            deviceBranchMap = deviceBranchService.mapDeviceBranchByCorpIdList(corpIdList);
            customDeviceBranchMap = deviceBranchService.mapCustomDeviceBranchByCustomIdList(customIdList);

            Result<Map<Long, String>> userMobileResult = uasFeignService.mapUserIdAndMobileByUserIdList(JsonUtil.toJson(userIdList));
            if (userMobileResult.getCode() == Result.SUCCESS) {
                userMobileMap = userMobileResult.getData();
                userMobileMap = userMobileMap == null ? new HashMap<>(0) : userMobileMap;
            }
        }
    }
}
