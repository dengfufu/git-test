package com.zjft.usp.wms.flow.composite.impl;

import cn.hutool.core.date.DateUtil;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.wms.baseinfo.enums.EnabledEnum;
import com.zjft.usp.wms.flow.dto.FlowTemplateCopyDto;
import com.zjft.usp.wms.flow.dto.FlowTemplateDto;
import com.zjft.usp.wms.flow.dto.FlowTemplateNodeDto;
import com.zjft.usp.wms.flow.model.FlowTemplate;
import com.zjft.usp.wms.flow.model.FlowTemplateNode;
import com.zjft.usp.wms.flow.composite.FlowTemplateCompoService;
import com.zjft.usp.wms.flow.model.FlowTemplateNodeHandler;
import com.zjft.usp.wms.flow.service.FlowInstanceService;
import com.zjft.usp.wms.flow.service.FlowTemplateNodeHandlerService;
import com.zjft.usp.wms.flow.service.FlowTemplateNodeService;
import com.zjft.usp.wms.flow.service.FlowTemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 流程模板全局服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowTemplateCompoServiceImpl implements FlowTemplateCompoService {
    @Autowired
    public FlowTemplateService flowTemplateService;

    @Autowired
    public FlowTemplateNodeService flowTemplateNodeService;

    @Autowired
    public FlowTemplateNodeHandlerService flowTemplateNodeHandlerService;

    @Autowired
    public FlowInstanceService flowInstanceService;

    @Override
    public void addFlowTemplate(FlowTemplateDto flowTemplateDto, UserInfo userInfo) {

        Long flowTemplateId = KeyUtil.getId();
        FlowTemplate flowTemplate = new FlowTemplate();
        BeanUtils.copyProperties(flowTemplateDto, flowTemplate);
        flowTemplate.setId(flowTemplateId);

        List<FlowTemplateNode> flowTemplateNodeList = new ArrayList<>();
        List<FlowTemplateNodeHandler> flowTemplateNodeHandlerList = new ArrayList<>();

        for (FlowTemplateNodeDto flowTemplateNodeDto : flowTemplateDto.getFlowTemplateNodeDtoList()) {

            Long flowTemplateNodeId = KeyUtil.getId();
            FlowTemplateNode newEntity = new FlowTemplateNode();
            BeanUtils.copyProperties(flowTemplateNodeDto, newEntity);
            newEntity.setId(flowTemplateNodeId);
            newEntity.setTemplateId(flowTemplateId);
            flowTemplateNodeList.add(newEntity);

            List<FlowTemplateNodeHandler> handlerList = flowTemplateNodeDto.getHandlerList();
            if(handlerList != null){
                for (FlowTemplateNodeHandler handler : handlerList) {
                    handler.setId(KeyUtil.getId());
                    handler.setTemplateId(flowTemplateId);
                    handler.setTemplateNodeId(flowTemplateNodeId);
                    flowTemplateNodeHandlerList.add(handler);
                }
            }
        }

        add(flowTemplate, flowTemplateNodeList, flowTemplateNodeHandlerList, userInfo);
    }

    private void add(FlowTemplate flowTemplate, List<FlowTemplateNode> flowTemplateNodeList, List<FlowTemplateNodeHandler> flowTemplateNodeHandlerList, UserInfo userInfo) {


        if (flowTemplate == null) {
            throw new AppException("流程模板数据传输出现意外错误，请重试！");
        }

        if (flowTemplateNodeList == null || flowTemplateNodeList.isEmpty()) {
            throw new AppException("流程步骤不能为空，请重试！");
        }

        /**添加流程模板主表*/
        flowTemplateService.add(flowTemplate, userInfo);

        /**添加流程模板节点表*/
        flowTemplateNodeService.addNodes(flowTemplateNodeList);

        /**添加流程模板节点处理人表*/
        this.flowTemplateNodeHandlerService.saveBatch(flowTemplateNodeHandlerList);
    }

    @Override
    public void addByCopy(FlowTemplateCopyDto flowTemplateCopyDto, UserInfo userInfo) {
        /**复制出新流程模板*/
        FlowTemplate newFlowTemplate = new FlowTemplate();
        FlowTemplate oldFlowTemplate = this.flowTemplateService.getById(flowTemplateCopyDto.getSourceFlowTemplateId());
        if (oldFlowTemplate != null) {
            BeanUtils.copyProperties(oldFlowTemplate, newFlowTemplate);
        }

        /**设置新模板属性值*/
        newFlowTemplate.setId(KeyUtil.getId());
        newFlowTemplate.setName(flowTemplateCopyDto.getNewFlowTemplateName());
        newFlowTemplate.setEnabled(EnabledEnum.YES.getCode());
        newFlowTemplate.setCreateBy(userInfo.getUserId());
        newFlowTemplate.setCreateTime(DateUtil.date().toTimestamp());
        newFlowTemplate.setUpdateBy(0L);
        newFlowTemplate.setUpdateTime(DateUtil.date().toTimestamp());
        this.flowTemplateService.save(newFlowTemplate);

        /**设置旧模板属性值*/
        oldFlowTemplate.setEnabled(flowTemplateCopyDto.getSourceEnabled());
        oldFlowTemplate.setUpdateBy(userInfo.getUserId());
        oldFlowTemplate.setUpdateTime(DateUtil.date().toTimestamp());
        this.flowTemplateService.updateById(oldFlowTemplate);

        /**设置新模板流程节点列表*/
        List<FlowTemplateNode> newNodeList = new ArrayList<>();
        List<FlowTemplateNodeHandler> newNodeHandlerList = new ArrayList<>();
        List<FlowTemplateNode> oldNodeList = this.flowTemplateNodeService.listBy(flowTemplateCopyDto.getSourceFlowTemplateId());
        for (FlowTemplateNode oldNode : oldNodeList) {

            Long oldTemplateNodeId = oldNode.getId();
            Long newNodeId = KeyUtil.getId();
            Long newTemplateId = newFlowTemplate.getId();

            FlowTemplateNode newNode = new FlowTemplateNode();
            BeanUtils.copyProperties(oldNode, newNode);
            newNode.setId(newNodeId);
            newNode.setTemplateId(newTemplateId);
            newNodeList.add(newNode);

            /**设置节点处理人列表*/
            List<FlowTemplateNodeHandler> oldHandlers = this.flowTemplateNodeHandlerService.listByTemplateNodeId(oldTemplateNodeId);
            for (FlowTemplateNodeHandler oldHandler : oldHandlers) {
                FlowTemplateNodeHandler newHandler = new FlowTemplateNodeHandler();
                BeanUtils.copyProperties(oldHandler, newHandler);
                newHandler.setId(KeyUtil.getId());
                newHandler.setTemplateId(newTemplateId);
                newHandler.setTemplateNodeId(newNodeId);
                newNodeHandlerList.add(newHandler);
            }
        }

        this.flowTemplateNodeService.saveBatch(newNodeList);
        this.flowTemplateNodeHandlerService.saveBatch(newNodeHandlerList);

    }

    @Override
    public void modFlowTemplateNode(FlowTemplateDto flowTemplateDto, UserInfo userInfo) {
        /**前端数据接口转换*/
        FlowTemplate flowTemplate = new FlowTemplate();
        BeanUtils.copyProperties(flowTemplateDto, flowTemplate);
        Long flowTemplateId = flowTemplate.getId();

        List<FlowTemplateNode> flowTemplateNodeList = new ArrayList<>();
        List<FlowTemplateNodeHandler> flowTemplateNodeHandlerList = new ArrayList<>();

        for (FlowTemplateNodeDto flowTemplateNodeDto : flowTemplateDto.getFlowTemplateNodeDtoList()) {

            Long flowTemplateNodeId = KeyUtil.getId();
            FlowTemplateNode newEntity = new FlowTemplateNode();
            BeanUtils.copyProperties(flowTemplateNodeDto, newEntity);
            newEntity.setId(flowTemplateNodeId);
            newEntity.setTemplateId(flowTemplateId);
            flowTemplateNodeList.add(newEntity);

            List<FlowTemplateNodeHandler> handlerList = flowTemplateNodeDto.getHandlerList();
            if(handlerList != null){
                for (FlowTemplateNodeHandler handler : handlerList) {
                    handler.setId(KeyUtil.getId());
                    handler.setTemplateId(flowTemplateId);
                    handler.setTemplateNodeId(flowTemplateNodeId);
                    flowTemplateNodeHandlerList.add(handler);
                }
            }
        }

        this.mod(flowTemplate, flowTemplateNodeList, flowTemplateNodeHandlerList, userInfo);
    }

    @Override
    public void modFlowTemplateBaseInfo(FlowTemplate flowTemplate, UserInfo userInfo) {
        /**修改流程模板主表*/
        flowTemplateService.mod(flowTemplate, userInfo);
    }

    private void mod(FlowTemplate flowTemplate,
                     List<FlowTemplateNode> flowTemplateNodeList,
                     List<FlowTemplateNodeHandler> flowTemplateNodeHandlerList,
                     UserInfo userInfo) {
        if (flowTemplate == null) {
            throw new AppException("流程模板数据传输出现意外错误，请重试！");
        }

        if (flowTemplateNodeList == null || flowTemplateNodeList.isEmpty()) {
            throw new AppException("流程步骤不能为空，请重试！");
        }

        /**如果有流程实例，当前执行禁止修改策略。*/
        boolean isRelationBy = this.flowInstanceService.isRelationBy(flowTemplate.getId());
        if (isRelationBy) {
            throw new AppException("该流程模板已有流程实例，不能修改，只能改为禁用，请检查！");
        }

        /**修改流程模板主表*/
        flowTemplateService.mod(flowTemplate, userInfo);

        /**修改流程模板节点表*/
        flowTemplateNodeService.modNodes(flowTemplate, flowTemplateNodeList);

        /**修改流程模板节点处理人表*/
        flowTemplateNodeHandlerService.modHandlers(flowTemplate, flowTemplateNodeHandlerList);
    }

    @Override
    public void delFlowTemplateBy(Long flowTemplateId) {
        boolean isRelationBy = this.flowInstanceService.isRelationBy(flowTemplateId);
        if (isRelationBy) {
            throw new AppException("该流程模板已有流程实例，不能删除，只能修改为禁用，请检查！");
        }

        this.flowTemplateService.removeById(flowTemplateId);
        this.flowTemplateNodeService.removeByTemplateId(flowTemplateId);
        this.flowTemplateNodeHandlerService.removeByTemplateId(flowTemplateId);

    }

    @Override
    public FlowTemplateDto getFlowTemplateById(Long flowTemplateId) {
        FlowTemplateDto dto = new FlowTemplateDto();

        /**流程模板基础信息*/
        FlowTemplate findFlowTemplate = this.flowTemplateService.getById(flowTemplateId);
        if (findFlowTemplate != null) {
            BeanUtils.copyProperties(findFlowTemplate, dto);
        }

        /**流程模板节点及其处理人*/
        List<FlowTemplateNodeDto> flowTemplateNodeDtoList = new ArrayList<>();

        List<FlowTemplateNode> nodeList = this.flowTemplateNodeService.listBy(flowTemplateId);
        for (FlowTemplateNode flowTemplateNode : nodeList) {
            /**复制节点一行*/
            FlowTemplateNodeDto flowTemplateNodeDto = new FlowTemplateNodeDto();
            BeanUtils.copyProperties(flowTemplateNode, flowTemplateNodeDto);

            /**获得节点处理人列表*/
            List<FlowTemplateNodeHandler> listHandlers = flowTemplateNodeHandlerService.listByTemplateNodeId(flowTemplateNode.getId());
            flowTemplateNodeDto.setHandlerList(listHandlers);

            flowTemplateNodeDtoList.add(flowTemplateNodeDto);
        }
        dto.setFlowTemplateNodeDtoList(flowTemplateNodeDtoList);

        return dto;
    }

    private List<FlowTemplateNode> getFlowTemplateNodeList(List<FlowTemplateNodeDto> flowTemplateNodeDtoList) {
        List<FlowTemplateNode> flowTemplateNodeList = new ArrayList<>();
        for (FlowTemplateNodeDto dto : flowTemplateNodeDtoList) {
            FlowTemplateNode newEntity = new FlowTemplateNode();
            BeanUtils.copyProperties(dto, newEntity);
            flowTemplateNodeList.add(newEntity);
        }
        return flowTemplateNodeList;
    }


}
