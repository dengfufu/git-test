package com.zjft.usp.wms.flow.service;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.wms.flow.model.FlowInstance;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 流程实例表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface FlowInstanceService extends IService<FlowInstance> {


    /**
     * 结束流程实例
     *
     * @param flowInstanceId
     * @param userInfo
     */
    void endFlow(Long flowInstanceId, UserInfo userInfo);


    /**
     * 更新流程实例当前节点ID
     *
     * @param flowInstanceId
     * @param currentNodeId
     */
    void updateCurrentNodeId(Long flowInstanceId, Long currentNodeId);

    /**
     * 根据流程模板ID查找流程实例列表
     *
     * @param templateId
     * @return
     */
    List<FlowInstance> listByTemplateId(Long templateId);

    /**
     * 是否引用流程模板
     *
     * @param templateId
     * @return
     */
    boolean isRelationBy(Long templateId);
}
