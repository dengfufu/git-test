package com.zjft.usp.zj.work.cases.atmcase.composite;

import com.alibaba.fastjson.JSONObject;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.CaseDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.CasePicDetailDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.CasePicDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.CasePicPageDto;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * ATM机CASE照片上传聚合接口
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-04-06 19:09
 **/
public interface AtmCasePicCompoService {

    /**
     * 获取未审核可修改的CASE照片列表
     *
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-04-06
     */
    List<CaseDto> listNotAuditCasePic(UserInfo userInfo, ReqParam reqParam);

    /**
     * 根据case编号获取case照片
     *
     * @param workCode
    * @param userInfo
     * @param reqParam
     * @return
     */
    CasePicPageDto initCasePhoto(String workCode, UserInfo userInfo, ReqParam reqParam);

    /**
     * 获取CASE的demo照片编号
     *
     * @param workCode
     * @param mainId
     * @param userInfo
     * @param reqParam
     * @return
     */
    Map<String, Object> listDemoPhotoId(String workCode, Long mainId, UserInfo userInfo, ReqParam reqParam);

    /**
     * 获取case照片编号
     *
     * @param workCode
     * @param userInfo
     * @param reqParam
     * @return
     */
    List<CasePicDetailDto> listCasePhotoId(String workCode, UserInfo userInfo, ReqParam reqParam);

    /**
     * 根据机器制造号查询case照片列表
     *
     * @param casePicDetailDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    List<CasePicDetailDto> listCasePhotoIdByMachineCode(CasePicDetailDto casePicDetailDto, UserInfo userInfo, ReqParam reqParam);

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
    void uploadCasePic(String base64Img, String jsonData, UserInfo userInfo, ReqParam reqParam) throws Exception;

    /**
     * 删除CASE照片
     *
     * @param workCode
     * @param fileId
     * @param userInfo
     * @param reqParam
     */
    void delCasePic(String workCode, Long fileId, UserInfo userInfo, ReqParam reqParam);

    /**
     * 显示CASE照片
     *
     * @param fileId
     * @param userInfo
     * @param reqParam
     * @param response
     */
    void viewCasePhoto(Long fileId, UserInfo userInfo, ReqParam reqParam, HttpServletResponse response);

    /**
     * 显示Demo照片
     *
     * @param fileId
     * @param userInfo
     * @param reqParam
     * @param response
     */
    void viewDemoPhoto(Long fileId, UserInfo userInfo, ReqParam reqParam, HttpServletResponse response);

    /**
     * 不上传照片提交
     *
     * @param casePicDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    String addNoPhotoReason(CasePicDto casePicDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 上传照片提交
     *
     * @param casePicDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    JSONObject uploadSubmit(CasePicDto casePicDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 未审核可修改的数量
     * @param userInfo
     * @param reqParam
     * @return
     */
    Integer countNotAuditCasePic(UserInfo userInfo, ReqParam reqParam);
}
