package com.zjft.usp.zj.work.workorder.strategy;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import org.springframework.util.MultiValueMap;

/**
 * 老平台派工单策略接口
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-23 16:47
 **/
public interface WorkOrderStrategy {
    /**
     * 进入新建派工页面
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    String addWorkOrder(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 新建派工提交
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    String addWorkOrderSubmit(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入接受派工页面
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    String acceptWorkOrder(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 接受派工提交
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    String acceptWorkOrderSubmit(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入拒绝派工页面
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    String refuseWorkOrder(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 拒绝派工提交
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    String refuseWorkOrderSubmit(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入工单预约页面
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    String preBook(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 工单预约提交
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    String preBookSubmit(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 重进入重新预约页面
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    String renewPreBook(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 重新预约提交
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    String renewPreBookSubmit(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入退回主管页面
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    String returnManager(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 退回主管提交
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    String returnManagerSubmit(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入主动结束派工页面
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    String activeEndWorkOrder(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 主动结束派工提交
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    String activeEndWorkOrderSubmit(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);
}

