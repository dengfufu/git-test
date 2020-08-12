package com.zjft.usp.anyfix.work.sign.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.enums.EnabledEnum;
import com.zjft.usp.anyfix.corp.branch.model.DeviceBranch;
import com.zjft.usp.anyfix.corp.branch.service.DeviceBranchService;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.listener.WorkMqTopic;
import com.zjft.usp.anyfix.work.operate.model.WorkOperate;
import com.zjft.usp.anyfix.work.operate.service.WorkOperateService;
import com.zjft.usp.anyfix.work.remind.enums.WorkRemindTypeEnum;
import com.zjft.usp.anyfix.work.remind.service.WorkRemindDealService;
import com.zjft.usp.anyfix.work.request.enums.ServiceModeEnum;
import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
import com.zjft.usp.anyfix.work.request.model.WorkRequest;
import com.zjft.usp.anyfix.work.request.service.WorkRequestService;
import com.zjft.usp.anyfix.work.sign.dto.WorkSignDto;
import com.zjft.usp.anyfix.work.sign.filter.WorkSignFilter;
import com.zjft.usp.anyfix.work.sign.mapper.WorkSignMapper;
import com.zjft.usp.anyfix.work.sign.model.WorkSign;
import com.zjft.usp.anyfix.work.sign.service.WorkSignService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.CoordinateUtil;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 工单签到表 服务实现类
 * </p>
 *
 * @author canlei
 * @since 2019-09-23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkSignServiceImpl extends ServiceImpl<WorkSignMapper, WorkSign> implements WorkSignService {

    @Autowired
    private DeviceBranchService deviceBranchService;
    @Autowired
    private WorkRequestService workRequestService;
    @Autowired
    private WorkDealService workDealService;
    @Autowired
    private WorkOperateService workOperateService;
    @Autowired
    private WorkRemindDealService workRemindDealService;

    /**
     * 工程师签到
     *
     * @param workSignDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/10/14 9:47 上午
     **/
    @Override
    public Long signWork(WorkSignDto workSignDto, UserInfo userInfo, ReqParam reqParam) {
        if (LongUtil.isZero(workSignDto.getWorkId())) {
            throw new AppException("工单编号不能为空！");
        }
        WorkSign workSign = new WorkSign();
        BeanUtils.copyProperties(workSignDto, workSign);
        WorkRequest workRequest = workRequestService.getById(workSign.getWorkId());
        WorkDeal workDeal = workDealService.getById(workSignDto.getWorkId());
        if (workRequest == null || workDeal == null) {
            throw new AppException("工单不存在！");
        }
        // 协同工程师
        List<Long> togetherEngineerIds = new ArrayList<>();
        if (!StringUtils.isEmpty(workDeal.getTogetherEngineers())) {
            List<String> togetherEngineerIdStrings = Arrays.asList(workDeal.getTogetherEngineers().split(","));
            togetherEngineerIds = togetherEngineerIdStrings.stream().map(engineerId -> Long.parseLong(engineerId)).collect(Collectors.toList());
        }
        if (!workDeal.getEngineer().equals(userInfo.getUserId()) && !togetherEngineerIds.contains(userInfo.getUserId())) {
            throw new AppException("该工单的服务工程师不是您，不能签到！");
        }
        if (workDeal.getWorkStatus() != WorkStatusEnum.TO_SIGN.getCode() && workDeal.getEngineer().equals(userInfo.getUserId())) {
            throw new AppException("工单状态不正确，当前状态为[" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "]！");
        }
        if (workDeal.getWorkStatus() != WorkStatusEnum.TO_SIGN.getCode()
                && workDeal.getWorkStatus() != WorkStatusEnum.IN_SERVICE.getCode()
                && togetherEngineerIds.contains(userInfo.getUserId())) {
            throw new AppException("工单状态不正确，当前状态为[" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "]！");
        }
        Integer traffic = workSignDto.getTraffic();
        if (IntUtil.isZero(traffic)) {
            throw new AppException("交通工具不能为空！");
        }
        if (traffic == 6 && StrUtil.isBlank(workSignDto.getTrafficNote())) {
            throw new AppException("交通工具为[出租车]时，交通说明不能为空！");
        }
        if( workSignDto.getGoTime() == null) {
            throw new AppException("出发时间不能为空！");
        }
        if (workSignDto.getSignTime() == null) {
            throw new AppException("到达时间不能为空！");
        }
        if (workSignDto.getSignTime().before(workSignDto.getGoTime())) {
            throw new AppException("到达时间不能早于出发时间！");
        }
        if (DateUtil.date().isBefore(workSignDto.getSignTime())) {
            throw new AppException("到达时间不能晚于当前时间！");
        }
        DeviceBranch deviceBranch = deviceBranchService.getById(workRequest.getDeviceBranch());
        if (deviceBranch != null && deviceBranch.getLat() != null && deviceBranch.getLon() != null
                && workSign.getLat() != null && workSign.getLon() != null) {
            // 根据经纬度获取距离
            workSign.setDeviation(CoordinateUtil.getDistance(workSign.getLat().doubleValue(), workSign.getLon().doubleValue()
                    , deviceBranch.getLat().doubleValue(), deviceBranch.getLon().doubleValue()).intValue());
        }
        workSign.setSignId(KeyUtil.getId());
        workSign.setSignTime(workSignDto.getSignTime());
        workSign.setOperator(userInfo.getUserId());
        workSign.setOperateTime(DateUtil.date());
        // 标记协同工程师
        if (togetherEngineerIds.contains(userInfo.getUserId()) && !workDeal.getEngineer().equals(userInfo.getUserId())) {
            workSign.setTogether(EnabledEnum.YES.getCode());
        }
        this.save(workSign);

        // 主负责工程师才更改workDeal和添加workOperate
        if (workDeal.getEngineer().equals(userInfo.getUserId())) {
            workDeal = new WorkDeal();
            workDeal.setWorkId(workSign.getWorkId());
            // 签到就代表为现场服务
            workDeal.setServiceMode(ServiceModeEnum.LOCALE_SERVICE.getCode());
            workDeal.setWorkStatus(WorkStatusEnum.IN_SERVICE.getCode());
            workDeal.setGoTime(workSignDto.getGoTime());
            workDeal.setSignTime(workSignDto.getSignTime());
            workDeal.setTraffic(workSignDto.getTraffic());
            workDeal.setTrafficNote(StrUtil.trimToEmpty(workSignDto.getTrafficNote()));
            workDealService.updateById(workDeal);

            // 添加操作记录
            WorkOperate workOperate = new WorkOperate();
            workOperate.setWorkId(workDeal.getWorkId());
            workOperate.setWorkStatus(WorkStatusEnum.IN_SERVICE.getCode());
            workOperate.setOperator(userInfo.getUserId());
            workOperate.setCorp(reqParam.getCorpId());
            workOperate.setReferId(workSign.getSignId());
            workOperateService.addWorkOperateBySign(workOperate, workSign);
        }

        workRemindDealService.addMessageQueue(WorkMqTopic.WORK_REMIND_DEAL, workSignDto.getWorkId(),
                WorkRemindTypeEnum.TO_SIGN_EXPIRE.getCode(), workSignDto.getSignTime());

        return workSign.getSignId();
    }

    @Override
    public List<WorkSign> queryWorkSignForCost(WorkSignFilter workSignFilter) {

        QueryWrapper queryWrapper  = new QueryWrapper<WorkSign>();

        if(LongUtil.isNotZero(workSignFilter.getOperator())){
            queryWrapper.eq("operator",workSignFilter.getOperator());
        }

        if(CollectionUtil.isNotEmpty(workSignFilter.getWorkIdSet())){
            queryWrapper.in("work_id",workSignFilter.getWorkIdSet());
        }

        /**必须升序，不要修改*/
        queryWrapper.orderByAsc("sign_time");

        List<WorkSign> workSignList = list(queryWrapper);

        return workSignList;
    }

    /**
     * 查询工单的签到记录
     *
     * @param workId
     * @return
     */
    @Override
    public List<WorkSign> listByWorkId(Long workId) {
        List<WorkSign> list = new ArrayList<>();
        if (LongUtil.isZero(workId)) {
            return list;
        }
        list = this.list(new QueryWrapper<WorkSign>().eq("work_id", workId));
        return list;
    }

    /**
     * 查询签到记录
     * @date 2020/5/15
     * @param signId
     * @return com.zjft.usp.anyfix.work.sign.dto.WorkSignDto
     */
    @Override
    public WorkSignDto querySign(Long signId) {
        WorkSignDto signDto = new WorkSignDto();
        WorkSign workSign = this.baseMapper.selectById(signId);
        BeanUtils.copyProperties(workSign, signDto);
        return signDto;
    }

}
