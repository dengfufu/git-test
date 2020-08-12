package com.zjft.usp.zj.work.workorder.strategy.impl;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.common.utils.SendWoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;

/**
 * 石嘴山派工单策略实现类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2019-11-14 10:09
 **/
@Component(AbstractWorkOrderStrategy.WORK_ORDER_SCS)
public class WorkOrderSZSStrategy extends AbstractWorkOrderStrategy {
    @Resource
    private SendWoUtil sendWoUtil;
    @Value("${wo.workorder.szs.addWorkOrderUrl}")
    private String addWorkOrderUrl;
    @Value("${wo.workorder.szs.addWorkOrderSubmitUrl}")
    private String addWorkOrderSubmitUrl;
    @Value("${wo.workorder.szs.acceptWorkOrderUrl}")
    private String acceptWorkOrderUrl;
    @Value("${wo.workorder.szs.acceptWorkOrderSubmitUrl}")
    private String acceptWorkOrderSubmitUrl;
    @Value("${wo.workorder.szs.refuseWorkOrderUrl}")
    private String refuseWorkOrderUrl;
    @Value("${wo.workorder.szs.refuseWorkOrderSubmitUrl}")
    private String refuseWorkOrderSubmitUrl;
    @Value("${wo.workorder.szs.preBookUrl}")
    private String preBookUrl;
    @Value("${wo.workorder.szs.preBookSubmitUrl}")
    private String preBookSubmitUrl;
    @Value("${wo.workorder.szs.renewPreBookUrl}")
    private String renewPreBookUrl;
    @Value("${wo.workorder.szs.renewPreBookSubmitUrl}")
    private String renewPreBookSubmitUrl;
    @Value("${wo.workorder.szs.returnManagerUrl}")
    private String returnManagerUrl;
    @Value("${wo.workorder.szs.returnManagerSubmitUrl}")
    private String returnManagerSubmitUrl;
    @Value("${wo.workorder.szs.activeEndWorkOrderUrl}")
    private String activeEndWorkOrderUrl;
    @Value("${wo.workorder.szs.activeEndWorkOrderSubmitUrl}")
    private String activeEndWorkOrderSubmitUrl;


    /**
     * 进入新建派工页面
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public String addWorkOrder(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, addWorkOrderUrl);
    }

    /**
     * 新建派工提交
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public String addWorkOrderSubmit(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, addWorkOrderSubmitUrl);
    }

    /**
     * 进入接受派工页面
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public String acceptWorkOrder(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, acceptWorkOrderUrl);
    }

    /**
     * 接受派工提交
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public String acceptWorkOrderSubmit(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, acceptWorkOrderSubmitUrl);
    }

    /**
     * 进入拒绝派工页面
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public String refuseWorkOrder(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, refuseWorkOrderUrl);
    }

    /**
     * 拒绝派工提交
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public String refuseWorkOrderSubmit(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, refuseWorkOrderSubmitUrl);
    }

    /**
     * 进入工单预约页面
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public String preBook(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, preBookUrl);
    }

    /**
     * 工单预约提交
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public String preBookSubmit(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, preBookSubmitUrl);
    }

    /**
     * 重进入重新预约页面
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public String renewPreBook(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, renewPreBookUrl);
    }

    /**
     * 重新预约提交
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public String renewPreBookSubmit(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, renewPreBookSubmitUrl);
    }

    /**
     * 进入退回主管页面
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public String returnManager(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, returnManagerUrl);
    }

    /**
     * 退回主管提交
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public String returnManagerSubmit(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, returnManagerSubmitUrl);
    }

    /**
     * 进入主动结束派工页面
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public String activeEndWorkOrder(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, activeEndWorkOrderUrl);
    }

    /**
     * 主动结束派工提交
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public String activeEndWorkOrderSubmit(MultiValueMap<String, Object> paramMap, UserInfo userInfo,
                                           ReqParam reqParam) {
        return this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, activeEndWorkOrderSubmitUrl);
    }
}