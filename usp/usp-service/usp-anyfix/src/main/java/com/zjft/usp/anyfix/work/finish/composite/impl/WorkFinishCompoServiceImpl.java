package com.zjft.usp.anyfix.work.finish.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjft.usp.anyfix.baseinfo.enums.EnabledEnum;
import com.zjft.usp.anyfix.baseinfo.enums.WorkSysTypeEnum;
import com.zjft.usp.anyfix.baseinfo.model.CustomFieldData;
import com.zjft.usp.anyfix.baseinfo.model.WorkType;
import com.zjft.usp.anyfix.baseinfo.service.CustomFieldDataService;
import com.zjft.usp.anyfix.baseinfo.service.WorkTypeService;
import com.zjft.usp.anyfix.common.feign.dto.UserRealDto;
import com.zjft.usp.anyfix.corp.branch.dto.DeviceBranchDto;
import com.zjft.usp.anyfix.corp.branch.model.ServiceBranch;
import com.zjft.usp.anyfix.corp.branch.service.DeviceBranchService;
import com.zjft.usp.anyfix.corp.branch.service.ServiceBranchService;
import com.zjft.usp.anyfix.corp.config.constant.ItemConfig;
import com.zjft.usp.anyfix.corp.config.dto.ServiceConfigDto;
import com.zjft.usp.anyfix.corp.config.service.ServiceConfigService;
import com.zjft.usp.anyfix.corp.fileconfig.dto.FileConfigDto;
import com.zjft.usp.anyfix.corp.fileconfig.enums.FormTypeEnum;
import com.zjft.usp.anyfix.corp.fileconfig.filter.FileConfigFilter;
import com.zjft.usp.anyfix.corp.fileconfig.model.WorkFiles;
import com.zjft.usp.anyfix.corp.fileconfig.service.FileConfigService;
import com.zjft.usp.anyfix.corp.fileconfig.service.WorkFilesService;
import com.zjft.usp.anyfix.work.assign.dto.WorkAssignDto;
import com.zjft.usp.anyfix.work.assign.dto.WorkAssignEngineerDto;
import com.zjft.usp.anyfix.work.assign.model.WorkAssign;
import com.zjft.usp.anyfix.work.assign.model.WorkAssignEngineer;
import com.zjft.usp.anyfix.work.assign.service.WorkAssignEngineerService;
import com.zjft.usp.anyfix.work.assign.service.WorkAssignService;
import com.zjft.usp.anyfix.work.auto.enums.AssignModeEnum;
import com.zjft.usp.anyfix.work.check.enums.FeeCheckStatusEnum;
import com.zjft.usp.anyfix.work.check.enums.ServiceCheckStatusEnum;
import com.zjft.usp.anyfix.work.check.model.WorkCheck;
import com.zjft.usp.anyfix.work.check.service.WorkCheckService;
import com.zjft.usp.anyfix.work.deal.dto.EngineerDto;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDto;
import com.zjft.usp.anyfix.work.fee.model.WorkFee;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeDetailService;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeService;
import com.zjft.usp.anyfix.work.finish.composite.WorkFinishCompoService;
import com.zjft.usp.anyfix.work.finish.dto.WorkFinishDto;
import com.zjft.usp.anyfix.work.finish.enums.WorkFeeStatusEnum;
import com.zjft.usp.anyfix.work.finish.model.WorkFinish;
import com.zjft.usp.anyfix.work.finish.service.WorkFinishService;
import com.zjft.usp.anyfix.work.listener.WorkMqTopic;
import com.zjft.usp.anyfix.work.operate.dto.WorkOperateDto;
import com.zjft.usp.anyfix.work.operate.enums.WorkOperateTypeEnum;
import com.zjft.usp.anyfix.work.operate.model.WorkOperate;
import com.zjft.usp.anyfix.work.operate.service.WorkOperateService;
import com.zjft.usp.anyfix.work.remind.enums.WorkRemindTypeEnum;
import com.zjft.usp.anyfix.work.remind.service.WorkRemindDealService;
import com.zjft.usp.anyfix.work.request.composite.WorkRequestCompoService;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.anyfix.work.request.dto.WorkRequestDto;
import com.zjft.usp.anyfix.work.request.enums.ServiceModeEnum;
import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
import com.zjft.usp.anyfix.work.request.model.WorkRequest;
import com.zjft.usp.anyfix.work.request.service.WorkRequestService;
import com.zjft.usp.anyfix.work.sign.model.WorkSign;
import com.zjft.usp.anyfix.work.transfer.enums.WorkTransferEnum;
import com.zjft.usp.anyfix.work.transfer.model.WorkTransfer;
import com.zjft.usp.anyfix.work.transfer.service.WorkTransferService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.device.dto.DeviceInfoDto;
import com.zjft.usp.device.service.DeviceFeignService;
import com.zjft.usp.file.service.FileFeignService;
import com.zjft.usp.uas.dto.CorpDto;
import com.zjft.usp.uas.service.UasFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.zjft.usp.anyfix.corp.fileconfig.constant.GroupConstant.FORM_PREFIX;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class WorkFinishCompoServiceImpl implements WorkFinishCompoService {

    @Autowired
    private WorkRequestService workRequestService;
    @Autowired
    private WorkDealService workDealService;
    @Autowired
    private DeviceBranchService deviceBranchService;
    @Autowired
    private WorkFinishService workFinishService;
    @Resource
    private DeviceFeignService deviceFeignService;
    @Autowired
    private CustomFieldDataService customFieldDataService;
    @Autowired
    private WorkFeeService workFeeService;
    @Autowired
    private WorkFeeDetailService workFeeDetailService;
    @Autowired
    private WorkCheckService workCheckService;
    @Autowired
    private WorkOperateService workOperateService;
    @Autowired
    private WorkRequestCompoService workRequestCompoService;
    @Autowired
    private FileFeignService fileFeignService;
    @Autowired
    private UasFeignService uasFeignService;
    @Autowired
    private ServiceBranchService serviceBranchService;
    @Autowired
    private WorkRemindDealService workRemindDealService;
    @Autowired
    private ServiceConfigService serviceConfigService;
    @Autowired
    private FileConfigService fileConfigService;
    @Autowired
    private WorkFilesService workFilesService;
    @Autowired
    private WorkAssignService workAssignService;
    @Autowired
    private WorkTransferService workTransferService;
    @Autowired
    private WorkAssignEngineerService workAssignEngineerService;
    @Autowired
    private WorkTypeService workTypeService;

    /**
     * 工程师现场服务工单
     *
     * @param workFinishDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/1/21 14:57
     **/
    @Override
    public void localeServiceWork(WorkFinishDto workFinishDto, UserInfo userInfo, ReqParam reqParam) {
        // 进行校验参数
        List<ServiceConfigDto> serviceConfigList = serviceConfigService.
                getConfigItemByWorkId(workFinishDto.getWorkId(), ItemConfig.WORK_FINISH_CONFIG__FORM_MAP);
        serviceConfigService.validateServiceConfig(serviceConfigList, workFinishDto,
                ItemConfig.WORK_FINISH_CONFIG__FORM_MAP);
        WorkRequest workRequest = workRequestService.getById(workFinishDto.getWorkId());
        WorkDeal workDeal = workDealService.getById(workFinishDto.getWorkId());
        FileConfigFilter fileConfigFilter = new FileConfigFilter();
        fileConfigFilter.setDemanderCorp(workFinishDto.getDemanderCorp());
        fileConfigFilter.setServiceCorp(workFinishDto.getServiceCorp());
        fileConfigFilter.setFormType(FormTypeEnum.SERVICE_FINISH.getCode());
        fileConfigFilter.setWorkType(workFinishDto.getWorkSysType());
        List<FileConfigDto> fileConfigDtos = fileConfigService.getFileConfigListByDemander(fileConfigFilter);
        if (CollectionUtil.isNotEmpty(fileConfigDtos)) {
            boolean isValid = this.validateFileConfig(fileConfigDtos, workFinishDto.getFileConfigListMap());
            if (isValid) {
                workFinishDto.setFilesStatus("1");
            } else {
                workFinishDto.setFilesStatus("2");
            }
        }
        if (workRequest == null || workDeal == null) {
            throw new AppException("工单不存在！");
        }
        if (!workDeal.getWorkStatus().equals(WorkStatusEnum.IN_SERVICE.getCode())) {
            throw new AppException("工单状态不正确，当前状态为[" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "]！");
        }
        if (!workDeal.getEngineer().equals(userInfo.getUserId())) {
            throw new AppException("工单的服务工程师不是您，不能提交！");
        }
        if (workFinishDto.getStartTime() == null) {
            throw new AppException("服务开始时间不能为空！");
        }
        if (workFinishDto.getEndTime() == null) {
            throw new AppException("服务结束时间不能为空！");
        }
        if (workFinishDto.getEndTime().before(workFinishDto.getStartTime())) {
            throw new AppException("服务开始时间不能大于服务结束时间！");
        }
        if (workFinishDto.getStartTime().before(workDeal.getSignTime())) {
            throw new AppException("服务开始时间不能小于签到时间！");
        }
        Date finishTime = DateUtil.date();
        if (finishTime.before(workFinishDto.getEndTime())) {
            throw new AppException("服务结束时间不能晚于当前时间！");
        }
        if (StrUtil.isEmpty(workFinishDto.getSignatureStatus())) {
            throw new AppException("客户签名不能为空！");
        }
//        // 先新增设备网点基础数据
        Long deviceBranchId = workFinishDto.getDeviceBranch();
        String deviceBranchName = StrUtil.trimToEmpty(workFinishDto.getDeviceBranchName());
        if (LongUtil.isZero(deviceBranchId) && StrUtil.isNotBlank(deviceBranchName)) {
            deviceBranchId = this.addDeviceBranch(workFinishDto, userInfo, reqParam);
            workFinishDto.setDeviceBranch(deviceBranchId);
        }
        // 检查工单数据
        this.checkWorkByLocalFinish(workFinishDto, workRequest);
        // 更新工单请求
        this.modWorkRequest(workFinishDto);
        // 添加自定义字段
        if (CollectionUtil.isNotEmpty(workFinishDto.getCustomFieldDataList())) {
            for (CustomFieldData customFieldData : workFinishDto.getCustomFieldDataList()) {
                customFieldData.setFormId(workFinishDto.getWorkId());
            }
            customFieldDataService.addCustomFieldDataList(workFinishDto.getCustomFieldDataList());
        }
        this.saveBatchFileGroupList(fileConfigDtos, workFinishDto.getFileConfigListMap(), workFinishDto.getWorkId());
        // 同步设备档案信息
        DeviceInfoDto deviceInfoDto = this.editDeviceInfoByFinish(workFinishDto, workDeal, userInfo);
        try {
            workRequest = new WorkRequest();
            workRequest.setWorkId(workFinishDto.getWorkId());
            if (LongUtil.isZero(workFinishDto.getModel()) && deviceInfoDto != null) {
                workRequest.setModel(deviceInfoDto.getModelId());
            }
            if (LongUtil.isZero(workFinishDto.getSpecification()) && deviceInfoDto != null) {
                workRequest.setSpecification(deviceInfoDto.getSpecificationId());
            }
            if (LongUtil.isNotZero(workRequest.getModel()) || LongUtil.isNotZero(workRequest.getSpecification())) {
                workRequestService.updateById(workRequest);
            }

            workDeal = new WorkDeal();
            workDeal.setWorkId(workFinishDto.getWorkId());
            workDeal.setManDay(workFinishDto.getManDay());
            if (deviceInfoDto != null && LongUtil.isNotZero(deviceInfoDto.getDeviceId())) {
                workDeal.setDeviceId(deviceInfoDto.getDeviceId());
            }
            workDealService.updateById(workDeal);
        } catch (Exception e) {
            log.error("服务完成关单时，更新工单信息失败：{}", e);
        }
        if (LongUtil.isZero(workFinishDto.getModel()) && deviceInfoDto != null) {
            workFinishDto.setModel(deviceInfoDto.getModelId());
        }
        if (LongUtil.isZero(workFinishDto.getSpecification()) && deviceInfoDto != null) {
            workFinishDto.setSpecification(deviceInfoDto.getSpecificationId());
        }
        workFinishService.localeServiceWork(workFinishDto, userInfo, reqParam);
        workRemindDealService.addMessageQueue(WorkMqTopic.WORK_REMIND_DEAL, workFinishDto.getWorkId(),
                WorkRemindTypeEnum.IN_SERVICE_EXPIRE.getCode(), workFinishDto.getEndTime());
    }

    /**
     * 补录工单
     *
     * @param workDto
     * @param userInfo
     * @param reqParam
     */
    @Override
    public List<Long> supplementWork(WorkDto workDto, UserInfo userInfo, ReqParam reqParam) {

        List<Long> workIdList = new ArrayList<>();

        WorkDeal workDeal = new WorkDeal();
        WorkRequestDto workRequestDto = new WorkRequestDto();

        WorkFinishDto workFinishDto = new WorkFinishDto();
        WorkFinish workFinish = new WorkFinish();
        WorkRequest workRequest = new WorkRequest();
        workDto.setBookTimeBegin(workDto.getBookTimeEnd());
        BeanUtils.copyProperties(workDto, workRequest, new String[]{"files"});

        BeanUtils.copyProperties(workDto, workDeal);
        BeanUtils.copyProperties(workDto, workRequestDto, new String[]{"files"});
        BeanUtils.copyProperties(workDto, workFinishDto);

        WorkFeeDto workFeeDto = new WorkFeeDto();
        BeanUtils.copyProperties(workDto, workFeeDto);
        workFinishDto.setWorkFeeDto(workFeeDto);

        workRequestDto.setFiles(workDto.getWorkFiles());
        workRequest.setFiles(workDto.getWorkFiles());
        workRequestDto.setIsSupplement("Y");
        workRequest.setIsSupplement("Y");

        workDeal.setWorkStatus(WorkStatusEnum.CLOSED.getCode());
        workDeal.setEngineer(userInfo.getUserId());
        workDeal.setCreator(userInfo.getUserId());


        // workFinishDto里不包含workType，会覆盖，所以去除
        BeanUtils.copyProperties(workDto, workFinish, new String[]{"workType"});


        // 如果已有客户有新增网点数据
        if (!LongUtil.isZero(workDto.getCustomId()) || !LongUtil.isZero(workDto.getCustomCorp())) {
            // 先新增设备网点基础数据
            this.setDeviceBranch(workFinishDto, userInfo, reqParam);
            workRequestDto.setDeviceBranch(workFinishDto.getDeviceBranch());
        }
        // 检查设备信息
        workRequestCompoService.checkDeviceInfoByAdd(workRequestDto);


        if (workDto.getServiceMode() == ServiceModeEnum.REMOTE_SERVICE.getCode()) {
            // 远程服务
            workIdList = this.addWorkRequest(workRequestDto, userInfo, reqParam);

            for (Long workId : workIdList) {
                if (StrUtil.isBlank(workFinishDto.getDescription())) {
                    throw new AppException("服务情况不能为空！");
                }
                WorkRequest workRequestTemp = workRequestService.getById(workId);
                workFinishDto.setWorkId(workId);
                workRequestDto.setWorkId(workId);
                workDeal.setWorkId(workId);
                workFinish.setWorkId(workId);
                workDeal.setWorkFeeStatus(WorkFeeStatusEnum.FILLED.getCode());
                workDeal.setFeeCheckStatus(FeeCheckStatusEnum.UN_CHECK.getCode());
                workDeal.setWorkCode(workRequestTemp.getWorkCode());
                workRequestDto.setWorkCode(workRequestTemp.getWorkCode());
                this.setDeviceBranchNoCustom(workDto, workId, workFinishDto, workDeal, userInfo, reqParam);
                this.addManyOperate(workDeal, workDto, userInfo, reqParam);


                workDeal.setCreateTime(DateUtil.date());
                // 服务时间作为提单时间
                workDeal.setDispatchTime(workDto.getServiceTime());
                // 服务时间作为手动输入的服务完成时间
                workDeal.setEndTime(workDto.getServiceTime());

                workDeal.setFinishTime(DateUtil.date());

                workDealService.save(workDeal);

                //设置完成时间
                Date endTime = workDto.getServiceTime();

                workFinish.setStartTime(new Timestamp(endTime.getTime()));
                workFinish.setEndTime(new Timestamp(endTime.getTime()));
                workFinish.setOperator(userInfo.getUserId());
                workFinish.setOperateTime(new Timestamp(endTime.getTime()));
                workFinishService.save(workFinish);

                // 更新工单费用
                workFinishService.updateRomoteWorkFee(workFinishDto, userInfo, workDto);
                // 添加自定义字段
                workFinishService.setCustomFieldDateList(workFinishDto);
                // 添加操作记录
                WorkOperate workOperate = new WorkOperate();
                workOperate.setWorkId(workDeal.getWorkId());
                workOperate.setWorkStatus(WorkStatusEnum.CLOSED.getCode());
                workOperate.setOperator(userInfo.getUserId());
                workOperate.setCorp(reqParam.getCorpId());
                workOperateService.addWorkOperateByRemoteService(workOperate);
                //workFinishService.addWorkOperateBySupplementWork(workRequestDto,workDeal.getServiceMode(), userInfo, reqParam);

            }

        } else if (workDto.getServiceMode() == ServiceModeEnum.LOCALE_SERVICE.getCode()) {
            // 现场服务

            // 检查工单数据
            this.checkWorkByLocalFinish(workFinishDto, workRequest);
            workRequestDto.setServiceTime(workDeal.getStartTime());
            // 同步设备档案信息
            workIdList = this.addWorkRequest(workRequestDto, userInfo, reqParam);
            for (Long workId : workIdList) {
                WorkRequest workRequestTemp = workRequestService.getById(workId);
                workFinishDto.setWorkId(workId);
                workRequestDto.setWorkId(workId);
                workDeal.setWorkId(workId);
                workFinish.setWorkId(workId);
                workDeal.setWorkCode(workRequestTemp.getWorkCode());
                workRequestDto.setWorkCode(workRequestTemp.getWorkCode());
                this.setDeviceBranchNoCustom(workDto, workId, workFinishDto, workDeal, userInfo, reqParam);
                this.addManyOperate(workDeal, workDto, userInfo, reqParam);

                // 签到
                this.addWorkOperateBySign(workDeal, userInfo, reqParam);
                // 服务完成
                workDeal.setCreateTime(DateUtil.date());
                // 服务时间作为提单时间
                workDeal.setDispatchTime(workDto.getStartTime());
                workDeal.setFinishTime(DateUtil.date());
                if (WorkFeeStatusEnum.FILLED.getCode().equals(workDto.getWorkFeeStatus())) {
                    workDeal.setFeeCheckStatus(FeeCheckStatusEnum.UN_CHECK.getCode());
                }

                workDealService.save(workDeal);

                // 添加自定义字段
                workFinishService.setCustomFieldDateList(workFinishDto);

                workFinishService.supplementWork(workDto, workFinishDto, workRequestDto, workDeal, userInfo, reqParam);

            }
        }
        return workIdList;

    }

    @Override
    public List<Long> supplementCreateWork(WorkDto workDto, UserInfo userInfo, ReqParam reqParam) {
        WorkRequestDto workRequestDto = new WorkRequestDto();
        // 设置提单
        workDto.setBookTimeBegin(workDto.getBookTimeEnd());
        BeanUtils.copyProperties(workDto, workRequestDto);
        // 设置为是补单
        workRequestDto.setIsSupplement("Y");
        workRequestDto.setCreatorCorpId(reqParam.getCorpId());

        workRequestDto.setServiceCorp(null);
        List<Long> workIdList = this.addWorkRequest(workRequestDto, userInfo, reqParam);
        for (Long workId : workIdList) {
            WorkDeal workDealFound = workDealService.getById(workId);
            this.addManyOperate(workDealFound, workDto, userInfo, reqParam);
        }
        return workIdList;
    }

    public void updateWorkDeal(WorkDeal workDeal, WorkDto workDto, UserInfo userInfo) {
        // 修改提单信息
        workDeal.setServiceCorp(workDto.getServiceCorp());
        workDeal.setServiceBranch(workDto.getServiceBranch());
        workDeal.setDispatchStaff(userInfo.getUserId());
        workDeal.setDispatchTime(workDto.getDispatchTime());

        //修改分配日期
        workDeal.setHandleStaff(userInfo.getUserId());
        workDeal.setHandleTime(workDto.getDispatchTime());

        //修改派单信息
        workDeal.setAssignStaff(userInfo.getUserId());
        workDeal.setAssignTime(workDto.getDispatchTime());
        workDeal.setAssignMode(AssignModeEnum.AUTO.getCode());
        // 设置接单时间
        workDeal.setWorkStatus(WorkStatusEnum.TO_SIGN.getCode());
        workDeal.setEngineer(userInfo.getUserId());
        workDeal.setAcceptTime(workDto.getDispatchTime());
        workDeal.setServiceMode(ServiceModeEnum.LOCALE_SERVICE.getCode());
        workDealService.updateById(workDeal);
    }


    public void addManyOperate(WorkDeal workDeal, WorkDto workDto, UserInfo userInfo, ReqParam reqParam) {
        // 自动提单操作记录
        this.addAutoWorkOperate(workDeal, workDto, userInfo, reqParam);
        // 分配服务网点操作记录
        this.addAutoWorkOperateServiceBranch(workDeal, workDto, userInfo, reqParam);
        // 派单
        this.addWorkOperateByAutoAssign(workDeal, workDto, userInfo, reqParam);
        // 接单
        this.addWorkOperateByClaim(workDeal, userInfo, reqParam);
        //更新workDeal表
        this.updateWorkDeal(workDeal, workDto, userInfo);

    }

    /**
     * 自动提单操作记录
     *
     * @param workDeal
     */
    public void addAutoWorkOperate(WorkDeal workDeal, WorkDto workDto, UserInfo userInfo, ReqParam reqParam) {
        WorkOperate workOperate = new WorkOperate();
        workOperate.setWorkId(workDeal.getWorkId());
        workOperate.setWorkStatus(WorkStatusEnum.TO_HANDLE.getCode());
        workOperate.setOperateType(WorkOperateTypeEnum.AUTO_HANDLE.getCode());


        // 设置流转方式表
        WorkTransfer workTransfer = new WorkTransfer();
        BeanUtils.copyProperties(workDto, workTransfer);
        workTransfer.setId(KeyUtil.getId());
        workTransfer.setMode(WorkTransferEnum.AUTO_HANDLE.getCode());
        workTransfer.setLat(reqParam.getLat());
        workTransfer.setLon(reqParam.getLon());
        workTransfer.setOperator(userInfo.getUserId());
        workTransfer.setOperateTime(DateUtil.date().toTimestamp());
        workTransferService.save(workTransfer);

        Result corpResult = uasFeignService.findCorpById(workDeal.getServiceCorp());
        CorpDto corpDto = JsonUtil.parseObject(JsonUtil.toJson(corpResult.getData()), CorpDto.class);
        workOperate.setSummary("自动提交服务商");
        if (StrUtil.isNotBlank(corpDto.getCorpName())) {
            workOperate.setSummary(workOperate.getSummary() + " " + corpDto.getCorpName());
        }
        workOperateService.addAutoWorkOperate(workOperate);
    }


    /**
     * 自动分配服务网点操作记录
     *
     * @param workDeal
     */
    public void addAutoWorkOperateServiceBranch(WorkDeal workDeal, WorkDto workDto, UserInfo userInfo, ReqParam reqParam) {
        WorkOperate workOperate = new WorkOperate();
        workOperate.setWorkId(workDeal.getWorkId());
        workOperate.setWorkStatus(WorkStatusEnum.TO_ASSIGN.getCode());
        workOperate.setOperateType(WorkOperateTypeEnum.AUTO_DISPATCH_BRANCH.getCode());


        WorkTransfer workTransfer = new WorkTransfer();
        BeanUtils.copyProperties(workDto, workTransfer);
        workTransfer.setId(KeyUtil.getId());
        workTransfer.setMode(WorkTransferEnum.AUTO_DISPATCH.getCode());
        workTransfer.setLat(reqParam.getLat());
        workTransfer.setLon(reqParam.getLon());
        workTransfer.setOperator(userInfo.getUserId());
        workTransfer.setOperateTime(DateUtil.date().toTimestamp());
        workTransfer.setServiceCorp(workDeal.getServiceCorp());
        workTransfer.setServiceBranch(workDeal.getServiceBranch());
        workTransferService.save(workTransfer);

        ServiceBranch serviceBranchEntity = serviceBranchService.getById(workDeal.getServiceBranch());
        workOperate.setSummary("自动分配服务商网点");
        if (serviceBranchEntity != null) {
            workOperate.setSummary(workOperate.getSummary() + " " + serviceBranchEntity.getBranchName());
        }
        workOperate.setSummary(workOperate.getSummary() + " 并自动受理");

        workOperateService.addAutoWorkOperate(workOperate);
    }

    /**
     * 派单工程师
     *
     * @param workDeal
     */
    public void addWorkOperateByAutoAssign(WorkDeal workDeal, WorkDto workDto, UserInfo userInfo, ReqParam reqParam) {
        //插入派单记录
        WorkAssign workAssign = new WorkAssign();
        BeanUtils.copyProperties(workDeal, workAssign);
        workAssign.setAssignId(KeyUtil.getId());
        workAssign.setEnabled(EnabledEnum.YES.getCode());
        workAssign.setAssignTime(workDeal.getAssignTime());
        workAssign.setStaffId(workDto.getCustomCorp());
        workAssignService.save(workAssign);
        //插入工程师派单记录
        WorkAssignEngineer workAssignEngineer;
        for (WorkAssignEngineerDto workAssignEngineerDto : workDto.getAssignEngineerList()) {
            workAssignEngineer = new WorkAssignEngineer();
            workAssignEngineer.setAssignId(workAssign.getAssignId());
            workAssignEngineer.setEngineerId(workAssignEngineerDto.getUserId());
            workAssignEngineerService.save(workAssignEngineer);
        }

        // 添加操作记录
        WorkOperate workOperate = new WorkOperate();
        workOperate.setWorkId(workDeal.getWorkId());
        workOperate.setReferId(workAssign.getAssignId());
        workOperate.setOperator(userInfo.getUserId());
        workOperate.setCorp(reqParam.getCorpId());

        WorkAssignDto workAssignDto = new WorkAssignDto();
        List<WorkAssignEngineerDto> assignEngineerList = new ArrayList<>();
        WorkAssignEngineerDto workAssignEngineerDto = new WorkAssignEngineerDto();
        workAssignEngineerDto.setUserId(userInfo.getUserId());
        Result userRealResult = uasFeignService.findUserRealDtoById(userInfo.getUserId());
        UserRealDto userRealDto = JsonUtil.parseObject(JsonUtil.toJson(userRealResult.getData()), UserRealDto.class);
        if (userRealDto != null && StrUtil.isNotBlank(userRealDto.getUserName())) {
            workAssignEngineerDto.setUserName(userRealDto.getUserName());
        }
        assignEngineerList.add(workAssignEngineerDto);
        workAssignDto.setAssignEngineerList(assignEngineerList);
        workOperateService.addWorkOperateByAutoAssign(workOperate, workAssignDto);
    }

    /**
     * 接单
     *
     * @param workDeal
     * @param userInfo
     * @param reqParam
     */
    public void addWorkOperateByClaim(WorkDeal workDeal, UserInfo userInfo, ReqParam reqParam) {
        WorkTransfer workTransfer = new WorkTransfer();
        BeanUtils.copyProperties(workDeal, workTransfer);
        workTransfer.setId(KeyUtil.getId());
        workTransfer.setMode(WorkTransferEnum.CLAIM_WORK.getCode());
        workTransfer.setLat(reqParam.getLat());
        workTransfer.setLon(reqParam.getLon());
        workTransfer.setOperator(userInfo.getUserId());
        workTransfer.setOperateTime(DateUtil.date());
        workTransferService.save(workTransfer);

        // 添加操作记录
        WorkOperate workOperate = new WorkOperateDto();
        workOperate.setWorkId(workDeal.getWorkId());
        workOperate.setOperator(userInfo.getUserId());
        workOperate.setCorp(reqParam.getCorpId());
        workOperateService.addWorkOperateByClaim(workOperate);
    }


    /**
     * 工程师签到操作记录
     *
     * @param workDeal
     * @param userInfo
     * @param reqParam
     */
    public void addWorkOperateBySign(WorkDeal workDeal, UserInfo userInfo, ReqParam reqParam) {
        WorkOperate workOperate = new WorkOperate();
        workOperate.setWorkId(workDeal.getWorkId());
        workOperate.setWorkStatus(WorkStatusEnum.IN_SERVICE.getCode());
        workOperate.setOperator(userInfo.getUserId());
        workOperate.setCorp(reqParam.getCorpId());
        workOperateService.addWorkOperateBySign(workOperate, new WorkSign());
    }


    /**
     * 工程师修改现场服务完成信息
     *
     * @param workFinishDto
     * @param userInfo
     * @param reqParam
     */
    @Override
    public void updateLocaleFinish(WorkFinishDto workFinishDto, UserInfo userInfo, ReqParam reqParam) {
        // 进行校验参数
        List<ServiceConfigDto> serviceConfigList = serviceConfigService.
                getConfigItemByWorkId(workFinishDto.getWorkId(), ItemConfig.WORK_FINISH_CONFIG__FORM_MAP);
        serviceConfigService.validateServiceConfig(serviceConfigList, workFinishDto,
                ItemConfig.WORK_FINISH_CONFIG__FORM_MAP);
        WorkRequest workRequest = workRequestService.getById(workFinishDto.getWorkId());
        WorkDeal workDeal = workDealService.getById(workFinishDto.getWorkId());
        List<FileConfigDto> fileConfigDtos = null;
        if (!workFinishDto.isForFinishFile()) {
            FileConfigFilter fileConfigFilter = new FileConfigFilter();
            fileConfigFilter.setDemanderCorp(workFinishDto.getDemanderCorp());
            fileConfigFilter.setServiceCorp(workFinishDto.getServiceCorp());
            fileConfigFilter.setFormType(FormTypeEnum.SERVICE_FINISH.getCode());
            fileConfigFilter.setWorkType(workFinishDto.getWorkSysType());
            fileConfigDtos = fileConfigService.getFileConfigListByDemander(fileConfigFilter);
            if (CollectionUtil.isNotEmpty(fileConfigDtos)) {
                boolean isValid = this.validateFileConfig(fileConfigDtos, workFinishDto.getFileConfigListMap());
                if (isValid) {
                    workFinishDto.setFilesStatus("1");
                } else {
                    workFinishDto.setFilesStatus("2");
                }
            }
        }
        if (workRequest == null || workDeal == null) {
            throw new AppException("工单不存在！");
        }
        if (!workDeal.getWorkStatus().equals(WorkStatusEnum.CLOSED.getCode())) {
            throw new AppException("工单状态不正确，当前状态为[" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "]！");
        }
        if (!workDeal.getEngineer().equals(userInfo.getUserId())) {
            throw new AppException("工单的服务工程师不是您，不能提交！");
        }
        WorkCheck workCheck = workCheckService.getById(workFinishDto.getWorkId());
        if (workCheck != null && ServiceCheckStatusEnum.CHECK_PASS.getCode().equals(workCheck.getFinishCheckStatus())) {
            throw new AppException("服务已审核通过，不能再次修改！");
        }
        if (workFinishDto.getStartTime() == null) {
            throw new AppException("服务开始时间不能为空！");
        }
        if (workFinishDto.getEndTime() == null) {
            throw new AppException("服务完成时间不能为空！");
        }
        if (workFinishDto.getEndTime().before(workFinishDto.getStartTime())) {
            throw new AppException("服务开始时间不能大于服务结束时间！");
        }
        if (workDeal.getFinishTime().before(workFinishDto.getEndTime())) {
            throw new AppException("服务结束时间不能大于完成操作时间！");
        }
        if (workFinishDto.getStartTime().before(workDeal.getSignTime())) {
            throw new AppException("服务开始时间不能小于签到时间！");
        }

        // 检查工单数据
        this.checkWorkByLocalFinish(workFinishDto, workRequest);

        // 先新增设备网点基础数据
        Long deviceBranchId = workFinishDto.getDeviceBranch();
        String deviceBranchName = StrUtil.trimToEmpty(workFinishDto.getDeviceBranchName());
        if (LongUtil.isZero(deviceBranchId) && StrUtil.isNotBlank(deviceBranchName)) {
            deviceBranchId = this.addDeviceBranch(workFinishDto, userInfo, reqParam);
            workFinishDto.setDeviceBranch(deviceBranchId);
        }
        // 更新工单请求
        this.modWorkRequest(workFinishDto);

        // 更新自定义字段
        if (CollectionUtil.isNotEmpty(workFinishDto.getCustomFieldDataList())) {
            customFieldDataService.modCustomFieldDataList(workFinishDto.getWorkId(), workFinishDto.getCustomFieldDataList());
        }
        if (!workFinishDto.isForFinishFile() && CollectionUtil.isNotEmpty(fileConfigDtos)) {
            this.updateBatchFileGroup(fileConfigDtos, workFinishDto.getFileConfigListMap(), workFinishDto.getWorkId());
        }
        // 同步设备档案信息
        DeviceInfoDto deviceInfoDto = this.editDeviceInfoByFinish(workFinishDto, workDeal, userInfo);
        try {
            workRequest = new WorkRequest();
            workRequest.setWorkId(workFinishDto.getWorkId());
            if (LongUtil.isZero(workFinishDto.getModel()) && deviceInfoDto != null) {
                workRequest.setModel(deviceInfoDto.getModelId());
            }
            if (LongUtil.isZero(workFinishDto.getSpecification()) && deviceInfoDto != null) {
                workRequest.setSpecification(deviceInfoDto.getSpecificationId());
            }
            if (LongUtil.isNotZero(workRequest.getModel()) || LongUtil.isNotZero(workRequest.getSpecification())) {
                workRequestService.updateById(workRequest);
            }

            workDeal = new WorkDeal();
            workDeal.setWorkId(workFinishDto.getWorkId());
            workDeal.setManDay(workFinishDto.getManDay());
            if (deviceInfoDto != null && LongUtil.isNotZero(deviceInfoDto.getDeviceId())) {
                workDeal.setDeviceId(deviceInfoDto.getDeviceId());
            }
            workDealService.updateById(workDeal);
        } catch (Exception e) {
            log.error("修改完成信息时，更新工单信息失败：{}", e);
        }
        if (LongUtil.isZero(workFinishDto.getModel()) && deviceInfoDto != null) {
            workFinishDto.setModel(deviceInfoDto.getModelId());
        }
        if (LongUtil.isZero(workFinishDto.getSpecification()) && deviceInfoDto != null) {
            workFinishDto.setSpecification(deviceInfoDto.getSpecificationId());
        }
        workFinishService.updateLocaleFinish(workFinishDto, userInfo, reqParam);
    }

    /**
     * 修改工单费用
     *
     * @param workFinishDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/5/17 10:39
     **/
    @Override
    public void updateWorkFee(WorkFinishDto workFinishDto, Long curUserId, Long curCorpId) {
        WorkRequest workRequest = workRequestService.getById(workFinishDto.getWorkId());
        WorkDeal workDeal = workDealService.getById(workFinishDto.getWorkId());
        if (workRequest == null || workDeal == null) {
            throw new AppException("工单不存在！");
        }
        if (!workDeal.getWorkStatus().equals(WorkStatusEnum.CLOSED.getCode())) {
            throw new AppException("工单状态不正确，当前状态为[" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "]！");
        }
        WorkCheck workCheck = workCheckService.getById(workFinishDto.getWorkId());
        if (workCheck != null && FeeCheckStatusEnum.CHECK_PASS.getCode().equals(workCheck.getFeeCheckStatus())) {
            throw new AppException("费用已审核不通过，不能再次修改！");
        }

        // 修改工单处理信息表
        UpdateWrapper<WorkDeal> dealUpdateWrapper = new UpdateWrapper<>();
        dealUpdateWrapper.eq("work_id", workFinishDto.getWorkId());
        // 将服务商审核状态置为待审核
        if (WorkFeeStatusEnum.FILLED.getCode().equals(workFinishDto.getWorkFeeStatus())) {
            dealUpdateWrapper.set("work_fee_status", WorkFeeStatusEnum.FILLED.getCode());
            dealUpdateWrapper.set("fee_check_status", FeeCheckStatusEnum.UN_CHECK.getCode());
            dealUpdateWrapper.set("fee_check_time", null);
            workDealService.update(dealUpdateWrapper);
        }

        // 工单费用
        WorkFee workFee = workFeeService.getById(workFinishDto.getWorkId());
        if (workFee == null) {
            workFee = new WorkFee();
        }
        workFee.setWareUseFee(workFinishDto.getWorkFeeDto().getWareUseFee());
        workFee.setOtherFee(workFinishDto.getWorkFeeDto().getOtherFee());
        workFee.setOtherFeeNote(workFinishDto.getWorkFeeDto().getOtherFeeNote());
        workFee.setWorkId(workFinishDto.getWorkId());
        // 保存实施发生费用明细
        workFee.setImplementFee(this.workFeeDetailService.addByImplementFeeList(workFinishDto.getWorkFeeImplementDtoList(),
                workFinishDto.getWorkId(), curUserId));
        // 获取总费用
        workFee.setTotalFee(this.workFeeService.getTotalFee(workFee));
        // 保存工单费用
        this.workFeeService.saveOrUpdate(workFee);

        // 添加工单审核表（状态是未审核）
        if (workCheck != null) {
            if (WorkFeeStatusEnum.FILLED.getCode().equals(workDeal.getWorkFeeStatus())) {
                UpdateWrapper<WorkCheck> checkUpdateWrapper = new UpdateWrapper<>();
                checkUpdateWrapper.eq("work_id", workFinishDto.getWorkId());
                checkUpdateWrapper.set("fee_check_status", FeeCheckStatusEnum.UN_CHECK.getCode());
                checkUpdateWrapper.set("fee_check_user", 0L);
                checkUpdateWrapper.set("fee_check_note", "");
                checkUpdateWrapper.set("fee_check_time", null);
                workCheckService.update(checkUpdateWrapper);
            }
        } else {
            workCheck = new WorkCheck();
            workCheck.setWorkId(workFinishDto.getWorkId());
            workCheck.setFeeCheckStatus(FeeCheckStatusEnum.UN_CHECK.getCode());
            workCheckService.save(workCheck);
        }

        // 添加操作记录
        WorkOperate workOperate = new WorkOperate();
        workOperate.setWorkId(workDeal.getWorkId());
        workOperate.setWorkStatus(WorkStatusEnum.CLOSED.getCode());
        workOperate.setOperator(curUserId);
        workOperate.setCorp(curCorpId);
        workOperateService.addWorkOperateByUpdateWorkFee(workOperate);
    }

    /**
     * 修改服务内容
     *
     * @param workDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @date 2020/3/31
     */
    @Override
    public void modFinish(WorkDto workDto, String type, UserInfo userInfo, ReqParam reqParam) {
        WorkRequest workRequest = workRequestService.getById(workDto.getWorkId());
        WorkDeal workDeal = workDealService.getById(workDto.getWorkId());
        if (workRequest == null || workDeal == null) {
            throw new AppException("工单不存在，请核查！");
        }
        // 只有服务中或已完成，并且未审核、未确认的工单才能修改服务信息
        if (workDeal.getServiceCorp().equals(reqParam.getCorpId())) {
//                && workDeal.getDemanderCheckStatus() == DemanderCheckStatusEnum.UNCHECK.getCode()
//                && workDeal.getServiceCheckStatus() == ServiceCheckStatusEnum.UNCHECK.getCode()
//                && (workDeal.getWorkStatus() == WorkStatusEnum.IN_SERVICE.getCode()
//                || workDeal.getWorkStatus() == WorkStatusEnum.CLOSED.getCode())) {
            WorkFinish workFinish = workFinishService.getById(workDto.getWorkId());
            if ("file".equalsIgnoreCase(type)) {
                String oldFiles = workFinish.getFiles();
                // 修改服务完成文件
                workFinish.setWorkId(workDto.getWorkId());
                if (StrUtil.isNotBlank(workDto.getFinishDescription())) {
                    workFinish.setDescription(workDto.getFinishDescription());
                }
                workFinish.setFiles(workDto.getFiles());
                workFinish.setFilesStatus(workDto.getFilesStatus());
                List<Long> fileIdList = null;
                if (StrUtil.isNotBlank(workDto.getFiles())) {
                    // 删除临时文件表数据
                    List<String> fileIds = Arrays.asList(workDto.getFiles().split(","));
                    fileIdList = fileIds.stream().map(fileId -> Long.parseLong(fileId)).collect(Collectors.toList());
                    this.fileFeignService.deleteFileTemporaryByFileIdList(fileIdList);
                }
                workFinishService.updateById(workFinish);
                // 删除旧文件
                if (StrUtil.isNotBlank(oldFiles)) {
                    List<String> fileIdsOld = Arrays.asList(oldFiles.split(","));
                    List<Long> fileIdListOld = fileIdsOld.stream().map(fileId -> Long.parseLong(fileId)).collect(
                            Collectors.toList());
                    if (fileIdList != null) {
                        fileIdListOld.removeAll(fileIdList);
                    }
                    try {
                        for (Long fileId : fileIdListOld) {
                            this.fileFeignService.delFile(fileId);
                        }
                    } catch (Exception e) {
                        log.error("删除文件失败。", e);
                    }
                }
            } else {
                // 修改工单处理文件
                workDeal.setTraffic(workDto.getTraffic());
                workDeal.setTrafficNote(StrUtil.trimToEmpty(workDto.getTrafficNote()));

                if (workDto.getGoTime() != null && workDto.getSignTime() != null) {
                    if (workDto.getGoTime().after(workDto.getSignTime())) {
                        throw new AppException("到达时间不能早于出发时间！");
                    }
                }
                if (workDto.getGoTime() != null) {
                    workDeal.setGoTime(workDto.getGoTime());
                }
                if (workDto.getSignTime() != null) {
                    workDeal.setSignTime(workDto.getSignTime());
                }
                // 协同工程师
                String togetherEngineers = "";
                if (CollectionUtil.isNotEmpty(workDto.getTogetherEngineerList())) {
                    for (EngineerDto engineerDto : workDto.getTogetherEngineerList()) {
                        if (StrUtil.isBlank(togetherEngineers)) {
                            togetherEngineers = engineerDto.getEngineerId().toString();
                        } else {
                            togetherEngineers = togetherEngineers + ',' + engineerDto.getEngineerId().toString();
                        }
                    }
                }
                workDeal.setTogetherEngineers(togetherEngineers);
                workDeal.setHelpNames(StrUtil.trimToEmpty(workDto.getHelpNames()));

                if (workFinish != null) {
                    if (workDto.getStartTime() != null) {
                        workDeal.setStartTime(workDto.getStartTime());
                        workFinish.setStartTime(workDto.getStartTime());
                    }
                    if (workDto.getStartTime() != null && workDto.getEndTime() != null) {
                        if (workDto.getStartTime().after(workDto.getEndTime())) {
                            throw new AppException("服务完成时间不能早于服务开始时间！");
                        }
                    }
                    if (workDto.getEndTime() != null) {
                        workDeal.setEndTime(workDto.getEndTime());
                        workFinish.setEndTime(workDto.getEndTime());
                    }
                    workFinish.setDescription(StrUtil.trimToEmpty(workDto.getFinishDescription()));
                    workFinishService.updateById(workFinish);
                }
                workDealService.updateById(workDeal);
            }
        } else {
            throw new AppException("该工单不允许修改！");
        }
    }

    @Override
    public void supplementFiles(WorkFinishDto workFinishDto, UserInfo userInfo, ReqParam reqParam) {
        boolean isModFile;
        WorkFinish foundWorkFinish = workFinishService.getById(workFinishDto.getWorkId());
        if (!workFinishDto.isForFinishFile()) {
            FileConfigFilter fileConfigFilter = new FileConfigFilter();
            fileConfigFilter.setDemanderCorp(workFinishDto.getDemanderCorp());
            fileConfigFilter.setServiceCorp(workFinishDto.getServiceCorp());
            fileConfigFilter.setFormType(FormTypeEnum.SERVICE_FINISH.getCode());
            fileConfigFilter.setWorkType(workFinishDto.getWorkSysType());
            List<FileConfigDto> fileConfigDtos = fileConfigService.getFileConfigListByDemander(fileConfigFilter);
            if (CollectionUtil.isNotEmpty(fileConfigDtos)) {
                boolean isValid = this.validateFileConfig(fileConfigDtos, workFinishDto.getFileConfigListMap());
                if (isValid) {
                    workFinishDto.setFilesStatus("1");
                } else {
                    workFinishDto.setFilesStatus("2");
                }
            }
            isModFile = true;
            this.updateBatchFileGroup(fileConfigDtos, workFinishDto.getFileConfigListMap(), workFinishDto.getWorkId());
        } else {
            String beforeFiles = foundWorkFinish.getFiles();
            if (StrUtil.isNullOrUndefined(beforeFiles)) {
                beforeFiles = "";
            }
            isModFile = !beforeFiles.equals(workFinishDto.getFiles());
        }
        // 上传客户签名图片
        WorkFinish workFinish = new WorkFinish();
        boolean isModSignature = !foundWorkFinish.getSignatureStatus().equals(workFinishDto.getSignatureStatus());
        workFinish.setWorkId(workFinishDto.getWorkId());
        workFinish.setFiles(workFinishDto.getFiles());
        workFinish.setFilesStatus(workFinishDto.getFilesStatus());
        workFinish.setSignatureStatus(workFinishDto.getSignatureStatus());
        if (StrUtil.isNotEmpty(workFinishDto.getSignatureBase64())) {
            workFinish.setWorkId(workFinishDto.getWorkId());
            Result signatureResult = fileFeignService.uploadBase64Img(workFinishDto.getSignatureBase64());
            if (signatureResult != null && signatureResult.getCode() == Result.SUCCESS) {
                Long signature = (Long) signatureResult.getData();
                workFinish.setSignature(signature);
                // 上传成功后删除临时文件表数据
                this.fileFeignService.deleteFileTemporaryByID(signature);
            } else {
                throw new AppException("上传客户签名失败！");
            }
        }
        workFinishService.updateById(workFinish);
        // 添加操作记录
        WorkDeal workDeal = workDealService.getById(workFinish.getWorkId());
        WorkOperate workOperate = new WorkOperate();
        workOperate.setWorkId(workDeal.getWorkId());
        workOperate.setWorkStatus(workDeal.getWorkStatus());
        workOperate.setOperator(userInfo.getUserId());
        workOperate.setCorp(reqParam.getCorpId());
        workOperateService.addWorkOperateByModFiles(workOperate, isModFile, isModSignature);

    }

    /**
     * 新增客户以及设备网点
     *
     * @param workDto
     * @param workId
     * @param workFinishDto
     * @param workDeal
     * @param userInfo
     * @param reqParam
     */
    public void setDeviceBranchNoCustom(WorkDto workDto, Long workId, WorkFinishDto workFinishDto, WorkDeal workDeal,
                                        UserInfo userInfo, ReqParam reqParam) {
        if (LongUtil.isZero(workDto.getCustomId()) && LongUtil.isZero(workDto.getCustomCorp())) {
            WorkRequest workRequestTemp = workRequestService.getById(workId);
            workFinishDto.setCustomId(workRequestTemp.getCustomId());
            workFinishDto.setCustomCorp(workRequestTemp.getCustomCorp());
            workFinishDto.setDeviceBranch(workRequestTemp.getDeviceBranch());
            this.setDeviceBranch(workFinishDto, userInfo, reqParam);
            DeviceInfoDto deviceInfoDto = this.editDeviceInfoByFinish(workFinishDto, workDeal, userInfo);
            if (LongUtil.isZero(workFinishDto.getModel()) && deviceInfoDto != null) {
                workRequestTemp.setModel(deviceInfoDto.getModelId());
                workRequestService.saveOrUpdate(workRequestTemp);
                workDeal.setDeviceId(deviceInfoDto.getDeviceId());
            }
        }
    }


    /**
     * 先新增设备网点基础数据
     *
     * @param workFinishDto
     * @param userInfo
     * @param reqParam
     */
    public void setDeviceBranch(WorkFinishDto workFinishDto, UserInfo userInfo, ReqParam reqParam) {
        Long deviceBranchId = workFinishDto.getDeviceBranch();
        String deviceBranchName = StrUtil.trimToEmpty(workFinishDto.getDeviceBranchName());
        if (LongUtil.isZero(deviceBranchId) && StrUtil.isNotBlank(deviceBranchName)) {
            deviceBranchId = this.addDeviceBranch(workFinishDto, userInfo, reqParam);
            workFinishDto.setDeviceBranch(deviceBranchId);
        }
    }

    /**
     * 补录工单插入工单请求
     *
     * @param workRequestDto
     */
    private List<Long> addWorkRequest(WorkRequestDto workRequestDto, UserInfo userInfo, ReqParam reqParam) {
        List<Long> workIdList = workRequestCompoService.addWorkRequest(workRequestDto, userInfo, reqParam);

        return workIdList;
    }

    /**
     * 添加设备网点
     *
     * @param workFinishDto 工单完成数据
     * @param userInfo      当前用户
     * @param reqParam      公共参数
     * @return 网点编号
     * @author zgpi
     * @date 2020/1/16 14:24
     **/
    private Long addDeviceBranch(WorkFinishDto workFinishDto, UserInfo userInfo, ReqParam reqParam) {
        DeviceBranchDto deviceBranchDto = new DeviceBranchDto();
        deviceBranchDto.setCustomId(workFinishDto.getCustomId());
        deviceBranchDto.setCustomCorp(workFinishDto.getCustomCorp());
        deviceBranchDto.setBranchName(workFinishDto.getDeviceBranchName());
        String province = workFinishDto.getDistrict().length() >= 2 ? workFinishDto.getDistrict().substring(0, 2) : "";
        String city = workFinishDto.getDistrict().length() >= 4 ? workFinishDto.getDistrict().substring(0, 4) : "";
        String distinct = workFinishDto.getDistrict().length() >= 6 ? workFinishDto.getDistrict().substring(0, 6) : "";
        deviceBranchDto.setProvince(province);
        deviceBranchDto.setCity(city);
        deviceBranchDto.setDistrict(distinct);
        deviceBranchDto.setContactName(workFinishDto.getContactName());
        deviceBranchDto.setContactPhone(workFinishDto.getContactPhone());
        deviceBranchDto.setAddress(workFinishDto.getAddress());
        deviceBranchDto.setEnabled(EnabledEnum.YES.getCode());
        deviceBranchDto.setOperator(userInfo.getUserId());
        deviceBranchDto.setOperateTime(DateUtil.date());
        return deviceBranchService.addDeviceBranch(deviceBranchDto, userInfo, reqParam);
    }

    /**
     * 服务完成时更新工单请求
     *
     * @param workFinishDto
     * @return
     * @author zgpi
     * @date 2020/1/21 16:13
     **/
    private void modWorkRequest(WorkFinishDto workFinishDto) {
        WorkRequest workRequest = new WorkRequest();
        // 不复制文件属性，不然会覆盖掉原来的工单附件
        BeanUtils.copyProperties(workFinishDto, workRequest, new String[]{"files"});
        workRequestService.saveOrUpdate(workRequest);
    }

    /**
     * 服务完成时检查工单数据
     *
     * @param workFinishDto
     * @param workRequest
     * @return
     * @author zgpi
     * @date 2020/1/21 16:10
     **/
    private void checkWorkByLocalFinish(WorkFinishDto workFinishDto, WorkRequest workRequest) {
        // 安装、维护、巡检类工单需要填写设备
        boolean needCheck = false;
        WorkType workType = this.workTypeService.getById(workRequest.getWorkType());
        if (workType != null &&
                (workType.getSysType() == WorkSysTypeEnum.INSTALL.getCode()
                        || workType.getSysType() == WorkSysTypeEnum.MAINTAIN.getCode()
                        || workType.getSysType() == WorkSysTypeEnum.PATROL.getCode())) {
            needCheck = true;
        }
        Long smallClass = workFinishDto.getSmallClass();
        if (needCheck && LongUtil.isZero(smallClass)) {
            throw new AppException("设备分类不能为空！");
        }
        Long specification = workFinishDto.getSpecification();
        if (needCheck && LongUtil.isZero(specification)) {
//            throw new AppException("设备规格不能为空！");
        }
        Long model = workFinishDto.getModel();
        if (needCheck && LongUtil.isZero(model) && StrUtil.isBlank(workFinishDto.getModelName())) {
//            throw new AppException("设备型号不能为空！");
        }
        String serial = StrUtil.trimToEmpty(workFinishDto.getSerial());
        if (needCheck && StrUtil.isBlank(serial)) {
            throw new AppException("出厂序列号不能为空！");
        }
        String district = workFinishDto.getDistrict();
        if (StrUtil.isBlank(district)) {
            throw new AppException("行政区划不能为空！");
        }
        String contractName = StrUtil.trimToEmpty(workFinishDto.getContactName());
        if (StrUtil.isBlank(contractName)) {
            throw new AppException("联系人不能为空！");
        }
        String contractPhone = StrUtil.trimToEmpty(workFinishDto.getContactPhone());
        if (StrUtil.isBlank(contractPhone)) {
            throw new AppException("联系人电话不能为空！");
        }
        String address = StrUtil.trimToEmpty(workFinishDto.getAddress());
        if (StrUtil.isBlank(address)) {
            throw new AppException("详细地址不能为空！");
        }
        Integer zone = workFinishDto.getZone();
        if (needCheck && IntUtil.isZero(zone)) {
            throw new AppException("分布不能为空！");
        }
//        String files = workFinishDto.getFiles();
//        if (StrUtil.isBlank(files)) {
//            throw new AppException("完成附件不能为空！");
//        }
//        String signatureBase64 = workFinishDto.getSignatureBase64();
//        if (StrUtil.isBlank(signatureBase64) && LongUtil.isZero(workFinishDto.getSignature())) {
//            throw new AppException("客户签名不能为空！");
//        }
    }

    /**
     * 服务完成时同步设备档案信息
     * 若存在，则修改设备档案并返回设备号
     * 若不存在，则新增设备档案并返回设备号
     *
     * @param workFinishDto
     * @param workDeal
     * @param userInfo
     * @return
     * @author zgpi
     * @date 2020/1/21 16:10
     **/
    private DeviceInfoDto editDeviceInfoByFinish(WorkFinishDto workFinishDto, WorkDeal workDeal, UserInfo userInfo) {
        Long model = workFinishDto.getModel();
        String serial = StrUtil.trimToEmpty(workFinishDto.getSerial());
        if (LongUtil.isZero(model) && StrUtil.isBlank(serial)) {
            return null;
        }
        DeviceInfoDto filter = new DeviceInfoDto();
        filter.setSerial(serial);
        filter.setModelId(model);
        filter.setDemanderCorp(workFinishDto.getDemanderCorp());
        Result deviceInfoResult = deviceFeignService.findDeviceInfoListBy(JsonUtil.toJson(filter));
        List<DeviceInfoDto> deviceInfoDtoList;
        DeviceInfoDto deviceInfoDto = null;
        if (deviceInfoResult.getCode() == Result.SUCCESS) {
            deviceInfoDtoList = JsonUtil.parseArray(JsonUtil.toJson(deviceInfoResult.getData()), DeviceInfoDto.class);
        } else {
            throw new AppException(deviceInfoResult.getMsg());
        }
        if (CollectionUtil.isNotEmpty(deviceInfoDtoList)) {
            if (deviceInfoDtoList.size() > 1) {
                throw new AppException("设备档案中存在多台设备");
            }
            deviceInfoDto = deviceInfoDtoList.get(0);
        }
        // 存在设备档案，则修改，不存在设备档案，则新增档案
        deviceInfoDto = this.transferDeviceInfo(deviceInfoDto, workFinishDto, workDeal, userInfo);
        Result<DeviceInfoDto> editDeviceResult = deviceFeignService.editDeviceInfo(JsonUtil.toJson(deviceInfoDto));
        if (editDeviceResult != null) {
            if (editDeviceResult.getCode() == Result.SUCCESS) {
                deviceInfoDto = editDeviceResult.getData();
            } else {
                throw new AppException(editDeviceResult.getMsg());
            }
        } else {
            throw new AppException("服务完成时，同步设备档案信息失败！");
        }
        return deviceInfoDto;
    }

    /**
     * 转换设备档案对象
     *
     * @param workFinishDto
     * @param workDeal
     * @param userInfo
     * @return
     * @author zgpi
     * @date 2020/2/6 11:28
     */
    private DeviceInfoDto transferDeviceInfo(DeviceInfoDto deviceInfoDto,
                                             WorkFinishDto workFinishDto,
                                             WorkDeal workDeal,
                                             UserInfo userInfo) {
        if (deviceInfoDto == null) {
            deviceInfoDto = new DeviceInfoDto();
        }
        deviceInfoDto.setDemanderCorp(workFinishDto.getDemanderCorp());
        deviceInfoDto.setServiceCorp(workFinishDto.getServiceCorp());
        deviceInfoDto.setSmallClassId(workFinishDto.getSmallClass());
        deviceInfoDto.setSpecificationId(workFinishDto.getSpecification());
        deviceInfoDto.setSpecificationName(workFinishDto.getSpecificationName());
        deviceInfoDto.setBrandId(workFinishDto.getBrand());
        deviceInfoDto.setModelId(workFinishDto.getModel());
        deviceInfoDto.setModelName(workFinishDto.getModelName());
        deviceInfoDto.setSerial(workFinishDto.getSerial());
        deviceInfoDto.setDeviceCode(workFinishDto.getDeviceCode());
        deviceInfoDto.setCustomId(workFinishDto.getCustomId());
        deviceInfoDto.setCustomCorp(workFinishDto.getCustomCorp());
        deviceInfoDto.setBranchId(workFinishDto.getDeviceBranch());
        deviceInfoDto.setDistrict(workFinishDto.getDistrict());
        deviceInfoDto.setContactName(workFinishDto.getContactName());
        deviceInfoDto.setContactPhone(workFinishDto.getContactPhone());
        deviceInfoDto.setAddress(workFinishDto.getAddress());
        deviceInfoDto.setServiceBranch(workDeal.getServiceBranch());
        deviceInfoDto.setEngineer(workDeal.getEngineer());
        deviceInfoDto.setOperator(userInfo.getUserId());
        deviceInfoDto.setWorkId(workFinishDto.getWorkId());
        deviceInfoDto.setDeviceDescription(workFinishDto.getDeviceDescription());
        return deviceInfoDto;
    }

    public boolean validateFileConfig(List<FileConfigDto> fileConfigDtos, Map<String, List<Long>> groupFileListMap) {
        return fileConfigService.validateConfig(fileConfigDtos, groupFileListMap);
    }

    public void updateBatchFileGroup(List<FileConfigDto> fileConfigDtos,
                                     Map<String, List<Long>> groupFileListMap,
                                     Long workId) {
        List<WorkFiles> workFilesList = workFilesService.getWorkFileByWorkId(workId);
        Map<Long, WorkFiles> workFilesDtoMap = new HashMap<>();
        List<WorkFiles> insertList = new ArrayList<>();
        List<WorkFiles> updateList = new ArrayList<>();
        for (WorkFiles workFiles : workFilesList) {
            workFilesDtoMap.put(workFiles.getConfigId(), workFiles);
        }
        List<Long> fileIdList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(fileConfigDtos)) {
            for (FileConfigDto fileConfigDto : fileConfigDtos) {
                Long id = fileConfigDto.getId();
                List<Long> groupFileList = groupFileListMap.get(FORM_PREFIX + id);
                WorkFiles workFiles = workFilesDtoMap.get(id);
                String fileIds = "";
                if (groupFileList == null) {
                    continue;
                }
                if (CollectionUtil.isNotEmpty(groupFileList)) {
                    fileIds = CollectionUtil.join(groupFileList, ",");
                    fileIdList.addAll(groupFileList);
                }
                if (workFiles != null) {
                    // 进行更新
                    workFiles.setFileIds(fileIds);
                    updateList.add(workFiles);
                } else {
                    // 进行插入
                    workFiles = new WorkFiles();
                    workFiles.setId(KeyUtil.getId());
                    workFiles.setWorkId(workId);
                    workFiles.setConfigId(fileConfigDto.getId());
                    workFiles.setFileIds(fileIds);
                    insertList.add(workFiles);
                }
            }
        }
        if (CollectionUtil.isNotEmpty(insertList)) {
            workFilesService.saveBatch(insertList);
        }
        if (CollectionUtil.isNotEmpty(updateList)) {
            workFilesService.updateBatchById(updateList);
        }
        // 删除缓存表
        List<Long> list = fileIdList.stream().distinct().collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(list)) {
            this.fileFeignService.deleteFileTemporaryByFileIdList(list);
        }
    }

    public void saveBatchFileGroupList(List<FileConfigDto> fileConfigDtos,
                                       Map<String, List<Long>> groupFileListMap,
                                       Long workId) {
        List<WorkFiles> workFilesList = null;
        List<Long> fileIdList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(fileConfigDtos)) {
            workFilesList = new ArrayList<>();
            for (FileConfigDto fileConfigDto : fileConfigDtos) {
                Long id = fileConfigDto.getId();
                List<Long> groupFileList = groupFileListMap.get(FORM_PREFIX + id);
                if (CollectionUtil.isNotEmpty(groupFileList)) {
                    String groupFiles = CollectionUtil.join(groupFileList, ",");
                    WorkFiles workFiles = new WorkFiles();
                    workFiles.setId(KeyUtil.getId());
                    workFiles.setWorkId(workId);
                    workFiles.setConfigId(fileConfigDto.getId());
                    workFiles.setFileIds(groupFiles);
                    workFilesList.add(workFiles);
                    fileIdList.addAll(groupFileList);
                }
            }
        }
        if (CollectionUtil.isNotEmpty(workFilesList)) {
            workFilesService.saveBatch(workFilesList);
        }
        // 删除缓存表
        List<Long> list = fileIdList.stream().distinct().collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(list)) {
            this.fileFeignService.deleteFileTemporaryByFileIdList(list);
        }
    }
}
