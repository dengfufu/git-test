package com.zjft.usp.anyfix.work.operate.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.model.WorkType;
import com.zjft.usp.anyfix.baseinfo.service.WorkTypeService;
import com.zjft.usp.anyfix.common.feign.dto.UserRealDto;
import com.zjft.usp.anyfix.corp.branch.model.ServiceBranch;
import com.zjft.usp.anyfix.corp.branch.service.ServiceBranchService;
import com.zjft.usp.anyfix.work.assign.dto.WorkAssignDto;
import com.zjft.usp.anyfix.work.assign.dto.WorkAssignEngineerDto;
import com.zjft.usp.anyfix.work.assign.service.WorkAssignEngineerService;
import com.zjft.usp.anyfix.work.chat.enums.OperateTypeEnum;
import com.zjft.usp.anyfix.work.check.dto.WorkCheckDto;
import com.zjft.usp.anyfix.work.check.enums.FeeCheckStatusEnum;
import com.zjft.usp.anyfix.work.check.enums.ServiceCheckStatusEnum;
import com.zjft.usp.anyfix.work.check.enums.ServiceConfirmStatusEnum;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.operate.dto.WorkOperateDto;
import com.zjft.usp.anyfix.work.operate.enums.WorkOperateTypeEnum;
import com.zjft.usp.anyfix.work.operate.mapper.WorkOperateMapper;
import com.zjft.usp.anyfix.work.operate.model.WorkOperate;
import com.zjft.usp.anyfix.work.operate.service.WorkOperateService;
import com.zjft.usp.anyfix.work.request.dto.WorkRequestDto;
import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
import com.zjft.usp.anyfix.work.request.model.WorkRequest;
import com.zjft.usp.anyfix.work.sign.model.WorkSign;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.uas.dto.CorpDto;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 工单操作过程表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkOperateServiceImpl extends ServiceImpl<WorkOperateMapper, WorkOperate> implements WorkOperateService {

    @Autowired
    private WorkTypeService workTypeService;
    @Autowired
    private WorkAssignEngineerService workAssignEngineerService;
    @Autowired
    private ServiceBranchService serviceBranchService;
    @Resource
    private UasFeignService uasFeignService;

    /**
     * 工单操作记录列表
     *
     * @param workId
     * @return
     * @author zgpi
     * @date 2019/11/1 08:41
     **/
    @Override
    public List<WorkOperateDto> listWorkOperate(Long workId) {
        QueryWrapper<WorkOperate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("work_id", workId);
        queryWrapper.orderByDesc("operate_time");
        queryWrapper.orderByDesc("work_status");
        List<WorkOperate> list = this.list(queryWrapper);
        List<WorkOperateDto> workOperateDtoList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            WorkOperateDto workOperateDto;
            for (WorkOperate workOperate : list) {
                workOperateDto = new WorkOperateDto();
                BeanUtils.copyProperties(workOperate, workOperateDto);
                workOperateDto.setWorkStatusName(WorkStatusEnum.getNameByCode(workOperate.getWorkStatus()));
                workOperateDto.setOperateTypeName(WorkOperateTypeEnum.getNameByCode(workOperate.getOperateType()));
                workOperateDtoList.add(workOperateDto);
            }
        }
        return workOperateDtoList;
    }

    /**
     * 建单操作记录
     *
     * @param workRequestDto
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 08:42
     **/
    @Override
    public void addWorkOperateByCreate(WorkRequestDto workRequestDto, WorkOperate workOperate) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setWorkStatus(WorkStatusEnum.TO_DISPATCH.getCode());
        workOperate.setOperateType(WorkOperateTypeEnum.CREATE.getCode());
        workOperate.setOperateTime(DateUtil.date().toTimestamp());
        String summary = this.findSummaryByCreate(workRequestDto, workOperate);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 补单操作记录
     *
     * @param workRequestDto
     * @param workOperate
     */
    @Override
    public void addWorkOperateBySupplement(WorkRequestDto workRequestDto, WorkOperate workOperate) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setWorkStatus(WorkStatusEnum.TO_DISPATCH.getCode());
        workOperate.setOperateType(WorkOperateTypeEnum.CREATE.getCode());
        workOperate.setOperateTime(DateUtil.date().toTimestamp());
        String summary = this.findSummaryBySupplement(workRequestDto, workOperate);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }


    /**
     * 修改工单操作记录
     *
     * @param workRequestDto
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2020/2/26 20:19
     */
    @Override
    public void addWorkOperateByMod(WorkRequestDto workRequestDto, WorkOperate workOperate) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setWorkStatus(workRequestDto.getWorkStatus());
        workOperate.setOperateType(WorkOperateTypeEnum.MOD.getCode());
        workOperate.setOperateTime(DateUtil.date());
        String summary = this.findSummaryByMod(workRequestDto, workOperate);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 重新提交工单操作记录
     *
     * @param workRequestDto
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2020/3/2 09:19
     */
    @Override
    public void addWorkOperateByResubmit(WorkRequestDto workRequestDto, WorkOperate workOperate) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setWorkStatus(WorkStatusEnum.TO_DISPATCH.getCode());
        workOperate.setOperateType(WorkOperateTypeEnum.RESUBMIT.getCode());
        workOperate.setOperateTime(DateUtil.date());
        String summary = this.findSummaryByResubmit(workRequestDto, workOperate);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 补提工单操作记录
     *
     * @param workRequestDto
     * @param workOperate
     * @return
     * @author ljzhu
     * @date 2020/3/8
     */
    @Override
    public void addWorkOperateBySupplementWork(WorkRequestDto workRequestDto, WorkOperate workOperate) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setWorkStatus(WorkStatusEnum.CLOSED.getCode());
        workOperate.setOperateTime(DateUtil.date());
        String summary = this.findSummaryBySupplementWork(workRequestDto, workOperate);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }


    /**
     * 客户撤单操作记录
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 08:46
     **/
    @Override
    public void addWorkOperateByCustomRecall(WorkOperate workOperate) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setWorkStatus(WorkStatusEnum.CANCELED.getCode());
        workOperate.setOperateType(WorkOperateTypeEnum.CUSTOM_RECALL.getCode());
        workOperate.setOperateTime(DateUtil.date());
        String summary = this.findSummaryByCustomRecall(workOperate);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 客户催单操作记录
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 08:49
     **/
    @Override
    public void addWorkOperateByCustomReminder(WorkOperate workOperate) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setOperateType(WorkOperateTypeEnum.CUSTOM_REMINDER.getCode());
        workOperate.setOperateTime(DateUtil.date().toTimestamp());
        String summary = this.findSummaryByCustomReminder(workOperate);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 分配工单操作记录
     *
     * @param workOperate
     * @param workDeal
     * @return
     * @author zgpi
     * @date 2019/11/1 08:50
     **/
    @Override
    public void addWorkOperateByDispatch(WorkOperate workOperate, WorkDeal workDeal) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setWorkStatus(WorkStatusEnum.TO_HANDLE.getCode());
        workOperate.setOperateType(WorkOperateTypeEnum.MANUAL_DISPATCH.getCode());
        workOperate.setOperateTime(DateUtil.date().toTimestamp());
        String summary = this.findSummaryByDispatch(workOperate, workDeal);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 服务商客服退单操作记录
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 08:53
     **/
    @Override
    public void addWorkOperateByServiceReturn(WorkOperate workOperate) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setWorkStatus(WorkStatusEnum.RETURNED.getCode());
        workOperate.setOperateType(WorkOperateTypeEnum.SERVICE_RETURN.getCode());
        workOperate.setOperateTime(DateUtil.date().toTimestamp());
        String summary = this.findSummaryByServiceReturn(workOperate);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 派单主管退回工单
     * @date 2020/6/28
     * @param workOperate
     * @return void
     */
    @Override
    public void addWorkOperateByServiceAssignReturn(WorkOperate workOperate) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setWorkStatus(WorkStatusEnum.TO_HANDLE.getCode());
        workOperate.setOperateType(WorkOperateTypeEnum.RETURN_ASSIGN_SERVICE.getCode());
        workOperate.setOperateTime(DateUtil.date().toTimestamp());
        String summary = this.findSummaryByServiceReturn(workOperate);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 客服受理工单操作记录
     *
     * @param workOperate
     * @param workDeal
     * @return
     * @author zgpi
     * @date 2019/11/1 08:55
     **/
    @Override
    public void addWorkOperateByHandle(WorkOperate workOperate, WorkDeal workDeal) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setWorkStatus(WorkStatusEnum.TO_ASSIGN.getCode());
        workOperate.setOperateType(WorkOperateTypeEnum.MANUAL_HANDLE.getCode());
        workOperate.setOperateTime(DateUtil.date());
        String summary = this.findSummaryByHandle(workOperate, workDeal);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 客服转处理工单操作记录
     *
     * @param workOperate
     * @param workDeal
     * @return
     * @author zgpi
     * @date 2019/11/1 09:44
     **/
    @Override
    public void addWorkOperateByTurnHandle(WorkOperate workOperate, WorkDeal workDeal) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setOperateType(WorkOperateTypeEnum.TURN_HANDLE.getCode());
        workOperate.setOperateTime(DateUtil.date().toTimestamp());
        String summary = this.findSummaryByTurnHandle(workOperate, workDeal);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 派工操作记录
     *
     * @param workOperate
     * @param workAssignDto
     * @return
     * @author zgpi
     * @date 2019/11/1 08:58
     **/
    @Override
    public void addWorkOperateByAssign(WorkOperate workOperate, WorkAssignDto workAssignDto) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setWorkStatus(WorkStatusEnum.TO_CLAIM.getCode());
        workOperate.setOperateType(WorkOperateTypeEnum.ASSIGN.getCode());
        workOperate.setOperateTime(DateUtil.date().toTimestamp());
        String summary = this.findSummaryByAssign(workOperate, workAssignDto);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 自动派工操作记录
     *
     * @param workOperate
     * @param workAssignDto
     * @return
     * @author ljzhu
     **/
    @Override
    public void addWorkOperateByAutoAssign(WorkOperate workOperate, WorkAssignDto workAssignDto) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setWorkStatus(WorkStatusEnum.TO_CLAIM.getCode());
        workOperate.setOperateType(WorkOperateTypeEnum.ASSIGN.getCode());
        workOperate.setOperateTime(DateUtil.date().toTimestamp());
        String summary = this.findSummaryByAutoAssign(workOperate, workAssignDto);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 撤回派工操作记录
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 09:01
     **/
    @Override
    public void addWorkOperateByRecallAssign(WorkOperate workOperate) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setWorkStatus(WorkStatusEnum.TO_ASSIGN.getCode());
        workOperate.setOperateType(WorkOperateTypeEnum.RECALL_ASSIGN.getCode());
        workOperate.setOperateTime(DateUtil.date().toTimestamp());
        String summary = this.findSummaryByRecallAssign(workOperate);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 工程师认领工单操作记录
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 09:03
     **/
    @Override
    public void addWorkOperateByClaim(WorkOperate workOperate) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setWorkStatus(WorkStatusEnum.TO_SIGN.getCode());
        workOperate.setOperateType(WorkOperateTypeEnum.CLAIM.getCode());
        workOperate.setOperateTime(DateUtil.date().toTimestamp());
        String summary = this.findSummaryByClaim(workOperate);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 工程师拒绝派工操作记录
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 09:04
     **/
    @Override
    public void addWorkOperateByRefuseAssign(WorkOperate workOperate) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setWorkStatus(WorkStatusEnum.TO_ASSIGN.getCode());
        workOperate.setOperateType(WorkOperateTypeEnum.REFUSE_ASSIGN.getCode());
        workOperate.setOperateTime(DateUtil.date().toTimestamp());
        String summary = this.findSummaryByRefuseAssign(workOperate);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 工程师退回派工操作记录
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 09:06
     **/
    @Override
    public void addWorkOperateByReturnAssign(WorkOperate workOperate) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setWorkStatus(WorkStatusEnum.TO_ASSIGN.getCode());
        workOperate.setOperateType(WorkOperateTypeEnum.RETURN_ASSIGN.getCode());
        workOperate.setOperateTime(DateUtil.date().toTimestamp());
        String summary = this.findSummaryByReturnAssign(workOperate);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 工程师修改预约时间操作记录
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 09:11
     **/
    @Override
    public void addWorkOperateByChangeBookingTime(WorkOperate workOperate) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setOperateType(WorkOperateTypeEnum.CHANGE_BOOKING_TIME.getCode());
        workOperate.setOperateTime(DateUtil.date().toTimestamp());
        String summary = this.findSummaryByChangeBookingTime(workOperate);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 工程师签到操作记录
     *
     * @param workOperate
     * @param workSign
     * @return
     * @author zgpi
     * @date 2019/11/1 09:12
     **/
    @Override
    public void addWorkOperateBySign(WorkOperate workOperate, WorkSign workSign) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setOperateType(WorkOperateTypeEnum.SIGN.getCode());
        workOperate.setOperateTime(DateUtil.date().toTimestamp());
        String summary = this.findSummaryBySign(workOperate, workSign);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 工程师现场服务操作记录
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 09:16
     **/
    @Override
    public void addWorkOperateByLocaleService(WorkOperate workOperate) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setOperateType(WorkOperateTypeEnum.LOCATE_SERVICE.getCode());
        workOperate.setOperateTime(DateUtil.date().toTimestamp());
        String summary = this.findSummaryByService(workOperate);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 工程师修改现场服务完成信息操作记录
     *
     * @param workOperate
     */
    @Override
    public void addWorkOperateByUpdateLocaleFinish(WorkOperate workOperate) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setOperateType(WorkOperateTypeEnum.MOD_LOCALE_FINISH.getCode());
        workOperate.setOperateTime(DateUtil.date().toTimestamp());
        String summary = this.findSummaryByModLocaleFinish(workOperate);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    @Override
    public void addWorkOperateByModFiles(WorkOperate workOperate, boolean isModFies, boolean isModSignature) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setOperateType(WorkOperateTypeEnum.MOD_LOCALE_FINISH.getCode());
        workOperate.setOperateTime(DateUtil.date().toTimestamp());
        String summary = this.findSummaryByModFiles(workOperate, isModFies, isModSignature);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 修改工单费用
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2020/5/17 11:01
     **/
    @Override
    public void addWorkOperateByUpdateWorkFee(WorkOperate workOperate) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setOperateType(WorkOperateTypeEnum.MOD_WORK_FEE.getCode());
        workOperate.setOperateTime(DateUtil.date());
        String summary = this.findSummaryByModWorkFee(workOperate);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 工程师远程服务操作记录
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 09:16
     **/
    @Override
    public void addWorkOperateByRemoteService(WorkOperate workOperate) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setOperateType(WorkOperateTypeEnum.REMOTE_SERVICE.getCode());
        workOperate.setOperateTime(DateUtil.date().toTimestamp());
        String summary = this.findSummaryByService(workOperate);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 客户评价操作记录
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 09:20
     **/
    @Override
    public void addWorkOperateByEvaluate(WorkOperate workOperate) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setOperateType(WorkOperateTypeEnum.EVALUATE.getCode());
        workOperate.setOperateTime(DateUtil.date().toTimestamp());
        String summary = this.findSummaryByEvaluate(workOperate);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 添加自动节点操作记录
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/13 13:48
     **/
    @Override
    public void addAutoWorkOperate(WorkOperate workOperate) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setOperateTime(DateUtil.date().toTimestamp());
        this.save(workOperate);
    }

    /**
     * 添加自动节点操作记录
     *
     * @param workRequest
     * @param summary
     * @param workStatus
     * @param workOperateType
     */
    @Override
    public void addAutoWorkOperate(WorkRequest workRequest, String summary, Integer workStatus, Integer workOperateType) {
        if (workRequest == null) {
            return;
        }
        WorkOperate workOperate = new WorkOperate();
        workOperate.setId(KeyUtil.getId());
        workOperate.setWorkId(workRequest.getWorkId());
        workOperate.setReferId(workRequest.getWorkId());
        workOperate.setWorkStatus(workStatus);
        workOperate.setSummary(summary);
        workOperate.setOperator(workRequest.getCreator());
        workOperate.setOperateType(workOperateType);
        workOperate.setOperateTime(DateUtil.date().toTimestamp());
        if (workOperate != null) {
            this.save(workOperate);
        }
    }

    /**
     * 审核服务操作记录
     *
     * @param workOperate
     * @param workCheckDto
     * @return
     * @author zgpi
     * @date 2020/5/29 17:00
     **/
    @Override
    public void addServiceCheckOperate(WorkOperate workOperate, WorkCheckDto workCheckDto) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setOperateType(WorkOperateTypeEnum.SERVICE_CHECK_FINISH.getCode());
        workOperate.setOperateTime(DateUtil.date());
        String userName = this.findUserNameById(workOperate.getOperator());
        String summary = this.findSummaryByServiceCheck(userName, workCheckDto);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 审核服务操作记录
     *
     * @param workOperate
     * @param workCheckDto
     * @return
     * @author zgpi
     * @date 2020/5/29 17:00
     **/
    @Override
    public void addFeeCheckOperate(WorkOperate workOperate, WorkCheckDto workCheckDto) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setOperateType(WorkOperateTypeEnum.SERVICE_CHECK_FEE.getCode());
        workOperate.setOperateTime(DateUtil.date());
        String userName = this.findUserNameById(workOperate.getOperator());
        String summary = this.findSummaryByFeeCheck(userName, workCheckDto);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 确认服务操作记录
     *
     * @param workOperate
     * @param workCheckDto
     * @return
     * @author zgpi
     * @date 2020/5/29 17:00
     **/
    @Override
    public void addServiceConfirmOperate(WorkOperate workOperate, WorkCheckDto workCheckDto) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setOperateType(WorkOperateTypeEnum.DEMANDER_CONFIRM_FINISH.getCode());
        workOperate.setOperateTime(DateUtil.date());
        String userName = this.findUserNameById(workOperate.getOperator());
        String summary = this.findSummaryByServiceConfirm(userName, workCheckDto);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 确认费用操作记录
     *
     * @param workOperate
     * @param workCheckDto
     * @return
     * @author zgpi
     * @date 2020/5/29 17:00
     **/
    @Override
    public void addFeeConfirmOperate(WorkOperate workOperate, WorkCheckDto workCheckDto) {
        workOperate.setId(KeyUtil.getId());
        workOperate.setOperateType(WorkOperateTypeEnum.DEMANDER_CONFIRM_FEE.getCode());
        workOperate.setOperateTime(DateUtil.date());
        String userName = this.findUserNameById(workOperate.getOperator());
        String summary = this.findSummaryByFeeConfirm(userName, workCheckDto);
        workOperate.setSummary(summary);
        this.save(workOperate);
    }

    /**
     * 批量添加审核服务操作记录
     *
     * @param workOperateList
     * @param workCheckDto
     * @return
     * @author zgpi
     * @date 2020/6/1 10:25
     **/
    @Override
    public void addBatchCheckServiceOperate(List<WorkOperate> workOperateList, WorkCheckDto workCheckDto) {
        String userName = this.findUserNameById(workCheckDto.getOperator());
        if (CollectionUtil.isNotEmpty(workOperateList)) {
            workOperateList.forEach(workOperate -> {
                workOperate.setId(KeyUtil.getId());
                workOperate.setOperateType(WorkOperateTypeEnum.SERVICE_BATCH_CHECK_FINISH.getCode());
                workOperate.setOperateTime(DateUtil.date());
                String summary = this.findSummaryByServiceCheck(userName, workCheckDto);
                workOperate.setSummary(summary);
            });
        }
        this.saveBatch(workOperateList);
    }

    /**
     * 批量添加审核费用操作记录
     *
     * @param workOperateList
     * @param workCheckDto
     * @return
     * @author zgpi
     * @date 2020/6/1 10:25
     **/
    @Override
    public void addBatchCheckFeeOperate(List<WorkOperate> workOperateList, WorkCheckDto workCheckDto) {
        String userName = this.findUserNameById(workCheckDto.getOperator());
        if (CollectionUtil.isNotEmpty(workOperateList)) {
            workOperateList.forEach(workOperate -> {
                workOperate.setId(KeyUtil.getId());
                workOperate.setOperateType(WorkOperateTypeEnum.SERVICE_BATCH_CHECK_FEE.getCode());
                workOperate.setOperateTime(DateUtil.date());
                String summary = this.findSummaryByFeeCheck(userName, workCheckDto);
                workOperate.setSummary(summary);
            });
        }
        this.saveBatch(workOperateList);
    }

    /**
     * 批量添加确认服务操作记录
     *
     * @param workOperateList
     * @param workCheckDto
     * @return
     * @author zgpi
     * @date 2020/6/1 10:25
     **/
    @Override
    public void addBatchConfirmServiceOperate(List<WorkOperate> workOperateList, WorkCheckDto workCheckDto) {
        String userName = this.findUserNameById(workCheckDto.getOperator());
        if (CollectionUtil.isNotEmpty(workOperateList)) {
            workOperateList.forEach(workOperate -> {
                workOperate.setId(KeyUtil.getId());
                workOperate.setOperateType(WorkOperateTypeEnum.DEMANDER_BATCH_CONFIRM_FINISH.getCode());
                workOperate.setOperateTime(DateUtil.date());
                String summary = this.findSummaryByServiceConfirm(userName, workCheckDto);
                workOperate.setSummary(summary);
            });
        }
        this.saveBatch(workOperateList);
    }

    /**
     * 批量添加确认费用操作记录
     *
     * @param workOperateList
     * @param workCheckDto
     * @return
     * @author zgpi
     * @date 2020/6/1 10:25
     **/
    @Override
    public void addBatchConfirmFeeOperate(List<WorkOperate> workOperateList, WorkCheckDto workCheckDto) {
        String userName = this.findUserNameById(workCheckDto.getOperator());
        if (CollectionUtil.isNotEmpty(workOperateList)) {
            workOperateList.forEach(workOperate -> {
                workOperate.setId(KeyUtil.getId());
                workOperate.setOperateType(WorkOperateTypeEnum.DEMANDER_BATCH_CONFIRM_FEE.getCode());
                workOperate.setOperateTime(DateUtil.date());
                String summary = this.findSummaryByFeeConfirm(userName, workCheckDto);
                workOperate.setSummary(summary);
            });
        }
        this.saveBatch(workOperateList);
    }

    /**
     * 添加自动确认服务操作记录
     *
     * @param workOperateList
     */
    @Override
    public void addAutoConfirmServiceOperate(List<WorkOperate> workOperateList) {
        if (CollectionUtil.isNotEmpty(workOperateList)) {
            workOperateList.forEach(workOperate -> {
                workOperate.setOperateType(WorkOperateTypeEnum.AUTO_CONFIRM_FINISH.getCode());
                workOperate.setOperateTime(DateUtil.date());
                workOperate.setSummary("自动确认通过");
            });
        }
        this.saveBatch(workOperateList);
    }

    /**
     * 添加自动确认费用操作记录
     *
     * @param workOperateList
     */
    @Override
    public void addAutoConfirmFeeOperate(List<WorkOperate> workOperateList) {
        if (CollectionUtil.isNotEmpty(workOperateList)) {
            workOperateList.forEach(workOperate -> {
                workOperate.setOperateType(WorkOperateTypeEnum.AUTO_CONFIRM_FEE.getCode());
                workOperate.setOperateTime(DateUtil.date());
                workOperate.setSummary("自动确认通过");
            });
        }
        this.saveBatch(workOperateList);
    }

    /**
     * 建单描述
     *
     * @param workRequestDto
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/11/1 08:44
     **/
    private String findSummaryByCreate(WorkRequestDto workRequestDto, WorkOperate workOperate) {
        StringBuilder summary = new StringBuilder(32);
        String userName = this.findUserNameById(workOperate.getOperator());
        if (StrUtil.isNotBlank(userName)) {
            summary.append(userName);
        }
        if (StrUtil.isNotBlank(workRequestDto.getCustomCorpName())) {
            summary.append("为客户 ");
            summary.append(workRequestDto.getCustomCorpName());
        }
        summary.append("<br/>");
        summary.append("创建了");
        WorkType workType = workTypeService.getById(workRequestDto.getWorkType());
        if (workType != null) {
            summary.append(workType.getName());
        }
        summary.append("工单");
        return summary.toString();
    }

    /**
     * 补单描述
     *
     * @param workRequestDto
     * @param workOperate
     * @return
     * @author ljzhu
     **/
    private String findSummaryBySupplement(WorkRequestDto workRequestDto, WorkOperate workOperate) {
        StringBuilder summary = new StringBuilder(32);
        String userName = this.findUserNameById(workOperate.getOperator());
        if (StrUtil.isNotBlank(userName)) {
            summary.append(userName);
        }
        if (StrUtil.isNotBlank(workRequestDto.getCustomCorpName())) {
            summary.append("为客户 ");
            summary.append(workRequestDto.getCustomCorpName());
        }
        summary.append("<br/>");
        summary.append("补提了");
        WorkType workType = workTypeService.getById(workRequestDto.getWorkType());
        if (workType != null) {
            summary.append(workType.getName());
        }
        summary.append("工单");
        return summary.toString();
    }

    /**
     * 修改工单描述
     *
     * @param workRequestDto
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2020/2/26 20:21
     */
    private String findSummaryByMod(WorkRequestDto workRequestDto, WorkOperate workOperate) {
        StringBuilder summary = new StringBuilder(32);
        String userName = this.findUserNameById(workOperate.getOperator());
        if (StrUtil.isNotBlank(userName)) {
            summary.append(userName);
        }
        summary.append("修改了");
        WorkType workType = workTypeService.getById(workRequestDto.getWorkType());
        if (workType != null) {
            summary.append(workType.getName());
        }
        summary.append("工单");
        return summary.toString();
    }

    /**
     * 重新提交工单描述
     *
     * @param workRequestDto
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2020/3/2 9:20
     */
    private String findSummaryByResubmit(WorkRequestDto workRequestDto, WorkOperate workOperate) {
        StringBuilder summary = new StringBuilder(32);
        String userName = this.findUserNameById(workOperate.getOperator());
        if (StrUtil.isNotBlank(userName)) {
            summary.append(userName);
        }
        if (StrUtil.isNotBlank(workRequestDto.getCustomCorpName())) {
            summary.append("为客户 ");
            summary.append(workRequestDto.getCustomCorpName());
        }
        summary.append("<br/>修改了");
        WorkType workType = workTypeService.getById(workRequestDto.getWorkType());
        if (workType != null) {
            summary.append(workType.getName());
        }
        summary.append("工单并重新提交");
        if (StrUtil.isNotBlank(workRequestDto.getResubmitWorkCode())) {
            summary.append(" <br/>新工单号为").append(workRequestDto.getResubmitWorkCode());
        }
        return summary.toString();
    }

    /**
     * 补提交工单描述
     *
     * @param workRequestDto
     * @param workOperate
     * @return
     * @author ljzhu
     * @date 2020/3/8
     */
    private String findSummaryBySupplementWork(WorkRequestDto workRequestDto, WorkOperate workOperate) {
        StringBuilder summary = new StringBuilder(32);
        String userName = this.findUserNameById(workOperate.getOperator());
        if (StrUtil.isNotBlank(userName)) {
            summary.append(userName);
        }
        if (StrUtil.isNotBlank(workRequestDto.getCustomCorpName())) {
            summary.append("为客户 ");
            summary.append(workRequestDto.getCustomCorpName());
        }
        summary.append("<br/>补提了");
        WorkType workType = workTypeService.getById(workRequestDto.getWorkType());
        if (workType != null) {
            summary.append(workType.getName());
        }
        summary.append("工单");
        String service = this.findSummaryByService(workOperate);

        if (StrUtil.isNotBlank(service)) {
            summary.append(" <br/>并且").append(service);
        }
        return summary.toString();
    }

    /**
     * 客户撤单描述
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/10/31 19:25
     **/
    private String findSummaryByCustomRecall(WorkOperate workOperate) {
        StringBuilder summary = new StringBuilder(32);
        String userName = this.findUserNameById(workOperate.getOperator());
        if (StrUtil.isNotBlank(userName)) {
            summary.append(userName);
        }
        summary.append("进行了客户撤单操作。");
        if (StrUtil.isNotBlank(workOperate.getSummary())) {
            summary.append("撤单原因：").append(workOperate.getSummary());
        }
        return summary.toString();
    }

    /**
     * 客户催单描述
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/10/31 19:25
     **/
    private String findSummaryByCustomReminder(WorkOperate workOperate) {
        StringBuilder summary = new StringBuilder(32);
        String userName = this.findUserNameById(workOperate.getOperator());
        if (StrUtil.isNotBlank(userName)) {
            summary.append(userName);
        }
        summary.append("催促工单");
        return summary.toString();
    }

    /**
     * 客户分配工单描述
     *
     * @param workOperate
     * @param workDeal
     * @return
     * @author zgpi
     * @date 2019/10/31 19:25
     **/
    private String findSummaryByDispatch(WorkOperate workOperate, WorkDeal workDeal) {
        StringBuilder summary = new StringBuilder(32);
        String userName = this.findUserNameById(workOperate.getOperator());
        if (StrUtil.isNotBlank(userName)) {
            summary.append(userName);
        }
        Result corpResult = uasFeignService.findCorpById(workDeal.getServiceCorp());
        CorpDto corpDto = JsonUtil.parseObject(JsonUtil.toJson(corpResult.getData()), CorpDto.class);
        if (corpDto != null && StrUtil.isNotBlank(corpDto.getCorpName())) {
            summary.append("将工单提交给服务商 ");
            summary.append(corpDto.getCorpName());
        }
        return summary.toString();
    }

    /**
     * 服务商客服退回工单描述
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/10/31 19:35
     **/
    private String findSummaryByServiceReturn(WorkOperate workOperate) {
        StringBuilder summary = new StringBuilder(32);
        String userName = this.findUserNameById(workOperate.getOperator());
        if (StrUtil.isNotBlank(userName)) {
            summary.append(userName);
        }
        summary.append("退回工单。");
        if (StrUtil.isNotBlank(workOperate.getSummary())) {
            summary.append("退单原因：").append(workOperate.getSummary());
        }
        return summary.toString();
    }

    /**
     * 服务商客服受理工单描述
     *
     * @param workOperate
     * @param workDeal
     * @return
     * @author zgpi
     * @date 2019/10/31 19:32
     **/
    private String findSummaryByHandle(WorkOperate workOperate, WorkDeal workDeal) {
        StringBuilder summary = new StringBuilder(32);
        String userName = this.findUserNameById(workOperate.getOperator());
        if (StrUtil.isNotBlank(userName)) {
            summary.append(userName);
        }
        ServiceBranch serviceBranch = serviceBranchService.getById(workDeal.getServiceBranch());
        if (serviceBranch != null && StrUtil.isNotBlank(serviceBranch.getBranchName())) {
            summary.append("分配给服务网点 ");
            summary.append(serviceBranch.getBranchName());
        }
        return summary.toString();
    }

    /**
     * 服务商客服转处理工单描述
     *
     * @param workOperate
     * @param workDeal
     * @return
     * @author zgpi
     * @date 2019/10/31 19:32
     **/
    private String findSummaryByTurnHandle(WorkOperate workOperate, WorkDeal workDeal) {
        StringBuilder summary = new StringBuilder(32);
        String userName = this.findUserNameById(workOperate.getOperator());
        if (StrUtil.isNotBlank(userName)) {
            summary.append(userName);
        }
        ServiceBranch serviceBranch = serviceBranchService.getById(workDeal.getServiceBranch());
        if (serviceBranch != null && StrUtil.isNotBlank(serviceBranch.getBranchName())) {
            summary.append("转分派给服务网点 ");
            summary.append(serviceBranch.getBranchName());
        }
        return summary.toString();
    }

    /**
     * 派工描述
     *
     * @param workOperate
     * @param workAssignDto
     * @return
     * @author zgpi
     * @date 2019/10/31 19:25
     **/
    private String findSummaryByAssign(WorkOperate workOperate, WorkAssignDto workAssignDto) {
        StringBuilder summary = new StringBuilder(32);
        String userName = this.findUserNameById(workOperate.getOperator());
        if (StrUtil.isNotBlank(userName)) {
            summary.append(userName);
        }
        if (CollectionUtil.isNotEmpty(workAssignDto.getAssignEngineerList())) {
            summary.append("派工给 ");
            for (WorkAssignEngineerDto workAssignEngineerDto : workAssignDto.getAssignEngineerList()) {
                if (StrUtil.isNotBlank(workAssignEngineerDto.getUserName())) {
                    summary.append("工程师");
                    summary.append(workAssignEngineerDto.getUserName()).append(" ");
                }
            }
        }
        return summary.toString();
    }

    /**
     * 自动派工描述
     *
     * @param workOperate
     * @param workAssignDto
     * @return
     * @author ljzhu
     **/
    private String findSummaryByAutoAssign(WorkOperate workOperate, WorkAssignDto workAssignDto) {
        StringBuilder summary = new StringBuilder(32);
        if (CollectionUtil.isNotEmpty(workAssignDto.getAssignEngineerList())) {
            summary.append("自动派工给 ");
            for (WorkAssignEngineerDto workAssignEngineerDto : workAssignDto.getAssignEngineerList()) {
                if (StrUtil.isNotBlank(workAssignEngineerDto.getUserName())) {
                    summary.append("工程师");
                    summary.append(workAssignEngineerDto.getUserName()).append(" ");
                }
            }
        }
        return summary.toString();
    }


    /**
     * 撤回派工描述
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/10/31 19:38
     **/
    private String findSummaryByRecallAssign(WorkOperate workOperate) {
        StringBuilder summary = new StringBuilder(32);
        String userName = this.findUserNameById(workOperate.getOperator());
        if (StrUtil.isNotBlank(userName)) {
            summary.append(userName);
        }
        summary.append("撤回派工。");
        if (StrUtil.isNotBlank(workOperate.getSummary())) {
            summary.append("撤回原因：").append(workOperate.getSummary());
        }
        return summary.toString();
    }

    /**
     * 认领工单描述
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/10/31 19:38
     **/
    private String findSummaryByClaim(WorkOperate workOperate) {
        StringBuilder summary = new StringBuilder(32);
        String userName = this.findUserNameById(workOperate.getOperator());
        if (StrUtil.isNotBlank(userName)) {
            summary.append(userName);
        }
        return summary.toString();
    }

    /**
     * 拒绝派工描述
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/10/31 19:38
     **/
    private String findSummaryByRefuseAssign(WorkOperate workOperate) {
        StringBuilder summary = new StringBuilder(32);
        String userName = this.findUserNameById(workOperate.getOperator());
        if (StrUtil.isNotBlank(userName)) {
            summary.append(userName);
        }
        summary.append("拒绝派工。");
        if (StrUtil.isNotBlank(workOperate.getSummary())) {
            summary.append("拒绝原因：").append(workOperate.getSummary());
        }
        return summary.toString();
    }

    /**
     * 退回派工描述
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/10/31 19:38
     **/
    private String findSummaryByReturnAssign(WorkOperate workOperate) {
        StringBuilder summary = new StringBuilder(32);
        String userName = this.findUserNameById(workOperate.getOperator());
        if (StrUtil.isNotBlank(userName)) {
            summary.append(userName);
        }
        summary.append("退回派工。");
        if (StrUtil.isNotBlank(workOperate.getSummary())) {
            summary.append("退回原因：").append(workOperate.getSummary());
        }
        return summary.toString();
    }

    /**
     * 修改预约时间描述
     *
     * @param workOperate
     * @return
     * @author zgpi
     * @date 2019/10/30 14:57
     **/
    private String findSummaryByChangeBookingTime(WorkOperate workOperate) {
        StringBuilder summary = new StringBuilder(32);
        String userName = this.findUserNameById(workOperate.getOperator());
        if (StrUtil.isNotBlank(userName)) {
            summary.append(userName).append("更改了上门服务时间");
        }
        return summary.toString();
    }

    /**
     * 签到描述
     *
     * @param workOperate 操作记录
     * @return
     * @author zgpi
     * @date 2019/10/31 19:25
     **/
    private String findSummaryBySign(WorkOperate workOperate, WorkSign workSign) {
        StringBuilder summary = new StringBuilder(32);
        String userName = this.findUserNameById(workOperate.getOperator());
        if (StrUtil.isNotBlank(userName)) {
            if (StrUtil.isNotBlank(workSign.getSignAddress())) {
                summary.append(StrUtil.trimToEmpty(workSign.getSignAddress())).append(" ");
            }
            summary.append(userName);
            summary.append(" 已签到");
        }
        return summary.toString();
    }

    /**
     * 服务描述
     *
     * @param workOperate 操作记录
     * @return
     * @author zgpi
     * @date 2019/10/31 19:25
     **/
    private String findSummaryByService(WorkOperate workOperate) {
        StringBuilder summary = new StringBuilder(8);
        String userName = this.findUserNameById(workOperate.getOperator());
        if (workOperate.getOperateType() == WorkOperateTypeEnum.LOCATE_SERVICE.getCode()) {
            summary.append(userName).append(" 现场服务顺利完成");
        } else if (workOperate.getOperateType() == WorkOperateTypeEnum.REMOTE_SERVICE.getCode()) {
            if (StrUtil.isNotBlank(userName)) {
                summary.append(userName).append(" 远程服务顺利完成");
            }
        }
        return summary.toString();
    }

    /**
     * 修改现场服务信息描述
     *
     * @param workOperate
     * @return
     */
    private String findSummaryByModLocaleFinish(WorkOperate workOperate) {
        StringBuilder summary = new StringBuilder(8);
        String userName = this.findUserNameById(workOperate.getOperator());
        if (!StringUtils.isEmpty(userName)) {
            summary.append(userName);
            summary.append(" 修改了现场服务完成信息");
        }
        return summary.toString();
    }

    /**
     * 修改现场服务信息描述
     *
     * @param workOperate
     * @return
     */
    private String findSummaryByModFiles(WorkOperate workOperate, boolean isModFiles, boolean isModSignature) {
        StringBuilder summary = new StringBuilder(8);
        String userName = this.findUserNameById(workOperate.getOperator());
        if (!StringUtils.isEmpty(userName)) {
            summary.append(userName);
            if(isModFiles) {
                summary.append("修改了补传附件的附件信息");
            }
            if(isModSignature) {
                if(isModFiles) {
                    summary.append("，");
                }
                summary.append("修改了补传附件的签名信息");
            }
        }
        return summary.toString();
    }

    /**
     * 修改工单费用描述
     *
     * @param workOperate
     * @return
     */
    private String findSummaryByModWorkFee(WorkOperate workOperate) {
        StringBuilder summary = new StringBuilder(8);
        String userName = this.findUserNameById(workOperate.getOperator());
        if (!StringUtils.isEmpty(userName)) {
            summary.append(userName);
            summary.append(" 修改了工单费用");
        }
        return summary.toString();
    }

    /**
     * 评价描述
     *
     * @param workOperate 操作记录
     * @return
     * @author zgpi
     * @date 2019/10/30 14:57
     **/
    private String findSummaryByEvaluate(WorkOperate workOperate) {
        StringBuilder summary = new StringBuilder(8);
        String userName = this.findUserNameById(workOperate.getOperator());
        if (StrUtil.isNotBlank(userName)) {
            summary.append(userName);
            summary.append(" 已评价");
        }
        return summary.toString();
    }

    /**
     * 服务商审核服务描述
     *
     * @param userName
     * @param workCheckDto
     * @return
     */
    private String findSummaryByServiceCheck(String userName, WorkCheckDto workCheckDto) {
        StringBuilder summary = new StringBuilder(8);
        if (StrUtil.isNotBlank(userName)) {
            summary.append(userName);
        }
        summary.append(" ");
        summary.append(StrUtil.trimToEmpty(ServiceCheckStatusEnum.getNameByCode(workCheckDto.getFinishCheckStatus())));
        if (StrUtil.isNotBlank(workCheckDto.getFinishCheckNote())) {
            summary.append("<br>")
                    .append(StrUtil.trimToEmpty(workCheckDto.getFinishCheckNote()));
        }
        return summary.toString();
    }

    /**
     * 服务商审核费用描述
     *
     * @param userName
     * @param workCheckDto
     * @return
     */
    private String findSummaryByFeeCheck(String userName, WorkCheckDto workCheckDto) {
        StringBuilder summary = new StringBuilder(8);
        if (!StringUtils.isEmpty(userName)) {
            summary.append(userName);
        }
        summary.append(" ").append(StrUtil.trimToEmpty(FeeCheckStatusEnum.getNameByCode(workCheckDto.getFeeCheckStatus())));
        if (StrUtil.isNotBlank(workCheckDto.getFeeCheckNote())) {
            summary.append("<br>")
                    .append(StrUtil.trimToEmpty(workCheckDto.getFeeCheckNote()));
        }
        return summary.toString();
    }

    /**
     * 委托商确认服务描述
     *
     * @param userName
     * @param workCheckDto
     * @return
     */
    private String findSummaryByServiceConfirm(String userName, WorkCheckDto workCheckDto) {
        StringBuilder summary = new StringBuilder(8);
        if (!StringUtils.isEmpty(userName)) {
            summary.append(userName);
        }
        summary.append(" ")
                .append(StrUtil.trimToEmpty(ServiceConfirmStatusEnum.getNameByCode(workCheckDto.getFinishConfirmStatus())));
        if (StrUtil.isNotBlank(workCheckDto.getFinishConfirmNote())) {
            summary.append("<br>")
                    .append(StrUtil.trimToEmpty(workCheckDto.getFinishConfirmNote()));
        }
        return summary.toString();
    }

    /**
     * 委托商确认费用描述
     *
     * @param userName
     * @param workCheckDto
     * @return
     */
    private String findSummaryByFeeConfirm(String userName, WorkCheckDto workCheckDto) {
        StringBuilder summary = new StringBuilder(8);
        if (!StringUtils.isEmpty(userName)) {
            summary.append(userName);
        }
        summary.append(" ")
                .append(StrUtil.trimToEmpty(ServiceConfirmStatusEnum.getNameByCode(workCheckDto.getFeeConfirmStatus())));
        if (StrUtil.isNotBlank(workCheckDto.getFeeConfirmNote())) {
            summary.append("<br>")
                    .append(StrUtil.trimToEmpty(workCheckDto.getFeeConfirmNote()));
        }
        return summary.toString();
    }

    /**
     * 获得人员姓名
     *
     * @param userId 人员编号
     * @return
     * @author zgpi
     * @date 2019/11/13 18:27
     **/
    private String findUserNameById(Long userId) {
        Result userRealResult = uasFeignService.findUserRealDtoById(userId);
        UserRealDto userRealDto = JsonUtil.parseObject(JsonUtil.toJson(userRealResult.getData()), UserRealDto.class);
        if (userRealDto != null && StrUtil.isNotBlank(userRealDto.getUserName())) {
            return userRealDto.getUserName();
        }
        return "";
    }
}
