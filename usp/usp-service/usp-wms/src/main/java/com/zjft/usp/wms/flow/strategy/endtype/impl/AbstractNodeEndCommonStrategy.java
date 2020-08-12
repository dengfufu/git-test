package com.zjft.usp.wms.flow.strategy.endtype.impl;

import cn.hutool.core.date.DateUtil;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.wms.flow.dto.FlowInstanceNodeDealResultDto;
import com.zjft.usp.wms.flow.dto.FlowInstanceNodeEndDto;
import com.zjft.usp.wms.flow.enums.MainDirectorEnum;
import com.zjft.usp.wms.flow.enums.SubmitEnum;
import com.zjft.usp.wms.flow.factory.FlowInstanceTraceFactory;
import com.zjft.usp.wms.flow.model.FlowInstance;
import com.zjft.usp.wms.flow.model.FlowInstanceNode;
import com.zjft.usp.wms.flow.model.FlowInstanceNodeHandler;
import com.zjft.usp.wms.flow.model.FlowInstanceTrace;
import com.zjft.usp.wms.flow.service.FlowInstanceNodeHandlerService;
import com.zjft.usp.wms.flow.service.FlowInstanceNodeService;
import com.zjft.usp.wms.flow.service.FlowInstanceService;
import com.zjft.usp.wms.flow.service.FlowInstanceTraceService;
import com.zjft.usp.wms.flow.strategy.endtype.INodeEndStrategy;
import com.zjft.usp.wms.flow.strategy.endtype.dto.FlowEntitiesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 节点结束类型抽象策略类，通常需要在本类中存放通用方法，然后提供抽象方法供子类实现
 *
 * @Author: JFZOU
 * @Date: 2019-11-11 14:40
 */
public abstract class AbstractNodeEndCommonStrategy implements INodeEndStrategy {

    /**
     * 结束节点并返回下一个节点
     *
     * @param dealResultDto
     * @param userInfo
     * @return 返回下一个需要处理的节点
     */
    public abstract FlowInstanceNode endNode(FlowInstanceNodeDealResultDto dealResultDto, UserInfo userInfo);

    @Autowired
    private FlowInstanceService flowInstanceService;

    @Autowired
    private FlowInstanceNodeService flowInstanceNodeService;

    @Autowired
    private FlowInstanceNodeHandlerService flowInstanceNodeHandlerService;

    @Autowired
    private FlowInstanceTraceService flowInstanceTraceService;


    /**
     * 节点处理接口(支持普通审批节点、会签审批节点等保存或者提交操作)
     *
     * @param dealResultDto
     * @param userInfo
     */
    @Override
    public final FlowInstanceNode updateDealResult(FlowInstanceNodeDealResultDto dealResultDto, UserInfo userInfo) {

        /**检查接口参数使用是否正确*/
        FlowInstanceNodeHandler flowInstanceNodeHandler = flowInstanceNodeHandlerService.getById(dealResultDto.getNodeHandlerId());
        FlowInstanceNode flowInstanceNode = flowInstanceNodeService.getById(flowInstanceNodeHandler.getInstanceNodeId());
        checkData(flowInstanceNodeHandler, dealResultDto);

        /**不管是提交还是保存，统一先进行保存*/
        saveNode(dealResultDto, userInfo);


        /**记录本次流程处理的流程跟踪信息，提交才记录，保存不记录*/
        if (SubmitEnum.YES.getCode().equalsIgnoreCase(dealResultDto.getIsSubmit())) {
            FlowInstanceTrace getFlowInstanceTrace = getFlowInstanceTrace(flowInstanceNode, dealResultDto, userInfo);
            this.flowInstanceTraceService.save(getFlowInstanceTrace);
        }

        Integer nodeEndTypeId = dealResultDto.getNodeEndTypeId();
        String isSubmit = dealResultDto.getIsSubmit();
        /**是否提交并结束流程节点*/
        boolean isEndNodeSubmit = false;
        if (IntUtil.isNotZero(nodeEndTypeId) && SubmitEnum.YES.getCode().equalsIgnoreCase(isSubmit)) {
            isEndNodeSubmit = true;
        }

        if (isEndNodeSubmit) {
            completedNode(flowInstanceNode.getId(), dealResultDto, userInfo);
            return endNode(dealResultDto, userInfo);
        } else {
            return flowInstanceNode;
        }

    }

    /**
     * 内部处理方法，如果节点结束，更新当前节点结束信息
     *
     * @param instanceNodeId
     * @param dealResultDto
     * @param userInfo
     */
    private void completedNode(Long instanceNodeId, FlowInstanceNodeDealResultDto dealResultDto, UserInfo userInfo) {
        /**结束当前节点，置为已完成*/
        FlowInstanceNodeEndDto flowNodeEndDto = new FlowInstanceNodeEndDto();
        flowNodeEndDto.setCurrentNodeId(instanceNodeId);
        flowNodeEndDto.setCompletedNodeEndTypeId(dealResultDto.getNodeEndTypeId());
        flowNodeEndDto.setCompletedBy(userInfo.getUserId());
        flowNodeEndDto.setCompletedDescription(dealResultDto.getDoDescription());

        this.flowInstanceNodeService.updateCompleted(flowNodeEndDto);
    }

