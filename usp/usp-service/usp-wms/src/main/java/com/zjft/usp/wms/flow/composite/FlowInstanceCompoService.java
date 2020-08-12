package com.zjft.usp.wms.flow.composite;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.wms.flow.dto.FlowInstanceNodeDealResultDto;
import com.zjft.usp.wms.flow.model.FlowInstanceNode;
import com.zjft.usp.wms.flow.model.FlowInstanceNodeHandler;

import java.util.List;

/**
 * 流程实例组合服务类
 *
 * @version 1.0
 * @Author: JFZOU
 * @Date: 2019-11-13 17:30
 */
public interface FlowInstanceCompoService {

    /**
     * 默认使用最新的流程模板创建流程实例列表（用于首次创建流程实例）
     *
     * @param instanceNumber 明细条数，一条明细一个流程实例
     * @param corpId         公司ID
     * @param largeClassId   业务大类ID
     * @param smallClassId   业务小类ID
     * @param userInfo       当前用户对象
     * @return 返回流程实例ID列表
     */
    List<Long> createInstanceList(Integer instanceNumber, Long corpId, Integer largeClassId, Integer smallClassId, UserInfo userInfo);


    /**
     * 运行中的流程实例拆单再创建一个新的流程实例
     * (本处只处理运行中的单个流程实例拆单，比如运行中的调拨流程，申请5个，实发3个，剩下2个没发，当出现这种情况时，由于
     * 一条明细对应一个流程实例，当出现部分发货、部分不发货时，如果不拆单，则无法控制流程走向，因此需要进行拆单处理
     * 拆单后创建的流程实例数据跟sourceInstanceId流程实例一样，只是主键ID不一样)
     * 本方法建议在endCurrentNode之前进行，即本方法在sourceInstanceId调用endCurrentNode前执行，
     * 然后sourceInstanceId再通过endCurrentNode方法继续运行
     * @param sourceInstanceId 源流程实例ID
     * @param userInfo         当前用户对象
     * @return 返回流程实例ID
     */
    Long createInstanceBySplit(Long sourceInstanceId, UserInfo userInfo);

    /**
     * 结束当前节点并根据dealResultDto里的nodeEndType处理以后的流程
     * 非会签节点或者会签节点的主处理人，需要传入handlerId(主键)、nodeEndTypeId、nodeEndTypeName、isSubmit、doDescription
     * 如果是会签节点，非主处理人则传入需要传入handlerId(主键)、countersignPassed、countersignPassedName、、isSubmit、doDescription
     *
     * @param dealResultDto
     * @param userInfo
     * @return 返回流程下一个需要处理的节点，如果没有则返回null
     */
    FlowInstanceNode endCurrentNode(FlowInstanceNodeDealResultDto dealResultDto, UserInfo userInfo);

    /**
     * 判断是否结束流程实例
     *
     * @param flowInstanceId
     * @return 如果为true，表示流程结束；如果为false，表示流程未结束。
     */
    boolean isEndFlow(Long flowInstanceId);

    /**
     * 查找流程节点当前处理人对象
     *
     * @param instanceNodeId
     * @param userId
     * @return
     */
    FlowInstanceNodeHandler getBy(Long instanceNodeId, Long userId);
}
