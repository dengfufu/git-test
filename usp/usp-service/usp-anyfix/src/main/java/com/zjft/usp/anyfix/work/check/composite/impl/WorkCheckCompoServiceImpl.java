package com.zjft.usp.anyfix.work.check.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjft.usp.anyfix.baseinfo.enums.EnabledEnum;
import com.zjft.usp.anyfix.common.constant.RightConstants;
import com.zjft.usp.anyfix.common.feign.dto.DeviceSmallClassDto;
import com.zjft.usp.anyfix.common.service.RightCompoService;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderAutoConfigDto;
import com.zjft.usp.anyfix.corp.manage.model.DemanderService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderAutoConfigService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderCustomService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderServiceManagerService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderServiceService;
import com.zjft.usp.anyfix.settle.enums.SettleTypeEnum;
import com.zjft.usp.anyfix.settle.enums.WorkSettleStatusEnum;
import com.zjft.usp.anyfix.settle.service.SettleDemanderService;
import com.zjft.usp.anyfix.work.check.composite.WorkCheckCompoService;
import com.zjft.usp.anyfix.work.check.dto.WorkCheckDto;
import com.zjft.usp.anyfix.work.check.enums.FeeCheckStatusEnum;
import com.zjft.usp.anyfix.work.check.enums.FeeConfirmStatusEnum;
import com.zjft.usp.anyfix.work.check.enums.ServiceCheckStatusEnum;
import com.zjft.usp.anyfix.work.check.enums.ServiceConfirmStatusEnum;
import com.zjft.usp.anyfix.work.check.model.WorkCheck;
import com.zjft.usp.anyfix.work.check.service.WorkCheckService;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeVerify;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeVerifyService;
import com.zjft.usp.anyfix.work.finish.enums.FilesStatusEnum;
import com.zjft.usp.anyfix.work.finish.enums.SignatureStatusEnum;
import com.zjft.usp.anyfix.work.finish.enums.WorkFeeStatusEnum;
import com.zjft.usp.anyfix.work.finish.model.WorkFinish;
import com.zjft.usp.anyfix.work.finish.service.WorkFinishService;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.anyfix.work.request.enums.WarrantyModeEnum;
import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
import com.zjft.usp.anyfix.work.request.model.WorkRequest;
import com.zjft.usp.anyfix.work.request.service.WorkRequestService;
import com.zjft.usp.anyfix.work.request.service.WorkRequestService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.feign.service.RightFeignService;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.device.service.DeviceFeignService;
import com.zjft.usp.msg.model.Message;
import com.zjft.usp.msg.model.TemplateMessage;
import com.zjft.usp.msg.service.PushService;
import com.zjft.usp.uas.service.UasFeignService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 工单审核聚合服务实现类
 *
 * @author zgpi
 * @date 2020/5/11 20:25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkCheckCompoServiceImpl implements WorkCheckCompoService {

    @Autowired
    private WorkRequestService workRequestService;
    @Autowired
    private WorkDealService workDealService;
    @Autowired
    private WorkCheckService workCheckService;
    @Autowired
    private WorkFinishService workFinishService;
    @Autowired
    private RightCompoService rightCompoService;
    @Autowired
    private DemanderServiceManagerService demanderServiceManagerService;
    @Autowired
    private DemanderServiceService demanderServiceService;
    @Autowired
    private WorkFeeVerifyService workFeeVerifyService;
    @Autowired
    private SettleDemanderService settleDemanderService;
    @Autowired
    private DemanderCustomService demanderCustomService;
    @Autowired
    private DemanderAutoConfigService demanderAutoConfigService;

    @Autowired
    private PushService pushService;

    @Resource
    private UasFeignService uasFeignService;
    @Resource
    private DeviceFeignService deviceFeignService;
    @Resource
    private RightFeignService rightFeignService;

    // 超时时间，定为 48 * 60 分钟
    private static final int BEYOND_MINUTES = 48 * 60;

    /**
     * 获得工单审核记录
     *
     * @param workId
     * @return
     * @author zgpi
     * @date 2020/5/12 16:36
     **/
    @Override
    public WorkCheckDto findWorkCheck(Long workId) {
        WorkCheck workCheck = workCheckService.getById(workId);
        WorkDeal workDeal = workDealService.getById(workId);
        WorkCheckDto workCheckDto = new WorkCheckDto();
        if (workCheck != null) {
            Set<Long> userIdSet = new HashSet<>();
            userIdSet.add(workCheck.getFinishCheckUser());
            userIdSet.add(workCheck.getFeeCheckUser());
            userIdSet.add(workCheck.getFinishConfirmUser());
            userIdSet.add(workCheck.getFeeConfirmUser());
            Map<Long, String> userMap = new HashMap<>();
            Result<Map<Long, String>> userMapResult = uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdSet));
            if (userMapResult != null && userMapResult.getCode() == Result.SUCCESS) {
                userMap = userMapResult.getData();
                userMap = userMap == null ? new HashMap<>() : userMap;
            }
            BeanUtils.copyProperties(workCheck, workCheckDto);
            workCheckDto.setFinishCheckUserName(StrUtil.trimToEmpty(userMap.get(workCheckDto.getFinishCheckUser())));
            workCheckDto.setFeeCheckUserName(StrUtil.trimToEmpty(userMap.get(workCheckDto.getFeeCheckUser())));
            workCheckDto.setFinishConfirmUserName(StrUtil.trimToEmpty(userMap.get(workCheckDto.getFinishConfirmUser())));
            workCheckDto.setFeeConfirmUserName(StrUtil.trimToEmpty(userMap.get(workCheckDto.getFeeConfirmUser())));
        } else {
            workCheckDto.setFinishCheckStatus(workDeal.getFinishCheckStatus());
            workCheckDto.setFeeCheckStatus(workDeal.getFeeCheckStatus());
            workCheckDto.setFinishConfirmStatus(workDeal.getFinishConfirmStatus());
            workCheckDto.setFeeConfirmStatus(workDeal.getFeeConfirmStatus());
        }
        workCheckDto.setWorkFeeStatus(workDeal.getWorkFeeStatus());
        workCheckDto.setFinishCheckStatusName(ServiceCheckStatusEnum.getNameByCode(workCheckDto.getFinishCheckStatus()));
        workCheckDto.setFeeCheckStatusName(FeeCheckStatusEnum.getNameByCode(workCheckDto.getFeeCheckStatus()));
        workCheckDto.setFinishConfirmStatusName(ServiceConfirmStatusEnum.getNameByCode(workCheckDto.getFinishConfirmStatus()));
        workCheckDto.setFeeConfirmStatusName(FeeConfirmStatusEnum.getNameByCode(workCheckDto.getFeeConfirmStatus()));
        return workCheckDto;
    }

    /**
     * 审核服务
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/5/11 20:24
     **/
    @Override
    public void checkService(WorkCheckDto workCheckDto, Long curUserId, Long curCorpId) {
        if (LongUtil.isZero(workCheckDto.getWorkId())) {
            throw new AppException("工单号不存在！");
        }
        WorkRequest workRequest = workRequestService.getById(workCheckDto.getWorkId());
        WorkDeal workDeal = workDealService.getById(workCheckDto.getWorkId());
        if (workRequest == null || workDeal == null) {
            throw new AppException("工单信息不存在！");
        }
        workCheckDto.setWorkStatus(workDeal.getWorkStatus());
        if (WorkStatusEnum.CLOSED.getCode() != workDeal.getWorkStatus()
                && WorkStatusEnum.TO_EVALUATE.getCode() != workDeal.getWorkStatus()) {
            throw new AppException("工单状态为【" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "】，不能审核！");
        }
        Long serviceCheckRightId = RightConstants.FINISH_SERVICE_CHECK; // 客服审核
        boolean serviceCheckRight = rightCompoService.hasRight(curCorpId, curUserId, serviceCheckRightId);
        Long managerCheckRightId = RightConstants.FINISH_MANAGER_CHECK; // 客户经理审核
        boolean managerCheckRight = rightCompoService.hasRight(curCorpId, curUserId, managerCheckRightId);
        // 只有客户经理审核权限
        if (!serviceCheckRight && managerCheckRight) {
            List<Long> demanderCorpList = demanderServiceManagerService.listDemanderCorpByManager(curCorpId, curUserId);
            // 工单中委托商的客户经理不是当前用户
            if (CollectionUtil.isNotEmpty(demanderCorpList) && !demanderCorpList.contains(workDeal.getDemanderCorp())) {
                throw new AppException("工单中委托商的客户经理不是您，不能审核！");
            }
        }

        Integer finishConfirmStatus = 0;
        WorkCheck dbWorkCheck = workCheckService.getById(workCheckDto.getWorkId());
        if (dbWorkCheck != null) {
            finishConfirmStatus = dbWorkCheck.getFinishConfirmStatus();
        }
        if (IntUtil.isZero(workCheckDto.getFinishCheckStatus())
                || ServiceCheckStatusEnum.UN_CHECK.getCode().equals(workCheckDto.getFinishCheckStatus())) {
            throw new AppException("请选择审核结果！");
        }
        if (ServiceConfirmStatusEnum.CONFIRM_PASS.getCode().equals(finishConfirmStatus)) {
            throw new AppException("服务已确认通过，不能再次审核服务！");
        }
        if (ServiceCheckStatusEnum.CHECK_PASS.getCode().equals(workCheckDto.getFinishCheckStatus())) {
            WorkFinish workFinish = workFinishService.getById(workDeal.getWorkId());
            if (FilesStatusEnum.FAIL.getCode().equals(workFinish.getFilesStatus())) {
                throw new AppException("附件要求不通过，需补充！");
            }
            if (SignatureStatusEnum.FAIL.getCode().equals(workFinish.getSignatureStatus())) {
                throw new AppException("签名不通过，需进行补签！");
            }
        }
        workCheckDto.setOperator(curUserId);
        workCheckDto.setFinishCheckUser(curUserId);
        workCheckDto.setFinishCheckTime(DateUtil.date());
        workCheckDto.setFinishCheckNote(StrUtil.trimToEmpty(workCheckDto.getFinishCheckNote()));
        // 若审核通过，则确认状态改为待确认
        if (ServiceCheckStatusEnum.CHECK_PASS.getCode().equals(workCheckDto.getFinishCheckStatus())) {
            workCheckDto.setFinishConfirmStatus(ServiceConfirmStatusEnum.UN_CONFIRM.getCode());
            workCheckDto.setFinishConfirmUser(0L);
            workCheckDto.setFinishConfirmTime(null);
            workCheckDto.setFinishConfirmNote("");
        } else {
            workCheckDto.setFinishConfirmStatus(0);
            workCheckDto.setFinishConfirmUser(0L);
            workCheckDto.setFinishConfirmTime(null);
            workCheckDto.setFinishConfirmNote("");
        }

        WorkCheck workCheck = new WorkCheck();
        BeanUtils.copyProperties(workCheckDto, workCheck);
        if (dbWorkCheck != null) {
            UpdateWrapper<WorkCheck> updateWrapper = new UpdateWrapper();
            updateWrapper.set("finish_check_user", workCheck.getFinishCheckUser());
            updateWrapper.set("finish_check_status", workCheck.getFinishCheckStatus());
            updateWrapper.set("finish_check_note", workCheck.getFinishCheckNote());
            updateWrapper.set("finish_check_time", workCheck.getFinishCheckTime());
            updateWrapper.set("finish_confirm_status", workCheck.getFinishConfirmStatus());
            updateWrapper.set("finish_confirm_user", workCheck.getFinishConfirmUser());
            updateWrapper.set("finish_confirm_time", workCheck.getFinishConfirmTime());
            updateWrapper.set("finish_confirm_note", workCheck.getFinishConfirmNote());
            updateWrapper.eq("work_id", workCheck.getWorkId());
            workCheckService.update(updateWrapper);
        } else {
            workCheckService.save(workCheck);
        }
        workDealService.checkService(workCheckDto, curUserId, curCorpId);
        // 通知委托商确认服务
        this.sendDemanderConfirmFinishMsg(workRequest, workDeal);
    }

    /**
     * 审核费用
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/5/29 11:13
     **/
    @Override
    public void checkFee(WorkCheckDto workCheckDto, Long curUserId, Long curCorpId) {
        if (LongUtil.isZero(workCheckDto.getWorkId())) {
            throw new AppException("工单号不存在！");
        }
        WorkRequest workRequest = workRequestService.getById(workCheckDto.getWorkId());
        WorkDeal workDeal = workDealService.getById(workCheckDto.getWorkId());
        if (workRequest == null || workDeal == null) {
            throw new AppException("工单信息不存在！");
        }
        workCheckDto.setWorkStatus(workDeal.getWorkStatus());
        if (WorkStatusEnum.CLOSED.getCode() != workDeal.getWorkStatus()
                && WorkStatusEnum.TO_EVALUATE.getCode() != workDeal.getWorkStatus()) {
            throw new AppException("工单状态为【" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "】，不能审核！");
        }
        Long serviceCheckRightId = RightConstants.FINISH_SERVICE_CHECK; // 客服审核
        boolean serviceCheckRight = rightCompoService.hasRight(curCorpId, curUserId, serviceCheckRightId);
        Long managerCheckRightId = RightConstants.FINISH_MANAGER_CHECK; // 客户经理审核
        boolean managerCheckRight = rightCompoService.hasRight(curCorpId, curUserId, managerCheckRightId);
        // 只有客户经理审核权限
        if (!serviceCheckRight && managerCheckRight) {
            List<Long> demanderCorpList = demanderServiceManagerService.listDemanderCorpByManager(curCorpId, curUserId);
            // 工单中委托商的客户经理不是当前用户
            if (CollectionUtil.isNotEmpty(demanderCorpList) && !demanderCorpList.contains(workDeal.getDemanderCorp())) {
                throw new AppException("工单中委托商的客户经理不是您，不能审核！");
            }
        }
        Integer feeConfirmStatus = 0;
        WorkCheck dbWorkCheck = workCheckService.getById(workCheckDto.getWorkId());
        if (dbWorkCheck != null) {
            feeConfirmStatus = dbWorkCheck.getFeeConfirmStatus();
        }
        if (IntUtil.isZero(workCheckDto.getFeeCheckStatus())
                || FeeCheckStatusEnum.UN_CHECK.getCode().equals(workCheckDto.getFeeCheckStatus())) {
            throw new AppException("请选择审核结果！");
        }
        if (FeeConfirmStatusEnum.CONFIRM_PASS.getCode().equals(feeConfirmStatus)) {
            throw new AppException("费用已确认通过，不能再次审核费用！");
        }
        if (!WorkFeeStatusEnum.FILLED.getCode().equals(workDeal.getWorkFeeStatus())) {
            throw new AppException("费用未录入完成，不能审核费用！");
        }
        workCheckDto.setOperator(curUserId);
        workCheckDto.setFeeCheckUser(curUserId);
        workCheckDto.setFeeCheckTime(DateUtil.date());
        workCheckDto.setFeeCheckNote(StrUtil.trimToEmpty(workCheckDto.getFeeCheckNote()));
        // 若审核通过，则确认状态改为待确认
        if (FeeCheckStatusEnum.CHECK_PASS.getCode().equals(workCheckDto.getFeeCheckStatus())) {
            workCheckDto.setFeeConfirmStatus(FeeConfirmStatusEnum.UN_CONFIRM.getCode());
            workCheckDto.setFeeConfirmUser(0L);
            workCheckDto.setFeeConfirmTime(null);
            workCheckDto.setFeeConfirmNote("");
        } else {
            workCheckDto.setFeeConfirmStatus(0);
            workCheckDto.setFeeConfirmUser(0L);
            workCheckDto.setFeeConfirmTime(null);
            workCheckDto.setFeeConfirmNote("");
        }
        WorkCheck workCheck = new WorkCheck();
        BeanUtils.copyProperties(workCheckDto, workCheck);
        if (dbWorkCheck != null) {
            UpdateWrapper<WorkCheck> updateWrapper = new UpdateWrapper();
            updateWrapper.set("fee_check_user", workCheck.getFeeCheckUser());
            updateWrapper.set("fee_check_status", workCheck.getFeeCheckStatus());
            updateWrapper.set("fee_check_note", workCheck.getFeeCheckNote());
            updateWrapper.set("fee_check_time", workCheck.getFeeCheckTime());
            updateWrapper.set("fee_confirm_user", workCheck.getFeeConfirmUser());
            updateWrapper.set("fee_confirm_status", workCheck.getFeeConfirmStatus());
            updateWrapper.set("fee_confirm_note", workCheck.getFeeConfirmNote());
            updateWrapper.set("fee_confirm_time", workCheck.getFeeConfirmTime());
            updateWrapper.eq("work_id", workCheck.getWorkId());
            workCheckService.update(updateWrapper);
        } else {
            workCheckService.save(workCheck);
        }
        workDealService.checkFee(workCheckDto, curUserId, curCorpId);
        // 提醒委托商确认费用
        this.sendDemanderConfirmFeeMsg(workRequest, workDeal);
    }

    /**
     * 确认服务
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/5/29 11:22
     **/
    @Override
    public void confirmService(WorkCheckDto workCheckDto, Long curUserId, Long curCorpId) {
        if (LongUtil.isZero(workCheckDto.getWorkId())) {
            throw new AppException("工单号不存在！");
        }
        WorkDeal workDeal = workDealService.getById(workCheckDto.getWorkId());
        if (workDeal == null) {
            throw new AppException("工单信息不存在！");
        }
        if (IntUtil.isZero(workCheckDto.getFinishConfirmStatus())
                || ServiceConfirmStatusEnum.UN_CONFIRM.getCode().equals(workCheckDto.getFinishConfirmStatus())) {
            throw new AppException("请选择确认结果！");
        }
        WorkCheck dbWorkCheck = workCheckService.getById(workCheckDto.getWorkId());
        if (WorkStatusEnum.RETURNED.getCode() != workDeal.getWorkStatus() && dbWorkCheck == null) {
            throw new AppException("缺少工单审核记录，请联系服务商审核工单！");
        }
        workCheckDto.setOperator(curUserId);
        Integer finishConfirmStatus = 0;
        if (dbWorkCheck != null) {
            finishConfirmStatus = dbWorkCheck.getFinishConfirmStatus();
        }
        if (ServiceConfirmStatusEnum.CONFIRM_PASS.getCode().equals(finishConfirmStatus)) {
            throw new AppException("服务已确认通过，不能再次确认！");
        }
        if (ServiceConfirmStatusEnum.CONFIRM_REFUSE.getCode().equals(finishConfirmStatus)) {
            throw new AppException("服务已确认不通过，不能再次确认！");
        }
        if (WorkStatusEnum.RETURNED.getCode() != workDeal.getWorkStatus()) {
            if (!ServiceCheckStatusEnum.CHECK_PASS.getCode().equals(dbWorkCheck.getFinishCheckStatus())) {
                throw new AppException("服务未审核通过，不能确认服务！");
            }
        }
        workCheckDto.setFinishConfirmUser(curUserId);
        workCheckDto.setFinishConfirmTime(DateUtil.date());
        workCheckDto.setFinishConfirmNote(StrUtil.trimToEmpty(workCheckDto.getFinishConfirmNote()));
        WorkCheck workCheck = new WorkCheck();
        BeanUtils.copyProperties(workCheckDto, workCheck);
        workCheckService.updateById(workCheck);
        workDealService.confirmService(workCheckDto, curUserId, curCorpId);
    }

    /**
     * 确认费用
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/5/29 11:22
     **/
    @Override
    public void confirmFee(WorkCheckDto workCheckDto, Long curUserId, Long curCorpId) {
        if (LongUtil.isZero(workCheckDto.getWorkId())) {
            throw new AppException("工单号不存在！");
        }
        WorkDeal workDeal = workDealService.getById(workCheckDto.getWorkId());
        WorkRequest workRequest = workRequestService.getById(workCheckDto.getWorkId());
        if (workDeal == null || workRequest == null) {
            throw new AppException("工单信息不存在！");
        }
        WorkCheck dbWorkCheck = workCheckService.getById(workCheckDto.getWorkId());
        if (WorkStatusEnum.RETURNED.getCode() != workDeal.getWorkStatus() && dbWorkCheck == null) {
            throw new AppException("缺少工单审核记录，请联系服务商审核工单！");
        }
        workCheckDto.setOperator(curUserId);
        Integer feeConfirmStatus = 0;
        Integer feeCheckStatus = 0;
        if (dbWorkCheck != null) {
            feeConfirmStatus = dbWorkCheck.getFeeConfirmStatus();
            feeCheckStatus = dbWorkCheck.getFeeCheckStatus();
        }
        if (IntUtil.isZero(workCheckDto.getFeeConfirmStatus())
                || FeeConfirmStatusEnum.UN_CONFIRM.getCode().equals(workCheckDto.getFeeConfirmStatus())) {
            throw new AppException("请选择确认结果！");
        }
        if (FeeConfirmStatusEnum.CONFIRM_PASS.getCode().equals(feeConfirmStatus)) {
            throw new AppException("费用已确认通过，不能再次确认！");
        }
        if (FeeConfirmStatusEnum.CONFIRM_REFUSE.getCode().equals(feeConfirmStatus)) {
            throw new AppException("费用已确认不通过，不能再次确认！");
        }
        if (!FeeCheckStatusEnum.CHECK_PASS.getCode().equals(feeCheckStatus)) {
            throw new AppException("费用未审核通过，不能确认费用！");
        }

        workCheckDto.setFeeConfirmUser(curUserId);
        workCheckDto.setFeeConfirmTime(DateUtil.date());
        workCheckDto.setFeeConfirmNote(StrUtil.trimToEmpty(workCheckDto.getFeeConfirmNote()));

        WorkCheck workCheck = new WorkCheck();
        BeanUtils.copyProperties(workCheckDto, workCheck);
        workCheckService.updateById(workCheck);
        workDealService.confirmFee(workCheckDto, curUserId, curCorpId);

        // 确认通过时，如果是按单结算的工单，自动生成结算单
        if (FeeConfirmStatusEnum.CONFIRM_PASS.getCode().equals(workCheckDto.getFeeConfirmStatus())) {
            // 获取委托关系
            DemanderAutoConfigDto autoConfigDto = this.demanderAutoConfigService.findByDemanderAndService(workDeal.getDemanderCorp(), workDeal.getServiceCorp());
            if (autoConfigDto == null) {
                return;
            }
            // 按单结算时，自动添加结算单
            if (SettleTypeEnum.PER_WORK.getCode().equals(autoConfigDto.getSettleType())
                    && WorkSettleStatusEnum.TO_VERIFY.getCode().equals(workDeal.getSettleDemanderStatus())
                    && WarrantyModeEnum.listSettleWarrantyStatus().contains(workRequest.getWarrantyMode())) {
                List<WorkDeal> workDealList = new ArrayList<>();
                workDealList.add(workDeal);
                if (CollectionUtil.isNotEmpty(workDealList)) {
                    this.settleDemanderService.batchAddByWorkListAndDemander(workDealList, curUserId, curCorpId);
                }
            }
        }
    }

    /**
     * 批量审核服务
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/6/1 10:20
     **/
    @Override
    public String batchCheckService(WorkCheckDto workCheckDto, Long curUserId, Long curCorpId) {
        if (CollectionUtil.isEmpty(workCheckDto.getWorkIdList())) {
            throw new AppException("工单号列表不存在！");
        }
        List<WorkDeal> workDealList = workDealService.list(new QueryWrapper<WorkDeal>()
                .in("work_id", workCheckDto.getWorkIdList()));
        if (CollectionUtil.isEmpty(workDealList)) {
            throw new AppException("工单信息不存在！");
        }
        Long serviceCheckRightId = RightConstants.FINISH_SERVICE_CHECK; // 客服审核
        boolean serviceCheckRight = rightCompoService.hasRight(curCorpId, curUserId, serviceCheckRightId);
        Long managerCheckRightId = RightConstants.FINISH_MANAGER_CHECK; // 客户经理审核
        boolean managerCheckRight = rightCompoService.hasRight(curCorpId, curUserId, managerCheckRightId);
        List<Long> demanderCorpList = new ArrayList<>();
        // 只有客户经理审核权限
        if (!serviceCheckRight && managerCheckRight) {
            demanderCorpList = demanderServiceManagerService.listDemanderCorpByManager(curCorpId, curUserId);
        }
        Map<Long, WorkFinish> longWorkFinishMap = workFinishService.mapWorkIdAndWorkFinish(workCheckDto.getWorkIdList());
        StringBuilder msg = new StringBuilder(8);
        Set<Integer> finishCheckStatusSet = new HashSet<>();
        for (WorkDeal workDeal : workDealList) {
            finishCheckStatusSet.add(workDeal.getFinishCheckStatus());
            WorkFinish workFinish = longWorkFinishMap.get(workDeal.getWorkId());
            StringBuilder stringBuilder = new StringBuilder(0);
            if (WorkStatusEnum.CLOSED.getCode() != workDeal.getWorkStatus()
                    && WorkStatusEnum.TO_EVALUATE.getCode() != workDeal.getWorkStatus()) {
                stringBuilder.append("状态为【").append(WorkStatusEnum.getNameByCode(workDeal.getWorkStatus())).append("】，不能审核！");
            }
            // 工单中委托商的客户经理不是当前用户
            if (CollectionUtil.isNotEmpty(demanderCorpList) && !demanderCorpList.contains(workDeal.getDemanderCorp())) {
                throw new AppException("委托商的客户经理不是您，不能审核！");
            }
            if (ServiceConfirmStatusEnum.CONFIRM_PASS.getCode().equals(workDeal.getFinishConfirmStatus())) {
                stringBuilder.append("委托商已确认服务通过，不能再次审核！");
            }
            if (workFinish == null) {
                stringBuilder.append("查询不到完成信息！");
            }
            if (FilesStatusEnum.FAIL.getCode().equals(workFinish.getFilesStatus())) {
                stringBuilder.append("附件要求不通过，需补充！");
            }
            if (SignatureStatusEnum.FAIL.getCode().equals(workFinish.getSignatureStatus())) {
                stringBuilder.append("签名不通过，需进行补签！");
            }
            if (stringBuilder.length() > 0) {
                stringBuilder.insert(0, "工单[" + workDeal.getWorkCode() + "]");
                msg.append(stringBuilder.toString());
            }
        }
        if (msg.length() > 0) {
            return msg.toString();
        }
        workCheckDto.setOperator(curUserId);
        workCheckDto.setFinishCheckStatus(ServiceCheckStatusEnum.CHECK_PASS.getCode());
        workCheckDto.setFinishCheckUser(curUserId);
        workCheckDto.setFinishCheckTime(DateUtil.date());
        workCheckDto.setFinishCheckNote(StrUtil.trimToEmpty(workCheckDto.getFinishCheckNote()));
        workCheckDto.setFinishConfirmStatus(ServiceConfirmStatusEnum.UN_CONFIRM.getCode());
        List<WorkCheck> workCheckList = new ArrayList<>();
        WorkCheck workCheck;
        for (Long workId : workCheckDto.getWorkIdList()) {
            workCheck = new WorkCheck();
            BeanUtils.copyProperties(workCheckDto, workCheck);
            workCheck.setWorkId(workId);
            workCheckList.add(workCheck);
        }
        workCheckService.saveOrUpdateBatch(workCheckList);
        workDealService.batchCheckService(workCheckDto, curUserId, curCorpId);
        // 发送消息通知委托商做确认服务操作
        sendBatchConfirmServiceMsg(workDealList);
        return msg.toString();
    }

    /**
     * 批量审核费用
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/6/1 10:20
     **/
    @Override
    public String batchCheckFee(WorkCheckDto workCheckDto, Long curUserId, Long curCorpId) {
        if (CollectionUtil.isEmpty(workCheckDto.getWorkIdList())) {
            throw new AppException("工单号列表不存在！");
        }
        List<WorkDeal> workDealList = workDealService.list(new QueryWrapper<WorkDeal>()
                .in("work_id", workCheckDto.getWorkIdList()));
        if (CollectionUtil.isEmpty(workDealList)) {
            throw new AppException("工单信息不存在！");
        }
        Long serviceCheckRightId = RightConstants.FINISH_SERVICE_CHECK; // 客服审核
        boolean serviceCheckRight = rightCompoService.hasRight(curCorpId, curUserId, serviceCheckRightId);
        Long managerCheckRightId = RightConstants.FINISH_MANAGER_CHECK; // 客户经理审核
        boolean managerCheckRight = rightCompoService.hasRight(curCorpId, curUserId, managerCheckRightId);
        List<Long> demanderCorpList = new ArrayList<>();
        // 只有客户经理审核权限
        if (!serviceCheckRight && managerCheckRight) {
            demanderCorpList = demanderServiceManagerService.listDemanderCorpByManager(curCorpId, curUserId);
        }
        StringBuilder msg = new StringBuilder(8);
        Set<Integer> feeCheckStatusSet = new HashSet<>();
        for (WorkDeal workDeal : workDealList) {
            feeCheckStatusSet.add(workDeal.getFeeCheckStatus());
            if (WorkStatusEnum.CLOSED.getCode() != workDeal.getWorkStatus()
                    && WorkStatusEnum.TO_EVALUATE.getCode() != workDeal.getWorkStatus()) {
                msg.append("状态为【"
                        + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "】，不能审核！");
            }
            // 工单中委托商的客户经理不是当前用户
            if (CollectionUtil.isNotEmpty(demanderCorpList) && !demanderCorpList.contains(workDeal.getDemanderCorp())) {
                throw new AppException("委托商的客户经理不是您，不能审核！");
            }
            if (!WorkFeeStatusEnum.FILLED.getCode().equals(workDeal.getWorkFeeStatus())) {
                msg.append("未录入费用，不能审核！");
            }
            if (FeeConfirmStatusEnum.CONFIRM_PASS.getCode().equals(workDeal.getFeeConfirmStatus())) {
                msg.append("委托商已确认费用，不能审核！");
            }
            if (msg.length() > 0) {
                msg.insert(0, "工单[" + workDeal.getWorkCode() + "]");
                msg.append(msg.toString());
            }
        }
        if (msg.length() > 0) {
            return msg.toString();
        }
        workCheckDto.setOperator(curUserId);
        workCheckDto.setFeeCheckStatus(FeeCheckStatusEnum.CHECK_PASS.getCode());
        workCheckDto.setFeeCheckUser(curUserId);
        workCheckDto.setFeeCheckTime(DateUtil.date());
        workCheckDto.setFeeCheckNote(StrUtil.trimToEmpty(workCheckDto.getFeeCheckNote()));
        workCheckDto.setFeeConfirmStatus(FeeConfirmStatusEnum.UN_CONFIRM.getCode());
        List<WorkCheck> workCheckList = new ArrayList<>();
        WorkCheck workCheck;
        for (Long workId : workCheckDto.getWorkIdList()) {
            workCheck = new WorkCheck();
            BeanUtils.copyProperties(workCheckDto, workCheck);
            workCheck.setWorkId(workId);
            workCheckList.add(workCheck);
        }
        workCheckService.saveOrUpdateBatch(workCheckList);
        workDealService.batchCheckFee(workCheckDto, curUserId, curCorpId);
        // 发送消息通知委托商做确认服务操作
        sendBatchConfirmFeeMsg(workDealList);
        return msg.toString();
    }

    /**
     * 批量确认服务
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/6/1 15:43
     **/
    @Override
    public String batchConfirmService(WorkCheckDto workCheckDto, Long curUserId, Long curCorpId) {
        if (CollectionUtil.isEmpty(workCheckDto.getWorkIdList())) {
            throw new AppException("工单号列表不存在！");
        }
        List<WorkDeal> workDealList = workDealService.list(new QueryWrapper<WorkDeal>()
                .in("work_id", workCheckDto.getWorkIdList()));
        if (CollectionUtil.isEmpty(workDealList)) {
            throw new AppException("工单信息不存在！");
        }
        List<WorkCheck> dbWorkCheckList = workCheckService.list(new QueryWrapper<WorkCheck>()
                .in("work_id", workCheckDto.getWorkIdList()));
        List<Long> workIdList = dbWorkCheckList.stream().map(e -> e.getWorkId()).collect(Collectors.toList());
        Map<Long, WorkDeal> workMap = workDealList.stream()
                .collect(Collectors.toMap(WorkDeal::getWorkId, workDeal -> workDeal));
        StringBuilder msg = new StringBuilder(8);
        workCheckDto.getWorkIdList().forEach(workId -> {
            if (!workIdList.contains(workId) && workMap.containsKey(workId)
                    && WorkStatusEnum.RETURNED.getCode() != workMap.get(workId).getWorkStatus()) {
                if (msg.length() > 0) {
                    msg.append("、");
                }
                msg.append("[" + workMap.get(workId).getWorkCode() + "]");
            }
        });
        if (msg.length() > 0) {
            return "工单" + msg.toString() + "缺少审核记录，请联系服务商审核工单！";
        }
        StringBuilder error = new StringBuilder(8);
        for (WorkDeal workDeal : workDealList) {
            if (ServiceConfirmStatusEnum.CONFIRM_PASS.getCode().equals(workDeal.getFinishConfirmStatus())) {
                error.append("工单[" + workDeal.getWorkCode() + "]服务已确认通过，不能再次确认！");
            } else if (ServiceConfirmStatusEnum.CONFIRM_REFUSE.getCode().equals(workDeal.getFinishConfirmStatus())) {
                error.append("工单[" + workDeal.getWorkCode() + "]服务已确认不通过，不能再次确认！");
            } else if (!ServiceConfirmStatusEnum.UN_CONFIRM.getCode().equals(workDeal.getFinishConfirmStatus())) {
                error.append("工单[" + workDeal.getWorkCode() + "]服务不是待确认状态，不能确认！");
            }
        }
        if (error.length() > 0) {
            return error.toString();
        }
        workCheckDto.setOperator(curUserId);
        // 确认服务通过
        workCheckDto.setFinishConfirmStatus(ServiceConfirmStatusEnum.CONFIRM_PASS.getCode());
        workCheckDto.setFinishConfirmUser(curUserId);
        workCheckDto.setFinishConfirmTime(DateUtil.date());
        workCheckDto.setFinishConfirmNote(StrUtil.trimToEmpty(workCheckDto.getFinishConfirmNote()));

        List<WorkCheck> workCheckList = new ArrayList<>();
        WorkCheck workCheck;
        for (Long workId : workCheckDto.getWorkIdList()) {
            workCheck = new WorkCheck();
            BeanUtils.copyProperties(workCheckDto, workCheck);
            workCheck.setWorkId(workId);
            workCheckList.add(workCheck);
        }
        workCheckService.updateBatchById(workCheckList);
        workDealService.batchConfirmService(workCheckDto, curUserId, curCorpId);
        return error.toString();
    }

    /**
     * 批量确认费用
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/6/1 15:43
     **/
    @Override
    public String batchConfirmFee(WorkCheckDto workCheckDto, Long curUserId, Long curCorpId) {
        if (CollectionUtil.isEmpty(workCheckDto.getWorkIdList())) {
            throw new AppException("工单号列表不存在！");
        }
        List<WorkDeal> workDealList = workDealService.list(new QueryWrapper<WorkDeal>()
                .in("work_id", workCheckDto.getWorkIdList()));
        if (CollectionUtil.isEmpty(workDealList)) {
            throw new AppException("工单信息不存在！");
        }
        Map<Long, WorkRequest> workRequestMap = this.workRequestService.mapByWorkIdList(workCheckDto.getWorkIdList());
        List<WorkCheck> dbWorkCheckList = workCheckService.list(new QueryWrapper<WorkCheck>()
                .in("work_id", workCheckDto.getWorkIdList()));
        List<Long> workIdList = dbWorkCheckList.stream().map(e -> e.getWorkId()).collect(Collectors.toList());
        Map<Long, WorkDeal> workMap = workDealList.stream()
                .collect(Collectors.toMap(WorkDeal::getWorkId, workDeal -> workDeal));
        StringBuilder msg = new StringBuilder(8);
        workCheckDto.getWorkIdList().forEach(workId -> {
            if (!workIdList.contains(workId) && workMap.containsKey(workId)
                    && WorkStatusEnum.RETURNED.getCode() != workMap.get(workId).getWorkStatus()) {
                if (msg.length() > 0) {
                    msg.append("、");
                }
                msg.append("[" + workMap.get(workId).getWorkCode() + "]");
            }
        });
        if (msg.length() > 0) {
            return "工单" + msg.toString() + "缺少审核记录，请联系服务商审核工单！";
        }
        StringBuilder error = new StringBuilder(8);
        for (WorkDeal workDeal : workDealList) {
            if (FeeConfirmStatusEnum.CONFIRM_PASS.getCode().equals(workDeal.getFeeConfirmStatus())) {
                error.append("工单[" + workDeal.getWorkCode() + "]费用已确认通过，不能再次确认！");
            } else if (FeeConfirmStatusEnum.CONFIRM_REFUSE.getCode().equals(workDeal.getFeeConfirmStatus())) {
                error.append("工单[" + workDeal.getWorkCode() + "]费用已确认不通过，不能再次确认！");
            } else if (!FeeConfirmStatusEnum.UN_CONFIRM.getCode().equals(workDeal.getFeeConfirmStatus())) {
                error.append("工单[" + workDeal.getWorkCode() + "]费用不是待确认状态，不能确认！");
            }
        }
        if (error.length() > 0) {
            return error.toString();
        }
        workCheckDto.setOperator(curUserId);
        workCheckDto.setFeeConfirmStatus(ServiceConfirmStatusEnum.CONFIRM_PASS.getCode());
        workCheckDto.setFeeConfirmUser(curUserId);
        workCheckDto.setFeeConfirmTime(DateUtil.date());
        workCheckDto.setFeeConfirmNote(StrUtil.trimToEmpty(workCheckDto.getFinishConfirmNote()));
        List<WorkCheck> workCheckList = new ArrayList<>();
        WorkCheck workCheck;
        for (Long workId : workCheckDto.getWorkIdList()) {
            workCheck = new WorkCheck();
            BeanUtils.copyProperties(workCheckDto, workCheck);
            workCheck.setWorkId(workId);
            workCheckList.add(workCheck);
        }
        workCheckService.updateBatchById(workCheckList);
        workDealService.batchConfirmFee(workCheckDto, curUserId, curCorpId);
        List<Long> serviceCorpList = workDealList.stream().map(workDeal -> workDeal.getServiceCorp()).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(serviceCorpList)) {
            return error.toString();
        }
        // 自动化配置映射
        Map<Long, DemanderAutoConfigDto> demanderAutoMap = this.demanderAutoConfigService.mapServiceAndAutoConfig(curCorpId);
        // 获取按单结算的工单
        List<WorkDeal> workDealsToSettle = new ArrayList<>();
        // 按单结算时，自动添加结算单
        for (WorkDeal workDeal : workDealList) {
            DemanderAutoConfigDto autoConfigDto = demanderAutoMap.get(workDeal.getServiceCorp());
            WorkRequest workRequest = workRequestMap.get(workDeal.getWorkId());
            if (autoConfigDto != null && workRequest != null
                    && SettleTypeEnum.PER_WORK.getCode().equals(autoConfigDto.getSettleType())
                    && WorkSettleStatusEnum.TO_VERIFY.getCode().equals(workDeal.getSettleDemanderStatus())
                    && WarrantyModeEnum.listSettleWarrantyStatus().contains(workRequest.getWarrantyMode())) {
                workDealsToSettle.add(workDeal);
            }
        }
        if (CollectionUtil.isNotEmpty(workDealsToSettle)) {
            this.settleDemanderService.batchAddByWorkListAndDemander(workDealsToSettle, curUserId, curCorpId);
        }
        return error.toString();
    }

    /**
     * 自动确认服务
     *
     * @param serviceCorp
     * @return
     */
    @Override
    public String autoConfirmService(Long serviceCorp) {
        StringBuilder sb = new StringBuilder(16);
        List<WorkDeal> workDealList = this.workRequestService.listAutoServiceConfirm(serviceCorp);
        if (CollectionUtil.isEmpty(workDealList)) {
            sb.append("无可自动确认服务工单");
            return sb.toString();
        }
        WorkCheckDto workCheckDto = new WorkCheckDto();
        workCheckDto.setFinishConfirmStatus(ServiceConfirmStatusEnum.CONFIRM_PASS.getCode());
        workCheckDto.setFinishConfirmTime(DateUtil.date());
        workCheckDto.setFinishConfirmNote("自动确认通过");
        List<WorkCheck> workCheckList = new ArrayList<>();
        List<Long> workIdList = workDealList.stream().map(workDeal -> workDeal.getWorkId()).collect(Collectors.toList());
        WorkCheck workCheck;
        for (Long workId : workIdList) {
            workCheck = new WorkCheck();
            BeanUtils.copyProperties(workCheckDto, workCheck);
            workCheck.setWorkId(workId);
            workCheckList.add(workCheck);
        }
        workCheckService.updateBatchById(workCheckList);
        workDealService.autoConfirmService(workIdList, workCheckDto);
        sb.append("自动确认服务完成");
        return sb.toString();
    }

    /**
     * 自动确认费用
     *
     * @param serviceCorp
     * @return
     */
    @Override
    public String autoConfirmFee(Long serviceCorp) {
        StringBuilder sb = new StringBuilder(16);
        List<WorkDeal> workDealList = this.workRequestService.listAutoFeeConfirm(serviceCorp);
        if (CollectionUtil.isEmpty(workDealList)) {
            sb.append("无可自动确认费用工单");
            return sb.toString();
        }
        WorkCheckDto workCheckDto = new WorkCheckDto();
        workCheckDto.setFeeConfirmStatus(ServiceConfirmStatusEnum.CONFIRM_PASS.getCode());
        workCheckDto.setFeeConfirmTime(DateUtil.date());
        workCheckDto.setFeeConfirmNote("自动确认通过");
        List<WorkCheck> workCheckList = new ArrayList<>();
        List<Long> workIdList = workDealList.stream().map(workDeal -> workDeal.getWorkId()).collect(Collectors.toList());
        Map<Long, WorkRequest> workRequestMap = workRequestService.mapByWorkIdList(workIdList);
        WorkCheck workCheck;
        for (Long workId : workIdList) {
            workCheck = new WorkCheck();
            BeanUtils.copyProperties(workCheckDto, workCheck);
            workCheck.setWorkId(workId);
            workCheckList.add(workCheck);
        }
        workCheckService.updateBatchById(workCheckList);
        workDealService.autoConfirmFee(workIdList, workCheckDto);
        sb.append("自动确认费用完成");
        List<Long> serviceCorpList = workDealList.stream().map(workDeal -> workDeal.getServiceCorp()).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(serviceCorpList)) {
            return sb.toString();
        }
        // 自动化配置映射
        Map<Long, DemanderAutoConfigDto> demanderAutoMap = this.demanderAutoConfigService.mapDemanderAndAutoConfig(serviceCorp);
        // 获取按单结算的工单
        List<WorkDeal> workDealsToSettle = new ArrayList<>();
        // 按单结算时，自动添加结算单
        for (WorkDeal workDeal : workDealList) {
            DemanderAutoConfigDto autoConfigDto = demanderAutoMap.get(workDeal.getDemanderCorp());
            WorkRequest workRequest = workRequestMap.get(workDeal.getWorkId());
            if (autoConfigDto != null && workRequest != null
                    && SettleTypeEnum.PER_WORK.getCode().equals(autoConfigDto.getSettleType())
                    && WorkSettleStatusEnum.TO_VERIFY.getCode().equals(workDeal.getSettleDemanderStatus())
                    && WarrantyModeEnum.listSettleWarrantyStatus().contains(workRequest.getWarrantyMode())) {
                workDealsToSettle.add(workDeal);
            }
        }
        if (CollectionUtil.isNotEmpty(workDealsToSettle)) {
            // 添加结算单
            this.settleDemanderService.batchAddByWorkListAndService(workDealsToSettle, 0L, serviceCorp);
        }
        return sb.toString();
    }

    /**
     * 提醒委托商确认服务
     *
     * @param workRequest
     * @param workDeal
     * @return
     * @author zgpi
     * @date 2020/7/3 17:01
     **/
    public void sendDemanderConfirmFinishMsg(WorkRequest workRequest, WorkDeal workDeal) {
        TemplateMessage templateMessage = new TemplateMessage();
        // 消息接收人为委托商有服务确认权限的人
        Result<List<Long>> authResult = rightFeignService.listUserByRightId(
                workDeal.getDemanderCorp(), RightConstants.WORK_FINISH_CONFIRM);
        if (Result.isSucceed(authResult)) {
            List<Long> list = authResult.getData();
            if (CollectionUtil.isEmpty(list)) {
                return;
            }
            templateMessage.setUserIds(CollectionUtil.join(list.stream().distinct().collect(Collectors.toList()), ","));
        } else {
            return;
        }
        templateMessage.setTplName("服务确认");
        templateMessage.setAppId(RightConstants.WORK_APPID);
        Map<String, Object> contentMap = new HashMap<>(1);
        StringBuilder msg = new StringBuilder(32);
        msg.append("您收到一个工单[").append(workDeal.getWorkCode()).append("]需要确认服务, 请及时确认。");
        DemanderAutoConfigDto autoConfigDto = this.demanderAutoConfigService.findByDemanderAndService(workDeal.getDemanderCorp(), workDeal.getServiceCorp());
        if (autoConfigDto != null && EnabledEnum.YES.getCode().equals(autoConfigDto.getAutoConfirmService())) {
            msg.append(autoConfigDto.getAutoConfirmServiceHours() + "小时后将自动确认，如有疑问，请联系客服！");
        }
        contentMap.put("msg", msg.toString());
        this.setMsgContentMap(contentMap, workRequest, workDeal);
        templateMessage.setDataMap(contentMap);
        pushService.pushTemplateMessage(templateMessage);
    }

    /**
     * 提醒委托商确认费用
     *
     * @param workRequest
     * @param workDeal
     * @return
     * @author zgpi
     * @date 2020/7/3 17:01
     **/
    public void sendDemanderConfirmFeeMsg(WorkRequest workRequest, WorkDeal workDeal) {
        TemplateMessage templateMessage = new TemplateMessage();
        // 消息接收人为委托商有服务确认权限的人
        Result<List<Long>> authResult = rightFeignService.listUserByRightId(
                workDeal.getDemanderCorp(), RightConstants.WORK_FEE_CONFIRM);
        if (Result.isSucceed(authResult)) {
            List<Long> list = authResult.getData();
            if (CollectionUtil.isEmpty(list)) {
                return;
            }
            templateMessage.setUserIds(CollectionUtil.join(list.stream().distinct().collect(Collectors.toList()), ","));
        } else {
            return;
        }
        templateMessage.setTplName("费用确认");
        templateMessage.setAppId(RightConstants.WORK_APPID);
        Map<String, Object> contentMap = new HashMap<>(1);
        StringBuilder msg = new StringBuilder(32);
        msg.append("您收到一个工单[").append(workDeal.getWorkCode()).append("]需要确认费用，请及时确认。");
        // 是否有自动确认
        DemanderAutoConfigDto autoConfigDto = this.demanderAutoConfigService.findByDemanderAndService(workDeal.getDemanderCorp(), workDeal.getServiceCorp());
        if (autoConfigDto != null && EnabledEnum.YES.getCode().equals(autoConfigDto.getAutoConfirmFee())) {
            msg.append(autoConfigDto.getAutoConfirmFeeHours() + "小时后将自动确认，如有疑问，请联系客服！");
        }
        contentMap.put("msg", msg.toString());
        this.setMsgContentMap(contentMap, workRequest, workDeal);
        templateMessage.setDataMap(contentMap);
        pushService.pushTemplateMessage(templateMessage);
    }

    /**
     * 批量审核服务通过后，发送消息通知委托商有权限人员
     *
     * @param workDealList
     */
    public void sendBatchConfirmServiceMsg(List<WorkDeal> workDealList) {
        if (CollectionUtil.isEmpty(workDealList)) {
            return;
        }
        // 获取所有委托商
        List<Long> demanderCorpList = workDealList.stream().map(workDeal -> workDeal.getDemanderCorp())
                .filter(LongUtil::isNotZero).distinct().collect(Collectors.toList());
        // 获取每个委托商及其工单数量的映射
        Map<Long, Long> demanderAndWorkNumMap = workDealList.stream()
                .collect(Collectors.groupingBy(WorkDeal::getDemanderCorp, Collectors.counting()));
        // 针对每个委托商推送消息
        if (CollectionUtil.isNotEmpty(demanderCorpList)) {
            for (Long demanderCorp : demanderCorpList) {
                Message message = new Message();
                // 消息接收人为委托商有服务确认权限的人
                Result<List<Long>> authResult = rightFeignService.listUserByRightId(demanderCorp, RightConstants.WORK_FINISH_CONFIRM);
                if (Result.isSucceed(authResult)) {
                    List<Long> list = authResult.getData();
                    if (CollectionUtil.isEmpty(list)) {
                        return;
                    }
                    message.setUserIds(CollectionUtil.join(list.stream().distinct().collect(Collectors.toList()), ","));
                } else {
                    return;
                }
                Long workNum = demanderAndWorkNumMap.get(demanderCorp);
                if (LongUtil.isZero(workNum)) {
                    continue;
                }
                // 消息接收人为创建人
                message.setAppId(RightConstants.WORK_APPID);
                message.setContent("您有" + workNum.toString() + "个工单需要确认服务！");
                message.setTitle("您好，您有" + workNum.toString() + "个工单需要确认服务！");
                pushService.pushMessage(message);
            }
        }
    }

    /**
     * 批量审核费用通过后，发送消息通知委托商有权限人员
     *
     * @param workDealList
     */
    public void sendBatchConfirmFeeMsg(List<WorkDeal> workDealList) {
        if (CollectionUtil.isEmpty(workDealList)) {
            return;
        }
        // 获取所有委托商
        List<Long> demanderCorpList = workDealList.stream().map(workDeal -> workDeal.getDemanderCorp())
                .filter(LongUtil::isNotZero).distinct().collect(Collectors.toList());
        // 获取每个委托商及其工单数量的映射
        Map<Long, Long> demanderAndWorkNumMap = workDealList.stream()
                .collect(Collectors.groupingBy(WorkDeal::getDemanderCorp, Collectors.counting()));
        // 针对每个委托商推送消息
        if (CollectionUtil.isNotEmpty(demanderCorpList)) {
            for (Long demanderCorp : demanderCorpList) {
                Message message = new Message();
                // 消息接收人为委托商有服务确认权限的人
                Result<List<Long>> authResult = rightFeignService.listUserByRightId(demanderCorp, RightConstants.WORK_FEE_CONFIRM);
                if (Result.isSucceed(authResult)) {
                    List<Long> list = authResult.getData();
                    if (CollectionUtil.isEmpty(list)) {
                        return;
                    }
                    message.setUserIds(CollectionUtil.join(list.stream().distinct().collect(Collectors.toList()), ","));
                } else {
                    return;
                }
                Long workNum = demanderAndWorkNumMap.get(demanderCorp);
                if (LongUtil.isZero(workNum)) {
                    continue;
                }
                // 消息接收人为创建人
                message.setAppId(RightConstants.WORK_APPID);
                message.setContent("您有" + workNum.toString() + "个工单需要确认费用！");
                message.setTitle("您好，您有" + workNum.toString() + "个工单需要确认费用！");
                pushService.pushMessage(message);
            }
        }
    }

    /**
     * 填充工单内容
     *
     * @param contentMap
     * @param workRequest
     * @param workDeal
     */
    private void setMsgContentMap(Map<String, Object> contentMap, WorkRequest workRequest, WorkDeal workDeal) {
        contentMap.put("workCode", workDeal.getWorkCode());
        contentMap.put("customCorp", workRequest.getCustomCorp());
        // 获得客户名称
        if (workRequest.getCustomId() != 0) {
            contentMap.put("customCorpName", demanderCustomService.getById(workRequest.getCustomId()).getCustomCorpName());
        } else {
            contentMap.put("customCorpName", "");
        }
        // 远程调用获得设备小类名称
        Result smallClassResult = deviceFeignService.findDeviceSmallClass(workRequest.getSmallClass());
        DeviceSmallClassDto deviceSmallClassDto = null;
        if (Result.isSucceed(smallClassResult)) {
            deviceSmallClassDto = JsonUtil.parseObject(JsonUtil.toJson(smallClassResult.getData()), DeviceSmallClassDto.class);
        }
        if (deviceSmallClassDto != null) {
            contentMap.put("smallClassName", StrUtil.trimToEmpty(deviceSmallClassDto.getName()));
        } else {
            contentMap.put("smallClassName", "");
        }
    }
}
