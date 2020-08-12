package com.zjft.usp.zj.work.cases.atmcase.composite.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.common.constant.StatusCodeConstants;
import com.zjft.usp.zj.common.utils.SendWoUtil;
import com.zjft.usp.zj.common.utils.VariableConvertUtil;
import com.zjft.usp.zj.work.cases.atmcase.composite.IcbcPartReplaceCompoService;
import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.OldCaseDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.icbc.IcbcModuleDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.icbc.IcbcPartReplaceDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.icbc.IcbcPartReplaceListDto;
import com.zjft.usp.zj.work.cases.atmcase.filter.IcbcPartReplaceFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 工行对接维修登记聚合实现类
 *
 * @author JFZOU
 * @version 1.0
 * @date 2020-03-31 19:00
 **/
@Slf4j
@Service
public class IcbcPartReplaceCompoServiceImpl implements IcbcPartReplaceCompoService {
    @Resource
    private SendWoUtil sendWoUtil;
    @Value("${wo.atmcase.icbc.listPartReplaceUrl}")
    private String listPartReplaceUrl;
    @Value("${wo.atmcase.icbc.addPartReplaceUrl}")
    private String addPartReplaceUrl;
    @Value("${wo.atmcase.icbc.addPartReplaceSubmitUrl}")
    private String addPartReplaceSubmitUrl;
    @Value("${wo.atmcase.icbc.modPartReplaceUrl}")
    private String modPartReplaceUrl;
    @Value("${wo.atmcase.icbc.modPartReplaceSubmitUrl}")
    private String modPartReplaceSubmitUrl;
    @Value("${wo.atmcase.icbc.delPartReplaceSubmitUrl}")
    private String delPartReplaceSubmitUrl;

    @Override
    public IcbcPartReplaceListDto listBy(String caseId, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", caseId);
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, listPartReplaceUrl);

        IcbcPartReplaceListDto icbcPartReplaceListDto = new IcbcPartReplaceListDto();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String caseData = resultObject.getString("yjcase");
            OldCaseDto oldCaseDto = JsonUtil.parseObject(caseData, OldCaseDto.class);
            icbcPartReplaceListDto.setOldCaseDto(oldCaseDto);

            String partReplaceRemoteListData = resultObject.getString("partReplaceRemoteList");
            List<IcbcPartReplaceDto> icbcPartReplaceDtoList = JsonUtil.parseArray(partReplaceRemoteListData, IcbcPartReplaceDto.class);
            icbcPartReplaceListDto.setIcbcPartReplaceDtoList(icbcPartReplaceDtoList);
        }
        return icbcPartReplaceListDto;
    }

    @Override
    public IcbcPartReplaceDto addIcbcReplace(IcbcPartReplaceFilter icbcPartReplaceFilter, UserInfo userInfo, ReqParam reqParam) {

        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", icbcPartReplaceFilter.getCaseId());
        paramMap.add("deviceType", icbcPartReplaceFilter.getDeviceType());
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, addPartReplaceUrl);
        IcbcPartReplaceDto icbcPartReplaceDto = new IcbcPartReplaceDto();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);

            String oldCaseData = resultObject.getString("yjcase");
            OldCaseDto oldCaseDto = JsonUtil.parseObject(oldCaseData, OldCaseDto.class);
            icbcPartReplaceDto.setOldCaseDto(oldCaseDto);

            String smallClass = resultObject.getString("smallClass");
            icbcPartReplaceDto.setSmallClass(smallClass);

            String moduleCodeAndName = resultObject.getString("mapModuleCodeAndName");
            Map<String, String> mapModuleCodeAndName = JsonUtil.parseMap(moduleCodeAndName);
            icbcPartReplaceDto.setMapModuleCodeAndName(mapModuleCodeAndName);
        }
        return icbcPartReplaceDto;
    }

    @Override
    public int addIcbcReplaceSubmit(UserInfo userInfo, ReqParam reqParam, IcbcPartReplaceDto icbcPartReplaceDto) {
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(icbcPartReplaceDto);
        return this.sendWoUtil.postToWoAndReturnCode(userInfo, reqParam, paramMap, addPartReplaceSubmitUrl);
    }

    @Override
    public IcbcPartReplaceDto modIcbcReplace(IcbcPartReplaceFilter icbcPartReplaceFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("replaceId", icbcPartReplaceFilter.getReplaceId());
        paramMap.add("ycaseId", icbcPartReplaceFilter.getCaseId());
        paramMap.add("deviceType", icbcPartReplaceFilter.getDeviceType());
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, modPartReplaceUrl);
        IcbcPartReplaceDto icbcPartReplaceDto = new IcbcPartReplaceDto();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);

            String partReplaceRemoteData = resultObject.getString("partReplaceRemote");
            icbcPartReplaceDto = JsonUtil.parseObject(partReplaceRemoteData, IcbcPartReplaceDto.class);

            String oldCaseData = resultObject.getString("yjcase");
            OldCaseDto oldCaseDto = JsonUtil.parseObject(oldCaseData, OldCaseDto.class);
            icbcPartReplaceDto.setOldCaseDto(oldCaseDto);

            String smallClass = resultObject.getString("smallClass");
            icbcPartReplaceDto.setSmallClass(smallClass);

            String moduleCodeAndName = resultObject.getString("mapModuleCodeAndName");
            Map<String, String> mapModuleCodeAndName = JsonUtil.parseMap(moduleCodeAndName);
            icbcPartReplaceDto.setMapModuleCodeAndName(mapModuleCodeAndName);

            List<IcbcModuleDto> icbcModuleDtoList = new ArrayList<>();
            for (String key : mapModuleCodeAndName.keySet()) {
                IcbcModuleDto icbcModuleDto = new IcbcModuleDto();
                icbcModuleDto.setCode(key);
                icbcModuleDto.setName(mapModuleCodeAndName.get(key));
                icbcModuleDtoList.add(icbcModuleDto);
            }
            icbcPartReplaceDto.setIcbcModuleDtoList(icbcModuleDtoList);
        }
        return icbcPartReplaceDto;
    }

    @Override
    public int modIcbcReplaceSubmit(IcbcPartReplaceDto icbcPartReplaceDto, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(icbcPartReplaceDto);
        return this.sendWoUtil.postToWoAndReturnCode(userInfo, reqParam, paramMap, modPartReplaceSubmitUrl);
    }

    @Override
    public int deleteIcbcReplaceSubmit(Long replaceId, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("replaceId", replaceId);
        return this.sendWoUtil.postToWoAndReturnCode(userInfo, reqParam, paramMap, delPartReplaceSubmitUrl);
    }
}
