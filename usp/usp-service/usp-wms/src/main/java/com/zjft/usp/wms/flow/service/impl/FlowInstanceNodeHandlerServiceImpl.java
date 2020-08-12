package com.zjft.usp.wms.flow.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.wms.flow.dto.FlowInstanceNodeHandlerDto;
import com.zjft.usp.wms.flow.enums.SubmitEnum;
import com.zjft.usp.wms.flow.model.FlowInstanceNodeHandler;
import com.zjft.usp.wms.flow.mapper.FlowInstanceNodeHandlerMapper;
import com.zjft.usp.wms.flow.service.FlowInstanceNodeHandlerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 流程实例节点处理人表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-18
 */
@Service
public class FlowInstanceNodeHandlerServiceImpl extends ServiceImpl<FlowInstanceNodeHandlerMapper, FlowInstanceNodeHandler> implements FlowInstanceNodeHandlerService {


    @Override
    public void clearByInstanceNodeId(Long instanceNodeId) {
        QueryWrapper<FlowInstanceNodeHandler> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("instance_node_id", instanceNodeId);
        List<FlowInstanceNodeHandler> list = this.list(queryWrapper);
        if (list != null && !list.isEmpty()) {
            for (FlowInstanceNodeHandler handler : list) {
                FlowInstanceNodeHandler newHandler = new FlowInstanceNodeHandler();
                newHandler.setId(handler.getId());
                newHandler.setDoBy(0L);
                newHandler.setDoTime(null);
                newHandler.setIsSubmit(SubmitEnum.NO.getCode());
                this.updateById(newHandler);
            }
        }
    }

    @Override
    public FlowInstanceNodeHandler getBy(Long instanceNodeId, Long userId) {
        QueryWrapper<FlowInstanceNodeHandler> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("instance_node_id", instanceNodeId);
        queryWrapper.eq("assigned_to_by", userId);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public List<FlowInstanceNodeHandler> listByInstanceNodeId(Long instanceNodeId) {
        QueryWrapper<FlowInstanceNodeHandler> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("instance_node_id", instanceNodeId);
        List<FlowInstanceNodeHandler> list = this.list(queryWrapper);
        return list;
    }

    @Override
    public Map<Long, List<Long>> mapCurAuditUserList(List<Long> flowInstanceIdList) {
        Map<Long, List<Long>> map = new HashMap<>();
        if (CollectionUtil.isEmpty(flowInstanceIdList)) {
            return map;
        }
        List<FlowInstanceNodeHandlerDto> handlerDtoList = this.baseMapper.listCurAssignedTo(flowInstanceIdList);
        if (CollectionUtil.isNotEmpty(handlerDtoList)) {
            handlerDtoList.forEach(handlerDto -> {
                if (map.containsKey(handlerDto.getFlowInstanceId())) {
                    List<Long> userIdList = map.get(handlerDto.getFlowInstanceId());
                    userIdList.add(handlerDto.getAssignedToBy());
                    map.put(handlerDto.getFlowInstanceId(), userIdList);
                } else {
                    List<Long> userIdList = new ArrayList<>();
                    userIdList.add(handlerDto.getAssignedToBy());
                    map.put(handlerDto.getFlowInstanceId(), userIdList);
                }
            });
        }
        return map;
    }

}
