package com.zjft.usp.zj.work.cases.atmcase.composite;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.common.dto.WoResult;
import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.*;
import com.zjft.usp.zj.work.cases.atmcase.dto.file.FileInfoDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.file.ImageDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.partreplace.DepotInfoDto;
import com.zjft.usp.zj.work.cases.atmcase.filter.AtmCaseFilter;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * ATM机CASE聚合接口
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-13 16:43
 **/
public interface AtmCaseCompoService {

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
    CaseCheckDto ifExistSameMachineCase(AtmCaseFilter atmCaseFilter, UserInfo userInfo, ReqParam reqParam);

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
    void checkCaseStatus(String workCode, UserInfo userInfo, ReqParam reqParam);

    /**
     * 检查CASE状态
     *
     * @param workCode
     * @param userInfo
     * @param reqParam
     * @return
     * @author jfzou
     * @date 2020/4/14 9:54
     */
    WoResult checkCaseStatusWoResult(String workCode, UserInfo userInfo, ReqParam reqParam);

    /**
     * 关闭CASE检查是否已做人脸识别签到
     *
     * @param workCode
     * @param userInfo
     * @param reqParam
     * @return
     * @author jfzou
     * @date 2020/4/6 9:54
     */
    WoResult checkFaceSignForCloseCase(String workCode, UserInfo userInfo, ReqParam reqParam);

    /**
     * 关闭CASE检查是否已经处理行方陪同人员签到
     *
     * @param workCode
     * @param userInfo
     * @param reqParam
     * @return
     * @author jfzou
     * @date 2020/4/14 9:54
     */
    WoResult checkEscortSignInForCloseCase(String workCode, UserInfo userInfo, ReqParam reqParam);

    /**
     * 处理监控相关业务
     *
     * @param caseMonitorDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    WoResult dealMonitorCase(CaseMonitorDto caseMonitorDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查看CASE详情
     *
     * @param caseId
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-14
     */
    CaseDetailDto viewCaseDetail(String caseId, UserInfo userInfo, ReqParam reqParam);

