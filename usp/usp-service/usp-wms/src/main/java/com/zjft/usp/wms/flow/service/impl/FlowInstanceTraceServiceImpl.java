package com.zjft.usp.wms.flow.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.service.UasFeignService;
import com.zjft.usp.wms.flow.dto.FlowInstanceTraceDto;
import com.zjft.usp.wms.flow.model.FlowInstanceNode;
import com.zjft.usp.wms.flow.model.FlowInstanceTrace;
import com.zjft.usp.wms.flow.mapper.FlowInstanceTraceMapper;
import com.zjft.usp.wms.flow.service.FlowInstanceNodeService;
import com.zjft.usp.wms.flow.service.FlowInstanceTraceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 流程实例处理过程表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-11
 */
@Service
public class FlowInstanceTraceServiceImpl extends ServiceImpl<FlowInstanceTraceMapper, FlowInstanceTrace> implements FlowInstanceTraceService {

    @Autowired
    private FlowInstanceNodeService flowInstanceNodeService;
    @Resource
    private UasFeignService uasFeignService;

    @Override
    public List<FlowInstanceTraceDto> listSortBy(Long flowInstanceId, ReqParam reqParam) {
        QueryWrapper<FlowInstanceTrace> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("flow_instance_id", flowInstanceId);
        queryWrapper.orderByAsc("completed_time");
        List<FlowInstanceTrace> list = this.list(queryWrapper);
        List<FlowInstanceTraceDto> dtoList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(list)) {
            List<Long> userIdList = list.stream().map(flowInstanceTrace -> flowInstanceTrace.getCompletedBy()).collect(Collectors.toList());
            Map<Long, String> userIdAndNameMap = new HashMap<>(16);
            if (CollectionUtil.isNotEmpty(userIdList)) {
                userIdAndNameMap = this.uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList)).getData();
            }
            Map<Long, FlowInstanceNode> idAndInstanceNodeMap = this.flowInstanceNodeService.mapBy(flowInstanceId);

            for (FlowInstanceTrace flowInstanceTrace : list) {
                FlowInstanceTraceDto dto = new FlowInstanceTraceDto();
                BeanUtils.copyProperties(flowInstanceTrace, dto);
                // 节点名称和处理人姓名
                FlowInstanceNode flowInstanceNode = idAndInstanceNodeMap.get(flowInstanceTrace.getCurrentNodeId());
                dto.setTemplateNodeName(flowInstanceNode == null ? "" : flowInstanceNode.getTemplateNodeName());
                dto.setCompletedByName(flowInstanceTrace.getCompletedBy() == null ? "" : userIdAndNameMap.get(flowInstanceTrace.getCompletedBy()));
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    @Override
    public List<FlowInstanceTrace> listByFlowInstanceId(Long flowInstanceId) {
        QueryWrapper<FlowInstanceTrace> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("flow_instance_id", flowInstanceId);
        queryWrapper.orderByAsc("completed_time");
        return this.list(queryWrapper);
    }
}
