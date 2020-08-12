package com.zjft.usp.zj.work.baseinfo.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.common.constant.StatusCodeConstants;
import com.zjft.usp.zj.common.utils.SendWoUtil;
import com.zjft.usp.zj.common.utils.VariableConvertUtil;
import com.zjft.usp.zj.device.atm.dto.DeviceDto;
import com.zjft.usp.zj.device.atm.filter.DeviceFilter;
import com.zjft.usp.zj.device.atm.mapping.DeviceMapping;
import com.zjft.usp.zj.work.baseinfo.composite.BaseInfoCompoService;
import com.zjft.usp.zj.work.baseinfo.dto.*;
import com.zjft.usp.zj.work.baseinfo.enums.BankLevelEnum;
import com.zjft.usp.zj.work.baseinfo.filter.*;
import com.zjft.usp.zj.work.baseinfo.mapping.DeviceBranchMapping;
import com.zjft.usp.zj.work.baseinfo.mapping.TraceRuleMapping;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 基础数据聚合实现类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-13 16:33
 **/
@Service
public class BaseInfoCompoServiceImpl implements BaseInfoCompoService {

    @Resource
    private SendWoUtil sendWoUtil;

    @Value("${wo.baseinfo.listManagerUrl}")
    private String listManagerUrl;
    @Value("${wo.baseinfo.listCaseAllUrl}")
    private String listCaseAllUrl;
    @Value("${wo.baseinfo.listCaseBaseUrl}")
    private String listCaseBaseUrl;
    @Value("${wo.baseinfo.listCaseSubTypeUrl}")
    private String listCaseSubTypeUrl;
    @Value("${wo.baseinfo.listServiceBranchLimitUrl}")
    private String listServiceBranchLimitUrl;
    @Value("${wo.baseinfo.listDeviceBranchUrl}")
    private String listDeviceBranchUrl;
    @Value("${wo.baseinfo.listDeviceModelUrl}")
    private String listDeviceModelUrl;
    @Value("${wo.baseinfo.listSoftVersionUrl}")
    private String listSoftVersionUrl;
    @Value("${wo.baseinfo.listBvSoftVersionUrl}")
    private String listBvSoftVersionUrl;
    @Value("${wo.baseinfo.listDeviceUrl}")
    private String listDeviceUrl;
    @Value("${wo.baseinfo.listEngineerUrl}")
    private String listEngineerUrl;
    @Value("${wo.baseinfo.listTraceRuleUrl}")
    private String listTraceRuleUrl;
    @Value("${wo.baseinfo.findDeviceCodesBySerialsUrl}")
    private String findDeviceCodesBySerialsUrl;
    @Value("${wo.baseinfo.findSerialsByDeviceCodesUrl}")
    private String findSerialsByDeviceCodesUrl;
    @Value("${wo.baseinfo.findDeviceDetailUrl}")
    private String findDeviceDetailUrl;
    @Value("${wo.baseinfo.checkDeviceStatusUrl}")
    private String checkDeviceStatusUrl;
    @Value("${wo.baseinfo.listManMadeUrl}")
    private String listManMadeUrl;
    @Value("${wo.baseinfo.listDealWayUrl}")
    private String listDealWayUrl;

