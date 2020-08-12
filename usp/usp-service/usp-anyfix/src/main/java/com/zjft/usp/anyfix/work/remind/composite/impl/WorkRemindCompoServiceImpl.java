package com.zjft.usp.anyfix.work.remind.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.baseinfo.service.WorkTypeService;
import com.zjft.usp.anyfix.corp.branch.service.DeviceBranchService;
import com.zjft.usp.anyfix.corp.branch.service.ServiceBranchService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderCustomService;
import com.zjft.usp.anyfix.work.remind.composite.WorkRemindCompoService;
import com.zjft.usp.anyfix.work.remind.dto.WorkRemindDealDto;
import com.zjft.usp.anyfix.work.remind.dto.WorkRemindDetailDto;
import com.zjft.usp.anyfix.work.remind.dto.WorkRemindMainDto;
import com.zjft.usp.anyfix.work.remind.dto.WorkRemindTypeDto;
import com.zjft.usp.anyfix.work.remind.enums.WorkRemindTypeEnum;
import com.zjft.usp.anyfix.work.remind.filter.WorkRemindFilter;
import com.zjft.usp.anyfix.work.remind.model.WorkRemindDetail;
import com.zjft.usp.anyfix.work.remind.model.WorkRemindMain;
import com.zjft.usp.anyfix.work.remind.service.WorkRemindDealService;
import com.zjft.usp.anyfix.work.remind.service.WorkRemindDetailService;
import com.zjft.usp.anyfix.work.remind.service.WorkRemindMainService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.device.service.DeviceFeignService;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.*;

/**
 * 工单预警聚合实现类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-04-23 10:45
 **/
@Transactional(rollbackFor = Exception.class)
@Service
public class WorkRemindCompoServiceImpl implements WorkRemindCompoService {
    @Autowired
    private WorkRemindMainService workRemindMainService;
    @Autowired
    private WorkRemindDetailService workRemindDetailService;
    @Autowired
    private DeviceBranchService deviceBranchService;
    @Autowired
    private WorkTypeService workTypeService;
    @Autowired
    private ServiceBranchService serviceBranchService;
    @Autowired
    private DemanderCustomService demanderCustomService;
    @Resource
    private UasFeignService uasFeignService;
    @Resource
    private DeviceFeignService deviceFeignService;
    @Autowired
    private WorkRemindDealService workRemindDealService;

    private Map<Integer, String> workTypeMap = null;
    private Map<Long, String> demanderCustomCorpNameMap = null;
    private Map<Long, String> corpMap = null;
    private Map<Long, String> userMap = null;
    private Map<String, String> areaMap = null;
    private Map<Long, String> largeClassMap = null;
    private Map<Long, String> smallClassMap = null;
    private Map<Long, String> deviceBrandMap = null;
    private Map<Long, String> deviceModelMap = null;
    private Map<Long, String> serviceBranchMap = null;
    private Map<Long, String> deviceBranchMap = null;

    /**
     * 查询工单预警设置列表
     *
     * @param workRemindFilter
     * @return
     * @author Qiugm
     * @date 2020-04-24
     */
    @Override
    public ListWrapper<WorkRemindMainDto> queryWorkRemind(WorkRemindFilter workRemindFilter) {
        Page page = new Page(workRemindFilter.getPageNum(), workRemindFilter.getPageSize());
        List<WorkRemindMainDto> workRemindMainDtoList = workRemindMainService.queryWorkRemind(workRemindFilter, page);
        // 初始化基础数据
        this.initData(workRemindMainDtoList);
        // 增加附加属性
        this.addExtraAttributes(workRemindMainDtoList);
        return ListWrapper.<WorkRemindMainDto>builder()
                .list(workRemindMainDtoList)
                .total(page.getTotal())
                .build();
    }

