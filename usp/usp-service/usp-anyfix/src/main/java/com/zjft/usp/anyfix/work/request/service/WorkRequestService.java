package com.zjft.usp.anyfix.work.request.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.anyfix.work.request.model.WorkRequest;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工单服务请求表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-23
 */
public interface WorkRequestService extends IService<WorkRequest> {

    /**
     * 根据服务商和超时时间获取应自动确认服务的工单
     *
     * @param serviceCorp
     * @return
     */
    List<WorkDeal> listAutoServiceConfirm(Long serviceCorp);

    /**
     * 根据服务商和超时时间获取应自动确认费用的工单
     *
     * @param serviceCorp
     * @return
     */
    List<WorkDeal> listAutoFeeConfirm(Long serviceCorp);

    /**
     * 根据工单编号列表获取工单编号和服务请求的映射
     *
     * @param workIdList
     * @return
     */
    Map<Long, WorkRequest> mapByWorkIdList(List<Long> workIdList);

}
