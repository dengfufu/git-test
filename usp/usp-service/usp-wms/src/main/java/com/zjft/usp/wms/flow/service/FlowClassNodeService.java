package com.zjft.usp.wms.flow.service;

import com.zjft.usp.wms.flow.dto.FlowClassNodeDto;
import com.zjft.usp.wms.flow.model.FlowClassNode;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 流程类型节点表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-08
 */
public interface FlowClassNodeService extends IService<FlowClassNode> {

    /**
     * 获得流程业务类型节点定义列表
     * @param flowClassNodeDto
     * @return
     * @throws Exception
     */
    List<FlowClassNodeDto> listBy(FlowClassNodeDto flowClassNodeDto);


    /**
     * 获得流程业务类型节点定义列表
     * @param corpId
     * @param largeClassId
     * @param smallClassId
     * @return
     * @throws Exception
     */
    List<FlowClassNodeDto> listBy(long corpId, int largeClassId, int smallClassId);
}
