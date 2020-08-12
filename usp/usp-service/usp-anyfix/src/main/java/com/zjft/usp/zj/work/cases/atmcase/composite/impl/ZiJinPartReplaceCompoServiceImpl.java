package com.zjft.usp.zj.work.cases.atmcase.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.common.constant.StatusCodeConstants;
import com.zjft.usp.zj.common.utils.SendWoUtil;
import com.zjft.usp.zj.common.utils.VariableConvertUtil;
import com.zjft.usp.zj.work.cases.atmcase.composite.ZiJinPartReplaceCompoService;
import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.CaseDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.partreplace.*;
import com.zjft.usp.zj.work.cases.atmcase.filter.PriStockPartFilter;
import com.zjft.usp.zj.work.cases.atmcase.filter.VendorPartFilter;
import com.zjft.usp.zj.work.cases.atmcase.mapping.atmcase.CaseMapping;
import com.zjft.usp.zj.work.cases.atmcase.mapping.partreplace.PartReplaceAddMapping;
import com.zjft.usp.zj.work.cases.atmcase.mapping.partreplace.PartReplaceModMapping;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import java.util.*;

/**
 * ATM机CASE备件更换聚合实现类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-13 16:44
 **/
@Service
public class ZiJinPartReplaceCompoServiceImpl implements ZiJinPartReplaceCompoService {
    @Resource
    private SendWoUtil sendWoUtil;
    @Value("${wo.atmcase.listPartReplaceUrl}")
    private String listPartReplaceUrl;
    @Value("${wo.atmcase.addPartReplaceUrl}")
    private String addPartReplaceUrl;
    @Value("${wo.atmcase.addPartReplaceSubmitUrl}")
    private String addPartReplaceSubmitUrl;
    @Value("${wo.atmcase.checkPartReplaceExistUrl}")
    private String checkPartReplaceExistUrl;
    @Value("${wo.atmcase.modPartReplaceUrl}")
    private String modPartReplaceUrl;
    @Value("${wo.atmcase.modPartReplaceSubmitUrl}")
    private String modPartReplaceSubmitUrl;
    @Value("${wo.atmcase.delMoPartReplaceUrl}")
    private String delMoPartReplaceUrl;
    @Value("${wo.atmcase.listPriStockPartUrl}")
    private String listPriStockPartUrl;
    @Value("${wo.atmcase.listUpGhPartUrl}")
    private String listUpGhPartUrl;
    @Value("${wo.atmcase.isVendorPartUrl}")
    private String isVendorPartUrl;
    @Value("${wo.atmcase.mapCarNoByBureauIdUrl}")
    private String mapCarNoByBureauIdUrl;
    @Value("${wo.atmcase.findPartStatusUrl}")
    private String findPartStatusUrl;
    @Value("${wo.atmcase.checkURUrl}")
    private String checkURUrl;
    @Value("${wo.atmcase.checkPartIdUrl}")
    private String checkPartIdUrl;
    @Value("${wo.atmcase.mandatoryUseVendorPartUrl}")
    private String mandatoryUseVendorPartUrl;

