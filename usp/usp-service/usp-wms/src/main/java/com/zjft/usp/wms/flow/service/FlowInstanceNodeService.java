package com.zjft.usp.wms.flow.service;

import com.zjft.usp.wms.flow.dto.FlowInstanceNodeEndDto;
import com.zjft.usp.wms.flow.model.FlowInstanceNode;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 流程实例节点处理记录表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface FlowInstanceNodeService extends IService<FlowInstanceNode> {

    /**
     * 查找所有流程节点实例列表(按照sort_no 升序排序)
     *
     * @param flowInstanceId
     * @return
     */
    List<FlowInstanceNode> listAllBy(Long flowInstanceId);

    /**
     * 获取节点id和节点的映射
     *
     * @param flowInstanceId
     * @return
     */
    Map<Long, FlowInstanceNode> mapBy(Long flowInstanceId);

    /**
     * 查找当前节点之后的所有节点实例列表
     *
     * @param currentFlowInstanceNode
     * @return
     * @throws Exception
     */
    List<FlowInstanceNode> listAfterCurrentNodeSortBy(FlowInstanceNode currentFlowInstanceNode);

    /**
     * 查找上一个待处理的流程实例节点
     *
     * @param currentFlowInstanceNode
     * @return
     * @throws Exception
     */
    FlowInstanceNode getTodoPreviousNode(FlowInstanceNode currentFlowInstanceNode);

    /**
     * 获得下一个待处理的流程实例节点
     *
     * @param currentFlowInstanceNode
     * @return
     * @throws Exception
     */
    FlowInstanceNode getTodoNextNode(FlowInstanceNode currentFlowInstanceNode);

    /**
     * 获得下一个待处理的流程实例节点
     *
     * @param flowInstanceId
     * @param currentNodeSortNo
     * @return
     * @throws Exception
     */
    FlowInstanceNode getTodoNextNode(Long flowInstanceId, Integer currentNodeSortNo);

    /**
     * 清除流程结点结束标记
     *
     * @param currentNodeId
     */
    void clearCompleted(Long currentNodeId);

    /**
     * 设置流程节点结束标记
     * @param flowNodeEndDto
     */
    void updateCompleted(FlowInstanceNodeEndDto flowNodeEndDto);

    /**
     * 根据流程实例Id删除流程节点
     *
     * @author Qiugm
     * @date 2019-11-20
     * @param flowInstanceId
     * @return boolean
     */
    boolean removeByFlowId(Long flowInstanceId);
}