    /**
     * 获得CASE基本信息
     * 工程师信息、当前时间等
     *
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/26 16:10
     */
    @Override
    public CaseBaseDto findCaseBase(UserInfo userInfo, ReqParam reqParam) {
        CaseBaseDto caseBaseDto = new CaseBaseDto();
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, listCaseAllUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String caseDto = resultObject.getString("yjcaseDto");
            if (StrUtil.isNotBlank(caseDto)) {
                JSONObject caseObject = JsonUtil.parseObject(caseDto, JSONObject.class);
                String engineerName = caseObject.getString("gcsname");
                caseBaseDto.setEngineerName(StrUtil.trimToEmpty(engineerName));
            }
            String userId = resultObject.getString("currentUserId");
            caseBaseDto.setUserId(StrUtil.trimToEmpty(userId));
            caseBaseDto.setCurrentTime(DateUtil.now());
            String warrantyMapString = resultObject.getString("deviceServiceStatusMap");
            if (StrUtil.isNotBlank(warrantyMapString)) {
                Map<String, String> warrantyMap = JsonUtil.parseMap(warrantyMapString);
                caseBaseDto.setWarrantyMap(warrantyMap);
            }
        }
        return caseBaseDto;
    }

    /**
     * 获得CASE查询的基本信息
     * 分布列表，权限列表
     *
     * @param serviceBranch
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/4/13 11:27
     */
    @Override
    public CaseBaseDto findCaseQueryBase(String serviceBranch, UserInfo userInfo, ReqParam reqParam) {
        CaseBaseDto caseBaseDto = new CaseBaseDto();
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        if (StrUtil.isNotBlank(serviceBranch)) {
            paramMap.add("ocode", serviceBranch);
        }
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, listCaseBaseUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String userId = resultObject.getString("currentUserId");
            caseBaseDto.setUserId(StrUtil.trimToEmpty(userId));
            String caseBaseMapJson = resultObject.getString("casebaseMap");
            if (StrUtil.isNotBlank(caseBaseMapJson)) {
                Map<String, JSONObject> map = JsonUtil.parseMap(caseBaseMapJson);
                map = map == null ? new HashMap<>(0) : map;
                JSONObject areaJsonObject = map.get("areaMap");
                if (areaJsonObject != null) {
                    Map<String, String> areaMap = JsonUtil.parseMapSort(JsonUtil.toJson(areaJsonObject));
                    areaMap = areaMap == null ? new HashMap<>(0) : areaMap;
                    List<AreaDto> areaDtoList = new ArrayList<>();
                    AreaDto areaDto;
                    for (Iterator<Map.Entry<String, String>> it = areaMap.entrySet().iterator(); it.hasNext(); ) {
                        Map.Entry<String, String> itt = it.next();
                        areaDto = new AreaDto();
                        areaDto.setCode(itt.getKey());
                        areaDto.setName(itt.getValue());
                        areaDtoList.add(areaDto);
                    }
                    caseBaseDto.setAreaList(areaDtoList);
                }
            }
            String loginUserJson = resultObject.getString("loginUser");
            if (StrUtil.isNotBlank(loginUserJson)) {
                resultObject = JsonUtil.parseObject(loginUserJson, JSONObject.class);
                String scRightJson = resultObject.getString("scRight");
                if (StrUtil.isNotBlank(scRightJson)) {
                    resultObject = JsonUtil.parseObject(scRightJson, JSONObject.class);
                    String rightA = resultObject.getString("rightA");
                    caseBaseDto.setRightA(StrUtil.trimToEmpty(rightA));
                }
            }
        }
        return caseBaseDto;
    }

    /**
     * 获得工程师
     *
     * @param serviceBranchName
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/26 16:10
     */
    @Override
    public CaseBaseDto findEngineer(String serviceBranchName, UserInfo userInfo, ReqParam reqParam) {
        CaseBaseDto caseBaseDto = new CaseBaseDto();
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        if (StrUtil.isNotBlank(serviceBranchName)) {
            paramMap.add("bureauName", serviceBranchName);
        }
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, listCaseAllUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String caseDto = resultObject.getString("yjcaseDto");
            if (StrUtil.isNotBlank(caseDto)) {
                JSONObject caseObject = JsonUtil.parseObject(caseDto, JSONObject.class);
                String engineerName = caseObject.getString("gcsname");
                caseBaseDto.setEngineerName(StrUtil.trimToEmpty(engineerName));
            }
            String userId = resultObject.getString("currentUserId");
            caseBaseDto.setUserId(StrUtil.trimToEmpty(userId));
        }
        return caseBaseDto;
    }

    /**
     * CASE类型
     *
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/23 09:26
     */
    @Override
    public List<String> listCaseType(UserInfo userInfo, ReqParam reqParam) {
        List<String> list = new ArrayList<>();
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, listCaseBaseUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String caseBaseMap = resultObject.getString("casebaseMap");
            if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
                resultObject = JsonUtil.parseObject(caseBaseMap, JSONObject.class);
            }
            String caseTypeList = resultObject.getString("caseTypeList");
            if (StrUtil.isNotBlank(caseTypeList)) {
                list = JsonUtil.parseArray(caseTypeList, String.class);
            }
        }
        return list;
    }

    /**
     * 获得CASE子类型列表
     *
     * @param caseType
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/23 10:40
     */
    @Override
    public Map<Integer, String> listCaseSubType(String caseType, UserInfo userInfo, ReqParam reqParam) {
        Map<Integer, String> map = new HashMap<>();
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycasetype", caseType);
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, listCaseSubTypeUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String mapSubTypeIdAndName = resultObject.getString("mapSubTypeIdAndName");
            if (StrUtil.isNotBlank(mapSubTypeIdAndName)) {
                map = JsonUtil.parseMap(mapSubTypeIdAndName);
                map = map == null ? new HashMap<>(0) : map;
            }
        }
        return map;
    }

    /**
     * 分页查询用户权限范围内的服务站列表
     *
     * @param serviceBranchFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/23 13:38
     */
    @Override
    public ListWrapper<ServiceBranchDto> queryServiceBranch(ServiceBranchFilter serviceBranchFilter,
                                                            UserInfo userInfo, ReqParam reqParam) {
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, listServiceBranchLimitUrl);
        List<ServiceBranchDto> serviceBranchDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String bureauMap = resultObject.getString("bureauMap");
            Map<String, String> map = JsonUtil.parseMap(bureauMap);
            map = map == null ? new HashMap<>(0) : map;
            ServiceBranchDto serviceBranchDto;
            for (Iterator<Map.Entry<String, String>> it = map.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, String> itt = it.next();
                serviceBranchDto = new ServiceBranchDto();
                serviceBranchDto.setBranchId(itt.getKey());
                serviceBranchDto.setBranchName(itt.getValue());
                serviceBranchDtoList.add(serviceBranchDto);
            }
        }
        // 根据名称模糊查询
        String branchName = StrUtil.trimToEmpty(serviceBranchFilter.getBranchName());
        if (StrUtil.isNotBlank(branchName)) {
            serviceBranchDtoList = serviceBranchDtoList.stream().filter(item -> item.getBranchName()
                    .contains(branchName)).collect(Collectors.toList());
        }
        serviceBranchDtoList.sort(Comparator.comparing(ServiceBranchDto::getBranchName));
        List<ServiceBranchDto> pageList = serviceBranchDtoList.stream()
                .skip(serviceBranchFilter.getPageSize() * (serviceBranchFilter.getPageNum() - 1))
                .limit(serviceBranchFilter.getPageSize()).collect(Collectors.toList());
        return ListWrapper.<ServiceBranchDto>builder()
                .list(pageList)
                .total(Long.parseLong(String.valueOf(serviceBranchDtoList.size())))
                .build();
    }

    /**
     * 分页查询设备网点列表
     *
     * @param deviceBranchFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/23 21:48
     */
    @Override
    public ListWrapper<DeviceBranchDto> queryDeviceBranch(DeviceBranchFilter deviceBranchFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("userCode", deviceBranchFilter.getBankCode());
        paramMap.add("depotCode", deviceBranchFilter.getServiceBranch());
        paramMap.add("branchName", deviceBranchFilter.getBranchName());
        Page<DeviceBranchDto> page = new Page(deviceBranchFilter.getPageNum(), deviceBranchFilter.getPageSize());
        paramMap.add("dto.pageSize", page.getSize());
        paramMap.add("dto.currentPage", page.getCurrent());
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, listDeviceBranchUrl);
        List<DeviceBranchDto> deviceBranchDtoList = new ArrayList<>();
        Long totalCount = 0L;
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String dataJson = resultObject.getString("dto");
            if (StrUtil.isNotEmpty(dataJson) && dataJson.matches(StatusCodeConstants.JSON_REGEX)) {
                resultObject = JsonUtil.parseObject(dataJson, JSONObject.class);
            }
            List<Object> list = new ArrayList<>();
            String data = resultObject.getString("data");
            if (StrUtil.isNotEmpty(data)) {
                list = JsonUtil.parseArray(data, Object.class);
                list = list == null ? new ArrayList<>() : list;
            }
            DeviceBranchDto deviceBranchDto;
            for (Object obj : list) {
                deviceBranchDto = VariableConvertUtil.convertToNewEntity(JsonUtil.toJson(obj), DeviceBranchMapping.getOldAndNewPropertyMap(), DeviceBranchDto.class);
                if (deviceBranchDto != null) {
                    deviceBranchDtoList.add(deviceBranchDto);
                }
            }
            totalCount = resultObject.getLong("totalCount");
        }
        return ListWrapper.<DeviceBranchDto>builder()
                .list(deviceBranchDtoList)
                .total(totalCount)
                .build();
    }

    /**
     * 分页查询总行列表
     *
     * @param bankFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/23 20:19
     */
    @Override
    public ListWrapper<BankDto> queryHeadBank(BankFilter bankFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ocode", StrUtil.trimToEmpty(bankFilter.getServiceBranch()));
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, listCaseBaseUrl);
        List<BankDto> bankDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String casebaseMap = resultObject.getString("casebaseMap");
            resultObject = JsonUtil.parseObject(casebaseMap, JSONObject.class);
            String headBankMap = resultObject.getString("headBankMap");
            Map<String, String> map = JsonUtil.parseMapSort(headBankMap);
            map = map == null ? new LinkedHashMap<>(0) : map;
            BankDto bankDto;
            for (Iterator<Map.Entry<String, String>> it = map.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, String> itt = it.next();
                bankDto = new BankDto();
                bankDto.setBankCode(itt.getKey());
                bankDto.setBankName(itt.getValue());
                bankDto.setServiceBranch(bankFilter.getServiceBranch());
                bankDto.setBankLevel(BankLevelEnum.FIRST.getCode());
                bankDtoList.add(bankDto);
            }
        }
        // 根据名称模糊查询
        String bankName = StrUtil.trimToEmpty(bankFilter.getBankName());
        if (StrUtil.isNotBlank(bankName)) {
            bankDtoList = bankDtoList.stream().filter(item -> item.getBankName()
                    .contains(bankName)).collect(Collectors.toList());
        }
        bankDtoList.sort(Comparator.comparing(BankDto::getBankName));
        List<BankDto> pageList = bankDtoList.stream()
                .skip(bankFilter.getPageSize() * (bankFilter.getPageNum() - 1))
                .limit(bankFilter.getPageSize()).collect(Collectors.toList());
        return ListWrapper.<BankDto>builder()
                .list(pageList)
                .total(Long.parseLong(String.valueOf(bankDtoList.size())))
                .build();
    }

    /**
     * 分页查询分行列表
     *
     * @param bankFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/23 21:48
     */
    @Override
    public ListWrapper<BankDto> querySubBank(BankFilter bankFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ocode", StrUtil.trimToEmpty(bankFilter.getServiceBranch()));
        paramMap.add("tcode", StrUtil.trimToEmpty(bankFilter.getHeadBankCode()));
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, listCaseBaseUrl);
        List<BankDto> bankDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String bnameMap = resultObject.getString("bnameMap");
            Map<String, String> map = JsonUtil.parseMapSort(bnameMap);
            map = map == null ? new LinkedHashMap<>(0) : map;
            BankDto bankDto;
            for (Iterator<Map.Entry<String, String>> it = map.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, String> itt = it.next();
                bankDto = new BankDto();
                bankDto.setBankCode(itt.getKey());
                bankDto.setBankName(itt.getValue());
                bankDto.setServiceBranch(bankFilter.getServiceBranch());
                bankDto.setHeadBankCode(bankFilter.getHeadBankCode());
                bankDto.setBankLevel(BankLevelEnum.SECOND.getCode());
                bankDtoList.add(bankDto);
            }
        }
        // 根据名称模糊查询
        String bankName = StrUtil.trimToEmpty(bankFilter.getBankName());
        if (StrUtil.isNotBlank(bankName)) {
            bankDtoList = bankDtoList.stream().filter(item -> item.getBankName()
                    .contains(bankName)).collect(Collectors.toList());
        }
        bankDtoList.sort(Comparator.comparing(BankDto::getBankName));
        List<BankDto> pageList = bankDtoList.stream()
                .skip(bankFilter.getPageSize() * (bankFilter.getPageNum() - 1))
                .limit(bankFilter.getPageSize()).collect(Collectors.toList());
        return ListWrapper.<BankDto>builder()
                .list(pageList)
                .total(Long.parseLong(String.valueOf(bankDtoList.size())))
                .build();
    }

    /**
     * 分页查询机器型号列表
     *
     * @param deviceModelFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/24 20:25
     */
    @Override
    public ListWrapper<DeviceModelDto> queryDeviceModel(DeviceModelFilter deviceModelFilter, UserInfo userInfo, ReqParam reqParam) {
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, listDeviceModelUrl);
        List<DeviceModelDto> deviceModelDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String deviceTypeMap = resultObject.getString("deviceTypeMap");
            Map<String, String> map = JsonUtil.parseMapSort(deviceTypeMap);
            map = map == null ? new LinkedHashMap<>(0) : map;
            DeviceModelDto deviceModelDto;
            for (Iterator<Map.Entry<String, String>> it = map.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, String> itt = it.next();
                deviceModelDto = new DeviceModelDto();
                deviceModelDto.setModelId(itt.getKey());
                deviceModelDto.setModelName(itt.getValue());
                deviceModelDtoList.add(deviceModelDto);
            }
        }
        // 根据名称模糊查询
        String modelName = StrUtil.trimToEmpty(deviceModelFilter.getModelName());
        if (StrUtil.isNotBlank(modelName)) {
            deviceModelDtoList = deviceModelDtoList.stream().filter(item -> item.getModelName()
                    .contains(modelName)).collect(Collectors.toList());
        }
        deviceModelDtoList.sort(Comparator.comparing(DeviceModelDto::getModelName));
        List<DeviceModelDto> pageList = deviceModelDtoList.stream()
                .skip(deviceModelFilter.getPageSize() * (deviceModelFilter.getPageNum() - 1))
                .limit(deviceModelFilter.getPageSize()).collect(Collectors.toList());
        return ListWrapper.<DeviceModelDto>builder()
                .list(pageList)
                .total(Long.parseLong(String.valueOf(deviceModelDtoList.size())))
                .build();
    }

    /**
     * 查询机器型号列表
     *
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/25 14:17
     */
    @Override
    public List<DeviceModelDto> listDeviceModel(UserInfo userInfo, ReqParam reqParam) {
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, listDeviceModelUrl);
        List<DeviceModelDto> deviceModelDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String deviceTypeMap = resultObject.getString("deviceTypeMap");
            Map<String, String> map = JsonUtil.parseMapSort(deviceTypeMap);
            map = map == null ? new LinkedHashMap<>(0) : map;
            DeviceModelDto deviceModelDto;
            for (Iterator<Map.Entry<String, String>> it = map.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, String> itt = it.next();
                deviceModelDto = new DeviceModelDto();
                deviceModelDto.setModelId(itt.getKey());
                deviceModelDto.setModelName(itt.getValue());
                deviceModelDtoList.add(deviceModelDto);
            }
        }
        deviceModelDtoList.sort(Comparator.comparing(DeviceModelDto::getModelName));
        return deviceModelDtoList;
    }

    /**
     * 查询机器列表
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/25 10:56
     */
    @Override
    public List<DeviceDto> listDevice(DeviceFilter deviceFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("branchId", StrUtil.trimToEmpty(deviceFilter.getDeviceBranch()));
        // 无网点编号时可以根据客户编号和网点名称查找网点编号
        paramMap.add("branchName", StrUtil.trimToEmpty(deviceFilter.getDeviceBranchName()));
        paramMap.add("bankCode", StrUtil.trimToEmpty(deviceFilter.getCustomId()));
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, listDeviceUrl);
        List<DeviceDto> deviceDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String dataJson = resultObject.getString("dto");
            if (StrUtil.isNotEmpty(dataJson) && dataJson.matches(StatusCodeConstants.JSON_REGEX)) {
                resultObject = JsonUtil.parseObject(dataJson, JSONObject.class);
            }
            List<Object> list = new ArrayList<>();
            String data = resultObject.getString("data");
            if (StrUtil.isNotEmpty(data)) {
                list = JsonUtil.parseArray(data, Object.class);
                list = list == null ? new ArrayList<>() : list;
            }
            DeviceDto deviceDto;
            for (Object obj : list) {
                deviceDto = VariableConvertUtil.convertToNewEntity(JsonUtil.toJson(obj), DeviceMapping.getOldAndNewPropertyMap(), DeviceDto.class);
                if (deviceDto != null) {
                    deviceDtoList.add(deviceDto);
                }
            }
        }
        deviceDtoList.sort(Comparator.comparing(DeviceDto::getDeviceModelName)
                .thenComparing(DeviceDto::getDeviceCode));
        return deviceDtoList;
    }

    /**
     * 获得机器详情
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/27 20:26
     */
    @Override
    public DeviceDto findDeviceDetail(DeviceFilter deviceFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("deviceType", StrUtil.trimToEmpty(deviceFilter.getDeviceModel()));
        paramMap.add("deviceCode", StrUtil.trimToEmpty(deviceFilter.getSerial()));
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, findDeviceDetailUrl);
        DeviceDto deviceDto = new DeviceDto();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String dataJson = resultObject.getString("jsonResult");
            if (StrUtil.isNotBlank(dataJson)) {
                deviceDto = VariableConvertUtil.convertToNewEntity(dataJson, DeviceMapping.getOldAndNewPropertyMap(), DeviceDto.class);
            }
        }
        return deviceDto;
    }

    /**
     * 获得保修状态列表
     *
     * @param serviceBranchName
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/27 20:50
     */
    @Override
    public List<WarrantyStatusDto> listWarrantyStatus(String serviceBranchName, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("bureauName", StrUtil.trimToEmpty(serviceBranchName));
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, listCaseAllUrl);
        List<WarrantyStatusDto> warrantyStatusDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String dataJson = resultObject.getString("deviceServiceStatusMap");
            if (StrUtil.isNotEmpty(dataJson)) {
                Map<String, String> map = JsonUtil.parseMapSort(dataJson);
                WarrantyStatusDto warrantyStatusDto;
                if (map != null && map.size() > 0) {
                    for (Iterator<Map.Entry<String, String>> it = map.entrySet().iterator(); it.hasNext(); ) {
                        Map.Entry<String, String> itt = it.next();
                        warrantyStatusDto = new WarrantyStatusDto();
                        warrantyStatusDto.setId(itt.getKey());
                        warrantyStatusDto.setName(itt.getValue());
                        warrantyStatusDtoList.add(warrantyStatusDto);
                    }
                }
            }
        }
        return warrantyStatusDtoList;
    }

    /**
     * 根据制造号查找终端号
     *
     * @param modelId
     * @param serials
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/27 09:42
     */
    @Override
    public String findDeviceCodesBySerials(String modelId, String serials, UserInfo userInfo, ReqParam reqParam) {
        String deviceCodes = "";
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("type1", StrUtil.trimToEmpty(modelId));
        paramMap.add("deviceCode", StrUtil.trimToEmpty(serials));
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, findDeviceCodesBySerialsUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            deviceCodes = resultObject.getString("atmcode");
        }
        return deviceCodes;
    }

    /**
     * 根据终端号查找制造号
     *
     * @param deviceCodes
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/27 09:42
     */
    @Override
    public String findSerialsByDeviceCodes(String deviceCodes, UserInfo userInfo, ReqParam reqParam) {
        String serials = "";
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("atmcode", StrUtil.trimToEmpty(deviceCodes));
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, findSerialsByDeviceCodesUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            serials = resultObject.getString("deviceCode");
        }
        return serials;
    }

    /**
     * 检查设备状态
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/28 11:40
     */
    @Override
    public DeviceCheckDto checkDeviceStatus(DeviceFilter deviceFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("type1", deviceFilter.getDeviceModel());
        paramMap.add("deviceCode", deviceFilter.getSerial());
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, checkDeviceStatusUrl);
        DeviceCheckDto deviceCheckDto = new DeviceCheckDto();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String returnCode = resultObject.getString("returnCode");
            String message = resultObject.getString("message");
            deviceCheckDto.setCheckCode(StrUtil.trimToEmpty(returnCode));
            deviceCheckDto.setCheckMsg(StrUtil.trimToEmpty(message));
        }
        return deviceCheckDto;
    }

    /**
     * 查询是否已存在CASE
     *
     * @param bankCode
     * @param branchName
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/26 11:10
     */
    @Override
    public Boolean ifExistCase(String bankCode, String branchName, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("usercode", StrUtil.trimToEmpty(bankCode));
        paramMap.add("branchname", StrUtil.trimToEmpty(branchName));
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, listCaseBaseUrl);
        Boolean isExistCase = false;
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String message = resultObject.getString("message");
            if ("isExistCase".equalsIgnoreCase(StrUtil.trimToEmpty(message))) {
                isExistCase = true;
            }
        }
        return isExistCase;
    }

    /**
     * 分页查询工程师列表
     *
     * @param engineerFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/27 11:21
     */
    @Override
    public ListWrapper<EngineerDto> queryEngineer(EngineerFilter engineerFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ocode", StrUtil.trimToEmpty(engineerFilter.getServiceBranch()));
        paramMap.add("engineerName", StrUtil.trimToEmpty(engineerFilter.getEngineerName()));
        Page<DeviceBranchDto> page = new Page(engineerFilter.getPageNum(), engineerFilter.getPageSize());
        paramMap.add("dto.pageSize", page.getSize());
        paramMap.add("dto.currentPage", page.getCurrent());
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, listEngineerUrl);
        Long totalCount = 0L;
        List<EngineerDto> engineerDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObjectSort(handResult, JSONObject.class);
            totalCount = resultObject.getLong("totalCount");
            String dataJson = resultObject.getString("userIdAndNameMap");
            Map<String, String> map = new LinkedHashMap<>();
            if (StrUtil.isNotEmpty(dataJson)) {
                map = JsonUtil.parseMapSort(dataJson);
                map = map == null ? new LinkedHashMap<>(0) : map;
            }
            EngineerDto engineerDto;
            for (Iterator<Map.Entry<String, String>> it = map.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, String> itt = it.next();
                engineerDto = new EngineerDto();
                engineerDto.setEngineerId(itt.getKey());
                engineerDto.setEngineerName(itt.getValue());
                engineerDtoList.add(engineerDto);
            }
        }
        return ListWrapper.<EngineerDto>builder()
                .list(engineerDtoList)
                .total(totalCount)
                .build();
    }

    /**
     * 查询跟单规则
     *
     * @param workTypeName
     * @param traceRequired
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public List<TraceRuleDto> listTraceRule(String workTypeName, String traceRequired, UserInfo userInfo, ReqParam reqParam) {
        List<TraceRuleDto> dtoList = new ArrayList<>();
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycasetype", StrUtil.trimToEmpty(workTypeName));
        paramMap.add("iftrace", traceRequired);
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, listTraceRuleUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String dataJson = resultObject.getString("caseTraceRuleList");
            List<Object> list = JsonUtil.parseArray(dataJson, Object.class);
            if (list != null && list.size() > 0) {
                TraceRuleDto traceRuleDto;
                for (Object obj : list) {
                    traceRuleDto = VariableConvertUtil.convertToNewEntity(JsonUtil.toJson(obj), TraceRuleMapping.getOldAndNewPropertyMap(), TraceRuleDto.class);
                    if (traceRuleDto != null) {
                        dtoList.add(traceRuleDto);
                    }
                }
            }
        }
        return dtoList;
    }

    /**
     * 分页查询紫金公司服务主管列表(含400)
     *
     * @param userFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-29
     */
    @Override
    public ListWrapper<UserDto> queryManager(UserFilter userFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("userName", StrUtil.trimToEmpty(userFilter.getUserName()));
        Page page = new Page(userFilter.getPageNum(), userFilter.getPageSize());
        paramMap.add("dto.pageSize", page.getSize());
        paramMap.add("dto.currentPage", page.getCurrent());
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, listManagerUrl);
        Long totalCount = 0L;
        List<UserDto> userDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObjectSort(handResult, JSONObject.class);
            totalCount = resultObject.getLong("totalCount");
            String dataJson = resultObject.getString("userIdAndNameMap");
            Map<String, String> map = new LinkedHashMap<>();
            if (StrUtil.isNotEmpty(dataJson)) {
                map = JsonUtil.parseMapSort(dataJson);
                map = map == null ? new LinkedHashMap<>(0) : map;
            }
            UserDto userDto;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                userDto = new UserDto();
                userDto.setUserId(entry.getKey());
                userDto.setUserName(entry.getValue());
                userDtoList.add(userDto);
            }
        }
        return ListWrapper.<UserDto>builder()
                .list(userDtoList)
                .total(totalCount)
                .build();
    }

    /**
     * 获取交通工具列表
     *
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public Map<String, String> listTraffic(UserInfo userInfo, ReqParam reqParam) {
        Map<String, String> trafficTypeMap = new HashMap<>();
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, listCaseBaseUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String trafficTypeMapString = resultObject.getString("trafficTypeMap");
            if (StrUtil.isNotEmpty(trafficTypeMapString) && trafficTypeMapString.matches(StatusCodeConstants.JSON_REGEX)) {
                trafficTypeMap = JsonUtil.parseMap(trafficTypeMapString);
            }
        }
        return trafficTypeMap;
    }

    /**
     * 根据客户获取软件版本，包括软件版本、sp软件版本、bv软件版本和其他软件版本
     *
     * @param customId
     * @param modelId
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public Map<String, List<SoftVersionDto>> listSoftVersion(String customId, String modelId, UserInfo userInfo, ReqParam reqParam) {
        Map<String, List<SoftVersionDto>> map = new HashMap<>();
        Map<String, Object> versionMap = new HashMap<>();
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("usercode", StrUtil.trimToEmpty(customId));
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, listSoftVersionUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            Map<String, Object> resultMap = JsonUtil.parseMap(handResult);
            if (resultMap != null && resultMap.containsKey("softVersionMap")) {
                versionMap = (Map<String, Object>) resultMap.get("softVersionMap");
            }
            // 软件版本
            List<SoftVersionDto> softVersionDtoList = new ArrayList<>();
            if (versionMap.containsKey("versionSoft")) {
                String versionSoftString = (String) versionMap.get("versionSoft");
                List<String> versionSoftList = Arrays.asList(versionSoftString.split("\\|"));
                if (CollectionUtil.isNotEmpty(versionSoftList)) {
                    for (String versionSoft : versionSoftList) {
                        SoftVersionDto softVersionDto = new SoftVersionDto();
                        String[] versionSoftIdAndName = versionSoft.split(",");
                        if (versionSoftIdAndName != null && versionSoftIdAndName.length == 2) {
                            softVersionDto.setVersionId(versionSoftIdAndName[0]);
                            softVersionDto.setVersionName(versionSoftIdAndName[1]);
                            softVersionDtoList.add(softVersionDto);
                        }
                    }
                }
            }
            map.put("softVersion", softVersionDtoList);
            // SP软件版本
            List<SoftVersionDto> softVersionSPDtoList = new ArrayList<>();
            if (versionMap.containsKey("versionSP")) {
                String versionSoftSPString = (String) versionMap.get("versionSP");
                List<String> versionSoftSPList = Arrays.asList(versionSoftSPString.split("\\|"));
                if (CollectionUtil.isNotEmpty(versionSoftSPList)) {
                    for (String versionSoftSP : versionSoftSPList) {
                        SoftVersionDto softVersionDto = new SoftVersionDto();
                        String[] versionSoftIdAndName = versionSoftSP.split(",");
                        if (versionSoftIdAndName != null && versionSoftIdAndName.length == 2) {
                            softVersionDto.setVersionId(versionSoftIdAndName[0]);
                            softVersionDto.setVersionName(versionSoftIdAndName[1]);
                            softVersionSPDtoList.add(softVersionDto);
                        }
                    }
                }
            }
            map.put("spSoftVersion", softVersionSPDtoList);
            // 其他软件版本
            List<SoftVersionDto> softVersionOtherDtoList = new ArrayList<>();
            if (versionMap.containsKey("versionOther")) {
                Map<String, String> versionOtherMap = (Map<String, String>) versionMap.get("versionOther");
                if (versionOtherMap != null) {
                    for (String key : versionOtherMap.keySet()) {
                        SoftVersionDto softVersionDto = new SoftVersionDto();
                        softVersionDto.setVersionId(key);
                        softVersionDto.setVersionName(versionOtherMap.get(key));
                        softVersionOtherDtoList.add(softVersionDto);
                    }
                }
            }
            map.put("otherSoftVersion", softVersionOtherDtoList);
        }
        Map<String, Object> bvResultMap = new HashMap<>();
        MultiValueMap<String, Object> bvParamMap = new LinkedMultiValueMap<>();
        bvParamMap.add("type1", StrUtil.trimToEmpty(modelId));
        String bvHandResult = sendWoUtil.postToWo(userInfo, reqParam, bvParamMap, listBvSoftVersionUrl);
        if (StrUtil.isNotEmpty(bvHandResult) && bvHandResult.matches(StatusCodeConstants.JSON_REGEX)) {
            bvResultMap = JsonUtil.parseMap(bvHandResult);
            Map<String, Object> versionBVMap = new HashMap<>();
            if (bvResultMap.containsKey("softVersionMap")) {
                versionBVMap = (Map<String, Object>) bvResultMap.get("softVersionMap");
            }
            // BV软件版本
            List<SoftVersionDto> softVersionBVDtoList = new ArrayList<>();
            if (versionBVMap.containsKey("versionBV")) {
                String versionSoftBVString = (String) versionBVMap.get("versionBV");
                List<String> versionSoftBVList = Arrays.asList(versionSoftBVString.split("\\|"));
                if (CollectionUtil.isNotEmpty(versionSoftBVList)) {
                    for (String versionSoftBV : versionSoftBVList) {
                        SoftVersionDto softVersionDto = new SoftVersionDto();
                        String[] versionSoftIdAndName = versionSoftBV.split(",");
                        if (versionSoftIdAndName != null && versionSoftIdAndName.length == 2) {
                            softVersionDto.setVersionId(versionSoftIdAndName[0]);
                            softVersionDto.setVersionName(versionSoftIdAndName[1]);
                            softVersionBVDtoList.add(softVersionDto);
                        }
                    }
                }
            }
            map.put("bvSoftVersion", softVersionBVDtoList);
        }
        return map;
    }

    /**
     * 获取是否人为损坏映射Map
     *
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public Map<Integer, String> listManMade(UserInfo userInfo, ReqParam reqParam) {
        Map<Integer, String> map = new HashMap<>();
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, listManMadeUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String manMadeMapStr = resultObject.getString("mapManMaded");
            if (StrUtil.isNotBlank(manMadeMapStr)) {
                map = JsonUtil.parseMap(manMadeMapStr);
            }
        }
        return map;
    }

    /**
     * 获取巡检处理方式映射Map
     *
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public Map<Integer, String> listDealWay(UserInfo userInfo, ReqParam reqParam) {
        Map<Integer, String> map = new HashMap<>();
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, listDealWayUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String dealWayMapStr = resultObject.getString("mapDealWay");
            if (StrUtil.isNotBlank(dealWayMapStr)) {
                map = JsonUtil.parseMap(dealWayMapStr);
            }
        }
        return map;
    }

}
