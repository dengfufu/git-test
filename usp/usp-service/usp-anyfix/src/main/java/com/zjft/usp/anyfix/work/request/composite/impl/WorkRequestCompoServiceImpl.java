package com.zjft.usp.anyfix.work.request.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.baseinfo.dto.CustomFieldDataDto;
import com.zjft.usp.anyfix.baseinfo.dto.CustomFieldDto;
import com.zjft.usp.anyfix.baseinfo.enums.EnabledEnum;
import com.zjft.usp.anyfix.baseinfo.filter.CustomFieldDataFilter;
import com.zjft.usp.anyfix.baseinfo.model.CustomFieldData;
import com.zjft.usp.anyfix.baseinfo.model.FaultType;
import com.zjft.usp.anyfix.baseinfo.model.RemoteWay;
import com.zjft.usp.anyfix.baseinfo.model.WorkType;
import com.zjft.usp.anyfix.baseinfo.service.*;
import com.zjft.usp.anyfix.common.constant.RightConstants;
import com.zjft.usp.anyfix.common.feign.dto.*;
import com.zjft.usp.anyfix.common.service.DataScopeCompoService;
import com.zjft.usp.anyfix.common.service.RightCompoService;
import com.zjft.usp.anyfix.corp.branch.dto.DeviceBranchDto;
import com.zjft.usp.anyfix.corp.branch.model.DeviceBranch;
import com.zjft.usp.anyfix.corp.branch.model.ServiceBranch;
import com.zjft.usp.anyfix.corp.branch.service.DeviceBranchService;
import com.zjft.usp.anyfix.corp.branch.service.ServiceBranchService;
import com.zjft.usp.anyfix.corp.config.constant.ItemConfig;
import com.zjft.usp.anyfix.corp.config.dto.ServiceConfigDto;
import com.zjft.usp.anyfix.corp.config.service.ServiceConfigService;
import com.zjft.usp.anyfix.corp.fileconfig.dto.WorkFilesDto;
import com.zjft.usp.anyfix.corp.fileconfig.filter.FileConfigFilter;
import com.zjft.usp.anyfix.corp.fileconfig.service.WorkFilesService;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderCustomDto;
import com.zjft.usp.anyfix.corp.manage.model.DemanderCustom;
import com.zjft.usp.anyfix.corp.manage.service.DemanderCustomService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderServiceManagerService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderServiceService;
import com.zjft.usp.anyfix.settle.enums.WorkSettleStatusEnum;
import com.zjft.usp.anyfix.utils.BusinessCodeGenerator;
import com.zjft.usp.anyfix.work.assign.dto.WorkAssignEngineerDto;
import com.zjft.usp.anyfix.work.assign.model.WorkAssign;
import com.zjft.usp.anyfix.work.assign.model.WorkAssignEngineer;
import com.zjft.usp.anyfix.work.assign.service.WorkAssignEngineerService;
import com.zjft.usp.anyfix.work.assign.service.WorkAssignService;
import com.zjft.usp.anyfix.work.attention.model.WorkAttention;
import com.zjft.usp.anyfix.work.attention.service.WorkAttentionService;
import com.zjft.usp.anyfix.work.auto.service.WorkDispatchServiceCorpService;
import com.zjft.usp.anyfix.work.check.enums.FeeCheckStatusEnum;
import com.zjft.usp.anyfix.work.check.enums.FeeConfirmStatusEnum;
import com.zjft.usp.anyfix.work.check.enums.ServiceCheckStatusEnum;
import com.zjft.usp.anyfix.work.check.enums.ServiceConfirmStatusEnum;
import com.zjft.usp.anyfix.work.check.model.WorkCheck;
import com.zjft.usp.anyfix.work.check.service.WorkCheckService;
import com.zjft.usp.anyfix.work.deal.dto.EngineerDto;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDto;
import com.zjft.usp.anyfix.work.fee.model.WorkFee;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeDetailService;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeService;
import com.zjft.usp.anyfix.work.finish.enums.FileConfigFormTypeEnum;
import com.zjft.usp.anyfix.work.finish.enums.FilesStatusEnum;
import com.zjft.usp.anyfix.work.finish.enums.SignatureStatusEnum;
import com.zjft.usp.anyfix.work.finish.enums.WorkFeeStatusEnum;
import com.zjft.usp.anyfix.work.finish.model.WorkFinish;
import com.zjft.usp.anyfix.work.finish.service.WorkFinishService;
import com.zjft.usp.anyfix.work.follow.dto.WorkFollowDto;
import com.zjft.usp.anyfix.work.follow.service.WorkFollowService;
import com.zjft.usp.anyfix.work.listener.WorkMqTopic;
import com.zjft.usp.anyfix.work.operate.model.WorkOperate;
import com.zjft.usp.anyfix.work.operate.service.WorkOperateService;
import com.zjft.usp.anyfix.work.remind.enums.WorkRemindTypeEnum;
import com.zjft.usp.anyfix.work.request.composite.WorkRequestCompoService;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.anyfix.work.request.dto.WorkForAssignDto;
import com.zjft.usp.anyfix.work.request.dto.WorkRequestDto;
import com.zjft.usp.anyfix.work.request.enums.*;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import com.zjft.usp.anyfix.work.request.mapper.WorkRequestMapper;
import com.zjft.usp.anyfix.work.request.model.WorkRequest;
import com.zjft.usp.anyfix.work.request.service.WorkRequestService;
import com.zjft.usp.anyfix.work.sign.service.WorkSignService;
import com.zjft.usp.anyfix.work.support.dto.WorkSupportDto;
import com.zjft.usp.anyfix.work.support.service.WorkSupportService;
import com.zjft.usp.anyfix.work.transfer.model.WorkTransfer;
import com.zjft.usp.anyfix.work.transfer.service.WorkTransferService;
import com.zjft.usp.anyfix.work.ware.service.WorkWareRecycleService;
import com.zjft.usp.anyfix.work.ware.service.WorkWareUsedService;
import com.zjft.usp.common.dto.RightScopeDto;
import com.zjft.usp.common.enums.WxTemplateEnum;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.device.dto.DeviceInfoDto;
import com.zjft.usp.device.dto.DeviceSpecificationDto;
import com.zjft.usp.device.service.DeviceFeignService;
import com.zjft.usp.file.dto.FileDto;
import com.zjft.usp.file.service.FileFeignService;
import com.zjft.usp.mq.util.MqSenderUtil;
import com.zjft.usp.common.model.WxTemplateMessage;
import com.zjft.usp.uas.dto.CorpDto;
import com.zjft.usp.uas.service.UasFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zgpi
 * @version 1.0
 * @date 2019/12/10 08:42
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkRequestCompoServiceImpl implements WorkRequestCompoService {

    @Autowired
    private WorkRequestService workRequestService;
    @Autowired
    private WorkDealService workDealService;
    @Autowired
    private WorkTransferService workTransferService;
    @Autowired
    private DeviceBranchService deviceBranchService;
    @Autowired
    private WorkTypeService workTypeService;
    @Autowired
    private WorkFinishService workFinishService;
    @Autowired
    private ServiceItemService serviceItemService;
    @Autowired
    private WorkOperateService workOperateService;
    @Autowired
    private ServiceBranchService serviceBranchService;
    @Autowired
    private FaultTypeService faultTypeService;
    @Autowired
    private RemoteWayService remoteWayService;
    @Autowired
    private DemanderCustomService demanderCustomService;
    @Autowired
    private DemanderServiceService demanderServiceService;
    @Autowired
    private WorkAssignService workAssignService;
    @Autowired
    private WorkAssignEngineerService workAssignEngineerService;
    @Autowired
    private WorkWareUsedService workWareUsedService;
    @Autowired
    private WorkWareRecycleService workWareRecycleService;
    @Autowired
    private WorkFeeService workFeeService;
    @Autowired
    private CustomFieldService customFieldService;
    @Autowired
    private CustomFieldDataService customFieldDataService;
    @Autowired
    private BusinessCodeGenerator businessCodeGenerator;
    @Autowired
    private WorkSignService workSignService;
    @Autowired
    private WorkAttentionService workAttentionService;
    @Autowired
    private WorkFollowService workFollowService;
    @Autowired
    private WorkSupportService workSupportService;
    @Autowired
    private RightCompoService rightCompoService;
    @Autowired
    private DataScopeCompoService dataScopeCompoService;
    @Autowired
    private WorkFeeDetailService workFeeDetailService;
    @Autowired
    private WorkDispatchServiceCorpService workDispatchServiceCorpService;
    @Autowired
    private WorkCheckService workCheckService;
    @Autowired
    private DemanderServiceManagerService demanderServiceManagerService;

    @Resource
    private MqSenderUtil mqSenderUtil;
    @Resource
    private UasFeignService uasFeignService;
    @Resource
    private DeviceFeignService deviceFeignService;
    @Resource
    private FileFeignService fileFeignService;
    @Resource
    private WorkRequestMapper workRequestMapper;
    @Resource
    private ServiceConfigService serviceConfigService;
    @Resource
    private WorkFilesService workFilesService;

    /**
     * 分页查询工单列表
     *
     * @param workFilter 查询条件
     * @param userInfo   当前登录用户
     * @param reqParam   公共参数
     * @return 工单列表
     * @author zgpi
     * @date 2019/10/15 9:51 上午
     **/
    @Override
    public ListWrapper<WorkDto> queryWork(WorkFilter workFilter,
                                          UserInfo userInfo,
                                          ReqParam reqParam) {
        Page<WorkDto> page = new Page(workFilter.getPageNum(), workFilter.getPageSize());
        this.findUserRight(workFilter, userInfo.getUserId(), reqParam.getCorpId());

        if (LongUtil.isZero(workFilter.getCorpId())) {
            return ListWrapper.<WorkDto>builder()
                    .list(new ArrayList<>())
                    .total(0L)
                    .build();
        }

        List<WorkDto> workDtoList = workRequestMapper.queryWork(page, workFilter);
        // 获得待接单人列表
        if ("1".equals(workFilter.getForAssignees())) {
            this.getWorkForAssignEngineers(String.valueOf(WorkStatusEnum.TO_CLAIM.getCode()), workDtoList);
        } else {
            this.getWorkForAssignEngineers(workFilter.getWorkStatuses(), workDtoList);
        }
        // 初始化基础数据
        Map<String, Object> baseMap = this.initBaseMap(workDtoList);
        // 增加附加属性
        this.addExtraAttributes(workDtoList, baseMap);

        return ListWrapper.<WorkDto>builder()
                .list(workDtoList)
                .total(page.getTotal())
                .build();
    }

    /**
     * 工单导出查询全部工单(不分页)
     *
     * @param workFilter
     * @param userInfo
     * @param reqParam
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.anyfix.work.request.dto.WorkDto>
     * @date 2020/3/17
     */
    @Override
    public List<WorkDto> exportWork(WorkFilter workFilter,
                                    UserInfo userInfo,
                                    ReqParam reqParam) {
        List<WorkDto> workDtoList = new ArrayList<>();
        this.findUserRight(workFilter, userInfo.getUserId(), reqParam.getCorpId());

        if (LongUtil.isZero(workFilter.getCorpId())) {
            return workDtoList;
        }

        workDtoList = workRequestMapper.queryWork(null, workFilter);
        // 获得待接单人列表
        this.getWorkForAssignEngineers(workFilter.getWorkStatuses(), workDtoList);
        // 初始化基础数据
        Map<String, Object> baseMap = this.initBaseMap(workDtoList);
        // 增加附加属性
        this.addExtraAttributes(workDtoList, baseMap);

        return workDtoList;
    }

    /**
     * 设置工单对应的待接单工程师（多个以逗号拼接）
     *
     * @param workStatuses
     * @param workDtoList
     */
    private void getWorkForAssignEngineers(String workStatuses, List<WorkDto> workDtoList) {
        //当状态为待接单时才执行。
        if (StrUtil.isBlank(workStatuses)) {
            return;
        }

        if (!new Integer(WorkStatusEnum.TO_CLAIM.getCode()).toString().equalsIgnoreCase(workStatuses)) {
            return;
        }

        if (CollectionUtil.isNotEmpty(workDtoList)) {
            Set<Long> serviceCorpSet = new TreeSet<>();
            for (WorkDto oneWorkDto : workDtoList) {
                serviceCorpSet.add(oneWorkDto.getServiceCorp());
            }

            //获得所有服务商工程师映射
            Map<Long, String> mapUserIdAndNameByCorpId = new HashMap<>();
            for (Long serviceCorp : serviceCorpSet) {
                Result<Map<Long, String>> mapUserIdAndNameByCorpIdResult = uasFeignService.mapUserIdAndNameByCorpId(serviceCorp);
                if (mapUserIdAndNameByCorpIdResult != null && mapUserIdAndNameByCorpIdResult.getCode() == Result.SUCCESS) {
                    mapUserIdAndNameByCorpId.putAll(mapUserIdAndNameByCorpIdResult.getData());
                }
            }

            List<Long> workIdList = new ArrayList<>();
            for (WorkDto entity : workDtoList) {
                workIdList.add(entity.getWorkId());
            }
            WorkFilter workForAssignFilter = new WorkFilter();
            workForAssignFilter.setListWorkIds(workIdList);
            List<WorkForAssignDto> listForAssignEngineers = workRequestMapper.queryForAssignEngineersDto(workForAssignFilter);
            if (listForAssignEngineers != null && listForAssignEngineers.size() > 0) {
                //LIST转MAP
                Map<Long, String> mapWorkIdAndForAssignEngineers = new HashMap<>();
                for (WorkForAssignDto dto : listForAssignEngineers) {
                    mapWorkIdAndForAssignEngineers.put(dto.getWorkId(), dto.getEngineers());
                }

                for (WorkDto entity : workDtoList) {
                    if (mapWorkIdAndForAssignEngineers.containsKey(entity.getWorkId())) {
                        String engineers = mapWorkIdAndForAssignEngineers.get(entity.getWorkId());

                        List<String> engineerList = Arrays.stream(engineers.split(","))
                                .filter(StrUtil::isNotBlank).collect(Collectors.toList());
                        List<String> engineerNameList = new ArrayList<>();
                        for (String engineer : engineerList) {
                            Long engineerId = Long.parseLong(engineer, 10);
                            if (mapUserIdAndNameByCorpId.containsKey(engineerId)) {
                                engineerNameList.add(mapUserIdAndNameByCorpId.get(engineerId));
                            }
                        }

                        entity.setEngineers(engineers);
                        if (CollectionUtil.isNotEmpty(engineerNameList)) {
                            entity.setEngineerNames(CollectionUtil.join(engineerNameList, ","));
                        }
                    }
                }
            }
        }
    }

    /**
     * 创建工单
     *
     * @param workRequestDto 建单数据
     * @param userInfo       当前用户
     * @param reqParam       公共参数
     * @return 工单编号列表
     */
    @Override
    public List<Long> addWorkRequest(WorkRequestDto workRequestDto, UserInfo userInfo, ReqParam reqParam) {
        workRequestDto.setBookTimeBegin(workRequestDto.getBookTimeEnd());
        if (workRequestDto.getBookTimeEnd() != null) {
            if (workRequestDto.getFaultTime() != null
                    && workRequestDto.getBookTimeEnd().before(workRequestDto.getFaultTime())) {
                throw new AppException("预约时间不能早于故障时间！");
            }
        }
        List<Long> workIdList = new ArrayList<>();
        // 检查设备信息
        List<Long> deviceIdList = this.checkDeviceInfoByAdd(workRequestDto);

        workRequestDto.setDeviceIdList(deviceIdList);
        workRequestDto.setWorkStatusList(WorkStatusEnum.getRunningStatus());
        workRequestDto.setSerials(StrUtil.trimToEmpty(workRequestDto.getSerials()).replace("，", ","));
        if (CollectionUtil.isNotEmpty(deviceIdList)
                || StrUtil.isNotBlank(workRequestDto.getSerials()) && LongUtil.isNotZero(workRequestDto.getBrand())) {
            workRequestDto.setSerialList(Arrays.stream(StrUtil.trimToEmpty(workRequestDto.getSerials()).split(","))
                    .filter(StrUtil::isNotBlank).collect(Collectors.toList()));
            //判断传入的serials是否存在重复序列号
            if (CollectionUtil.isNotEmpty(workRequestDto.getSerialList())) {
                Set<String> serialSet = new HashSet<>(workRequestDto.getSerialList());
                if (workRequestDto.getSerialList().size() != serialSet.size()) {
                    throw new AppException("出厂序列号重复，请重新输入！");
                }
            }
            List<WorkDeal> workDealList = workDealService.listSameWork(workRequestDto);
            if (CollectionUtil.isNotEmpty(workDealList)) {
                Result result = deviceFeignService.findDeviceBrand(workRequestDto.getBrand());
                DeviceBrandDto deviceBrandDto = JsonUtil.parseObject(JsonUtil.toJsonString(result.getData()), DeviceBrandDto.class);
                Map<Integer, String> workTypeMap = workTypeService.mapWorkType(workRequestDto.getDemanderCorp());
                throw new AppException("设备品牌[" + (deviceBrandDto == null ? "" : StrUtil.trimToEmpty(deviceBrandDto.getName()))
                        + "] 出厂序列号[" + StrUtil.trimToEmpty(workRequestDto.getSerials()) + "]存在["
                        + workTypeMap.get(workRequestDto.getWorkType()) + "]工单[" + workDealList.get(0).getWorkCode() + "]未结束，不能重复建单！");
            }
        }
//        if (CollectionUtil.isNotEmpty(deviceIdList)) {
//            List<WorkDeal> workDealList = workDealService.list(new QueryWrapper<WorkDeal>()
//                    .in("device_id", deviceIdList)
//                    .inSql("work_id",
//                            " select work_id from work_request" +
//                                    " where demander_corp=" + workRequestDto.getDemanderCorp() +
//                                    " and work_type=" + workRequestDto.getWorkType())
//                    .in("work_status", WorkStatusEnum.getRunningStatus()));
//            if (CollectionUtil.isNotEmpty(workDealList)) {
//                Map<Integer, String> workTypeMap = workTypeService.mapWorkType(workRequestDto.getDemanderCorp());
//                throw new AppException("设备存在[" + workTypeMap.get(workRequestDto.getWorkType()) + "]工单未结束，不能重复建单！");
//            }
//        }
        Long demanderCorp = workRequestDto.getDemanderCorp();
        Result result = uasFeignService.findCorpById(demanderCorp);
        if (result != null && result.getCode() == Result.SUCCESS) {
            CorpDto corpDto = JsonUtil.parseObject(JsonUtil.toJson(result.getData()), CorpDto.class);
        }
        String serials = StrUtil.trimToEmpty(workRequestDto.getSerials());
        String deviceCodes = StrUtil.trimToEmpty(workRequestDto.getDeviceCodes());
        List<String> serialList = Arrays.stream(serials.split(","))
                .filter(StrUtil::isNotBlank).collect(Collectors.toList());
        List<String> deviceCodeList = Arrays.stream(deviceCodes.split(","))
                .filter(StrUtil::isNotBlank).collect(Collectors.toList());
        int length = serialList.size() >= deviceCodeList.size() ? serialList.size() : deviceCodeList.size();
        if (workRequestDto.getDeviceNum() == null) {
            workRequestDto.setDeviceNum(1);
        }
        if (length < workRequestDto.getDeviceNum()) {
            length = workRequestDto.getDeviceNum();
        }

        // 先新增客户、设备网点基础数据
        Long customId = workRequestDto.getCustomId();
        String customCorpName = StrUtil.trimToEmpty(workRequestDto.getCustomCorpName());
        if (LongUtil.isZero(customId) && StrUtil.isNotBlank(customCorpName)) {
            // 添加客户关系
            customId = this.addDemanderCustom(workRequestDto, userInfo);
            workRequestDto.setCustomId(customId);
        }
        Long deviceBranchId = workRequestDto.getDeviceBranch();
        String deviceBranchName = StrUtil.trimToEmpty(workRequestDto.getDeviceBranchName());
        if (LongUtil.isZero(deviceBranchId) && StrUtil.isNotBlank(deviceBranchName)) {
            deviceBranchId = this.addDeviceBranch(workRequestDto, userInfo, reqParam);
            workRequestDto.setDeviceBranch(deviceBranchId);
        }
        Long relateWorkId = KeyUtil.getId();
        // 临时文件编号列表
        List<Long> tempFileIdList = new ArrayList<>();
        List<Long> fileIdList = null;
        if (StrUtil.isNotBlank(workRequestDto.getFiles())) {
            List<String> fileIds = Arrays.asList(workRequestDto.getFiles().split(","));
            fileIdList = fileIds.stream().map(fileId -> Long.parseLong(fileId)).collect(Collectors.toList());
            tempFileIdList.addAll(fileIdList);
        }

        WorkRequest workRequest;
        for (int i = 0; i < length; i++) {
            String serialTemp = "";
            String deviceCodeTemp = "";
            if (serialList.size() > i) {
                serialTemp = serialList.get(i);
                workRequestDto.setSerial(serialTemp);
            } else {
                workRequestDto.setSerial("");
            }
            if (deviceCodeList.size() > i) {
                deviceCodeTemp = deviceCodeList.get(i);
                workRequestDto.setDeviceCode(deviceCodeTemp);
            } else {
                workRequestDto.setDeviceCode("");
            }
            if (workRequestDto.getIsSupplement() != null && "Y".equals(workRequestDto.getIsSupplement().trim())) {
                // 根据提单日期进行workCode
                workRequestDto.setWorkCode(this.getWorkCodeByTime(workRequestDto.getDispatchTime()));
            } else {
                workRequestDto.setWorkCode(businessCodeGenerator.getWorkCode());
            }
            workRequest = new WorkRequest();
            BeanUtils.copyProperties(workRequestDto, workRequest);
            workRequest.setLon(reqParam.getLon());
            workRequest.setLat(reqParam.getLat());
            workRequest.setCreator(userInfo.getUserId());
            workRequest.setCreateTime(DateUtil.date());
            workRequest.setWorkId(KeyUtil.getId());
            workRequest.setRelateWorkId(relateWorkId);

            List<FaultType> faultTypeList = workRequestDto.getFaultTypeList();
            if (CollectionUtil.isNotEmpty(faultTypeList)) {
                StringBuilder faultTypes = new StringBuilder(16);
                for (FaultType faultType : faultTypeList) {
                    if (faultTypes.length() > 0) {
                        faultTypes.append(",");
                    }
                    faultTypes.append(faultType.getId());
                }
                workRequest.setFaultType(faultTypes.toString());
            }

            // 第二台设备开始或者是重提单拷贝附件
            if ((i > 0 || (workRequestDto.getIfResubmitWork() != null && workRequestDto.getIfResubmitWork()))
                    && CollectionUtil.isNotEmpty(fileIdList)) {
                Result<List<FileDto>> fileResult = fileFeignService.copyFileList(fileIdList);
                if (fileResult != null && fileResult.getCode() == Result.SUCCESS) {
                    List<FileDto> fileDtoList = fileResult.getData();
                    List<String> idList = fileDtoList.stream().map(e -> e.getFileId().toString()).collect(Collectors.toList());
                    workRequest.setFiles(idList.stream().collect(Collectors.joining(",")));
                    tempFileIdList.addAll(idList.stream().map(e -> Long.parseLong(e)).collect(Collectors.toList()));
                }
            }

            this.workRequestService.save(workRequest);

            workRequestDto.setWorkId(workRequest.getWorkId());
            workRequestDto.setCreator(userInfo.getUserId());

            // 添加处理信息
            workDealService.addWorkDeal(workRequestDto);
            // 添加工单费用
            WorkFee workFee = new WorkFee();
            workFee.setWorkId(workRequest.getWorkId());
            workFee.setBasicServiceFee(workRequestDto.getBasicServiceFee() == null ?
                    BigDecimal.ZERO : workRequestDto.getBasicServiceFee());
            workFee.setOtherFee(workRequestDto.getOtherFee() == null ? BigDecimal.ZERO : workRequestDto.getOtherFee());
            workFee.setOtherFeeNote(workRequestDto.getOtherFeeNote());
            workFee.setTotalFee(workFee.getBasicServiceFee().add(workFee.getOtherFee()));
            this.workFeeService.save(workFee);

            // 添加自定义字段
            if (CollectionUtil.isNotEmpty(workRequestDto.getCustomFieldDataList())) {
                for (CustomFieldData customFieldData : workRequestDto.getCustomFieldDataList()) {
                    customFieldData.setFormId(workRequestDto.getWorkId());
                }
                customFieldDataService.addCustomFieldDataList(workRequestDto.getCustomFieldDataList());
            }

            if (workRequestDto.getIfResubmitWork() != null && workRequestDto.getIfResubmitWork()) {
                // 添加重提单操作记录
                this.addWorkOperateByResubimt(workRequestDto, userInfo, reqParam);
            } else if (workRequestDto.getIsSupplement() != null && "Y".equals(workRequestDto.getIsSupplement().trim())) {
                // 添加补单操作记录
                this.addWorkOperateBySupplement(workRequestDto, userInfo, reqParam);
            } else {
                // 添加建单操作记录
                this.addWorkOperateByCreate(workRequestDto, userInfo, reqParam);
            }
            workIdList.add(workRequest.getWorkId());
        }
        // 删除临时文件表数据
        if (CollectionUtil.isNotEmpty(tempFileIdList)) {
            this.fileFeignService.deleteFileTemporaryByFileIdList(tempFileIdList);
        }
        return workIdList;
    }

    /**
     * 修改工单
     *
     * @param workRequestDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/2/26 19:31
     */
    @Override
    public Long modWorkRequest(WorkRequestDto workRequestDto, UserInfo userInfo, ReqParam reqParam) {
        List<ServiceConfigDto> serviceConfigDtoList = serviceConfigService.getConfigByCorpId(workRequestDto.getDemanderCorp(), ItemConfig.WORK_EDIT_CONFIG_FORM_MAP);
        serviceConfigService.validateServiceConfig(serviceConfigDtoList, workRequestDto, ItemConfig.WORK_EDIT_CONFIG_FORM_MAP);
        workRequestDto.setBookTimeBegin(workRequestDto.getBookTimeEnd());
        if (workRequestDto.getBookTimeEnd() != null) {
            if (workRequestDto.getFaultTime() != null
                    && workRequestDto.getBookTimeEnd().before(workRequestDto.getFaultTime())) {
                throw new AppException("预约时间不能早于故障时间！");
            }
        }
        WorkRequest workRequest = workRequestService.getById(workRequestDto.getWorkId());
        WorkDeal workDeal = workDealService.getById(workRequest.getWorkId());
        if (workRequest == null || workDeal == null) {
            throw new AppException("工单不存在，请核查！");
        }
        if (workDeal.getWorkStatus() == WorkStatusEnum.RETURNED.getCode()
                && !userInfo.getUserId().equals(workRequest.getCreator())) {
            throw new AppException("不是建单人，不允许修改已退单的工单！");
        }
        if (workDeal.getWorkStatus() == WorkStatusEnum.TO_DISPATCH.getCode()
                && !userInfo.getUserId().equals(workRequest.getCreator())) {
            throw new AppException("不是建单人，不允许修改待提单的工单！");
        }
        if (workDeal.getServiceCorp().equals(reqParam.getCorpId())
                && (workDeal.getWorkStatus() == WorkStatusEnum.RETURNED.getCode()
                || workDeal.getWorkStatus() == WorkStatusEnum.CANCELED.getCode()
                || workDeal.getWorkStatus() == WorkStatusEnum.TO_DISPATCH.getCode())) {
            throw new AppException("[" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "]的工单不允许" +
                    "服务商客服修改！");
        }
        // 先新增客户、设备网点基础数据
        Long customId = workRequestDto.getCustomId();
        String customCorpName = StrUtil.trimToEmpty(workRequestDto.getCustomCorpName());
        if (LongUtil.isZero(customId) && StrUtil.isNotBlank(customCorpName)) {
            // 添加客户关系
            customId = this.addDemanderCustom(workRequestDto, userInfo);
            workRequestDto.setCustomId(customId);
        }
        Long deviceBranchId = workRequestDto.getDeviceBranch();
        String deviceBranchName = StrUtil.trimToEmpty(workRequestDto.getDeviceBranchName());
        if (LongUtil.isZero(deviceBranchId) && StrUtil.isNotBlank(deviceBranchName)) {
            deviceBranchId = this.addDeviceBranch(workRequestDto, userInfo, reqParam);
            workRequestDto.setDeviceBranch(deviceBranchId);
        }
        workRequestDto.setSerial(StrUtil.trimToEmpty(workRequestDto.getSerials()));
        workRequestDto.setDeviceCode(StrUtil.trimToEmpty(workRequestDto.getDeviceCodes()));
        workRequestDto.setLon(null);
        workRequestDto.setLat(null);
        workRequestDto.setCreator(null);
        workRequestDto.setCreateTime(null);
        BeanUtils.copyProperties(workRequestDto, workRequest);
//        workRequest.setLon(reqParam.getLon());
//        workRequest.setLat(reqParam.getLat());
//        workRequest.setCreator(userInfo.getUserId());
//        workRequest.setCreateTime(DateUtil.date());

        List<FaultType> faultTypeList = workRequestDto.getFaultTypeList();
        if (CollectionUtil.isNotEmpty(faultTypeList)) {
            StringBuilder faultTypes = new StringBuilder(16);
            for (FaultType faultType : faultTypeList) {
                if (faultTypes.length() > 0) {
                    faultTypes.append(",");
                }
                faultTypes.append(faultType.getId());
            }
            workRequest.setFaultType(faultTypes.toString());
        }

        Long resubmitWorkId = this.modWorkLogic(workRequestDto, workRequest, workDeal, userInfo, reqParam);

        // 修改工单费用
        WorkFee workFee = this.workFeeService.getById(workRequestDto.getWorkId());
        if (workFee == null) {
            workFee = new WorkFee();
            workFee.setWorkId(workRequestDto.getWorkId());
        }
        workFee.setBasicServiceFee(workRequestDto.getBasicServiceFee() == null ? BigDecimal.ZERO : workRequestDto.getBasicServiceFee());
        // 已完成的工单且费用未确认通过的，需要修改工单收费
        if ((WorkStatusEnum.CLOSED.getCode() == workDeal.getWorkStatus() ||
                WorkStatusEnum.TO_EVALUATE.getCode() == workDeal.getWorkStatus())
                && !FeeConfirmStatusEnum.CONFIRM_PASS.getCode().equals(workDeal.getFeeConfirmStatus())) {
            WorkDto workDto = new WorkDto();
            WorkFinish workFinish = this.workFinishService.getById(workRequestDto.getWorkId());
            BeanUtils.copyProperties(workRequestDto, workDto);
            BeanUtils.copyProperties(workDeal, workDto);
            workDto.setServiceItem(workFinish.getServiceItem());
            workFee.setAssortFee(this.workFeeDetailService.addAssortFeeDetail(workDto, userInfo.getUserId()));
        }
        workFee.setTotalFee(this.workFeeService.getTotalFee(workFee));
        this.workFeeService.saveOrUpdate(workFee);

        // 删除临时文件表数据
        if (CollectionUtil.isNotEmpty(workRequestDto.getNewFileIdList())) {
            this.fileFeignService.deleteFileTemporaryByFileIdList(workRequestDto.getNewFileIdList());
        }

        //删除选择的文件
        if (CollectionUtil.isNotEmpty(workRequestDto.getDeleteFileIdList())) {
            this.fileFeignService.deleteFileList(workRequestDto.getDeleteFileIdList());
        }

        return resubmitWorkId;
    }

    /**
     * 处理编辑工单逻辑
     *
     * @param workRequestDto
     * @param workRequest
     * @param workDeal
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/3 14:46
     */
    private Long modWorkLogic(WorkRequestDto workRequestDto, WorkRequest workRequest, WorkDeal workDeal,
                              UserInfo userInfo, ReqParam reqParam) {
        Long resubmitWorkId = 0L;
        if (workDeal.getWorkStatus() == WorkStatusEnum.RETURNED.getCode()) {
            // 已退单重新提单，发起一个新工单
            if (LongUtil.isNotZero(workRequest.getResubmitWorkId())) {
                throw new AppException("该工单已重新提单，不可重复操作！");
            }
            workRequestDto.setIfResubmitWork(true);
            List<Long> workIdList = this.addWorkRequest(workRequestDto, userInfo, reqParam);
            String resubmitWorkCode = "";
            if (CollectionUtil.isNotEmpty(workIdList)) {
                WorkRequest dbRequest = workRequestService.getById(workIdList.get(0));
                resubmitWorkCode = dbRequest != null ? dbRequest.getWorkCode() : "";
                resubmitWorkId = workIdList.get(0);
            }
            workRequestDto.setResubmitWorkCode(resubmitWorkCode);
            workRequestDto.setWorkId(workRequest.getWorkId());

            // 已退单的工单记录重提单的工单ID
            WorkRequest oldRequest = new WorkRequest();
            oldRequest.setWorkId(workRequest.getWorkId());
            oldRequest.setResubmitWorkId(resubmitWorkId);
            workRequestService.updateById(oldRequest);

            // 已退单的工单置为委托商确认通过
            WorkDeal oldWorkDeal = new WorkDeal();
            WorkCheck workCheck = new WorkCheck();
            if (!ServiceConfirmStatusEnum.CONFIRM_PASS.getCode().equals(workDeal.getFinishConfirmStatus())) {
                oldWorkDeal.setWorkId(workRequest.getWorkId());
                workCheck.setWorkId(workRequest.getWorkId());
                oldWorkDeal.setFinishConfirmStatus(ServiceConfirmStatusEnum.UN_CONFIRM.getCode());
                oldWorkDeal.setFinishConfirmTime(DateUtil.date());
                workCheck.setFinishConfirmStatus(ServiceConfirmStatusEnum.UN_CONFIRM.getCode());
                workCheck.setFinishConfirmUser(userInfo.getUserId());
                workCheck.setFinishConfirmTime(DateUtil.date());
                workCheck.setFinishConfirmNote("重新提单，自动确认通过");
                workDealService.updateById(oldWorkDeal);
                workCheckService.updateById(workCheck);
            }

            // 添加操作记录
            this.addWorkOperateByResubimt(workRequestDto, userInfo, reqParam);
        } else {
            // 服务商客服修改工单
            workRequestService.updateById(workRequest);
            WorkDeal entity = new WorkDeal();
            entity.setDeviceId(workRequestDto.getDeviceId());
            entity.setWorkId(workRequestDto.getWorkId());
            entity.setBookTimeBegin(workRequestDto.getBookTimeBegin());
            entity.setBookTimeEnd(workRequestDto.getBookTimeEnd());
            workDealService.updateById(entity);
            customFieldDataService.modCustomFieldDataList(workRequestDto.getWorkId(),
                    workRequestDto.getCustomFieldDataList());
            // 已完成的工单需同步设备档案信息
            if (workDeal.getWorkStatus() == WorkStatusEnum.TO_EVALUATE.getCode()
                    || workDeal.getWorkStatus() == WorkStatusEnum.CLOSED.getCode()) {
                DeviceInfoDto deviceInfoDto = this.editDeviceInfoByMod(workRequestDto, workDeal, userInfo);
                try {
                    if (LongUtil.isZero(workRequestDto.getModel()) && deviceInfoDto != null) {
                        workRequest.setModel(deviceInfoDto.getModelId());
                    }
                    workRequestService.updateById(workRequest);

                    if (deviceInfoDto != null && LongUtil.isNotZero(deviceInfoDto.getDeviceId())) {
                        workDeal = new WorkDeal();
                        workDeal.setWorkId(workRequestDto.getWorkId());
                        workDeal.setDeviceId(deviceInfoDto.getDeviceId());
                        workDealService.updateById(workDeal);
                    }
                } catch (Exception e) {
                    log.error("客服重新修改工单时，更新工单信息失败：{}", e);
                }
            }
            workRequestDto.setWorkStatus(workDeal.getWorkStatus());
            // 添加操作记录
            this.addWorkOperateByMod(workRequestDto, userInfo, reqParam);
        }
        return resubmitWorkId;
    }

    /**
     * 客服修改已完成的工单时同步设备档案信息
     * 若存在，则修改设备档案并返回设备号
     * 若不存在，则新增设备档案并返回设备号
     *
     * @param workRequestDto
     * @param workDeal
     * @param userInfo
     * @return
     * @author zgpi
     * @date 2020/3/3 15:00
     **/
    private DeviceInfoDto editDeviceInfoByMod(WorkRequestDto workRequestDto, WorkDeal workDeal, UserInfo userInfo) {
        Long model = workRequestDto.getModel();
        String serial = StrUtil.trimToEmpty(workRequestDto.getSerial());
        if (LongUtil.isZero(model) && StrUtil.isBlank(serial)) {
            return null;
        }
        DeviceInfoDto filter = new DeviceInfoDto();
        filter.setDemanderCorp(workRequestDto.getDemanderCorp());
        filter.setWorkId(workRequestDto.getWorkId());
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
        deviceInfoDto = this.transferDeviceInfo(deviceInfoDto, workRequestDto, workDeal, userInfo);
        Result<DeviceInfoDto> editDeviceResult = deviceFeignService.editDeviceInfo(JsonUtil.toJson(deviceInfoDto));
        if (editDeviceResult != null) {
            if (editDeviceResult.getCode() == Result.SUCCESS) {
                deviceInfoDto = editDeviceResult.getData();
            } else {
                throw new AppException(editDeviceResult.getMsg());
            }
        } else {
            throw new AppException("修改工单时，同步设备档案信息失败！");
        }
        return deviceInfoDto;
    }

    /**
     * 转换设备档案对象
     *
     * @param workRequestDto
     * @param workDeal
     * @param userInfo
     * @return
     * @author zgpi
     * @date 2020/3/3 14:54
     */
    private DeviceInfoDto transferDeviceInfo(DeviceInfoDto deviceInfoDto,
                                             WorkRequestDto workRequestDto,
                                             WorkDeal workDeal,
                                             UserInfo userInfo) {
        if (deviceInfoDto == null) {
            deviceInfoDto = new DeviceInfoDto();
        }
        deviceInfoDto.setDemanderCorp(workRequestDto.getDemanderCorp());
        deviceInfoDto.setServiceCorp(workRequestDto.getServiceCorp());
        deviceInfoDto.setSmallClassId(workRequestDto.getSmallClass());
        deviceInfoDto.setSpecificationId(workRequestDto.getSpecification());
        deviceInfoDto.setBrandId(workRequestDto.getBrand());
        deviceInfoDto.setModelId(workRequestDto.getModel());
        deviceInfoDto.setModelName(workRequestDto.getModelName());
        deviceInfoDto.setSerial(workRequestDto.getSerial());
        deviceInfoDto.setDeviceCode(workRequestDto.getDeviceCode());
        deviceInfoDto.setCustomId(workRequestDto.getCustomId());
        deviceInfoDto.setCustomCorp(workRequestDto.getCustomCorp());
        deviceInfoDto.setBranchId(workRequestDto.getDeviceBranch());
        deviceInfoDto.setDistrict(workRequestDto.getDistrict());
        deviceInfoDto.setContactName(workRequestDto.getContactName());
        deviceInfoDto.setContactPhone(workRequestDto.getContactPhone());
        deviceInfoDto.setAddress(workRequestDto.getAddress());
        deviceInfoDto.setServiceBranch(workDeal.getServiceBranch());
        deviceInfoDto.setEngineer(workDeal.getEngineer());
        deviceInfoDto.setOperator(userInfo.getUserId());
        deviceInfoDto.setWorkId(workRequestDto.getWorkId());
        return deviceInfoDto;
    }

    /**
     * 获得工单详情
     *
     * @param workId   工单编号
     * @param reqParam 公共参数
     * @return 工单详情
     * @author zgpi
     * @date 2019/10/15 9:51 上午
     **/
    @Override
    public WorkDto findWorkDetail(Long workId, UserInfo userInfo, ReqParam reqParam) {
        WorkRequest workRequest = workRequestService.getById(workId);
        WorkDeal workDeal = workDealService.getById(workId);
        WorkCheck workCheck = workCheckService.getById(workId);
        WorkDto workDto = new WorkDto();
        BeanUtils.copyProperties(workRequest, workDto);
        BeanUtils.copyProperties(workDeal, workDto);
        if (workCheck != null) {
            BeanUtils.copyProperties(workCheck, workDto);
        }
        workDto.setWorkStatusName(WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()));
        // 姓名
        List<Long> userIdList = new ArrayList<>();

        // 获得已关注工单集合
        List<WorkAttention> workAttentionlist = workAttentionService.queryByWorkIdAndCorpId(workId, reqParam.getCorpId());
        if (CollectionUtil.isNotEmpty(workAttentionlist)) {
            for (WorkAttention workAttention : workAttentionlist) {
                if (workAttention.getWorkId().equals(workId)) {
                    workDto.setIsAttention(workAttention.getOperateType());
                }
            }
        }

        // 获得已技术支持的工单集合
        List<WorkSupportDto> workSupportlist = workSupportService.queryByWorkIdAndCorpId(workId, reqParam.getCorpId());
        if (CollectionUtil.isNotEmpty(workSupportlist)) {
            workDto.setWorkSupportList(workSupportlist);
            for (WorkSupportDto workSupportDto : workSupportlist) {
                workDto.setIsSupport("N");
                if (workSupportDto.getSupportType().equals("Y")) {
                    workDto.setIsSupport("Y");
                }
            }
        }

        // 获得工单跟进记录的集合
        List<WorkFollowDto> workFollowDtolist = workFollowService.queryByWorkIdAndCorpId(workId, reqParam.getCorpId());
        if (CollectionUtil.isNotEmpty(workFollowDtolist)) {
            workDto.setWorkFollowList(workFollowDtolist);
            for (WorkFollowDto workFollowDto : workFollowDtolist) {
                userIdList.add(workFollowDto.getOperator());
            }
        }
        // 获取委托商结算状态
        workDto.setSettleDemanderStatusName(WorkSettleStatusEnum.lookup(workDto.getSettleDemanderStatus()));

        // 获得所有关联的企业ID列表
        List<Long> corpIdList = demanderServiceService.listRelatedCorpIdsByDemander(workDeal.getDemanderCorp());
        corpIdList.add(workDeal.getServiceCorp());
        corpIdList.add(workRequest.getCustomCorp());

        WorkType workType = workTypeService.getById(workDto.getWorkType());
        if (workType != null) {
            workDto.setWorkTypeName(StrUtil.trimToEmpty(workType.getName()));
            workDto.setWorkSysType(workType.getSysType());
        }
        workDto.setServiceModeName(ServiceModeEnum.getNameByCode(workDeal.getServiceMode()));
        // 远程调用获得企业映射
        Result<Map<Long, String>> corpResult = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList));
        Map<Long, String> corpMap = corpResult.getData();
        corpMap = corpMap == null ? new HashMap<>() : corpMap;
        if (LongUtil.isNotZero(workDto.getDemanderCorp())) {
            workDto.setDemanderCorpName(StrUtil.trimToEmpty(corpMap.get(workDto.getDemanderCorp())));
        }
        if (LongUtil.isNotZero(workDto.getServiceCorp())) {
            workDto.setServiceCorpName(StrUtil.trimToEmpty(corpMap.get(workDto.getServiceCorp())));
        }
        if (LongUtil.isNotZero(workDto.getCustomCorp())) {
            workDto.setCustomCorpName(StrUtil.trimToEmpty(corpMap.get(workDto.getCustomCorp())));
        } else {
            DemanderCustom demanderCustom = demanderCustomService.getById(workDto.getCustomId());
            if (demanderCustom != null) {
                workDto.setCustomCorpName(StrUtil.trimToEmpty(demanderCustom.getCustomCorpName()));
            }
        }

        // 故障现象
        String faultTypes = StrUtil.trimToEmpty(workRequest.getFaultType());
        if (StrUtil.isNotBlank(faultTypes)) {
            Map<Integer, String> faultTypeMap = faultTypeService.mapIdAndName(workDeal.getDemanderCorp());
            List<FaultType> faultTypeList = new ArrayList<>();
            String[] faultTypeArray = faultTypes.split(",");
            StringBuilder types = new StringBuilder(32);
            for (String faultType : faultTypeArray) {
                if (types.length() > 0) {
                    types.append(",");
                }
                FaultType fault = new FaultType();
                fault.setId(Integer.parseInt(faultType));
                fault.setName(faultTypeMap.get(Integer.parseInt(faultType)));
                faultTypeList.add(fault);
                types.append(StrUtil.trimToEmpty(faultTypeMap.get(Integer.parseInt(faultType))));
            }
            workDto.setFaultType(types.toString());
            workDto.setFaultTypeList(faultTypeList);
        }

        // 维保方式
        workDto.setWarrantyModeName(WarrantyModeEnum.getNameByCode(workDto.getWarrantyMode()));

        ServiceBranch serviceBranch = serviceBranchService.getById(workDto.getServiceBranch());
        if (serviceBranch != null) {
            workDto.setServiceBranchName(serviceBranch.getBranchName());
        }

        if (LongUtil.isNotZero(workDto.getDeviceBranch())) {
            DeviceBranch deviceBranch = deviceBranchService.getById(workDto.getDeviceBranch());
            if (deviceBranch != null) {
                workDto.setDeviceBranchName(deviceBranchService.buildFullBranchName(deviceBranch));
            }
        }
        // 远程调用获得地区映射
        AreaDto areaDto = null;
        if (StrUtil.isNotBlank(workDto.getDistrict())) {
            Result areaResult = uasFeignService.findAreaByCode(workDto.getDistrict());
            if (areaResult.getCode() == Result.SUCCESS) {
                areaDto = JsonUtil.parseObject(JsonUtil.toJson(areaResult.getData()), AreaDto.class);
            }
        }
        if (areaDto != null) {
            workDto.setDistrictName(areaDto.getAreaName());
        }
        workDto.setPriorityName(WorkPriorityEnum.getNameByCode(workDto.getPriority()));
        // 远程调用获得设备小类映射
        DeviceSmallClassDto deviceSmallClassDto = null;
        if (LongUtil.isNotZero(workDto.getSmallClass())) {
            Result smallClassResult = deviceFeignService.findDeviceSmallClass(workDto.getSmallClass());
            if (smallClassResult.getCode() == Result.SUCCESS) {
                deviceSmallClassDto = JsonUtil.parseObject(JsonUtil.toJson(smallClassResult.getData()), DeviceSmallClassDto.class);
            }
        }
        if (deviceSmallClassDto != null) {
            workDto.setLargeClassId(deviceSmallClassDto.getLargeClassId());
            workDto.setLargeClassName(deviceSmallClassDto.getLargeClassName());
            workDto.setSmallClassName(deviceSmallClassDto.getName());
        }
        DeviceSpecificationDto deviceSpecificationDto = null;
        if (LongUtil.isNotZero(workDto.getSpecification())) {
            Result specificationResult = deviceFeignService.findDeviceSpecification(workDto.getSpecification());
            if (specificationResult.getCode() == Result.SUCCESS) {
                deviceSpecificationDto = JsonUtil.parseObject(JsonUtil.toJson(specificationResult.getData()), DeviceSpecificationDto.class);
            }
        }
        if (deviceSpecificationDto != null) {
            workDto.setSpecification(deviceSpecificationDto.getId());
            workDto.setSpecificationName(deviceSpecificationDto.getName());
        }
        DeviceBrandDto deviceBrandDto = null;
        if (LongUtil.isNotZero(workDto.getBrand())) {
            Result deviceBrandResult = deviceFeignService.findDeviceBrand(workDto.getBrand());
            if (deviceBrandResult.getCode() == Result.SUCCESS) {
                deviceBrandDto = JsonUtil.parseObject(JsonUtil.toJson(deviceBrandResult.getData()), DeviceBrandDto.class);
            }
        }
        if (deviceBrandDto != null) {
            workDto.setBrandName(deviceBrandDto.getName());
        }
        DeviceModelDto deviceModelDto = null;
        if (LongUtil.isNotZero(workDto.getModel())) {
            Result deviceModelResult = deviceFeignService.findDeviceModel(workDto.getModel());
            if (deviceModelResult.getCode() == Result.SUCCESS) {
                deviceModelDto = JsonUtil.parseObject(JsonUtil.toJson(deviceModelResult.getData()), DeviceModelDto.class);
            }
        }
        if (deviceModelDto != null) {
            workDto.setModelName(deviceModelDto.getName());
        }
        if (workDeal.getWorkStatus() == WorkStatusEnum.TO_CLAIM.getCode()) {
            WorkAssign workAssign = workAssignService.getOne(new QueryWrapper<WorkAssign>().eq("work_id", workDto.getWorkId())
                    .eq("enabled", "Y"));
            if (workAssign != null) {
                List<WorkAssignEngineerDto> assignEngineerDtoList = new ArrayList<>();
                List<Long> userIdList2 = new ArrayList<>();
                List<WorkAssignEngineer> assignEngineerList = workAssignEngineerService.list(
                        new QueryWrapper<WorkAssignEngineer>().eq("assign_id", workAssign.getAssignId()));
                for (WorkAssignEngineer workAssignEngineer : assignEngineerList) {
                    userIdList2.add(workAssignEngineer.getEngineerId());
                }
                Map<Long, String> nameMap = uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList2)).getData();
                for (WorkAssignEngineer workAssignEngineer : assignEngineerList) {
                    WorkAssignEngineerDto workAssignEngineerDto = new WorkAssignEngineerDto();
                    workAssignEngineerDto.setUserId(workAssignEngineer.getEngineerId());
                    workAssignEngineerDto.setUserName(nameMap.get(workAssignEngineer.getEngineerId()));
                    assignEngineerDtoList.add(workAssignEngineerDto);
                }
                workDto.setAssignEngineerList(assignEngineerDtoList);
            }
        }

        // 获取签到记录
        workDto.setWorkSignList(this.workSignService.listByWorkId(workId));

        List<Long> engineerList = new ArrayList<>();
        userIdList.add(workDto.getCreator());
        if (LongUtil.isNotZero(workDto.getEngineer())) {
            engineerList.add(workDto.getEngineer());
            if (StrUtil.isNotBlank(workDto.getTogetherEngineers())) {
                String[] togetherEngineers = workDto.getTogetherEngineers().split(",");
                for (String togetherEngineer : togetherEngineers) {
                    if (StrUtil.isNotBlank(togetherEngineer)) {
                        engineerList.add(Long.parseLong(togetherEngineer));
                    }
                }
            }
            userIdList.addAll(engineerList);
        }
        Result<Map<Long, String>> userMapResult = uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList));
        Map<Long, String> userMap = new HashMap<>();
        if (userMapResult != null && userMapResult.getCode() == Result.SUCCESS) {
            userMap = userMapResult.getData();
        }
        workDto.setEngineerName(StrUtil.trimToEmpty(userMap.get(workDto.getEngineer())));
        workDto.setCreatorName(StrUtil.trimToEmpty(userMap.get(workDto.getCreator())));
        List<EngineerDto> togetherEngineerList = new ArrayList<>();
        for (Long engineerId : engineerList) {
            // 排除掉处理人员
            if (!workDto.getEngineer().equals(engineerId)) {
                EngineerDto engineerDto = new EngineerDto();
                engineerDto.setEngineerId(engineerId);
                engineerDto.setEngineerName(StrUtil.trimToEmpty(userMap.get(engineerId)));
                togetherEngineerList.add(engineerDto);
            }
        }
        workDto.setTogetherEngineerList(togetherEngineerList);
        if (CollectionUtil.isNotEmpty(workFollowDtolist)) {
            for (WorkFollowDto workFollowDto : workFollowDtolist) {
                workFollowDto.setOperatorName(StrUtil.trimToEmpty(userMap.get(workFollowDto.getOperator())));
            }
        }
        workDto.setWarrantyName(WarrantyEnum.getNameByCode(workDto.getWarranty()));
        workDto.setSourceName(WorkSourceEnum.getNameByCode(workRequest.getSource()));
        workDto.setWorkOperateList(workOperateService.listWorkOperate(workDto.getWorkId()));
        // 获取工单费用信息
        workDto.setWorkFeeDto(this.workFeeService.getDtoById(workId));
        workDto.setWorkFeeDetailDtoList(this.workFeeDetailService.listByWorkId(workId));
        //获取完成信息
        WorkFinish workFinish = workFinishService.getById(workId);
        if (workFinish != null) {
            Map<Integer, String> serviceItemMap = this.serviceItemService.mapIdAndNameByCorp(workDeal.getServiceCorp());
            workDto.setFinishDescription(workFinish.getDescription());
            workDto.setServiceItem(workFinish.getServiceItem());
            workDto.setServiceItemName(StrUtil.trimToEmpty(
                    this.workDispatchServiceCorpService.getIntegerName(workFinish.getServiceItem(), serviceItemMap, ",")));
            if (ServiceModeEnum.REMOTE_SERVICE.getCode() == workDeal.getServiceMode()) {
                RemoteWay remoteWay = remoteWayService.getById(workFinish.getRemoteWay());
                workDto.setRemoteWayName(remoteWay == null ? "" : remoteWay.getName());
            }
            workDto.setUsedPartList(workWareUsedService.listByWorkId(workId));
            workDto.setFaultPartList(workWareRecycleService.listByWorkId(workId));
            workDto.setSignature(workFinish.getSignature());
            workDto.setFilesStatus(workFinish.getFilesStatus());
            workDto.setSignatureStatus(workFinish.getSignatureStatus());
            workDto.setSignatureDescription(workFinish.getSignatureDescription());
            // 获取换下备件邮寄单
//            workDto.setWorkPostDtoList(this.workPostService.listByWorkId(workId));
        }
        this.findFileList(workDto, workRequest, workFinish);
        // 服务状态
        if (IntUtil.isNotZero(workDto.getFinishCheckStatus())) {
            workDto.setFinishStatusName(ServiceCheckStatusEnum.getNameByCode(workDto.getFinishCheckStatus()));
        }
        if (IntUtil.isNotZero(workDto.getFinishConfirmStatus())) {
            workDto.setFinishStatusName(ServiceConfirmStatusEnum.getNameByCode(workDto.getFinishConfirmStatus()));
        }
        // 费用状态
        if (IntUtil.isNotZero(workDto.getFeeCheckStatus())) {
            workDto.setFeeStatusName(FeeCheckStatusEnum.getNameByCode(workDto.getFeeCheckStatus()));
        }
        if (IntUtil.isNotZero(workDto.getFeeConfirmStatus())) {
            workDto.setFeeStatusName(FeeConfirmStatusEnum.getNameByCode(workDto.getFeeConfirmStatus()));
        }
        // 审核状态
        workDto.setFinishCheckStatusName(StrUtil.trimToEmpty(ServiceCheckStatusEnum.getNameByCode(workDto.getFinishCheckStatus())));
        workDto.setFeeCheckStatusName(StrUtil.trimToEmpty(FeeCheckStatusEnum.getNameByCode(workDto.getFeeCheckStatus())));
        // 确认状态
        workDto.setFinishConfirmStatusName(StrUtil.trimToEmpty(ServiceConfirmStatusEnum.getNameByCode(workDto.getFinishConfirmStatus())));
        workDto.setFeeConfirmStatusName(StrUtil.trimToEmpty(FeeConfirmStatusEnum.getNameByCode(workDto.getFeeConfirmStatus())));

        workDto.setWorkFeeStatusName(StrUtil.trimToEmpty(WorkFeeStatusEnum.getNameByCode(workDto.getWorkFeeStatus())));

        // 获取自定义字段数据列表
        CustomFieldDataFilter customFieldDataFilter = new CustomFieldDataFilter();
        customFieldDataFilter.setFormId(workDto.getWorkId());
        List<CustomFieldData> customFieldDataList = customFieldDataService.queryCustomFieldData(customFieldDataFilter);
        List<CustomFieldDataDto> customFieldDtoList = new ArrayList<>();
        CustomFieldDataDto customFieldDataDto;
        CustomFieldDto customFieldDto;
        for (CustomFieldData customFieldData : customFieldDataList) {
            customFieldDataDto = new CustomFieldDataDto();
            BeanUtils.copyProperties(customFieldData, customFieldDataDto);
//            customFieldDataDto.setServiceCorp(customFieldService.findCustomFieldById(customFieldData.getFieldId()).getCorpId());
            if (reqParam.getCorpId().equals(workDeal.getServiceCorp())) {
                // 若当前企业是服务商则显示所有
                customFieldDtoList.add(customFieldDataDto);
            } else {
                // 若当前企业不是服务商则显示自己定义的
                customFieldDto = customFieldService.findCustomFieldById(customFieldData.getFieldId());
                if (customFieldDto != null && !reqParam.getCorpId().equals(workDeal.getServiceCorp())
                        && reqParam.getCorpId().equals(customFieldDto.getCorpId())) {
                    customFieldDtoList.add(customFieldDataDto);
                }
            }
        }
        workDto.setCustomFieldDataList(customFieldDtoList);
        workDto.setCurrentTime(DateUtil.date());
        return workDto;
    }

    /**
     * 分页查询当前用户的工单列表
     *
     * @param workFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/2/16 14:14
     */
    @Override
    public ListWrapper<WorkDto> queryUserWork(WorkFilter workFilter, UserInfo userInfo, ReqParam reqParam) {
        Page page = new Page(workFilter.getPageNum(), workFilter.getPageSize());
        workFilter.setUserId(userInfo.getUserId());
        workFilter.setCorpId(reqParam.getCorpId());
        this.findUserRight(workFilter, userInfo.getUserId(), reqParam.getCorpId());
        workFilter = this.addRightTypeList(workFilter);

        List<WorkDto> workDtoList = workRequestMapper.queryUserWork(page, workFilter);

        // 初始化基础数据
        Map<String, Object> baseMap = this.initBaseMap(workDtoList);
        // 增加附加属性
        this.addExtraAttributes(workDtoList, baseMap);

        return ListWrapper.<WorkDto>builder()
                .list(workDtoList)
                .total(page.getTotal())
                .build();
    }

    /**
     * 分页查询当前用户待办工单列表
     *
     * @param workFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/5/18 13:37
     **/
    @Override
    public ListWrapper<WorkDto> queryUserTodoWork(WorkFilter workFilter, UserInfo userInfo, ReqParam reqParam) {
        Page page = new Page(workFilter.getPageNum(), workFilter.getPageSize());
        workFilter.setUserId(userInfo.getUserId());
        workFilter.setCorpId(reqParam.getCorpId());
        this.findUserRight(workFilter, userInfo.getUserId(), reqParam.getCorpId());
        workFilter = this.addRightTypeList(workFilter);

        List<WorkDto> workDtoList = new ArrayList<>();
        if (!workFilter.getRightTypeList().contains(0)) {
            workDtoList = workRequestMapper.queryUserTodoWork(page, workFilter);
        }

        // 初始化基础数据
        Map<String, Object> baseMap = this.initBaseMap(workDtoList);
        // 增加附加属性
        this.addExtraAttributes(workDtoList, baseMap);

        return ListWrapper.<WorkDto>builder()
                .list(workDtoList)
                .total(page.getTotal())
                .build();
    }

    @Override
    public ListWrapper<WorkDto> queryDetailDto(WorkFilter workFilter) {
        Page page = new Page(workFilter.getPageNum(), workFilter.getPageSize());
        if (LongUtil.isZero(workFilter.getCustomCorp())) {
            return ListWrapper.<WorkDto>builder()
                    .list(null)
                    .total(0L)
                    .build();
        }
        List<WorkDto> workDtoList = workRequestMapper.queryWorkDetailDto(page, workFilter.getCustomCorp(),
                workFilter.getDemanderCorp());
        if (CollectionUtil.isNotEmpty(workDtoList)) {
            Map<Integer, String> faultTypeMap = faultTypeService.mapIdAndName(workFilter.getDemanderCorp());
            for (WorkDto workDto : workDtoList) {
                workDto.setServiceModeName(ServiceModeEnum.getNameByCode(workDto.getServiceMode()));
                workDto.setWarrantyName(WarrantyEnum.getNameByCode(workDto.getWarranty()));
                workDto.setWorkStatusName(WorkStatusEnum.getNameByCode(workDto.getWorkStatus()));
                // 故障现象
                String faultTypes = StrUtil.trimToEmpty(workDto.getFaultType());
                if (StrUtil.isNotBlank(faultTypes)) {
                    String[] faultTypeArray = faultTypes.split(",");
                    StringBuilder types = new StringBuilder(32);
                    for (String faultType : faultTypeArray) {
                        if (types.length() > 0) {
                            types.append(",");
                        }
                        types.append(StrUtil.trimToEmpty(faultTypeMap.get(Integer.parseInt(faultType))));
                    }
                    workDto.setFaultType(types.toString());
                }
            }
        }
        return ListWrapper.<WorkDto>builder()
                .list(workDtoList)
                .total(page.getTotal())
                .build();
    }

    /**
     * 委托商确认服务分页查询工单
     *
     * @param workFilter
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/1 13:43
     **/
    @Override
    public ListWrapper<WorkDto> queryServiceConfirm(WorkFilter workFilter, Long corpId) {
        if (LongUtil.isZero(workFilter.getDemanderCorp())) {
            workFilter.setDemanderCorp(corpId);
        }
        Page page = new Page(workFilter.getPageNum(), workFilter.getPageSize());
        List<WorkDto> workDtoList = workRequestMapper.queryServiceConfirm(page, workFilter);
        // 初始化基础数据
        Map<String, Object> baseMap = this.initBaseMap(workDtoList);
        // 增加附加属性
        this.addExtraAttributes(workDtoList, baseMap);

        return ListWrapper.<WorkDto>builder()
                .list(workDtoList)
                .total(page.getTotal())
                .build();
    }

    /**
     * 委托商确认费用分页查询工单
     *
     * @param workFilter
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/1 13:43
     **/
    @Override
    public ListWrapper<WorkDto> queryFeeConfirm(WorkFilter workFilter, Long corpId) {
        if (LongUtil.isZero(workFilter.getDemanderCorp())) {
            workFilter.setDemanderCorp(corpId);
        }
        Page page = new Page(workFilter.getPageNum(), workFilter.getPageSize());
        List<WorkDto> workDtoList = workRequestMapper.queryFeeConfirm(page, workFilter);
        // 初始化基础数据
        Map<String, Object> baseMap = this.initBaseMap(workDtoList);
        // 增加附加属性
        this.addExtraAttributes(workDtoList, baseMap);

        // 获取工单费用
        List<Long> workIdList = new ArrayList<>();
        workDtoList.forEach(workDto -> workIdList.add(workDto.getWorkId()));
        Map<Long, WorkFeeDto> feeMap = this.workFeeService.mapWorkIdAndFeeDto(workIdList);
        workDtoList.forEach(workDto -> {
            WorkFeeDto workFeeDto = feeMap.containsKey(workDto.getWorkId()) ? feeMap.get(workDto.getWorkId()) : new WorkFeeDto();
            workFeeDto.setBasicServiceFee((workFeeDto.getBasicServiceFee() == null ? BigDecimal.ZERO : workFeeDto.getBasicServiceFee())
                    .add(workFeeDto.getAssortBasicFee() == null ? BigDecimal.ZERO : workFeeDto.getAssortBasicFee()));
            workDto.setWorkFeeDto(workFeeDto);
        });

        return ListWrapper.<WorkDto>builder()
                .list(workDtoList)
                .total(page.getTotal())
                .build();
    }

    @Override
    public ListWrapper<WorkDto> queryServiceCheck(WorkFilter workFilter, Long userId, Long corpId) {
        if (LongUtil.isZero(workFilter.getServiceCorp())) {
            workFilter.setServiceCorp(corpId);
        }
        workFilter.setCorpId(corpId);
        workFilter.setUserId(userId);
        Page page = new Page(workFilter.getPageNum(), workFilter.getPageSize());
        Long serviceCheckRightId = RightConstants.FINISH_SERVICE_CHECK; // 客服审核
        if (rightCompoService.hasRight(corpId, userId, serviceCheckRightId)) {
            workFilter.setIfServiceCheck("Y");
        }
        Long managerCheckRightId = RightConstants.FINISH_MANAGER_CHECK; // 客户经理审核
        if (rightCompoService.hasRight(corpId, userId, managerCheckRightId)) {
            workFilter.setIfManagerCheck("Y");
            workFilter.setDemanderCorpList(demanderServiceManagerService.listDemanderCorpByManager(corpId, userId));
        }
        this.listNotInFinishConfirmStatus(workFilter);
        List<WorkDto> workDtoList = workRequestMapper.queryServiceCheck(page, workFilter);
        // 初始化基础数据
        Map<String, Object> baseMap = this.initBaseMap(workDtoList);
        // 增加附加属性
        this.addExtraAttributes(workDtoList, baseMap);

        return ListWrapper.<WorkDto>builder()
                .list(workDtoList)
                .total(page.getTotal())
                .build();
    }

    /**
     * 审核费用查询工单记录
     *
     * @param workFilter
     * @param corpId
     * @return
     */
    @Override
    public ListWrapper<WorkDto> queryFeeCheck(WorkFilter workFilter, Long userId, Long corpId) {
        if (LongUtil.isZero(workFilter.getServiceCorp())) {
            workFilter.setServiceCorp(corpId);
        }
        workFilter.setCorpId(corpId);
        workFilter.setUserId(userId);
        Page page = new Page(workFilter.getPageNum(), workFilter.getPageSize());
        Long serviceCheckRightId = RightConstants.FEE_SERVICE_CHECK; // 客服审核
        if (rightCompoService.hasRight(corpId, userId, serviceCheckRightId)) {
            workFilter.setIfServiceCheck("Y");
        }
        Long managerCheckRightId = RightConstants.FEE_MANAGER_CHECK; // 客户经理审核
        if (rightCompoService.hasRight(corpId, userId, managerCheckRightId)) {
            workFilter.setIfManagerCheck("Y");
            workFilter.setDemanderCorpList(demanderServiceManagerService.listDemanderCorpByManager(corpId, userId));
        }
        this.listNotInFeeConfirmStatus(workFilter);
        List<WorkDto> workDtoList = workRequestMapper.queryFeeCheck(page, workFilter);
        // 初始化基础数据
        Map<String, Object> baseMap = this.initBaseMap(workDtoList);
        // 增加附加属性
        this.addExtraAttributes(workDtoList, baseMap);
        // 获取工单费用
        List<Long> workIdList = new ArrayList<>();
        workDtoList.forEach(workDto -> workIdList.add(workDto.getWorkId()));
        Map<Long, WorkFeeDto> feeMap = this.workFeeService.mapWorkIdAndFeeDto(workIdList);
        workDtoList.forEach(workDto -> {
            WorkFeeDto workFeeDto = feeMap.containsKey(workDto.getWorkId()) ? feeMap.get(workDto.getWorkId()) : new WorkFeeDto();
            workFeeDto.setBasicServiceFee((workFeeDto.getBasicServiceFee() == null ? BigDecimal.ZERO : workFeeDto.getBasicServiceFee())
                    .add(workFeeDto.getAssortBasicFee() == null ? BigDecimal.ZERO : workFeeDto.getAssortBasicFee()));
            workDto.setWorkFeeDto(workFeeDto);
        });

        return ListWrapper.<WorkDto>builder()
                .list(workDtoList)
                .total(page.getTotal())
                .build();
    }

    /**
     * 扫一扫建单时添加消息队列
     *
     * @param workIdList
     * @return
     * @author zgpi
     * @date 2020/2/26 20:28
     */
    @Override
    public void addMessageQueueByScanCreate(List<Long> workIdList) {
        Map<String, Object> msg;
        for (Long workId : workIdList) {
            msg = new HashMap<>(1);
            msg.put("workId", workId);
            mqSenderUtil.sendMessage(WorkMqTopic.SCAN_CREATE_WORK, JsonUtil.toJson(msg));
        }
    }

    /**
     * 建单时添加消息队列
     *
     * @param workIdList
     * @return
     * @author zgpi
     * @date 2020/2/26 20:28
     */
    @Override
    public void addMessageQueueByCreate(List<Long> workIdList) {
        Map<String, Object> msg;
        for (Long workId : workIdList) {
            msg = new HashMap<>(1);
            msg.put("workId", workId);
            mqSenderUtil.sendMessage(WorkMqTopic.CREATE_WORK, JsonUtil.toJson(msg));
        }
    }

    /**
     * 修改工单时添加消息队列
     *
     * @param workId
     * @return
     * @author zgpi
     * @date 2020/2/26 20:28
     */
    @Override
    public void addMessageQueueByMod(Long workId) {
        if (LongUtil.isNotZero(workId)) {
            Map<String, Object> msg = new HashMap<>(1);
            msg.put("workId", workId);
            mqSenderUtil.sendMessage(WorkMqTopic.MOD_WORK, JsonUtil.toJson(msg));
        }
    }

    /**
     * 获得工单附件
     *
     * @param workDto
     * @param workRequest
     * @param workFinish
     * @return
     * @author zgpi
     * @date 2019/11/7 18:51
     **/
    private void findFileList(WorkDto workDto, WorkRequest workRequest, WorkFinish workFinish) {
        List<Long> workFileIdList = new ArrayList<>(); // 工单附件编号列表
        List<Long> finishFileIdList = new ArrayList<>(); // 工单完成附件编号列表
        List<Long> fileIdList = new ArrayList<>();
        if (workFinish != null && StrUtil.isNotBlank(workFinish.getFiles())) {
            List<Long> list = Arrays.asList(workFinish.getFiles().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            fileIdList.addAll(list);
            finishFileIdList.addAll(list);
        }
        if (workRequest != null && StrUtil.isNotBlank(workRequest.getFiles())) {
            List<Long> list = Arrays.asList(workRequest.getFiles().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            fileIdList.addAll(list);
            workFileIdList.addAll(list);
        }
        Result fileResult = fileFeignService.listFileDtoByIdList(JsonUtil.toJson(fileIdList));
        List<FileInfoDto> fileInfoDtoList = null;
        if (fileResult.getCode() == Result.SUCCESS) {
            fileInfoDtoList = JsonUtil.parseArray(JsonUtil.toJson(fileResult.getData()), FileInfoDto.class);
        }
        List<FileInfoDto> workFileList = new ArrayList<>();
        List<FileInfoDto> finishFileList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(fileInfoDtoList)) {
            for (FileInfoDto fileInfoDto : fileInfoDtoList) {
                if (workFileIdList.contains(fileInfoDto.getFileId())) {
                    workFileList.add(fileInfoDto);
                }
                if (finishFileIdList.contains(fileInfoDto.getFileId())) {
                    finishFileList.add(fileInfoDto);
                }
            }
        }
        if (workDto.getWorkSysType() == null || workDto.getWorkSysType() == 0) {
            return;
        }
        FileConfigFilter fileConfigFilter = new FileConfigFilter();
        fileConfigFilter.setWorkType(workDto.getWorkSysType());
        fileConfigFilter.setWorkId(workDto.getWorkId());
        fileConfigFilter.setServiceCorp(workDto.getServiceCorp());
        fileConfigFilter.setDemanderCorp(workDto.getDemanderCorp());
        fileConfigFilter.setFormType(FileConfigFormTypeEnum.FINISH.getCode());
        List<WorkFilesDto> workFiles = this.workFilesService.getWorkFileList(fileConfigFilter);
        workDto.setFileList(workFileList);
        workDto.setFinishFileList(finishFileList);
        workDto.setFileGroupList(workFiles);
    }

    /**
     * 获得用户权限
     *
     * @param workFilter 查询条件
     * @param userId     当前用户
     * @param corpId     公共参数
     * @return 空
     * @author zgpi
     * @date 2019/12/10 09:12
     **/
    private void findUserRight(WorkFilter workFilter, Long userId, Long corpId) {
        workFilter.setUserId(userId);
        workFilter.setCorpId(corpId);
        Long rightId = RightConstants.WORK_QUERY;
        // 范围权限列表
        List<RightScopeDto> rightScopeDtoList = dataScopeCompoService.listUserRightScope(corpId, userId, rightId);
        workFilter.setRightScopeList(rightScopeDtoList);
    }

    /**
     * 获得客户经理审核权限
     *
     * @param workFilter 查询条件
     * @param userId     当前用户
     * @param corpId     公共参数
     * @return 空
     * @author zgpi
     * @date 2020/6/17 10:08
     **/
    private void findManagerCheckRight(WorkFilter workFilter, Long userId, Long corpId) {
        workFilter.setUserId(userId);
        workFilter.setCorpId(corpId);
        Long rightId = RightConstants.FINISH_MANAGER_CHECK;
        if (rightCompoService.hasRight(corpId, userId, rightId)) {
            workFilter.setIfManagerCheck("Y");
        }
    }

    /**
     * 初始化MAP数据
     *
     * @param workDtoList 工单列表
     * @return 空
     * @author zgpi
     * @date 2019/12/10 08:53
     **/
    private Map<String, Object> initBaseMap(List<WorkDto> workDtoList) {
        Map<String, Object> baseMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(workDtoList)) {
            // 企业列表
            Set<Long> corpIdSet = new HashSet<>();
            Set<Long> customIdSet = new HashSet<>();
            Set<String> areaCodeSet = new HashSet<>();
            Set<Long> userIdSet = new HashSet<>();
            Set<Long> specificationSet = new HashSet<>();
            Set<Long> workIdSet = new HashSet<>();
            for (WorkDto workDto : workDtoList) {
                corpIdSet.add(workDto.getServiceCorp());
                corpIdSet.add(workDto.getDemanderCorp());
                corpIdSet.add(workDto.getCustomCorp());
                customIdSet.add(workDto.getCustomId());
                if (StrUtil.isNotBlank(workDto.getDistrict())) {
                    if (StrUtil.isNotBlank(workDto.getDistrict()) && workDto.getDistrict().length() >= 2) {
                        areaCodeSet.add(workDto.getDistrict().substring(0, 2));
                    }
                    if (StrUtil.isNotBlank(workDto.getDistrict()) && workDto.getDistrict().length() >= 4) {
                        areaCodeSet.add(workDto.getDistrict().substring(0, 4));
                    }
                    if (StrUtil.isNotBlank(workDto.getDistrict()) && workDto.getDistrict().length() >= 6) {
                        areaCodeSet.add(workDto.getDistrict().substring(0, 6));
                    }
                }
                if (LongUtil.isNotZero(workDto.getEngineer())) {
                    userIdSet.add(workDto.getEngineer());
                }
                // 创建人
                if (LongUtil.isNotZero(workDto.getCreator())) {
                    userIdSet.add(workDto.getCreator());
                }
                // 设备规格
                if (LongUtil.isNotZero(workDto.getSpecification())) {
                    specificationSet.add(workDto.getSpecification());
                }

                if (LongUtil.isNotZero(workDto.getWorkId())) {
                    workIdSet.add(workDto.getWorkId());
                }
                // 协同工程师
                if (StrUtil.isNotBlank(workDto.getTogetherEngineers())) {
                    List<String> togetherEngineers = Arrays.asList(workDto.getTogetherEngineers().split(","));
                    for (String togetherEngineer : togetherEngineers) {
                        if (StrUtil.isNotBlank(togetherEngineer) && !togetherEngineer.equals(' ')) {
                            userIdSet.add(Long.parseLong(togetherEngineer));
                        }
                    }
                }
                // 审核人
                if (LongUtil.isNotZero(workDto.getFinishCheckUser())) {
                    userIdSet.add(workDto.getFinishCheckUser());
                }
                if (LongUtil.isNotZero(workDto.getFeeCheckUser())) {
                    userIdSet.add(workDto.getFeeCheckUser());
                }
                // 确认人
                if (LongUtil.isNotZero(workDto.getFinishConfirmUser())) {
                    userIdSet.add(workDto.getFinishConfirmUser());
                }
                if (LongUtil.isNotZero(workDto.getFeeConfirmUser())) {
                    userIdSet.add(workDto.getFeeConfirmUser());
                }
            }

            // 添加工单流转表处理人信息
            List<WorkTransfer> workTransferList = workTransferService.queryWorkTransfer(workIdSet);
            Map<Long, WorkTransfer> workTransferMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(workTransferList)) {
                for (WorkTransfer workTransfer : workTransferList) {
                    if (LongUtil.isNotZero(workTransfer.getOperator())) {
                        userIdSet.add(workTransfer.getOperator());
                        workTransferMap.put(workTransfer.getWorkId(), workTransfer);
                    }
                }
                baseMap.put("workTransferMap", workTransferMap);
            }

            // 处理信息
            List<WorkDeal> workDealList = workDealService.queryWorkDeal(workIdSet);
            Map<Long, WorkDeal> workDealMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(workDealList)) {
                for (WorkDeal workDeal : workDealList) {
                    if (workDeal != null) {
                        workDealMap.put(workDeal.getWorkId(), workDeal);
                    }
                }
                baseMap.put("workDealMap", workDealMap);
            }

            List<Long> corpIdList = new ArrayList<>(corpIdSet);
            List<Long> customIdList = new ArrayList<>(customIdSet);
            List<String> areaCodeList = new ArrayList<>(areaCodeSet);
            List<Long> userIdList = new ArrayList<>(userIdSet);
            List<Long> specificationList = new ArrayList<>(specificationSet);


            Map<Long, String> specificationMap;
            Result<Map<Long, String>> specificationResult = deviceFeignService.mapSpecificationAndNameByIdList(JsonUtil.toJson(specificationList));
            if (specificationResult.getCode() == Result.SUCCESS) {
                specificationMap = specificationResult.getData();
                specificationMap = specificationMap == null ? new HashMap<>(0) : specificationMap;
                baseMap.put("specificationMap", specificationMap);
            }

            Map<Long, String> corpMap;
            Map<Integer, String> workTypeMap = workTypeService.mapWorkType();
            baseMap.put("workTypeMap", workTypeMap);
            Result<Map<Long, String>> corpResult = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList));
            if (corpResult.getCode() == Result.SUCCESS) {
                corpMap = corpResult.getData();
                corpMap = corpMap == null ? new HashMap<>(0) : corpMap;
                baseMap.put("corpMap", corpMap);
            }

            Map<Long, String> userMap;
            Result<Map<Long, String>> userResult = uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList));
            if (userResult.getCode() == Result.SUCCESS) {
                userMap = userResult.getData();
                userMap = userMap == null ? new HashMap<>(0) : userMap;
                baseMap.put("userMap", userMap);
            }

            Map<Long, String> demanderCustomCorpNameMap = demanderCustomService.mapIdAndCustomNameByIdList(customIdList);
            demanderCustomCorpNameMap = demanderCustomCorpNameMap == null ? new HashMap<>(0) : demanderCustomCorpNameMap;
            baseMap.put("demanderCustomCorpNameMap", demanderCustomCorpNameMap);

            Map<String, String> areaMap;
            Result<Map<String, String>> areaResult = uasFeignService.mapAreaCodeAndNameByCodeList(JsonUtil.toJson(areaCodeList));
            if (areaResult.getCode() == Result.SUCCESS) {
                areaMap = areaResult.getData();
                areaMap = areaMap == null ? new HashMap<>(0) : areaMap;
                baseMap.put("areaMap", areaMap);
            }

            Map<Long, String> smallClassMap;
            Result<Map<Long, String>> smallClassResult = deviceFeignService.mapSmallClassByCorpIdList(corpIdList);
            if (smallClassResult.getCode() == Result.SUCCESS) {
                smallClassMap = smallClassResult.getData();
                smallClassMap = smallClassMap == null ? new HashMap<>(0) : smallClassMap;
                baseMap.put("smallClassMap", smallClassMap);
            }

            Map<Long, String> deviceBrandMap;
            Result<Map<Long, String>> deviceBrandResult = deviceFeignService.mapDeviceBrandByCorpIdList(corpIdList);
            if (deviceBrandResult.getCode() == Result.SUCCESS) {
                deviceBrandMap = deviceBrandResult.getData();
                deviceBrandMap = deviceBrandMap == null ? new HashMap<>(0) : deviceBrandMap;
                baseMap.put("deviceBrandMap", deviceBrandMap);
            }

            Map<Long, String> deviceModelMap;
            Result<Map<Long, String>> deviceModelResult = deviceFeignService.mapDeviceModelByCorpIdList(corpIdList);
            if (deviceModelResult.getCode() == Result.SUCCESS) {
                deviceModelMap = deviceModelResult.getData();
                deviceModelMap = deviceModelMap == null ? new HashMap<>(0) : deviceModelMap;
                baseMap.put("deviceModelMap", deviceModelMap);
            }
            Map<Long, String> serviceBranchMap = serviceBranchService.mapServiceBranchByCorpIdList(corpIdList);
            baseMap.put("serviceBranchMap", serviceBranchMap);
            Map<Long, String> deviceBranchMap = deviceBranchService.mapDeviceBranchByCorpIdList(corpIdList);
            baseMap.put("deviceBranchMap", deviceBranchMap);
            Map<Long, String> customDeviceBranchMap = deviceBranchService.mapCustomDeviceBranchByCustomIdList(customIdList);
            baseMap.put("customDeviceBranchMap", customDeviceBranchMap);
        }
        return baseMap;
    }

    /**
     * 添加客户关系
     *
     * @param workRequestDto 工单请求数据
     * @param userInfo       当前用户
     * @return 客户编号
     * @author zgpi
     * @date 2020/1/16 14:22
     **/
    @Override
    public Long addDemanderCustom(WorkRequestDto workRequestDto, UserInfo userInfo) {
        DemanderCustomDto demanderCustomDto = new DemanderCustomDto();
        demanderCustomDto.setDemanderCorp(workRequestDto.getDemanderCorp());
        demanderCustomDto.setCustomCorpName(workRequestDto.getCustomCorpName());
        demanderCustomDto.setOperator(userInfo.getUserId());
        demanderCustomDto.setEnabled(EnabledEnum.YES.getCode());
        return demanderCustomService.addDemanderCustom(demanderCustomDto, userInfo.getUserId());
    }

    /**
     * 添加设备网点
     *
     * @param workRequestDto 工单请求数据
     * @param userInfo       当前用户
     * @param reqParam       公共参数
     * @return 网点编号
     * @author zgpi
     * @date 2020/1/16 14:24
     **/
    @Override
    public Long addDeviceBranch(WorkRequestDto workRequestDto, UserInfo userInfo, ReqParam reqParam) {
        DeviceBranchDto deviceBranchDto = new DeviceBranchDto();
        deviceBranchDto.setCustomId(workRequestDto.getCustomId());
        deviceBranchDto.setCustomCorp(workRequestDto.getCustomCorp());
        deviceBranchDto.setBranchName(workRequestDto.getDeviceBranchName());
        String province = workRequestDto.getDistrict().length() >= 2 ? workRequestDto.getDistrict().substring(0, 2) : "";
        String city = workRequestDto.getDistrict().length() >= 4 ? workRequestDto.getDistrict().substring(0, 4) : "";
        String distinct = workRequestDto.getDistrict().length() >= 6 ? workRequestDto.getDistrict().substring(0, 6) : "";
        deviceBranchDto.setProvince(province);
        deviceBranchDto.setCity(city);
        deviceBranchDto.setDistrict(distinct);
        deviceBranchDto.setContactName(workRequestDto.getContactName());
        deviceBranchDto.setContactPhone(workRequestDto.getContactPhone());
        deviceBranchDto.setAddress(workRequestDto.getAddress());
        deviceBranchDto.setEnabled(EnabledEnum.YES.getCode());
        deviceBranchDto.setOperator(userInfo.getUserId());
        deviceBranchDto.setOperateTime(DateUtil.date());
        return deviceBranchService.addDeviceBranch(deviceBranchDto, userInfo, reqParam);
    }

    @Override
    public int getReplenishCount(UserInfo userInfo, ReqParam reqParam) {
        return this.workRequestMapper.getReplenishCount(SignatureStatusEnum.FAIL.getCode(),
                FilesStatusEnum.FAIL.getCode(), userInfo.getUserId(), reqParam.getCorpId());
    }

    /**
     * 根据条件分页查询工单预警
     *
     * @param workFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-05-09
     */
    @Override
    public ListWrapper<WorkDto> queryRemindWork(WorkFilter workFilter, UserInfo userInfo, ReqParam reqParam) {
        Page<WorkDto> page = new Page(workFilter.getPageNum(), workFilter.getPageSize());
        this.findUserRight(workFilter, userInfo.getUserId(), reqParam.getCorpId());

        if (LongUtil.isZero(workFilter.getCorpId())) {
            return ListWrapper.<WorkDto>builder()
                    .list(new ArrayList<>())
                    .total(0L)
                    .build();
        }

        List<WorkDto> workDtoList = workRequestMapper.queryRemindWork(page, workFilter);
        // 获得待接单人列表
        if ("1".equals(workFilter.getForAssignees())) {
            this.getWorkForAssignEngineers(String.valueOf(WorkStatusEnum.TO_CLAIM.getCode()), workDtoList);
        } else {
            this.getWorkForAssignEngineers(workFilter.getWorkStatuses(), workDtoList);
        }
        // 初始化基础数据
        Map<String, Object> baseMap = this.initBaseMap(workDtoList);
        // 增加附加属性
        this.addExtraAttributes(workDtoList, baseMap);

        return ListWrapper.<WorkDto>builder()
                .list(workDtoList)
                .total(page.getTotal())
                .build();
    }

    /**
     * 建单添加操作记录
     *
     * @param workRequestDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/10/31 18:25
     **/
    private void addWorkOperateByCreate(WorkRequestDto workRequestDto, UserInfo userInfo, ReqParam reqParam) {
        if (workRequestDto != null) {
            WorkOperate workOperate = new WorkOperate();
            workOperate.setWorkId(workRequestDto.getWorkId());
            workOperate.setOperator(userInfo.getUserId());
            workOperate.setCorp(reqParam.getCorpId());
            workOperateService.addWorkOperateByCreate(workRequestDto, workOperate);
        }
    }

    /**
     * 补单添加操作记录
     *
     * @param workRequestDto
     * @param userInfo
     * @param reqParam
     */
    private void addWorkOperateBySupplement(WorkRequestDto workRequestDto, UserInfo userInfo, ReqParam reqParam) {
        if (workRequestDto != null) {
            WorkOperate workOperate = new WorkOperate();
            workOperate.setWorkId(workRequestDto.getWorkId());
            workOperate.setOperator(userInfo.getUserId());
            workOperate.setCorp(reqParam.getCorpId());
            workOperateService.addWorkOperateBySupplement(workRequestDto, workOperate);
        }
    }


    /**
     * 修改工单添加操作记录
     *
     * @param workRequestDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/2/26 20:23
     */
    private void addWorkOperateByMod(WorkRequestDto workRequestDto, UserInfo userInfo, ReqParam reqParam) {
        if (workRequestDto != null) {
            WorkOperate workOperate = new WorkOperate();
            workOperate.setWorkId(workRequestDto.getWorkId());
            workOperate.setOperator(userInfo.getUserId());
            workOperate.setCorp(reqParam.getCorpId());
            workOperateService.addWorkOperateByMod(workRequestDto, workOperate);
        }
    }

    /**
     * 重新提交工单添加操作记录
     *
     * @param workRequestDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/2 09:22
     */
    private void addWorkOperateByResubimt(WorkRequestDto workRequestDto, UserInfo userInfo, ReqParam reqParam) {
        if (workRequestDto != null) {
            WorkOperate workOperate = new WorkOperate();
            workOperate.setWorkId(workRequestDto.getWorkId());
            workOperate.setOperator(userInfo.getUserId());
            workOperate.setCorp(reqParam.getCorpId());
            workOperateService.addWorkOperateByResubmit(workRequestDto, workOperate);
        }
    }

    /**
     * 建单时检查设备档案
     *
     * @param workRequestDto
     * @return
     * @author zgpi
     * @date 2020/1/17 17:10
     **/
    @Override
    public List<Long> checkDeviceInfoByAdd(WorkRequestDto workRequestDto) {
        List<Long> deviceIdList = new ArrayList<>();
        Long modelId = workRequestDto.getModel();
        String serials = StrUtil.trimToEmpty(workRequestDto.getSerials());
        String deviceCodes = StrUtil.trimToEmpty(workRequestDto.getDeviceCodes());
        // 型号为空，或出厂序列号和设备号都为空时，不检查
        if (LongUtil.isZero(modelId) || (StrUtil.isBlank(serials) && StrUtil.isBlank(deviceCodes))) {
            return null;
        }
        List<String> serialList = Arrays.stream(serials.split(","))
                .filter(StrUtil::isNotBlank).collect(Collectors.toList());
        List<String> deviceCodeList = Arrays.stream(StrUtil.trimToEmpty(workRequestDto.getDeviceCodes()).split(","))
                .filter(StrUtil::isNotBlank).collect(Collectors.toList());
        String serial = serialList.size() > 0 ? StrUtil.trimToEmpty(serialList.get(0)) : "";
        String deviceCode = deviceCodeList.size() > 0 ? StrUtil.trimToEmpty(deviceCodeList.get(0)) : "";
        int length = serialList.size() >= deviceCodeList.size() ? serialList.size() : deviceCodeList.size();
        if (IntUtil.isZero(workRequestDto.getDeviceNum())) {
            workRequestDto.setDeviceNum(1);
        }
        if (length > workRequestDto.getDeviceNum()) {
            throw new AppException("设备数量不能小于实际设备数！");
        }
        DeviceInfoDto filter = new DeviceInfoDto();
        filter.setSerial(serial);
        filter.setDeviceCode(deviceCode);
        filter.setModelId(modelId);
        filter.setDemanderCorp(workRequestDto.getDemanderCorp());
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
        DeviceInfoDto deviceInfoDtoTemp = null;
        // 从第二台设备开始
        for (int i = 1; i < length; i++) {
            String serialTemp = "";
            String deviceCodeTemp = "";
            if (serialList.size() > i) {
                serialTemp = serialList.get(i);
            }
            if (deviceCodeList.size() > i) {
                deviceCodeTemp = deviceCodeList.get(i);
            }
            filter.setSerial(serialTemp);
            filter.setDeviceCode(deviceCodeTemp);
            List<DeviceInfoDto> deviceInfoDtoTempList;
            Result deviceInfoTempResult = deviceFeignService.findDeviceInfoListBy(JsonUtil.toJson(filter));
            if (deviceInfoTempResult.getCode() == Result.SUCCESS) {
                deviceInfoDtoTempList = JsonUtil.parseArray(JsonUtil.toJson(deviceInfoTempResult.getData()), DeviceInfoDto.class);
            } else {
                throw new AppException(deviceInfoTempResult.getMsg());
            }
            if (CollectionUtil.isNotEmpty(deviceInfoDtoTempList)) {
                if (deviceInfoDtoTempList.size() > 1) {
                    throw new AppException("设备档案中存在多台设备");
                }
                deviceInfoDtoTemp = deviceInfoDtoTempList.get(0);
            }
            if (deviceInfoDto == null) {
                deviceInfoDto = deviceInfoDtoTemp;
            }
            if (deviceInfoDto != null && deviceInfoDtoTemp != null) {
                deviceIdList.add(deviceInfoDto.getDeviceId());
                deviceIdList.add(deviceInfoDtoTemp.getDeviceId());
                StringBuilder error = new StringBuilder(32);
                if (!deviceInfoDto.getDemanderCorp().equals(deviceInfoDtoTemp.getDemanderCorp())) {
                    error.append("委托商不一致！");
                }
                if (!deviceInfoDto.getCustomCorp().equals(deviceInfoDtoTemp.getCustomCorp())) {
                    error.append("客户不一致！");
                }
                if (!deviceInfoDto.getServiceCorp().equals(deviceInfoDtoTemp.getServiceCorp())) {
                    error.append("服务商不一致！");
                }
                if (!deviceInfoDto.getBranchId().equals(deviceInfoDtoTemp.getBranchId())) {
                    error.append("设备网点不一致！");
                }
                if (!deviceInfoDto.getSmallClassId().equals(deviceInfoDtoTemp.getSmallClassId())) {
                    error.append("设备分类不一致！");
                }
                if (!deviceInfoDto.getModelId().equals(deviceInfoDtoTemp.getModelId())) {
                    error.append("设备型号不一致！");
                }
                if (error.length() > 0) {
                    throw new AppException(this.findDeviceErrorMsg(deviceInfoDto, deviceInfoDtoTemp)
                            + error.toString());
                }
            }
        }
        return deviceIdList;
    }

    /**
     * 获得设备校验的错误信息
     *
     * @param deviceInfoDto
     * @param deviceInfoDtoTemp
     * @return
     */
    private String findDeviceErrorMsg(DeviceInfoDto deviceInfoDto, DeviceInfoDto deviceInfoDtoTemp) {
        if (StrUtil.isNotBlank(deviceInfoDto.getSerial())
                && StrUtil.isNotBlank(deviceInfoDtoTemp.getSerial())) {
            return "出厂序列号[" + StrUtil.trimToEmpty(deviceInfoDto.getSerial()) + "]与["
                    + StrUtil.trimToEmpty(deviceInfoDtoTemp.getSerial()) + "]的";
        } else if (StrUtil.isNotBlank(deviceInfoDto.getDeviceCode())
                && StrUtil.isNotBlank(deviceInfoDtoTemp.getDeviceCode())) {
            return "设备编号[" + StrUtil.trimToEmpty(deviceInfoDto.getDeviceCode()) + "]与["
                    + StrUtil.trimToEmpty(deviceInfoDtoTemp.getDeviceCode()) + "]的";
        }
        return "";
    }

    /**
     * 获取附加属性
     *
     * @param workDtoList
     * @param baseMap
     * @return
     * @author zgpi
     * @date 2020/4/28 09:20
     **/
    private void addExtraAttributes(List<WorkDto> workDtoList, Map<String, Object> baseMap) {
        if (CollectionUtil.isNotEmpty(workDtoList)) {
            Map<Integer, String> workTypeMap = (Map<Integer, String>) baseMap.get("workTypeMap");
            workTypeMap = workTypeMap == null ? new HashMap<>(0) : workTypeMap;
            Map<Long, String> corpMap = (Map<Long, String>) baseMap.get("corpMap");
            corpMap = corpMap == null ? new HashMap<>(0) : corpMap;
            Map<Long, String> userMap = (Map<Long, String>) baseMap.get("userMap");
            userMap = userMap == null ? new HashMap<>(0) : userMap;
            Map<Long, String> demanderCustomCorpNameMap = (Map<Long, String>) baseMap.get("demanderCustomCorpNameMap");
            demanderCustomCorpNameMap = demanderCustomCorpNameMap == null ? new HashMap<>(0) : demanderCustomCorpNameMap;
            Map<Long, String> serviceBranchMap = (Map<Long, String>) baseMap.get("serviceBranchMap");
            serviceBranchMap = serviceBranchMap == null ? new HashMap<>(0) : serviceBranchMap;
            Map<Long, String> deviceBranchMap = (Map<Long, String>) baseMap.get("deviceBranchMap");
            deviceBranchMap = deviceBranchMap == null ? new HashMap<>(0) : deviceBranchMap;
            Map<Long, String> customDeviceBranchMap = (Map<Long, String>) baseMap.get("customDeviceBranchMap");
            customDeviceBranchMap = customDeviceBranchMap == null ? new HashMap<>(0) : customDeviceBranchMap;
            Map<String, String> areaMap = (Map<String, String>) baseMap.get("areaMap");
            areaMap = areaMap == null ? new HashMap<>(0) : areaMap;
            Map<Long, String> smallClassMap = (Map<Long, String>) baseMap.get("smallClassMap");
            smallClassMap = smallClassMap == null ? new HashMap<>(0) : smallClassMap;
            Map<Long, String> deviceBrandMap = (Map<Long, String>) baseMap.get("deviceBrandMap");
            deviceBrandMap = deviceBrandMap == null ? new HashMap<>(0) : deviceBrandMap;
            Map<Long, String> deviceModelMap = (Map<Long, String>) baseMap.get("deviceModelMap");
            deviceModelMap = deviceModelMap == null ? new HashMap<>(0) : deviceModelMap;
            Map<Long, String> specificationMap = (Map<Long, String>) baseMap.get("specificationMap");
            specificationMap = specificationMap == null ? new HashMap<>(0) : specificationMap;
            Map<Long, WorkDeal> workDealMap = (Map<Long, WorkDeal>) baseMap.get("workDealMap");
            workDealMap = workDealMap == null ? new HashMap<>(0) : workDealMap;

            for (WorkDto workDto : workDtoList) {
                workDto.setWarrantyName(WarrantyEnum.getNameByCode(workDto.getWarranty()));
                workDto.setServiceModeName(ServiceModeEnum.getNameByCode(workDto.getServiceMode()));
                workDto.setWorkStatusName(WorkStatusEnum.getNameByCode(workDto.getWorkStatus()));
                workDto.setWorkTypeName(StrUtil.trimToEmpty(workTypeMap.get(workDto.getWorkType())));
                workDto.setSourceName(WorkSourceEnum.getNameByCode(workDto.getSource()));
                if (LongUtil.isNotZero(workDto.getServiceCorp())) {
                    workDto.setServiceCorpName(StrUtil.trimToEmpty(corpMap.get(workDto.getServiceCorp())));
                }
                if (LongUtil.isNotZero(workDto.getEngineer())) {
                    workDto.setEngineerName(StrUtil.trimToEmpty(userMap.get(workDto.getEngineer())));
                }
                if (LongUtil.isNotZero(workDto.getCustomId())) {
                    workDto.setCustomCorpName(StrUtil.trimToEmpty(demanderCustomCorpNameMap.get(workDto.getCustomId())));
                }
                if (LongUtil.isNotZero(workDto.getServiceBranch())) {
                    workDto.setServiceBranchName(StrUtil.trimToEmpty(serviceBranchMap.get(workDto.getServiceBranch())));
                }
                if (LongUtil.isNotZero(workDto.getDeviceBranch())) {
                    workDto.setDeviceBranchName(StrUtil.trimToEmpty(deviceBranchMap.get(workDto.getDeviceBranch())));
                }
                if (StrUtil.isBlank(workDto.getDeviceBranchName()) && LongUtil.isNotZero(workDto.getDeviceBranch())) {
                    workDto.setDeviceBranchName(StrUtil.trimToEmpty(customDeviceBranchMap.get(workDto.getDeviceBranch())));
                }

                if (StrUtil.isNotBlank(workDto.getDistrict())) {
                    String province = "";
                    String city = "";
                    String district = "";
                    if (workDto.getDistrict().length() >= 2) {
                        province = StrUtil.trimToEmpty(areaMap.get(workDto.getDistrict().substring(0, 2)));
                    }
                    if (workDto.getDistrict().length() >= 4) {
                        // 如果是省直辖市
                        if (workDto.getDistrict().matches("\\d{2}9\\d{3}")) {
                            city = StrUtil.trimToEmpty(areaMap.get(workDto.getDistrict()));
                        } else if (workDto.getDistrict().matches("5002\\d{2}")) {
                            city = StrUtil.trimToEmpty(areaMap.get("5001"));
                        } else {
                            city = StrUtil.trimToEmpty(areaMap.get(workDto.getDistrict().substring(0, 4)));
                        }
                    }
                    // 非省直辖市
                    if (workDto.getDistrict().length() >= 6 && !workDto.getDistrict().matches("\\d{2}9\\d{3}")) {
                        district = StrUtil.trimToEmpty(areaMap.get(workDto.getDistrict().substring(0, 6)));
                    }
                    workDto.setCityName(city);
                    workDto.setProvinceName(province.replace("省", "").replace("自治区", "").replace("特别行政区", "")
                            .replace("回族", "").replace("壮族", "").replace("维吾尔", ""));
                    workDto.setDistrictName(district);
                }
                if (LongUtil.isNotZero(workDto.getSmallClass())) {
                    workDto.setSmallClassName(StrUtil.trimToEmpty(smallClassMap.get(workDto.getSmallClass())));
                }
                if (LongUtil.isNotZero(workDto.getBrand())) {
                    workDto.setBrandName(StrUtil.trimToEmpty(deviceBrandMap.get(workDto.getBrand())));
                }
                if (LongUtil.isNotZero(workDto.getModel())) {
                    workDto.setModelName(StrUtil.trimToEmpty(deviceModelMap.get(workDto.getModel())));
                }
                workDto.setFinishCheckStatusName(ServiceCheckStatusEnum.getNameByCode(workDto.getFinishCheckStatus()));
                workDto.setFeeCheckStatusName(FeeCheckStatusEnum.getNameByCode(workDto.getFeeCheckStatus()));
                workDto.setFinishConfirmStatusName(ServiceConfirmStatusEnum.getNameByCode(workDto.getFinishConfirmStatus()));
                workDto.setFeeConfirmStatusName(FeeConfirmStatusEnum.getNameByCode(workDto.getFeeConfirmStatus()));
                // 可结算的工单状态
                if (WorkStatusEnum.getSettleStatusList().contains(workDto.getWorkStatus())) {
                    if (IntUtil.isNotZero(workDto.getFinishCheckStatus())) {
                        workDto.setFinishStatusName(ServiceCheckStatusEnum.getNameByCode(workDto.getFinishCheckStatus()));
                    }
                    if (IntUtil.isNotZero(workDto.getFeeCheckStatus())) {
                        workDto.setFeeStatusName(FeeCheckStatusEnum.getNameByCode(workDto.getFeeCheckStatus()));
                    }
                }
                // 可确认的工单状态
                if (WorkStatusEnum.getDemanderConfirmStatusList().contains(workDto.getWorkStatus())) {
                    if (IntUtil.isNotZero(workDto.getFinishConfirmStatus())) {
                        workDto.setFinishStatusName(ServiceConfirmStatusEnum.getNameByCode(workDto.getFinishConfirmStatus()));
                    }
                    if (IntUtil.isNotZero(workDto.getFeeConfirmStatus())) {
                        workDto.setFeeStatusName(FeeConfirmStatusEnum.getNameByCode(workDto.getFeeConfirmStatus()));
                    }
                }
                workDto.setFinishCheckUserName(StrUtil.trimToEmpty(userMap.get(workDto.getFinishCheckUser())));
                workDto.setFeeCheckUserName(StrUtil.trimToEmpty(userMap.get(workDto.getFeeCheckUser())));
                workDto.setFinishConfirmUserName(StrUtil.trimToEmpty(userMap.get(workDto.getFinishConfirmUser())));
                workDto.setFeeConfirmUserName(StrUtil.trimToEmpty(userMap.get(workDto.getFeeConfirmUser())));

                // 创建人
                if (LongUtil.isNotZero(workDto.getCreator())) {
                    workDto.setCreatorName(StrUtil.trimToEmpty(userMap.get(workDto.getCreator())));
                }

                // 设备规格名称
                if (LongUtil.isNotZero(workDto.getSpecification())) {
                    workDto.setSpecificationName(StrUtil.trimToEmpty(specificationMap.get(workDto.getSpecification())));
                }

                if (LongUtil.isNotZero(workDto.getDemanderCorp())) {
                    workDto.setDemanderCorpName(StrUtil.trimToEmpty(corpMap.get(workDto.getDemanderCorp())));
                }
                if (IntUtil.isNotZero(workDto.getWarranty())) {
                    workDto.setWarrantyName(StrUtil.trimToEmpty(WarrantyEnum.getNameByCode(workDto.getWarranty())));
                }
                if (IntUtil.isNotZero(workDto.getWarrantyMode())) {
                    workDto.setWarrantyModeName(StrUtil.trimToEmpty(WarrantyModeEnum.getNameByCode(workDto.getWarrantyMode())));
                }

                // 添加工单处理信息表数据
                WorkDeal workDeal = new WorkDeal();
                if (CollectionUtil.isNotEmpty(workDealMap)) {
                    workDeal = workDealMap.get(workDto.getWorkId());
                }

                // 接单时间
                if (workDeal != null && workDeal.getAcceptTime() != null) {
                    workDto.setAcceptTime(workDeal.getAcceptTime());
                }

                // 外部协同人员
                if (workDeal != null && workDeal.getHelpNames() != null) {
                    workDto.setHelpNames(workDeal.getHelpNames());
                }

                // 协同工程师
                if (workDeal != null && workDeal.getTogetherEngineers() != null) {
                    String[] togethers = workDeal.getTogetherEngineers().split(",");
                    String engineerStr = null;
                    for (int i = 0; i < togethers.length; i++) {
                        if (StrUtil.isNotBlank(togethers[i])) {
                            if (engineerStr == null) {
                                engineerStr = userMap.get(Long.parseLong(togethers[i]));
                            } else {
                                engineerStr = engineerStr + ',' + userMap.get(Long.parseLong(togethers[i]));
                            }
                        }
                    }
                    workDto.setTogetherEngineers(engineerStr);
                }
                if (IntUtil.isNotZero(workDto.getRemindType())) {
                    String remindTypeName = WorkRemindTypeEnum.getNameByCode(workDto.getRemindType());
                    workDto.setRemindTypeName(remindTypeName);
                }
            }
        }
    }

    /**
     * 添加权限类型列表
     *
     * @param workFilter
     * @return
     * @author zgpi
     * @date 2020/2/16 17:18
     */
    private WorkFilter addRightTypeList(WorkFilter workFilter) {
        Result<List<Long>> rightIdListResult = uasFeignService.listUserRightId(workFilter.getUserId(), workFilter.getCorpId());
        List<Long> rightIdList = new ArrayList<>();
        if (rightIdListResult != null && rightIdListResult.getCode() == Result.SUCCESS) {
            rightIdList = rightIdListResult.getData();
            rightIdList = rightIdList == null ? new ArrayList<>() : rightIdList;
        }
        boolean hasOpRight = false;
        List<Integer> rightTypeList = new ArrayList<>();
        // 有提单权限
        if (rightIdList.contains(RightConstants.WORK_DISPATCH)) {
            rightTypeList.add(1);
            hasOpRight = true;
        }
        // 有分配权限
        if (rightIdList.contains(RightConstants.WORK_HANDLE)) {
            rightTypeList.add(2);
            hasOpRight = true;
        }
        // 有派单权限
        if (rightIdList.contains(RightConstants.WORK_ASSIGN) || rightIdList.contains(RightConstants.WORK_ASSIGN_ALL)) {
            rightTypeList.add(3);
            hasOpRight = true;
        }
        // 有接单权限
        if (rightIdList.contains(RightConstants.WORK_CLAIM)) {
            rightTypeList.add(4);
            hasOpRight = true;
        }
        // 有签到权限
        if (rightIdList.contains(RightConstants.WORK_SIGN)) {
            rightTypeList.add(5);
            hasOpRight = true;
        }
        // 有远程服务权限
        if (rightIdList.contains(RightConstants.WORK_TO_SERVICE)) {
            rightTypeList.add(6);
            hasOpRight = true;
        }
        // 有现场服务权限
        if (rightIdList.contains(RightConstants.WORK_IN_SERVICE)) {
            rightTypeList.add(7);
            hasOpRight = true;
        }
        if (!hasOpRight) {
            rightTypeList.add(0);
        }
        workFilter.setRightTypeList(rightTypeList);
        return workFilter;
    }


    /**
     * 根据时间生成WorkCode
     *
     * @param serviceTime
     * @return
     */
    private String getWorkCodeByTime(Date serviceTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String day = format.format(serviceTime).replaceAll("-", "");
        List<WorkDto> list = workRequestMapper.listWorkCodeByDay(day);
        long code = 1;
        if (list != null && list.size() > 0) {
            String workCode = list.get(0).getWorkCode();
            String nowNum = workCode.substring(day.length() + 1).replaceFirst("^0*", "");
            code = Integer.parseInt(nowNum) + 1;
        }

        DecimalFormat decimalFormat = new DecimalFormat("000000");
        return day + decimalFormat.format(code);
    }

    /**
     * 选择了服务审核状态，需要排除掉部分确认状态
     *
     * @param workFilter
     * @return
     * @author zgpi
     * @date 2020/7/3 14:01
     **/
    private void listNotInFinishConfirmStatus(WorkFilter workFilter) {
        if (workFilter != null && StrUtil.isNotBlank(workFilter.getFinishCheckStatuses())) {
            List<String> statusList = StrUtil.split(workFilter.getFinishCheckStatuses(), ',', true, true);
            if (CollectionUtil.isNotEmpty(statusList)) {
                List<Integer> notInList = new ArrayList<>();
                for (String status : statusList) {
                    if (ServiceCheckStatusEnum.UN_CHECK.getCode().equals(Integer.parseInt(status))) {
                        notInList.addAll(ServiceConfirmStatusEnum.listServiceConfirmStatus());
                    }
                    if (ServiceCheckStatusEnum.CHECK_REFUSE.getCode().equals(Integer.parseInt(status))) {
                        notInList.addAll(ServiceConfirmStatusEnum.listServiceConfirmStatus());
                    }
                    if (ServiceCheckStatusEnum.CHECK_PASS.getCode().equals(Integer.parseInt(status))) {
                        notInList.add(ServiceConfirmStatusEnum.CONFIRM_PASS.getCode());
                        notInList.add(ServiceConfirmStatusEnum.CONFIRM_REFUSE.getCode());
                    }
                }
                workFilter.setNotInFinishConfirmStatusList(notInList.stream().distinct().collect(Collectors.toList()));
            }
        }
    }

    /**
     * 选择了费用审核状态，需要排除掉部分确认状态
     *
     * @param workFilter
     * @return
     * @author zgpi
     * @date 2020/7/3 14:01
     **/
    private void listNotInFeeConfirmStatus(WorkFilter workFilter) {
        if (workFilter != null && StrUtil.isNotBlank(workFilter.getFeeCheckStatuses())) {
            List<String> statusList = StrUtil.split(workFilter.getFeeCheckStatuses(), ',', true, true);
            if (CollectionUtil.isNotEmpty(statusList)) {
                List<Integer> notInList = new ArrayList<>();
                for (String status : statusList) {
                    if (FeeCheckStatusEnum.UN_CHECK.getCode().equals(Integer.parseInt(status))) {
                        notInList.addAll(FeeConfirmStatusEnum.listFeeConfirmStatus());
                    }
                    if (FeeCheckStatusEnum.CHECK_REFUSE.getCode().equals(Integer.parseInt(status))) {
                        notInList.addAll(FeeConfirmStatusEnum.listFeeConfirmStatus());
                    }
                    if (FeeCheckStatusEnum.CHECK_PASS.getCode().equals(Integer.parseInt(status))) {
                        notInList.add(FeeConfirmStatusEnum.CONFIRM_PASS.getCode());
                        notInList.add(FeeConfirmStatusEnum.CONFIRM_REFUSE.getCode());
                    }
                }
                workFilter.setNotInFeeConfirmStatusList(notInList.stream().distinct().collect(Collectors.toList()));
            }
        }
    }

    /**
     * 微信用户新建工单
     *
     * @param workFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public ListWrapper<WorkDto> queryWXWork(WorkFilter workFilter,
                                            UserInfo userInfo,
                                            ReqParam reqParam) {
        Page<WorkDto> page = new Page(workFilter.getPageNum(), workFilter.getPageSize());
        List<WorkDto> workDtoList = workRequestMapper.queryWXWork(page, workFilter);
        // 初始化基础数据
        Map<String, Object> baseMap = this.initBaseMap(workDtoList);
        // 增加附加属性
        this.addExtraAttributes(workDtoList, baseMap);

        return ListWrapper.<WorkDto>builder()
                .list(workDtoList)
                .total(page.getTotal())
                .build();
    }


    /**
     * 发送微信建单消息
     * 工单编号：工单编号
     * 工单类型：工单类型
     * 设备名称：设备类型 - 设备规格
     * 工单状态：工单状态
     * 创建时间：建单时间
     *
     * @param workId
     */
    @Override
    public WxTemplateMessage buildWxMessage(String workId) {
        WorkRequest workRequest = workRequestService.getById(workId);
        WxTemplateMessage message = new WxTemplateMessage();

        String openid = uasFeignService.getOpenidByUserid(workRequest.getCreator());

        // 检查建单用户是否有绑定的微信账号
        if (openid != null && !openid.trim().equals("")) {
            message.setToUser(StrUtil.trimToEmpty(openid));
        } else {
            return null;
        }

        StringBuilder titleValue = new StringBuilder("你好，您的工单创建成功，我们将尽快处理");

        message.setFirst(titleValue.toString());
        message.setTemplateId(WxTemplateEnum.getvalueByCode(1));
        message.setRedirectUrl("work-detail?workId=" + workRequest.getWorkId());


        Map<Integer, String> workTypeMap = workTypeService.mapWorkType();

        WorkDeal workDeal = workDealService.getById(workId);
        String workStatusName = WorkStatusEnum.getNameByCode(workDeal.getWorkStatus());

        // 远程调用获得设备小类映射
        DeviceSmallClassDto deviceSmallClassDto = null;
        if (LongUtil.isNotZero(workRequest.getSmallClass())) {
            Result smallClassResult = deviceFeignService.findDeviceSmallClass(workRequest.getSmallClass());
            if (smallClassResult.getCode() == Result.SUCCESS) {
                deviceSmallClassDto = JsonUtil.parseObject(JsonUtil.toJson(smallClassResult.getData()), DeviceSmallClassDto.class);
            }
        }

        String largeClassName = null;
        String smallClassName = null;
        if (deviceSmallClassDto != null) {
            largeClassName = deviceSmallClassDto.getLargeClassName();
            smallClassName = deviceSmallClassDto.getName();
        }

        DeviceSpecificationDto deviceSpecificationDto = null;
        if (LongUtil.isNotZero(workRequest.getSpecification())) {
            Result specificationResult = deviceFeignService.findDeviceSpecification(workRequest.getSpecification());
            if (specificationResult.getCode() == Result.SUCCESS) {
                deviceSpecificationDto = JsonUtil.parseObject(JsonUtil.toJson(specificationResult.getData()), DeviceSpecificationDto.class);
            }
        }
        String specificationName = null;
        if (deviceSpecificationDto != null) {
            specificationName = deviceSpecificationDto.getName();
        }
        StringBuilder deviceName = new StringBuilder();
        if (largeClassName != null) {
            deviceName.append(largeClassName);
            if (smallClassName != null) {
                deviceName.append(" | ").append(smallClassName);
                if (specificationName != null) {
                    deviceName.append(" | ").append(specificationName);
                }
            }
        } else {
            if (smallClassName != null) {
                deviceName.append(smallClassName);
                if (specificationName != null) {
                    deviceName.append(" | ").append(specificationName);
                }
            }
        }
        String WorkTypeName = StrUtil.trimToEmpty(workTypeMap.get(workRequest.getWorkType()));

        List<Object> dataList = new ArrayList<>();


        dataList.add(String.valueOf(workRequest.getWorkCode()));
        dataList.add(WorkTypeName);
        dataList.add(StrUtil.trimToEmpty(deviceName));
        dataList.add(workStatusName);
        dataList.add(DateUtil.format(workRequest.getCreateTime(), "yyyy-MM-dd HH:mm"));
        message.setKeyWordList(dataList);


        return message;
    }
}
