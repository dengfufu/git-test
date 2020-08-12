package com.zjft.usp.zj.work.repair.strategy.impl;

import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.common.utils.SendWoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 石嘴山银行报修策略实现类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2019-11-14 10:09
 **/
@Component(AbstractRepairStrategy.REPAIR_SCS)
public class RepairSZSStrategy extends AbstractRepairStrategy {
    @Resource
    private SendWoUtil sendWoUtil;
    @Value("${wo.repair.szs.findRepairDetailUrl}")
    private String findRepairDetailUrl;
    @Value("${wo.repair.szs.turnHandleUrl}")
    private String turnHandleUrl;
    @Value("${wo.repair.szs.turnHandleSubmitUrl}")
    private String turnHandleSubmitUrl;
    @Value("${wo.repair.szs.phoneHandleUrl}")
    private String phoneHandleUrl;
    @Value("${wo.repair.szs.phoneHandleSubmitUrl}")
    private String phoneHandleSubmitUrl;
    @Value("${wo.repair.szs.returnHandleUrl}")
    private String returnHandleUrl;
    @Value("${wo.repair.szs.returnHandleSubmitUrl}")
    private String returnHandleSubmitUrl;
    @Value("${wo.repair.szs.associateCaseUrl}")
    private String associateCaseUrl;
    @Value("${wo.repair.szs.associateCaseSubmitUrl}")
    private String associateCaseSubmitUrl;
    @Value("${wo.repair.szs.findBankMapUrl}")
    private String findBankMapUrl;
    @Value("${wo.repair.szs.findBureauAndBranchMapUrl}")
    private String findBureauAndBranchMapUrl;
    @Value("${wo.repair.szs.listFaultRepairPicUrl}")
    private String listFaultRepairPicUrl;
    @Value("${wo.repair.szs.viewFaultRepairPicUrl}")
    private String viewFaultRepairPicUrl;

    /**
     * 查看报修单详情
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    @Override
    public String findRepairDetail(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, findRepairDetailUrl);
    }

    /**
     * 进入转处理页面
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    @Override
    public String turnHandle(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, turnHandleUrl);
    }

    /**
     * 转处理提交
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    @Override
    public String turnHandleSubmit(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, turnHandleSubmitUrl);
    }

    /**
     * 进入电话处理页面
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    @Override
    public String phoneHandle(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, phoneHandleUrl);
    }

    /**
     * 电话处理提交
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    @Override
    public String phoneHandleSubmit(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, phoneHandleSubmitUrl);
    }

    /**
     * 进入退单页面
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    @Override
    public String returnHandle(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, returnHandleUrl);
    }

    /**
     * 退单提交
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    @Override
    public String returnHandleSubmit(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, returnHandleSubmitUrl);
    }

    /**
     * 进入关联CASE页面
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    @Override
    public String associateCase(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, associateCaseUrl);
    }

    /**
     * 关联CASE提交
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    @Override
    public String associateCaseSubmit(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, associateCaseSubmitUrl);
    }

    /**
     * 查询补录失败的记录
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    @Override
    public String listBxSendCreateFail(MultiValueMap<String, Object> paramMap, UserInfo userInfo,
                                       ReqParam reqParam) {
        throw new AppException("该方法尚未实现");
    }

    /**
     * 进入补录失败修改页面
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    @Override
    public String modBxSendCreateFail(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        throw new AppException("该方法尚未实现");
    }

    /**
     * 补录失败修改提交
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    @Override
    public String modBxSendCreateFailSubmit(MultiValueMap<String, Object> paramMap, UserInfo userInfo,
                                            ReqParam reqParam) {
        throw new AppException("该方法尚未实现");
    }

    /**
     * 获取银行编号与名称映射Map
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-23
     */
    @Override
    public String findBankMap(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, findBankMapUrl);
    }

    /**
     * 获取服务站和网点编号与名称映射Map
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-25
     */
    @Override
    public String findBureauAndBranchMap(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, findBureauAndBranchMapUrl);
    }

    /**
     * 根据交易号获取工行对接报修图片
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-25
     */
    @Override
    public String listFaultRepairPic(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, listFaultRepairPicUrl);
    }

    /**
     * 查看工行对接报修图片
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @param response
     * @return
     * @author Qiugm
     * @date 2020-03-25
     */
    @Override
    public void viewFaultRepairPic(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam,
            HttpServletResponse response) {
        this.sendWoUtil.downloadFile(userInfo, reqParam, paramMap, viewFaultRepairPicUrl, response);
    }
}