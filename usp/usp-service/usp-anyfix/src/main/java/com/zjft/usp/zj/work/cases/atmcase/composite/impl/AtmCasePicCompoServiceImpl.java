package com.zjft.usp.zj.work.cases.atmcase.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.common.constant.ImageConstants;
import com.zjft.usp.zj.common.constant.StatusCodeConstants;
import com.zjft.usp.zj.common.utils.ImageUtil;
import com.zjft.usp.zj.common.utils.SendWoUtil;
import com.zjft.usp.zj.common.utils.VariableConvertUtil;
import com.zjft.usp.zj.work.cases.atmcase.composite.AtmCasePicCompoService;
import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.*;
import com.zjft.usp.zj.work.cases.atmcase.dto.file.ImageJsonToDto;
import com.zjft.usp.zj.work.cases.atmcase.mapping.atmcase.CaseMapping;
import com.zjft.usp.zj.work.cases.atmcase.mapping.atmcase.CasePicMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ATM机CASE照片上传聚合实现类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-04-06 19:09
 **/
@Slf4j
@Service
public class AtmCasePicCompoServiceImpl implements AtmCasePicCompoService {
    @Resource
    private SendWoUtil sendWoUtil;
    @Resource
    private ImageUtil imageUtil;
    @Value("${wo.atmcase.listNotAuditCasePicUrl}")
    private String listNotAuditCasePicUrl;
    @Value("${wo.atmcase.initCasePhotoUrl}")
    private String initCasePhotoUrl;
    @Value("${wo.atmcase.listDemoPhotoIdUrl}")
    private String listDemoPhotoIdUrl;
    @Value("${wo.atmcase.listCasePhotoIdUrl}")
    private String listCasePhotoIdUrl;
    @Value("${wo.atmcase.listCasePhotoIdByMachineCodeUrl}")
    private String listCasePhotoIdByMachineCodeUrl;
    @Value("${wo.atmcase.uploadCasePicUrl}")
    private String uploadCasePicUrl;
    @Value("${wo.atmcase.viewCasePhotoUrl}")
    private String viewCasePhotoUrl;
    @Value("${wo.atmcase.viewDemoPhotoUrl}")
    private String viewDemoPhotoUrl;
    @Value("${wo.atmcase.addNoPhotoReasonUrl}")
    private String addNoPhotoReasonUrl;
    @Value("${wo.atmcase.uploadSubmitUrl}")
    private String uploadSubmitUrl;

