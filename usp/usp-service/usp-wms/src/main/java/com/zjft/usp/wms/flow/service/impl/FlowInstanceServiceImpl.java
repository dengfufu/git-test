package com.zjft.usp.wms.flow.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.wms.baseinfo.enums.EnabledEnum;
import com.zjft.usp.wms.flow.enums.CompletedEnum;
import com.zjft.usp.wms.flow.model.FlowInstance;
import com.zjft.usp.wms.flow.mapper.FlowInstanceMapper;
import com.zjft.usp.wms.flow.model.FlowTemplate;
import com.zjft.usp.wms.flow.service.FlowInstanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 流程实例表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowInstanceServiceImpl extends ServiceImpl<FlowInstanceMapper, FlowInstance> implements FlowInstanceService {

    @Override
    public void endFlow(Long flowInstanceId, UserInfo userInfo) {
        FlowInstance updateFlowInstance = new FlowInstance();
        updateFlowInstance.setId(flowInstanceId);
        updateFlowInstance.setCurrentNodeId(0L);
        updateFlowInstance.setIsEnd(CompletedEnum.YES.getCode());
        updateFlowInstance.setEndBy(userInfo.getUserId());
        updateFlowInstance.setEndTime(DateUtil.date().toTimestamp());
        super.updateById(updateFlowInstance);
    }

    @Override
    public void updateCurrentNodeId(Long flowInstanceId, Long currentNodeId) {
        FlowInstance updateFlowInstance = new FlowInstance();
        updateFlowInstance.setId(flowInstanceId);
        updateFlowInstance.setCurrentNodeId(currentNodeId);
        super.updateById(updateFlowInstance);
    }

    @Override
    public List<FlowInstance> listByTemplateId(Long templateId) {
        QueryWrapper<FlowInstance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template_id", templateId);
        return this.list(queryWrapper);
    }

    private FlowInstance getOneByTemplateId(Long templateId) {
        QueryWrapper<FlowInstance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template_id", templateId);
        queryWrapper.last("LIMIT 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean isRelationBy(Long templateId) {
        FlowInstance getFlowInstance = this.getOneByTemplateId(templateId);
        return getFlowInstance != null ? true : false;
    }
}