    /**
     * 内部处理方法
     *
     * @param dealResultDto
     */
    private void checkData(FlowInstanceNodeHandler flowInstanceNodeHandler, FlowInstanceNodeDealResultDto dealResultDto) {

        if (LongUtil.isZero(dealResultDto.getNodeHandlerId())) {
            throw new AppException("关键值handlerId不能为空，请检查！");
        }

        if (StringUtils.isEmpty(dealResultDto.getIsSubmit())) {
            throw new AppException("关键值isSubmit不能为空，请检查！");
        }

        if (IntUtil.isZero(dealResultDto.getNodeEndTypeId()) && StringUtils.isEmpty(dealResultDto.getCountersignPassed())) {
            throw new AppException("关键值nodeEndTypeId与countersignPassed不能同时为空，请检查！");
        }

        if (IntUtil.isNotZero(dealResultDto.getNodeEndTypeId()) && !StringUtils.isEmpty(dealResultDto.getCountersignPassed())) {
            throw new AppException("关键值nodeEndTypeId与countersignPassed不能同时存在，请检查！");
        }

        if (StringUtils.isEmpty(dealResultDto.getNodeEndTypeName()) && StringUtils.isEmpty(dealResultDto.getCountersignPassedName())) {
            throw new AppException("关键值nodeEndTypeName与countersignPassedName不能同时为空，请检查！");
        }

        if (!StringUtils.isEmpty(dealResultDto.getNodeEndTypeName()) && !StringUtils.isEmpty(dealResultDto.getCountersignPassedName())) {
            throw new AppException("关键值nodeEndTypeName与countersignPassedName不能同时存在，请检查！");
        }

        if (SubmitEnum.YES.getCode().equalsIgnoreCase(dealResultDto.getIsSubmit())) {
            if (MainDirectorEnum.YES.getCode().equalsIgnoreCase(flowInstanceNodeHandler.getMainDirector())) {
                if (IntUtil.isZero(dealResultDto.getNodeEndTypeId())) {
                    throw new AppException("当前处理人为本节点主要处理人，审批结论不能为空，否则会影响流程走向，请检查！");
                }
            }

            if (!MainDirectorEnum.YES.getCode().equalsIgnoreCase(flowInstanceNodeHandler.getMainDirector())) {
                if (StringUtils.isEmpty(dealResultDto.getCountersignPassed())) {
                    throw new AppException("当前处理人为会签处理人，审批结论不能为空，请检查！");
                }
            }
        }
    }

    /**
     * 保存节点信息通用实现
     *
     * @param dealResultDto
     * @param userInfo
     */
    private void saveNode(FlowInstanceNodeDealResultDto dealResultDto, UserInfo userInfo) {
        FlowInstanceNodeHandler flowInstanceNodeHandler = new FlowInstanceNodeHandler();
        flowInstanceNodeHandler.setId(dealResultDto.getNodeHandlerId());
        flowInstanceNodeHandler.setMainDirectorEndTypeId(dealResultDto.getNodeEndTypeId());
        flowInstanceNodeHandler.setCountersignPassed(dealResultDto.getCountersignPassed());
        flowInstanceNodeHandler.setDoDescription(dealResultDto.getDoDescription());
        flowInstanceNodeHandler.setIsSubmit(dealResultDto.getIsSubmit());
        if (userInfo != null) {
            flowInstanceNodeHandler.setDoBy(userInfo.getUserId());
        }
        flowInstanceNodeHandler.setDoTime(DateUtil.date().toTimestamp());

        this.flowInstanceNodeHandlerService.updateById(flowInstanceNodeHandler);
    }

    /**
     * 获得FlowEntitiesDto
     *
     * @param handlerId
     * @return
     */
    protected FlowEntitiesDto getFlowEntities(Long handlerId) {

        if (handlerId == null) {
            throw new AppException("数据传输出现意外错误，请重试！");
        }

        FlowInstanceNodeHandler flowInstanceNodeHandler = flowInstanceNodeHandlerService.getById(handlerId);
        FlowInstanceNode flowInstanceNode = this.flowInstanceNodeService.getById(flowInstanceNodeHandler.getInstanceNodeId());
        FlowInstance flowInstance = this.flowInstanceService.getById(flowInstanceNode.getFlowInstanceId());

        Long currentNodeId = flowInstance.getCurrentNodeId();
        FlowInstanceNode currentInstanceNode = this.flowInstanceNodeService.getById(currentNodeId);

        FlowEntitiesDto flowInstanceStrategyDto = new FlowEntitiesDto();
        flowInstanceStrategyDto.setFlowInstance(flowInstance);
        flowInstanceStrategyDto.setCurrentInstanceNode(currentInstanceNode);
        return flowInstanceStrategyDto;
    }

    /**
     * 重新调整节点
     *
     * @param flowInstanceId 流程实例ID
     * @param targetNode     重新指定节点对象
     */
    protected void resetFrontNodeData(Long flowInstanceId, FlowInstanceNode targetNode) {
        /**流程进入目标节点*/
        this.flowInstanceService.updateCurrentNodeId(flowInstanceId, targetNode.getId());

        /**回退后，重置目标节点之后的所有节点（不包含上一个节点自己）*/
        List<FlowInstanceNode> listAfterCurrentNodeSortBy = this.flowInstanceNodeService.listAfterCurrentNodeSortBy(targetNode);
        for (FlowInstanceNode nextNode : listAfterCurrentNodeSortBy) {
            this.flowInstanceNodeService.clearCompleted(nextNode.getId());
        }
        /**重置目标节点自身数据*/
        this.flowInstanceNodeService.clearCompleted(targetNode.getId());
    }

    /**
     * 构建流程跟踪信息对象
     *
     * @param flowInstanceNode
     * @param dealResultDto
     * @param userInfo
     * @return
     */
    private FlowInstanceTrace getFlowInstanceTrace(
            FlowInstanceNode flowInstanceNode,
            FlowInstanceNodeDealResultDto dealResultDto,
            UserInfo userInfo) {
        return FlowInstanceTraceFactory.getFlowInstanceTrace(flowInstanceNode, dealResultDto, userInfo);

    }
}
