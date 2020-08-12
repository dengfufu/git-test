package com.zjft.usp.anyfix.work.deal.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.work.check.dto.WorkCheckDto;
import com.zjft.usp.anyfix.work.deal.dto.EngineerDto;
import com.zjft.usp.anyfix.work.deal.mapper.WorkDealMapper;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.operate.model.WorkOperate;
import com.zjft.usp.anyfix.work.operate.service.WorkOperateService;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.anyfix.work.request.dto.WorkRequestDto;
import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * <p>
 * 工单处理信息表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkDealServiceImpl extends ServiceImpl<WorkDealMapper, WorkDeal> implements WorkDealService {

    @Autowired
    private WorkOperateService workOperateService;

    @Resource
    private UasFeignService uasFeignService;
    @Resource
    private WorkDealMapper workDealMapper;

    /**
     * 建单时添加处理信息
     *
     * @param workRequestDto
     * @return
     * @author zgpi
     * @date 2019/10/15 9:48 上午
     **/
    @Override
    public void addWorkDeal(WorkRequestDto workRequestDto) {
        WorkDeal workDeal = new WorkDeal();
        workDeal.setWorkId(workRequestDto.getWorkId());
        workDeal.setWorkCode(workRequestDto.getWorkCode());
        workDeal.setDeviceId(workRequestDto.getDeviceId());
        workDeal.setDemanderCorp(workRequestDto.getDemanderCorp());
        workDeal.setWorkStatus(WorkStatusEnum.TO_DISPATCH.getCode());
//        if (LongUtil.isNotZero(workRequestDto.getServiceCorp())) {
//            workDeal.setWorkStatus(WorkStatusEnum.TO_HANDLE.getCode());
//        } else {
//        }
        workDeal.setBookTimeBegin(workRequestDto.getBookTimeBegin());
        workDeal.setBookTimeEnd(workRequestDto.getBookTimeEnd());
        workDeal.setCreator(workRequestDto.getCreator());
        workDeal.setCreateTime(DateUtil.date().toTimestamp());
        this.save(workDeal);
    }

    /**
     * 修改工单处理信息
     *
     * @param workRequestDto
     * @return
     * @author zgpi
     * @date 2020/2/26 19:53
     */
    @Override
    public void modWorkDealByEdit(WorkRequestDto workRequestDto) {
        this.update(Wrappers.<WorkDeal>lambdaUpdate()
                .set(WorkDeal::getDeviceId, workRequestDto.getDeviceId())
                .set(WorkDeal::getWorkStatus, workRequestDto.getWorkStatus())
                .set(WorkDeal::getServiceCorp, 0L).set(WorkDeal::getServiceBranch, 0L)
                .set(WorkDeal::getDispatchStaff, 0L).set(WorkDeal::getDispatchTime, null)
                .set(WorkDeal::getHandleStaff, 0L).set(WorkDeal::getHandleTime, null)
                .set(WorkDeal::getServiceMode, 0).set(WorkDeal::getAssignMode, 0)
                .set(WorkDeal::getAssignStaff, 0L).set(WorkDeal::getAssignTime, null)
                .set(WorkDeal::getEngineer, 0L).set(WorkDeal::getTogetherEngineers, "")
                .set(WorkDeal::getHelpNames, "").set(WorkDeal::getAcceptTime, null)
                .set(WorkDeal::getBookTimeBegin, workRequestDto.getBookTimeBegin())
                .set(WorkDeal::getBookTimeEnd, workRequestDto.getBookTimeEnd())
                .set(WorkDeal::getSignTime, null).set(WorkDeal::getStartTime, null)
                .set(WorkDeal::getFinishTime, null).set(WorkDeal::getEvaluateTime, null)
                .set(WorkDeal::getFinishCheckStatus, 1).set(WorkDeal::getFinishCheckTime, null)
                .set(WorkDeal::getFeeCheckStatus, 1).set(WorkDeal::getFeeCheckTime, null)
                .set(WorkDeal::getFinishConfirmStatus, 1).set(WorkDeal::getFinishConfirmTime, null)
                .set(WorkDeal::getFeeConfirmStatus, 1).set(WorkDeal::getFeeConfirmTime, null)
                .set(WorkDeal::getReviewStaff, 0L).set(WorkDeal::getReviewTime, null)
                .set(WorkDeal::getTraffic, 0).set(WorkDeal::getTrafficNote, "")
                .set(WorkDeal::getCreator, workRequestDto.getCreator())
                .set(WorkDeal::getCreateTime, DateUtil.date())
                .eq(WorkDeal::getWorkId, workRequestDto.getWorkId()));
    }

    @Override
    public List<WorkDeal> queryWorkDeal(Set<Long> workIdList) {
        return workDealMapper.selectBatchIds(workIdList);
    }

    @Override
    public List<EngineerDto> queryEngineerDto(WorkFilter workFilter) {
        List<EngineerDto> engineerDtoList = this.baseMapper.queryEngineerDto(workFilter);
        if (engineerDtoList != null && engineerDtoList.size() > 0) {
            List<EngineerDto> engineerDtoTempList = new ArrayList<>();
            for (EngineerDto engineerDto : engineerDtoList) {
                if (engineerDto.getEngineerId() != 0) {
                    engineerDtoTempList.add(engineerDto);
                }
            }
            List<Long> userIdList = new ArrayList<>();
            for (EngineerDto engineerDto : engineerDtoTempList) {
                userIdList.add(engineerDto.getEngineerId());
            }
            Map<String, Map<String, String>> userNameMap = uasFeignService.mapCorpUserInfoByUserIdList(userIdList, workFilter.getServiceCorp()).getData();
            for (EngineerDto engineerDto : engineerDtoTempList) {
                Map<String, String> userInfoMap = userNameMap.get(engineerDto.getEngineerId() + "");
                if (userInfoMap != null) {
                    engineerDto.setEngineerName(userInfoMap.get("userName"));
                    engineerDto.setFaceImg(userInfoMap.get("faceImg"));
                }
            }
            return engineerDtoTempList;
        }
        return null;
    }

    /**
     * 修改协同工程师
     *
     * @param workDto
     * @param curUserId
     */
    @Override
    public void updateTogetherEngineers(WorkDto workDto, Long curUserId) {
        if (workDto == null || LongUtil.isZero(workDto.getWorkId())) {
            throw new AppException("工单编号不能为空");
        }
        WorkDeal workDeal = this.getById(workDto.getWorkId());
        if (workDeal == null) {
            throw new AppException("工单不存在，请检查");
        }
        if (!curUserId.equals(workDeal.getEngineer())) {
            throw new AppException("您不是该工单的负责工程师，不能修改");
        }
        if (workDeal.getWorkStatus() != WorkStatusEnum.TO_SIGN.getCode() &&
                workDeal.getWorkStatus() != WorkStatusEnum.IN_SERVICE.getCode()) {
            throw new AppException("工单状态为【" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "】，不能修改");
        }
        WorkDeal workDealNew = new WorkDeal();
        workDealNew.setWorkId(workDto.getWorkId());
        if (CollectionUtil.isNotEmpty(workDto.getTogetherEngineerList())) {
            String togetherEngineers = workDto.getTogetherEngineerList().stream()
                    .map(togetherEngineer -> String.valueOf(togetherEngineer.getEngineerId())).collect(Collectors.joining(","));
            workDealNew.setTogetherEngineers(togetherEngineers);
        } else {
            workDealNew.setTogetherEngineers("");
        }
        this.updateById(workDealNew);
    }

    /**
     * 审核服务
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/5/29 11:16
     **/
    @Override
    public void checkService(WorkCheckDto workCheckDto, Long curUserId, Long curCorpId) {
        UpdateWrapper<WorkDeal> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("work_id", workCheckDto.getWorkId());
        updateWrapper.set("finish_check_status", workCheckDto.getFinishCheckStatus());
        updateWrapper.set("finish_check_time", workCheckDto.getFinishCheckTime());
        updateWrapper.set("finish_confirm_status", workCheckDto.getFinishConfirmStatus());
        updateWrapper.set("finish_confirm_time", workCheckDto.getFinishConfirmTime());
        this.update(updateWrapper);

        // 添加操作记录
        WorkOperate workOperate = new WorkOperate();
        workOperate.setWorkId(workCheckDto.getWorkId());
        // 不会变更工单状态
        workOperate.setWorkStatus(workCheckDto.getWorkStatus());
        workOperate.setOperator(curUserId);
        workOperate.setCorp(curCorpId);
        workOperateService.addServiceCheckOperate(workOperate, workCheckDto);
    }

    /**
     * 审核费用
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/5/29 11:16
     **/
    @Override
    public void checkFee(WorkCheckDto workCheckDto, Long curUserId, Long curCorpId) {
        UpdateWrapper<WorkDeal> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("work_id", workCheckDto.getWorkId());
        updateWrapper.set("fee_check_status", workCheckDto.getFeeCheckStatus());
        updateWrapper.set("fee_check_time", workCheckDto.getFeeCheckTime());
        updateWrapper.set("fee_confirm_status", workCheckDto.getFeeConfirmStatus());
        updateWrapper.set("fee_confirm_time", workCheckDto.getFeeConfirmTime());
        this.update(updateWrapper);

        // 添加操作记录
        WorkOperate workOperate = new WorkOperate();
        workOperate.setWorkId(workCheckDto.getWorkId());
        // 不会变更工单状态
        workOperate.setWorkStatus(workCheckDto.getWorkStatus());
        workOperate.setOperator(curUserId);
        workOperate.setCorp(curCorpId);
        workOperateService.addFeeCheckOperate(workOperate, workCheckDto);
    }

    /**
     * 确认服务
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/5/29 13:42
     **/
    @Override
    public void confirmService(WorkCheckDto workCheckDto, Long curUserId, Long curCorpId) {
        WorkDeal workDeal = new WorkDeal();
        workDeal.setWorkId(workCheckDto.getWorkId());
        workDeal.setFinishConfirmStatus(workCheckDto.getFinishConfirmStatus());
        workDeal.setFinishConfirmTime(workCheckDto.getFinishConfirmTime());
        this.updateById(workDeal);

        // 添加操作记录
        WorkOperate workOperate = new WorkOperate();
        workOperate.setWorkId(workDeal.getWorkId());
        workOperate.setWorkStatus(workDeal.getWorkStatus());
        workOperate.setOperator(curUserId);
        workOperate.setCorp(curCorpId);
        workOperateService.addServiceConfirmOperate(workOperate, workCheckDto);
    }

    /**
     * 确认费用
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/5/29 13:42
     **/
    @Override
    public void confirmFee(WorkCheckDto workCheckDto, Long curUserId, Long curCorpId) {
        WorkDeal workDeal = new WorkDeal();
        workDeal.setWorkId(workCheckDto.getWorkId());
        workDeal.setFeeConfirmStatus(workCheckDto.getFeeConfirmStatus());
        workDeal.setFeeConfirmTime(workCheckDto.getFeeConfirmTime());
        this.updateById(workDeal);

        // 添加操作记录
        WorkOperate workOperate = new WorkOperate();
        workOperate.setWorkId(workDeal.getWorkId());
        workOperate.setWorkStatus(workDeal.getWorkStatus());
        workOperate.setOperator(curUserId);
        workOperate.setCorp(curCorpId);
        workOperateService.addFeeConfirmOperate(workOperate, workCheckDto);
    }

    /**
     * 批量审核服务
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/6/1 10:23
     **/
    @Override
    public void batchCheckService(WorkCheckDto workCheckDto, Long curUserId, Long curCorpId) {
        if (CollectionUtil.isEmpty(workCheckDto.getWorkIdList())) {
            throw new AppException("工单列表不能为空");
        }
        List<WorkDeal> workDealList = new ArrayList<>();
        List<WorkOperate> workOperateList = new ArrayList<>();
        WorkDeal workDeal;
        for (Long workId : workCheckDto.getWorkIdList()) {
            workDeal = new WorkDeal();
            workDeal.setWorkId(workId);
            workDeal.setFinishCheckStatus(workCheckDto.getFinishCheckStatus());
            workDeal.setFinishCheckTime(workCheckDto.getFinishCheckTime());
            workDeal.setFinishConfirmStatus(workCheckDto.getFinishConfirmStatus());
            workDealList.add(workDeal);

            WorkOperate workOperate = new WorkOperate();
            workOperate.setWorkId(workDeal.getWorkId());
            workOperate.setWorkStatus(workDeal.getWorkStatus());
            workOperate.setOperator(curUserId);
            workOperate.setCorp(curCorpId);
            workOperateList.add(workOperate);
        }
        this.updateBatchById(workDealList);
        // 添加操作记录
        this.workOperateService.addBatchCheckServiceOperate(workOperateList, workCheckDto);
    }

    /**
     * 批量审核费用
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/6/1 10:23
     **/
    @Override
    public void batchCheckFee(WorkCheckDto workCheckDto, Long curUserId, Long curCorpId) {
        if (CollectionUtil.isEmpty(workCheckDto.getWorkIdList())) {
            throw new AppException("工单列表不能为空");
        }
        List<WorkDeal> workDealList = new ArrayList<>();
        List<WorkOperate> workOperateList = new ArrayList<>();
        WorkDeal workDeal;
        for (Long workId : workCheckDto.getWorkIdList()) {
            workDeal = new WorkDeal();
            workDeal.setWorkId(workId);
            workDeal.setFeeCheckStatus(workCheckDto.getFeeCheckStatus());
            workDeal.setFeeCheckTime(workCheckDto.getFeeCheckTime());
            workDeal.setFeeConfirmStatus(workCheckDto.getFeeConfirmStatus());
            workDealList.add(workDeal);

            WorkOperate workOperate = new WorkOperate();
            workOperate.setWorkId(workDeal.getWorkId());
            workOperate.setWorkStatus(workDeal.getWorkStatus());
            workOperate.setOperator(curUserId);
            workOperate.setCorp(curCorpId);
            workOperateList.add(workOperate);
        }
        this.updateBatchById(workDealList);
        // 添加操作记录
        this.workOperateService.addBatchCheckFeeOperate(workOperateList, workCheckDto);
    }

    /**
     * 批量确认服务
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/6/1 15:48
     **/
    @Override
    public void batchConfirmService(WorkCheckDto workCheckDto, Long curUserId, Long curCorpId) {
        if (CollectionUtil.isEmpty(workCheckDto.getWorkIdList())) {
            throw new AppException("工单列表不能为空");
        }
        List<WorkDeal> workDealList = new ArrayList<>();
        List<WorkOperate> workOperateList = new ArrayList<>();
        for (Long workId : workCheckDto.getWorkIdList()) {
            WorkDeal workDeal = new WorkDeal();
            workDeal.setWorkId(workId);
            workDeal.setFinishConfirmStatus(workCheckDto.getFinishConfirmStatus());
            workDeal.setFinishConfirmTime(workCheckDto.getFinishConfirmTime());
            workDealList.add(workDeal);

            WorkOperate workOperate = new WorkOperate();
            workOperate.setWorkId(workDeal.getWorkId());
            workOperate.setWorkStatus(workDeal.getWorkStatus());
            workOperate.setOperator(curUserId);
            workOperate.setCorp(curCorpId);
            workOperateList.add(workOperate);
        }
        this.updateBatchById(workDealList);
        // 添加操作记录
        this.workOperateService.addBatchConfirmServiceOperate(workOperateList, workCheckDto);
    }

    /**
     * 批量确认费用
     *
     * @param workCheckDto
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/6/1 15:48
     **/
    @Override
    public void batchConfirmFee(WorkCheckDto workCheckDto, Long curUserId, Long curCorpId) {
        if (CollectionUtil.isEmpty(workCheckDto.getWorkIdList())) {
            throw new AppException("工单列表不能为空");
        }
        List<WorkDeal> workDealList = new ArrayList<>();
        List<WorkOperate> workOperateList = new ArrayList<>();
        for (Long workId : workCheckDto.getWorkIdList()) {
            WorkDeal workDeal = new WorkDeal();
            workDeal.setWorkId(workId);
            workDeal.setFeeConfirmStatus(workCheckDto.getFeeConfirmStatus());
            workDeal.setFeeConfirmTime(workCheckDto.getFeeConfirmTime());
            workDealList.add(workDeal);

            WorkOperate workOperate = new WorkOperate();
            workOperate.setWorkId(workDeal.getWorkId());
            workOperate.setWorkStatus(workDeal.getWorkStatus());
            workOperate.setOperator(curUserId);
            workOperate.setCorp(curCorpId);
            workOperateList.add(workOperate);
        }
        this.updateBatchById(workDealList);
        // 添加操作记录
        this.workOperateService.addBatchConfirmFeeOperate(workOperateList, workCheckDto);
    }

    /**
     * 查询是否存在相同类型的未完成工单
     * @param workRequestDto
     * @return
     */
    @Override
    public List<WorkDeal> listSameWork(WorkRequestDto workRequestDto) {
        return this.baseMapper.listSameWork(workRequestDto);
    }

    /**
     * 自动确认服务
     *
     * @param workIdList
     * @param workCheckDto
     */
    @Override
    public void autoConfirmService(List<Long> workIdList, WorkCheckDto workCheckDto) {
        if (CollectionUtil.isEmpty(workIdList)) {
            return;
        }
        List<WorkDeal> workDealList = new ArrayList<>();
        List<WorkOperate> workOperateList = new ArrayList<>();
        for (Long workId : workIdList) {
            WorkDeal workDeal = new WorkDeal();
            workDeal.setWorkId(workId);
            workDeal.setFinishConfirmStatus(workCheckDto.getFinishConfirmStatus());
            workDeal.setFinishConfirmTime(workCheckDto.getFinishConfirmTime());
            workDealList.add(workDeal);

            WorkOperate workOperate = new WorkOperate();
            workOperate.setWorkId(workDeal.getWorkId());
            workOperate.setWorkStatus(workDeal.getWorkStatus());
            workOperateList.add(workOperate);
        }
        this.updateBatchById(workDealList);
        // 添加操作记录
        this.workOperateService.addAutoConfirmServiceOperate(workOperateList);
    }

    /**
     * 自动确认费用
     *
     * @param workIdList
     * @param workCheckDto
     */
    @Override
    public void autoConfirmFee(List<Long> workIdList, WorkCheckDto workCheckDto) {
        if (CollectionUtil.isEmpty(workIdList)) {
            return;
        }
        List<WorkDeal> workDealList = new ArrayList<>();
        List<WorkOperate> workOperateList = new ArrayList<>();
        for (Long workId : workIdList) {
            WorkDeal workDeal = new WorkDeal();
            workDeal.setWorkId(workId);
            workDeal.setFeeConfirmStatus(workCheckDto.getFeeConfirmStatus());
            workDeal.setFeeConfirmTime(workCheckDto.getFeeConfirmTime());
            workDealList.add(workDeal);

            WorkOperate workOperate = new WorkOperate();
            workOperate.setWorkId(workDeal.getWorkId());
            workOperate.setWorkStatus(workDeal.getWorkStatus());
            workOperateList.add(workOperate);
        }
        this.updateBatchById(workDealList);
        // 添加操作记录
        this.workOperateService.addAutoConfirmFeeOperate(workOperateList);
    }

}
