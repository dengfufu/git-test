package com.zjft.usp.zj.device.atm.composite.impl;

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
import com.zjft.usp.zj.device.atm.composite.DeviceInfoCompoService;
import com.zjft.usp.zj.device.atm.dto.*;
import com.zjft.usp.zj.device.atm.filter.DeviceFilter;
import com.zjft.usp.zj.device.atm.mapping.*;
import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.CaseDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.partreplace.PartReplaceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ATM设备聚合实现类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-13 17:11
 **/
@Slf4j
@Service
public class DeviceInfoCompoServiceImpl implements DeviceInfoCompoService {
    @Resource
    private SendWoUtil sendWoUtil;
    @Value("${wo.device.atm.queryDeviceUrl}")
    private String queryDeviceUrl;
    @Value("${wo.device.atm.findDeviceDetailUrl}")
    private String findDeviceDetailUrl;
    @Value("${wo.device.atm.findDeviceArchiveUrl}")
    private String findDeviceArchiveUrl;
    @Value("${wo.device.atm.queryInstallRecordUrl}")
    private String queryInstallRecordUrl;
    @Value("${wo.device.atm.queryUpdateRecordUrl}")
    private String queryUpdateRecordUrl;
    @Value("${wo.device.atm.queryMaintainPmUrl}")
    private String queryMaintainPmUrl;
    @Value("${wo.device.atm.viewMaintainDetailUrl}")
    private String viewMaintainDetailUrl;
    @Value("${wo.device.atm.viewPmDetailUrl}")
    private String viewPmDetailUrl;
    @Value("${wo.device.atm.queryPartReplaceUrl}")
    private String queryPartReplaceUrl;
    @Value("${wo.device.atm.listRecentCaseUrl}")
    private String listRecentCaseUrl;
    @Value("${wo.device.atm.listRecentPartReplaceUrl}")
    private String listRecentPartReplaceUrl;

    /**
     * 分页查询设备列表
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-29
     */
    @Override
    public ListWrapper<DeviceDto> queryDevice(DeviceFilter deviceFilter, UserInfo userInfo, ReqParam reqParam) {
        Page page = new Page(deviceFilter.getPageNum(), deviceFilter.getPageSize());
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("deviceCode", StrUtil.trimToEmpty(deviceFilter.getSerial()));
        paramMap.add("atmCode", StrUtil.trimToEmpty(deviceFilter.getDeviceCode()));
        paramMap.add("lightingLamp", StrUtil.trimToEmpty(deviceFilter.getLightingLamp()));
        paramMap.add("dto.currentPage", page.getCurrent());
        paramMap.add("dto.pageSize", page.getSize());

        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, queryDeviceUrl);

