package com.zjft.usp.anyfix.work.booking.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.anyfix.baseinfo.enums.EnabledEnum;
import com.zjft.usp.anyfix.work.booking.dto.WorkBookingDto;
import com.zjft.usp.anyfix.work.request.enums.ServiceModeEnum;
import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
import com.zjft.usp.anyfix.work.booking.model.WorkBooking;
import com.zjft.usp.anyfix.work.booking.mapper.WorkBookingMapper;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.operate.model.WorkOperate;
import com.zjft.usp.anyfix.work.booking.service.WorkBookingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.operate.service.WorkOperateService;
import com.zjft.usp.anyfix.work.request.model.WorkRequest;
import com.zjft.usp.anyfix.work.request.service.WorkRequestService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * <p>
 * 工单预约表 服务实现类
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkBookingServiceImpl extends ServiceImpl<WorkBookingMapper, WorkBooking> implements WorkBookingService {

    @Autowired
    private WorkRequestService workRequestService;
    @Autowired
    private WorkDealService workDealService;
    @Autowired
    private WorkOperateService workOperateService;

    /**
     * 工程师修改预约时间
     *
     * @param workBookingDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/11/5 09:04
     **/
    @Override
    public void changeBookingTime(WorkBookingDto workBookingDto, UserInfo userInfo, ReqParam reqParam) {
        if (LongUtil.isZero(workBookingDto.getWorkId())) {
            throw new AppException("工单编号不能为空！");
        }
        WorkRequest workRequest = workRequestService.getById(workBookingDto.getWorkId());
        WorkDeal workDeal = workDealService.getById(workBookingDto.getWorkId());
        if (workRequest == null || workDeal == null) {
            throw new AppException("工单不存在！");
        }
        if (workDeal.getWorkStatus() != WorkStatusEnum.IN_SERVICE.getCode() &&
                workDeal.getWorkStatus() != WorkStatusEnum.TO_SIGN.getCode() &&
                workDeal.getWorkStatus() != WorkStatusEnum.TO_CLAIM.getCode() &&
                workDeal.getWorkStatus() != WorkStatusEnum.TO_ASSIGN.getCode() &&
                workDeal.getWorkStatus() != WorkStatusEnum.TO_HANDLE.getCode()) {
            throw new AppException("工单状态不正确，当前状态为[" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "]！");
        }
        if (workBookingDto.getBookTimeEnd() != null) {
            workBookingDto.setBookTimeBegin(workBookingDto.getBookTimeEnd());
            if (workRequest.getFaultTime() != null
                    && workBookingDto.getBookTimeEnd().before(workRequest.getFaultTime())) {
                throw new AppException("预约时间不能早于故障时间！");
            }
            workDeal = new WorkDeal();
            workDeal.setWorkId(workBookingDto.getWorkId());
            workDeal.setBookTimeBegin(workBookingDto.getBookTimeBegin());
            workDeal.setBookTimeEnd(workBookingDto.getBookTimeEnd());
            workDealService.updateById(workDeal);

            WorkRequest workRequestUpdate = new WorkRequest();
            workRequestUpdate.setWorkId(workBookingDto.getWorkId());
            workRequestUpdate.setBookTimeBegin(workBookingDto.getBookTimeBegin());
            workRequestUpdate.setBookTimeEnd(workBookingDto.getBookTimeEnd());
            workRequestService.updateById(workRequestUpdate);
        }

        // 将之前的预约改为无效
        List<WorkBooking> workBookingList = this.list(new QueryWrapper<WorkBooking>()
                .eq("work_id", workBookingDto.getWorkId()).eq("enabled", EnabledEnum.YES.getCode()));
        if (CollectionUtil.isNotEmpty(workBookingList)) {
            for (WorkBooking workBooking : workBookingList) {
                workBooking.setEnabled(EnabledEnum.NO.getCode());
                workBooking.setOperator(userInfo.getUserId());
                workBooking.setOperateTime(DateUtil.date().toTimestamp());
                this.updateById(workBooking);
            }
        }
        if (workBookingDto.getBookTimeEnd() != null) {
            // 新增预约
            WorkBooking workBooking = new WorkBooking();
            BeanUtils.copyProperties(workBookingDto, workBooking);
            workBooking.setBookingId(KeyUtil.getId());
            workBooking.setEnabled(EnabledEnum.YES.getCode());
            workBooking.setLon(reqParam.getLon());
            workBooking.setLat(reqParam.getLat());
            workBooking.setOperator(userInfo.getUserId());
            workBooking.setOperateTime(DateUtil.date().toTimestamp());
            this.save(workBooking);

            // 添加操作记录
            WorkOperate workOperate = new WorkOperate();
            workOperate.setWorkId(workDeal.getWorkId());
            workOperate.setWorkStatus(WorkStatusEnum.TO_SIGN.getCode());
            workOperate.setOperator(userInfo.getUserId());
            workOperate.setCorp(reqParam.getCorpId());
            workOperateService.addWorkOperateByChangeBookingTime(workOperate);
        }
    }

    /**
     * 修改服务模式为远程处理
     * 修改工单状态为待服务
     *
     * @param workId
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/10/14 3:57 下午
     **/
    @Override
    public void modServiceModeToRemote(Long workId, UserInfo userInfo, ReqParam reqParam) {
        WorkDeal workDeal = workDealService.getById(workId);
        if (workDeal == null) {
            throw new AppException("工单不存在！");
        }
        if (workDeal.getWorkStatus() != WorkStatusEnum.TO_SIGN.getCode()) {
            throw new AppException("工单状态不正确，当前状态为[" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "]！");
        }
        workDeal = new WorkDeal();
        workDeal.setWorkId(workId);
        workDeal.setBookTimeBegin(null);
        workDeal.setBookTimeEnd(null);
        workDeal.setServiceMode(ServiceModeEnum.REMOTE_SERVICE.getCode());
        workDeal.setWorkStatus(WorkStatusEnum.TO_SERVICE.getCode());
        workDealService.updateById(workDeal);

        // 将之前的预约改为无效
        List<WorkBooking> workBookingList = this.list(new QueryWrapper<WorkBooking>()
                .eq("work_id", workId).eq("enabled", EnabledEnum.YES.getCode()));
        if (workBookingList != null && !workBookingList.isEmpty()) {
            for (WorkBooking workBooking : workBookingList) {
                workBooking.setEnabled(EnabledEnum.NO.getCode());
                workBooking.setOperator(userInfo.getUserId());
                workBooking.setOperateTime(DateUtil.date().toTimestamp());
                this.updateById(workBooking);
            }
        }

        WorkBooking workBooking = new WorkBooking();
        workBooking.setWorkId(workId);
        workBooking.setOperator(userInfo.getUserId());
    }

    /**
     * 修改服务模式为现场处理
     * 修改工单状态为待签到
     *
     * @param workId
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/10/14 4:21 下午
     **/
    @Override
    public void modServiceModeToLocale(Long workId, UserInfo userInfo, ReqParam reqParam) {
        WorkDeal workDeal = workDealService.getById(workId);
        if (workDeal == null) {
            throw new AppException("工单不存在！");
        }
        if (workDeal.getWorkStatus() != WorkStatusEnum.TO_SERVICE.getCode()) {
            throw new AppException("工单状态不正确，当前状态为[" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "]！");
        }
        workDeal = new WorkDeal();
        workDeal.setWorkId(workId);
        workDeal.setBookTimeBegin(null);
        workDeal.setBookTimeEnd(null);
        workDeal.setServiceMode(ServiceModeEnum.LOCALE_SERVICE.getCode());
        workDeal.setWorkStatus(WorkStatusEnum.TO_SIGN.getCode());
        workDealService.updateById(workDeal);

        // 将之前的预约改为无效
        List<WorkBooking> workBookingList = this.list(new QueryWrapper<WorkBooking>()
                .eq("work_id", workId).eq("enabled", EnabledEnum.YES.getCode()));
        if (workBookingList != null && !workBookingList.isEmpty()) {
            for (WorkBooking workBooking : workBookingList) {
                workBooking.setEnabled(EnabledEnum.NO.getCode());
                workBooking.setOperator(userInfo.getUserId());
                workBooking.setOperateTime(DateUtil.date().toTimestamp());
                this.updateById(workBooking);
            }
        }

        // 新增预约
        WorkBooking workBooking = new WorkBooking();
        workBooking.setBookingId(KeyUtil.getId());
        workBooking.setWorkId(workId);
        workBooking.setEnabled(EnabledEnum.YES.getCode());
        workBooking.setLon(reqParam.getLon());
        workBooking.setLat(reqParam.getLat());
        workBooking.setOperator(userInfo.getUserId());
        workBooking.setOperateTime(DateUtil.date().toTimestamp());
        this.save(workBooking);
    }

}
