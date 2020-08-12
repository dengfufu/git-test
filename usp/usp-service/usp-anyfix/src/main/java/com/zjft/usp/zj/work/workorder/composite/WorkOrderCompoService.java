package com.zjft.usp.zj.work.workorder.composite;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.work.workorder.dto.WorkOrderDto;
import com.zjft.usp.zj.work.workorder.filter.WorkOrderFilter;

import java.util.Map;

/**
 * 派工单聚合接口
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-13 17:30
 **/
public interface WorkOrderCompoService {
    /**
     * 查询我的工单列表
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param workOrderFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    ListWrapper<WorkOrderDto> listMyWork(WorkOrderFilter workOrderFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查看派工单详情
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param workOrderFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    Map findWorkOrderDetail(WorkOrderFilter workOrderFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入新建派工页面
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param workOrderFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    Map<String, Object> addWorkOrder(WorkOrderFilter workOrderFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 新建派工提交
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param workOrderDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    void addWorkOrderSubmit(WorkOrderDto workOrderDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入接受派工页面
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param workOrderFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    Map<String, Object> acceptWorkOrder(WorkOrderFilter workOrderFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 接受派工提交
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param workOrderDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    void acceptWorkOrderSubmit(WorkOrderDto workOrderDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入拒绝派工页面
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param workOrderFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    Map<String, Object> refuseWorkOrder(WorkOrderFilter workOrderFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 拒绝派工提交
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param workOrderDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    void refuseWorkOrderSubmit(WorkOrderDto workOrderDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入工单预约页面
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param workOrderFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    Map<String, Object> preBook(WorkOrderFilter workOrderFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 工单预约提交
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param workOrderDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    void preBookSubmit(WorkOrderDto workOrderDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 重进入重新预约页面
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param workOrderFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    Map<String, Object> renewPreBook(WorkOrderFilter workOrderFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 重新预约提交
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param workOrderDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    void renewPreBookSubmit(WorkOrderDto workOrderDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入退回主管页面
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param workOrderFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    Map<String, Object> returnManager(WorkOrderFilter workOrderFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 退回主管提交
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param workOrderDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    void returnManagerSubmit(WorkOrderDto workOrderDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入主动结束派工页面
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param workOrderFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    Map<String, Object> activeEndWorkOrder(WorkOrderFilter workOrderFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 主动结束派工提交
     *
     * @author Qiugm
     * @date 2020-03-19
     * @param workOrderDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    void activeEndWorkOrderSubmit(WorkOrderDto workOrderDto, UserInfo userInfo, ReqParam reqParam);
}
