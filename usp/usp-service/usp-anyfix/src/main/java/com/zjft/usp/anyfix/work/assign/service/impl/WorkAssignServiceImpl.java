package com.zjft.usp.anyfix.work.assign.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.enums.EnabledEnum;
import com.zjft.usp.anyfix.common.feign.dto.CorpUserDto;
import com.zjft.usp.anyfix.corp.user.dto.ServiceBranchUserDto;
import com.zjft.usp.anyfix.corp.user.dto.StaffSkillDto;
import com.zjft.usp.anyfix.corp.user.enums.RecommendLevelEnum;
import com.zjft.usp.anyfix.corp.user.model.ServiceBranchUser;
import com.zjft.usp.anyfix.corp.user.service.ServiceBranchUserService;
import com.zjft.usp.anyfix.corp.user.service.StaffSkillService;
import com.zjft.usp.anyfix.work.assign.dto.WorkAssignDto;
import com.zjft.usp.anyfix.work.assign.dto.WorkAssignEngineerDto;
import com.zjft.usp.anyfix.work.assign.filter.WorkAssignFilter;
import com.zjft.usp.anyfix.work.assign.mapper.WorkAssignMapper;
import com.zjft.usp.anyfix.work.assign.model.WorkAssign;
import com.zjft.usp.anyfix.work.assign.model.WorkAssignEngineer;
import com.zjft.usp.anyfix.work.assign.service.WorkAssignEngineerService;
import com.zjft.usp.anyfix.work.assign.service.WorkAssignService;
import com.zjft.usp.anyfix.work.auto.enums.AssignModeEnum;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.listener.WorkMqTopic;
import com.zjft.usp.anyfix.work.operate.model.WorkOperate;
import com.zjft.usp.anyfix.work.operate.service.WorkOperateService;
import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
import com.zjft.usp.anyfix.work.request.model.WorkRequest;
import com.zjft.usp.anyfix.work.request.service.WorkRequestService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.feign.service.RightFeignService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.device.dto.DeviceInfoDto;
import com.zjft.usp.device.service.DeviceFeignService;
import com.zjft.usp.mq.util.MqSenderUtil;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zphu
 * @date 2019/9/23 9:23
 * @Version 1.0
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkAssignServiceImpl extends ServiceImpl<WorkAssignMapper, WorkAssign> implements WorkAssignService {

    @Autowired
    private WorkAssignEngineerService workAssignEngineerService;
    @Autowired
    private WorkRequestService workRequestService;
    @Autowired
    private StaffSkillService staffSkillService;
    @Autowired
    private WorkDealService workDealService;
    @Autowired
    private WorkOperateService workOperateService;
    @Autowired
    private ServiceBranchUserService serviceBranchUserService;

    @Resource
    private UasFeignService uasFeignService;
    @Resource
    private DeviceFeignService deviceFeignService;
    @Resource
    private RightFeignService rightFeignService;

    @Resource
    private MqSenderUtil mqSenderUtil;
    /**
     * 获得可派单工程师列表
     *
     * @param workAssignFilter
     * @return
     * @author zgpi
     * @date 2019/10/12 2:19 下午
     **/
    @Override
    public ListWrapper<ServiceBranchUserDto> listEngineer(WorkAssignFilter workAssignFilter) {
        List<Long> engineerIds = null;
        Long corpId = null;
        if ("1".equals(workAssignFilter.getForAll())) {
            corpId = workAssignFilter.getCorpId();
            if (LongUtil.isZero(workAssignFilter.getServiceBranch())) {
                if (LongUtil.isNotZero(workAssignFilter.getWorkId())) {
                    WorkDeal workDeal = workDealService.getById(workAssignFilter.getWorkId());
                    if (workDeal == null) {
                        throw new AppException("工单不存在！");
                    }
                    workAssignFilter.setServiceBranch(workDeal.getServiceBranch());
                    corpId = workDeal.getServiceCorp();
                }
            }
            if (LongUtil.isNotZero(workAssignFilter.getServiceBranch())) {
                engineerIds = serviceBranchUserService.listUserIdsByBranchId(workAssignFilter.getServiceBranch());
            }

        } else {
            if (LongUtil.isZero(workAssignFilter.getWorkId())) {
                throw new AppException("工单编号不能为空！");
            }
            WorkRequest workRequest = workRequestService.getById(workAssignFilter.getWorkId());
            WorkDeal workDeal = workDealService.getById(workAssignFilter.getWorkId());
            if (workRequest == null || workDeal == null) {
                throw new AppException("工单不存在！");
            }
            if (LongUtil.isZero(workAssignFilter.getServiceBranch())) {
                workAssignFilter.setServiceBranch(workDeal.getServiceBranch());
            }
            corpId = workDeal.getServiceCorp();
            List<ServiceBranchUserDto> serviceBranchUserDtoList = new ArrayList<>();

            ServiceBranchUserDto serviceBranchUserDto = new ServiceBranchUserDto();
            // 优先添加设备档案中的服务工程师
            Result<DeviceInfoDto> deviceInfoResult = deviceFeignService.findDeviceInfo(workDeal.getDeviceId());
            DeviceInfoDto deviceInfoDto = new DeviceInfoDto();
            if (deviceInfoResult != null && deviceInfoResult.getCode() == Result.SUCCESS) {
                deviceInfoDto = deviceInfoResult.getData();
            }
            if (LongUtil.isNotZero(deviceInfoDto.getEngineer())) {
                serviceBranchUserDto.setUserId(deviceInfoDto.getEngineer());
                serviceBranchUserDto.setRecommend(RecommendLevelEnum.STRONG_RECOMMEND.getCode());
                serviceBranchUserDto.setRecommendReason("设备负责工程师");
                serviceBranchUserDtoList.add(serviceBranchUserDto);
            }
            // 匹配人员技能
            if (workAssignFilter.getIsMatchSkill() != null && workAssignFilter.getIsMatchSkill()) {
                //设置技能信息
                StaffSkillDto staffSkillDto = new StaffSkillDto();
                staffSkillDto.setBrandIds(deviceInfoDto.getBrandId().toString());
                staffSkillDto.setCorpId(workDeal.getServiceCorp());
                staffSkillDto.setBranchId(workAssignFilter.getServiceBranch());
                staffSkillDto.setModelIds(deviceInfoDto.getModelId().toString());
                staffSkillDto.setWorkTypes(workRequest.getWorkType().toString());
                staffSkillDto.setSmallClassIds(deviceInfoDto.getSmallClassId().toString());
                engineerIds = staffSkillService.skillMatchUserByBranch(staffSkillDto);
            } else {
                // 不匹配技能, 查询网点所有人员
                engineerIds = serviceBranchUserService.listUserIdsByBranchId(workAssignFilter.getServiceBranch());
            }
            // 添加设备负责工程师
            if (LongUtil.isNotZero(deviceInfoDto.getEngineer())) {
                engineerIds.add(deviceInfoDto.getEngineer());
            }
        }

        ListWrapper<ServiceBranchUserDto> listWrapper = new ListWrapper<>();
        Page page = new Page(workAssignFilter.getPageNum(), workAssignFilter.getPageSize());
        // 根据姓名搜寻工程师
        if (CollectionUtil.isNotEmpty(engineerIds)) {
            String jsonFilter = JsonUtil.toJsonString("mobileFilter", workAssignFilter.getUserName(),
                    "corpId", corpId, "userIdList", engineerIds, "pageNum", workAssignFilter.getPageNum(),
                    "pageSize", workAssignFilter.getPageSize());
            Result result = uasFeignService.queryCorpUser(jsonFilter);
            ListWrapper corpUserDtoListWrapper = JsonUtil.parseObject(JsonUtil.toJson(result.getData()), ListWrapper.class);
            if (corpUserDtoListWrapper != null && CollectionUtil.isNotEmpty(corpUserDtoListWrapper.getList())) {
                List<CorpUserDto> list = JsonUtil.parseArray(JsonUtil.toJson(corpUserDtoListWrapper.getList()), CorpUserDto.class);
                List<ServiceBranchUserDto> serviceBranchUserDtoListResult = new ArrayList<>();
                ServiceBranchUserDto entity;
                for (CorpUserDto corpUserDto : list) {
                    entity = new ServiceBranchUserDto();
                    entity.setUserId(corpUserDto.getUserId());
                    entity.setUserName(corpUserDto.getUserName());
                    entity.setMobile(corpUserDto.getMobile());
                    entity.setRoleNames(corpUserDto.getRoleNames());
                    serviceBranchUserDtoListResult.add(entity);
                }
                listWrapper.setList(serviceBranchUserDtoListResult);
                listWrapper.setTotal(corpUserDtoListWrapper.getTotal());
                return listWrapper;
            }
        }

        return listWrapper;
    }

    /**
     * 派单
     *
     * @param workAssignDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/10/12 5:30 下午
     **/
    @Override
    public Long assignWork(WorkAssignDto workAssignDto, UserInfo userInfo, ReqParam reqParam) {
        if (LongUtil.isZero(workAssignDto.getWorkId())) {
            throw new AppException("工单编号不能为空！");
        }
        if (CollectionUtil.isEmpty(workAssignDto.getAssignEngineerList())) {
            throw new AppException("工程师不能为空！");
        }
        WorkRequest workRequest = workRequestService.getById(workAssignDto.getWorkId());
        if (workRequest == null) {
            throw new AppException("工单不存在！");
        }
        WorkDeal workDeal = workDealService.getById(workAssignDto.getWorkId());
        if (workDeal == null) {
            throw new AppException("工单不存在！");
        }
        if (workDeal.getWorkStatus() != WorkStatusEnum.TO_ASSIGN.getCode()) {
            throw new AppException("工单状态不正确，当前状态为[" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "]！");
        }

        //插入派单记录
        this.modAssignNotEnable(workAssignDto.getWorkId());
        WorkAssign workAssign = new WorkAssign();
        BeanUtils.copyProperties(workAssignDto, workAssign);
        workAssign.setAssignId(KeyUtil.getId());
        workAssign.setEnabled(EnabledEnum.YES.getCode());
        workAssign.setAssignTime(DateUtil.date());

        workAssign.setStaffId(workRequest.getCustomCorp());
        this.save(workAssign);
        //插入工程师派单记录
        WorkAssignEngineer workAssignEngineer;
        for (WorkAssignEngineerDto workAssignEngineerDto : workAssignDto.getAssignEngineerList()) {
            workAssignEngineer = new WorkAssignEngineer();
            workAssignEngineer.setAssignId(workAssign.getAssignId());
            workAssignEngineer.setEngineerId(workAssignEngineerDto.getUserId());
            workAssignEngineerService.save(workAssignEngineer);
        }


        //修改工单详情表
        workDeal = new WorkDeal();
        workDeal.setWorkId(workRequest.getWorkId());
        workDeal.setWorkStatus(WorkStatusEnum.TO_CLAIM.getCode());
        workDeal.setAssignStaff(userInfo.getUserId());
        workDeal.setAssignTime(DateUtil.date());
        workDeal.setAssignMode(AssignModeEnum.MANUAL.getCode());
        workDealService.updateById(workDeal);

        // 添加操作记录
        WorkOperate workOperate = new WorkOperate();
        workOperate.setWorkId(workDeal.getWorkId());
        workOperate.setReferId(workAssign.getAssignId());
        workOperate.setOperator(userInfo.getUserId());
        workOperate.setCorp(reqParam.getCorpId());
        workOperateService.addWorkOperateByAssign(workOperate, workAssignDto);

        return workAssign.getAssignId();
    }


    /**
     * 转派单
     *
     * @param workAssignDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/10/12 5:32 下午
     **/
    @Override
    public void turnAssignWork(WorkAssignDto workAssignDto, UserInfo userInfo, ReqParam reqParam) {
        Assert.notNull(workAssignDto, "派单参数不能为空");
        Assert.notNull(userInfo, "当前用户信息不能为空");
        if (LongUtil.isZero(workAssignDto.getWorkId())) {
            throw new AppException("工单号不能为空！");
        }
        if (CollectionUtil.isEmpty(workAssignDto.getAssignEngineerList())) {
            throw new AppException("工程师不能为空！");
        }
        WorkRequest workRequest = workRequestService.getById(workAssignDto.getWorkId());
        if (workRequest == null) {
            throw new AppException("工单不存在！");
        }
        WorkDeal workDeal = workDealService.getById(workAssignDto.getWorkId());
        if (workDeal == null) {
            throw new AppException("工单不存在！");
        }
        if (workDeal.getWorkStatus() != WorkStatusEnum.TO_CLAIM.getCode()) {
            throw new AppException("工单状态不正确，当前状态为[" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "]！");
        }

        List<ServiceBranchUser> engineerList = serviceBranchUserService.listByBranchId(workDeal.getServiceBranch());
        List<Long> idList = engineerList.stream().map(e -> e.getUserId()).collect(Collectors.toList());
        for (WorkAssignEngineerDto workAssignEngineerDto : workAssignDto.getAssignEngineerList()) {
            if (!idList.contains(workAssignEngineerDto.getUserId())) {
                throw new AppException("工程师[" + workAssignEngineerDto.getUserName() + "]必须是该服务网点人员！");
            }
        }

        //插入派单记录
        this.modAssignNotEnable(workAssignDto.getWorkId());
        WorkAssign workAssign = new WorkAssign();
        BeanUtils.copyProperties(workAssignDto, workAssign);
        workAssign.setAssignId(KeyUtil.getId());
        workAssign.setEnabled(EnabledEnum.YES.getCode());
        workAssign.setAssignTime(DateUtil.date());
        workAssign.setStaffId(workRequest.getCustomCorp());
        this.save(workAssign);

        //插入工程师派单记录
        WorkAssignEngineer workAssignEngineer;
        for (WorkAssignEngineerDto workAssignEngineerDto : workAssignDto.getAssignEngineerList()) {
            workAssignEngineer = new WorkAssignEngineer();
            workAssignEngineer.setAssignId(workAssign.getAssignId());
            workAssignEngineer.setEngineerId(workAssignEngineerDto.getUserId());
            workAssignEngineerService.save(workAssignEngineer);
        }

        //修改工单详情表
        workDeal = new WorkDeal();
        workDeal.setWorkId(workRequest.getWorkId());
        workDeal.setWorkStatus(WorkStatusEnum.TO_CLAIM.getCode());
        workDeal.setAssignStaff(userInfo.getUserId());
        workDeal.setAssignTime(DateUtil.date().toTimestamp());
        workDeal.setAssignMode(AssignModeEnum.MANUAL.getCode());
        workDealService.updateById(workDeal);

        // 添加操作记录
        WorkOperate workOperate = new WorkOperate();
        workOperate.setWorkId(workDeal.getWorkId());
        workOperate.setReferId(workAssign.getAssignId());
        workOperate.setOperator(userInfo.getUserId());
        workOperate.setCorp(reqParam.getCorpId());
        workOperateService.addWorkOperateByAssign(workOperate, workAssignDto);
    }

    /**
     * 将工单之前的派工置为无效
     *
     * @param workId
     * @return
     * @author zgpi
     * @date 2019/10/14 11:04 上午
     **/
    @Override
    public void modAssignNotEnable(Long workId) {
        WorkAssign workAssign = new WorkAssign();
        workAssign.setWorkId(workId);
        workAssign.setEnabled(EnabledEnum.NO.getCode());
        UpdateWrapper<WorkAssign> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("work_id", workId);
        this.update(workAssign, updateWrapper);
    }


    /**
     * 派单给工程师后提醒工程师
     * @param workId
     */
    @Override
    public void assignWorkListener(long workId) {
        Map<String, Object> msg = new HashMap<>(1);
        msg.put("workId", workId);
        mqSenderUtil.sendMessage(WorkMqTopic.ASSIGN_WORK, JsonUtil.toJson(msg));
    }

}