    /**
     * 建立ATM机CASE提交
     *
     * @param caseAddDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-17
     */
    void addCaseSubmit(CaseAddDto caseAddDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入工程师签到页面
     *
     * @param caseId
     * @param userInfo
     * @param reqParam
     * @return
     * @author JFZOU
     * @date 2020-03-18
     */
    CaseSignDto sign(String caseId, UserInfo userInfo, ReqParam reqParam);

    /**
     * 处理工程师签到提交请求
     *
     * @param caseSignDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-17
     */
    WoResult signSubmit(CaseSignDto caseSignDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 修改CASE提交
     *
     * @param caseModDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    WoResult modCaseSubmit(CaseModDto caseModDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入取消CASE页面
     *
     * @param workCode
     * @param userInfo
     * @param reqParam
     * @return
     */
    Map<String, Object> cancelCase(String workCode, UserInfo userInfo, ReqParam reqParam);

    /**
     * 取消CASE提交
     *
     * @param caseCancelDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    WoResult cancelCaseSubmit(CaseCancelDto caseCancelDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入延期CASE
     *
     * @param caseId
     * @param userInfo
     * @param reqParam
     * @return
     */
    CaseDelayDto delayCase(String caseId, UserInfo userInfo, ReqParam reqParam);

    /**
     * 延期CASE提交
     *
     * @param caseDelayDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    int delayCaseSubmit(CaseDelayDto caseDelayDto, UserInfo userInfo, ReqParam reqParam);


    /**
     * 关注CASE提交
     *
     * @param caseDelayDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    int concernCaseSubmit(CaseDelayDto caseDelayDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 显示文件
     *
     * @param fileId
     * @param userInfo
     * @param reqParam
     * @param response
     */
    void showOmeCaseImg(Long fileId, UserInfo userInfo, ReqParam reqParam, HttpServletResponse response);

    /**
     * 上传人脸图片
     *
     * @param file
     * @param jsonData
     * @param userInfo
     * @param reqParam
     * @return
     * @throws Exception
     */
    FileInfoDto uploadFaceImg(MultipartFile file, String jsonData, UserInfo userInfo, ReqParam reqParam) throws Exception;


    /**
     * 上传人脸图片
     *
     * @param jsonData
     * @param base64Img
     * @param userInfo
     * @param reqParam
     * @return
     * @throws Exception
     */
    FileInfoDto uploadFaceImgBase64(String jsonData, String base64Img, UserInfo userInfo, ReqParam reqParam) throws Exception;

    /**
     * 根据文件名称删除文件
     *
     * @param fileId
     * @param userInfo
     * @param reqParam
     * @throws Exception
     */
    void delOmeCaseImg(Long fileId, UserInfo userInfo, ReqParam reqParam) throws Exception;

    /**
     * 计算CASE延期时间
     *
     * @param caseId
     * @param reBookTime
     * @param userInfo
     * @param reqParam
     * @return
     */
    String calcExpectCompletionTime(String caseId, String reBookTime, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入关闭CASE页面
     *
     * @param workCode
     * @param userInfo
     * @param reqParam
     * @return
     */
    CaseFinishPageDto finishCase(String workCode, UserInfo userInfo, ReqParam reqParam);

    /**
     * 关闭CASE提交
     *
     * @param paramsMap
     * @param userInfo
     * @param reqParam
     * @return
     */
    WoResult finishCaseSubmit(Map<String, Object> paramsMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 根据CASE编号查找照片记录
     *
     * @param workCode
     * @param userInfo
     * @param reqParam
     * @return
     */
    List<ImageDto> listPicByWorkCode(String workCode, UserInfo userInfo, ReqParam reqParam);

    /**
     * 检索工单类型
     *
     * @param workType
     * @param workSubType
     * @param userInfo
     * @param reqParam
     * @return
     */
    String findAppTypeAndSerType(String workType, Integer workSubType, UserInfo userInfo, ReqParam reqParam);

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
    ListWrapper<CaseDto> listCaseByWorkStatus(AtmCaseFilter atmCaseFilter, UserInfo userInfo, ReqParam reqParam);

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
    List<DepotInfoDto> listDepotByEngineer(String serviceBranch, String engineerId,
                                           UserInfo userInfo, ReqParam reqParam);

    /**
     * 校验CASE关闭
     *
     * @param atmCaseFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    Map<String, Object> closeCaseOrtherCheck(AtmCaseFilter atmCaseFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 检查位置信息
     *
     * @param workCode
     * @param userInfo
     * @param reqParam
     * @return
     */
    Map<String, Object> checkPosition(String workCode, UserInfo userInfo, ReqParam reqParam);

    /**
     * 上传位置信息
     *
     * @param workCode
     * @param userInfo
     * @param reqParam
     * @return
     */
    WoResult uploadLocation(String workCode, UserInfo userInfo, ReqParam reqParam);

    /**
     * 检查CASE照片上传
     *
     * @param workCode
     * @param userInfo
     * @param reqParam
     * @return
     */
    Map<String, Object> checkNeedUploadPic(String workCode, UserInfo userInfo, ReqParam reqParam);

    /**
     * 上传设备照片
     *
     * @param base64Img
     * @param jsonData
     * @param userInfo
     * @param reqParam
     * @return
     * @throws Exception
     */
    FileInfoDto uploadPhoto(String base64Img, String jsonData, UserInfo userInfo, ReqParam reqParam) throws Exception;

    /**
     * 删除照片
     *
     * @param fileId
     * @param userInfo
     * @param reqParam
     */
    void delPhoto(Long fileId, UserInfo userInfo, ReqParam reqParam);

    /**
     * 显示照片
     *
     * @param fileId
     * @param userInfo
     * @param reqParam
     * @param response
     */
    void viewPhoto(Long fileId, UserInfo userInfo, ReqParam reqParam, HttpServletResponse response);

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
    FileInfoDto uploadReceiptPhoto(String base64Img, String jsonData, UserInfo userInfo, ReqParam reqParam) throws Exception;

    /**
     * 删除单据照片
     *
     * @param fileId
     * @param userInfo
     * @param reqParam
     */
    void delReceiptPhoto(Long fileId, UserInfo userInfo, ReqParam reqParam);

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
    WorkOrderDto findWorkOrderById(String workOrderId, UserInfo userInfo, ReqParam reqParam);

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
    String checkYjCase(String workCode, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查询工程师已关闭未审核CASE
     *
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-04-13
     */
    List<CaseDto> listCloseNoAuditCase(UserInfo userInfo, ReqParam reqParam);

}
