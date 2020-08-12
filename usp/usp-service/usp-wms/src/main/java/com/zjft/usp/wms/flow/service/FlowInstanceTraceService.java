package com.zjft.usp.wms.flow.service;

import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.flow.dto.FlowInstanceTraceDto;
import com.zjft.usp.wms.flow.model.FlowInstanceTrace;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 流程实例处理过程表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-11
 */
public interface FlowInstanceTraceService extends IService<FlowInstanceTrace> {

    /**
     * 获得流程实例处理列表(按照处理时间排序)
     *
     * @param flowInstanceId
     * @return
     */
    List<FlowInstanceTraceDto> listSortBy(Long flowInstanceId, ReqParam reqParam);

    /**
     * 获得流程实例处理列表(按照处理时间排序)
     *
     * @param flowInstanceId
     * @return
     */
    List<FlowInstanceTrace> listByFlowInstanceId(Long flowInstanceId);
}
