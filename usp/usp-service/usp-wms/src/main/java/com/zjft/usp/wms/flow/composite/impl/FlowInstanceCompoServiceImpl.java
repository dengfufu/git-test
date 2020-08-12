package com.zjft.usp.wms.flow.composite.impl;

import cn.hutool.core.date.DateUtil;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.wms.flow.dto.FlowInstanceNodeDealResultDto;
import com.zjft.usp.wms.flow.composite.FlowInstanceCompoService;
import com.zjft.usp.wms.flow.enums.CounterSignResultEnum;
import com.zjft.usp.wms.flow.enums.FlowEndEnum;
import com.zjft.usp.wms.flow.model.*;
import com.zjft.usp.wms.flow.service.*;
import com.zjft.usp.wms.flow.strategy.endtype.INodeEndStrategy;
import com.zjft.usp.wms.flow.strategy.endtype.factory.INodeEndStrategyFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: JFZOU
 * @Date: 2019-11-13 17:31
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class FlowInstanceCompoServiceImpl implements FlowInstanceCompoService {

    @Autowired
    private FlowTemplateService flowTemplateService;

    @Autowired
    private FlowTemplateNodeService flowTemplateNodeService;

    @Autowired
    private FlowTemplateNodeHandlerService flowTemplateNodeHandlerService;

    @Autowired
    private FlowInstanceService flowInstanceService;

    @Autowired
    private FlowInstanceNodeService flowInstanceNodeService;

    @Autowired
    private FlowInstanceNodeHandlerService flowInstanceNodeHandlerService;

    @Autowired
    private FlowInstanceTraceService flowInstanceTraceService;

    @Autowired
    private INodeEndStrategyFactory nodeEndStrategyFactory;

    /**
     * 同时创建多个流程实例
     *
     * @param instanceNumber
     * @param corpId
     * @param largeClassId
     * @param smallClassId
     * @param userInfo
     * @return
     */
    @Override
    public List<Long> createInstanceList(Integer instanceNumber, Long corpId, Integer largeClassId, Integer smallClassId, UserInfo userInfo) {
        if (instanceNumber == null) {
            instanceNumber = new Integer(0);
        }
        List<Long> createInstanceList = new ArrayList<>();

        for (int i = 0; i < instanceNumber.intValue(); i++) {
            Long instanceId = this.createInstance(corpId, largeClassId, smallClassId, userInfo);
            createInstanceList.add(instanceId);
        }

        return createInstanceList;
    }

    @Override
    public Long createInstanceBySplit(Long sourceInstanceId, UserInfo userInfo) {
        FlowInstance oldFlowInstance = this.flowInstanceService.getById(sourceInstanceId);
        if (oldFlowInstance == null) {
            throw new AppException("拆单自动创建流程实例失败，当前流程实例已不存在！");
        }

        List<FlowInstanceNode> oldFlowInstanceNodes = this.flowInstanceNodeService.listAllBy(sourceInstanceId);
        if (oldFlowInstanceNodes == null) {
            throw new AppException("拆单自动创建流程实例失败，当前流程实例步骤没有定义！");
        }

        /**新流程实例ID*/
        Long newFlowInstanceId = KeyUtil.getId();

        /**新节点列表*/
        List<FlowInstanceNode> newInstanceNodeList = new ArrayList<>();
        /**新节点处理人列表*/
        List<FlowInstanceNodeHandler> newInstanceNodeHandlerList = new ArrayList<>();
        /**新旧节点ID映射*/
        Map<Long, Long> oldNodeIdAndNewNodeIdMap = new HashMap<>(256);

        for (FlowInstanceNode oldNode : oldFlowInstanceNodes) {
            Long newNodeId = KeyUtil.getId();

            /**获得新旧节点ID映射*/
            oldNodeIdAndNewNodeIdMap.put(oldNode.getId(), newNodeId);

            /**创建新节点列表*/
            FlowInstanceNode newNode = new FlowInstanceNode();
            BeanUtils.copyProperties(oldNode, newNode);
            newNode.setId(newNodeId);
            newNode.setFlowInstanceId(newFlowInstanceId);
            newInstanceNodeList.add(newNode);

            /**创建节点处理人列表*/
            List<FlowInstanceNodeHandler> listByInstanceNodeId = this.flowInstanceNodeHandlerService.listByInstanceNodeId(oldNode.getId());
            for (FlowInstanceNodeHandler oldHandler : listByInstanceNodeId) {
                FlowInstanceNodeHandler newHandler = new FlowInstanceNodeHandler();
                BeanUtils.copyProperties(oldHandler, newHandler);
                newHandler.setId(KeyUtil.getId());
                newHandler.setInstanceNodeId(newNodeId);
                newInstanceNodeHandlerList.add(newHandler);
            }
        }

        /**创建新的流程实例*/
        Long newCurrentNodeId = 0L;
        if (oldNodeIdAndNewNodeIdMap.containsKey(oldFlowInstance.getCurrentNodeId())) {
            newCurrentNodeId = oldNodeIdAndNewNodeIdMap.get(oldFlowInstance.getCurrentNodeId());
        }


        FlowInstance newFlowInstance = new FlowInstance();
        newFlowInstance.setId(newFlowInstanceId);
        newFlowInstance.setCorpId(oldFlowInstance.getCorpId());
        newFlowInstance.setTemplateId(oldFlowInstance.getTemplateId());
        newFlowInstance.setCurrentNodeId(newCurrentNodeId);
        newFlowInstance.setCreateBy(userInfo.getUserId());
        newFlowInstance.setCreateTime(DateUtil.date().toTimestamp());
        newFlowInstance.setSourceInstanceId(oldFlowInstance.getId());


        /**持久化流程实例与流程实例节点、节点处理人、节点处理记录*/
        flowInstanceService.save(newFlowInstance);

        flowInstanceNodeService.saveBatch(newInstanceNodeList);


        flowInstanceNodeHandlerService.saveBatch(newInstanceNodeHandlerList);


        List<FlowInstanceTrace> newFlowInstanceTraceList = new ArrayList<>();
        List<FlowInstanceTrace> oldFlowInstanceTraceList = this.flowInstanceTraceService.listByFlowInstanceId(sourceInstanceId);
        for (FlowInstanceTrace oldFlowInstanceTrace : oldFlowInstanceTraceList) {
            FlowInstanceTrace newFlowInstanceTrace = new FlowInstanceTrace();
            BeanUtils.copyProperties(oldFlowInstanceTrace, newFlowInstanceTrace);
            newFlowInstanceTrace.setId(KeyUtil.getId());
            newFlowInstanceTrace.setFlowInstanceId(newFlowInstanceId);
            newFlowInstanceTrace.setCurrentNodeId(oldNodeIdAndNewNodeIdMap.get(oldFlowInstanceTrace.getCurrentNodeId()));
            newFlowInstanceTraceList.add(newFlowInstanceTrace);
        }
        flowInstanceTraceService.saveBatch(newFlowInstanceTraceList);

        /**5、成功后返回流程实例Id*/
        return newFlowInstanceId;
    }

    @Override
    public FlowInstanceNode endCurrentNode(FlowInstanceNodeDealResultDto dealResultDto, UserInfo userInfo) {
        /**根据getNodeEndType获得具体策略实现类*/
        INodeEndStrategy nodeEndStrategy = nodeEndStrategyFactory.getStrategy(INodeEndStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + dealResultDto.getNodeEndTypeId().toString());
        /**动态调用实现方法*/
        return nodeEndStrategy.updateDealResult(dealResultDto, userInfo);
    }

    @Override
    public boolean isEndFlow(Long flowInstanceId) {
        FlowInstance flowInstance = flowInstanceService.getById(flowInstanceId);
        if (FlowEndEnum.YES.getCode().equalsIgnoreCase(flowInstance.getIsEnd())) {
            return true;
        }
        return false;
    }

    @Override
    public FlowInstanceNodeHandler getBy(Long instanceNodeId, Long userId) {
        return flowInstanceNodeHandlerService.getBy(instanceNodeId, userId);
    }

    /**
     * 创建单个流程实例
     *
     * @param corpId
     * @param largeClassId
     * @param smallClassId
     * @param userInfo
     * @return
     */
    private Long createInstance(Long corpId, Integer largeClassId, Integer smallClassId, UserInfo userInfo) {
        /**1、根据业务类型查找最新启用的流程模板对象*/
        FlowTemplate findFlowTemplate = flowTemplateService.findLatestEnabled(corpId, largeClassId, smallClassId);
        if (findFlowTemplate == null) {
            throw new AppException("获得最新的可用的流程模板错误，请检查！");
        }

        /**2、根据流程模板查找最新的流程模板节点列表*/
        List<FlowTemplateNode> listEnabledBy = flowTemplateNodeService.listEnabledBy(findFlowTemplate.getId());
        if (listEnabledBy == null || listEnabledBy.isEmpty()) {
            throw new AppException("获得最新的可用的流程模板节点列表错误，请检查！");
        }

        /**3、构建流程实例对象*/
        Long flowInstanceId = KeyUtil.getId();
        FlowInstance newFlowInstance = new FlowInstance();
        newFlowInstance.setId(flowInstanceId);
        newFlowInstance.setCorpId(corpId);
        newFlowInstance.setTemplateId(findFlowTemplate.getId());
        newFlowInstance.setCurrentNodeId(0L);
        newFlowInstance.setCreateBy(userInfo.getUserId());
        newFlowInstance.setCreateTime(DateUtil.date().toTimestamp());

        /**4、构建流程实例节点列表*/
        List<FlowInstanceNode> instanceNodeList = new ArrayList<>();
        List<FlowInstanceNodeHandler> instanceNodeHandlerList = new ArrayList<>();
        for (FlowTemplateNode flowTemplateNode : listEnabledBy) {

            Long instanceNodeId = KeyUtil.getId();
            FlowInstanceNode newFlowInstanceNode = new FlowInstanceNode();
            newFlowInstanceNode.setId(instanceNodeId);
            newFlowInstanceNode.setFlowInstanceId(flowInstanceId);
            newFlowInstanceNode.setTemplateNodeId(flowTemplateNode.getId());
            newFlowInstanceNode.setTemplateNodeName(flowTemplateNode.getName());
            newFlowInstanceNode.setSortNo(flowTemplateNode.getSortNo());
            instanceNodeList.add(newFlowInstanceNode);


            /**构建节点处理人列表*/
            List<FlowTemplateNodeHandler> listAllAssignToPeople = flowTemplateNodeHandlerService.listAllAssignToPeople(flowTemplateNode.getId(), userInfo);
            for (FlowTemplateNodeHandler flowTemplateNodeHandler : listAllAssignToPeople) {
                FlowInstanceNodeHandler flowInstanceNodeHandler = new FlowInstanceNodeHandler();
                Long id = KeyUtil.getId();
                flowInstanceNodeHandler.setId(id);
                flowInstanceNodeHandler.setInstanceNodeId(instanceNodeId);
                flowInstanceNodeHandler.setMainDirector(flowTemplateNodeHandler.getIsMainDirector());
                flowInstanceNodeHandler.setMainDirectorEndTypeId(0);
                flowInstanceNodeHandler.setCountersignPassed(CounterSignResultEnum.NO.getCode());
                flowInstanceNodeHandler.setAssignedToBy(flowTemplateNodeHandler.getHandlerId());
                instanceNodeHandlerList.add(flowInstanceNodeHandler);
            }
        }

        FlowInstanceNode firstFlowInstanceNode = instanceNodeList.get(0);
        if (firstFlowInstanceNode == null) {
            throw new AppException("流程实例至少需要定义一个步骤来处理，请检查！");
        }
        /**创建时，设置当前正在运行的节点 = 第一个节点*/
        newFlowInstance.setCurrentNodeId(firstFlowInstanceNode.getId());

        /**持久化流程实例与流程实例节点、节点处理人*/
        flowInstanceService.save(newFlowInstance);
        flowInstanceNodeService.saveBatch(instanceNodeList);
        flowInstanceNodeHandlerService.saveBatch(instanceNodeHandlerList);
        /**5、成功后返回流程实例Id*/
        return flowInstanceId;
    }
}
