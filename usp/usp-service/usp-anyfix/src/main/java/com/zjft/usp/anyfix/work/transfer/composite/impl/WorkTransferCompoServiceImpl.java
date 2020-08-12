package com.zjft.usp.anyfix.work.transfer.composite.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjft.usp.anyfix.baseinfo.enums.EnabledEnum;
import com.zjft.usp.anyfix.baseinfo.model.CustomReason;
import com.zjft.usp.anyfix.baseinfo.model.ServiceReason;
import com.zjft.usp.anyfix.baseinfo.service.CustomReasonService;
import com.zjft.usp.anyfix.baseinfo.service.ServiceReasonService;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderServiceDto;
import com.zjft.usp.anyfix.corp.manage.filter.DemanderServiceFilter;
import com.zjft.usp.anyfix.corp.manage.service.DemanderServiceService;
import com.zjft.usp.anyfix.work.assign.model.WorkAssign;
import com.zjft.usp.anyfix.work.assign.model.WorkAssignEngineer;
import com.zjft.usp.anyfix.work.assign.service.WorkAssignEngineerService;
import com.zjft.usp.anyfix.work.assign.service.WorkAssignService;
import com.zjft.usp.anyfix.work.check.enums.ServiceConfirmStatusEnum;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.operate.dto.WorkOperateDto;
import com.zjft.usp.anyfix.work.operate.model.WorkOperate;
import com.zjft.usp.anyfix.work.operate.service.WorkOperateService;
import com.zjft.usp.anyfix.work.request.enums.ServiceModeEnum;
import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
import com.zjft.usp.anyfix.work.transfer.composite.WorkTransferCompoService;
import com.zjft.usp.anyfix.work.transfer.dto.WorkTransferDto;
import com.zjft.usp.anyfix.work.transfer.enums.WorkTransferEnum;
import com.zjft.usp.anyfix.work.transfer.model.WorkTransfer;
import com.zjft.usp.anyfix.work.transfer.service.WorkTransferService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.mq.util.MqSenderUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class WorkTransferCompoServiceImpl implements WorkTransferCompoService {

    @Autowired
    private WorkTransferService workTransferService;
    @Autowired
    private WorkDealService workDealService;
    @Autowired
    private WorkAssignService workAssignService;
    @Autowired
    private WorkAssignEngineerService workAssignEngineerService;
    @Autowired
    private DemanderServiceService demanderServiceService;
    @Autowired
    private WorkOperateService workOperateService;
    @Autowired
    private ServiceReasonService serviceReasonService;
    @Autowired
    private CustomReasonService custocmReasonService;

    @Autowired
    private MqSenderUtil mqSenderUtil;


    /**
     * 客户撤单
     *
     * @param workTransferDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/2/27 18:12
     */
    @Override
    public void recallWorkByCustom(WorkTransferDto workTransferDto, UserInfo userInfo, ReqParam reqParam) {
        if (IntUtil.isZero(workTransferDto.getReasonId()) && StrUtil.isBlank(workTransferDto.getNote())) {
            throw new AppException("撤单原因不能为空！");
        }
        if (workTransferDto.getWorkId() == null || workTransferDto.getWorkId() == 0L) {
            throw new AppException("工单编号不能为空！");
        }
        WorkDeal workDeal = workDealService.getById(workTransferDto.getWorkId());
        if (workDeal == null) {
            throw new AppException("工单不存在！");
        }
        if (workDeal.getWorkStatus() != WorkStatusEnum.TO_DISPATCH.getCode()
                && workDeal.getWorkStatus() != WorkStatusEnum.RETURNED.getCode()) {
            throw new AppException("工单状态不正确，当前状态为[" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "]！");
        }
        if (StrUtil.isBlank(workTransferDto.getNote())) {
            CustomReason customReason = custocmReasonService.getById(workTransferDto.getReasonId());
            if (customReason != null) {
                workTransferDto.setNote(customReason.getName());
            }
        }

        WorkTransfer workTransfer = new WorkTransfer();
        BeanUtils.copyProperties(workTransferDto, workTransfer);
        workTransfer.setId(KeyUtil.getId());
        workTransfer.setMode(WorkTransferEnum.CUSTOM_RECALL.getCode());
        workTransfer.setLat(reqParam.getLat());
        workTransfer.setLon(reqParam.getLon());
        workTransfer.setOperator(userInfo.getUserId());
        workTransfer.setOperateTime(DateUtil.date().toTimestamp());
        workTransferService.save(workTransfer);

        // 修改工单处理信息表
        workDeal.setWorkStatus(WorkStatusEnum.CANCELED.getCode());
        workDeal.setRecallStaff(userInfo.getUserId());
        workDeal.setRecallTime(DateUtil.date());
        workDealService.updateById(workDeal);

        // 添加操作记录
        WorkOperate workOperate = new WorkOperate();
        workOperate.setWorkId(workTransfer.getWorkId());
        workOperate.setOperator(userInfo.getUserId());
        workOperate.setCorp(reqParam.getCorpId());
        workOperate.setSummary(workTransferDto.getNote());
        workOperateService.addWorkOperateByCustomRecall(workOperate);
    }

    /**
     * 客户手工分配工单
     *
     * @param workTransferDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/11/14 14:48
     **/
    @Override
    public void dispatchWorkByCustom(WorkTransferDto workTransferDto, UserInfo userInfo, ReqParam reqParam) {
        if (workTransferDto.getWorkId() == null || workTransferDto.getWorkId() == 0L) {
            throw new AppException("工单编号不能为空！");
        }
        if (workTransferDto.getServiceCorp() == null || workTransferDto.getServiceCorp() == 0L) {
            throw new AppException("服务商不能为空！");
        }
        WorkDeal workDeal = workDealService.getById(workTransferDto.getWorkId());
        if (workDeal == null) {
            throw new AppException("工单不存在！");
        }
        DemanderServiceFilter demanderServiceFilter = new DemanderServiceFilter();
        demanderServiceFilter.setDemanderCorp(workDeal.getDemanderCorp());
        demanderServiceFilter.setEnabled(EnabledEnum.YES.getCode());
        List<DemanderServiceDto> demanderServiceDtoList = demanderServiceService.listServiceByDemander(demanderServiceFilter);
        List<Long> corpIdList = demanderServiceDtoList.stream().map(e -> e.getServiceCorp()).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(corpIdList)) {
            throw new AppException("请选择可用的服务商！");
        }
        if (workDeal.getWorkStatus() != WorkStatusEnum.TO_DISPATCH.getCode()) {
            throw new AppException("工单状态不正确，当前状态为[" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "]！");
        }
        WorkTransfer workTransfer = new WorkTransfer();
        BeanUtils.copyProperties(workTransferDto, workTransfer);
        workTransfer.setId(KeyUtil.getId());
        workTransfer.setMode(WorkTransferEnum.MANUAL_DISPATCH.getCode());
        workTransfer.setLat(reqParam.getLat());
        workTransfer.setLon(reqParam.getLon());
        workTransfer.setOperator(userInfo.getUserId());
        workTransfer.setOperateTime(DateUtil.date().toTimestamp());
        workTransfer.setServiceCorp(workTransferDto.getServiceCorp());
        workTransfer.setServiceBranch(workTransferDto.getServiceBranch());
        workTransferService.save(workTransfer);

        //修改工单处理信息表
        workDeal.setWorkStatus(WorkStatusEnum.TO_HANDLE.getCode());
        workDeal.setServiceCorp(workTransferDto.getServiceCorp());
        workDeal.setServiceBranch(workTransferDto.getServiceBranch());
        workDeal.setDispatchStaff(userInfo.getUserId());
        workDeal.setDispatchTime(DateUtil.date().toTimestamp());
        workDealService.updateById(workDeal);

        // 添加操作记录
        WorkOperate workOperate = new WorkOperate();
        workOperate.setWorkId(workTransfer.getWorkId());
        workOperate.setOperator(userInfo.getUserId());
        workOperate.setCorp(reqParam.getCorpId());
        workOperateService.addWorkOperateByDispatch(workOperate, workDeal);
    }

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
    @Override
    public void returnWorkByService(WorkTransferDto workTransferDto, UserInfo userInfo, ReqParam reqParam) {
        if (IntUtil.isZero(workTransferDto.getReasonId()) && StrUtil.isBlank(workTransferDto.getNote())) {
            throw new AppException("退单原因不能为空！");
        }
        if (workTransferDto.getWorkId() == null || workTransferDto.getWorkId() == 0L) {
            throw new AppException("工单编号不能为空！");
        }
        WorkDeal workDeal = workDealService.getById(workTransferDto.getWorkId());
        if (workDeal == null) {
            throw new AppException("工单不存在！");
        }
        if (workDeal.getWorkStatus() != WorkStatusEnum.TO_HANDLE.getCode()
                && workDeal.getWorkStatus() != WorkStatusEnum.TO_ASSIGN.getCode()) {
            throw new AppException("工单状态不正确，当前状态为[" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "]！");
        }
        if (StrUtil.isBlank(workTransferDto.getNote())) {
            ServiceReason serviceReason = serviceReasonService.getById(workTransferDto.getReasonId());
            if (serviceReason != null) {
                workTransferDto.setNote(serviceReason.getName());
            }
        }
        WorkTransfer workTransfer = new WorkTransfer();
        BeanUtils.copyProperties(workTransferDto, workTransfer);
        workTransfer.setId(KeyUtil.getId());
        workTransfer.setMode(WorkTransferEnum.MANUAL_RETURN.getCode());
        workTransfer.setLat(reqParam.getLat());
        workTransfer.setLon(reqParam.getLon());
        workTransfer.setOperator(userInfo.getUserId());
        workTransfer.setOperateTime(DateUtil.date());
        workTransferService.save(workTransfer);

        // 修改工单处理信息表
        workDeal.setWorkStatus(WorkStatusEnum.RETURNED.getCode());
        workDeal.setReturnStaff(userInfo.getUserId());
        workDeal.setReturnTime(DateUtil.date());
        workDeal.setFinishConfirmStatus(ServiceConfirmStatusEnum.UN_CONFIRM.getCode());
        workDealService.updateById(workDeal);

        //添加操作记录
        WorkOperate workOperate = new WorkOperateDto();
        workOperate.setWorkId(workTransfer.getWorkId());
        workOperate.setOperator(userInfo.getUserId());
        workOperate.setCorp(reqParam.getCorpId());
        workOperate.setSummary(workTransferDto.getNote());
        workOperateService.addWorkOperateByServiceAssignReturn(workOperate);
    }

    /**
     * 客服手工受理工单
     *
     * @param workTransferDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/11/13 10:47
     **/
    @Override
    public void handleWorkByManual(WorkTransferDto workTransferDto, UserInfo userInfo, ReqParam reqParam) {
        if (workTransferDto.getWorkId() == null || workTransferDto.getWorkId() == 0L) {
            throw new AppException("工单编号不能为空！");
        }
        if (workTransferDto.getServiceBranch() == null || workTransferDto.getServiceBranch() == 0L) {
            throw new AppException("服务网点不能为空！");
        }
        WorkDeal workDeal = workDealService.getById(workTransferDto.getWorkId());
        if (workDeal == null) {
            throw new AppException("工单不存在！");
        }
        if (workDeal.getWorkStatus() != WorkStatusEnum.TO_HANDLE.getCode()) {
            throw new AppException("工单状态不正确，当前状态为[" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "]！");
        }
        if (!workDeal.getServiceCorp().equals(reqParam.getCorpId())) {
            throw new AppException("分配的服务商不是当前企业，不能受理工单！");
        }
        WorkTransfer workTransfer = new WorkTransfer();
        BeanUtils.copyProperties(workTransferDto, workTransfer);
        workTransfer.setId(KeyUtil.getId());
        workTransfer.setMode(WorkTransferEnum.MANUAL_HANDLE.getCode());
        workTransfer.setLat(reqParam.getLat());
        workTransfer.setLon(reqParam.getLon());
        workTransfer.setOperator(userInfo.getUserId());
        workTransfer.setOperateTime(DateUtil.date().toTimestamp());
        workTransferService.save(workTransfer);

        //修改工单处理信息表
        workDeal.setWorkStatus(WorkStatusEnum.TO_ASSIGN.getCode());
        // 默认现场服务
        workDeal.setServiceMode(ServiceModeEnum.LOCALE_SERVICE.getCode());
        workDeal.setServiceBranch(workTransferDto.getServiceBranch());
        workDeal.setHandleStaff(userInfo.getUserId());
        workDeal.setHandleTime(DateUtil.date().toTimestamp());
        workDealService.updateById(workDeal);

        // 添加操作记录
        WorkOperate workOperate = new WorkOperateDto();
        workOperate.setWorkId(workTransfer.getWorkId());
        workOperate.setOperator(userInfo.getUserId());
        workOperate.setCorp(reqParam.getCorpId());
        workOperateService.addWorkOperateByHandle(workOperate, workDeal);
    }

    /**
     * 客服转处理工单
     *
     * @param workTransferDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/11/15 13:35
     **/
    @Override
    public void turnHandleWork(WorkTransferDto workTransferDto, UserInfo userInfo, ReqParam reqParam) {
        if (LongUtil.isZero(workTransferDto.getWorkId())) {
            throw new AppException("工单编号不能为空！");
        }
        if (LongUtil.isZero(workTransferDto.getServiceBranch())) {
            throw new AppException("服务网点不能为空！");
        }
        WorkDeal workDeal = workDealService.getById(workTransferDto.getWorkId());
        if (workDeal == null) {
            throw new AppException("工单不存在！");
        }
        if (workDeal.getWorkStatus() != WorkStatusEnum.TO_ASSIGN.getCode()) {
            throw new AppException("工单状态不正确，当前状态为[" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "]！");
        }

        if (workDeal.getServiceBranch().equals(workTransferDto.getServiceBranch())) {
            throw new AppException("转处理后的服务网点和之前相同，不能进行转处理操作！");
        }

        WorkTransfer workTransfer = new WorkTransfer();
        BeanUtils.copyProperties(workTransferDto, workTransfer);
        workTransfer.setId(KeyUtil.getId());
        workTransfer.setMode(WorkTransferEnum.MANUAL_TURN_HANDLE.getCode());
        workTransfer.setLat(reqParam.getLat());
        workTransfer.setLon(reqParam.getLon());
        workTransfer.setOperator(userInfo.getUserId());
        workTransfer.setOperateTime(DateUtil.date().toTimestamp());
        workTransfer.setServiceCorp(workTransferDto.getServiceCorp());
        workTransfer.setServiceBranch(workTransferDto.getServiceBranch());
        workTransferService.save(workTransfer);

        //修改工单处理信息表
        workDeal.setServiceBranch(workTransferDto.getServiceBranch());
        workDeal.setHandleStaff(userInfo.getUserId());
        workDeal.setHandleTime(DateUtil.date().toTimestamp());
        workDealService.updateById(workDeal);

        // 添加操作记录
        WorkOperate workOperate = new WorkOperateDto();
        workOperate.setWorkId(workTransfer.getWorkId());
        workOperate.setOperator(userInfo.getUserId());
        workOperate.setCorp(reqParam.getCorpId());
        workOperate.setWorkStatus(WorkStatusEnum.TO_ASSIGN.getCode());
        workOperateService.addWorkOperateByTurnHandle(workOperate, workDeal);
    }

    /**
     * 派单主管撤回派单
     *
     * @param workTransferDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/10/14 9:26 上午
     **/
    @Override
    public void recallAssign(WorkTransferDto workTransferDto, UserInfo userInfo, ReqParam reqParam) {
        if (IntUtil.isZero(workTransferDto.getReasonId()) && StrUtil.isBlank(workTransferDto.getNote())) {
            throw new AppException("撤回派单原因不能为空！");
        }
        if (LongUtil.isZero(workTransferDto.getWorkId())) {
            throw new AppException("工单编号不能为空！");
        }
        WorkDeal workDeal = workDealService.getById(workTransferDto.getWorkId());
        if (workDeal == null) {
            throw new AppException("工单不存在！");
        }
        if (workDeal.getWorkStatus() != WorkStatusEnum.TO_CLAIM.getCode()) {
            throw new AppException("工单状态不正确，当前状态为[" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "]！");
        }
        if (StrUtil.isBlank(workTransferDto.getNote())) {
            ServiceReason serviceReason = serviceReasonService.getById(workTransferDto.getReasonId());
            if (serviceReason != null) {
                workTransferDto.setNote(serviceReason.getName());
            }
        }

        WorkTransfer workTransfer = new WorkTransfer();
        BeanUtils.copyProperties(workTransferDto, workTransfer);
        workTransfer.setId(KeyUtil.getId());
        workTransfer.setMode(WorkTransferEnum.ASSIGN_RECALL.getCode());
        workTransfer.setLat(reqParam.getLat());
        workTransfer.setLon(reqParam.getLon());
        workTransfer.setOperator(userInfo.getUserId());
        workTransfer.setOperateTime(DateUtil.date().toTimestamp());
        workTransferService.save(workTransfer);

        //修改工单处理信息表
        workDeal.setWorkStatus(WorkStatusEnum.TO_ASSIGN.getCode());
        workDeal.setEngineer(0L);
        workDealService.updateById(workDeal);

        // 将派工置为无效
        workAssignService.modAssignNotEnable(workDeal.getWorkId());

        // 添加操作记录
        WorkOperate workOperate = new WorkOperateDto();
        workOperate.setWorkId(workTransfer.getWorkId());
        workOperate.setOperator(userInfo.getUserId());
        workOperate.setCorp(reqParam.getCorpId());
        workOperate.setSummary(workTransferDto.getNote());
        workOperateService.addWorkOperateByRecallAssign(workOperate);
    }

    /**
     * 工程师认领工单
     *
     * @param workTransferDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @throws
     * @author zphu
     * @date 2019/9/29 15:33
     **/
    @Override
    public void claimWork(WorkTransferDto workTransferDto, UserInfo userInfo, ReqParam reqParam) {
        if (LongUtil.isZero(workTransferDto.getWorkId())) {
            throw new AppException("工单编号不能为空！");
        }
        WorkDeal workDeal = workDealService.getById(workTransferDto.getWorkId());
        if (workDeal == null) {
            throw new AppException("工单不存在！");
        }
        if (workDeal.getWorkStatus() != WorkStatusEnum.TO_CLAIM.getCode()) {
            throw new AppException("工单状态不正确，当前状态为[" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "]！");
        }
        List<WorkAssign> workAssignList = workAssignService.list(new QueryWrapper<WorkAssign>()
                .eq("work_id", workTransferDto.getWorkId()).eq("enabled", EnabledEnum.YES.getCode()));
        if (CollectionUtil.isEmpty(workAssignList)) {
            throw new AppException("该工单还未进行派单！");
        }
        List<Long> assignIdList = workAssignList.stream().map(e -> e.getAssignId()).collect(Collectors.toList());
        List<WorkAssignEngineer> workAssignEngineerList = workAssignEngineerService
                .list(new QueryWrapper<WorkAssignEngineer>().in("assign_id", assignIdList));
        if (CollectionUtil.isEmpty(workAssignEngineerList)) {
            throw new AppException("该工单还未进行派单！");
        }
        List<Long> idList = workAssignEngineerList.stream().map(e -> e.getEngineerId()).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(idList) || !idList.contains(userInfo.getUserId())) {
            throw new AppException("该工单未派单给您，不能接单！");
        }

        WorkTransfer workTransfer = new WorkTransfer();
        BeanUtils.copyProperties(workTransferDto, workTransfer);
        workTransfer.setId(KeyUtil.getId());
        workTransfer.setMode(WorkTransferEnum.CLAIM_WORK.getCode());
        workTransfer.setLat(reqParam.getLat());
        workTransfer.setLon(reqParam.getLon());
        workTransfer.setOperator(userInfo.getUserId());
        workTransfer.setOperateTime(DateUtil.date());
        workTransferService.save(workTransfer);

        //修改工单处理信息表
        if (workTransferDto.getGoTime() != null) {
            workDeal.setGoTime(workTransferDto.getGoTime());
        }
        workDeal.setWorkStatus(WorkStatusEnum.TO_SIGN.getCode());
        workDeal.setEngineer(userInfo.getUserId());
        workDeal.setAcceptTime(DateUtil.date());
        workDealService.updateById(workDeal);

        // 添加操作记录
        WorkOperate workOperate = new WorkOperateDto();
        workOperate.setWorkId(workTransfer.getWorkId());
        workOperate.setOperator(userInfo.getUserId());
        workOperate.setCorp(reqParam.getCorpId());
        workOperateService.addWorkOperateByClaim(workOperate);
    }

    /**
     * 工程师拒绝派单
     *
     * @param workTransferDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @throws
     * @author zphu
     * @date 2019/9/30 8:59
     **/
    @Override
    public void refuseAssign(WorkTransferDto workTransferDto, UserInfo userInfo, ReqParam reqParam) {
        if (IntUtil.isZero(workTransferDto.getReasonId()) && StrUtil.isBlank(workTransferDto.getNote())) {
            throw new AppException("拒绝派单原因不能为空！");
        }
        if (LongUtil.isZero(workTransferDto.getWorkId())) {
            throw new AppException("工单编号不能为空！");
        }
        WorkDeal workDeal = workDealService.getById(workTransferDto.getWorkId());
        if (workDeal == null) {
            throw new AppException("工单不存在！");
        }
        if (workDeal.getWorkStatus() != WorkStatusEnum.TO_CLAIM.getCode()) {
            throw new AppException("工单状态不正确，当前状态为[" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "]！");
        }
        WorkAssign workAssign = workAssignService.getOne(new QueryWrapper<WorkAssign>()
                .eq("work_id", workTransferDto.getWorkId()).eq("enabled", EnabledEnum.YES.getCode()));
        if (workAssign == null) {
            throw new AppException("该工单还未进行派工！");
        }
        List<WorkAssignEngineer> workAssignEngineerList = workAssignEngineerService
                .list(new QueryWrapper<WorkAssignEngineer>().eq("assign_id", workAssign.getAssignId()));
        if (workAssignEngineerList == null || workAssignEngineerList.size() == 0) {
            throw new AppException("该工单还未进行派工！");
        }
        List<Long> idList = workAssignEngineerList.stream().map(e -> e.getEngineerId()).collect(Collectors.toList());
        if (idList == null || !idList.contains(userInfo.getUserId())) {
            throw new AppException("该工单未派工给您，不能拒绝派工！");
        }
        if (StrUtil.isBlank(workTransferDto.getNote())) {
            ServiceReason serviceReason = serviceReasonService.getById(workTransferDto.getReasonId());
            if (serviceReason != null) {
                workTransferDto.setNote(serviceReason.getName());
            }
        }

        WorkTransfer workTransfer = new WorkTransfer();
        BeanUtils.copyProperties(workTransferDto, workTransfer);
        workTransfer.setId(KeyUtil.getId());
        workTransfer.setMode(WorkTransferEnum.REFUSE_WORK.getCode());
        workTransfer.setLat(reqParam.getLat());
        workTransfer.setLon(reqParam.getLon());
        workTransfer.setOperator(userInfo.getUserId());
        workTransfer.setOperateTime(DateUtil.date());
        workTransferService.save(workTransfer);

        //修改工单处理信息表
        workDeal.setWorkStatus(WorkStatusEnum.TO_ASSIGN.getCode());
        workDeal.setAssignMode(0);
        workDeal.setAssignStaff(0L);
        workDeal.setAssignTime(null);
        workDeal.setEngineer(0L);
        workDealService.updateById(workDeal);

        // 将派工置为无效
        workAssignService.modAssignNotEnable(workDeal.getWorkId());

        // 添加操作记录
        WorkOperate workOperate = new WorkOperateDto();
        workOperate.setWorkId(workTransfer.getWorkId());
        workOperate.setOperator(userInfo.getUserId());
        workOperate.setCorp(reqParam.getCorpId());
        workOperate.setSummary(workTransferDto.getNote());
        workOperateService.addWorkOperateByRefuseAssign(workOperate);
    }

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
    @Override
    public void returnAssign(WorkTransferDto workTransferDto, UserInfo userInfo, ReqParam reqParam) {
        if (IntUtil.isZero(workTransferDto.getReasonId()) && StrUtil.isBlank(workTransferDto.getNote())) {
            throw new AppException("退回原因不能为空！");
        }
        if (LongUtil.isZero(workTransferDto.getWorkId())) {
            throw new AppException("工单编号不能为空！");
        }
        WorkDeal workDeal = workDealService.getById(workTransferDto.getWorkId());
        if (workDeal == null) {
            throw new AppException("工单不存在！");
        }
        if (workDeal.getWorkStatus() != WorkStatusEnum.TO_SIGN.getCode()
                && workDeal.getWorkStatus() != WorkStatusEnum.TO_SERVICE.getCode()
                && workDeal.getWorkStatus() != WorkStatusEnum.IN_SERVICE.getCode()) {
            throw new AppException("工单状态不正确，当前状态为[" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "]！");
        }
        if (StrUtil.isBlank(workTransferDto.getNote())) {
            ServiceReason serviceReason = serviceReasonService.getById(workTransferDto.getReasonId());
            if (serviceReason != null) {
                workTransferDto.setNote(serviceReason.getName());
            }
        }
        WorkTransfer workTransfer = new WorkTransfer();
        BeanUtils.copyProperties(workTransferDto, workTransfer);
        workTransfer.setId(KeyUtil.getId());
        workTransfer.setMode(WorkTransferEnum.RETURN_WORK.getCode());
        workTransfer.setLat(reqParam.getLat());
        workTransfer.setLon(reqParam.getLon());
        workTransfer.setOperator(userInfo.getUserId());
        workTransfer.setOperateTime(DateUtil.date().toTimestamp());
        workTransfer.setServiceCorp(workDeal.getServiceCorp());
        workTransfer.setServiceBranch(workDeal.getServiceBranch());
        workTransferService.save(workTransfer);

        //修改工单处理信息表
        workDeal.setWorkStatus(WorkStatusEnum.TO_ASSIGN.getCode());
        workDeal.setAssignMode(0);
        workDeal.setAssignStaff(0L);
//        workDeal.setAssignTime(null);
        workDeal.setEngineer(0L);
        workDeal.setTraffic(0);
        workDeal.setTrafficNote("");
        workDeal.setTogetherEngineers("");
        workDeal.setHelpNames("");
        workDealService.updateById(workDeal);

        UpdateWrapper<WorkDeal> updateWrapper = new UpdateWrapper<WorkDeal>().set("assign_time", null)
                .set("accept_time", null).set("sign_time", null).set("go_time", null)
                .eq("work_id", workDeal.getWorkId());
        workDealService.update(updateWrapper);

        // 将派工置为无效
        workAssignService.modAssignNotEnable(workDeal.getWorkId());

        // 添加操作记录
        WorkOperate workOperate = new WorkOperateDto();
        workOperate.setWorkId(workTransfer.getWorkId());
        workOperate.setOperator(userInfo.getUserId());
        workOperate.setCorp(reqParam.getCorpId());
        workOperate.setSummary(workTransferDto.getNote());
        workOperateService.addWorkOperateByReturnAssign(workOperate);
    }

    /**
     * 派单主管退回工单
     *
     * @param workTransferDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author zphu
     * @date 2019/9/30 9:50
     **/
    @Override
    public void returnAssignByService(WorkTransferDto workTransferDto, UserInfo userInfo, ReqParam reqParam) {
        if (IntUtil.isZero(workTransferDto.getReasonId()) && StrUtil.isBlank(workTransferDto.getNote())) {
            throw new AppException("退单原因不能为空！");
        }
        if (LongUtil.isZero(workTransferDto.getWorkId())) {
            throw new AppException("工单编号不能为空！");
        }
        WorkDeal workDeal = workDealService.getById(workTransferDto.getWorkId());
        if (workDeal == null) {
            throw new AppException("工单不存在！");
        }
        if (workDeal.getWorkStatus() != WorkStatusEnum.TO_ASSIGN.getCode()) {
            throw new AppException("工单状态不正确，当前状态为[" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "]！");
        }
        if (StrUtil.isBlank(workTransferDto.getNote())) {
            ServiceReason serviceReason = serviceReasonService.getById(workTransferDto.getReasonId());
            if (serviceReason != null) {
                workTransferDto.setNote(serviceReason.getName());
            }
        }
        WorkTransfer workTransfer = new WorkTransfer();
        BeanUtils.copyProperties(workTransferDto, workTransfer);
        workTransfer.setId(KeyUtil.getId());
        workTransfer.setMode(WorkTransferEnum.RETURN_ASSIGN_SERVICE.getCode());
        workTransfer.setLat(reqParam.getLat());
        workTransfer.setLon(reqParam.getLon());
        workTransfer.setOperator(userInfo.getUserId());
        workTransfer.setOperateTime(DateUtil.date().toTimestamp());
        workTransfer.setServiceCorp(workDeal.getServiceCorp());
        /*workTransfer.setServiceBranch(workDeal.getServiceBranch());*/
        workTransferService.save(workTransfer);

        //修改工单处理信息表
        workDeal.setWorkStatus(WorkStatusEnum.TO_HANDLE.getCode());
        // 默认现场服务
        workDeal.setServiceMode(0);
        workDeal.setServiceBranch(0L);
        workDeal.setHandleStaff(0L);
        workDeal.setHandleTime(null);
        workDealService.updateById(workDeal);

        /*UpdateWrapper<WorkDeal> updateWrapper = new UpdateWrapper<WorkDeal>().set("assign_time", null)
                .set("accept_time", null).set("sign_time", null).set("go_time", null)
                .eq("work_id", workDeal.getWorkId());
        workDealService.update(updateWrapper);*/

        // 添加操作记录
        WorkOperate workOperate = new WorkOperateDto();
        workOperate.setWorkId(workTransfer.getWorkId());
        workOperate.setOperator(userInfo.getUserId());
        workOperate.setCorp(reqParam.getCorpId());
        workOperate.setSummary(workTransferDto.getNote());
        workOperateService.addWorkOperateByServiceAssignReturn(workOperate);
    }

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
    @Override
    public void checkWork(WorkTransferDto workTransferDto, UserInfo userInfo, ReqParam reqParam) {
        WorkDeal workDeal = workDealService.getById(workTransferDto.getWorkId());
        if (workDeal == null) {
            throw new AppException("工单不存在，请核查！");
        }
        if (WorkStatusEnum.TO_HANDLE.getCode() == workDeal.getWorkStatus()) {
            // 分配工单前检查
            if (!workTransferDto.getServiceCorp().equals(reqParam.getCorpId())) {
                throw new AppException("分配的服务商不是当前企业，不能分配工单！");
            }
        } else if (WorkStatusEnum.TO_SERVICE.getCode() == workDeal.getWorkStatus()
                || WorkStatusEnum.IN_SERVICE.getCode() == workDeal.getWorkStatus()) {
            // 服务前检查
            if (!workDeal.getEngineer().equals(userInfo.getUserId())) {
                throw new AppException("您不是该工单的维护工程师，不能服务！");
            }
        } else if (WorkStatusEnum.TO_EVALUATE.getCode() == workDeal.getWorkStatus()) {
            // 评价工单前检查
            if (!workDeal.getCreator().equals(userInfo.getUserId())
                    && !workDeal.getEngineer().equals(userInfo.getUserId())) {
                throw new AppException("您不是建单人或维护工程师，不能评价工单！");
            }
        }
    }

    /**
     * 添加消息到消息队列
     *
     * @param topic
     * @param workId
     * @return
     * @author zgpi
     * @date 2020/2/28 17:24
     */
    @Override
    public void addMessageQueue(String topic, Long workId) {
        Map<String, Object> msg = new HashMap<>(1);
        msg.put("workId", workId);
        mqSenderUtil.sendMessage(topic, JsonUtil.toJson(msg));
    }
}
