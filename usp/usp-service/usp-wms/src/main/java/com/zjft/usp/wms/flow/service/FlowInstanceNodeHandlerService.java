package com.zjft.usp.wms.flow.service;

import com.zjft.usp.wms.flow.model.FlowInstanceNodeHandler;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 流程实例节点处理人表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-18
 */
public interface FlowInstanceNodeHandlerService extends IService<FlowInstanceNodeHandler> {

    /**
     * 清空流程节点信息
     *
     * @param instanceNodeId
     */
    void clearByInstanceNodeId(Long instanceNodeId);

    /**
     * 查找当前处理人对象
     *
     * @param instanceNodeId
     * @param userId
     * @return
     */
    FlowInstanceNodeHandler getBy(Long instanceNodeId, Long userId);

    /**
     * 查找节点处理人列表
     *
     * @param instanceNodeId
     * @return
     */
    List<FlowInstanceNodeHandler> listByInstanceNodeId(Long instanceNodeId);

    /**
     * 根据流程实例id列表获取当前处理人列表
     *
     * @author canlei
     * @param flowInstanceIdList
     * @return
     */
    Map<Long, List<Long>> mapCurAuditUserList(List<Long> flowInstanceIdList);

}
