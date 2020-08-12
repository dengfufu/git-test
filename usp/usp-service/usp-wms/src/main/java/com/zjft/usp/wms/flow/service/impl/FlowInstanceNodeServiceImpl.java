package com.zjft.usp.wms.flow.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.wms.flow.dto.FlowInstanceNodeEndDto;
import com.zjft.usp.wms.flow.enums.CompletedEnum;
import com.zjft.usp.wms.flow.mapper.FlowInstanceNodeMapper;
import com.zjft.usp.wms.flow.model.FlowInstanceNode;
import com.zjft.usp.wms.flow.service.FlowInstanceNodeHandlerService;
import com.zjft.usp.wms.flow.service.FlowInstanceNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 流程实例节点处理记录表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class FlowInstanceNodeServiceImpl extends ServiceImpl<FlowInstanceNodeMapper, FlowInstanceNode> implements FlowInstanceNodeService {

    @Autowired
    private FlowInstanceNodeHandlerService flowInstanceNodeHandlerService;

    @Override
    public List<FlowInstanceNode> listAllBy(Long flowInstanceId) {
        QueryWrapper<FlowInstanceNode> queryWrapper = new QueryWrapper<>();
        /**流程实例ID*/
        queryWrapper.eq("flow_instance_id", flowInstanceId);
        /**升序*/
        queryWrapper.orderByAsc("sort_no");
        return this.list(queryWrapper);
    }

    @Override
    public Map<Long, FlowInstanceNode> mapBy(Long flowInstanceId) {
        List<FlowInstanceNode> list = this.listAllBy(flowInstanceId);
        Map<Long, FlowInstanceNode> map = new HashMap<>(16);
        if(CollectionUtil.isNotEmpty(list)) {
            map = list.stream().collect(Collectors.toMap(flowInstanceNode -> flowInstanceNode.getId(), flowInstanceNode -> flowInstanceNode));
        }
        return map;
    }

    @Override
    public List<FlowInstanceNode> listAfterCurrentNodeSortBy(FlowInstanceNode currentNode) {
        QueryWrapper<FlowInstanceNode> queryWrapper = new QueryWrapper<>();
        /**流程实例ID*/
        queryWrapper.eq("flow_instance_id", currentNode.getFlowInstanceId());
        /**排序号大于当前节点排序号*/
        queryWrapper.gt("sort_no", currentNode.getSortNo());
        /**升序*/
        queryWrapper.orderByAsc("sort_no");
        return this.list(queryWrapper);
    }

    @Override
    public FlowInstanceNode getTodoPreviousNode(FlowInstanceNode currentNode) {
        QueryWrapper<FlowInstanceNode> queryWrapper = new QueryWrapper<>();
        /**流程实例ID*/
        queryWrapper.eq("flow_instance_id", currentNode.getFlowInstanceId());
        /**排序号小于当前节点排序号*/
        queryWrapper.lt("sort_no", currentNode.getSortNo());
        /**降序*/
        queryWrapper.orderByDesc("sort_no");
        List<FlowInstanceNode> list = this.list(queryWrapper);
        return list != null && !list.isEmpty() ? list.get(0) : null;

    }

    @Override
    public FlowInstanceNode getTodoNextNode(FlowInstanceNode currentNode) {
        return this.getTodoNextNode(currentNode.getFlowInstanceId(), currentNode.getSortNo());
    }

    @Override
    public FlowInstanceNode getTodoNextNode(Long flowInstanceId, Integer currentNodeSortNo) {
        QueryWrapper<FlowInstanceNode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("flow_instance_id", flowInstanceId);
        queryWrapper.gt("sort_no", currentNodeSortNo);
        queryWrapper.orderByAsc("sort_no");
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);

    }

    @Override
    public void clearCompleted(Long currentInstanceNodeId) {
        /**重置节点基本信息*/
        FlowInstanceNode clearCompletedNode = new FlowInstanceNode();
        clearCompletedNode.setId(currentInstanceNodeId);
        clearCompletedNode.setCompletedEndTypeId(0);
        clearCompletedNode.setCompletedBy(0L);
        clearCompletedNode.setCompletedTime(DateUtil.date().toTimestamp());
        clearCompletedNode.setCompletedDescription("");
        clearCompletedNode.setCompleted(CompletedEnum.NO.getCode());
        super.updateById(clearCompletedNode);

        /**重置节点处理人*/
        flowInstanceNodeHandlerService.clearByInstanceNodeId(currentInstanceNodeId);

    }

    @Override
    public void updateCompleted(FlowInstanceNodeEndDto flowNodeEndDto) {
        FlowInstanceNode updateCompletedNode = new FlowInstanceNode();
        updateCompletedNode.setId(flowNodeEndDto.getCurrentNodeId());
        updateCompletedNode.setCompletedEndTypeId(flowNodeEndDto.getCompletedNodeEndTypeId());
        updateCompletedNode.setCompletedBy(flowNodeEndDto.getCompletedBy());
        updateCompletedNode.setCompletedTime(DateUtil.date().toTimestamp());
        updateCompletedNode.setCompletedDescription(flowNodeEndDto.getCompletedDescription());
        updateCompletedNode.setCompleted(CompletedEnum.YES.getCode());
        super.updateById(updateCompletedNode);
    }

    /**
     * 根据流程实例Id删除流程节点
     *
     * @author Qiugm
     * @date 2019-11-20
     * @param flowInstanceId
     * @return boolean
     */
    @Override
    public boolean removeByFlowId(Long flowInstanceId) {
        return this.remove(new QueryWrapper<FlowInstanceNode>().eq("flow_instance_id", flowInstanceId));
    }

}
