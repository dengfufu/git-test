package com.zjft.usp.anyfix.work.fee.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.service.WorkTypeService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderCustomService;
import com.zjft.usp.anyfix.work.check.enums.FeeCheckStatusEnum;
import com.zjft.usp.anyfix.work.check.enums.FeeConfirmStatusEnum;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeVerifyDetailDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeVerifyDetailExcelDto;
import com.zjft.usp.anyfix.work.fee.filter.WorkFeeVerifyDetailFilter;
import com.zjft.usp.anyfix.work.fee.mapper.WorkFeeVerifyDetailMapper;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeVerify;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeVerifyDetail;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeService;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeVerifyDetailService;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeVerifyService;
import com.zjft.usp.anyfix.work.request.enums.WarrantyEnum;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.device.service.DeviceFeignService;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 委托商对账单明细表 服务实现类
 * </p>
 *
 * @author canlei
 * @since 2020-05-12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkFeeVerifyDetailServiceImpl extends ServiceImpl<WorkFeeVerifyDetailMapper, WorkFeeVerifyDetail> implements WorkFeeVerifyDetailService {

    @Autowired
    private DemanderCustomService demanderCustomService;
    @Autowired
    private WorkTypeService workTypeService;
    @Autowired
    private WorkFeeVerifyService workFeeVerifyService;
    @Autowired
    private WorkFeeService workFeeService;

    @Resource
    private DeviceFeignService deviceFeignService;
    @Resource
    private UasFeignService uasFeignService;

    public static final DateFormat FAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * 根据对账单号查询明细
     *
     * @param verifyId
     * @return
     */
    @Override
    public List<WorkFeeVerifyDetailDto> listDtoByVerifyId(Long verifyId) {
        if (LongUtil.isZero(verifyId)) {
            return null;
        }
        WorkFeeVerify workFeeVerify = this.workFeeVerifyService.getById(verifyId);
        if (workFeeVerify == null) {
            throw new AppException("对账单不存在，请检查");
        }
        List<WorkFeeVerifyDetailDto> dtoList = this.baseMapper.listByVerifyId(verifyId);
        this.addExtraAttributes(dtoList, workFeeVerify);
        return dtoList;
    }

    /**
     * 分页查询对账单明细,根据对账单号
     *
     * @param workFeeVerifyDetailFilter
     * @return
     */
    @Override
    public ListWrapper<WorkFeeVerifyDetailDto> query(WorkFeeVerifyDetailFilter workFeeVerifyDetailFilter) {
        ListWrapper<WorkFeeVerifyDetailDto> listWrapper = new ListWrapper<>();
        if (workFeeVerifyDetailFilter == null || LongUtil.isZero(workFeeVerifyDetailFilter.getVerifyId())) {
            return listWrapper;
        }
        Page page = new Page(workFeeVerifyDetailFilter.getPageNum(), workFeeVerifyDetailFilter.getPageSize());
        List<WorkFeeVerifyDetailDto> list = this.baseMapper.pageByFilter(page, workFeeVerifyDetailFilter);
        WorkFeeVerify workFeeVerify = this.workFeeVerifyService.getById(workFeeVerifyDetailFilter.getVerifyId());
        if (CollectionUtil.isNotEmpty(list)) {
            this.addExtraAttributes(list, workFeeVerify);
        }
        listWrapper.setList(list);
        listWrapper.setTotal(page.getTotal());
        return listWrapper;
    }

    /**
     * 根据对账单号删除明细
     *
     * @param verifyId
     */
    @Override
    public void deleteByVerifyId(Long verifyId) {
        if (LongUtil.isZero(verifyId)) {
            throw new AppException("对账单号不能为空");
        }
        this.remove(new QueryWrapper<WorkFeeVerifyDetail>().eq("verify_id", verifyId));
    }

    /**
     * 根据对账单号查询WorkDeal列表
     *
     * @param verifyId
     * @return
     */
    @Override
    public List<WorkDeal> listWorkDealByVerifyId(Long verifyId) {
        if (LongUtil.isZero(verifyId)) {
            return null;
        }
        return this.baseMapper.listWorkDealByVerifyId(verifyId);
    }

    /**
     * 查询对账单明细
     *
     * @param verifyId
     * @return
     */
    @Override
    public List<WorkFeeVerifyDetail> listByVerifyId(Long verifyId) {
        if (LongUtil.isZero(verifyId)) {
            return null;
        }
        List<WorkFeeVerifyDetail> workFeeVerifyDetailList = this.list(new QueryWrapper<WorkFeeVerifyDetail>()
                .eq("verify_id", verifyId));
        return workFeeVerifyDetailList;
    }

    /**
     * 根据对账单号list获取明细list
     *
     * @param verifyIdList
     * @return
     */
    @Override
    public List<WorkFeeVerifyDetail> listByVerifyIdList(List<Long> verifyIdList) {
        if (CollectionUtil.isEmpty(verifyIdList)) {
            return null;
        }
        List<WorkFeeVerifyDetail> list = this.list(new QueryWrapper<WorkFeeVerifyDetail>().in("verify_id", verifyIdList));
        return list;
    }

    /**
     * 根据工单编号查询
     *
     * @param workId
     * @return
     */
    @Override
    public WorkFeeVerifyDetail findByWorkId(Long workId) {
        if (LongUtil.isZero(workId)) {
            return null;
        }
        List<WorkFeeVerifyDetail> list = this.list(new QueryWrapper<WorkFeeVerifyDetail>().eq("work_id", workId));
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据对账单号获取工单编号与对账明细的映射
     *
     * @param verifyId
     * @return
     */
    @Override
    public Map<Long, WorkFeeVerifyDetail> mapWorkIdAndDetailByVerifyId(Long verifyId) {
        Map<Long, WorkFeeVerifyDetail> map = new HashMap<>();
        List<WorkFeeVerifyDetail> list = this.listByVerifyId(verifyId);
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(workFeeVerifyDetail -> {
                map.put(workFeeVerifyDetail.getWorkId(), workFeeVerifyDetail);
            });
        }
        return map;
    }

    /**
     * 根据对账单编号获取导出明细列表
     *
     * @param verifyId
     * @return
     */
    @Override
    public List<WorkFeeVerifyDetailExcelDto> listExcelDto(Long verifyId) {
        if (LongUtil.isZero(verifyId)) {
            throw new AppException("对账单编号不能为空");
        }
        WorkFeeVerify workFeeVerify = this.workFeeVerifyService.getById(verifyId);
        if (workFeeVerify == null) {
            throw new AppException("对账单不存在，请检查");
        }
        List<WorkFeeVerifyDetailDto> detailDtoList = this.listDtoByVerifyId(verifyId);
        List<WorkFeeVerifyDetailExcelDto> excelDtoList = new ArrayList<>();
        this.addExtraAttributes(detailDtoList, workFeeVerify);
        if (CollectionUtil.isNotEmpty(detailDtoList)) {
            for (WorkFeeVerifyDetailDto workFeeVerifyDetailDto : detailDtoList) {
                WorkFeeVerifyDetailExcelDto excelDto = new WorkFeeVerifyDetailExcelDto();
                BeanUtils.copyProperties(workFeeVerifyDetailDto, excelDto);
                if (LongUtil.isNotZero(workFeeVerifyDetailDto.getWorkId())) {
                    excelDto.setWorkId(workFeeVerifyDetailDto.getWorkId().toString());
                }
                if (workFeeVerifyDetailDto.getCreateTime() != null) {
                    excelDto.setCreateTime(FAULT_DATE_FORMAT.format(workFeeVerifyDetailDto.getCreateTime()));
                }
                if (workFeeVerifyDetailDto.getTotalFee() != null) {
                    excelDto.setTotalFee(workFeeVerifyDetailDto.getTotalFee().toString());
                }
                if (workFeeVerifyDetailDto.getVerifyAmount() != null) {
                    excelDto.setVerifyAmount(workFeeVerifyDetailDto.getVerifyAmount().toString());
                }
                if (workFeeVerifyDetailDto.getWorkFeeDto() != null) {
                    excelDto.setAssortBasicFee(workFeeVerifyDetailDto.getWorkFeeDto().getAssortBasicFee().toString());
                    excelDto.setAssortSupportFee(workFeeVerifyDetailDto.getWorkFeeDto().getAssortSupportFee().toString());
                    excelDto.setSuburbTrafficExpense(workFeeVerifyDetailDto.getWorkFeeDto().getSuburbTrafficExpense().toString());
                    excelDto.setLongTrafficExpense(workFeeVerifyDetailDto.getWorkFeeDto().getLongTrafficExpense().toString());
                    excelDto.setHotelExpense(workFeeVerifyDetailDto.getWorkFeeDto().getHotelExpense().toString());
                    excelDto.setTravelExpense(workFeeVerifyDetailDto.getWorkFeeDto().getTravelExpense().toString());
                    excelDto.setPostExpense(workFeeVerifyDetailDto.getWorkFeeDto().getPostExpense().toString());
                    excelDto.setOtherFee(workFeeVerifyDetailDto.getWorkFeeDto().getOtherFee().toString());
                }
                excelDtoList.add(excelDto);
            }
        }
        return excelDtoList;
    }

    /**
     * 根据工单编号列表获取工单编号和对账单明细的映射
     *
     * @param workIdList
     * @return
     */
    @Override
    public Map<Long, WorkFeeVerifyDetail> mapWorkIdAndDetail(List<Long> workIdList) {
        Map<Long, WorkFeeVerifyDetail> map = new HashMap<>();
        if (CollectionUtil.isEmpty(workIdList)) {
            return map;
        }
        List<WorkFeeVerifyDetail> list = this.list(new QueryWrapper<WorkFeeVerifyDetail>().in("work_id", workIdList));
        if (CollectionUtil.isNotEmpty(list)) {
            map = list.stream().collect(Collectors.toMap(item -> item.getWorkId(), item -> item));
        }
        return map;
    }

    /**
     * 添加额外属性
     *
     * @param detailList
     * @param workFeeVerify
     */
    private void addExtraAttributes(List<WorkFeeVerifyDetailDto> detailList, WorkFeeVerify workFeeVerify) {
        if (CollectionUtil.isNotEmpty(detailList)) {
            HashSet<Long> corpIdSet = new HashSet<>();
            HashSet<Long> customIdSet = new HashSet<>();
            HashSet<Long> userIdSet = new HashSet<>();
            HashSet<Long> specIdSet = new HashSet<>();
            corpIdSet.add(workFeeVerify.getDemanderCorp());
            List<Long> workIdList = new ArrayList<>();
            detailList.forEach(dto -> {
                customIdSet.add(dto.getCustomId());
                workIdList.add(dto.getWorkId());
                userIdSet.add(dto.getCreator());
                specIdSet.add(dto.getSpecification());
            });
            List<Long> corpIdList = new ArrayList<>(corpIdSet);
            List<Long> customIdList = new ArrayList<>(customIdSet);
            List<Long> userIdList = new ArrayList<>(userIdSet);
            List<Long> specIdList = new ArrayList<>(specIdSet);
            Map<Long, String> customMap = this.demanderCustomService.mapIdAndCustomNameByIdList(customIdList);
            Map<Integer, String> workTypeMap = this.workTypeService.mapWorkType(workFeeVerify.getDemanderCorp());
            Result<Map<Long, String>> smallClassMapResult = this.deviceFeignService.mapSmallClassByCorp(workFeeVerify.getDemanderCorp());
            Map<Long, String> smallClassMap = new HashMap<>();
            if (smallClassMapResult != null && smallClassMapResult.getCode() == 0) {
                smallClassMap = smallClassMapResult.getData() == null ? new HashMap<>() : smallClassMapResult.getData();
            }
            String specIdListJson = JsonUtil.toJson(specIdList);
            Result<Map<Long, String>> specMapResult = this.deviceFeignService.mapSpecificationAndNameByIdList(specIdListJson);
            Map<Long, String> specMap = new HashMap<>();
            if (specMapResult != null && Result.SUCCESS == specMapResult.getCode().intValue()) {
                specMap = specMapResult.getData() == null ? new HashMap<>() : specMapResult.getData();
            }
            Result<Map<Long, String>> brandMapResult = this.deviceFeignService.mapDeviceBrandByCorp(workFeeVerify.getDemanderCorp());
            Map<Long, String> brandMap = new HashMap<>();
            if (brandMapResult != null && brandMapResult.getCode() == 0) {
                brandMap = brandMapResult.getData() == null ? new HashMap<>() : brandMapResult.getData();
            }
            Result<Map<Long, String>> modelMapResult = this.deviceFeignService.mapDeviceModelByCorpIdList(corpIdList);
            Map<Long, String> modelMap = new HashMap<>();
            if (modelMapResult != null && modelMapResult.getCode() == 0) {
                modelMap = modelMapResult.getData() == null ? new HashMap<>() : modelMapResult.getData();
            }
            Result<Map<String, String>> areaMapResult = this.uasFeignService.mapAreaCodeAndName();
            Map<String, String> areaMap = new HashMap<>();
            if (areaMapResult != null && Result.SUCCESS == areaMapResult.getCode().intValue()) {
                areaMap = areaMapResult.getData() == null ? new HashMap<>() : areaMapResult.getData();
            }
            String userIdListJson = JsonUtil.toJson(userIdList);
            Result<Map<Long, String>> userMapResult = this.uasFeignService.mapUserIdAndNameByUserIdList(userIdListJson);
            Map<Long, String> userMap = new HashMap<>();
            if (userMapResult != null && Result.SUCCESS == userMapResult.getCode().intValue()) {
                userMap = userMapResult.getData() == null ? new HashMap<>() : userMapResult.getData();
            }
            Map<Long, WorkFeeDto> workFeeMap = this.workFeeService.mapWorkIdAndFeeDto(workIdList);
            for (WorkFeeVerifyDetailDto workFeeVerifyDetailDto : detailList) {
                workFeeVerifyDetailDto.setCustomCorpName(customMap.get(workFeeVerifyDetailDto.getCustomId()));
                workFeeVerifyDetailDto.setWorkTypeName(workTypeMap.get(workFeeVerifyDetailDto.getWorkType()));
                workFeeVerifyDetailDto.setSmallClassName(smallClassMap.get(workFeeVerifyDetailDto.getSmallClass()));
                workFeeVerifyDetailDto.setSpecificationName(specMap.get(workFeeVerifyDetailDto.getSpecification()));
                workFeeVerifyDetailDto.setBrandName(brandMap.get(workFeeVerifyDetailDto.getBrand()));
                workFeeVerifyDetailDto.setModelName(modelMap.get(workFeeVerifyDetailDto.getModel()));
                workFeeVerifyDetailDto.setWarrantyName(WarrantyEnum.getNameByCode(workFeeVerifyDetailDto.getWarranty()));
                workFeeVerifyDetailDto.setFeeConfirmStatusName(FeeConfirmStatusEnum.getNameByCode(workFeeVerifyDetailDto.getFeeConfirmStatus()));
                workFeeVerifyDetailDto.setFeeCheckStatusName(FeeCheckStatusEnum.getNameByCode(workFeeVerifyDetailDto.getFeeCheckStatus()));
                workFeeVerifyDetailDto.setCreatorName(StrUtil.trimToEmpty(userMap.get(workFeeVerifyDetailDto.getCreator())));
                String provinceName = "", cityName = "";
                if (StrUtil.isNotEmpty(workFeeVerifyDetailDto.getDistrict()) && workFeeVerifyDetailDto.getDistrict().length() >= 2) {
                    provinceName = StrUtil.trimToEmpty(areaMap.get(workFeeVerifyDetailDto.getDistrict().substring(0, 2)));
                }
                if (StrUtil.isNotEmpty(workFeeVerifyDetailDto.getDistrict()) && workFeeVerifyDetailDto.getDistrict().length() >= 4) {
                    // 如果是省直辖市
                    if (workFeeVerifyDetailDto.getDistrict().matches("\\d{2}9\\d{3}")) {
                        cityName = StrUtil.trimToEmpty(areaMap.get(workFeeVerifyDetailDto.getDistrict()));
                    } else if (workFeeVerifyDetailDto.getDistrict().matches("5002\\d{2}")) {
                        cityName = StrUtil.trimToEmpty(areaMap.get("5001"));
                    } else {
                        cityName = StrUtil.trimToEmpty(areaMap.get(workFeeVerifyDetailDto.getDistrict().substring(0, 4)));
                    }
                }
                workFeeVerifyDetailDto.setProvinceName(provinceName);
                workFeeVerifyDetailDto.setCityName(cityName);
                workFeeVerifyDetailDto.setWorkFeeDto(workFeeMap.get(workFeeVerifyDetailDto.getWorkId()));
            }
        }
    }

}
