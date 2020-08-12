package com.zjft.usp.anyfix.work.fee.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.enums.EnabledEnum;
import com.zjft.usp.anyfix.baseinfo.service.WorkTypeService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderCustomService;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDetailDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDto;
import com.zjft.usp.anyfix.work.fee.enums.FeeTypeEnum;
import com.zjft.usp.anyfix.work.fee.enums.ImplementTypeEnum;
import com.zjft.usp.anyfix.work.fee.mapper.WorkFeeMapper;
import com.zjft.usp.anyfix.work.fee.model.WorkFee;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeDetail;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeVerifyDetail;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeDetailService;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeService;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeVerifyDetailService;
import com.zjft.usp.anyfix.work.operate.model.WorkOperate;
import com.zjft.usp.anyfix.work.operate.service.WorkOperateService;
import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.device.service.DeviceFeignService;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 工单费用表 服务实现类
 * </p>
 *
 * @author canlei
 * @since 2020-01-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkFeeServiceImpl extends ServiceImpl<WorkFeeMapper, WorkFee> implements WorkFeeService {

    @Autowired
    private WorkTypeService workTypeService;
    @Autowired
    private DemanderCustomService demanderCustomService;
    @Autowired
    private WorkFeeVerifyDetailService workFeeVerifyDetailService;
    @Autowired
    private WorkFeeDetailService workFeeDetailService;
    @Autowired
    private WorkOperateService workOperateService;

    @Resource
    private UasFeignService uasFeignService;
    @Resource
    private DeviceFeignService deviceFeignService;

    /**
     * 根据工单编号查询
     *
     * @param workId
     * @return
     * @author canlei
     */
    @Override
    public WorkFeeDto getDtoById(Long workId) {
        if (LongUtil.isZero(workId)) {
            return null;
        }
        WorkFee workFee = this.getById(workId);
        if (workFee != null) {
            WorkFeeDto workFeeDto = new WorkFeeDto();
            BeanUtils.copyProperties(workFee, workFeeDto);
            WorkFeeVerifyDetail workFeeVerifyDetail = this.workFeeVerifyDetailService.findByWorkId(workId);
            if (workFeeVerifyDetail != null) {
                workFeeDto.setVerifyAmount(workFeeVerifyDetail.getVerifyAmount());
                workFeeDto.setVerifyNote(workFeeVerifyDetail.getNote());
            }
            List<WorkFeeDetailDto> detailList = this.workFeeDetailService.listByWorkId(workId);
            workFeeDto.setDetailList(detailList);
            workFeeDto.setAssortBasicFee(BigDecimal.ZERO);
            workFeeDto.setAssortSupportFee(BigDecimal.ZERO);
            workFeeDto.setSuburbTrafficExpense(BigDecimal.ZERO);
            workFeeDto.setLongTrafficExpense(BigDecimal.ZERO);
            workFeeDto.setTravelExpense(BigDecimal.ZERO);
            workFeeDto.setPostExpense(BigDecimal.ZERO);
            workFeeDto.setHotelExpense(BigDecimal.ZERO);
            if (CollectionUtil.isNotEmpty(detailList)) {
                for (WorkFeeDetailDto workFeeDetailDto : detailList) {
                    // 工单收费
                    if (FeeTypeEnum.ASSORT.getCode() == workFeeDetailDto.getFeeType()) {
                        // 辅助人员费用
                        if (EnabledEnum.YES.getCode().equals(workFeeDetailDto.getTogether())) {
                            workFeeDto.setAssortSupportFee(workFeeDto.getAssortSupportFee().add(workFeeDetailDto.getAmount()));
                            // 基础服务费
                        } else {
                            workFeeDto.setAssortBasicFee(workFeeDto.getAssortBasicFee().add(workFeeDetailDto.getAmount()));
                        }
                        // 工单支出费用
                    } else if (FeeTypeEnum.IMPLEMENT.getCode() == workFeeDetailDto.getFeeType()) {
                        // 郊区交通费
                        if (ImplementTypeEnum.SUBURB_TRAFFIC_EXPENSE.getCode().equals(workFeeDetailDto.getImplementType())) {
                            workFeeDto.setSuburbTrafficExpense(workFeeDto.getSuburbTrafficExpense().add(workFeeDetailDto.getAmount()));
                        } else if (ImplementTypeEnum.LONG_TRAFFIC_EXPENSE.getCode().equals(workFeeDetailDto.getImplementType())) {
                            workFeeDto.setLongTrafficExpense(workFeeDto.getLongTrafficExpense().add(workFeeDetailDto.getAmount()));
                        } else if (ImplementTypeEnum.HOTEL_EXPENSE.getCode().equals(workFeeDetailDto.getImplementType())) {
                            workFeeDto.setHotelExpense(workFeeDto.getHotelExpense().add(workFeeDetailDto.getAmount()));
                        } else if (ImplementTypeEnum.TRAVEL_EXPENSE.getCode().equals(workFeeDetailDto.getImplementType())) {
                            workFeeDto.setTravelExpense(workFeeDto.getTravelExpense().add(workFeeDetailDto.getAmount()));
                        } else if (ImplementTypeEnum.POST_EXPENSE.getCode().equals(workFeeDetailDto.getImplementType())) {
                            workFeeDto.setPostExpense(workFeeDetailDto.getAmount());
                        }
                    }
                }
            }
            return workFeeDto;
        }
        return null;
    }

    /**
     * 更新
     *
     * @param workFeeDto
     * @param curUserId
     * @author canlei
     */
    @Override
    public void update(WorkFeeDto workFeeDto, Long curUserId, Long curCorpId) {
        if (workFeeDto == null || LongUtil.isZero(workFeeDto.getWorkId())) {
            throw new AppException("工单编号不能为空");
        }
        WorkFee workFee = this.getById(workFeeDto.getWorkId());
        if (workFee == null) {
            throw new AppException("该工单费用记录不存在，请检查");
        }
        workFee.setBasicServiceFee(workFeeDto.getBasicServiceFee());
        workFee.setOtherFee(workFeeDto.getOtherFee());
        workFee.setOtherFeeNote(workFeeDto.getOtherFeeNote());
        if (CollectionUtil.isNotEmpty(workFeeDto.getDetailList())) {
            // 明细只能更新工单支出费用
            BigDecimal implementFee = BigDecimal.ZERO;
            List<WorkFeeDetail> implementFeeList = new ArrayList<>();
            for (WorkFeeDetailDto workFeeDetailDto : workFeeDto.getDetailList()) {
                if (FeeTypeEnum.IMPLEMENT.getCode().equals(workFeeDetailDto.getFeeType())) {
                    WorkFeeDetail workFeeDetail = new WorkFeeDetail();
                    workFeeDetail.setDetailId(KeyUtil.getId());
                    workFeeDetail.setAmount(workFeeDetailDto.getAmount() == null ? BigDecimal.ZERO : workFeeDetailDto.getAmount());
                    workFeeDetail.setNote(workFeeDetailDto.getNote());
                    workFeeDetail.setWorkId(workFeeDto.getWorkId());
                    workFeeDetail.setFeeId(workFeeDetailDto.getImplementId());
                    workFeeDetail.setFeeType(FeeTypeEnum.IMPLEMENT.getCode());
                    workFeeDetail.setOperator(curUserId);
                    workFeeDetail.setOperateTime(DateUtil.date());
                    implementFeeList.add(workFeeDetail);
                    implementFee = implementFee.add(workFeeDetailDto.getAmount() == null ? BigDecimal.ZERO : workFeeDetailDto.getAmount());
                }
            }
            // 先删除后添加
            this.workFeeDetailService.delImplementById(workFeeDto.getWorkId());
            if (CollectionUtil.isNotEmpty(implementFeeList)) {
                this.workFeeDetailService.saveBatch(implementFeeList);
            }
            workFee.setImplementFee(implementFee);
        }
        // 设置总费用
        workFee.setTotalFee(this.getTotalFee(workFee));
        this.updateById(workFee);
        // 添加操作记录
        WorkOperate workOperate = new WorkOperate();
        workOperate.setWorkId(workFeeDto.getWorkId());
        workOperate.setWorkStatus(WorkStatusEnum.CLOSED.getCode());
        workOperate.setOperator(curUserId);
        workOperate.setCorp(curCorpId);
        workOperateService.addWorkOperateByUpdateWorkFee(workOperate);
    }

    /**
     * 根据条件分页查询
     *
     * @author canlei
     * @param workFilter
     * @return
     */
    @Override
    public ListWrapper<WorkFeeDto> queryByWorkFilter(WorkFilter workFilter) {
        ListWrapper<WorkFeeDto> listWrapper = new ListWrapper<>();
        if (workFilter == null || LongUtil.isZero(workFilter.getDemanderCorp()) || LongUtil.isZero(workFilter.getServiceCorp())) {
            return listWrapper;
        }
        Page page = new Page(workFilter.getPageNum(), workFilter.getPageSize());
        List<WorkFeeDto> dtoList = this.baseMapper.queryByWorkFilter(page, workFilter);
        if (CollectionUtil.isNotEmpty(dtoList)) {
            HashSet<Long> set = new HashSet<>();
            HashSet<Long> customIdSet = new HashSet<>();
            set.add(workFilter.getDemanderCorp());
            dtoList.forEach(workFeeDto -> {
                customIdSet.add(workFeeDto.getCustomId());
            });
            List<Long> corpIdList = new ArrayList<>(set);
            List<Long> customIdList = new ArrayList<>(customIdSet);

            Map<Long, String> customMap = this.demanderCustomService.mapIdAndCustomNameByIdList(customIdList);
            Map<Integer, String> workTypeMap = this.workTypeService.mapWorkType(workFilter.getDemanderCorp());
            Result<Map<Long, String>> smallClassMapResult = this.deviceFeignService.mapSmallClassByCorp(workFilter.getDemanderCorp());
            Map<Long, String> smallClassMap = new HashMap<>();
            if (smallClassMapResult != null && smallClassMapResult.getCode() == 0) {
                smallClassMap = smallClassMapResult.getData() == null ? new HashMap<>() : smallClassMapResult.getData();
            }
            Result<Map<Long, String>> brandMapResult = this.deviceFeignService.mapDeviceBrandByCorp(workFilter.getDemanderCorp());
            Map<Long, String> brandMap = new HashMap<>();
            if (brandMapResult != null && brandMapResult.getCode() == 0) {
                brandMap = brandMapResult.getData() == null ? new HashMap<>() : brandMapResult.getData();
            }
            Result<Map<Long, String>> modelMapResult = this.deviceFeignService.mapDeviceModelByCorpIdList(corpIdList);
            Map<Long, String> modelMap = new HashMap<>();
            if (modelMapResult != null && modelMapResult.getCode() == 0) {
                modelMap = modelMapResult.getData() == null ? new HashMap<>() : modelMapResult.getData();
            }
            Result<Map<Long, String>> specificationMapResult = this.deviceFeignService.mapSpecificationByCorp(workFilter.getDemanderCorp());
            Map<Long, String> specificationMap = new HashMap<>();
            if (specificationMapResult != null && specificationMapResult.getCode() == 0) {
                specificationMap = specificationMapResult.getData() == null ? new HashMap<>() : specificationMapResult.getData();
            }

            for (WorkFeeDto workFeeDto: dtoList) {
                workFeeDto.setCustomCorpName(customMap.get(workFeeDto.getCustomId()));
                workFeeDto.setWorkTypeName(workTypeMap.get(workFeeDto.getWorkType()));
                workFeeDto.setSmallClassName(smallClassMap.get(workFeeDto.getSmallClassId()));
                workFeeDto.setBrandName(brandMap.get(workFeeDto.getBrand()));
                workFeeDto.setModelName(modelMap.get(workFeeDto.getModel()));
                workFeeDto.setSpecificationName(specificationMap.get(workFeeDto.getSpecification()));
            }

            listWrapper.setList(dtoList);
            listWrapper.setTotal(page.getTotal());
        }
        return listWrapper;
    }

    /**
     * 根据条件查询
     *
     * @author canlei
     * @param workFilter
     * @return
     */
    @Override
    public List<WorkFeeDto> listByWorkFilter(WorkFilter workFilter) {
        List<WorkFeeDto> dtoList = new ArrayList<>();
        if (workFilter == null || LongUtil.isZero(workFilter.getDemanderCorp()) || LongUtil.isZero(workFilter.getServiceCorp())) {
            return dtoList;
        }
        dtoList = this.baseMapper.listByWorkFilter(workFilter);
        if (CollectionUtil.isNotEmpty(dtoList)) {
            HashSet<Long> set = new HashSet<>();
            HashSet<Long> customIdSet = new HashSet<>();
            set.add(workFilter.getDemanderCorp());
            dtoList.forEach(workFeeDto -> {
                customIdSet.add(workFeeDto.getCustomId());
            });
            List<Long> corpIdList = new ArrayList<>(set);
            List<Long> customIdList = new ArrayList<>(customIdSet);

            Map<Long, String> customMap = this.demanderCustomService.mapIdAndCustomNameByIdList(customIdList);
            Result<Map<Long, String>> corpMapResult = this.uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList));
            Map<Long, String> corpMap = new HashMap<>();
            if (corpMapResult != null && corpMapResult.getCode() == 0) {
                corpMap = corpMapResult.getData() == null ? new HashMap<>() : corpMapResult.getData();
            }
            Map<Integer, String> workTypeMap = this.workTypeService.mapWorkType(workFilter.getDemanderCorp());
            Result<Map<Long, String>> smallClassMapResult = this.deviceFeignService.mapSmallClassByCorp(workFilter.getDemanderCorp());
            Map<Long, String> smallClassMap = new HashMap<>();
            if (smallClassMapResult != null && smallClassMapResult.getCode() == 0) {
                smallClassMap = smallClassMapResult.getData() == null ? new HashMap<>() : smallClassMapResult.getData();
            }
            Result<Map<Long, String>> brandMapResult = this.deviceFeignService.mapDeviceBrandByCorp(workFilter.getDemanderCorp());
            Map<Long, String> brandMap = new HashMap<>();
            if (brandMapResult != null && brandMapResult.getCode() == 0) {
                brandMap = brandMapResult.getData() == null ? new HashMap<>() : brandMapResult.getData();
            }
            Result<Map<Long, String>> modelMapResult = this.deviceFeignService.mapDeviceModelByCorpIdList(corpIdList);
            Map<Long, String> modelMap = new HashMap<>();
            if (modelMapResult != null && modelMapResult.getCode() == 0) {
                modelMap = modelMapResult.getData() == null ? new HashMap<>() : modelMapResult.getData();
            }
            Result<Map<Long, String>> specificationMapResult = this.deviceFeignService.mapSpecificationByCorp(workFilter.getDemanderCorp());
            Map<Long, String> specificationMap = new HashMap<>();
            if (specificationMapResult != null && specificationMapResult.getCode() == 0) {
                specificationMap = specificationMapResult.getData() == null ? new HashMap<>() : specificationMapResult.getData();
            }

            for (WorkFeeDto workFeeDto: dtoList) {
                workFeeDto.setCustomCorpName(customMap.get(workFeeDto.getCustomId()));
                workFeeDto.setWorkTypeName(workTypeMap.get(workFeeDto.getWorkType()));
                workFeeDto.setSmallClassName(smallClassMap.get(workFeeDto.getSmallClassId()));
                workFeeDto.setBrandName(brandMap.get(workFeeDto.getBrand()));
                workFeeDto.setModelName(modelMap.get(workFeeDto.getModel()));
                workFeeDto.setSpecificationName(specificationMap.get(workFeeDto.getSpecification()));
            }
        }
        return dtoList;
    }

    /**
     * 获取总费用
     *
     * @param workFee
     * @return
     */
    @Override
    public BigDecimal getTotalFee(WorkFee workFee) {
        if (workFee.getBasicServiceFee() == null) {
            workFee.setBasicServiceFee(BigDecimal.ZERO);
        }
        return workFee.getBasicServiceFee()
                .add(workFee.getAssortFee() == null ? BigDecimal.ZERO : workFee.getAssortFee())
                .add(workFee.getImplementFee() == null ? BigDecimal.ZERO : workFee.getImplementFee())
                .add(workFee.getWareUseFee() == null ? BigDecimal.ZERO : workFee.getWareUseFee())
                .add(workFee.getOtherFee() == null ? BigDecimal.ZERO : workFee.getOtherFee());
    }

    /**
     * 根据工单列表编号获取工单编号与工单费用的映射
     *
     * @param workIdList
     * @return
     */
    @Override
    public Map<Long, WorkFee> mapWorkFee(List<Long> workIdList) {
        Map<Long, WorkFee> map = new HashMap<>();
        if (CollectionUtil.isEmpty(workIdList)) {
            return map;
        }
        List<WorkFee> list = this.list(new QueryWrapper<WorkFee>().in("work_id", workIdList));
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(workFee -> {
                map.put(workFee.getWorkId(), workFee);
            });
        }
        return map;
    }

    /**
     * 根据对账单号列表查询
     *
     * @param verifyIdList
     * @return
     */
    @Override
    public List<WorkFeeDto> listByVerifyIdList(List<Long> verifyIdList) {
        if (CollectionUtil.isEmpty(verifyIdList)) {
            return null;
        }
        List<WorkFeeDto> list = this.baseMapper.listByVerifyIdList(verifyIdList);
        return list;
    }

    /**
     * 根据工单编号列表获取工单编号与WorkFeeDto的映射
     *
     * @param workIdList
     * @return
     */
    @Override
    public Map<Long, WorkFeeDto> mapWorkIdAndFeeDto(List<Long> workIdList) {
        Map<Long, WorkFeeDto> map = new HashMap<>();
        if (CollectionUtil.isEmpty(workIdList)) {
            return map;
        }
        List<WorkFeeDetailDto> detailDtoList = this.workFeeDetailService.listDtoByWorkIdList(workIdList);
        Map<Long, WorkFee> workFeeMap = this.mapWorkFee(workIdList);
        for (Map.Entry<Long, WorkFee> entry : workFeeMap.entrySet()) {
            WorkFeeDto workFeeDto = new WorkFeeDto();
            BeanUtils.copyProperties(entry.getValue(), workFeeDto);
            // 初始化bigdecimal null值置为0
            workFeeDto.setBasicServiceFee(workFeeDto.getBasicServiceFee() == null ? BigDecimal.ZERO : workFeeDto.getBasicServiceFee());
            workFeeDto.setAssortBasicFee(workFeeDto.getAssortBasicFee() == null ? BigDecimal.ZERO : workFeeDto.getAssortBasicFee());
            workFeeDto.setAssortSupportFee(workFeeDto.getAssortSupportFee() == null ? BigDecimal.ZERO : workFeeDto.getAssortSupportFee());
            workFeeDto.setSuburbTrafficExpense(workFeeDto.getSuburbTrafficExpense() == null ? BigDecimal.ZERO : workFeeDto.getSuburbTrafficExpense());
            workFeeDto.setLongTrafficExpense(workFeeDto.getLongTrafficExpense() == null ? BigDecimal.ZERO : workFeeDto.getLongTrafficExpense());
            workFeeDto.setHotelExpense(workFeeDto.getHotelExpense() == null ? BigDecimal.ZERO : workFeeDto.getHotelExpense());
            workFeeDto.setTravelExpense(workFeeDto.getTravelExpense() == null ? BigDecimal.ZERO : workFeeDto.getTravelExpense());
            workFeeDto.setPostExpense(workFeeDto.getPostExpense() == null ? BigDecimal.ZERO : workFeeDto.getPostExpense());
            map.put(entry.getKey(), workFeeDto);
        }
        if (CollectionUtil.isNotEmpty(detailDtoList)) {
            for (WorkFeeDetailDto workFeeDetailDto : detailDtoList) {
                WorkFeeDto workFeeDto = map.get(workFeeDetailDto.getWorkId());
                if (workFeeDto == null) {
                    workFeeDto = new WorkFeeDto();
                }
                workFeeDto.setWorkId(workFeeDetailDto.getWorkId());

                // 基础维护费或辅助人员费用
                if (FeeTypeEnum.ASSORT.getCode().equals(workFeeDetailDto.getFeeType())) {
                    if (EnabledEnum.YES.getCode().equals(workFeeDetailDto.getTogether())) {
                        workFeeDto.setAssortSupportFee(workFeeDto.getAssortSupportFee().add(workFeeDetailDto.getAmount()));
                    } else {
                        workFeeDto.setAssortBasicFee(workFeeDto.getAssortBasicFee().add(workFeeDetailDto.getAmount()));
                    }
                // 工单支出费用
                } else if (FeeTypeEnum.IMPLEMENT.getCode().equals(workFeeDetailDto.getFeeType())) {
                    if (ImplementTypeEnum.SUBURB_TRAFFIC_EXPENSE.getCode().equals(workFeeDetailDto.getImplementType())) {
                        workFeeDto.setSuburbTrafficExpense(workFeeDto.getSuburbTrafficExpense().add(workFeeDetailDto.getAmount()));
                    } else if (ImplementTypeEnum.LONG_TRAFFIC_EXPENSE.getCode().equals(workFeeDetailDto.getImplementType())) {
                        workFeeDto.setLongTrafficExpense(workFeeDto.getLongTrafficExpense().add(workFeeDetailDto.getAmount()));
                    } else if (ImplementTypeEnum.HOTEL_EXPENSE.getCode().equals(workFeeDetailDto.getImplementType())) {
                        workFeeDto.setHotelExpense(workFeeDto.getHotelExpense().add(workFeeDetailDto.getAmount()));
                    } else if (ImplementTypeEnum.TRAVEL_EXPENSE.getCode().equals(workFeeDetailDto.getImplementType())) {
                        workFeeDto.setTravelExpense(workFeeDto.getTravelExpense().add(workFeeDetailDto.getAmount()));
                    } else if (ImplementTypeEnum.POST_EXPENSE.getCode().equals(workFeeDetailDto.getImplementType())) {
                        workFeeDto.setPostExpense(workFeeDto.getPostExpense().add(workFeeDetailDto.getAmount()));
                    }
                }
                map.put(workFeeDetailDto.getWorkId(), workFeeDto);
            }
        }
        return map;
    }


}
