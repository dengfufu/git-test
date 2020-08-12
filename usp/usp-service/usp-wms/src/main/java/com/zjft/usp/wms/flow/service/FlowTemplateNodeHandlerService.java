package com.zjft.usp.wms.flow.service;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.wms.flow.dto.FlowTemplateNodeHandlerDto;
import com.zjft.usp.wms.flow.model.FlowTemplate;
import com.zjft.usp.wms.flow.model.FlowTemplateNode;
import com.zjft.usp.wms.flow.model.FlowTemplateNodeHandler;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 流程模板节点处理人表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-18
 */
public interface FlowTemplateNodeHandlerService extends IService<FlowTemplateNodeHandler> {

    /**
     * 根据流程模板节点ID查找处理人逻辑
     *
     * @param templateNodeId
     * @return
     */
    List<FlowTemplateNodeHandler> listByTemplateNodeId(Long templateNodeId);


    /**
     * 根据流程模板节点ID查找所有处理人列表(如果是指定角色，也要转为指定处理人)
     * @param templateNodeId
     * @param userInfo
     * @return
     */
    List<FlowTemplateNodeHandler> listAllAssignToPeople(Long templateNodeId, UserInfo userInfo);

    /**
     * 通过流程模板ID进行删除
     *
     * @param templateId
     */
    void removeByTemplateId(Long templateId);

    /**
     * 通过流程模板节点ID进行删除
     *
     * @param templateNodeId
     */
    void removeByTemplateNodeId(Long templateNodeId);

    /**
     * 修改流程模板节点处理人列表
     *
     * @param flowTemplate
     * @param handlerList
     */
    void modHandlers(FlowTemplate flowTemplate, List<FlowTemplateNodeHandler> handlerList);


    /**
     * 修改流程模板节点处理人列表
     *
     * @param handlerDto
     */
    void modHandlers(FlowTemplateNodeHandlerDto handlerDto);


}