    /**
     * 备件更换列表
     *
     * @param workCode
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/31 14:17
     */
    @Override
    public PartReplaceListDto listPartReplace(String workCode, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", StrUtil.trimToEmpty(workCode));
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, listPartReplaceUrl);
        PartReplaceListDto partReplaceListDto = new PartReplaceListDto();
        List<PartReplaceDto> partReplaceDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String replaceTypeJson = resultObject.getString("replaceTypeMap");
            Map<String, String> replaceTypeMap = new HashMap<>();
            if (StrUtil.isNotBlank(replaceTypeJson)) {
                replaceTypeMap = JsonUtil.parseMap(replaceTypeJson);
                replaceTypeMap = replaceTypeMap == null ? new HashMap<>(0) : replaceTypeMap;
            }
            String replaceSourceJson = resultObject.getString("replaceSourceMap");
            Map<String, String> replaceSourceMap = new HashMap<>();
            if (StrUtil.isNotBlank(replaceSourceJson)) {
                replaceSourceMap = JsonUtil.parseMap(replaceSourceJson);
                replaceSourceMap = replaceSourceMap == null ? new HashMap<>(0) : replaceSourceMap;
            }
            String replaceStatusJson = resultObject.getString("replaceStatusMap");
            Map<String, String> replaceStatusMap = new HashMap<>();
            if (StrUtil.isNotBlank(replaceStatusJson)) {
                replaceStatusMap = JsonUtil.parseMap(replaceStatusJson);
                replaceStatusMap = replaceStatusMap == null ? new HashMap<>(0) : replaceStatusMap;
            }
            String dataJson = resultObject.getString("partReplaceList");
            if (StrUtil.isNotBlank(dataJson)) {
                partReplaceDtoList = JsonUtil.parseArray(dataJson, PartReplaceDto.class);
            }
            if (CollectionUtil.isNotEmpty(partReplaceDtoList)) {
                for (PartReplaceDto partReplaceDto : partReplaceDtoList) {
                    partReplaceDto.setReplaceTypeName(StrUtil.trimToEmpty(replaceTypeMap
                            .get(StrUtil.toString(partReplaceDto.getReplaceType()))));
                    partReplaceDto.setUseSourceName(StrUtil.trimToEmpty(replaceSourceMap
                            .get(StrUtil.toString(partReplaceDto.getUseSource()))));
                    partReplaceDto.setStatusName(StrUtil.trimToEmpty(replaceStatusMap
                            .get(StrUtil.toString(partReplaceDto.getStatus()))));
                }
                partReplaceListDto.setPartReplaceList(partReplaceDtoList);
            }
            String caseJson = resultObject.getString("yjcase");
            if (StrUtil.isNotBlank(caseJson)) {
                CaseDto caseDto = VariableConvertUtil
                        .convertToNewEntity(caseJson, CaseMapping.getOldAndNewPropertyMap(), CaseDto.class);
                partReplaceListDto.setCaseDto(caseDto);
            }
        }
        return partReplaceListDto;
    }

    /**
     * 添加备件更换页面初始化
     *
     * @param workCode
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-17
     */
    @Override
    public PartReplaceAddPageDto addPartReplace(String workCode, UserInfo userInfo, ReqParam reqParam) {
        PartReplaceAddPageDto partReplaceAddPageDto = new PartReplaceAddPageDto();
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", StrUtil.trimToEmpty(workCode));
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, addPartReplaceUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObjectSort(handResult, JSONObject.class);
            String currentUserId = resultObject.getString("currentUserId");
            partReplaceAddPageDto.setUserId(currentUserId);
            String replaceTypeJson = resultObject.getString("replaceTypeMap");
            if (StrUtil.isNotBlank(replaceTypeJson)) {
                Map<String, String> map = JsonUtil.parseMapSort(replaceTypeJson);
                String json = this.changeToJsonString(map);
                partReplaceAddPageDto.setReplaceTypeList(JsonUtil.parseArray(json, ReplaceTypeDto.class));
            }
            String caseJson = resultObject.getString("yjcase");
            if (StrUtil.isNotBlank(caseJson)) {
                CaseDto caseDto = VariableConvertUtil
                        .convertToNewEntity(caseJson, CaseMapping.getOldAndNewPropertyMap(), CaseDto.class);
                partReplaceAddPageDto.setCaseDto(caseDto);
            }
            String machineCodeListJson = resultObject.getString("machineCodelist");
            if (StrUtil.isNotBlank(machineCodeListJson)) {
                List<String> serialList = JsonUtil.parseArray(machineCodeListJson, String.class);
                partReplaceAddPageDto.setSerialList(serialList);
            }
            String moduleCodeAndTypeMapJson = resultObject.getString("moduleCodeAndTypeMap");
            if (StrUtil.isNotBlank(moduleCodeAndTypeMapJson)) {
                Map<String, String> map = JsonUtil.parseMapSort(moduleCodeAndTypeMapJson);
                String json = this.changeToPartJsonString(map);
                partReplaceAddPageDto.setPartTypeList(JsonUtil.parseArray(json, PartTypeDto.class));
            }
            String mapCodeAndIfUseBarcodeJson = resultObject.getString("mapCodeAndIfUseBarcode");
            if (StrUtil.isNotBlank(mapCodeAndIfUseBarcodeJson)) {
                Map<String, String> map = JsonUtil.parseMapSort(mapCodeAndIfUseBarcodeJson);
                String json = this.changeToPartJsonString(map);
                partReplaceAddPageDto.setPartUseBarCodeList(JsonUtil.parseArray(json, PartUseBarCodeDto.class));
            }
            String zcodeAndNameJson = resultObject.getString("zcodeAndNameMap");
            if (StrUtil.isNotBlank(zcodeAndNameJson)) {
                Map<String, String> map = JsonUtil.parseMapSort(zcodeAndNameJson);
                String json = this.changeToJsonString(map);
                partReplaceAddPageDto.setPartInfoList(JsonUtil.parseArray(json, PartInfoDto.class));
            }
            String currentTime = resultObject.getString("currentTime");
            partReplaceAddPageDto.setCurrentTime(StrUtil.trimToEmpty(currentTime));
            String depotAndNameListJson = resultObject.getString("depotAndNameList");
            if (StrUtil.isNotBlank(depotAndNameListJson)) {
                partReplaceAddPageDto.setDepotInfoList(JsonUtil.parseArray(depotAndNameListJson, DepotInfoDto.class));
            }
            String replaceSourceJson = resultObject.getString("replaceSourceMap");
            if (StrUtil.isNotBlank(replaceSourceJson)) {
                Map<String, String> map = JsonUtil.parseMapSort(replaceSourceJson);
                String json = this.changeToJsonString(map);
                partReplaceAddPageDto.setReplaceSourceList(JsonUtil.parseArray(json, ReplaceSourceDto.class));
            }
            String replaceStatusJson = resultObject.getString("replaceStatusMap");
            if (StrUtil.isNotBlank(replaceStatusJson)) {
                Map<String, String> map = JsonUtil.parseMapSort(replaceStatusJson);
                String json = this.changeToJsonString(map);
                partReplaceAddPageDto.setPartStatusList(JsonUtil.parseArray(json, PartStatusDto.class));
            }
            String bar1codeReform = resultObject.getString("bar1codeReform");
            partReplaceAddPageDto.setBar1codeReform(StrUtil.trimToEmpty(bar1codeReform));
            String enablePriStockPart = resultObject.getString("enablePriStockPart");
            partReplaceAddPageDto.setEnablePriStockPart(StrUtil.trimToEmpty(enablePriStockPart));
            String enablePriStockQuery = resultObject.getString("enablePriStockQuery");
            partReplaceAddPageDto.setEnablePriStockQuery(enablePriStockQuery);
            String partStoreStatusJson = resultObject.getString("partStoreStatusMap");
            if (StrUtil.isNotBlank(partStoreStatusJson)) {
                Map<String, String> map = JsonUtil.parseMapSort(partStoreStatusJson);
                String json = this.changeToJsonString(map);
                partReplaceAddPageDto.setPartStoreStatusList(JsonUtil.parseArray(json, PartStoreStatusDto.class));
            }
            String caseEngineerJson = resultObject.getString("caseEngineerMap");
            if (StrUtil.isNotBlank(caseEngineerJson)) {
                Map<String, String> map = JsonUtil.parseMapSort(caseEngineerJson);
                String json = this.changeToJsonString(map);
                partReplaceAddPageDto.setEngineerList(JsonUtil.parseArray(json, EngineerDto.class));
            }

        }
        return partReplaceAddPageDto;
    }

    /**
     * 添加备件更换提交
     *
     * @param partReplaceAddDto 备件更换对象
     * @param userInfo          当前用户
     * @param reqParam          公共参数
     * @return 空
     * @author zgpi
     * @date 2020/4/4 15:13
     */
    @Override
    public JSONObject addPartReplaceSubmit(PartReplaceAddDto partReplaceAddDto, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(partReplaceAddDto,
                PartReplaceAddMapping.getNewAndOldPropertyMap());
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap,
                addPartReplaceSubmitUrl);
        JSONObject jsonObject = new JSONObject();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            Integer returnCode = resultObject.getInteger("returnCode");
            String message = resultObject.getString("message");
            jsonObject.put("returnCode", returnCode);
            jsonObject.put("message", StrUtil.trimToEmpty(message));
        }
        return jsonObject;
    }

    /**
     * 检查备件更换是否存在
     *
     * @param partReplaceId 更换ID
     * @param userInfo      当前用户
     * @param reqParam      公共参数
     * @return 空
     * @author zgpi
     * @date 2020/4/5 16:07
     */
    @Override
    public JSONObject checkPartReplaceExist(String partReplaceId, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("partReplaceId", StrUtil.trimToEmpty(partReplaceId));
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap,
                checkPartReplaceExistUrl);
        JSONObject jsonObject = new JSONObject();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            Integer returnCode = resultObject.getInteger("returnCode");
            String message = resultObject.getString("message");
            jsonObject.put("returnCode", returnCode);
            jsonObject.put("message", StrUtil.trimToEmpty(message));
        }
        return jsonObject;
    }

    /**
     * 进入修改备件更换页面
     *
     * @param partReplaceId 更换ID
     * @param workCode      CASE号
     * @param userInfo      当前用户
     * @param reqParam      公共参数
     * @return 备件更换页面dto
     * @author zgpi
     * @date 2020/4/5 16:23
     */
    @Override
    public PartReplaceModPageDto modPartReplace(String partReplaceId, String workCode,
                                                UserInfo userInfo, ReqParam reqParam) {
        PartReplaceModPageDto partReplaceModPageDto = new PartReplaceModPageDto();
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", StrUtil.trimToEmpty(workCode));
        paramMap.add("partReplaceId", StrUtil.trimToEmpty(partReplaceId));
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, modPartReplaceUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObjectSort(handResult, JSONObject.class);
            String moPartReplaceJson = resultObject.getString("moPartReplace");
            if (StrUtil.isNotBlank(moPartReplaceJson)) {
                PartReplaceModDto partReplaceModDto = VariableConvertUtil
                        .convertToNewEntity(moPartReplaceJson, PartReplaceModMapping.getOldAndNewPropertyMap(),
                                PartReplaceModDto.class);
                partReplaceModPageDto.setPartReplaceModDto(partReplaceModDto);
            }
            String currentUserId = resultObject.getString("currentUserId");
            partReplaceModPageDto.setUserId(currentUserId);
            String replaceTypeJson = resultObject.getString("replaceTypeMap");
            if (StrUtil.isNotBlank(replaceTypeJson)) {
                Map<String, String> map = JsonUtil.parseMapSort(replaceTypeJson);
                String json = this.changeToJsonString(map);
                partReplaceModPageDto.setReplaceTypeList(JsonUtil.parseArray(json, ReplaceTypeDto.class));
            }
            String caseJson = resultObject.getString("yjcase");
            if (StrUtil.isNotBlank(caseJson)) {
                CaseDto caseDto = VariableConvertUtil
                        .convertToNewEntity(caseJson, CaseMapping.getOldAndNewPropertyMap(), CaseDto.class);
                partReplaceModPageDto.setCaseDto(caseDto);
            }
            String machineCodeListJson = resultObject.getString("machineCodelist");
            if (StrUtil.isNotBlank(machineCodeListJson)) {
                List<String> serialList = JsonUtil.parseArray(machineCodeListJson, String.class);
                partReplaceModPageDto.setSerialList(serialList);
            }
            String moduleCodeAndTypeMapJson = resultObject.getString("moduleCodeAndTypeMap");
            if (StrUtil.isNotBlank(moduleCodeAndTypeMapJson)) {
                Map<String, String> map = JsonUtil.parseMapSort(moduleCodeAndTypeMapJson);
                String json = this.changeToPartJsonString(map);
                partReplaceModPageDto.setPartTypeList(JsonUtil.parseArray(json, PartTypeDto.class));
            }
            String mapCodeAndIfUseBarcodeJson = resultObject.getString("mapCodeAndIfUseBarcode");
            if (StrUtil.isNotBlank(mapCodeAndIfUseBarcodeJson)) {
                Map<String, String> map = JsonUtil.parseMapSort(mapCodeAndIfUseBarcodeJson);
                String json = this.changeToPartJsonString(map);
                partReplaceModPageDto.setPartUseBarCodeList(JsonUtil.parseArray(json, PartUseBarCodeDto.class));
            }
            String zcodeAndNameJson = resultObject.getString("zcodeAndNameMap");
            if (StrUtil.isNotBlank(zcodeAndNameJson)) {
                Map<String, String> map = JsonUtil.parseMapSort(zcodeAndNameJson);
                String json = this.changeToJsonString(map);
                partReplaceModPageDto.setPartInfoList(JsonUtil.parseArray(json, PartInfoDto.class));
            }
            String currentTime = resultObject.getString("currentTime");
            partReplaceModPageDto.setCurrentTime(StrUtil.trimToEmpty(currentTime));
            String depotAndNameListJson = resultObject.getString("depotAndNameList");
            if (StrUtil.isNotBlank(depotAndNameListJson)) {
                partReplaceModPageDto.setDepotInfoList(JsonUtil.parseArray(depotAndNameListJson, DepotInfoDto.class));
            }
            String replaceSourceJson = resultObject.getString("replaceSourceMap");
            if (StrUtil.isNotBlank(replaceSourceJson)) {
                Map<String, String> map = JsonUtil.parseMapSort(replaceSourceJson);
                String json = this.changeToJsonString(map);
                partReplaceModPageDto.setReplaceSourceList(JsonUtil.parseArray(json, ReplaceSourceDto.class));
            }
            String replaceStatusJson = resultObject.getString("replaceStatusMap");
            if (StrUtil.isNotBlank(replaceStatusJson)) {
                Map<String, String> map = JsonUtil.parseMapSort(replaceStatusJson);
                String json = this.changeToJsonString(map);
                partReplaceModPageDto.setPartStatusList(JsonUtil.parseArray(json, PartStatusDto.class));
            }
            String bar1codeReform = resultObject.getString("bar1codeReform");
            partReplaceModPageDto.setBar1codeReform(StrUtil.trimToEmpty(bar1codeReform));
            String enablePriStockPart = resultObject.getString("enablePriStockPart");
            partReplaceModPageDto.setEnablePriStockPart(StrUtil.trimToEmpty(enablePriStockPart));
            String enablePriStockQuery = resultObject.getString("enablePriStockQuery");
            partReplaceModPageDto.setEnablePriStockQuery(enablePriStockQuery);
            String partStoreStatusJson = resultObject.getString("partStoreStatusMap");
            if (StrUtil.isNotBlank(partStoreStatusJson)) {
                Map<String, String> map = JsonUtil.parseMapSort(partStoreStatusJson);
                String json = this.changeToJsonString(map);
                partReplaceModPageDto.setPartStoreStatusList(JsonUtil.parseArray(json, PartStoreStatusDto.class));
            }
            String caseEngineerJson = resultObject.getString("caseEngineerMap");
            if (StrUtil.isNotBlank(caseEngineerJson)) {
                Map<String, String> map = JsonUtil.parseMapSort(caseEngineerJson);
                String json = this.changeToJsonString(map);
                partReplaceModPageDto.setEngineerList(JsonUtil.parseArray(json, EngineerDto.class));
            }

        }
        return partReplaceModPageDto;
    }

    /**
     * 修改备件更换提交
     *
     * @param partReplaceModDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/4/6 10:29
     */
    @Override
    public JSONObject modPartReplaceSubmit(PartReplaceModDto partReplaceModDto, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(partReplaceModDto,
                PartReplaceModMapping.getNewAndOldPropertyMap());
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap,
                modPartReplaceSubmitUrl);
        JSONObject jsonObject = new JSONObject();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            Integer returnCode = resultObject.getInteger("returnCode");
            String message = resultObject.getString("message");
            jsonObject.put("returnCode", returnCode);
            jsonObject.put("message", StrUtil.trimToEmpty(message));
        }
        return jsonObject;
    }

    /**
     * 删除备件更换
     *
     * @param id       更换ID
     * @param userInfo 当前用户
     * @param reqParam 公共参数
     * @return 空
     * @author zgpi
     * @date 2020/4/5 14:16
     */
    @Override
    public JSONObject delPartReplace(String id, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("partReplaceId", StrUtil.trimToEmpty(id));
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap,
                delMoPartReplaceUrl);
        JSONObject jsonObject = new JSONObject();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            Integer returnCode = resultObject.getInteger("returnCode");
            String message = resultObject.getString("message");
            jsonObject.put("returnCode", returnCode);
            jsonObject.put("message", StrUtil.trimToEmpty(message));
        }
        return jsonObject;
    }

    /**
     * 获得个人备件列表
     *
     * @param priStockPartFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/4/2 19:48
     */
    @Override
    public List<PriStockPartDto> listPriStockPart(PriStockPartFilter priStockPartFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("zcode", StrUtil.trimToEmpty(priStockPartFilter.getPartCode()));
        paramMap.add("caseDepot", StrUtil.trimToEmpty(priStockPartFilter.getServiceBranch()));
        paramMap.add("engineerId", StrUtil.trimToEmpty(priStockPartFilter.getEngineerId()));
        paramMap.add("storeStatus", priStockPartFilter.getStoreStatus());
        paramMap.add("carNo", StrUtil.trimToEmpty(priStockPartFilter.getCarNo()));

        String handleResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, listPriStockPartUrl);
        List<PriStockPartDto> priStockPartDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handleResult) && handleResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            String priStockPartMapJson = resultObject.getString("priStockPartMap");
            if (StrUtil.isNotBlank(priStockPartMapJson)) {
                Map<String, String> map = JsonUtil.parseMapSort(priStockPartMapJson);
                String json = this.changeToJsonString(map);
                priStockPartDtoList = JsonUtil.parseArray(json, PriStockPartDto.class);
            }
        }
        return priStockPartDtoList;
    }

    /**
     * 获得CASE的换上备件列表
     *
     * @param partCode
     * @param workCode
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/4/2 20:22
     */
    @Override
    public List<PartReplaceDto> listUpGhPart(String partCode, String workCode, UserInfo userInfo, ReqParam reqParam) {
        List<PartReplaceDto> partReplaceDtoList = new ArrayList<>();
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("zcode", StrUtil.trimToEmpty(partCode));
        paramMap.add("ycaseId", StrUtil.trimToEmpty(workCode));
        String handleResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, listUpGhPartUrl);
        if (StrUtil.isNotEmpty(handleResult) && handleResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            String moPartReplaceListJson = resultObject.getString("moPartReplaceList");
            partReplaceDtoList = JsonUtil.parseArray(moPartReplaceListJson, PartReplaceDto.class);
        }
        return partReplaceDtoList;
    }

    /**
     * 判断是否厂商备件
     *
     * @param vendorPartFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/4/3 15:10
     */
    @Override
    public VendorPartDto isVendorPart(VendorPartFilter vendorPartFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("moPartReplace.zcode", StrUtil.trimToEmpty(vendorPartFilter.getPartCode()));
        paramMap.add("moPartReplace.newPartId", StrUtil.trimToEmpty(vendorPartFilter.getNewPartId()));
        paramMap.add("moPartReplace.newBarcode", StrUtil.trimToEmpty(vendorPartFilter.getNewBarCode()));
        String handleResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, isVendorPartUrl);
        VendorPartDto vendorPartDto = null;
        if (StrUtil.isNotEmpty(handleResult) && handleResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            String vendorPartJson = resultObject.getString("vendorPart");
            vendorPartDto = JsonUtil.parseObject(vendorPartJson, VendorPartDto.class);
        }
        return vendorPartDto;
    }

    /**
     * 获得车牌号列表
     *
     * @param serviceBranch
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/4/3 15:54
     */
    @Override
    public List<String> listCarNo(String serviceBranch, UserInfo userInfo, ReqParam reqParam) {
        List<String> carNoList = new ArrayList<>();
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("bureauId", StrUtil.trimToEmpty(serviceBranch));
        String handleResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, mapCarNoByBureauIdUrl);
        if (StrUtil.isNotEmpty(handleResult) && handleResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            String carNoMapJson = resultObject.getString("carNoMap");
            Map<String, String> map = JsonUtil.parseMapSort(carNoMapJson);
            for (Iterator<Map.Entry<String, String>> it = map.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, String> itt = it.next();
                carNoList.add(itt.getKey());
            }
        }
        return carNoList;
    }

    /**
     * 获得备件状态
     *
     * @param partCode
     * @param newPartId
     * @param newBarCode
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/4/3 16:20
     */
    @Override
    public Integer findPartStatus(String partCode, String newPartId, String newBarCode,
                                  UserInfo userInfo, ReqParam reqParam) {
        Integer status = 0;
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("zcode", StrUtil.trimToEmpty(partCode));
        paramMap.add("newPartId", StrUtil.trimToEmpty(newPartId));
        paramMap.add("newBarcode", StrUtil.trimToEmpty(newBarCode));
        String handleResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, findPartStatusUrl);
        if (StrUtil.isNotEmpty(handleResult) && handleResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            status = resultObject.getInteger("status");
        }
        return status;
    }

    /**
     * 检查UR
     *
     * @param partCode
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/4/4 14:22
     */
    @Override
    public Integer checkUR(String partCode, UserInfo userInfo, ReqParam reqParam) {
        Integer returnCode = 0;
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("zcode", StrUtil.trimToEmpty(partCode));
        String handleResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, checkURUrl);
        if (StrUtil.isNotEmpty(handleResult) && handleResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            returnCode = resultObject.getInteger("returnCode");
        }
        return returnCode;
    }

    /**
     * 检查换上的专用备件是否已经被使用
     *
     * @param workCode
     * @param partCode
     * @param newPartId
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/4/4 14:37
     */
    @Override
    public JSONObject checkPartId(String workCode, String partCode, String newPartId, UserInfo userInfo, ReqParam reqParam) {
        JSONObject jsonObject = new JSONObject();
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", StrUtil.trimToEmpty(workCode));
        paramMap.add("zcode", StrUtil.trimToEmpty(partCode));
        paramMap.add("newPartId", StrUtil.trimToEmpty(newPartId));
        String handleResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, checkPartIdUrl);
        if (StrUtil.isNotEmpty(handleResult) && handleResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            Integer returnCode = resultObject.getInteger("returnCode");
            String message = resultObject.getString("message");
            jsonObject.put("returnCode", returnCode);
            jsonObject.put("message", message);
        }
        return jsonObject;
    }

    /**
     * 是否强制使用厂商备件
     *
     * @param partReplaceDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/4/4 15:01
     */
    @Override
    public String mandatoryUseVendorPart(PartReplaceDto partReplaceDto, UserInfo userInfo, ReqParam reqParam) {
        String flag = "";
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("zcode", StrUtil.trimToEmpty(partReplaceDto.getZcode()));
        paramMap.add("statusVendor", partReplaceDto.getStatus());
        paramMap.add("quantity", partReplaceDto.getQuantity());
        paramMap.add("upDepot", StrUtil.trimToEmpty(partReplaceDto.getUpDepot()));
        paramMap.add("type1", StrUtil.trimToEmpty(partReplaceDto.getMachineType()));
        paramMap.add("code", StrUtil.trimToEmpty(partReplaceDto.getMachineCode()));
        String handleResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, mandatoryUseVendorPartUrl);
        if (StrUtil.isNotEmpty(handleResult) && handleResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            flag = resultObject.getString("ifUseVendorPart");
        }
        return StrUtil.trimToEmpty(flag);
    }

    /**
     * 转换成json
     *
     * @param map
     * @return
     * @author zgpi
     * @date 2020/4/1 21:12
     */
    private String changeToJsonString(Map<String, String> map) {
        JSONObject jsonObject;
        if (CollectionUtil.isEmpty(map)) {
            return "";
        }
        List<JSONObject> list = new ArrayList<>();
        for (Iterator<Map.Entry<String, String>> it = map.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, String> itt = it.next();
            jsonObject = new JSONObject();
            jsonObject.put("code", itt.getKey());
            jsonObject.put("name", itt.getValue());
            list.add(jsonObject);
        }
        return JsonUtil.toJson(list);
    }


    /**
     * 转换成json
     *
     * @param map
     * @return
     * @author zgpi
     * @date 2020/4/2 14:56
     */
    private String changeToPartJsonString(Map<String, String> map) {
        JSONObject jsonObject;
        if (CollectionUtil.isEmpty(map)) {
            return "";
        }
        List<JSONObject> list = new ArrayList<>();
        for (Iterator<Map.Entry<String, String>> it = map.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, String> itt = it.next();
            jsonObject = new JSONObject();
            jsonObject.put("partCode", itt.getKey());
            jsonObject.put("value", itt.getValue());
            list.add(jsonObject);
        }
        return JsonUtil.toJson(list);
    }
}
