package com.zjft.usp.anyfix.work.transfer.composite;

import com.zjft.usp.anyfix.work.transfer.dto.WorkTransferDto;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

/**
 * 工单流转聚合服务类
 *
 * @author zgpi
 * @since 2020-02-27
 */
public interface WorkTransferCompoService {

    /**
     * 客户撤单
     *
     * @param workTransferDto
     * @param userInfo
     * @param reqParam
     * @author zgpi
     * @date 2020/2/27 18:12
     */
    void recallWorkByCustom(WorkTransferDto workTransferDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 客户手工分配工单
     *
     * @param workTransferDto
     * @param userInfo
     * @param reqParam
     * @author zgpi
     * @date 2019/11/14 14:46
     **/
    void dispatchWorkByCustom(WorkTransferDto workTransferDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 服务商客服退单
     *
     * @param workTransferDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/2/28 17:28
     */
    void returnWorkByService(WorkTransferDto workTransferDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 客服手工受理工单
     *
     * @param workTransferDto
     * @param userInfo
     * @param reqParam
     * @author zgpi
     * @date 2019/11/13 10:46
     **/
    void handleWorkByManual(WorkTransferDto workTransferDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 客服转处理工单
     *
     * @param workTransferDto
     * @param userInfo
     * @param reqParam
     * @author zgpi
     * @date 2019/11/15 13:35
     **/
    void turnHandleWork(WorkTransferDto workTransferDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 派单主管撤回派单
     *
     * @param workTransferDto
     * @param userInfo
     * @param reqParam
     * @author zgpi
     * @date 2019/10/14 9:26 上午
     **/
    void recallAssign(WorkTransferDto workTransferDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 工程师认领工单
     *
     * @param workTransferDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author zphu
     * @date 2019/9/29 15:33
     **/
    void claimWork(WorkTransferDto workTransferDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 工程师拒绝派单
     *
     * @param workTransferDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author zphu
     * @date 2019/9/30 8:59
     **/
    void refuseAssign(WorkTransferDto workTransferDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 工程师退回派单
     *
     * @param workTransferDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author zphu
     * @date 2019/9/30 9:50
     **/
    void returnAssign(WorkTransferDto workTransferDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 派单主管退回工单
     * @date 2020/6/28
     * @param workTransferDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    void returnAssignByService(WorkTransferDto workTransferDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 检查工单
     *
     * @param workTransferDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/11/12 15:10
     **/
    void checkWork(WorkTransferDto workTransferDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 添加消息到消息队列
     *
     * @param topic
     * @param workId
     * @return
     * @author zgpi
     * @date 2020/2/28 17:24
     */
    void addMessageQueue(String topic, Long workId);
}
