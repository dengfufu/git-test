package com.zjft.usp.anyfix.work.transfer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.work.transfer.filter.WorkTransferFilter;
import com.zjft.usp.anyfix.work.transfer.model.WorkTransfer;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 工单流转表 服务类
 * </p>
 *
 * @author zphu
 * @since 2019-09-25
 */
public interface WorkTransferService extends IService<WorkTransfer> {

    /**
     * 查询工单流转记录(OA报销专用)
     * @param workTransferFilter
     * @return
     */
    List<WorkTransfer> queryWorkTransferForCost(WorkTransferFilter workTransferFilter);

    /**
     * 根据workId查询工单流转表详情
     * @date 2020/3/15
     * @param workIdSet
     * @return java.util.List<com.zjft.usp.anyfix.work.transfer.model.WorkTransfer>
     */
    List<WorkTransfer> queryWorkTransfer(Set<Long> workIdSet);
}
