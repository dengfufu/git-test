package com.zjft.usp.zj.work.cases.atmcase.composite.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.common.constant.ImageConstants;
import com.zjft.usp.zj.common.constant.StatusCodeConstants;
import com.zjft.usp.zj.common.dto.WoResult;
import com.zjft.usp.zj.common.utils.ImageUtil;
import com.zjft.usp.zj.common.utils.SendWoUtil;
import com.zjft.usp.zj.common.utils.VariableConvertUtil;
import com.zjft.usp.zj.work.cases.atmcase.composite.AtmCaseCompoService;
import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.*;
import com.zjft.usp.zj.work.cases.atmcase.dto.file.FileInfoDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.file.ImageDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.file.ImageJsonToDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.partreplace.DepotInfoDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.partreplace.PartReplaceDto;
import com.zjft.usp.zj.work.cases.atmcase.enums.CaseImageTypeEnum;
import com.zjft.usp.zj.work.cases.atmcase.filter.AtmCaseFilter;
import com.zjft.usp.zj.work.cases.atmcase.mapping.atmcase.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * ATM机CASE聚合实现类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-13 16:44
 **/
@Slf4j
@Service
@RefreshScope
public class AtmCaseCompoServiceImpl implements AtmCaseCompoService {
    @Resource
    private ImageUtil imageUtil;
    @Resource
    private SendWoUtil sendWoUtil;
    @Value("${wo.atmcase.findCaseUrl}")
    private String findCaseUrl;
    @Value("${wo.atmcase.addAtmCaseSubmitUrl}")
    private String addAtmCaseSubmitUrl;
    @Value("${wo.atmcase.updateAtmCaseArriveTimeUrl}")
    private String updateAtmCaseArriveTimeUrl;
    @Value("${wo.atmcase.updateAtmCaseArriveTimeSubmitUrl}")
    private String updateAtmCaseArriveTimeSubmitUrl;
    @Value("${wo.atmcase.uploadFaceIdentImageSubmitUrl}")
    private String uploadFaceIdentImageSubmitUrl;
    @Value("${wo.atmcase.caseImageDBDataUrl}")
    private String caseImageDBDataUrl;
    @Value("${wo.atmcase.showOmeCaseImageUrl}")
    private String showOmeCaseImageUrl;
    @Value("${wo.atmcase.deleteOmeImageSubmitUrl}")
    private String deleteOmeImageSubmitUrl;
    @Value("${wo.atmcase.modAtmCaseSubmitUrl}")
    private String modAtmCaseSubmitUrl;
    @Value("${wo.atmcase.cancelAtmCaseUrl}")
    private String cancelAtmCaseUrl;
    @Value("${wo.atmcase.cancelAtmCaseSubmitUrl}")
    private String cancelAtmCaseSubmitUrl;
    @Value("${wo.atmcase.delayAtmCaseUrl}")
    private String delayAtmCaseUrl;
    @Value("${wo.atmcase.delayAtmCaseSubmitUrl}")
    private String delayAtmCaseSubmitUrl;
    @Value("${wo.atmcase.calcCompletionTimeUrl}")
    private String calcCompletionTimeUrl;
    @Value("${wo.atmcase.finishAtmCaseUrl}")
    private String finishAtmCaseUrl;
    @Value("${wo.atmcase.finishAtmCaseSubmitUrl}")
    private String finishAtmCaseSubmitUrl;
    @Value("${wo.atmcase.concertAtmCaseSubmitUrl}")
    private String concertAtmCaseSubmitUrl;
    @Value("${wo.atmcase.isExistSameMachineCaseUrl}")
    private String isExistSameMachineCaseUrl;
    @Value("${wo.atmcase.checkCaseStatusUrl}")
    private String checkCaseStatusUrl;
    @Value("${wo.atmcase.checkFaceSignForCloseCaseUrl}")
    private String checkFaceSignForCloseCaseUrl;
    @Value("${wo.atmcase.checkEscortSignInForCloseCaseUrl}")
    private String checkEscortSignInForCloseCaseUrl;
    @Value("${wo.atmcase.listPicByCaseIdUrl}")
    private String listPicByCaseIdUrl;
    @Value("${wo.atmcase.findAppTypeAndSerTypeUrl}")
    private String findAppTypeAndSerTypeUrl;
    @Value("${wo.atmcase.listCaseByUspUrl}")
    private String listCaseByUspUrl;
    @Value("${wo.atmcase.listDepotByEngineerUrl}")
    private String listDepotByEngineerUrl;
    @Value("${wo.atmcase.closeCaseOrtherCheckUrl}")
    private String closeCaseOrtherCheckUrl;
    @Value("${wo.atmcase.checkPositionUrl}")
    private String checkPositionUrl;
    @Value("${wo.atmcase.checkNeedUploadPicUrl}")
    private String checkNeedUploadPicUrl;
    @Value("${wo.atmcase.uploadPhotoUrl}")
    private String uploadPhotoUrl;
    @Value("${wo.atmcase.delPhotoUrl}")
    private String delPhotoUrl;
    @Value("${wo.atmcase.viewPhotoUrl}")
    private String viewPhotoUrl;
    @Value("${wo.atmcase.uploadReceiptPhotoUrl}")
    private String uploadReceiptPhotoUrl;
    @Value("${wo.atmcase.delReceiptPhotoUrl}")
    private String delReceiptPhotoUrl;
    @Value("${wo.atmcase.addYjcaseFromWorkOrderInitUrl}")
    private String addYjcaseFromWorkOrderInitUrl;
    @Value("${wo.atmcase.dealMonitorCaseUrl}")
    private String dealMonitorCaseUrl;
    @Value("${wo.atmcase.checkYjcaseUrl}")
    private String checkYjcaseUrl;
    @Value("${wo.atmcase.listCloseNoAuditAtmCaseUrl}")
    private String listCloseNoAuditAtmCaseUrl;
    @Value("${wo.atmcase.uploadLocationUrl}")
    private String uploadLocationUrl;