        Long totalCount = 0L;
        List<DeviceDto> deviceDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObjectSort(handResult, JSONObject.class);
            String dtoData = resultObject.getString("dto");
            JSONObject dtoObject = JsonUtil.parseObject(dtoData, JSONObject.class);
            totalCount = dtoObject.getLong("totalCount");
            deviceDtoList = VariableConvertUtil.convertToNewEntityList(dtoObject.getString("data"),
                    DeviceMapping.getOldAndNewPropertyMap(),
                    DeviceDto.class);
        }
        return ListWrapper.<DeviceDto>builder()
                .list(deviceDtoList)
                .total(totalCount)
                .build();
    }

    /**
     * 获得机器详情
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-28
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
                deviceDto = VariableConvertUtil
                        .convertToNewEntity(dataJson, DeviceMapping.getOldAndNewPropertyMap(), DeviceDto.class);
                EarlyWarnInfoDto earlyWarnInfoDto = VariableConvertUtil
                        .convertToNewEntity(dataJson, EarlyWarnInfoMapping.getOldAndNewPropertyMap(),
                                EarlyWarnInfoDto.class);
                if (earlyWarnInfoDto != null) {
                    deviceDto.setEarlyWarnInfoDto(earlyWarnInfoDto);
                }
            }
        }
        return deviceDto;
    }

    /**
     * 获得设备档案
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-28
     */
    @Override
    public DeviceDto findDeviceArchive(DeviceFilter deviceFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("deviceType", StrUtil.trimToEmpty(deviceFilter.getDeviceModel()));
        paramMap.add("deviceCode", StrUtil.trimToEmpty(deviceFilter.getSerial()));
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, findDeviceArchiveUrl);
        DeviceDto deviceDto = new DeviceDto();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String dataJson = resultObject.getString("jsonResult");
            if (StrUtil.isNotBlank(dataJson)) {
                deviceDto = VariableConvertUtil
                        .convertToNewEntity(dataJson, DeviceMapping.getOldAndNewPropertyMap(), DeviceDto.class);
            }
        }
        return deviceDto;
    }

    /**
     * 查询安装记录
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-28
     */
    @Override
    public ListWrapper<InstallRecordDto> listInstallRecord(DeviceFilter deviceFilter, UserInfo userInfo,
            ReqParam reqParam) {
        Page<InstallRecordDto> page = new Page(deviceFilter.getPageNum(), deviceFilter.getPageSize());
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("deviceType", StrUtil.trimToEmpty(deviceFilter.getDeviceModel()));
        paramMap.add("deviceCode", StrUtil.trimToEmpty(deviceFilter.getSerial()));
        paramMap.add("dto.currentPage", page.getCurrent());
        paramMap.add("dto.pageSize", page.getSize());

        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, queryInstallRecordUrl);

        Long totalCount = 0L;
        List<InstallRecordDto> installRecordDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObjectSort(handResult, JSONObject.class);
            String dtoData = resultObject.getString("dto");
            JSONObject dtoObject = JsonUtil.parseObject(dtoData, JSONObject.class);
            totalCount = dtoObject.getLong("totalCount");
            installRecordDtoList = VariableConvertUtil.convertToNewEntityList(dtoObject.getString("data"),
                    InstallRecordMapping.getOldAndNewPropertyMap(),
                    InstallRecordDto.class);
        }
        return ListWrapper.<InstallRecordDto>builder()
                .list(installRecordDtoList)
                .total(totalCount)
                .build();
    }

    /**
     * 查询升级记录
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-28
     */
    @Override
    public ListWrapper<UpdateRecordDto> listUpdateRecord(DeviceFilter deviceFilter, UserInfo userInfo,
            ReqParam reqParam) {
        Page page = new Page(deviceFilter.getPageNum(), deviceFilter.getPageSize());
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("deviceType", StrUtil.trimToEmpty(deviceFilter.getDeviceModel()));
        paramMap.add("deviceCode", StrUtil.trimToEmpty(deviceFilter.getSerial()));
        paramMap.add("dto.currentPage", page.getCurrent());
        paramMap.add("dto.pageSize", page.getSize());

        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, queryUpdateRecordUrl);

        Long totalCount = 0L;
        List<UpdateRecordDto> updateRecordDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObjectSort(handResult, JSONObject.class);
            String dtoData = resultObject.getString("dto");
            JSONObject dtoObject = JsonUtil.parseObject(dtoData, JSONObject.class);
            totalCount = dtoObject.getLong("totalCount");
            updateRecordDtoList = VariableConvertUtil.convertToNewEntityList(dtoObject.getString("data"),
                    UpdateRecordMapping.getOldAndNewPropertyMap(),
                    UpdateRecordDto.class);
        }
        return ListWrapper.<UpdateRecordDto>builder()
                .list(updateRecordDtoList)
                .total(totalCount)
                .build();
    }

    /**
     * 查询维护PM记录
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-28
     */
    @Override
    public ListWrapper<MaintainPmDto> listMaintainPm(DeviceFilter deviceFilter, UserInfo userInfo, ReqParam reqParam) {
        Page page = new Page(deviceFilter.getPageNum(), deviceFilter.getPageSize());
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("deviceType", StrUtil.trimToEmpty(deviceFilter.getDeviceModel()));
        paramMap.add("deviceCode", StrUtil.trimToEmpty(deviceFilter.getSerial()));
        paramMap.add("dto.currentPage", page.getCurrent());
        paramMap.add("dto.pageSize", page.getSize());

        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, queryMaintainPmUrl);

        Long totalCount = 0L;
        List<MaintainPmDto> maintainPmDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObjectSort(handResult, JSONObject.class);
            String dtoData = resultObject.getString("dto");
            JSONObject dtoObject = JsonUtil.parseObject(dtoData, JSONObject.class);
            totalCount = dtoObject.getLong("totalCount");
            maintainPmDtoList = VariableConvertUtil
                    .convertToNewEntityList(dtoObject.getString("data"), MaintainPmMapping.getOldAndNewPropertyMap(),
                            MaintainPmDto.class);
        }
        return ListWrapper.<MaintainPmDto>builder()
                .list(maintainPmDtoList)
                .total(totalCount)
                .build();
    }

    /**
     * 查看维护详情
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-28
     */
    @Override
    public MaintainDto viewMaintainDetail(DeviceFilter deviceFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("recordId", StrUtil.trimToEmpty(deviceFilter.getRecordId()));

        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, viewMaintainDetailUrl);

        MaintainDto maintainDto = null;
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String dataJson = resultObject.getString("jsonResult");
            if (StrUtil.isNotBlank(dataJson)) {
                maintainDto = VariableConvertUtil
                        .convertToNewEntity(dataJson, MaintainMapping.getOldAndNewPropertyMap(), MaintainDto.class);
            }
        }
        return maintainDto;
    }

    /**
     * 查看PM详情
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-28
     */
    @Override
    public PmDto viewPmDetail(DeviceFilter deviceFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("recordId", StrUtil.trimToEmpty(deviceFilter.getRecordId()));

        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, viewPmDetailUrl);

        PmDto pmDto = null;
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String dataJson = resultObject.getString("jsonResult");
            if (StrUtil.isNotBlank(dataJson)) {
                pmDto = VariableConvertUtil
                        .convertToNewEntity(dataJson, PmMapping.getOldAndNewPropertyMap(), PmDto.class);
            }
        }
        return pmDto;
    }

    /**
     * 查询维护PM更换备件记录
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-28
     */
    @Override
    public ListWrapper<PartDto> listPartReplace(DeviceFilter deviceFilter, UserInfo userInfo, ReqParam reqParam) {
        Page page = new Page(deviceFilter.getPageNum(), deviceFilter.getPageSize());
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("deviceType", StrUtil.trimToEmpty(deviceFilter.getDeviceModel()));
        paramMap.add("deviceCode", StrUtil.trimToEmpty(deviceFilter.getSerial()));
        paramMap.add("dto.currentPage", page.getCurrent());
        paramMap.add("dto.pageSize", page.getSize());

        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, queryPartReplaceUrl);

        Long totalCount = 0L;
        List<PartDto> partDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObjectSort(handResult, JSONObject.class);
            String dtoData = resultObject.getString("dto");
            JSONObject dtoObject = JsonUtil.parseObject(dtoData, JSONObject.class);
            totalCount = dtoObject.getLong("totalCount");
            partDtoList = VariableConvertUtil.convertToNewEntityList(dtoObject.getString("data"),
                    PartMapping.getOldAndNewPropertyMap(),
                    PartDto.class);
        }
        return ListWrapper.<PartDto>builder()
                .list(partDtoList)
                .total(totalCount)
                .build();
    }

    /**
     * 查找机器最近CASE情况
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-28
     */
    @Override
    public ListWrapper<CaseDto> listRecentCase(DeviceFilter deviceFilter, UserInfo userInfo, ReqParam reqParam) {
        Page page = new Page(deviceFilter.getPageNum(), deviceFilter.getPageSize());
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("type1", StrUtil.trimToEmpty(deviceFilter.getDeviceModel()));
        paramMap.add("deviceCode", StrUtil.trimToEmpty(deviceFilter.getSerial()));
        paramMap.add("dto.currentPage", page.getCurrent());
        paramMap.add("dto.pageSize", page.getSize());

        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, listRecentCaseUrl);

        Long totalCount = 0L;
        List<CaseDto> caseDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObjectSort(handResult, JSONObject.class);
            String dtoData = resultObject.getString("dto");
            JSONObject dtoObject = JsonUtil.parseObject(dtoData, JSONObject.class);
            totalCount = dtoObject.getLong("totalCount");
            caseDtoList = VariableConvertUtil.convertToNewEntityList(dtoObject.getString("data"),
                    RencentCaseMapping.getOldAndNewPropertyMap(), CaseDto.class);
        }
        return ListWrapper.<CaseDto>builder()
                .list(caseDtoList)
                .total(totalCount)
                .build();
    }

    /**
     * 查找机器最近更换备件情况
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-28
     */
    @Override
    public ListWrapper<PartReplaceDto> listRecentPartReplace(DeviceFilter deviceFilter, UserInfo userInfo,
            ReqParam reqParam) {
        Page page = new Page(deviceFilter.getPageNum(), deviceFilter.getPageSize());
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("type1", StrUtil.trimToEmpty(deviceFilter.getDeviceModel()));
        paramMap.add("deviceCode", StrUtil.trimToEmpty(deviceFilter.getSerial()));
        paramMap.add("dto.currentPage", page.getCurrent());
        paramMap.add("dto.pageSize", page.getSize());

        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, listRecentPartReplaceUrl);

        Long totalCount = 0L;
        List<PartReplaceDto> partReplaceDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObjectSort(handResult, JSONObject.class);
            String dtoData = resultObject.getString("dto");
            JSONObject dtoObject = JsonUtil.parseObject(dtoData, JSONObject.class);
            totalCount = dtoObject.getLong("totalCount");
            partReplaceDtoList = VariableConvertUtil
                    .convertToNewEntityList(dtoObject.getString("data"), new HashMap<>(), PartReplaceDto.class);
        }
        return ListWrapper.<PartReplaceDto>builder()
                .list(partReplaceDtoList)
                .total(totalCount)
                .build();
    }
}
