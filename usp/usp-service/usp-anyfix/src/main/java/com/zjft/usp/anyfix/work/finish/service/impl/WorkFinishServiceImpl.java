package com.zjft.usp.anyfix.work.finish.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.model.CustomFieldData;
import com.zjft.usp.anyfix.baseinfo.service.CustomFieldDataService;
import com.zjft.usp.anyfix.work.check.enums.FeeCheckStatusEnum;
import com.zjft.usp.anyfix.work.check.enums.ServiceCheckStatusEnum;
import com.zjft.usp.anyfix.work.check.model.WorkCheck;
import com.zjft.usp.anyfix.work.check.service.WorkCheckService;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeImplementDto;
import com.zjft.usp.anyfix.work.fee.model.WorkFee;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeDetailService;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeImplementDefineService;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeService;
import com.zjft.usp.anyfix.work.finish.dto.WorkFinishDto;
import com.zjft.usp.anyfix.work.finish.enums.WorkFeeStatusEnum;
import com.zjft.usp.anyfix.work.finish.mapper.WorkFinishMapper;
import com.zjft.usp.anyfix.work.finish.model.WorkFinish;
import com.zjft.usp.anyfix.work.finish.service.WorkFinishService;
import com.zjft.usp.anyfix.work.operate.enums.WorkOperateTypeEnum;
import com.zjft.usp.anyfix.work.operate.model.WorkOperate;
import com.zjft.usp.anyfix.work.operate.service.WorkOperateService;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.anyfix.work.request.dto.WorkRequestDto;
import com.zjft.usp.anyfix.work.request.enums.ServiceModeEnum;
import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
import com.zjft.usp.anyfix.work.request.model.WorkRequest;
import com.zjft.usp.anyfix.work.request.service.WorkRequestService;
import com.zjft.usp.anyfix.work.ware.service.WorkWareRecycleService;
import com.zjft.usp.anyfix.work.ware.service.WorkWareUsedService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.file.service.FileFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 工单服务完成表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-10-14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkFinishServiceImpl extends ServiceImpl<WorkFinishMapper, WorkFinish> implements WorkFinishService {

    @Autowired
    private WorkRequestService workRequestService;
    @Autowired
    private WorkDealService workDealService;
    @Autowired
    private WorkWareUsedService workWareUsedService;
    @Autowired
    private WorkWareRecycleService workWareRecycleService;
    @Autowired
    private WorkOperateService workOperateService;
    @Autowired
    private WorkFeeService workFeeService;
    @Autowired
    private CustomFieldDataService customFieldDataService;
    @Autowired
    private WorkFeeDetailService workFeeDetailService;
    @Autowired
    private WorkCheckService workCheckService;
    @Autowired
    private WorkFeeImplementDefineService workFeeImplementDefineService;

    @Resource
    private FileFeignService fileFeignService;

    /**
     * 工程师现场服务工单
     *
     * @param workFinishDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/10/14 1:56 下午
     **/
    @Override
    public void localeServiceWork(WorkFinishDto workFinishDto, UserInfo userInfo, ReqParam reqParam) {
        WorkRequest workRequest = workRequestService.getById(workFinishDto.getWorkId());
        WorkDeal workDeal = workDealService.getById(workFinishDto.getWorkId());
        if (workDeal == null || workRequest == null) {
            throw new AppException("工单不存在！");
        }
        if (!workDeal.getWorkStatus().equals(WorkStatusEnum.IN_SERVICE.getCode())) {
            throw new AppException("工单状态不正确，当前状态为[" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "]！");
        }
        WorkDto workDto = new WorkDto();
        BeanUtils.copyProperties(workRequest, workDto);
        BeanUtils.copyProperties(workDeal, workDto);
        // workFinishDto里不包含workType，会覆盖，所以去除
        BeanUtils.copyProperties(workFinishDto, workDto, new String[] {"workType"});
        WorkFinish workFinish = new WorkFinish();
        BeanUtils.copyProperties(workFinishDto, workFinish);

        // 上传客户签名图片
        if(StrUtil.isNotEmpty(workFinishDto.getSignatureBase64())) {
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

        // 服务开始时间默认签到时间
//        workFinish.setStartTime(workDeal.getSignTime());
        workFinish.setStartTime(workFinishDto.getStartTime());
        workFinish.setEndTime(workFinishDto.getEndTime());
        workFinish.setOperator(userInfo.getUserId());
        workFinish.setOperateTime(DateUtil.date());
        this.save(workFinish);
        // 删除临时文件表数据
        if (!StringUtils.isEmpty(workFinishDto.getFiles())) {
            List<String> fileIds = Arrays.asList(workFinishDto.getFiles().split(","));
            List<Long> fileIdList = fileIds.stream().map(fileId -> Long.parseLong(fileId)).collect(Collectors.toList());
            this.fileFeignService.deleteFileTemporaryByFileIdList(fileIdList);
        }

        // 修改工单处理信息表
        workDeal = new WorkDeal();
        workDeal.setWorkId(workFinish.getWorkId());
        //设置服务开始和完成时间
        workDeal.setStartTime(workFinishDto.getStartTime());
        workDeal.setEndTime(workFinishDto.getEndTime());
        workDeal.setFinishTime(workFinish.getOperateTime());
        workDeal.setTogetherEngineers(workFinishDto.getTogetherEngineers());
        workDeal.setHelpNames(workFinishDto.getHelpNames());
        workDeal.setServiceMode(ServiceModeEnum.LOCALE_SERVICE.getCode());
        workDeal.setWorkStatus(WorkStatusEnum.CLOSED.getCode());
        workDeal.setFinishCheckStatus(ServiceCheckStatusEnum.UN_CHECK.getCode());
        if (WorkFeeStatusEnum.FILLED.getCode().equals(workFinishDto.getWorkFeeStatus())) {
            workDeal.setFeeCheckStatus(FeeCheckStatusEnum.UN_CHECK.getCode());
        }
        if (IntUtil.isNotZero(workFinishDto.getWorkFeeStatus())) {
            workDeal.setWorkFeeStatus(workFinishDto.getWorkFeeStatus());
        }
        workDealService.updateById(workDeal);

        // 添加部件
        workWareUsedService.addWorkUsedList(workFinishDto.getUsedWareList(),
                userInfo.getUserId(), workFinishDto.getWorkId());
        workWareRecycleService.addWorkRecycleList(workFinishDto.getRecycleWareList(),
                userInfo.getUserId(), workFinishDto.getWorkId());

        // 工单费用
        WorkFee workFee = this.workFeeService.getById(workFinishDto.getWorkId());
        if (workFee == null) {
            workFee = new WorkFee();
        }
        workFee.setWareUseFee(workFinishDto.getWorkFeeDto().getWareUseFee());
        workFee.setOtherFee(workFinishDto.getWorkFeeDto().getOtherFee());
        workFee.setOtherFeeNote(workFinishDto.getWorkFeeDto().getOtherFeeNote());
        workFee.setWorkId(workFinishDto.getWorkId());
        // 设置为现场服务
        workDto.setServiceMode(ServiceModeEnum.LOCALE_SERVICE.getCode());
        // 保存实施发生费用明细
        List<WorkFeeImplementDto> implementDtoList = this.workFeeImplementDefineService.listDtoByWork(workDto);
        workFee.setImplementFee(this.workFeeDetailService.addByImplementFeeList(implementDtoList,
                workFinishDto.getWorkId(), userInfo.getUserId()));
        // 保存分类费用明细
        workFee.setAssortFee(this.workFeeDetailService.addAssortFeeDetail(workDto, userInfo.getUserId()));
        // 获取总费用
        workFee.setTotalFee(this.workFeeService.getTotalFee(workFee));
        // 保存工单费用
        this.workFeeService.saveOrUpdate(workFee);

        // 添加工单审核表（状态是未审核）
        WorkCheck workCheck = new WorkCheck();
        workCheck.setWorkId(workDeal.getWorkId());
        workCheck.setFinishCheckStatus(ServiceCheckStatusEnum.UN_CHECK.getCode());
        if (WorkFeeStatusEnum.FILLED.getCode().equals(workDeal.getWorkFeeStatus())) {
            workDeal.setFeeCheckStatus(FeeCheckStatusEnum.UN_CHECK.getCode());
        }
        workCheckService.saveOrUpdate(workCheck);

        // 添加操作记录
        WorkOperate workOperate = new WorkOperate();
        workOperate.setWorkId(workDeal.getWorkId());
        workOperate.setWorkStatus(WorkStatusEnum.CLOSED.getCode());
        workOperate.setOperator(userInfo.getUserId());
        workOperate.setCorp(reqParam.getCorpId());
        workOperateService.addWorkOperateByLocaleService(workOperate);
    }

    /**
     * 工程师远程服务工单
     *
     * @param workFinishDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/10/14 1:56 下午
     **/
    @Override
    public void remoteServiceWork(WorkFinishDto workFinishDto, UserInfo userInfo, ReqParam reqParam) {
        WorkRequest workRequest = workRequestService.getById(workFinishDto.getWorkId());
        WorkDeal workDeal = workDealService.getById(workFinishDto.getWorkId());
        if (workDeal == null || workRequest == null) {
            throw new AppException("工单不存在！");
        }
        if (workDeal.getWorkStatus() != WorkStatusEnum.TO_SIGN.getCode()
                && workDeal.getWorkStatus() != WorkStatusEnum.TO_SERVICE.getCode()
                && workDeal.getWorkStatus() != WorkStatusEnum.TO_ASSIGN.getCode()) {
            throw new AppException("工单状态不正确，当前状态为[" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "]！");
        }
        if (StrUtil.isBlank(workFinishDto.getDescription())) {
            throw new AppException("服务情况不能为空！");
        }
        WorkDto workDto = new WorkDto();
        BeanUtils.copyProperties(workRequest, workDto);
        BeanUtils.copyProperties(workDeal, workDto);
        WorkFinish workFinish = new WorkFinish();
        BeanUtils.copyProperties(workFinishDto, workFinish);

        workFinish.setStartTime(DateUtil.date().toTimestamp());
        workFinish.setEndTime(DateUtil.date().toTimestamp());
        workFinish.setOperator(userInfo.getUserId());
        workFinish.setOperateTime(DateUtil.date().toTimestamp());
        this.save(workFinish);

        // 修改工单处理信息表
        workDeal = new WorkDeal();
        workDeal.setWorkId(workFinish.getWorkId());
        workDeal.setServiceMode(ServiceModeEnum.REMOTE_SERVICE.getCode());
        workDeal.setWorkStatus(WorkStatusEnum.CLOSED.getCode());
        workDeal.setFinishTime(workFinish.getEndTime());
        workDeal.setFinishCheckStatus(ServiceCheckStatusEnum.UN_CHECK.getCode());
        workDeal.setFeeCheckStatus(FeeCheckStatusEnum.UN_CHECK.getCode());
        workDeal.setWorkFeeStatus(WorkFeeStatusEnum.FILLED.getCode());
        workDealService.updateById(workDeal);

        // 更新工单费用
        this.updateRomoteWorkFee(workFinishDto, userInfo, workDto);

        // 添加自定义字段
        this.setCustomFieldDateList(workFinishDto);

        // 添加工单审核表（状态是未审核）
        WorkCheck workCheck = new WorkCheck();
        workCheck.setWorkId(workDeal.getWorkId());
        workCheck.setFinishCheckStatus(ServiceCheckStatusEnum.UN_CHECK.getCode());
        if (WorkFeeStatusEnum.FILLED.getCode().equals(workDeal.getWorkFeeStatus())) {
            workDeal.setFeeCheckStatus(FeeCheckStatusEnum.UN_CHECK.getCode());
        }
        workCheckService.saveOrUpdate(workCheck);

        // 添加操作记录
        WorkOperate workOperate = new WorkOperate();
        workOperate.setWorkId(workDeal.getWorkId());
        workOperate.setWorkStatus(WorkStatusEnum.CLOSED.getCode());
        workOperate.setOperator(userInfo.getUserId());
        workOperate.setCorp(reqParam.getCorpId());
        workOperateService.addWorkOperateByRemoteService(workOperate);
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
        WorkRequest workRequest = workRequestService.getById(workFinishDto.getWorkId());
        WorkDeal workDeal = workDealService.getById(workFinishDto.getWorkId());
        WorkFinish workFinishOld = this.getById(workFinishDto.getWorkId());
        String oldFiles = workFinishOld.getFiles();

        if (workDeal == null || workRequest == null) {
            throw new AppException("工单不存在！");
        }
        if (!workDeal.getWorkStatus().equals(WorkStatusEnum.CLOSED.getCode())) {
            throw new AppException("工单状态不正确，当前状态为[" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "]！");
        }
        WorkDto workDto = new WorkDto();
        BeanUtils.copyProperties(workRequest, workDto);
        BeanUtils.copyProperties(workDeal, workDto);
        // workFinishDto里不包含workType，会覆盖，所以去除
        BeanUtils.copyProperties(workFinishDto, workDto, new String[] {"workType"});
        WorkFinish workFinish = new WorkFinish();
        workFinish.setWorkId(workFinishDto.getWorkId());
        BeanUtils.copyProperties(workFinishDto, workFinish);
        workFinish.setStartTime(workFinishDto.getStartTime());
        workFinish.setOperator(userInfo.getUserId());
        workFinish.setOperateTime(DateUtil.date());

        // 上传客户签名图片
        if(StrUtil.isNotEmpty(workFinishDto.getSignatureBase64())) {
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
        this.updateById(workFinish);
        List<Long> fileIdList = null;
        // 删除临时文件表数据
        if (!StringUtils.isEmpty(workFinishDto.getFiles())) {
            List<String> fileIds = Arrays.asList(workFinishDto.getFiles().split(","));
            fileIdList = fileIds.stream().map(fileId -> Long.parseLong(fileId)).collect(Collectors.toList());
            this.fileFeignService.deleteFileTemporaryByFileIdList(fileIdList);
        }
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

        // 修改工单处理信息表
        workDeal = new WorkDeal();
        workDeal.setWorkId(workFinish.getWorkId());
        //设置服务开始和完成时间
        workDeal.setStartTime(workFinishDto.getStartTime());
        workDeal.setEndTime(workFinishDto.getEndTime());
        workDeal.setTogetherEngineers(workFinishDto.getTogetherEngineers());
        workDeal.setHelpNames(workFinishDto.getHelpNames());
        workDeal.setServiceMode(ServiceModeEnum.LOCALE_SERVICE.getCode());
//        workDeal.setFinishTime(workFinish.getEndTime());
        // 将服务商审核状态置为待审核
        workDeal.setFinishCheckStatus(ServiceCheckStatusEnum.UN_CHECK.getCode());
        if (WorkFeeStatusEnum.FILLED.getCode().equals(workFinishDto.getWorkFeeStatus())) {
            workDeal.setFeeCheckStatus(FeeCheckStatusEnum.UN_CHECK.getCode());
        }
        if (IntUtil.isNotZero(workFinishDto.getWorkFeeStatus())) {
            workDeal.setWorkFeeStatus(workFinishDto.getWorkFeeStatus());
        }
        workDealService.updateById(workDeal);

        // 先删除再添加部件
        workWareUsedService.deleteByWorkId(workFinishDto.getWorkId());
        workWareUsedService.addWorkUsedList(workFinishDto.getUsedWareList(),
                userInfo.getUserId(), workFinishDto.getWorkId());

        workWareRecycleService.deleteByWorkId(workFinishDto.getWorkId());
        workWareRecycleService.addWorkRecycleList(workFinishDto.getRecycleWareList(),
                userInfo.getUserId(), workFinishDto.getWorkId());

        // 先删除再添加换下备件邮寄单
//        workPostService.deleteByWorkId(workFinishDto.getWorkId());
//        if (CollectionUtil.isNotEmpty(workFinishDto.getWorkPostDtoList())) {
//            List<WorkPost> workPostList = workFinishDto.getWorkPostDtoList().stream().map(workPostDto -> {
//                WorkPost workPost = new WorkPost();
//                BeanUtils.copyProperties(workPostDto, workPost);
//                workPost.setPostId(KeyUtil.getId());
//                workPost.setWorkId(workFinishDto.getWorkId());
//                workPost.setOperator(userInfo.getUserId());
//                workPost.setOperateTime(DateUtil.date());
//                return workPost;
//            }).collect(Collectors.toList());
//            this.workPostService.saveBatch(workPostList);
//        }

        // 工单费用
        WorkFee workFee = this.workFeeService.getById(workFinishDto.getWorkId());
        if (workFee == null) {
            workFee = new WorkFee();
        }
        workFee.setWareUseFee(workFinishDto.getWorkFeeDto().getWareUseFee());
        workFee.setOtherFee(workFinishDto.getWorkFeeDto().getOtherFee());
        workFee.setOtherFeeNote(workFinishDto.getWorkFeeDto().getOtherFeeNote());
        workFee.setWorkId(workFinishDto.getWorkId());
        workDto.setServiceMode(ServiceModeEnum.LOCALE_SERVICE.getCode());
        // 保存实施发生费用明细
        workFee.setImplementFee(this.workFeeDetailService.addByImplementFeeList(workFinishDto.getWorkFeeImplementDtoList(),
                workFinishDto.getWorkId(), userInfo.getUserId()));
        // 保存分类费用明细
        workFee.setAssortFee(this.workFeeDetailService.addAssortFeeDetail(workDto, userInfo.getUserId()));
        // 获取总费用
        workFee.setTotalFee(this.workFeeService.getTotalFee(workFee));
        // 保存工单费用
        this.workFeeService.saveOrUpdate(workFee);

        // 添加工单审核表（状态是未审核）
        WorkCheck workCheck = new WorkCheck();
        workCheck.setWorkId(workDeal.getWorkId());
        workCheck.setFinishCheckStatus(ServiceCheckStatusEnum.UN_CHECK.getCode());
        if (WorkFeeStatusEnum.FILLED.getCode().equals(workDeal.getWorkFeeStatus())) {
            workCheck.setFeeCheckStatus(FeeCheckStatusEnum.UN_CHECK.getCode());
        }
        workCheckService.saveOrUpdate(workCheck);

        // 添加操作记录
        WorkOperate workOperate = new WorkOperate();
        workOperate.setWorkId(workDeal.getWorkId());
        workOperate.setWorkStatus(WorkStatusEnum.CLOSED.getCode());
        workOperate.setOperator(userInfo.getUserId());
        workOperate.setCorp(reqParam.getCorpId());
        workOperateService.addWorkOperateByUpdateLocaleFinish(workOperate);
    }


    /**
     * 添加自定义字段
     *
     * @param workFinishDto
     */
    @Override
    public void setCustomFieldDateList(WorkFinishDto workFinishDto) {
        if (CollectionUtil.isNotEmpty(workFinishDto.getCustomFieldDataList())) {
            for (CustomFieldData customFieldData : workFinishDto.getCustomFieldDataList()) {
                customFieldData.setFormId(workFinishDto.getWorkId());
            }
            customFieldDataService.addCustomFieldDataList(workFinishDto.getCustomFieldDataList());
        }
    }


    /**
     * 更新工单费用
     *
     * @param workFinishDto
     * @param userInfo
     * @param workDto
     */
    @Override
    public void updateRomoteWorkFee(WorkFinishDto workFinishDto, UserInfo userInfo, WorkDto workDto) {
        // 更新工单费用
        WorkFee workFee = this.workFeeService.getById(workFinishDto.getWorkId());
        if (workFee == null) {
            workFee = new WorkFee();
        }
        workFee.setWorkId(workFinishDto.getWorkId());
        workDto.setServiceMode(ServiceModeEnum.REMOTE_SERVICE.getCode());
        workFee.setAssortFee(this.workFeeDetailService.addAssortFeeDetail(workDto, userInfo.getUserId()));
        workFee.setTotalFee(this.workFeeService.getTotalFee(workFee));
        this.workFeeService.saveOrUpdate(workFee);
    }


    @Override
    public void supplementWork(WorkDto workDto, WorkFinishDto workFinishDto, WorkRequestDto workRequestDto, WorkDeal workDeal, UserInfo userInfo, ReqParam reqParam) {

        WorkFinish workFinish = new WorkFinish();
        BeanUtils.copyProperties(workFinishDto, workFinish);

        // 上传客户签名图片
        if (StrUtil.isNotBlank(workFinishDto.getSignatureBase64())) {
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

        // 服务开始时间默认签到时间
//        workFinish.setStartTime(workDeal.getSignTime());
        workFinish.setStartTime(workFinishDto.getStartTime());
        workFinish.setEndTime(workFinishDto.getEndTime());
        workFinish.setOperator(userInfo.getUserId());
        workFinish.setOperateTime(DateUtil.date());
        this.save(workFinish);
        // 删除临时文件表数据
        if (!StringUtils.isEmpty(workFinishDto.getFiles())) {
            List<String> fileIds = Arrays.asList(workFinishDto.getFiles().split(","));
            List<Long> fileIdList = fileIds.stream().map(fileId -> Long.parseLong(fileId)).collect(Collectors.toList());
            this.fileFeignService.deleteFileTemporaryByFileIdList(fileIdList);
        }

        // 添加部件
        workWareUsedService.addWorkUsedList(workFinishDto.getUsedWareList(),
                userInfo.getUserId(), workFinishDto.getWorkId());
        workWareRecycleService.addWorkRecycleList(workFinishDto.getRecycleWareList(),
                userInfo.getUserId(), workFinishDto.getWorkId());

        // 换下备件邮寄单
//        if (CollectionUtil.isNotEmpty(workFinishDto.getWorkPostDtoList())) {
//            List<WorkPost> workPostList = workFinishDto.getWorkPostDtoList().stream().map(workPostDto -> {
//                WorkPost workPost = new WorkPost();
//                BeanUtils.copyProperties(workPostDto, workPost);
//                workPost.setPostId(KeyUtil.getId());
//                workPost.setWorkId(workFinishDto.getWorkId());
//                workPost.setOperator(userInfo.getUserId());
//                workPost.setOperateTime(DateUtil.date());
//                return workPost;
//            }).collect(Collectors.toList());
//            this.workPostService.saveBatch(workPostList);
//        }

        // 工单费用
        WorkFee workFee = this.workFeeService.getById(workFinishDto.getWorkId());
        if (workFee == null) {
            workFee = new WorkFee();
        }
        BeanUtils.copyProperties(workFinishDto.getWorkFeeDto(), workFee);
        workFee.setWorkId(workFinishDto.getWorkId());
        workDto.setServiceMode(ServiceModeEnum.LOCALE_SERVICE.getCode());
        // 保存实施发生费用明细
        workFee.setImplementFee(this.workFeeDetailService.addByImplementFeeList(workFinishDto.getWorkFeeImplementDtoList(),
                workFinishDto.getWorkId(), userInfo.getUserId()));
        // 保存分类费用明细
        workFee.setAssortFee(this.workFeeDetailService.addAssortFeeDetail(workDto, userInfo.getUserId()));
        // 获取总费用
        workFee.setTotalFee(this.workFeeService.getTotalFee(workFee));
        // 保存工单费用
        this.workFeeService.saveOrUpdate(workFee);

        // 添加工单审核表（状态是未审核）
        WorkCheck workCheck = new WorkCheck();
        workCheck.setWorkId(workDeal.getWorkId());
        workCheckService.saveOrUpdate(workCheck);

        // 添加操作记录
        WorkOperate workOperate = new WorkOperate();
        workOperate.setWorkId(workDeal.getWorkId());
        workOperate.setWorkStatus(WorkStatusEnum.CLOSED.getCode());
        workOperate.setOperator(userInfo.getUserId());
        workOperate.setCorp(reqParam.getCorpId());
        workOperateService.addWorkOperateByLocaleService(workOperate);
        //this.addWorkOperateBySupplementWork(workRequestDto,workDeal.getServiceMode(), userInfo, reqParam);

    }


    /**
     * 补提工单添加操作记录
     *
     * @param workRequestDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/2 09:22
     */
    @Override
    public void addWorkOperateBySupplementWork(WorkRequestDto workRequestDto, Integer serviceMode, UserInfo userInfo, ReqParam reqParam) {
        if (workRequestDto != null) {
            WorkOperate workOperate = new WorkOperate();
            workOperate.setWorkId(workRequestDto.getWorkId());
            workOperate.setOperator(userInfo.getUserId());
            workOperate.setCorp(reqParam.getCorpId());
            if (serviceMode == ServiceModeEnum.LOCALE_SERVICE.getCode()) {
                workOperate.setOperateType(WorkOperateTypeEnum.LOCATE_SERVICE.getCode());
            } else if (serviceMode == ServiceModeEnum.REMOTE_SERVICE.getCode()) {
                workOperate.setOperateType(WorkOperateTypeEnum.REMOTE_SERVICE.getCode());
            }
            workOperate.setWorkStatus(WorkStatusEnum.CLOSED.getCode());
            workOperateService.addWorkOperateBySupplementWork(workRequestDto, workOperate);
        }
    }

    @Override
    public Map<Long,WorkFinish> mapWorkIdAndWorkFinish(List<Long> workIdList) {
        if(CollectionUtil.isEmpty(workIdList)) {
            return null;
        }
        Map<Long,WorkFinish> longWorkFinishMap =  new HashMap<>();
        List<WorkFinish> workFinishList = this.list(new QueryWrapper<WorkFinish>()
                .in("work_id", workIdList));
        for (WorkFinish workFinish : workFinishList) {
            longWorkFinishMap.put(workFinish.getWorkId(),workFinish);
        }
        return longWorkFinishMap;
    }

}
