package com.zjft.usp.wms.flow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.wms.baseinfo.enums.EnabledEnum;
import com.zjft.usp.wms.flow.enums.NodeTypeEnum;
import com.zjft.usp.wms.flow.model.FlowTemplate;
import com.zjft.usp.wms.flow.model.FlowTemplateNode;
import com.zjft.usp.wms.flow.mapper.FlowTemplateNodeMapper;
import com.zjft.usp.wms.flow.service.FlowTemplateNodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 流程模板节点表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowTemplateNodeServiceImpl extends ServiceImpl<FlowTemplateNodeMapper, FlowTemplateNode> implements FlowTemplateNodeService {


    @Override
    public void addNodes(List<FlowTemplateNode> flowTemplateNodeList) {
        if(flowTemplateNodeList == null){
            throw new AppException("流程模板步骤数据传输出现意外错误，请重试！");
        }

        if (flowTemplateNodeList.isEmpty()) {
            throw new AppException("流程模板步骤不能为空，请重试！");
        }

        Map<Integer, Integer> nodeTypeIdAndCountMap = new HashMap<>(128);

        for (FlowTemplateNode entity : flowTemplateNodeList) {

            int nodeTypeId = entity.getNodeType();

            if (nodeTypeIdAndCountMap.containsKey(nodeTypeId)) {
                Integer count = nodeTypeIdAndCountMap.get(nodeTypeId);
                if (count == null || count == 0) {
                    nodeTypeIdAndCountMap.put(nodeTypeId, 1);
                } else {
                    int newCount = count + 1;
                    nodeTypeIdAndCountMap.put(nodeTypeId, newCount);
                }
            } else {
                nodeTypeIdAndCountMap.put(nodeTypeId, 1);
            }
        }

        /**检查flowTemplateNodeList的合法性*/
        for (Integer key : nodeTypeIdAndCountMap.keySet()) {
            Integer countInteger = nodeTypeIdAndCountMap.get(key);
            if (countInteger != null && countInteger > 1) {
                if (NodeTypeEnum.COMMON_APPROVAL.getCode() == key || NodeTypeEnum.COUNTERSIGN_APPROVAL.getCode() == key) {
                    /**审批节点或者会签审批节点允许多个*/
                } else {
                    throw new AppException("流程模板步骤定义错误，只有普通审批节点或者会签审批节点才能定义多个，请重试！");
                }
            }
        }
        super.saveBatch(flowTemplateNodeList);
    }

    @Override
    public void modNodes(FlowTemplate flowTemplate, List<FlowTemplateNode> flowTemplateNodeList) {
        if (flowTemplateNodeList == null) {
            throw new AppException("流程模板节点列表数据传输出现意外错误，请重试！");
        }

        /**先删除*/
        this.removeByTemplateId(flowTemplate.getId());

        /**再保存*/
        this.saveBatch(flowTemplateNodeList);

    }

    @Override
    public void removeByTemplateId(Long templateId) {
        QueryWrapper<FlowTemplateNode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template_id", templateId);
        super.remove(queryWrapper);
    }

    @Override
    public FlowTemplateNode getByFormMainId(Long formMainId) {
        QueryWrapper<FlowTemplateNode> queryWrapper = new QueryWrapper<>();

        if (LongUtil.isNotZero(formMainId)) {
            queryWrapper.eq("form_main_id", formMainId);
        }
        queryWrapper.last("LIMIT 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean isExistByFormMainId(Long formMainId) {
        FlowTemplateNode findEntity = this.getByFormMainId(formMainId);
        return findEntity != null ? true : false;
    }

    @Override
    public List<FlowTemplateNode> listEnabledBy(Long flowTemplateId) {
        QueryWrapper<FlowTemplateNode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template_id", flowTemplateId);
        queryWrapper.eq("enabled", EnabledEnum.YES.getCode());
        queryWrapper.orderByAsc("sort_no");
        return this.list(queryWrapper);
    }

    @Override
    public List<FlowTemplateNode> listBy(Long flowTemplateId) {
        QueryWrapper<FlowTemplateNode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template_id", flowTemplateId);
        queryWrapper.orderByAsc("sort_no");
        return this.list(queryWrapper);
    }
}
