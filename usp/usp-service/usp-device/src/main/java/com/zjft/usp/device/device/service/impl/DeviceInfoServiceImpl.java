package com.zjft.usp.device.device.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.dto.CustomFieldDataDto;
import com.zjft.usp.anyfix.service.AnyfixFeignService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.device.baseinfo.dto.DeviceBrandDto;
import com.zjft.usp.device.baseinfo.dto.DeviceLargeClassDto;
import com.zjft.usp.device.baseinfo.dto.DeviceModelDto;
import com.zjft.usp.device.baseinfo.dto.DeviceSmallClassDto;
import com.zjft.usp.device.baseinfo.enums.EnabledEnum;
import com.zjft.usp.device.baseinfo.model.*;
import com.zjft.usp.device.baseinfo.service.*;
import com.zjft.usp.device.device.dto.DeviceInfoDto;
import com.zjft.usp.device.device.enums.DeviceStatusEnum;
import com.zjft.usp.device.device.enums.WarrantyEnum;
import com.zjft.usp.device.device.enums.WarrantyModeEnum;
import com.zjft.usp.device.device.enums.ZoneEnum;
import com.zjft.usp.device.device.filter.CustomFieldDataFilter;
import com.zjft.usp.device.device.filter.DeviceInfoFilter;
import com.zjft.usp.device.device.mapper.DeviceInfoMapper;
import com.zjft.usp.device.device.model.CustomDeviceRelate;
import com.zjft.usp.device.device.model.DeviceInfo;
import com.zjft.usp.device.device.model.DeviceLocate;
import com.zjft.usp.device.device.model.DeviceService;
import com.zjft.usp.device.device.service.DeviceInfoService;
import com.zjft.usp.device.device.service.DeviceLocateService;
import com.zjft.usp.device.device.service.DeviceServiceService;
import com.zjft.usp.feign.dto.AreaDto;
import com.zjft.usp.feign.dto.DemanderCustomDto;
import com.zjft.usp.feign.dto.DeviceBranchDto;
import com.zjft.usp.feign.dto.ServiceBranchDto;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 设备档案基本表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceInfoServiceImpl extends ServiceImpl<DeviceInfoMapper, DeviceInfo> implements DeviceInfoService {

    @Resource
    private DeviceInfoMapper deviceInfoMapper;

    @Resource
    private UasFeignService uasFeignService;
    @Resource
    private AnyfixFeignService anyfixFeignService;

    @Autowired
    private DeviceSmallClassService deviceSmallClassService;
    @Autowired
    private DeviceSpecificationService deviceSpecificationService;
    @Autowired
    private DeviceLargeClassService deviceLargeClassService;
    @Autowired
    private DeviceBrandService deviceBrandService;
    @Autowired
    private DeviceModelService deviceModelService;
    @Autowired
    private DeviceLocateService deviceLocateService;
    @Autowired
    private DeviceServiceService deviceServiceService;

    /**
     * 查询设备档案
     *
     * @param deviceInfoFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/10/16 6:52 下午
     **/
    @Override
    public ListWrapper<DeviceInfoDto> queryDeviceInfo(DeviceInfoFilter deviceInfoFilter,
                                                      UserInfo userInfo,
                                                      ReqParam reqParam) {
        deviceInfoFilter.setCurCorpId(reqParam.getCorpId());
        List<Long> demanderCustomIdList = this.anyfixFeignService.listCustomIdsByCustomCorp(reqParam.getCorpId()).getData();
        deviceInfoFilter.setCustomIdList(demanderCustomIdList);
        Page<DeviceInfoDto> page = new Page(deviceInfoFilter.getPageNum(), deviceInfoFilter.getPageSize());

        List<DeviceInfoDto> deviceInfoDtoList = deviceInfoMapper.queryDeviceInfo(page, deviceInfoFilter);
        if (deviceInfoDtoList != null) {
            Result corpIdResult = anyfixFeignService.listAllCorpIdByCorpId(reqParam.getCorpId());
            List<Long> corpIdList = JsonUtil.parseArray(JsonUtil.toJson(corpIdResult.getData()), Long.class);
            Result corpResult = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList));
            Map<String, String> corpMap = JsonUtil.parseObject(JsonUtil.toJson(corpResult.getData()), Map.class);
            Result corpUserResult = uasFeignService.mapUserIdAndNameByCorpIdList(corpIdList);
            Map<String, String> corpUserMap = JsonUtil.parseObject(JsonUtil.toJson(corpUserResult.getData()), Map.class);
            Result areaResult = uasFeignService.mapAreaCodeAndName();
            Map<String, String> areaMap = JsonUtil.parseObject(JsonUtil.toJson(areaResult.getData()), Map.class);

            List<Long> customIdList = new ArrayList<>();
            List<Long> specificationIdList = new ArrayList<>();
            List<Long> demanderAndServiceCorpList = new ArrayList<>();
            List<Long> userIdList = new ArrayList<>();
            List<Long> branchIdList = new ArrayList<>();
            List<Long> serviceBranchIdList = new ArrayList<>();
            for (DeviceInfoDto deviceInfoDto : deviceInfoDtoList) {
                customIdList.add(deviceInfoDto.getCustomId());
                // 设备规格
                if (LongUtil.isNotZero(deviceInfoDto.getSpecificationId())) {
                    specificationIdList.add(deviceInfoDto.getSpecificationId());
                }
                //服务委托方和服务商
                if (LongUtil.isNotZero(deviceInfoDto.getDemanderCorp())) {
                    demanderAndServiceCorpList.add(deviceInfoDto.getDemanderCorp());
                }
                if (LongUtil.isNotZero(deviceInfoDto.getServiceCorp())) {
                    demanderAndServiceCorpList.add(deviceInfoDto.getServiceCorp());
                }
                //  //派单主管和工程师姓名
                if (LongUtil.isNotZero(deviceInfoDto.getWorkManager())) {
                    userIdList.add(deviceInfoDto.getWorkManager());
                }
                if (LongUtil.isNotZero(deviceInfoDto.getEngineer())) {
                    userIdList.add(deviceInfoDto.getEngineer());
                }
                if(LongUtil.isNotZero(deviceInfoDto.getBranchId())) {
                    branchIdList.add(deviceInfoDto.getBranchId());
                }
                if(LongUtil.isNotZero(deviceInfoDto.getServiceBranch())) {
                    serviceBranchIdList.add(deviceInfoDto.getServiceBranch());
                }
            }
            branchIdList = branchIdList.stream().distinct().collect(Collectors.toList());
            serviceBranchIdList = serviceBranchIdList.stream().distinct().collect(Collectors.toList());
            Map<Long, String> customMap = this.anyfixFeignService.mapIdAndCustomNameByIdList(JsonUtil.toJson(customIdList)).getData();
            Map<Long, String> specificationMap = this.deviceSpecificationService.mapIdAndName(specificationIdList);
            Map<Long, String> demanderAndServiceCorpMap = this.uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList)).getData();
            Map<Long, String> userIdAndNameMap = this.uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList)).getData();
            Map<Long, String> deviceBranchMap = this.anyfixFeignService.mapIdAndBranchName(branchIdList).getData();
            String serviceBranchIdListJson = JsonUtil.toJson(serviceBranchIdList);
            Map<Long, String> serviceBranchMap = this.anyfixFeignService.mapServiceBranchIdAndName(serviceBranchIdListJson).getData();

            for (DeviceInfoDto deviceInfoDto : deviceInfoDtoList) {
                if (LongUtil.isNotZero(deviceInfoDto.getDemanderCorp())) {
                    deviceInfoDto.setDemanderCorpName(StrUtil.trimToEmpty(corpMap.get(deviceInfoDto.getDemanderCorp().toString())));
                }
                if (LongUtil.isNotZero(deviceInfoDto.getServiceCorp())) {
                    deviceInfoDto.setServiceCorpName(StrUtil.trimToEmpty(corpMap.get(deviceInfoDto.getServiceCorp().toString())));
                }
                if (deviceInfoDto.getWarrantyStatus() != null) {
                    deviceInfoDto.setWarrantyStatusName(WarrantyEnum.getNameByCode(deviceInfoDto.getWarrantyStatus()));
                }
                deviceInfoDto.setDistrictName(StrUtil.trimToEmpty(areaMap.get(deviceInfoDto.getDistrict())));
                if (deviceInfoDto.getStatus() != null) {
                    deviceInfoDto.setStatusName(DeviceStatusEnum.getNameByCode(deviceInfoDto.getStatus()));
                }
                if(LongUtil.isNotZero(deviceInfoDto.getServiceBranch())) {
                    deviceInfoDto.setServiceBranchName(serviceBranchMap.get(deviceInfoDto.getServiceBranch()));
                }
                if (LongUtil.isNotZero(deviceInfoDto.getWorkManager())) {
                    deviceInfoDto.setWorkManagerName(StrUtil.trimToEmpty(corpUserMap.get(deviceInfoDto.getWorkManager().toString())));
                }
                if (LongUtil.isNotZero(deviceInfoDto.getEngineer())) {
                    deviceInfoDto.setEngineerName(StrUtil.trimToEmpty(corpUserMap.get(deviceInfoDto.getEngineer().toString())));
                }
                if (LongUtil.isNotZero(deviceInfoDto.getCustomId())) {
                    deviceInfoDto.setCustomCorpName(customMap.get(deviceInfoDto.getCustomId()));
                }
                // 设备规格
                if (LongUtil.isNotZero(deviceInfoDto.getSpecificationId())) {
                    deviceInfoDto.setSpecificationName(specificationMap.get(deviceInfoDto.getSpecificationId()));
                }
                //服务委托方和服务商
                if (demanderAndServiceCorpMap != null) {
                    deviceInfoDto.setDemanderCorpName(demanderAndServiceCorpMap.get(deviceInfoDto.getDemanderCorp()));
                    deviceInfoDto.setServiceCorpName(demanderAndServiceCorpMap.get(deviceInfoDto.getServiceCorp()));
                }
                //设备网点
                if (LongUtil.isNotZero(deviceInfoDto.getBranchId())) {
                    deviceInfoDto.setBranchName(deviceBranchMap.get(deviceInfoDto.getBranchId()));
                }
                //设备状态
                if (deviceInfoDto.getStatus() != null&&deviceInfoDto.getStatus()>0) {
                    deviceInfoDto.setStatusName(DeviceStatusEnum.getNameByCode(deviceInfoDto.getStatus()));
                }
                //派单主管和工程师姓名
                if (userIdAndNameMap != null) {
                    deviceInfoDto.setWorkManagerName(userIdAndNameMap.get(deviceInfoDto.getWorkManager()));
                    deviceInfoDto.setEngineerName(userIdAndNameMap.get(deviceInfoDto.getEngineer()));
                }
                // 省市
                if (StrUtil.isNotBlank(deviceInfoDto.getDistrict())) {
                    String province = "";
                    String city = "";
                    String district = "";
                    if (deviceInfoDto.getDistrict().length() >= 2) {
                        province = StrUtil.trimToEmpty(areaMap.get(deviceInfoDto.getDistrict().substring(0, 2)));
                    }
                    if (deviceInfoDto.getDistrict().length() >= 4) {
                        // 如果是省直辖市
                        if (deviceInfoDto.getDistrict().matches("\\d{2}9\\d{3}")) {
                            city = StrUtil.trimToEmpty(areaMap.get(deviceInfoDto.getDistrict()));
                        } else if (deviceInfoDto.getDistrict().matches("5002\\d{2}")) {
                            city = StrUtil.trimToEmpty(areaMap.get("5001"));
                        } else {
                            city = StrUtil.trimToEmpty(areaMap.get(deviceInfoDto.getDistrict().substring(0, 4)));
                        }
                    }
                    // 非省直辖市
                    if (deviceInfoDto.getDistrict().length() >= 6 && !deviceInfoDto.getDistrict().matches("\\d{2}9\\d{3}")) {
                        district = StrUtil.trimToEmpty(areaMap.get(deviceInfoDto.getDistrict().substring(0, 6)));
                    }
                    deviceInfoDto.setCityName(city);
                    deviceInfoDto.setProvinceName(province.replace("省", "").replace("自治区", "").replace("特别行政区", "")
                            .replace("回族", "").replace("壮族", "").replace("维吾尔族", ""));
                    deviceInfoDto.setDistrictName(district);
                    deviceInfoDto.setWarrantyModeName(WarrantyModeEnum.getNameByCode(deviceInfoDto.getWarrantyMode()));
                    deviceInfoDto.setZoneName(ZoneEnum.getNameByCode(deviceInfoDto.getZone()));
                }
            }
        }
        return ListWrapper.<DeviceInfoDto>builder()
                .list(deviceInfoDtoList)
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加设备档案
     *
     * @param deviceInfoDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/10/17 1:39 下午
     **/
    @Override
    public Long addDeviceInfo(DeviceInfoDto deviceInfoDto, UserInfo userInfo, ReqParam reqParam) {
        if (LongUtil.isZero(deviceInfoDto.getDemanderCorp())) {
            deviceInfoDto.setDemanderCorp(reqParam.getCorpId());
        }
        if (StrUtil.isNotBlank(deviceInfoDto.getSerial())) {
            QueryWrapper<DeviceInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("serial", deviceInfoDto.getSerial());
            queryWrapper.eq("demander_corp", deviceInfoDto.getDemanderCorp());
            queryWrapper.eq("model_id", deviceInfoDto.getModelId());
            List<DeviceInfo> deviceInfoList = this.list(queryWrapper);
            if (CollectionUtil.isNotEmpty(deviceInfoList)) {
                throw new AppException("该设备已经存在！");
            }
        }
        Date curDateTime = DateUtil.date();
        Long deviceId = KeyUtil.getId();
        DeviceInfo deviceInfo = new DeviceInfo();
        BeanUtils.copyProperties(deviceInfoDto, deviceInfo);
        deviceInfo.setDeviceId(deviceId);
        deviceInfo.setOperator(userInfo.getUserId());
        deviceInfo.setOperateTime(curDateTime);
        this.save(deviceInfo);
        // 添加了用户信息则需保存到设备位置表
        if (LongUtil.isNotZero(deviceInfoDto.getCustomId())) {
            DeviceLocate deviceLocate = new DeviceLocate();
            BeanUtils.copyProperties(deviceInfoDto, deviceLocate);
            deviceLocate.setDeviceId(deviceId);
            deviceLocate.setStatus(0);
            deviceLocate.setOperator(userInfo.getUserId());
            deviceLocate.setOperateTime(DateUtil.date());
            this.deviceLocateService.save(deviceLocate);
        }

        // 添加自定义字段 String jsonFilter
        String jsonObject;
        List<CustomFieldDataDto> customFieldDataList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(deviceInfoDto.getCustomFieldDataList())) {
            for (CustomFieldDataDto customFieldDataDto : deviceInfoDto.getCustomFieldDataList()) {
                customFieldDataDto.setFormId(deviceInfo.getDeviceId());
                customFieldDataList.add(customFieldDataDto);
            }
            jsonObject = JsonUtil.toJson(customFieldDataList);
            this.anyfixFeignService.addCustomFieldDataList(jsonObject);
        }
        return deviceId;
    }

    /**
     * 远程调用：添加/修改设备档案
     *
     * @param deviceInfoDto
     * @return
     * @author zgpi
     * @date 2020/2/6 11:18
     **/
    @Override
    public DeviceInfoDto editDeviceInfo(DeviceInfoDto deviceInfoDto) {
        Long deviceId = deviceInfoDto.getDeviceId();
        // 处理设备型号、设备规格
        deviceInfoDto = this.dealDeviceModel(deviceInfoDto);
        deviceInfoDto = this.dealDeviceSpecification(deviceInfoDto);
        if (StrUtil.isNotBlank(deviceInfoDto.getSerial()) && LongUtil.isZero(deviceId)) {
            QueryWrapper<DeviceInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("serial", StrUtil.trimToEmpty(deviceInfoDto.getSerial()));
            queryWrapper.eq("demander_corp", deviceInfoDto.getDemanderCorp());
            queryWrapper.eq("model_id", deviceInfoDto.getModelId());
            List<DeviceInfo> deviceInfoList = this.list(queryWrapper);
            if (CollectionUtil.isNotEmpty(deviceInfoList)) {
                throw new AppException("该设备已存在！");
            }
        }
//        Long serviceCorp = deviceInfoDto.getServiceCorp();
//        if (StrUtil.isNotBlank(deviceInfoDto.getDeviceCode()) && LongUtil.isZero(deviceId)) {
//            QueryWrapper<DeviceInfo> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("service_corp", serviceCorp);
//            List<DeviceInfo> deviceInfoList = this.list(queryWrapper);
//            if (CollectionUtil.isNotEmpty(deviceInfoList)) {
//                List<Long> deviceIdList = deviceInfoList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList());
//                QueryWrapper<DeviceLocate> wrapper = new QueryWrapper<>();
//                wrapper.eq("device_code", StrUtil.trimToEmpty(deviceInfoDto.getDeviceCode()));
//                wrapper.in("device_id", deviceIdList);
//                List<DeviceLocate> deviceLocateList = deviceLocateService.list(wrapper);
//                if (CollectionUtil.isNotEmpty(deviceLocateList)) {
//                    throw new AppException("该设备编号已存在！");
//                }
//            }
//        }
        Date curDateTime = DateUtil.date();
        // 没有则新增
        if (LongUtil.isZero(deviceId)) {
            deviceId = KeyUtil.getId();
            deviceInfoDto.setDeviceId(deviceId);
            DeviceInfo deviceInfo = new DeviceInfo();
            BeanUtils.copyProperties(deviceInfoDto, deviceInfo);
            deviceInfo.setDeviceId(deviceId);
            deviceInfo.setOperator(deviceInfoDto.getOperator());
            deviceInfo.setOperateTime(curDateTime);
            this.save(deviceInfo);
            // 添加了用户信息则需保存到设备位置表
            if (LongUtil.isNotZero(deviceInfoDto.getCustomId())) {
                DeviceLocate deviceLocate = new DeviceLocate();
                BeanUtils.copyProperties(deviceInfoDto, deviceLocate);
                deviceLocate.setDeviceId(deviceId);
                deviceLocate.setStatus(0);
                deviceLocate.setOperator(deviceInfoDto.getOperator());
                deviceLocate.setDescription(deviceInfoDto.getDeviceDescription());
                deviceLocate.setOperateTime(curDateTime);
                deviceLocateService.save(deviceLocate);
            }
            // 添加了服务网点则需保存在设备服务表
            if (LongUtil.isNotZero(deviceInfoDto.getServiceBranch())) {
                DeviceService deviceService = new DeviceService();
                deviceService.setDeviceId(deviceId);
                deviceService.setServiceBranch(deviceInfoDto.getServiceBranch());
                deviceService.setOperator(deviceInfoDto.getOperator());
                deviceService.setOperateTime(curDateTime);
                deviceService.setWorkId(deviceInfoDto.getWorkId());
                deviceServiceService.save(deviceService);
            }
        } else {
            // 修改设备档案
            DeviceInfo deviceInfo = this.getById(deviceId);
            if (deviceInfo != null) {
                deviceInfo.setDeviceId(deviceId);
                deviceInfo.setSerial(deviceInfoDto.getSerial());
                deviceInfo.setSmallClassId(deviceInfoDto.getSmallClassId());
                deviceInfo.setSpecificationId(deviceInfoDto.getSpecificationId());
                deviceInfo.setBrandId(deviceInfoDto.getBrandId());
                deviceInfo.setModelId(deviceInfoDto.getModelId());
                deviceInfo.setContactName(deviceInfoDto.getContactName());
                deviceInfo.setContactPhone(deviceInfoDto.getContactPhone());
                Long serviceBranch = 0L;
                if (LongUtil.isZero(deviceInfo.getServiceCorp())) {
                    deviceInfo.setServiceCorp(deviceInfoDto.getServiceCorp());
                    serviceBranch = deviceInfoDto.getServiceBranch();
                }
                deviceInfo.setOperator(deviceInfoDto.getOperator());
                deviceInfo.setOperateTime(curDateTime);
                this.updateById(deviceInfo);
                // 修改设备位置信息
                DeviceLocate deviceLocate = new DeviceLocate();
                deviceLocate.setDeviceId(deviceId);
                deviceLocate.setDeviceCode(deviceInfoDto.getDeviceCode());
                deviceLocate.setCustomId(deviceInfoDto.getCustomId());
                deviceLocate.setDistrict(deviceInfoDto.getDistrict());
                deviceLocate.setZone(deviceInfoDto.getZone());
                deviceLocate.setAddress(deviceInfoDto.getAddress());
                deviceLocate.setBranchId(deviceInfoDto.getBranchId());
                deviceLocate.setDeviceCode(deviceInfoDto.getDeviceCode());
                deviceLocate.setOperator(deviceInfoDto.getOperator());
                deviceLocate.setOperateTime(curDateTime);
                deviceLocate.setDescription(deviceInfoDto.getDeviceDescription());
                deviceLocateService.saveOrUpdate(deviceLocate);
                // 修改设备服务信息
                DeviceService deviceService = deviceServiceService.getById(deviceId);
                deviceService = deviceService == null ? new DeviceService() : deviceService;
                deviceService.setDeviceId(deviceId);
                if (LongUtil.isZero(deviceService.getServiceBranch())) {
                    deviceService.setServiceBranch(serviceBranch);
                }
                deviceService.setOperator(deviceInfoDto.getOperator());
                deviceService.setOperateTime(curDateTime);
                deviceServiceService.saveOrUpdate(deviceService);
            }
        }
        return deviceInfoDto;
    }

    /**
     * 修改设备档案
     *
     * @param deviceInfoDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/10/17 1:39 下午
     **/
    @Override
    public void updateDeviceInfo(DeviceInfoDto deviceInfoDto, UserInfo userInfo, ReqParam reqParam) {
        Assert.notNull(deviceInfoDto, "设备档案参数不能为空！");
        Date curDateTime = DateUtil.date().toTimestamp();
        if (LongUtil.isZero(deviceInfoDto.getServiceCorp())) {
            deviceInfoDto.setServiceCorp(0L);
        }
        if (LongUtil.isZero(deviceInfoDto.getCustomId())) {
            deviceInfoDto.setCustomId(0L);
            deviceInfoDto.setCustomCorp(0L);
        }
        DeviceInfo deviceInfo = this.getById(deviceInfoDto.getDeviceId());
        List<DeviceInfo> listBySerial = this.list(new QueryWrapper<DeviceInfo>().eq("serial", deviceInfoDto.getSerial())
                .eq("demander_corp", reqParam.getCorpId())
                .eq("brand_id", deviceInfoDto.getBranchId())
                .eq("model_id", deviceInfoDto.getModelId()));
        if (!deviceInfo.getSerial().equals(deviceInfoDto.getSerial()) && listBySerial != null && listBySerial.size() > 0) {
            throw new AppException("该设备已存在！");
        }
        DeviceService deviceService = this.deviceServiceService.getById(deviceInfoDto.getDeviceId());
        //更改了服务商，则删除服务信息
        if (deviceService != null) {
            if (!deviceInfoDto.getServiceCorp().equals(deviceInfo.getServiceCorp())) {
                this.deviceServiceService.removeById(deviceService);
            }
        }
        //更改了客户，则清除位置信息的设备网点
        DeviceLocate deviceLocate = this.deviceLocateService.getById(deviceInfoDto.getDeviceId());
        if (deviceLocate != null) {
            if (!deviceInfoDto.getCustomId().equals(deviceLocate.getCustomId())) {
                deviceLocate.setCustomId(deviceInfoDto.getCustomId());
                deviceLocate.setBranchId(0L);
                deviceLocate.setOperator(userInfo.getUserId());
                deviceLocate.setOperateTime(DateUtil.date());
                this.deviceLocateService.updateById(deviceLocate);
            }
            //增加了服务商，则增加设备位置信息
        } else {
            if (LongUtil.isNotZero(deviceInfoDto.getCustomId())) {
                DeviceLocate deviceLocateNew = new DeviceLocate();
                deviceLocateNew.setDeviceId(deviceInfoDto.getDeviceId());
                deviceLocateNew.setCustomId(deviceInfoDto.getCustomId());
                deviceLocateNew.setOperator(userInfo.getUserId());
                deviceLocateNew.setOperateTime(DateUtil.date());
                this.deviceLocateService.save(deviceLocateNew);
            }
        }
        BeanUtils.copyProperties(deviceInfoDto, deviceInfo);
        deviceInfo.setOperator(userInfo.getUserId());
        deviceInfo.setOperateTime(curDateTime);
        this.updateById(deviceInfo);
    }

    /**
     * 删除设备档案
     *
     * @param deviceId
     * @return
     * @author zgpi
     * @date 2019/10/17 1:40 下午
     **/
    @Override
    public void deleteDeviceInfo(Long deviceId) {
        this.removeById(deviceId);
        deviceLocateService.removeById(deviceId);
        deviceServiceService.removeById(deviceId);
        // 删除自定义数据
        this.anyfixFeignService.deleteByDeviceId(deviceId);
        ;
    }

    /**
     * 查找设备档案
     *
     * @param deviceInfoFilter
     * @return
     * @author zgpi
     * @date 2019/10/22 3:21 下午
     **/
    @Override
    public DeviceInfoDto findDeviceInfoDto(DeviceInfoFilter deviceInfoFilter) {
        List<DeviceInfoDto> list = this.baseMapper.listDeviceInfo(deviceInfoFilter);
        if (CollectionUtil.isEmpty(list) || CollectionUtil.isNotEmpty(list) && list.size() > 1) {
            // 没有查出设备档案或查出多个设备档案，不返回结果
            return null;
        }
        DeviceInfoDto deviceInfoDto = list.get(0);
        if (deviceInfoDto != null) {
            DeviceLocate deviceLocate = deviceLocateService.getById(deviceInfoDto.getDeviceId());
            Long branchId = 0L;
            if (deviceLocate != null) {
                deviceInfoDto.setDistrict(deviceLocate.getDistrict());
                Result areResult = uasFeignService.findAreaByCode(deviceLocate.getDistrict());
                AreaDto areaDto = JsonUtil.parseObject(JsonUtil.toJson(areResult.getData()), AreaDto.class);
                if (areaDto != null) {
                    deviceInfoDto.setDistrictName(StrUtil.isNotBlank(areaDto.getProvinceName()) ? areaDto.getProvinceName()
                            : "");
                    deviceInfoDto.setDistrictName(StrUtil.isNotBlank(areaDto.getCityName()) ?
                            areaDto.getProvinceName() + areaDto.getCityName() : areaDto.getDistrictName());
                    deviceInfoDto.setDistrictName(StrUtil.isNotBlank(areaDto.getDistrictName()) ?
                            areaDto.getProvinceName() + areaDto.getCityName() + areaDto.getDistrictName()
                            : areaDto.getDistrictName());
                }
                deviceInfoDto.setDeviceCode(deviceLocate.getDeviceCode());
                Result demanderCustomResult = anyfixFeignService.findDemanderCustomDtoById(deviceLocate.getCustomId());
                DemanderCustomDto demanderCustomDto = JsonUtil.parseObject(
                        JsonUtil.toJson(demanderCustomResult.getData()), DemanderCustomDto.class);
                if (demanderCustomDto != null) {
                    deviceInfoDto.setCustomCorp(demanderCustomDto.getCustomCorp());
                    deviceInfoDto.setCustomCorpName(demanderCustomDto.getCustomCorpName());
                }
                deviceInfoDto.setBranchId(deviceLocate.getBranchId());
                branchId = deviceLocate.getBranchId();
            }
            Result deviceBranchResult = anyfixFeignService.findDeviceBranch(branchId);
            DeviceBranchDto deviceBranchDto = JsonUtil.parseObject(JsonUtil.toJson(deviceBranchResult.getData()), DeviceBranchDto.class);
            if (deviceBranchDto != null) {
                deviceInfoDto.setBranchName(deviceBranchDto.getBranchName());
                deviceInfoDto.setContactName(deviceBranchDto.getContactName());
                deviceInfoDto.setContactPhone(deviceBranchDto.getContactPhone());
                deviceInfoDto.setAddress(deviceBranchDto.getAddress());
            }
        }
        return deviceInfoDto;
    }

    /**
     * 根据出厂序列号、设备编号、设备型号查找设备档案
     *
     * @param deviceInfoFilter
     * @return
     * @author zgpi
     * @date 2020/1/17 18:18
     **/
    @Override
    public DeviceInfoDto findDeviceInfoBy(DeviceInfoFilter deviceInfoFilter) {
        List<DeviceInfoDto> deviceInfoList =  this.baseMapper.findDeviceInfo(deviceInfoFilter);
        if(CollectionUtil.isNotEmpty(deviceInfoList)) {
            return deviceInfoList.get(0);
        }
        return null;
    }

    @Override
    public List<Long> findDeviceEngineers(String deviceCode, Long smallClassId, Long brandId, Long modelId, String serial, Long demanderCorp, Long serviceCorp) {

        List<Long> engineers = null;

        List<DeviceInfo> deviceInfoList = this.list(new QueryWrapper<DeviceInfo>()
                .inSql("device_id", "select device_id from device_locate where device_code=" + deviceCode)
                .eq("brand_id", brandId)
                .eq("model_id", modelId)
                .eq("small_class_id", smallClassId)
                .eq("serial", serial)
                .eq("demander_corp", demanderCorp)
                .eq("service_corp", serviceCorp)
        );
        if (deviceInfoList != null && deviceInfoList.size() > 0) {
            List<Long> deviceInfoIds = new ArrayList<>();
            for (DeviceInfo info : deviceInfoList) {
                deviceInfoIds.add(info.getDeviceId());
            }
            List<DeviceService> deviceServiceList = deviceServiceService.list(new QueryWrapper<DeviceService>()
                    .in("device_id", deviceInfoIds));

            if (deviceServiceList != null && deviceServiceList.size() > 0) {
                engineers = new ArrayList<>();
                for (DeviceService deviceService : deviceServiceList) {
                    engineers.add(deviceService.getEngineer());

                }
            }

        }
        return engineers;
    }

    /**
     * 根据设备条件获得设备分类列表
     *
     * @param deviceInfoFilter
     * @return
     * @author zgpi
     * @date 2019/10/22 2:08 下午
     **/
    @Override
    public List<DeviceLargeClassDto> listDeviceClass(DeviceInfoFilter deviceInfoFilter) {
        if (StrUtil.isBlank(deviceInfoFilter.getSerial()) && StrUtil.isBlank(deviceInfoFilter.getDeviceCode())) {
            throw new AppException("出厂序列号和设备编号不能同时为空！");
        }
        List<DeviceLargeClassDto> largeClassDtoList = new ArrayList<>();
        List<DeviceSmallClassDto> list = deviceInfoMapper.listDeviceClass(deviceInfoFilter);
        Map<DeviceLargeClassDto, List<DeviceSmallClassDto>> map = new LinkedHashMap<>();
        for (DeviceSmallClassDto deviceClassDto : list) {
            DeviceLargeClassDto largeDto = new DeviceLargeClassDto();
            largeDto.setId(deviceClassDto.getLargeClassId());
            largeDto.setName(deviceClassDto.getLargeClassName());
            List<DeviceSmallClassDto> smallClassList = new ArrayList<>();
            if (map.containsKey(largeDto)) {
                smallClassList = map.get(largeDto);
            }
            smallClassList.add(deviceClassDto);
            map.put(largeDto, smallClassList);
        }
        for (DeviceLargeClassDto deviceLargeClassDto : map.keySet()) {
            deviceLargeClassDto.setDeviceSmallClassDtoList(map.get(deviceLargeClassDto));
            largeClassDtoList.add(deviceLargeClassDto);
        }
        return largeClassDtoList;
    }

    /**
     * 根据设备条件获得设备型号列表
     *
     * @param deviceInfoFilter
     * @return
     * @author zgpi
     * @date 2019/10/22 2:08 下午
     **/
    @Override
    public List<DeviceBrandDto> listDeviceModel(DeviceInfoFilter deviceInfoFilter) {
        if (StrUtil.isBlank(deviceInfoFilter.getSerial()) && StrUtil.isBlank(deviceInfoFilter.getDeviceCode())) {
            throw new AppException("出厂序列号和设备编号不能同时为空！");
        }
        List<DeviceBrandDto> deviceBrandDtoList = new ArrayList<>();
        List<DeviceModelDto> list = deviceInfoMapper.listDeviceModel(deviceInfoFilter);
        Map<DeviceBrandDto, List<DeviceModelDto>> map = new LinkedHashMap<>();
        for (DeviceModelDto deviceModelDto : list) {
            DeviceBrandDto deviceBrandDto = new DeviceBrandDto();
            deviceBrandDto.setId(deviceModelDto.getBrandId());
            deviceBrandDto.setName(deviceModelDto.getBrandName());
            List<DeviceModelDto> modelDtoList = new ArrayList<>();
            if (map.containsKey(deviceBrandDto)) {
                modelDtoList = map.get(deviceBrandDto);
            }
            modelDtoList.add(deviceModelDto);
            map.put(deviceBrandDto, modelDtoList);
        }
        for (DeviceBrandDto deviceBrandDto : map.keySet()) {
            deviceBrandDto.setDeviceModelDtoList(map.get(deviceBrandDto));
            deviceBrandDtoList.add(deviceBrandDto);
        }
        return deviceBrandDtoList;
    }

    @Override
    public DeviceInfoDto getByDeviceId(Long deviceId) {
        if (LongUtil.isZero(deviceId)) {
            throw new AppException("设备编号不能为空！");
        }
        DeviceInfoDto deviceInfoDto = new DeviceInfoDto();
        DeviceInfo deviceInfo = this.getById(deviceId);
        if (deviceInfo == null) {
            throw new AppException("设备档案不存在！");
        }
        DeviceLocate deviceLocate = this.deviceLocateService.getById(deviceId);
        DeviceService deviceService = this.deviceServiceService.getById(deviceId);
        if (deviceLocate == null) {
            deviceInfoDto.setHasLocate(false);
        }
        if (deviceService == null) {
            deviceInfoDto.setHasService(false);
        }
        BeanUtils.copyProperties(deviceInfo, deviceInfoDto);
        //设备小类
        DeviceSmallClass deviceSmallClass = this.deviceSmallClassService.getById(deviceInfo.getSmallClassId());
        if (deviceSmallClass != null) {
            deviceInfoDto.setSmallClassName(deviceSmallClass.getName());
            deviceInfoDto.setLargeClassId(deviceSmallClass.getLargeClassId());
            //设备大类
            DeviceLargeClass deviceLargeClass = this.deviceLargeClassService.getById(deviceSmallClass.getLargeClassId());
            if (deviceLargeClass != null) {
                deviceInfoDto.setLargeClassName(deviceLargeClass.getName());
            }
        }
        // 设备规格
        if (LongUtil.isNotZero(deviceInfo.getSpecificationId())) {
            DeviceSpecification specification = this.deviceSpecificationService.getById(deviceInfo.getSpecificationId());
            if (specification != null) {
                deviceInfoDto.setSpecificationName(specification.getName());
            }
        }
        //品牌
        if (LongUtil.isNotZero(deviceInfo.getBrandId())) {
            DeviceBrand deviceBrand = this.deviceBrandService.getById(deviceInfo.getBrandId());
            if (deviceBrand != null) {
                deviceInfoDto.setBrandName(deviceBrand.getName());
            }
        }
        //型号
        DeviceModel deviceModel = this.deviceModelService.getById(deviceInfo.getModelId());
        if (deviceModel != null) {
            deviceInfoDto.setModelName(deviceModel.getName());
        }
        List<Long> corpIdList = new ArrayList<>();
        corpIdList.add(deviceInfo.getDemanderCorp());
        corpIdList.add(deviceInfo.getServiceCorp());
        Map<Long, String> corpMap = this.uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList)).getData();
        if (corpMap != null) {
            //服务委托方和服务商
            deviceInfoDto.setDemanderCorpName(corpMap.get(deviceInfo.getDemanderCorp()));
            deviceInfoDto.setServiceCorpName(corpMap.get(deviceInfo.getServiceCorp()));
        }
        deviceInfoDto.setWarrantyStatusName(WarrantyEnum.getNameByCode(deviceInfo.getWarrantyStatus()));
        if (deviceLocate != null) {
            deviceInfoDto.setHasLocate(true);
            BeanUtils.copyProperties(deviceLocate, deviceInfoDto);
            Result customResult = this.anyfixFeignService.findDemanderCustomDtoById(deviceLocate.getCustomId());
            DemanderCustomDto demanderCustomDto = JsonUtil.parseObject(JsonUtil.toJson(customResult.getData()), DemanderCustomDto.class);
            //客户名称
            if (demanderCustomDto != null) {
                deviceInfoDto.setCustomCorp(demanderCustomDto.getCustomCorp());
                deviceInfoDto.setCustomCorpName(demanderCustomDto.getCustomCorpName());
            }
            Result branchResult = this.anyfixFeignService.findDeviceBranch(deviceLocate.getBranchId());
            //设备网点
            DeviceBranchDto deviceBranchDto = JsonUtil.parseObject(JsonUtil.toJson(branchResult.getData()), DeviceBranchDto.class);
            if (deviceBranchDto != null) {
                deviceInfoDto.setBranchName(deviceBranchDto.getBranchName());
//                deviceInfoDto.setContactName(deviceBranchDto.getContactName());
//                deviceInfoDto.setContactPhone(deviceBranchDto.getContactPhone());
            }
            Result areaResult = uasFeignService.mapAreaCodeAndName();
            Map<String, String> areaMap = JsonUtil.parseObject(JsonUtil.toJson(areaResult.getData()), Map.class);
            //行政区划
            if (deviceLocate.getDistrict() != null && deviceLocate.getDistrict().length() > 0) {
                if (deviceLocate.getDistrict().length() == 6 || deviceLocate.getDistrict().length() == 4
                        || deviceLocate.getDistrict().length() == 2) {
                    String provinceName = areaMap.get(deviceLocate.getDistrict().substring(0, 2));
                    provinceName = provinceName == null ? "" : provinceName;
                    if (deviceLocate.getDistrict().length() == 2) {
                        deviceInfoDto.setDistrictName(provinceName);
                    } else {
                        String cityName = areaMap.get(deviceLocate.getDistrict().substring(0, 4));
                        cityName = cityName == null ? "" : cityName;
                        if (deviceLocate.getDistrict().length() == 6) {
                            String districtName = areaMap.get(deviceLocate.getDistrict());
                            districtName = districtName == null ? "" : districtName;
                            deviceInfoDto.setDistrictName(provinceName + cityName + districtName);
                        } else {
                            deviceInfoDto.setDistrictName(provinceName + cityName);
                        }
                    }
                }
            }
            //设备状态
            deviceInfoDto.setStatusName(DeviceStatusEnum.getNameByCode(deviceLocate.getStatus()));
        }
        if (deviceService != null) {
            deviceInfoDto.setHasService(true);
            BeanUtils.copyProperties(deviceService, deviceInfoDto);
            //服务网点
            if (LongUtil.isNotZero(deviceService.getServiceBranch())) {
                Result result = this.anyfixFeignService.findServiceBranch(deviceService.getServiceBranch());
                ServiceBranchDto serviceBranchDto = JsonUtil.parseObject(JsonUtil.toJson(result.getData()), ServiceBranchDto.class);
                if (serviceBranchDto != null) {
                    deviceInfoDto.setServiceBranchName(serviceBranchDto.getBranchName());
                }
            }
            List<Long> userIdList = new ArrayList<>();
            if (LongUtil.isNotZero(deviceService.getWorkManager())) {
                userIdList.add(deviceService.getWorkManager());
            }
            if (LongUtil.isNotZero(deviceService.getEngineer())) {
                userIdList.add(deviceService.getEngineer());
            }
            Map<Long, String> userIdAndNameMap = this.uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList)).getData();
            if (userIdAndNameMap != null) {
                //派单主管和工程师姓名
                deviceInfoDto.setWorkManagerName(userIdAndNameMap.get(deviceService.getWorkManager()));
                deviceInfoDto.setEngineerName(userIdAndNameMap.get(deviceService.getEngineer()));
            }
        }

        // 获取自定义字段数据列表
        CustomFieldDataFilter customFieldDataFilter = new CustomFieldDataFilter();
        customFieldDataFilter.setFormId(deviceId);
        String jsonObject = JsonUtil.toJson(customFieldDataFilter);

        Result customFieldDataResult = this.anyfixFeignService.queryCustomFieldData(jsonObject);
        List<CustomFieldDataDto> customFieldDataDtoList = JsonUtil.parseArray(JsonUtil.toJson(customFieldDataResult.getData()), CustomFieldDataDto.class);
        deviceInfoDto.setCustomFieldDataList(customFieldDataDtoList);
        return deviceInfoDto;
    }

    /**
     * 批量添加设备档案，此处无校验重复性，需在调用次方法之前校验重复性
     *
     * @param deviceInfoDtoList
     * @return
     */
    @Override
    public void batchSaveDeviceInfo(List<DeviceInfoDto> deviceInfoDtoList,
                                    UserInfo userInfo, Long demanderCorp,
                                    Map<String,Object> needInsertMap) {
        List<String> needInsertCustomCorpName = (List<String>) needInsertMap.get("customCorpName");
        List<CustomDeviceRelate> customDeviceRelateList = (List<CustomDeviceRelate>) needInsertMap.get("customDevice");
        List<DeviceModel> needInsertDeviceMode = ( List<DeviceModel>) needInsertMap.get("deviceModel");
        Map<DeviceInfoDto, DeviceModel> infoDtoDeviceModelMap = (Map<DeviceInfoDto, DeviceModel>) needInsertMap.get("infoDeviceModel");
        Map<String,Map<String,Long>> customCorpCustomMap = new HashMap<>();
        Map<DeviceModel,Long> deviceModelMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(needInsertCustomCorpName)) {
            Result<Map<String,Map<String,Long>>> result = anyfixFeignService.batchSaveCustomCorp(needInsertCustomCorpName,
                    demanderCorp, userInfo.getUserId());
            if(result != null && result.getCode() == Result.SUCCESS) {
                customCorpCustomMap = result.getData();
            }
        }
        if(CollectionUtil.isNotEmpty(needInsertDeviceMode)) {
            deviceModelMap = deviceModelService.batchAddDeviceModel(needInsertDeviceMode,userInfo.getUserId());
        }
        if(CollectionUtil.isNotEmpty(infoDtoDeviceModelMap)) {
            //map.entrySet()得到的是set集合，可以使用迭代器遍历
            for (Map.Entry<DeviceInfoDto, DeviceModel> entry : infoDtoDeviceModelMap.entrySet()) {
                DeviceInfoDto deviceInfoDto = entry.getKey();
                deviceInfoDto.setModelId(deviceModelMap.get(entry.getValue()));
            }
        }
        Map<String,Long> nameBranchIdMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(customDeviceRelateList)) {
            for( CustomDeviceRelate customDeviceRelate : customDeviceRelateList) {
                if(LongUtil.isZero(customDeviceRelate.getCustomId())) {
                    Map<String,Long> stringLongMap = customCorpCustomMap.get(customDeviceRelate.getCustomCorpName());
                    if(stringLongMap != null) {
                        customDeviceRelate.setCustomCorp(stringLongMap.get("customCorp"));
                        customDeviceRelate.setCustomId(stringLongMap.get("customId"));
                    }
                }
            }
            Result<Map<String,Long>> result = anyfixFeignService.batchAddDeviceBranch(JsonUtil.toJson(customDeviceRelateList));
            if(result != null && result.getCode() == Result.SUCCESS) {
                nameBranchIdMap = result.getData();
            }
        }
        List<DeviceInfo> deviceInfoList = new ArrayList<>();
        List<DeviceLocate> deviceLocateList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(deviceInfoDtoList)) {
            for (DeviceInfoDto deviceInfoDto: deviceInfoDtoList) {
                if(LongUtil.isZero(deviceInfoDto.getCustomId())) {
                    Map<String,Long> stringLongMap = customCorpCustomMap
                            .get(deviceInfoDto.getCustomCorpName());
                    if(stringLongMap != null) {
                        deviceInfoDto.setCustomId(stringLongMap.get("customId"));
                    }
                }
                if(StrUtil.isNotBlank(deviceInfoDto.getBranchName())) {
                    if(LongUtil.isZero(deviceInfoDto.getBranchId())) {
                        deviceInfoDto.setBranchId(nameBranchIdMap.get(deviceInfoDto.getBranchName()));
                    }
                }
                if(StrUtil.isNotBlank(deviceInfoDto.getModelName())) {
                    if(LongUtil.isZero(deviceInfoDto.getModelId())) {

                    }
                }
                Date curDateTime = DateUtil.date();
                Long deviceId = KeyUtil.getId();
                DeviceInfo deviceInfo = new DeviceInfo();
                BeanUtils.copyProperties(deviceInfoDto, deviceInfo);
                deviceInfo.setDeviceId(deviceId);
                deviceInfo.setOperator(userInfo.getUserId());
                deviceInfo.setOperateTime(curDateTime);
                deviceInfoList.add(deviceInfo);
                // 添加了用户信息则需保存到设备位置表
                if (LongUtil.isNotZero(deviceInfoDto.getCustomId())) {
                    DeviceLocate deviceLocate = new DeviceLocate();
                    BeanUtils.copyProperties(deviceInfoDto, deviceLocate);
                    deviceLocate.setDeviceId(deviceId);
                    deviceLocate.setOperator(userInfo.getUserId());
                    deviceLocate.setOperateTime(DateUtil.date());
                    deviceLocateList.add(deviceLocate);
                }
            }
        }
        if (CollectionUtil.isNotEmpty(deviceInfoList)) {
            this.saveBatch(deviceInfoList);
        }
        if (CollectionUtil.isNotEmpty(deviceLocateList)) {
            this.deviceLocateService.saveBatch(deviceLocateList);
        }
    }

    /**
     * 查询是否已存在设备档案时使用,key为序列号+设备型号,value为deviceId
     *
     * @param demanderCorp
     * @return
     */
    @Override
    public Map<String, Long> checkExistMap(Long demanderCorp) {
        Map<String, Long> map = new HashMap<>();
        if (LongUtil.isZero(demanderCorp)) {
            return map;
        }
        QueryWrapper<DeviceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("demander_corp", demanderCorp);
        List<DeviceInfo> list = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(deviceInfo -> {
                if (!StringUtils.isEmpty(deviceInfo.getSerial()) && LongUtil.isNotZero(deviceInfo.getModelId())) {
                    map.put(deviceInfo.getSerial() + deviceInfo.getModelId(), deviceInfo.getDeviceId());
                }
            });
        }
        return map;
    }

    @Override
    public List<DeviceInfoDto> findDeviceInfoListBy(DeviceInfoFilter deviceInfoFilter) {
        return this.baseMapper.findDeviceInfo(deviceInfoFilter);
    }

    /**
     * 工单服务完成时，处理设备型号
     *
     * @param deviceInfoDto
     * @return
     * @author zgpi
     * @date 2020/2/15 20:15
     */
    private DeviceInfoDto dealDeviceModel(DeviceInfoDto deviceInfoDto) {
        if (LongUtil.isZero(deviceInfoDto.getModelId())) {
            DeviceModel deviceModel = new DeviceModel();
            deviceModel.setCorp(deviceInfoDto.getDemanderCorp());
            deviceModel.setSmallClassId(deviceInfoDto.getSmallClassId());
            deviceModel.setBrandId(deviceInfoDto.getBrandId());
            deviceModel.setEnabled(EnabledEnum.YES.getCode());
            deviceModel.setName(StrUtil.trimToEmpty(deviceInfoDto.getModelName()));
            deviceModel.setOperator(deviceInfoDto.getOperator());
            deviceModel.setOperateTime(DateUtil.date());
            deviceModel.setId(KeyUtil.getId());
            QueryWrapper<DeviceModel> modelQueryWrapper = new QueryWrapper<>();
            modelQueryWrapper.eq("corp", deviceModel.getCorp());
            modelQueryWrapper.eq("small_class_id", deviceModel.getSmallClassId());
            modelQueryWrapper.eq("brand_id", deviceModel.getBrandId());
            modelQueryWrapper.eq("enabled", EnabledEnum.YES.getCode());
            modelQueryWrapper.eq("name", deviceModel.getName());
            List<DeviceModel> deviceModelList = deviceModelService.list(modelQueryWrapper);
            if (CollectionUtil.isNotEmpty(deviceModelList)) {
                deviceInfoDto.setModelId(deviceModelList.get(0).getId());
            } else {
                deviceModelService.save(deviceModel);
                deviceInfoDto.setModelId(deviceModel.getId());
            }
        }
        return deviceInfoDto;
    }

    /**
     * 工单服务完成时，处理设备规格
     *
     * @param deviceInfoDto
     * @author zgpi
     * @date 2020/7/2 15:07
     * @return
     **/
    private DeviceInfoDto dealDeviceSpecification(DeviceInfoDto deviceInfoDto) {
        if (LongUtil.isZero(deviceInfoDto.getSpecificationId())) {
            DeviceSpecification deviceSpecification = new DeviceSpecification();
            deviceSpecification.setCorp(deviceInfoDto.getDemanderCorp());
            deviceSpecification.setSmallClassId(deviceInfoDto.getSmallClassId());
            deviceSpecification.setEnabled(EnabledEnum.YES.getCode());
            deviceSpecification.setName(StrUtil.trimToEmpty(deviceInfoDto.getSpecificationName()));
            deviceSpecification.setOperator(deviceInfoDto.getOperator());
            deviceSpecification.setOperateTime(DateUtil.date());
            deviceSpecification.setId(KeyUtil.getId());
            QueryWrapper<DeviceSpecification> specificationWrapper = new QueryWrapper<>();
            specificationWrapper.eq("corp", deviceSpecification.getCorp());
            specificationWrapper.eq("small_class_id", deviceSpecification.getSmallClassId());
            specificationWrapper.eq("enabled", EnabledEnum.YES.getCode());
            specificationWrapper.eq("name", deviceSpecification.getName());
            List<DeviceSpecification> specificationList = deviceSpecificationService.list(specificationWrapper);
            if (CollectionUtil.isNotEmpty(specificationList)) {
                deviceInfoDto.setSpecificationId(specificationList.get(0).getId());
            } else {
                deviceSpecificationService.save(deviceSpecification);
                deviceInfoDto.setSpecificationId(deviceSpecification.getId());
            }
        }
        return deviceInfoDto;
    }
}
