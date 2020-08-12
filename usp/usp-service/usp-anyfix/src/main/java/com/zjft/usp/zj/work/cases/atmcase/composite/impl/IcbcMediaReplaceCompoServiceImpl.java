package com.zjft.usp.zj.work.cases.atmcase.composite.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.common.constant.StatusCodeConstants;
import com.zjft.usp.zj.common.dto.WoResult;
import com.zjft.usp.zj.common.utils.SendWoUtil;
import com.zjft.usp.zj.common.utils.VariableConvertUtil;
import com.zjft.usp.zj.work.cases.atmcase.composite.IcbcMediaReplaceCompoService;
import com.zjft.usp.zj.work.cases.atmcase.dto.icbc.IcbcMediaDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.icbc.IcbcMediaListDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.OldCaseDto;
import com.zjft.usp.zj.work.cases.atmcase.filter.IcbcMediaAddFilter;
import com.zjft.usp.zj.work.cases.atmcase.filter.IcbcMediaCloseFilter;
import com.zjft.usp.zj.work.cases.atmcase.filter.IcbcMediaDeleteFilter;
import com.zjft.usp.zj.work.cases.atmcase.filter.IcbcMediaQueryFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 工行对接介质更换聚合实现类
 *
 * @author JFZOU
 * @version 1.0
 * @date 2020-03-17 16:44
 **/
@Slf4j
@Service
@RefreshScope
public class IcbcMediaReplaceCompoServiceImpl implements IcbcMediaReplaceCompoService {

    @Resource
    private SendWoUtil sendWoUtil;
    @Value("${wo.atmcase.icbc.listMediaReplaceUrl}")
    private String listMediaReplaceUrl;
    @Value("${wo.atmcase.icbc.addMediaReplaceUrl}")
    private String addMediaReplaceUrl;
    @Value("${wo.atmcase.icbc.addMediaReplaceSubmitUrl}")
    private String addMediaReplaceSubmitUrl;
    @Value("${wo.atmcase.icbc.modMediaReplaceUrl}")
    private String modMediaReplaceUrl;
    @Value("${wo.atmcase.icbc.modMediaReplaceSubmitUrl}")
    private String modMediaReplaceSubmitUrl;
    @Value("${wo.atmcase.icbc.delMediaReplaceSubmitUrl}")
    private String delMediaReplaceSubmitUrl;
    @Value("${wo.atmcase.icbc.checkMediaByCloseUrl}")
    private String checkMediaByCloseUrl;
    @Override
    public OldCaseDto addMediaReplace(UserInfo userInfo,
                                      ReqParam reqParam,
                                      IcbcMediaAddFilter icbcMediaAddFilter) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("bankModeFlag", "");
        paramMap.add("ycaseId", icbcMediaAddFilter != null ? icbcMediaAddFilter.getWorkCode() : "");
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, addMediaReplaceUrl);
        OldCaseDto oldCaseDto = new OldCaseDto();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);

            String oldCaseData = resultObject.getString("yjcase");
            oldCaseDto = JsonUtil.parseObject(oldCaseData, OldCaseDto.class);
        }
        return oldCaseDto;
    }

    @Override
    public int addMediaReplaceSubmit(UserInfo userInfo, ReqParam reqParam, IcbcMediaDto icbcMediaDto) {
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(icbcMediaDto);
        return this.sendWoUtil.postToWoAndReturnCode(userInfo, reqParam, paramMap, addMediaReplaceSubmitUrl);
    }

    @Override
    public IcbcMediaDto modMediaReplace(UserInfo userInfo, ReqParam reqParam, Long replaceId) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("replaceId", replaceId);
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, modMediaReplaceUrl);
        IcbcMediaDto icbcMediaDto = new IcbcMediaDto();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String caseMediaRemoteData = resultObject.getString("bxCaseMediaRemote");
            icbcMediaDto = JsonUtil.parseObject(caseMediaRemoteData, IcbcMediaDto.class);
        }
        return icbcMediaDto;
    }

    @Override
    public int modMediaReplaceSubmit(UserInfo userInfo, ReqParam reqParam, IcbcMediaDto icbcMediaDto) {
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(icbcMediaDto);
        return this.sendWoUtil.postToWoAndReturnCode(userInfo, reqParam, paramMap, modMediaReplaceSubmitUrl);
    }

    @Override
    public int delMediaReplaceSubmit(UserInfo userInfo, ReqParam reqParam, IcbcMediaDeleteFilter icbcMediaDeleteFilter) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("replaceId", icbcMediaDeleteFilter.getReplaceId());
        paramMap.add("bankModeFlag", "");
        return this.sendWoUtil.postToWoAndReturnCode(userInfo, reqParam, paramMap, delMediaReplaceSubmitUrl);
    }

    @Override
    public WoResult checkMediaByClose(UserInfo userInfo, ReqParam reqParam, IcbcMediaCloseFilter icbcMediaCloseFilter) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", StrUtil.trimToEmpty(icbcMediaCloseFilter.getWorkCode()));
        paramMap.add("bankModeFlag", StrUtil.trimToEmpty(icbcMediaCloseFilter.getBankModeFlag()));
        return this.sendWoUtil.postToWoAndWoResult(userInfo, reqParam, paramMap, checkMediaByCloseUrl);
    }

    @Override
    public IcbcMediaListDto listMediaReplace(UserInfo userInfo, ReqParam reqParam, IcbcMediaQueryFilter icbcMediaQueryFilter) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", icbcMediaQueryFilter.getWorkCode());
        paramMap.add("bankModeFlag", "");

        IcbcMediaListDto icbcMediaListDto = new IcbcMediaListDto();
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, listMediaReplaceUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String mediaReplaceRemoteListData = resultObject.getString("mediaReplaceRemoteList");
            List<IcbcMediaDto> icbcMediaDtoList = JsonUtil.parseArray(mediaReplaceRemoteListData, IcbcMediaDto.class);
            List<IcbcMediaDto> newIcbcMediaDtoList = icbcMediaDtoList.stream().filter(item -> item.getReplaceId() > 0).collect(Collectors.toList());

            icbcMediaListDto.setIcbcMediaDtoList(newIcbcMediaDtoList);
        }

        return icbcMediaListDto;
    }

}
