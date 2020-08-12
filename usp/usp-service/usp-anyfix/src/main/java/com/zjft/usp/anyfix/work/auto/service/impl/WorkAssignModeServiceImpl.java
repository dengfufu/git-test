package com.zjft.usp.anyfix.work.auto.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.enums.EnabledEnum;
import com.zjft.usp.anyfix.baseinfo.model.WorkCondition;
import com.zjft.usp.anyfix.baseinfo.service.WorkConditionService;
import com.zjft.usp.anyfix.baseinfo.service.WorkTypeService;
import com.zjft.usp.anyfix.common.feign.dto.DeviceSmallClassDto;
import com.zjft.usp.anyfix.corp.branch.service.DeviceBranchService;
import com.zjft.usp.anyfix.corp.user.model.ServiceBranchUser;
import com.zjft.usp.anyfix.corp.user.service.ServiceBranchUserService;
import com.zjft.usp.anyfix.work.assign.model.WorkAssign;
import com.zjft.usp.anyfix.work.assign.service.WorkAssignEngineerService;
import com.zjft.usp.anyfix.work.assign.service.WorkAssignService;
import com.zjft.usp.anyfix.work.auto.dto.WorkAssignModeDto;
import com.zjft.usp.anyfix.work.auto.dto.WorkDispatchServiceCorpDto;
import com.zjft.usp.anyfix.work.auto.enums.AssignModeEnum;
import com.zjft.usp.anyfix.work.auto.filter.WorkAssignModeFilter;
import com.zjft.usp.anyfix.work.auto.mapper.WorkAssignModeMapper;
import com.zjft.usp.anyfix.work.auto.model.WorkAssignMode;
import com.zjft.usp.anyfix.work.auto.model.WorkAssignRule;
import com.zjft.usp.anyfix.work.auto.service.WorkAssignModeService;
import com.zjft.usp.anyfix.work.auto.service.WorkAssignRuleService;
import com.zjft.usp.anyfix.work.auto.service.WorkDispatchServiceCorpService;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.operate.enums.WorkOperateTypeEnum;
import com.zjft.usp.anyfix.work.operate.model.WorkOperate;
import com.zjft.usp.anyfix.work.operate.service.WorkOperateService;
import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
import com.zjft.usp.anyfix.work.request.model.WorkRequest;
import com.zjft.usp.anyfix.work.transfer.model.WorkTransfer;
import com.zjft.usp.anyfix.work.transfer.service.WorkTransferService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.device.dto.DeviceInfoDto;
import com.zjft.usp.device.service.DeviceFeignService;
import com.zjft.usp.pos.service.PosFeignService;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 派单模式配置 服务实现类
 * </p>
 *
 * @author zphu
 * @since 2019-09-26
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkAssignModeServiceImpl extends ServiceImpl<WorkAssignModeMapper, WorkAssignMode> implements WorkAssignModeService {

    @Resource
    private WorkAssignModeMapper workAssignModeMapper;

    @Resource
    private WorkConditionService workConditionService;

    @Resource
    private WorkAssignRuleService workAssignRuleService;
    @Resource
    private ServiceBranchUserService serviceBranchUserService;
    @Resource
    private WorkTransferService workTransferService;

    @Resource
    private WorkAssignEngineerService workAssignEngineerService;

    @Resource
    private WorkDealService workDealService;

    @Resource
    private WorkAssignService workAssignService;


    @Resource
    private UasFeignService uasFeignService;

    @Resource
    private DeviceFeignService deviceFeignService;

    @Resource
    private DeviceBranchService deviceBranchService;

    @Resource
    private WorkTypeService workTypeService;

    @Resource
    private WorkDispatchServiceCorpService workDispatchServiceCorpService;

    @Autowired
    private WorkOperateService workOperateService;

    @Resource
    private PosFeignService posFeignService;

    /**
     * 分页查询自动派单规则列表
     *
     * @param workAssignModeFilter
     * @return
     * @author zgpi
     * @date 2019/11/14 11:02
     **/
    @Override
    public ListWrapper<WorkAssignModeDto> query(WorkAssignModeFilter workAssignModeFilter) {
        Page<WorkAssignModeDto> page = new Page(workAssignModeFilter.getPageNum(), workAssignModeFilter.getPageSize());
        List<WorkAssignModeDto> workAssignModeDtoList = workAssignModeMapper.query(workAssignModeFilter, page);
        if (CollectionUtil.isNotEmpty(workAssignModeDtoList)) {
            List<Long> corpIdList = new ArrayList<>();
            List<Long> serviceCorpList = new ArrayList<>();
            for (WorkAssignModeDto dto : workAssignModeDtoList) {
                serviceCorpList.add(dto.getServiceCorp());
                corpIdList.add(dto.getServiceCorp());
                corpIdList.add(dto.getCustomCorp());
                corpIdList.add(dto.getDemanderCorp());
            }
            Map<Long, String> userMap = uasFeignService.mapUserIdAndNameByCorpIdList(serviceCorpList).getData();
            Map<Long, String> corpMap = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList)).getData();
            Map<String, String> areaMap = JsonUtil.parseObject(JsonUtil.toJson(uasFeignService.mapAreaCodeAndName().getData()), Map.class);
            Map<Long, String> modelMap = deviceFeignService.mapDeviceModelByCorpIdList(corpIdList).getData();
            Map<Long, String> largeClassMap = deviceFeignService.mapLargeClassByCorpIdList(corpIdList).getData();
            Map<Long, String> smallClassMap = deviceFeignService.mapSmallClassByCorpIdList(corpIdList).getData();
            Map<Long, String> brandMap = deviceFeignService.mapDeviceBrandByCorpIdList(corpIdList).getData();
            Map<Long, String> deviceBranchMap = deviceBranchService.mapDeviceBranchByCorpIdList(corpIdList);
            Map<Integer, String> workTypeMap = workTypeService.mapWorkTypeByCorpIdList(corpIdList);

            for (WorkAssignModeDto dto : workAssignModeDtoList) {
                dto.setDemanderCorpName(StrUtil.trimToEmpty(corpMap.get(dto.getDemanderCorp())));
                dto.setCustomCorpName(StrUtil.trimToEmpty(corpMap.get(dto.getCustomCorp())));
                dto.setServiceCorpName(StrUtil.trimToEmpty(corpMap.get(dto.getServiceCorp())));
                dto.setLargeClassName(StrUtil.trimToEmpty(largeClassMap.get(dto.getLargeClassId())));
                dto.setWorkTypeName(workDispatchServiceCorpService.getIntegerName(dto.getWorkType(), workTypeMap, ","));
                dto.setSmallClassName(workDispatchServiceCorpService.getName(dto.getSmallClassId(), smallClassMap, ","));
                dto.setBrandName(workDispatchServiceCorpService.getName(dto.getBrandId(), brandMap, ","));
                dto.setModelName(workDispatchServiceCorpService.getName(dto.getModelId(), modelMap, ","));
                dto.setDeviceBranchName(workDispatchServiceCorpService.getName(dto.getDeviceBranch(), deviceBranchMap, ","));
                dto.setUserNameList(workDispatchServiceCorpService.getName(dto.getUserList(), userMap, ","));
                dto.setDistrictName(workDispatchServiceCorpService.getDistrictName(dto.getDistrict(), areaMap, "-"));
            }
        }
        return ListWrapper.<WorkAssignModeDto>builder()
                .list(workAssignModeDtoList)
                .total(page.getTotal())
                .build();
    }

    @Override
    public Integer add(WorkAssignModeDto workAssignMode) {

        workAssignMode.setId(KeyUtil.getId());//主键

        workAssignMode.setConditionId(KeyUtil.getId());//条件配置主键

        workAssignMode.setAssignRule(KeyUtil.getId());//规则主键

        //条件配置
        workConditionService.addWorkCondition(workAssignMode, workAssignMode.getConditionId());

        //规则
        workAssignRuleService.addByWorkAssignMode(workAssignMode);

        return workAssignModeMapper.insert(workAssignMode);

    }

    @Override
    public Integer mod(WorkAssignModeDto workAssignMode) {

        workConditionService.delById(workAssignMode.getConditionId());//先删除条件配置

        workAssignRuleService.delById(workAssignMode.getAssignRule());//先删除规则


        workConditionService.addWorkCondition(workAssignMode, workAssignMode.getConditionId());
        workAssignRuleService.addByWorkAssignMode(workAssignMode);


        return workAssignModeMapper.updateById(workAssignMode);

    }

    @Override
    public void delById(Long id) {
        WorkAssignMode workAssignMode = workAssignModeMapper.selectById(id);
        workConditionService.delById(workAssignMode.getConditionId());
        workAssignRuleService.delById(workAssignMode.getAssignRule());
        workAssignModeMapper.deleteById(id);
    }

    /**
     * 自动派单
     *
     * @param workRequest
     * @param workDeal
     * @return
     * @author zgpi
     * @date 2019/11/13 13:40
     **/
    @Override
    public Integer autoAssign(WorkRequest workRequest, WorkDeal workDeal) {
        // 先获得设备档案中的服务工程师
        Long engineer = 0L;
        if (LongUtil.isNotZero(workDeal.getDeviceId())) {
            Result deviceInfoResult = deviceFeignService.findDeviceInfo(workDeal.getDeviceId());
            DeviceInfoDto deviceInfoDto = JsonUtil.parseObject(JsonUtil.toJson(deviceInfoResult.getData()), DeviceInfoDto.class);
            if (deviceInfoDto != null && LongUtil.isNotZero(deviceInfoDto.getEngineer())) {
                engineer = deviceInfoDto.getEngineer();
            }
        }
        // 设备档案中有服务工程师，直接派单
        if (LongUtil.isNotZero(engineer)) {
            WorkAssign workAssign = new WorkAssign();
            workAssign.setAssignId(KeyUtil.getId());
            workAssign.setStaffId(workRequest.getCustomCorp());
            workAssign.setWorkId(workRequest.getWorkId());
            workAssign.setRemark("自动派单");
            this.addWorkAssign(workAssign);

            workAssignEngineerService.add(workAssign.getAssignId(), engineer);
            List<Long> engineerIds = new ArrayList<>();
            engineerIds.add(engineer);
            this.updateWorkDeal(workDeal, workAssign, AssignModeEnum.DUTY_SERVICE.getCode(), engineerIds);
            return 0;
        }

        WorkAssignModeDto assign = this.matchAssignMode(workRequest, workDeal);
        // 没有设置派单规则需要人工派单
        if (assign == null || LongUtil.isZero(assign.getId())) {
            return null;
        } else {
            WorkAssign workAssign = new WorkAssign();
            workAssign.setStaffId(workRequest.getCustomCorp());
            workAssign.setWorkId(workRequest.getWorkId());

            boolean needCheckManualMode = true;

            // 设置了自动派单
            if (assign.getAutoMode() > 0) {
                switch (assign.getAutoMode()) {
                    // 派给设备负责工程师
                    case AssignModeEnum.DUTY_SERVICE_CODE:
                        if (workRequest.getDeviceCode() != null && workRequest.getDeviceCode().trim().length() > 0) {

                            List<Long> engineerIds = deviceFeignService.findDeviceEngineers(workRequest.getDeviceCode(),
                                    workRequest.getSmallClass(), workRequest.getBrand(), workRequest.getModel(),
                                    workRequest.getSerial(),
                                    workRequest.getCustomCorp(), assign.getServiceCorp()).getData();

                            if (engineerIds != null && engineerIds.size() > 0) {
                                needCheckManualMode = false;
                                workAssign.setAssignId(KeyUtil.getId());
                                workAssign.setRemark("自动派单");
                                this.addWorkAssign(workAssign);

                                for (Long engineerId : engineerIds) {
                                    workAssignEngineerService.add(workAssign.getAssignId(), engineerId);
                                }
                                this.updateWorkDeal(workDeal, workAssign, AssignModeEnum.DUTY_SERVICE.getCode(), engineerIds);
                            }
                        }
                        break;
                    // 派给小组
                    case AssignModeEnum.TEAM_SERVICE_CODE:
                        String users = assign.getUserList();
                        if (users != null && users.trim().length() > 0) {
                            needCheckManualMode = false;
                            workAssign.setAssignId(KeyUtil.getId());
                            workAssign.setRemark("自动派单");
                            this.addWorkAssign(workAssign);
                            String[] userList = users.split(",");
                            List<Long> userIdList = new ArrayList<>();
                            for (String userId : userList) {
                                userIdList.add(Long.parseLong(userId));
                                workAssignEngineerService.add(workAssign.getAssignId(), Long.parseLong(userId));
                            }
                            this.updateWorkDeal(workDeal, workAssign, AssignModeEnum.TEAM_SERVICE.getCode(), userIdList);

                        }
                        break;
                    // 距离优先
                    case AssignModeEnum.DISTANCE_SERVICE_CODE:
                        int distance = assign.getDistance();

                        //距离优先 匹配工程师
                        List<Long> userList = this.distanceAssigner(distance, workRequest, workDeal);
                        if (userList != null && userList.size() > 0) {
                            workAssign.setAssignId(KeyUtil.getId());
                            workAssign.setRemark("自动派单");
                            this.addWorkAssign(workAssign);

                            for (Long userId : userList) {
                                workAssignEngineerService.add(workAssign.getAssignId(), userId);
                            }
                            this.updateWorkDeal(workDeal, workAssign, AssignModeEnum.DISTANCE_SERVICE.getCode(), userList);
                        }

                        break;
                    // 派给网点所有人
                    case AssignModeEnum.NRANCHALL_SERVICE_CODE:
                        WorkTransfer workTransfer = workTransferService.getById(workRequest.getWorkId());
                        List<ServiceBranchUser> serviceBranchUserList = serviceBranchUserService.list(new QueryWrapper<ServiceBranchUser>()
                                .eq("branch_id", workTransfer.getServiceBranch()));

                        if (serviceBranchUserList != null && serviceBranchUserList.size() > 0) {
                            needCheckManualMode = false;
                            workAssign.setAssignId(KeyUtil.getId());
                            workAssign.setRemark("自动派单");
                            this.addWorkAssign(workAssign);

                            for (ServiceBranchUser user : serviceBranchUserList) {
                                Long engineerId = user.getUserId();
                                workAssignEngineerService.add(workAssign.getAssignId(), engineerId);
                            }
                            List<Long> userIdList = serviceBranchUserList.stream().map(e -> e.getUserId()).collect(Collectors.toList());
                            this.updateWorkDeal(workDeal, workAssign, AssignModeEnum.NRANCHALL_SERVICE.getCode(), userIdList);
                        }
                        break;
                    default:
                        break;
                }
                // 如果自动匹配模式没有找到任何人，还是需要服务商客服人工派单
                if (!needCheckManualMode) {
                    return -1;
                } else {
                    return 0; // 自动派单成功
                }
            }
            // 设置了人工派单
            else if (assign.getManualMode() > 0 && needCheckManualMode) {
                return assign.getManualMode();
            }
            return -1;
        }
    }

    /**
     * 按距离指派工程师
     *
     * @param distance
     * @param workRequest
     * @return
     */
    private List<Long> distanceAssigner(int distance, WorkRequest workRequest, WorkDeal workDeal) {

        if (workRequest.getLon() == null || workRequest.getLon().compareTo(BigDecimal.ZERO) == 0 ||
                workRequest.getLat() == null || workRequest.getLat().compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }

        List<Long> list = this.posFeignService.findNearEngineers(distance, workRequest.getLon().doubleValue(),
                workRequest.getLat().doubleValue()).getData();

        // todo 判断用户所属公司
        // workDeal.getServiceCorp();

        return list;
    }

    /**
     * 根据条件获得自动派单规则
     *
     * @param workRequest
     * @param workDeal
     * @return
     * @author zgpi
     * @date 2019/11/14 18:59
     **/
    private WorkAssignModeDto matchAssignMode(WorkRequest workRequest, WorkDeal workDeal) {
        WorkDispatchServiceCorpDto workDispatchServiceCorpDto = new WorkDispatchServiceCorpDto();
        workDispatchServiceCorpDto.setDemanderCorp(workDeal.getDemanderCorp());
        workDispatchServiceCorpDto.setServiceCorp(workDeal.getServiceCorp());
        workDispatchServiceCorpDto.setCustomCorp(workRequest.getCustomCorp());
        workDispatchServiceCorpDto.setWorkType(String.valueOf(workRequest.getWorkType()));

        Long smallClassId = workRequest.getSmallClass();
        Result smallClassResult = deviceFeignService.findDeviceSmallClass(smallClassId);
        DeviceSmallClassDto deviceSmallClassDto = JsonUtil.parseObject(JsonUtil.toJson(smallClassResult.getData()), DeviceSmallClassDto.class);
        workDispatchServiceCorpDto.setLargeClassId(deviceSmallClassDto.getLargeClassId());
        workDispatchServiceCorpDto.setSmallClassId(String.valueOf(smallClassId));
        workDispatchServiceCorpDto.setBrandId(String.valueOf(workRequest.getBrand()));
        workDispatchServiceCorpDto.setModelId(String.valueOf(workRequest.getModel()));
        workDispatchServiceCorpDto.setDistrict(workRequest.getDistrict());
        workDispatchServiceCorpDto.setDeviceBranch(String.valueOf(workRequest.getDeviceBranch()));
        workDispatchServiceCorpDto.setDeviceCode(workRequest.getDeviceCode());

        List<WorkAssignModeDto> list = workAssignModeMapper.matchAssignMode(workDispatchServiceCorpDto);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    private void addWorkAssign(WorkAssign workAssign) {
        workAssign.setAssignTime(DateUtil.date().toTimestamp());
        workAssign.setEnabled(EnabledEnum.YES.getCode());
        workAssignService.save(workAssign);
    }

    public void updateWorkDeal(WorkDeal workDeal, WorkAssign workAssign, Integer assignMode, List<Long> userIdList) {
        if (workDeal != null) {
            workDeal.setWorkStatus(WorkStatusEnum.TO_CLAIM.getCode());
            workDeal.setAssignMode(assignMode);
            workDeal.setAssignTime(workAssign.getAssignTime());
            workDealService.updateById(workDeal);

            WorkOperate workOperate = new WorkOperate();
            workOperate.setWorkStatus(WorkStatusEnum.TO_CLAIM.getCode());
            workOperate.setOperateType(WorkOperateTypeEnum.AUTO_ASSIGN.getCode());
            workOperate.setReferId(workAssign.getAssignId());
            workOperate.setWorkId(workDeal.getWorkId());
            if (CollectionUtil.isNotEmpty(userIdList)) {
                Result<Map<Long, String>> userMapResult = uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList));
                Map<Long, String> userMap = userMapResult.getData();
                StringBuilder summary = new StringBuilder(32);
                summary.append("自动派工给");
                for (Long userId : userIdList) {
                    summary.append(" 工程师").append(StrUtil.trimToEmpty(userMap.get(userId)));
                }
                workOperate.setSummary(summary.toString());
            }
            workOperateService.addAutoWorkOperate(workOperate);
        }
    }

    @Override
    public WorkAssignModeDto getWorkAssignMode(Long id) {

        WorkAssignMode workAssignMode = workAssignModeMapper.selectById(id);

        WorkCondition workCondition = workConditionService.getById(workAssignMode.getConditionId());


        WorkAssignRule workAssignRule = workAssignRuleService.getById(workAssignMode.getAssignRule());

        WorkAssignModeDto workAssignModeDto = new WorkAssignModeDto();

        BeanUtils.copyProperties(workAssignMode, workAssignModeDto);
        BeanUtils.copyProperties(workCondition, workAssignModeDto);
        BeanUtils.copyProperties(workAssignRule, workAssignModeDto);

        return workAssignModeDto;
    }
}
