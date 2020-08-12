package com.zjft.usp.wms.flow.service;

import com.zjft.usp.wms.flow.model.FlowTemplate;
import com.zjft.usp.wms.flow.model.FlowTemplateNode;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 流程模板节点表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface FlowTemplateNodeService extends IService<FlowTemplateNode> {

    /**
     * 添加流程模板节点列表
     *
     * @param flowTemplateNodeList 流程模板节点对象列表
     */
    void addNodes(List<FlowTemplateNode> flowTemplateNodeList);

    /**
     * 修改流程模板节点列表
     * @param flowTemplate
     * @param flowTemplateNodeList
     */
    void modNodes(FlowTemplate flowTemplate, List<FlowTemplateNode> flowTemplateNodeList);

    /**
     * 根据流程模板ID删除流程模板节点
     *
     * @param templateId
     */
    void removeByTemplateId(Long templateId);

    /**
     * 获得表单模板节点对象
     *
     * @param formMainId 表单模板ID
     * @return
     */
    FlowTemplateNode getByFormMainId(Long formMainId);

    /**
     * 判断是否存在表单模板节点对象
     *
     * @param formMainId 表单模板ID
     * @return
     */
    boolean isExistByFormMainId(Long formMainId);

    /**
     * 获得已启用的流程模板节点对象列表
     *
     * @param flowTemplateId 流程模板ID
     * @return
     */
    List<FlowTemplateNode> listEnabledBy(Long flowTemplateId);

    /**
     * 获得流程模板节点对象列表
     *
     * @param flowTemplateId
     * @return
     */
    List<FlowTemplateNode> listBy(Long flowTemplateId);
}