    /**
     * 初始化数据
     *
     * @param workRemindMainDtoList
     * @return
     * @author Qiugm
     * @date 2020-04-24
     */
    private void initData(List<WorkRemindMainDto> workRemindMainDtoList) {
        if (CollectionUtil.isNotEmpty(workRemindMainDtoList)) {
            // 企业列表
            Set<Long> corpIdSet = new HashSet<>();
            Set<Long> customIdSet = new HashSet<>();
            Set<String> areaCodeSet = new HashSet<>();
            Set<Long> userIdSet = new HashSet<>();
            for (WorkRemindMainDto workRemindMainDto : workRemindMainDtoList) {
                corpIdSet.add(workRemindMainDto.getServiceCorp());
                corpIdSet.add(workRemindMainDto.getDemanderCorp());
                corpIdSet.add(workRemindMainDto.getCustomCorp());
                customIdSet.add(workRemindMainDto.getCustomId());
                if (StrUtil.isNotBlank(workRemindMainDto.getDistrict())) {
                    if (StrUtil.isNotBlank(workRemindMainDto.getDistrict()) && workRemindMainDto.getDistrict()
                            .length() >= 2) {
                        areaCodeSet.add(workRemindMainDto.getDistrict().substring(0, 2));
                    }
                    if (StrUtil.isNotBlank(workRemindMainDto.getDistrict()) && workRemindMainDto.getDistrict()
                            .length() >= 4) {
                        areaCodeSet.add(workRemindMainDto.getDistrict().substring(0, 4));
                    }
                    if (StrUtil.isNotBlank(workRemindMainDto.getDistrict()) && workRemindMainDto.getDistrict()
                            .length() >= 6) {
                        areaCodeSet.add(workRemindMainDto.getDistrict().substring(0, 6));
                    }
                }
                // 创建人
                if (LongUtil.isNotZero(workRemindMainDto.getCreator())) {
                    userIdSet.add(workRemindMainDto.getCreator());
                }
            }

            List<Long> corpIdList = new ArrayList<>(corpIdSet);
            List<Long> customIdList = new ArrayList<>(customIdSet);
            List<String> areaCodeList = new ArrayList<>(areaCodeSet);
            List<Long> userIdList = new ArrayList<>(userIdSet);

            workTypeMap = workTypeService.mapWorkType();
            Result<Map<Long, String>> corpResult = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList));
            if (corpResult.getCode() == Result.SUCCESS) {
                corpMap = corpResult.getData();
                corpMap = corpMap == null ? new HashMap<>(0) : corpMap;
            }
            Result<Map<Long, String>> userResult = uasFeignService
                    .mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList));
            if (userResult.getCode() == Result.SUCCESS) {
                userMap = userResult.getData();
                userMap = userMap == null ? new HashMap<>(0) : userMap;
            }
            demanderCustomCorpNameMap = demanderCustomService.mapIdAndCustomNameByIdList(customIdList);
            demanderCustomCorpNameMap = demanderCustomCorpNameMap == null ? new HashMap<>(
                    0) : demanderCustomCorpNameMap;
            Result<Map<String, String>> areaResult = uasFeignService.mapAreaCodeAndNameByCodeList(JsonUtil.toJson(areaCodeList));
            if (areaResult.getCode() == Result.SUCCESS) {
                areaMap = areaResult.getData();
                areaMap = areaMap == null ? new HashMap<>(0) : areaMap;
            }
            Result<Map<Long, String>> largeClassResult = deviceFeignService.mapLargeClassByCorpIdList(corpIdList);
            if (largeClassResult.getCode() == Result.SUCCESS) {
                largeClassMap = largeClassResult.getData();
                largeClassMap = largeClassMap == null ? new HashMap<>(0) : largeClassMap;
            }
            Result<Map<Long, String>> smallClassResult = deviceFeignService.mapSmallClassByCorpIdList(corpIdList);
            if (smallClassResult.getCode() == Result.SUCCESS) {
                smallClassMap = smallClassResult.getData();
                smallClassMap = smallClassMap == null ? new HashMap<>(0) : smallClassMap;
            }
            Result<Map<Long, String>> deviceBrandResult = deviceFeignService.mapDeviceBrandByCorpIdList(corpIdList);
            if (deviceBrandResult.getCode() == Result.SUCCESS) {
                deviceBrandMap = deviceBrandResult.getData();
                deviceBrandMap = deviceBrandMap == null ? new HashMap<>(0) : deviceBrandMap;
            }
            Result<Map<Long, String>> deviceModelResult = deviceFeignService.mapDeviceModelByCorpIdList(corpIdList);
            if (deviceModelResult.getCode() == Result.SUCCESS) {
                deviceModelMap = deviceModelResult.getData();
                deviceModelMap = deviceModelMap == null ? new HashMap<>(0) : deviceModelMap;
            }
            serviceBranchMap = serviceBranchService.mapServiceBranchByCorpIdList(corpIdList);
            deviceBranchMap = deviceBranchService.mapDeviceBranchByCorpIdList(corpIdList);
        }
    }

    /**
     * 添加附加属性
     *
     * @param workRemindMainDtoList
     * @return
     * @author Qiugm
     * @date 2020-04-24
     */
    private void addExtraAttributes(List<WorkRemindMainDto> workRemindMainDtoList) {
        if (CollectionUtil.isNotEmpty(workRemindMainDtoList)) {
            for (WorkRemindMainDto workRemindMainDto : workRemindMainDtoList) {
                workRemindMainDto
                        .setWorkTypeName(StrUtil.trimToEmpty(workTypeMap.get(workRemindMainDto.getWorkType())));
                if (LongUtil.isNotZero(workRemindMainDto.getServiceCorp())) {
                    workRemindMainDto
                            .setServiceCorpName(StrUtil.trimToEmpty(corpMap.get(workRemindMainDto.getServiceCorp())));
                }
                if (LongUtil.isNotZero(workRemindMainDto.getCustomId())) {
                    workRemindMainDto.setCustomCorpName(
                            StrUtil.trimToEmpty(demanderCustomCorpNameMap.get(workRemindMainDto.getCustomId())));
                }
                if (LongUtil.isNotZero(workRemindMainDto.getDeviceBranch())) {
                    workRemindMainDto.setDeviceBranchName(
                            StrUtil.trimToEmpty(deviceBranchMap.get(workRemindMainDto.getDeviceBranch())));
                }
                if (LongUtil.isNotZero(workRemindMainDto.getLargeClassId())) {
                    workRemindMainDto.setLargeClassName(
                            StrUtil.trimToEmpty(largeClassMap.get(workRemindMainDto.getLargeClassId())));
                }
                if (LongUtil.isNotZero(workRemindMainDto.getSmallClassId())) {
                    workRemindMainDto.setSmallClassName(
                            StrUtil.trimToEmpty(smallClassMap.get(workRemindMainDto.getSmallClassId())));
                }
                if (LongUtil.isNotZero(workRemindMainDto.getBrandId())) {
                    workRemindMainDto
                            .setBrandName(StrUtil.trimToEmpty(deviceBrandMap.get(workRemindMainDto.getBrandId())));
                }
                if (LongUtil.isNotZero(workRemindMainDto.getModelId())) {
                    workRemindMainDto
                            .setModelName(StrUtil.trimToEmpty(deviceModelMap.get(workRemindMainDto.getModelId())));
                }
                // 创建人
                if (LongUtil.isNotZero(workRemindMainDto.getCreator())) {
                    workRemindMainDto.setCreatorName(StrUtil.trimToEmpty(userMap.get(workRemindMainDto.getCreator())));
                }

                if (LongUtil.isNotZero(workRemindMainDto.getDemanderCorp())) {
                    workRemindMainDto
                            .setDemanderCorpName(StrUtil.trimToEmpty(corpMap.get(workRemindMainDto.getDemanderCorp())));
                }
                if (StrUtil.isNotEmpty(workRemindMainDto.getDistrict())) {
                    String province = "";
                    String city = "";
                    String district = "";
                    if (workRemindMainDto.getDistrict().length() >= 2) {
                        province = StrUtil.trimToEmpty(areaMap.get(workRemindMainDto.getDistrict().substring(0, 2)));
                    }
                    if (workRemindMainDto.getDistrict().length() >= 4) {
                        // 如果是省直辖市
                        if (workRemindMainDto.getDistrict().matches("\\d{2}9\\d{3}")) {
                            city = StrUtil.trimToEmpty(areaMap.get(workRemindMainDto.getDistrict()));
                        } else if (workRemindMainDto.getDistrict().matches("5002\\d{2}")) {
                            city = StrUtil.trimToEmpty(areaMap.get("5001"));
                        } else {
                            city = StrUtil.trimToEmpty(areaMap.get(workRemindMainDto.getDistrict().substring(0, 4)));
                        }
                    }
                    // 非省直辖市
                    if (workRemindMainDto.getDistrict().length() >= 6 && !workRemindMainDto.getDistrict().matches("\\d{2}9\\d{3}")) {
                        district = StrUtil.trimToEmpty(areaMap.get(workRemindMainDto.getDistrict().substring(0, 6)));
                    }
                    workRemindMainDto.setCityName(city);
                    workRemindMainDto.setProvinceName(province.replace("省", "").replace("自治区", "").replace("特别行政区", "")
                                .replace("回族", "").replace("壮族", "").replace("维吾尔族", ""));
                    workRemindMainDto.setDistrictName(district);
                    workRemindMainDto
                            .setDistrictName(StrUtil.trimToEmpty(areaMap.get(workRemindMainDto.getDistrict())));
                }
                if (LongUtil.isNotZero(workRemindMainDto.getServiceBranch())) {
                    workRemindMainDto.setServiceBranchName(
                            StrUtil.trimToEmpty(serviceBranchMap.get(workRemindMainDto.getServiceBranch())));
                }
                if (IntUtil.isNotZero(workRemindMainDto.getRemindType())) {
                    workRemindMainDto
                            .setRemindTypeName(WorkRemindTypeEnum.getNameByCode(workRemindMainDto.getRemindType()));
                }
            }
        }
    }

    /**
     * 查询工单预警设置详情
     *
     * @param remindId
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-04-24
     */
    @Override
    public WorkRemindMainDto findWorkRemind(Long remindId, ReqParam reqParam) {
        WorkRemindMainDto workRemindMainDto;
        WorkRemindMain workRemindMain = workRemindMainService.getById(remindId);
        workRemindMainDto = JsonUtil.parseObject(JsonUtil.toJson(workRemindMain), WorkRemindMainDto.class);
        if (workRemindMainDto != null) {
            List<WorkRemindDetailDto> workRemindDetailDtoList = workRemindDetailService.listByRemindId(remindId);
            workRemindMainDto.setWorkRemindDetailDtoList(workRemindDetailDtoList);
        }
        return workRemindMainDto;
    }

    /**
     * 获取工单预警类型列表
     *
     * @param
     * @return
     * @author Qiugm
     * @date 2020-04-24
     */
    @Override
    public List<WorkRemindTypeDto> listWorkRemindType() {
        List<WorkRemindTypeDto> workRemindTypeDtoList = new ArrayList<>();
        for (WorkRemindTypeEnum workRemindTypeEnum : WorkRemindTypeEnum.values()) {
            WorkRemindTypeDto workRemindTypeDto = new WorkRemindTypeDto();
            workRemindTypeDto.setId(workRemindTypeEnum.getCode());
            workRemindTypeDto.setName(workRemindTypeEnum.getName());
            workRemindTypeDtoList.add(workRemindTypeDto);
        }
        return workRemindTypeDtoList;
    }

    /**
     * 添加工单预警设置
     *
     * @param workRemindMainDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-04-24
     */
    @Override
    public void addWorkRemind(WorkRemindMainDto workRemindMainDto, UserInfo userInfo, ReqParam reqParam) {
        checkFormData(workRemindMainDto);
        WorkRemindMain workRemindMain = JsonUtil.parseObject(JsonUtil.toJson(workRemindMainDto), WorkRemindMain.class);
        workRemindMain.setCreator(userInfo.getUserId());
        workRemindMain.setCreateTime(new Date(System.currentTimeMillis()));
        if (CollectionUtil.isNotEmpty(workRemindMainDto.getWorkRemindDetailDtoList())) {
            List<WorkRemindDetail> workRemindDetailList =
                    JsonUtil.parseArray(JsonUtil.toJson(workRemindMainDto.getWorkRemindDetailDtoList()),
                            WorkRemindDetail.class);
            for (WorkRemindDetail workRemindDetail : workRemindDetailList) {
                workRemindMain.setRemindId(KeyUtil.getId());
                workRemindMainService.save(workRemindMain);

                workRemindDetail.setDetailId(KeyUtil.getId());
                workRemindDetail.setRemindId(workRemindMain.getRemindId());
                workRemindDetailService.save(workRemindDetail);
            }
        }
    }

    /**
     * 修改工单预警设置
     *
     * @param workRemindMainDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-04-24
     */
    @Override
    public void modWorkRemind(WorkRemindMainDto workRemindMainDto, UserInfo userInfo, ReqParam reqParam) {
        checkFormData(workRemindMainDto);
        WorkRemindMain workRemindMain = JsonUtil.parseObject(JsonUtil.toJson(workRemindMainDto), WorkRemindMain.class);
        workRemindMainService.updateById(workRemindMain);
        workRemindDetailService.deleteByRemindId(workRemindMain.getRemindId());
        if (CollectionUtil.isNotEmpty(workRemindMainDto.getWorkRemindDetailDtoList())) {
            List<WorkRemindDetail> workRemindDetailList =
                    JsonUtil.parseArray(JsonUtil.toJson(workRemindMainDto.getWorkRemindDetailDtoList()),
                            WorkRemindDetail.class);
            for (WorkRemindDetail workRemindDetail : workRemindDetailList) {
                workRemindDetail.setDetailId(KeyUtil.getId());
                workRemindDetail.setRemindId(workRemindMain.getRemindId());
                workRemindDetailService.save(workRemindDetail);
            }
        }
    }

    /**
     * 删除工单预警设置
     *
     * @param remindId
     * @return
     * @author Qiugm
     * @date 2020-04-24
     */
    @Override
    public void removeById(Long remindId) {
        WorkRemindMain workRemindMain = workRemindMainService.getById(remindId);
        if (workRemindMain == null) {
            throw new AppException("该工单预警设置已不存在，请确认");
        }
        //删除工单预警设置主表信息
        workRemindMainService.removeById(remindId);
        //删除工单预警设置明细信息
        workRemindDetailService.deleteByRemindId(remindId);
    }

    /**
     * 修改预警时间
     *
     * @param workRemindDealDto
     * @param userInfo
     * @return
     * @author Qiugm
     * @date 2020-05-13
     */
    @Override
    public void modRemindTime(WorkRemindDealDto workRemindDealDto, UserInfo userInfo) {
        if (workRemindDealDto != null) {
            workRemindDealDto.setOperator(userInfo.getUserId());
            workRemindDealDto.setEnabled("Y");
            workRemindDealDto.setOperateTime(new Date(System.currentTimeMillis()));
            workRemindDealService.modRemindTime(workRemindDealDto);
        }
    }

    /**
     * 检查表单数据
     *
     * @param workRemindMainDto
     * @return
     * @author Qiugm
     * @date 2020-04-24
     */
    private void checkFormData(WorkRemindMainDto workRemindMainDto) {
        if (workRemindMainDto == null) {
            throw new AppException("工单预警信息不能为空");
        }
        if (CollectionUtil.isEmpty(workRemindMainDto.getWorkRemindDetailDtoList())) {
            throw new AppException("工单预警条件不能为空");
        }
    }

}
