package com.zjft.usp.anyfix.work.operate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.work.assign.dto.WorkAssignDto;
import com.zjft.usp.anyfix.work.check.dto.WorkCheckDto;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.operate.dto.WorkOperateDto;
import com.zjft.usp.anyfix.work.operate.model.WorkOperate;
import com.zjft.usp.anyfix.work.request.dto.WorkRequestDto;
import com.zjft.usp.anyfix.work.request.model.WorkRequest;
import com.zjft.usp.anyfix.work.sign.model.WorkSign;

import java.util.List;

/**
 * <p>
 * 工单操作过程表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-23
 */
public interface WorkOperateService extends IService<WorkOperate> {

    /**
     * 工单处理过程列表
     *
     * @param workId
     * @return
     * @author zgpi
     * @date 2019/10/11 7:07 下午
     **/
    List<WorkOperateDto> listWorkOperate(Long workId);

    /**
     * 建单操作记录
     *
     * @param workRequestDto
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 08:37
     **/
    void addWorkOperateByCreate(WorkRequestDto workRequestDto, WorkOperate workOperate);

    void addWorkOperateBySupplement(WorkRequestDto workRequestDto, WorkOperate workOperate);

    /**
     * 修改工单操作记录
     *
     * @param workRequestDto
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2020/2/26 20:19
     */
    void addWorkOperateByMod(WorkRequestDto workRequestDto, WorkOperate workOperate);

    /**
     * 重新提交工单操作记录
     *
     * @param workRequestDto
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2020/3/2 09:19
     */
    void addWorkOperateByResubmit(WorkRequestDto workRequestDto, WorkOperate workOperate);

    /**
     * 补提单操作记录
     *
     * @param workRequestDto
     * @param workOperate
     */
    void addWorkOperateBySupplementWork(WorkRequestDto workRequestDto, WorkOperate workOperate);

    /**
     * 客户撤单操作记录
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 08:40
     **/
    void addWorkOperateByCustomRecall(WorkOperate workOperate);

    /**
     * 客户催单操作记录
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 08:41
     **/
    void addWorkOperateByCustomReminder(WorkOperate workOperate);

    /**
     * 分配工单操作记录
     *
     * @param workOperate
     * @param workDeal
     * @return
     * @author zgpi
     * @date 2019/11/1 08:38
     **/
    void addWorkOperateByDispatch(WorkOperate workOperate, WorkDeal workDeal);

    /**
     * 服务商客服退单操作记录
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 08:52
     **/
    void addWorkOperateByServiceReturn(WorkOperate workOperate);

    /**
     * 派单主管退回工单
     * @date 2020/6/28
     * @param workOperate
     * @return void
     */
    void addWorkOperateByServiceAssignReturn(WorkOperate workOperate);

    /**
     * 客服受理工单操作记录
     *
     * @param workOperate
     * @param workDeal
     * @return
     * @author zgpi
     * @date 2019/11/1 08:54
     **/
    void addWorkOperateByHandle(WorkOperate workOperate, WorkDeal workDeal);

    /**
     * 客服转处理工单操作记录
     *
     * @param workOperate
     * @param workDeal
     * @return
     * @author zgpi
     * @date 2019/11/1 09:43
     **/
    void addWorkOperateByTurnHandle(WorkOperate workOperate, WorkDeal workDeal);

    /**
     * 派工操作记录
     *
     * @param workOperate
     * @param workAssignDto
     * @return
     * @author zgpi
     * @date 2019/11/1 08:57
     **/
    void addWorkOperateByAssign(WorkOperate workOperate, WorkAssignDto workAssignDto);

    /**
     * 自动派工操作记录
     *
     * @param workOperate
     * @param workAssignDto
     */
    void addWorkOperateByAutoAssign(WorkOperate workOperate, WorkAssignDto workAssignDto);

    /**
     * 撤回派工操作记录
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 09:01
     **/
    void addWorkOperateByRecallAssign(WorkOperate workOperate);

    /**
     * 工程师认领工单操作记录
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 09:02
     **/
    void addWorkOperateByClaim(WorkOperate workOperate);

    /**
     * 工程师拒绝派工操作记录
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 09:04
     **/
    void addWorkOperateByRefuseAssign(WorkOperate workOperate);

    /**
     * 工程师退回派工操作记录
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 09:06
     **/
    void addWorkOperateByReturnAssign(WorkOperate workOperate);