    /**
     * 获取未审核可修改的CASE照片列表
     *
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-04-06
     */
    @Override
    public List<CaseDto> listNotAuditCasePic(UserInfo userInfo, ReqParam reqParam) {
        String handleResult = this.sendWoUtil.postToWo(userInfo, reqParam, listNotAuditCasePicUrl);
        List<CaseDto> caseDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handleResult) && handleResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            caseDtoList = VariableConvertUtil.convertToNewEntityList(resultObject.getString("caseList"),
                    CaseMapping.getOldAndNewPropertyMap(), CaseDto.class);
        }
        return caseDtoList;
    }

    /**
     * 根据case编号获取case照片
     *
     * @param workCode
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public CasePicPageDto initCasePhoto(String workCode, UserInfo userInfo, ReqParam reqParam) {
        CasePicPageDto casePictureDto = new CasePicPageDto();
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", workCode);
        String handleResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, initCasePhotoUrl);
        if (StrUtil.isNotEmpty(handleResult) && handleResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            if (resultObject != null) {
                String isNeedUploadUpPartPhoto = resultObject.getString("isNeedUploadUpPartPhoto");
                String isNeedUploadDownPartPhoto = resultObject.getString("isNeedUploadDownPartPhoto");
                String casePicRemoteDtoStr = resultObject.getString("casePicRemote");
                String casePicDefineRemoteStr = resultObject.getString("casePicDefineRemote");
                String partReplacePicDefineRemoteListStr = resultObject.getString("partReplacePicDefineRemoteList");
                String caseDtoStr = resultObject.getString("yjcase");
                casePictureDto.setIsNeedUploadUpPartPhoto(isNeedUploadUpPartPhoto);
                casePictureDto.setIsNeedUploadDownPartPhoto(isNeedUploadDownPartPhoto);
                if (StrUtil.isNotBlank(casePicRemoteDtoStr)) {
                    CasePicDto casePicDto = JsonUtil.parseObject(casePicRemoteDtoStr, CasePicDto.class);
                    casePictureDto.setCasePicDto(casePicDto);
                }
                if (StrUtil.isNotBlank(casePicDefineRemoteStr)) {
                    CasePicDefineDto casePicDefineDto = JsonUtil.parseObject(casePicDefineRemoteStr,
                            CasePicDefineDto.class);
                    casePictureDto.setCasePicDefineDto(casePicDefineDto);
                }
                if (StrUtil.isNotBlank(partReplacePicDefineRemoteListStr)) {
                    List<Object> partReplacePicDefineRemoteList = JsonUtil.parseArray(partReplacePicDefineRemoteListStr, Object.class);
                    casePictureDto.setPartReplacePicDefineList(partReplacePicDefineRemoteList);
                }
                if (StrUtil.isNotBlank(caseDtoStr)) {
                    CaseDto caseDto = VariableConvertUtil.convertToNewEntity(caseDtoStr,
                            CasePicMapping.getOldAndNewPropertyMap(), CaseDto.class);
                    casePictureDto.setCaseDto(caseDto);
                }
            }
        }
        return casePictureDto;
    }

    /**
     * 获取CASE的demo照片编号
     *
     * @param workCode
     * @param mainId
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public Map<String, Object> listDemoPhotoId(String workCode, Long mainId, UserInfo userInfo, ReqParam reqParam) {
        Map<String, Object> map = new HashMap<>(2);
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", workCode);
        paramMap.add("mainId", mainId);
        String handleResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, listDemoPhotoIdUrl);
        if (StrUtil.isNotEmpty(handleResult) && handleResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            if (resultObject != null) {
                String casePicDefineDetailListStr = resultObject.getString("casePicDefineDetailRemoteList");
                String partReplacePicDefineListStr = resultObject.getString("partReplacePicDefineRemoteList");
                List<CasePicDefineDetailDto> casePicDefineDetailDtoList = StrUtil.isBlank(casePicDefineDetailListStr) ?
                        new ArrayList<>() : JsonUtil.parseArray(casePicDefineDetailListStr, CasePicDefineDetailDto.class);
                List<CasePicDefineDetailDto> partReplacePicDefineDetailDtoList = StrUtil.isBlank(partReplacePicDefineListStr) ?
                        new ArrayList<>() : JsonUtil.parseArray(partReplacePicDefineListStr, CasePicDefineDetailDto.class);
                map.put("casePicDefineDetailDtoList", casePicDefineDetailDtoList);
                map.put("partReplacePicDefineDetailDtoList", partReplacePicDefineDetailDtoList);
            }
        }
        return map;
    }

    /**
     * 获取case照片编号
     *
     * @param workCode
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public List<CasePicDetailDto> listCasePhotoId(String workCode, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", workCode);
        String handleResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, listCasePhotoIdUrl);
        if (StrUtil.isNotEmpty(handleResult) && handleResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            if (resultObject != null) {
                String casePicDetailDtoListStr = resultObject.getString("casePicDetailRemoteList");
                return StrUtil.isBlank(casePicDetailDtoListStr) ?
                        new ArrayList<>() : JsonUtil.parseArray(casePicDetailDtoListStr, CasePicDetailDto.class);
            }
        }
        return null;
    }

    /**
     * 根据机器制造号查询case照片列表
     *
     * @param casePicDetailDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public List<CasePicDetailDto> listCasePhotoIdByMachineCode(CasePicDetailDto casePicDetailDto, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", casePicDetailDto.getYcaseId());
        paramMap.add("photoType", casePicDetailDto.getPhotoType());
        paramMap.add("machineCode", casePicDetailDto.getMachineCode());
        String handleResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, listCasePhotoIdByMachineCodeUrl);
        if (StrUtil.isNotEmpty(handleResult) && handleResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            if (resultObject != null) {
                String casePicDetailDtoListStr = resultObject.getString("casePicDetailRemoteList");
                return StrUtil.isBlank(casePicDetailDtoListStr) ?
                        new ArrayList<>() : JsonUtil.parseArray(casePicDetailDtoListStr, CasePicDetailDto.class);
            }
        }
        return null;
    }

    /**
     * 上传CASE照片
     *
     * @param base64Img
     * @param jsonData
     * @param userInfo
     * @param reqParam
     * @return
     * @throws Exception
     */
    @Override
    public void uploadCasePic(String base64Img, String jsonData, UserInfo userInfo, ReqParam reqParam) throws Exception {
        ImageJsonToDto imageJsonToDto = JsonUtil.parseObject(jsonData, ImageJsonToDto.class);
        base64Img = base64Img.replace(ImageConstants.BASE64_VIEW_PREFIX, "");
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("ycaseId", imageJsonToDto.getWorkCode());
        paramMap.add("base64Images", base64Img);
        paramMap.add("photoType", imageJsonToDto.getPhotoType());
        paramMap.add("machineCode", imageJsonToDto.getSerial());
        paramMap.add("fileNames", "");
        // 此处为上传case照片，删除使用delCasePic
        paramMap.add("delPhotoIds", "");

        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, uploadCasePicUrl);
        if (SendWoUtil.HANDLE_FAIL.equalsIgnoreCase(handResult)) {
            throw new AppException("照片上传失败。");
        }
        if (handResult.contains(StatusCodeConstants.RETURN_CODE_STRING)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            JSONObject jsonResult = resultObject.getJSONObject("jsonResult");
            if (jsonResult != null) {
                String tipmsg = jsonResult.getString("tipmsg");
                if (StrUtil.isNotEmpty(tipmsg) && !"CASE照片上传成功。".equals(tipmsg)) {
                    throw new AppException(tipmsg);
                }
            }
        }
    }

    /**
     * 删除CASE照片
     *
     * @param workCode
     * @param fileId
     * @param userInfo
     * @param reqParam
     */
    @Override
    public void delCasePic(String workCode, Long fileId, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        // 删除照片只用传入文件编号即可
        paramMap.add("ycaseId", workCode);
        paramMap.add("fileNames", "");
        paramMap.add("photoType", "");
        paramMap.add("machineCode", "");
        paramMap.add("delPhotoIds", fileId);
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, uploadCasePicUrl);
        if (SendWoUtil.HANDLE_FAIL.equalsIgnoreCase(handResult)) {
            throw new AppException("照片删除失败。");
        }
    }

    /**
     * 显示CASE照片
     *
     * @param fileId
     * @param userInfo
     * @param reqParam
     * @param response
     */
    @Override
    public void viewCasePhoto(Long fileId, UserInfo userInfo, ReqParam reqParam, HttpServletResponse response) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("photoId", fileId);
        this.sendWoUtil.downloadFile(userInfo, reqParam, paramMap, viewCasePhotoUrl, response);
    }

    /**
     * 显示Demo照片
     *
     * @param fileId
     * @param userInfo
     * @param reqParam
     * @param response
     */
    @Override
    public void viewDemoPhoto(Long fileId, UserInfo userInfo, ReqParam reqParam, HttpServletResponse response) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("photoId", fileId);
        this.sendWoUtil.downloadFile(userInfo, reqParam, paramMap, viewDemoPhotoUrl, response);
    }

    /**
     * 不上传照片提交
     *
     * @param casePicDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public String addNoPhotoReason(CasePicDto casePicDto, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(casePicDto);
        String handResult =  this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, addNoPhotoReasonUrl);
        if (SendWoUtil.HANDLE_FAIL.equalsIgnoreCase(handResult)) {
            throw new AppException("提交失败。");
        }
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String message = resultObject.getString("message");
            if (StrUtil.isNotBlank(message) && !message.contains("成功")) {
                throw new AppException(message);
            }
            return message;
        }
        return null;
    }

    /**
     * 上传照片提交
     *
     * @param casePicDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public JSONObject uploadSubmit(CasePicDto casePicDto, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("casePicDefineRemote.mainId", casePicDto.getMainId());
        paramMap.add("photoType", casePicDto.getPhotoType());
        paramMap.add("casePicDefineRemote.photoNum", casePicDto.getPhotoNum());
        paramMap.add("uploadNum", casePicDto.getUploadNum());
        paramMap.add("ycaseId", casePicDto.getYcaseId());
        String handResult =  this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, uploadSubmitUrl);
        if (SendWoUtil.HANDLE_FAIL.equalsIgnoreCase(handResult)) {
            throw new AppException("上传提交失败。");
        }
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            if (resultObject != null) {
                JSONObject jsonResult = resultObject.getJSONObject("jsonResult");
                return jsonResult;
            }
        }
        return null;
    }

    @Override
    public Integer countNotAuditCasePic(UserInfo userInfo, ReqParam reqParam) {
        List<CaseDto> caseDtos = this.listNotAuditCasePic(userInfo, reqParam);
        if(CollectionUtil.isNotEmpty(caseDtos)) {
            return caseDtos.size();
        }
        return 0;
    }
}