    /**
     * 是否存在相同CASE类型开启状态的CASE
     *
     * @param atmCaseFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/28 11:15
     */
    @Override
    public CaseCheckDto ifExistSameMachineCase(AtmCaseFilter atmCaseFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("yjcase.ycasetype", atmCaseFilter.getWorkType());
        paramMap.add("yjcase.type1", atmCaseFilter.getDeviceModel());
        paramMap.add("yjcase.code", atmCaseFilter.getSerial());
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, isExistSameMachineCaseUrl);
        CaseCheckDto caseCheckDto = new CaseCheckDto();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String message = resultObject.getString("message");
            caseCheckDto.setCheckCode(StrUtil.trimToEmpty(message));
            String dataJson = resultObject.getString("jsonResult");
            if (StrUtil.isNotBlank(dataJson)) {
                resultObject = JsonUtil.parseObject(dataJson, JSONObject.class);
                String returnCode = StrUtil.trimToEmpty(resultObject.getString("resultCode"));
                caseCheckDto.setCheckMsg(returnCode);
            }
        }
        return caseCheckDto;
    }

    /**
     * 检查CASE状态
     *
     * @param workCode
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/4/1 9:54
     */
    @Override
    public void checkCaseStatus(String workCode, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", StrUtil.trimToEmpty(workCode));
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, checkCaseStatusUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String returnCode = resultObject.getString("returnCode");
            if (!"0".equalsIgnoreCase(returnCode)) {
                String message = resultObject.getString("message");
                throw new AppException(message);
            }
        }
    }

    @Override
    public WoResult checkCaseStatusWoResult(String workCode, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", StrUtil.trimToEmpty(workCode));
        return this.sendWoUtil.postToWoAndWoResult(userInfo, reqParam, paramMap, checkCaseStatusUrl);
    }

    @Override
    public WoResult checkFaceSignForCloseCase(String workCode, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", StrUtil.trimToEmpty(workCode));
        return this.sendWoUtil.postToWoAndWoResult(userInfo, reqParam, paramMap, checkFaceSignForCloseCaseUrl);
    }

    @Override
    public WoResult checkEscortSignInForCloseCase(String workCode, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", StrUtil.trimToEmpty(workCode));
        return this.sendWoUtil.postToWoAndWoResult(userInfo, reqParam, paramMap, checkEscortSignInForCloseCaseUrl);
    }

    @Override
    public WoResult dealMonitorCase(CaseMonitorDto caseMonitorDto, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("yjcaseDto.ycaseId", StrUtil.trimToEmpty(caseMonitorDto.getWorkCode()));
        paramMap.add("yjcaseDto.monitorState", caseMonitorDto.getMonitorState());
        return this.sendWoUtil.postToWoAndWoResult(userInfo, reqParam, paramMap, this.dealMonitorCaseUrl);
    }

    /**
     * 查看CASE详情
     *
     * @param caseId
     * @param userInfo
     * @param reqParam
     * @return
     * @author JFZOU
     * @date 2020-03-20
     */
    @Override
    public CaseDetailDto viewCaseDetail(String caseId, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseid", caseId);
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, findCaseUrl);
        CaseDetailDto caseDetailDto = new CaseDetailDto();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {

            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String dtoData = resultObject.getString("dto");
            JSONObject dataObject = JsonUtil.parseObject(dtoData, JSONObject.class);
            String caseDataJson = dataObject.getJSONArray("data").get(0).toString();

            /**主CASE信息*/
            CaseDto caseDto = new CaseDto();
            caseDto = VariableConvertUtil.convertToNewEntity(caseDataJson, CaseDetailMapping.getOldAndNewPropertyMap(), CaseDto.class);
            caseDetailDto.setCaseDto(caseDto);

            /**CASE日志，日志是根据ORDER BY t.tracedatetime,t.traceid ASC排序*/
            String caseTraceListData = resultObject.getString("caseTraceList");
            List<CaseTraceDto> caseTraceDtos = JsonUtil.parseArray(caseTraceListData, CaseTraceDto.class);
            /**反转成倒序排序*/
            CollUtil.reverse(caseTraceDtos);
            caseDetailDto.setCaseTraceDtoList(caseTraceDtos);

            /**备件更换 备件更换是根据ORDER BY p.ghdatetime ASC排序*/
            String partReplaceListData = resultObject.getString("partReplaceList");
            List<PartReplaceDto> partReplaceDtos = JsonUtil.parseArray(partReplaceListData, PartReplaceDto.class);
            caseDetailDto.setPartReplaceDtoList(partReplaceDtos);

            if (resultObject.containsKey("signFlag")) {
                caseDetailDto.setSignFlag(Boolean.parseBoolean(resultObject.getString("signFlag")));
            }
            if (resultObject.containsKey("modCaseFlag")) {
                caseDetailDto.setModCaseFlag(Boolean.parseBoolean(resultObject.getString("modCaseFlag")));
            }
            if (resultObject.containsKey("finishFlag")) {
                caseDetailDto.setFinishFlag(Boolean.parseBoolean(resultObject.getString("finishFlag")));
            }
            if (resultObject.containsKey("delayFlag")) {
                caseDetailDto.setDelayFlag(Boolean.parseBoolean(resultObject.getString("delayFlag")));
            }
            if (resultObject.containsKey("partReplaceFlag")) {
                caseDetailDto.setPartReplaceFlag(Boolean.parseBoolean(resultObject.getString("partReplaceFlag")));
            }
            if (resultObject.containsKey("icbcMediaFlag")) {
                caseDetailDto.setIcbcMediaFlag(Boolean.parseBoolean(resultObject.getString("icbcMediaFlag")));
            }
            if (resultObject.containsKey("cancelFlag")) {
                caseDetailDto.setCancelFlag(Boolean.parseBoolean(resultObject.getString("cancelFlag")));
            }
            if (resultObject.containsKey("setMonitorFlag")) {
                caseDetailDto.setSetMonitorFlag(Boolean.parseBoolean(resultObject.getString("setMonitorFlag")));
            }
            if (resultObject.containsKey("cancelMonitorFlag")) {
                caseDetailDto.setCancelMonitorFlag(Boolean.parseBoolean(resultObject.getString("cancelMonitorFlag")));
            }
        }
        return caseDetailDto;
    }

    /**
     * 建立ATM机CASE
     *
     * @param caseAddDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020-03-17
     */
    @Override
    public void addCaseSubmit(CaseAddDto caseAddDto, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(caseAddDto, CaseAddMapping.getNewAndOldPropertyMap());
        this.sendWoUtil.postToWoAndReturnString(userInfo, reqParam, paramMap, addAtmCaseSubmitUrl);
    }

    @Override
    public CaseSignDto sign(String caseId, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("yjcaseDto.ycaseId", caseId);
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, updateAtmCaseArriveTimeUrl);
        CaseSignDto caseSignDto = new CaseSignDto();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {

            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String caseData = resultObject.getString("yjcase");
            caseSignDto = VariableConvertUtil.convertToNewEntity(caseData, CaseSignMapping.getOldAndNewPropertyMap(), CaseSignDto.class);

            /**属性处理原则，老平台的属性名不能修改，只能新平台适配老平台。*/
            String signEscortFlag = resultObject.getString("signEscortFlag");
            String faceIdentFlag = resultObject.getString("faceidentFlag");
            caseSignDto.setSignEscortFlag(signEscortFlag);
            caseSignDto.setFaceIdentFlag(faceIdentFlag);
        }

        /**获得人脸图片数据库数据，前端使用SRC下载图片*/
        paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", caseId);
        paramMap.add("pictureType", CaseImageTypeEnum.SIGN.getCode());
        String imagesResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, caseImageDBDataUrl);
        if (StrUtil.isNotEmpty(imagesResult) && imagesResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(imagesResult, JSONObject.class);
            String imageData = resultObject.getString("pictureSendOmeRemoteList");
            List<ImageDto> imageList = JsonUtil.parseArray(imageData, ImageDto.class);
            caseSignDto.setImageList(imageList);

        }
        caseSignDto.setCurrentTime(DateUtil.now());
        return caseSignDto;
    }

    @Override
    public WoResult signSubmit(CaseSignDto caseSignDto, UserInfo userInfo, ReqParam reqParam) {

        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("yjcaseDto.ycaseId", caseSignDto.getWorkCode());
        paramMap.add("yjcaseDto.sdDateTime", caseSignDto.getSignTime());
        /**行方陪同人员*/
        paramMap.add("yjcaseDto.escort", caseSignDto.getEscort());
        /**经度*/
        paramMap.add("longitude", reqParam.getLon());
        /**纬度*/
        paramMap.add("latitude", reqParam.getLat());

        if (StrUtil.isEmpty(caseSignDto.getSignTime())) {
            throw new AppException("签到时间不能为空！");
        }

        return this.sendWoUtil.postToWoAndWoResult(userInfo, reqParam, paramMap, updateAtmCaseArriveTimeSubmitUrl);
    }

    /**
     * 修改CASE提交
     *
     * @param caseModDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public WoResult modCaseSubmit(CaseModDto caseModDto, UserInfo userInfo, ReqParam reqParam) {
        WoResult woResult = new WoResult();
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(caseModDto, CaseModMapping.getNewAndOldPropertyMap());
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, modAtmCaseSubmitUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            if (resultObject != null) {
                woResult.setReturnCode(resultObject.getIntValue("returnCode"));
                woResult.setMessage(resultObject.getString("message"));
                if (woResult.getReturnCode() != 0) {
                    throw new AppException(woResult.getMessage());
                }
            }
        }
        return woResult;
    }

    /**
     * 进入取消CASE页面
     *
     * @param workCode
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public Map<String, Object> cancelCase(String workCode, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", workCode);
        Map<String, Object> map = new HashMap<>(2);
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, cancelAtmCaseUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String caseDetailDtoStr = resultObject.getString("yjcase");
            if (StrUtil.isNotEmpty(caseDetailDtoStr)) {
                CaseDto caseDto = VariableConvertUtil.convertToNewEntity(caseDetailDtoStr, CaseDetailMapping.getOldAndNewPropertyMap(), CaseDto.class);
                map.put("work", caseDto);
            }
            String setEScortNameAndPhoneStr = resultObject.getString("setEScortNameAndPhone");
            if (StrUtil.isNotEmpty(setEScortNameAndPhoneStr)) {
                List<String> setEScortNameAndPhone = JsonUtil.parseArray(setEScortNameAndPhoneStr, String.class);
                map.put("setEScortNameAndPhone", setEScortNameAndPhone);
            }
        }
        return map;
    }

    /**
     * 取消CASE提交
     *
     * @param caseCancelDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public WoResult cancelCaseSubmit(CaseCancelDto caseCancelDto, UserInfo userInfo, ReqParam reqParam) {
        WoResult woResult = new WoResult();
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(caseCancelDto, CaseCancelMapping.getNewAndOldPropertyMap());
        String handResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, cancelAtmCaseSubmitUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            if (resultObject != null) {
                woResult.setReturnCode(resultObject.getIntValue("returnCode"));
                woResult.setMessage(resultObject.getString("message"));
                if (woResult.getReturnCode() == 2) {
                    throw new AppException(woResult.getMessage());
                }
            }
        }
        return woResult;
    }

    @Override
    public CaseDelayDto delayCase(String caseId, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", caseId);
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, delayAtmCaseUrl);
        CaseDelayDto caseDelayDto = new CaseDelayDto();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {

            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String today = resultObject.getString("date");
            String appType = resultObject.getString("appType");
            String caseData = resultObject.getString("yjcase");
            caseDelayDto = VariableConvertUtil.convertToNewEntity(caseData, CaseDelayMapping.getOldAndNewPropertyMap(), CaseDelayDto.class);
            caseDelayDto.setToday(today);
            caseDelayDto.setAppType(appType);

            /**CASE日志，日志是根据ORDER BY t.tracedatetime,t.traceid ASC排序*/
            String caseTraceListData = resultObject.getString("caseTraceList");
            List<CaseTraceDto> caseTraceDtos = JsonUtil.parseArray(caseTraceListData, CaseTraceDto.class);
            /**反转成倒序排序*/
            CollUtil.reverse(caseTraceDtos);
            caseDelayDto.setCaseTraceDtoList(caseTraceDtos);
        }
        return caseDelayDto;
    }

    @Override
    public int delayCaseSubmit(CaseDelayDto caseDelayDto, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(caseDelayDto, CaseDelayMapping.getNewAndOldPropertyMap());
        return this.sendWoUtil.postToWoAndReturnCode(userInfo, reqParam, paramMap, delayAtmCaseSubmitUrl);
    }

    @Override
    public int concernCaseSubmit(CaseDelayDto caseDelayDto, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", caseDelayDto.getWorkCode());
        return this.sendWoUtil.postToWoAndReturnCode(userInfo, reqParam, paramMap, concertAtmCaseSubmitUrl);
    }


    @Override
    public String calcExpectCompletionTime(String caseId, String reBookTime, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("yyDateTime", reBookTime);
        paramMap.add("ycaseId", caseId);
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, calcCompletionTimeUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            return resultObject.getString("ywDateTime");
        }
        return "";
    }

    /**
     * 进入关闭CASE页面
     *
     * @param workCode
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public CaseFinishPageDto finishCase(String workCode, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", workCode);
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, finishAtmCaseUrl);
        CaseFinishPageDto caseFinishPageDto = new CaseFinishPageDto();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String caseData = resultObject.getString("yjcase");
            String curDeviceMap = resultObject.getString("curDeviceMap");
            JSONObject deviceInfoMap = resultObject.getJSONObject("deviceInfoMap");
            String modelMap = resultObject.getString("deviceTypeMap");
            String faultModelMap = resultObject.getString("fcodeAndFnameMap");
            String caseDealMap = resultObject.getString("mapCaseDealResult");
            String dealWayMap = resultObject.getString("mapDealWay");
            String maintainMap = resultObject.getString("mapMaintainType");
            String pasteBarcodeMap = resultObject.getString("pasteBarcodeMap");
            String softVersionMap = resultObject.getString("softVersionMap");
            String machineMap = resultObject.getString("machineMap");
            String zModuleICReaderMap = resultObject.getString("zModuleICReaderMap");
            Boolean needIcbcImageFlag = resultObject.getBooleanValue("needIcbcImageFlag");
            Boolean mustUploadIcbcImageFlag = resultObject.getBooleanValue("mustUploadIcbcImageFlag");
            Boolean mustUploadReceiptPicFlag = resultObject.getBooleanValue("mustUploadReceiptPicFlag");
            Boolean firstCaseFlag = resultObject.getBooleanValue("firstCaseFlag");
            Boolean enableOnlineBxFlag = resultObject.getBooleanValue("enableOnlineBxFlag");
            Boolean faultTypeFlag = resultObject.getBooleanValue("faultTypeFlag");
            Boolean inspectTypeFlag = resultObject.getBooleanValue("inspectTypeFlag");
            Boolean enableOmeBxFlag = resultObject.getBoolean("enableOmeBxFlag");
            Boolean ifExistFaultRepair = resultObject.getBooleanValue("ifExistFaultRepair");
            String icbcTopBankCode = resultObject.getString("icbcTopBankCode");
            String appTypeFault = resultObject.getString("appTypeFault");
            String appTypeInspect = resultObject.getString("appTypeInspect");
            Boolean enableUploadReceiptPic = resultObject.getBooleanValue("enableUploadReceiptPic");
            Integer icbcImageMaxNum = resultObject.getIntValue("icbcImageMaxNum");
            Integer uploadReceiptPicMaxNum = resultObject.getIntValue("uploadReceiptPicMaxNum");
            String setEScortNameAndPhone = resultObject.getString("setEScortNameAndPhone");
            if (StrUtil.isNotBlank(caseData)) {
                CaseFinishDto caseFinishDto = VariableConvertUtil.convertToNewEntity(caseData,
                        CaseFinishInitMapping.getOldAndNewPropertyMap(), CaseFinishDto.class);
                caseFinishPageDto.setCaseFinishDto(caseFinishDto);
            }
            if (StrUtil.isNotBlank(curDeviceMap)) {
                Map<String, Object> map = JsonUtil.parseMap(curDeviceMap) == null ? new HashMap<>() : JsonUtil.parseMap(curDeviceMap);
                caseFinishPageDto.setCurDeviceMap(map);
            }
            if (deviceInfoMap != null) {
                Map<String, Object> map = JSONObject.parseObject(JSONObject.toJSONString(deviceInfoMap, SerializerFeature.WriteMapNullValue), Map.class);
                caseFinishPageDto.setDeviceInfoMap(map);
            }
            if (StrUtil.isNotBlank(modelMap)) {
                Map<String, String> map = JsonUtil.parseMap(modelMap) == null ? new HashMap<>() : JsonUtil.parseMap(modelMap);
                caseFinishPageDto.setModelMap(map);
            }
            if (StrUtil.isNotBlank(faultModelMap)) {
                Map<String, String> map = JsonUtil.parseMap(faultModelMap) == null ? new HashMap<>() : JsonUtil.parseMap(faultModelMap);
                caseFinishPageDto.setFaultModelMap(map);
            }
            if (StrUtil.isNotBlank(caseDealMap)) {
                Map<String, String> map = JsonUtil.parseMap(caseDealMap) == null ? new HashMap<>() : JsonUtil.parseMap(caseDealMap);
                caseFinishPageDto.setCaseDealMap(this.changeToIntKeyMap(map));
            }
            if (StrUtil.isNotBlank(dealWayMap)) {
                Map<String, String> map = JsonUtil.parseMap(dealWayMap) == null ? new HashMap<>() : JsonUtil.parseMap(dealWayMap);
                caseFinishPageDto.setDealWayMap(this.changeToIntKeyMap(map));
            }
            if (StrUtil.isNotBlank(maintainMap)) {
                Map<String, String> map = JsonUtil.parseMap(maintainMap) == null ? new HashMap<>() : JsonUtil.parseMap(maintainMap);
                caseFinishPageDto.setMaintainMap(changeToIntKeyMap(map));
            }
            if (StrUtil.isNotBlank(pasteBarcodeMap)) {
                Map<String, String> map = JsonUtil.parseMap(pasteBarcodeMap) == null ? new HashMap<>() : JsonUtil.parseMap(pasteBarcodeMap);
                caseFinishPageDto.setPasteBarcodeMap(map);
            }
            if (StrUtil.isNotBlank(softVersionMap)) {
                Map<String, Object> map = JsonUtil.parseMap(softVersionMap) == null ? new HashMap<>() : JsonUtil.parseMap(softVersionMap);
                caseFinishPageDto.setSoftVersionMap(map);
            }
            if (StrUtil.isNotBlank(machineMap)) {
                Map<String, String> map = JsonUtil.parseMap(machineMap) == null ? new HashMap<>() : JsonUtil.parseMap(machineMap);
                caseFinishPageDto.setMachineMap(map);
            }
            if (StrUtil.isNotBlank(zModuleICReaderMap)) {
                Map<String, String> map = JsonUtil.parseMap(zModuleICReaderMap) == null ? new HashMap<>() : JsonUtil.parseMap(zModuleICReaderMap);
                caseFinishPageDto.setZModuleICReaderMap(map);
            }
            if (StrUtil.isNotBlank(setEScortNameAndPhone)) {
                List<String> list = JsonUtil.parseArray(setEScortNameAndPhone, String.class);
                caseFinishPageDto.setSetEScortNameAndPhone(list == null ? new ArrayList<>() : list);
            }
            caseFinishPageDto.setNeedIcbcImageFlag(needIcbcImageFlag);
            caseFinishPageDto.setMustUploadIcbcImageFlag(mustUploadIcbcImageFlag);
            caseFinishPageDto.setMustUploadReceiptPicFlag(mustUploadReceiptPicFlag);
            caseFinishPageDto.setFirstCaseFlag(firstCaseFlag);
            caseFinishPageDto.setEnableOnlineBxFlag(enableOnlineBxFlag);
            caseFinishPageDto.setEnableOmeBxFlag(enableOmeBxFlag);
            caseFinishPageDto.setIfExistFaultRepair(ifExistFaultRepair);
            caseFinishPageDto.setFaultTypeFlag(faultTypeFlag);
            caseFinishPageDto.setInspectTypeFlag(inspectTypeFlag);
            caseFinishPageDto.setEnableUploadReceiptPic(enableUploadReceiptPic);
            caseFinishPageDto.setIcbcTopBankCode(icbcTopBankCode);
            caseFinishPageDto.setAppTypeFault(appTypeFault);
            caseFinishPageDto.setAppTypeInspect(appTypeInspect);
            caseFinishPageDto.setIcbcImageMaxNum(icbcImageMaxNum);
            caseFinishPageDto.setUploadReceiptPicMaxNum(uploadReceiptPicMaxNum);
        }
        return caseFinishPageDto;
    }

    /**
     * 关闭CASE提交
     *
     * @param paramsMap
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public WoResult finishCaseSubmit(Map<String, Object> paramsMap, UserInfo userInfo, ReqParam reqParam) {
        WoResult woResult = new WoResult();
        if (!paramsMap.containsKey("caseFinishDto")) {
            throw new AppException("无法获取CASE完成信息，请重试");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        CaseFinishDto caseFinishDto = objectMapper.convertValue(paramsMap.get("caseFinishDto"), CaseFinishDto.class);
        if (caseFinishDto == null) {
            throw new AppException("无法获取CASE完成信息，请重试");
        }
        MultiValueMap<String, Object> requestParamsMap = VariableConvertUtil.convertToOldEntity(caseFinishDto, CaseFinishSubmitMapping.getNewAndOldPropertyMap());
        for (String key : paramsMap.keySet()) {
            if (!"caseFinishDto".equals(key)) {
                requestParamsMap.add(key, paramsMap.get(key));
            }
        }
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, requestParamsMap, finishAtmCaseSubmitUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            if (resultObject != null) {
                woResult.setReturnCode(resultObject.getIntValue("returnCode"));
                woResult.setMessage(resultObject.getString("message"));
                if (woResult.getReturnCode() != 0) {
                    throw new AppException(woResult.getMessage());
                }
            }
        }
        return woResult;
    }

    /**
     * 根据CASE编号查找照片记录
     *
     * @param workCode
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public List<ImageDto> listPicByWorkCode(String workCode, UserInfo userInfo, ReqParam reqParam) {
        List<ImageDto> imageDtoList = new ArrayList<>();
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", workCode);
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, listPicByCaseIdUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String imageListString = resultObject.getString("pictureRemoteList");
            if (StrUtil.isNotBlank(imageListString)) {
                imageDtoList = JsonUtil.parseArray(imageListString, ImageDto.class);
            }
        }
        return imageDtoList;
    }

    /**
     * 根据新平台工单状态查询CASE信息
     *
     * @param atmCaseFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-04-01
     */
    @Override
    public ListWrapper<CaseDto> listCaseByWorkStatus(AtmCaseFilter atmCaseFilter, UserInfo userInfo,
                                                     ReqParam reqParam) {
        Page page = new Page(atmCaseFilter.getPageNum(), atmCaseFilter.getPageSize());
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(atmCaseFilter);
        paramMap.add("dto.currentPage", page.getCurrent());
        paramMap.add("dto.pageSize", page.getSize());

        String handleResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, listCaseByUspUrl);

        Long totalCount = 0L;
        List<CaseDto> caseDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handleResult) && handleResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObjectSort(handleResult, JSONObject.class);
            if (resultObject.containsKey("message")) {
                String returnMsg = resultObject.getString("message");
                if (StrUtil.isNotEmpty(returnMsg)) {
                    throw new AppException(returnMsg);
                }
            }
            JSONObject dtoObject = JsonUtil.parseObjectSort(resultObject.getString("dto"), JSONObject.class);
            totalCount = dtoObject.getLong("totalCount");
            caseDtoList = VariableConvertUtil.convertToNewEntityList(dtoObject.getString("data"),
                    CaseScRemoteMapping.getOldAndNewPropertyMap(), CaseDto.class);
        }
        return ListWrapper.<CaseDto>builder()
                .list(caseDtoList)
                .total(totalCount)
                .build();
    }

    /**
     * 获得工程师的库房列表
     *
     * @param serviceBranch
     * @param engineerId
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/4/2 17:04
     */
    @Override
    public List<DepotInfoDto> listDepotByEngineer(String serviceBranch, String engineerId,
                                                  UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ocode", StrUtil.trimToEmpty(serviceBranch));
        paramMap.add("engineerId", StrUtil.trimToEmpty(engineerId));
        String handleResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, listDepotByEngineerUrl);
        List<DepotInfoDto> depotInfoDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handleResult) && handleResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            String depotAndNameListJson = resultObject.getString("depotAndNameList");
            depotInfoDtoList = JsonUtil.parseArray(depotAndNameListJson, DepotInfoDto.class);
        }
        return depotInfoDtoList;
    }

    @Override
    public void showOmeCaseImg(Long fileId, UserInfo userInfo, ReqParam reqParam, HttpServletResponse response) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("fileId", fileId);
        this.sendWoUtil.downloadFile(userInfo, reqParam, paramMap, showOmeCaseImageUrl, response);
    }

    @Override
    public FileInfoDto uploadFaceImg(MultipartFile file, String jsonData, UserInfo userInfo, ReqParam reqParam) throws Exception {
        FileInfoDto fileInfoDto = new FileInfoDto();
        Long fileId = 0L;

        String faceIdentImage = this.imageUtil.getBase64Images(file);

        ImageJsonToDto imageJsonToDto = JsonUtil.parseObject(jsonData, ImageJsonToDto.class);

        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", imageJsonToDto.getWorkCode());
        paramMap.add("base64Images", faceIdentImage);
        paramMap.add("fileNames", file.getOriginalFilename());
        paramMap.add("pictureType", CaseImageTypeEnum.SIGN.getCode());

        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, uploadFaceIdentImageSubmitUrl);
        if (SendWoUtil.HANDLE_FAIL.equalsIgnoreCase(handResult)) {
            throw new AppException("人脸识别图片上传失败。");
        }
        if (handResult.contains(StatusCodeConstants.RETURN_CODE_STRING)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String returnCodeStr = resultObject.getString("returnCode");
            String message = resultObject.getString("message");
            if (!StatusCodeConstants.SUCCESS_ZERO.equals(returnCodeStr)
                    && !StatusCodeConstants.SUCCESS_ONE.equals(returnCodeStr)) {
                throw new AppException(message);
            }

            String fileIdStr = resultObject.getString("fileIdList");
            List<Long> fileIdList = JsonUtil.parseArray(fileIdStr, Long.class);
            if (CollUtil.isNotEmpty(fileIdList)) {
                Long returnFileId = fileIdList.get(0);
                fileInfoDto.setFileId(returnFileId);
                return fileInfoDto;
            }
        }
        return fileInfoDto;
    }

    @Override
    public FileInfoDto uploadFaceImgBase64(String jsonData, String base64Img, UserInfo userInfo, ReqParam reqParam) throws Exception {

        base64Img = base64Img.replace(ImageConstants.BASE64_VIEW_PREFIX, "");
        FileInfoDto fileInfoDto = new FileInfoDto();

        ImageJsonToDto imageJsonToDto = JsonUtil.parseObject(jsonData, ImageJsonToDto.class);

        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", imageJsonToDto.getWorkCode());
        paramMap.add("base64Images", base64Img);
        paramMap.add("fileNames", "demo.jpg");
        paramMap.add("pictureType", CaseImageTypeEnum.SIGN.getCode());

        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, uploadFaceIdentImageSubmitUrl);
        if (SendWoUtil.HANDLE_FAIL.equalsIgnoreCase(handResult)) {
            throw new AppException("人脸识别图片上传失败。");
        }
        if (handResult.contains(StatusCodeConstants.RETURN_CODE_STRING)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String returnCodeStr = resultObject.getString("returnCode");
            String message = resultObject.getString("message");
            if (!StatusCodeConstants.SUCCESS_ZERO.equals(returnCodeStr)
                    && !StatusCodeConstants.SUCCESS_ONE.equals(returnCodeStr)) {
                throw new AppException(message);
            }

            String fileIdStr = resultObject.getString("fileIdList");
            List<Long> fileIdList = JsonUtil.parseArray(fileIdStr, Long.class);
            if (CollUtil.isNotEmpty(fileIdList)) {
                Long returnFileId = fileIdList.get(0);
                fileInfoDto.setFileId(returnFileId);
                return fileInfoDto;
            }
        }
        return fileInfoDto;
    }

    @Override
    public void delOmeCaseImg(Long fileId, UserInfo userInfo, ReqParam reqParam) throws Exception {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("fileId", fileId);
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, deleteOmeImageSubmitUrl);
        if (SendWoUtil.HANDLE_FAIL.equalsIgnoreCase(handResult)) {
            throw new AppException("文件删除失败。");
        }
    }

    /**
     * 检索工单类型
     *
     * @param workType
     * @param workSubType
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public String findAppTypeAndSerType(String workType, Integer workSubType, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycasetype", workType);
        paramMap.add("caseSubType", workSubType);
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, findAppTypeAndSerTypeUrl);
        String appTypeAndSerType = "";
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            appTypeAndSerType = resultObject.getString("appTypeAndSerType");
        }
        return appTypeAndSerType;
    }

    /**
     * 校验CASE关闭
     *
     * @param atmCaseFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public Map<String, Object> closeCaseOrtherCheck(AtmCaseFilter atmCaseFilter, UserInfo userInfo, ReqParam reqParam) {
        Map<String, Object> resultMap = new HashMap<>(3);
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("type1", atmCaseFilter.getDeviceModel());
        paramMap.add("deviceCode", atmCaseFilter.getDeviceCode());
        paramMap.add("ycaseId", atmCaseFilter.getWorkCode());
        String hasPartReplace = "";
        Integer returnCode = 0;
        String message = "";
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, closeCaseOrtherCheckUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            if (resultObject != null) {
                hasPartReplace = resultObject.getString("hasPartreplace");
                returnCode = resultObject.getIntValue("returnCode");
                message = resultObject.getString("message");
            }
        }
        resultMap.put("hasPartReplace", hasPartReplace);
        resultMap.put("returnCode", returnCode);
        resultMap.put("message", message);
        return resultMap;
    }

    /**
     * 检查位置信息
     *
     * @param workCode
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public Map<String, Object> checkPosition(String workCode, UserInfo userInfo, ReqParam reqParam) {
        Map<String, Object> resultMap = new HashMap<>(1);
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", workCode);
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, closeCaseOrtherCheckUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            if (resultObject != null) {
                int returnCode = resultObject.getIntValue("returnCode");
                resultMap.put("returnCode", returnCode);
            }
        }
        return resultMap;
    }

    /**
     * 上传位置信息
     *
     * @param workCode
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public WoResult uploadLocation(String workCode, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", workCode);
        paramMap.add("lon", reqParam.getLon());
        paramMap.add("lat", reqParam.getLat());
        WoResult woResult = new WoResult();
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, uploadLocationUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject jsonObject = JsonUtil.parseObject(handResult, JSONObject.class);
            if (jsonObject != null) {
                Integer returnCode = jsonObject.getIntValue("returnCode");
                String message = jsonObject.getString("message");
                woResult.setReturnCode(returnCode);
                woResult.setMessage(message);
            }
        }
        return woResult;
    }

    /**
     * 检查CASE照片上传
     *
     * @param workCode
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public Map<String, Object> checkNeedUploadPic(String workCode, UserInfo userInfo, ReqParam reqParam) {
        Map<String, Object> resultMap = new HashMap<>(3);
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", workCode);
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, closeCaseOrtherCheckUrl);
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            if (resultObject != null) {
                int returnCode = resultObject.getIntValue("returnCode");
                resultMap.put("returnCode", returnCode);
            }
        }
        return resultMap;
    }

    /**
     * 上传照片
     *
     * @param base64Img
     * @param jsonData
     * @param userInfo
     * @param reqParam
     * @return
     * @throws Exception
     */
    @Override
    public FileInfoDto uploadPhoto(String base64Img, String jsonData, UserInfo userInfo, ReqParam reqParam) throws Exception {
        FileInfoDto fileInfoDto = new FileInfoDto();
        base64Img = base64Img.replace(ImageConstants.BASE64_VIEW_PREFIX, "");
        ImageJsonToDto imageJsonToDto = JsonUtil.parseObject(jsonData, ImageJsonToDto.class);

        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", imageJsonToDto.getWorkCode());
        paramMap.add("base64Images", base64Img);
        paramMap.add("fileNames", "");
        paramMap.add("atmcode", imageJsonToDto.getDeviceCode());

        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, uploadPhotoUrl);
        if (SendWoUtil.HANDLE_FAIL.equalsIgnoreCase(handResult)) {
            throw new AppException("照片上传失败。");
        }
        if (handResult.contains(StatusCodeConstants.RETURN_CODE_STRING)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String returnCodeStr = resultObject.getString("returnCode");
            String message = resultObject.getString("message");
            if (!StatusCodeConstants.SUCCESS_ZERO.equals(returnCodeStr)
                    && !StatusCodeConstants.SUCCESS_ONE.equals(returnCodeStr)) {
                throw new AppException(message);
            }

            List<Long> fileIdList = new ArrayList<>();
            JSONArray remoteFileList = resultObject.getJSONArray("pictureRemoteList");
            if (remoteFileList != null && remoteFileList.size() > 0) {
                for (int i=0; i < remoteFileList.size(); i++) {
                    JSONObject remoteFile = remoteFileList.getJSONObject(i);
                    if (remoteFile != null) {
                        String fileId = remoteFile.getString("fileId");
                        if (fileId != null && fileId.length() > 0){
                            fileIdList.add(Long.parseLong(fileId));
                        }
                    }
                }
            }
            if (CollUtil.isNotEmpty(fileIdList)) {
                Long returnFileId = fileIdList.get(0);
                fileInfoDto.setFileId(returnFileId);
                return fileInfoDto;
            }
        }
        return fileInfoDto;
    }

    /**
     * 删除照片
     *
     * @param fileId
     * @param userInfo
     * @param reqParam
     */
    @Override
    public void delPhoto(Long fileId, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("fileId", fileId);
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, delPhotoUrl);
        if (SendWoUtil.HANDLE_FAIL.equalsIgnoreCase(handResult)) {
            throw new AppException("照片删除失败。");
        }
        if (handResult.contains(StatusCodeConstants.RETURN_CODE_STRING)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String returnCodeStr = resultObject.getString("returnCode");
            String message = resultObject.getString("message");
            if (!StatusCodeConstants.SUCCESS_ZERO.equals(returnCodeStr)
                    && !StatusCodeConstants.SUCCESS_ONE.equals(returnCodeStr)) {
                throw new AppException(message);
            }
        }
    }

    /**
     * 显示照片
     *
     * @param fileId
     * @param userInfo
     * @param reqParam
     * @param response
     */
    @Override
    public void viewPhoto(Long fileId, UserInfo userInfo, ReqParam reqParam, HttpServletResponse response) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("fileId", fileId);
        this.sendWoUtil.downloadFile(userInfo, reqParam, paramMap, viewPhotoUrl, response);
    }

    /**
     * 上传单据照片
     *
     * @param base64Img
     * @param jsonData
     * @param userInfo
     * @param reqParam
     * @return
     * @throws Exception
     */
    @Override
    public FileInfoDto uploadReceiptPhoto(String base64Img, String jsonData, UserInfo userInfo, ReqParam reqParam) throws Exception {
        FileInfoDto fileInfoDto = new FileInfoDto();
        base64Img = base64Img.replace(ImageConstants.BASE64_VIEW_PREFIX, "");
        ImageJsonToDto imageJsonToDto = JsonUtil.parseObject(jsonData, ImageJsonToDto.class);

        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", imageJsonToDto.getWorkCode());
        paramMap.add("base64Images", base64Img);
        paramMap.add("fileNames", "");
        paramMap.add("deviceType", imageJsonToDto.getDeviceModel());
        paramMap.add("deviceCode", imageJsonToDto.getSerial());

        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, uploadReceiptPhotoUrl);
        if (SendWoUtil.HANDLE_FAIL.equalsIgnoreCase(handResult)) {
            throw new AppException("照片上传失败。");
        }
        if (handResult.contains(StatusCodeConstants.RETURN_CODE_STRING)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String returnCodeStr = resultObject.getString("returnCode");
            String message = resultObject.getString("returnMsg");
            if (!StatusCodeConstants.SUCCESS_ZERO.equals(returnCodeStr)
                    && !StatusCodeConstants.SUCCESS_ONE.equals(returnCodeStr)) {
                throw new AppException(message);
            }

            List<Long> fileIdList = new ArrayList<>();
            JSONArray remoteFileList = resultObject.getJSONArray("bankRepairPicRemoteList");
            if (remoteFileList != null && remoteFileList.size() > 0) {
                for (int i=0; i < remoteFileList.size(); i++) {
                    JSONObject remoteFile = remoteFileList.getJSONObject(i);
                    if (remoteFile != null) {
                        String fileId = remoteFile.getString("fileId");
                        if (fileId != null && fileId.length() > 0){
                            fileIdList.add(Long.parseLong(fileId));
                        }
                    }
                }
            }
            if (CollUtil.isNotEmpty(fileIdList)) {
                Long returnFileId = fileIdList.get(0);
                fileInfoDto.setFileId(returnFileId);
                return fileInfoDto;
            }
        }
        return fileInfoDto;
    }

    /**
     * 删除单据照片
     *
     * @param fileId
     * @param userInfo
     * @param reqParam
     */
    @Override
    public void delReceiptPhoto(Long fileId, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("fileId", fileId);
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, delReceiptPhotoUrl);
        if (SendWoUtil.HANDLE_FAIL.equalsIgnoreCase(handResult)) {
            throw new AppException("照片删除失败。");
        }
    }

    /**
     * 获得派工单信息，用于建CASE
     *
     * @param workOrderId
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/4/7 09:12
     */
    @Override
    public WorkOrderDto findWorkOrderById(String workOrderId, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("workOrderId", StrUtil.trimToEmpty(workOrderId));
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, addYjcaseFromWorkOrderInitUrl);
        WorkOrderDto workOrderDto = new WorkOrderDto();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String yjCaseJson = resultObject.getString("yjcase");
            if (StrUtil.isNotBlank(yjCaseJson)) {
                workOrderDto = VariableConvertUtil.convertToNewEntity(yjCaseJson,
                        WorkOrderMapping.getOldAndNewPropertyMap(), WorkOrderDto.class);
            }
            String workOrderIdJson = resultObject.getString("workOrderId");
            if (StrUtil.isNotBlank(workOrderIdJson) && workOrderDto != null) {
                workOrderDto.setWorkOrderId(workOrderIdJson);
            }
            workOrderDto.setCurrentTime(DateUtil.now());
        }
        return workOrderDto;
    }

    /**
     * 检查CASE
     *
     * @param workCode
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/4/7 14:18
     */
    @Override
    public String checkYjCase(String workCode, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("workCode", StrUtil.trimToEmpty(workCode));
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, checkYjcaseUrl);
        String message = "";
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            message = resultObject.getString("message");
        }
        return StrUtil.trimToEmpty(message);
    }

    /**
     * 查询工程师已关闭未审核CASE
     *
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-04-13
     */
    @Override
    public List<CaseDto> listCloseNoAuditCase(UserInfo userInfo, ReqParam reqParam) {
        String handleResult = this.sendWoUtil.postToWo(userInfo, reqParam, listCloseNoAuditAtmCaseUrl);

        List<CaseDto> caseDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handleResult) && handleResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObjectSort(handleResult, JSONObject.class);
            caseDtoList = VariableConvertUtil.convertToNewEntityList(resultObject.getString("caseList"),
                    CaseMapping.getOldAndNewPropertyMap(), CaseDto.class);
        }
        return caseDtoList;
    }

    /**
     * 转换map的key为int
     *
     * @param map
     * @return
     */
    private Map<Integer, String> changeToIntKeyMap(Map<String, String> map) {
        Map<Integer, String> newMap = new LinkedHashMap<>();
        if (CollectionUtil.isEmpty(map)) {
            return newMap;
        }
        for (Iterator<Map.Entry<String, String>> it = map.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, String> itt = it.next();
            newMap.put(Integer.parseInt(itt.getKey()), itt.getValue());
        }
        return newMap;
    }

}