    /**
     * 工程师修改预约时间操作记录
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 09:11
     **/
    void addWorkOperateByChangeBookingTime(WorkOperate workOperate);

    /**
     * 工程师签到操作记录
     *
     * @param workOperate
     * @param workSign
     * @return
     * @author zgpi
     * @date 2019/11/1 09:11
     **/
    void addWorkOperateBySign(WorkOperate workOperate, WorkSign workSign);

    /**
     * 工程师现场服务操作记录
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 09:15
     **/
    void addWorkOperateByLocaleService(WorkOperate workOperate);

    /**
     * 工程师修改现场服务完成信息
     *
     * @param workOperate
     */
    void addWorkOperateByUpdateLocaleFinish(WorkOperate workOperate);

    /**
     * 工程师修改附件信息
     *
     * @param workOperate
     */
    void addWorkOperateByModFiles(WorkOperate workOperate, boolean isModFies, boolean isModSignature);

    /**
     * 修改工单费用
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2020/5/17 11:01
     **/
    void addWorkOperateByUpdateWorkFee(WorkOperate workOperate);

    /**
     * 工程师远程服务
     * 操作记录
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 09:15
     **/
    void addWorkOperateByRemoteService(WorkOperate workOperate);

    /**
     * 客户评价操作记录
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 09:19
     **/
    void addWorkOperateByEvaluate(WorkOperate workOperate);

    /**
     * 自动节点操作记录
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 09:19
     **/
    void addAutoWorkOperate(WorkOperate workOperate);

    /**
     * 添加自动节点操作记录
     *
     * @param workRequest
     * @param summary
     * @param workStatus
     * @param workOperateType
     */
    void addAutoWorkOperate(WorkRequest workRequest, String summary, Integer workStatus, Integer workOperateType);

    /**
     * 审核服务操作记录
     *
     * @param workOperate
     * @param workCheckDto
     * @return
     * @author zgpi
     * @date 2020/5/29 17:00
     **/
    void addServiceCheckOperate(WorkOperate workOperate, WorkCheckDto workCheckDto);

    /**
     * 审核服务操作记录
     *
     * @param workOperate
     * @param workCheckDto
     * @return
     * @author zgpi
     * @date 2020/5/29 17:00
     **/
    void addFeeCheckOperate(WorkOperate workOperate, WorkCheckDto workCheckDto);

    /**
     * 确认服务操作记录
     *
     * @param workOperate
     * @param workCheckDto
     * @return
     * @author zgpi
     * @date 2020/5/29 17:00
     **/
    void addServiceConfirmOperate(WorkOperate workOperate, WorkCheckDto workCheckDto);

    /**
     * 确认服务操作记录
     *
     * @param workOperate
     * @param workCheckDto
     * @return
     * @author zgpi
     * @date 2020/5/29 17:00
     **/
    void addFeeConfirmOperate(WorkOperate workOperate, WorkCheckDto workCheckDto);

    /**
     * 批量添加审核服务操作记录
     *
     * @param workOperateList
     * @param workCheckDto
     * @return
     * @author zgpi
     * @date 2020/6/1 10:25
     **/
    void addBatchCheckServiceOperate(List<WorkOperate> workOperateList, WorkCheckDto workCheckDto);

    /**
     * 批量添加审核费用操作记录
     *
     * @param workOperateList
     * @param workCheckDto
     * @return
     * @author zgpi
     * @date 2020/6/1 10:25
     **/
    void addBatchCheckFeeOperate(List<WorkOperate> workOperateList, WorkCheckDto workCheckDto);

    /**
     * 批量添加确认服务操作记录
     *
     * @param workOperateList
     * @param workCheckDto
     * @return
     * @author zgpi
     * @date 2020/6/1 10:25
     **/
    void addBatchConfirmServiceOperate(List<WorkOperate> workOperateList, WorkCheckDto workCheckDto);

    /**
     * 批量添加确认费用操作记录
     *
     * @param workOperateList
     * @param workCheckDto
     * @return
     * @author zgpi
     * @date 2020/6/1 10:25
     **/
    void addBatchConfirmFeeOperate(List<WorkOperate> workOperateList, WorkCheckDto workCheckDto);

    /**
     * 添加自动确认服务操作记录
     *
     * @param workOperateList
     */
    void addAutoConfirmServiceOperate(List<WorkOperate> workOperateList);

    /**
     * 添加自动确认费用操作记录
     *
     * @param workOperateList
     */
    void addAutoConfirmFeeOperate(List<WorkOperate> workOperateList);

}
