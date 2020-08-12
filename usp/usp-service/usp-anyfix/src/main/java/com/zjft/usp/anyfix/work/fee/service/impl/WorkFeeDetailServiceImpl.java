package com.zjft.usp.anyfix.work.fee.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.enums.EnabledEnum;
import com.zjft.usp.anyfix.common.feign.dto.DeviceSmallClassDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeAssortDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDetailDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeImplementDto;
import com.zjft.usp.anyfix.work.fee.enums.FeeTypeEnum;
import com.zjft.usp.anyfix.work.fee.filter.WorkFeeDetailFilter;
import com.zjft.usp.anyfix.work.fee.mapper.WorkFeeDetailMapper;
import com.zjft.usp.anyfix.work.fee.model.WorkFee;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeDetail;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeAssortDefineService;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeDetailService;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeService;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.device.service.DeviceFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 工单费用明细 服务实现类
 * </p>
 *
 * @author canlei
 * @since 2020-04-17
 */
@Service
public class WorkFeeDetailServiceImpl extends ServiceImpl<WorkFeeDetailMapper, WorkFeeDetail> implements WorkFeeDetailService {

    @Autowired
    private WorkFeeAssortDefineService workFeeAssortDefineService;
    @Autowired
    private WorkFeeService workFeeService;
    @Autowired
    private DeviceFeignService deviceFeignService;

    /**
     * 根据实施发生费用编号获取费用明细
     *
     * @param implementId
     * @return
     */
    @Override
    public List<WorkFeeDetail> listByImplementId(Long implementId) {
        if (LongUtil.isZero(implementId)) {
            return null;
        }
        List<WorkFeeDetail> workFeeDetails = this.list(new QueryWrapper<WorkFeeDetail>()
                .eq("fee_id", implementId).eq("fee_type", FeeTypeEnum.IMPLEMENT.getCode()));
        return workFeeDetails;
    }

    /**
     * 根据分类费用编号获取已审核工单费用明细
     *
     * @param assortId
     * @return
     */
    @Override
    public List<WorkFeeDetail> listCheckedByAssortId(Long assortId) {
        return this.baseMapper.listCheckedByAssortId(assortId);
    }

    /**
     * 根据工单支出费用生成工单费用明细，返回总额
     *
     * @param implementDtoList
     * @param workId
     * @param userId
     */
    @Override
    public BigDecimal addByImplementFeeList(List<WorkFeeImplementDto> implementDtoList, Long workId, Long userId) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(implementDtoList)) {
            List<WorkFeeDetail> workFeeDetailList = new ArrayList<>();
            for (WorkFeeImplementDto workFeeImplementDto : implementDtoList) {
                totalAmount = totalAmount.add(workFeeImplementDto.getAmount() == null ? BigDecimal.ZERO : workFeeImplementDto.getAmount());
                WorkFeeDetail workFeeDetail = new WorkFeeDetail();
                workFeeDetail.setDetailId(KeyUtil.getId());
                workFeeDetail.setAmount(workFeeImplementDto.getAmount() == null ? BigDecimal.ZERO : workFeeImplementDto.getAmount());
                workFeeDetail.setNote(StrUtil.trimToEmpty(workFeeImplementDto.getNote()));
                workFeeDetail.setFeeType(FeeTypeEnum.IMPLEMENT.getCode());
                workFeeDetail.setFeeId(workFeeImplementDto.getImplementId());
                workFeeDetail.setWorkId(workId);
                workFeeDetail.setOperateTime(DateTime.now());
                workFeeDetail.setOperator(userId);
                workFeeDetailList.add(workFeeDetail);
            }
            // 先删除再新增
            this.remove(new QueryWrapper<WorkFeeDetail>().eq("work_id", workId)
                    .eq("fee_type", FeeTypeEnum.IMPLEMENT.getCode()));
            if (CollectionUtil.isNotEmpty(workFeeDetailList)) {
                this.saveBatch(workFeeDetailList);
            }
        }
        return totalAmount;
    }

    /**
     * 生成工单收费规则生成工单费用明细，返回总额
     *
     * @param workDto
     * @param userId
     */
    @Override
    public BigDecimal addAssortFeeDetail(WorkDto workDto, Long userId) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        // 由于工单表未保存设备大类编号，所以需要单独从设备小类获取
        if (LongUtil.isNotZero(workDto.getSmallClass())) {
            Result smallClassDtoResult = this.deviceFeignService.findDeviceSmallClass(workDto.getSmallClass());
            if (smallClassDtoResult != null && Result.SUCCESS == smallClassDtoResult.getCode().intValue()) {
                DeviceSmallClassDto deviceSmallClassDto = JsonUtil.parseObject(JsonUtil.toJson(smallClassDtoResult.getData()),
                        DeviceSmallClassDto.class);
                if (deviceSmallClassDto != null) {
                    workDto.setLargeClassId(deviceSmallClassDto.getLargeClassId());
                }
            }
        }
        List<WorkFeeDetail> workFeeDetailList = new ArrayList<>();
        List<WorkFeeAssortDto> workFeeAssortDtoList = this.workFeeAssortDefineService.matchWorkFeeAssortDefine(workDto);
        if (CollectionUtil.isNotEmpty(workFeeAssortDtoList)) {
            for (WorkFeeAssortDto workFeeAssortDto : workFeeAssortDtoList) {
                WorkFeeDetail workFeeDetail = new WorkFeeDetail();
                // 是否用于协同工程师单独判断
                if (EnabledEnum.YES.getCode().equals(workFeeAssortDto.getTogether())) {
                    if (StrUtil.isBlank(workDto.getTogetherEngineers()) && StrUtil.isBlank(workDto.getHelpNames())) {
                        continue;
                    }
                    int togetherNum = 0;
                    if (StrUtil.isNotEmpty(workDto.getTogetherEngineers())) {
                        String[] togetherEngineers = workDto.getTogetherEngineers().split(",");
                        togetherNum += togetherEngineers.length;
                    }
                    if (StrUtil.isNotEmpty(workDto.getHelpNames())) {
                        String[] helpNames = workDto.getHelpNames().split(",");
                        togetherNum += helpNames.length;
                    }
                    workFeeDetail.setAmount(workFeeAssortDto.getAmount() == null ?
                            BigDecimal.ZERO : workFeeAssortDto.getAmount().multiply(BigDecimal.valueOf(togetherNum)));
                    totalAmount = totalAmount.add(workFeeDetail.getAmount());
                } else {
                    workFeeDetail.setAmount(workFeeAssortDto.getAmount() == null ? BigDecimal.ZERO : workFeeAssortDto.getAmount());
                    totalAmount = totalAmount.add(workFeeAssortDto.getAmount() == null ? BigDecimal.ZERO : workFeeAssortDto.getAmount());
                }
                workFeeDetail.setDetailId(KeyUtil.getId());
                workFeeDetail.setWorkId(workDto.getWorkId());
                workFeeDetail.setFeeType(FeeTypeEnum.ASSORT.getCode());
                workFeeDetail.setFeeId(workFeeAssortDto.getAssortId());
                // 将规则备注置为工单费用明细备注
                workFeeDetail.setNote(workFeeAssortDto.getNote());
                workFeeDetail.setOperator(userId);
                workFeeDetail.setOperateTime(DateTime.now());
                workFeeDetailList.add(workFeeDetail);
            }
        }
        // 先删除再新增
        this.remove(new QueryWrapper<WorkFeeDetail>().eq("work_id", workDto.getWorkId())
                .eq("fee_type", FeeTypeEnum.ASSORT.getCode()));
        if (CollectionUtil.isNotEmpty(workFeeDetailList)) {
            this.saveBatch(workFeeDetailList);
        }
        return totalAmount;
    }

    /**
     * 获取工单费用明细
     *
     * @param workId
     * @return
     */
    @Override
    public List<WorkFeeDetailDto> listByWorkId(Long workId) {
        if (LongUtil.isZero(workId)) {
            return null;
        }
        List<WorkFeeDetailDto> dtoList = this.baseMapper.listDtoByWorkId(workId);
        if (CollectionUtil.isNotEmpty(dtoList)) {
            dtoList.forEach(workFeeDetailDto -> {
                workFeeDetailDto.setFeeTypeName(FeeTypeEnum.getNameByCode(workFeeDetailDto.getFeeType()));
                if (FeeTypeEnum.ASSORT.getCode() == workFeeDetailDto.getFeeType()) {
                    workFeeDetailDto.setFeeName(workFeeDetailDto.getAssortName());
                } else if (FeeTypeEnum.IMPLEMENT.getCode() == workFeeDetailDto.getFeeType()) {
                    workFeeDetailDto.setFeeName(workFeeDetailDto.getImplementName());
                }
            });
        }
        return dtoList;
    }

    /**
     * 查询工单费用明细
     *
     * @param workFeeDetailFilter
     * @return
     */
    @Override
    public List<WorkFeeDetailDto> listByFilter(WorkFeeDetailFilter workFeeDetailFilter) {
        if (workFeeDetailFilter == null || LongUtil.isZero(workFeeDetailFilter.getWorkId())) {
            return null;
        }
        return this.baseMapper.listDtoByFilter(workFeeDetailFilter);
    }

    /**
     * 添加分类费用定义时，自动初始化未审核且满足条件的工单
     *
     * @param workFeeAssortDto
     * @param userId
     */
    @Override
    public void addDetailByAssort(WorkFeeAssortDto workFeeAssortDto, Long userId) {
        // 由于工单表里没有保存设备大类，所以这里做特殊处理，利用设备大类查询设备小类列表，从而根据设备小类列表去做匹配
        if (LongUtil.isNotZero(workFeeAssortDto.getLargeClassId())) {
            Result<List<Long>> smallClassIdListResult = this.deviceFeignService.listSmallClassIdByLargeClassId(workFeeAssortDto.getLargeClassId());
            if (smallClassIdListResult != null && Result.SUCCESS == smallClassIdListResult.getCode().intValue()) {
                workFeeAssortDto.setSmallClassIdList(smallClassIdListResult.getData());
            }
        }
        // 获取满足条件的未审核工单
        List<WorkDto> workDtoList = this.workFeeAssortDefineService.selectMatchWork(workFeeAssortDto);
        if (CollectionUtil.isNotEmpty(workDtoList)) {
            List<Long> workIdList = new ArrayList<>();
            workDtoList.forEach((workDto -> {
                workIdList.add(workDto.getWorkId());
            }));
            Map<Long, WorkFee> workFeeMap = this.workFeeService.mapWorkFee(workIdList);

            List<WorkFeeDetail> detailList = new ArrayList<>();
            List<WorkFee> workFeeList = new ArrayList<>();
            for (WorkDto workDto: workDtoList) {
                WorkFeeDetail workFeeDetail = new WorkFeeDetail();
                workFeeDetail.setWorkId(workDto.getWorkId());
                // 是否用于协同工程师单独判断
                if (EnabledEnum.YES.getCode().equals(workFeeAssortDto.getTogether())) {
                    if (StrUtil.isBlank(workDto.getTogetherEngineers()) && StrUtil.isBlank(workDto.getHelpNames())) {
                        continue;
                    }
                    int togetherNum = 0;
                    if (StrUtil.isNotEmpty(workDto.getTogetherEngineers())) {
                        String[] togetherEngineers = workDto.getTogetherEngineers().split(",");
                        togetherNum += togetherEngineers.length;
                    }
                    if (StrUtil.isNotEmpty(workDto.getHelpNames())) {
                        String[] helpNames = workDto.getHelpNames().split(",");
                        togetherNum += helpNames.length;
                    }
                    workFeeDetail.setAmount(workFeeAssortDto.getAmount().multiply(BigDecimal.valueOf(togetherNum)));
                } else {
                    workFeeDetail.setAmount(workFeeAssortDto.getAmount());
                }
                workFeeDetail.setDetailId(KeyUtil.getId());
                workFeeDetail.setFeeType(FeeTypeEnum.ASSORT.getCode());
                workFeeDetail.setFeeId(workFeeAssortDto.getAssortId());
                workFeeDetail.setNote(workFeeAssortDto.getNote());
                workFeeDetail.setOperator(userId);
                workFeeDetail.setOperateTime(DateTime.now());
                detailList.add(workFeeDetail);
                WorkFee workFee = workFeeMap.get(workDto.getWorkId());
                workFee.setAssortFee(workFee.getAssortFee().add(workFeeDetail.getAmount()));
                workFee.setTotalFee(workFee.getTotalFee().add(workFeeDetail.getAmount()));
                workFeeList.add(workFee);
            }
            // 增加工单费用明细
            this.saveBatch(detailList);
            // 修改对应工单费用
            this.workFeeService.updateBatchById(workFeeList);
        }
    }

    /**
     * 根据分类费用编号以及工单编号列表获取工单编号和对应费用明细的映射
     *
     * @param assortId
     * @param workIdList
     * @return
     */
    @Override
    public Map<Long, WorkFeeDetail> mapWorkIdAndFeeDetailByAssortId(Long assortId, List<Long> workIdList) {
        Map<Long, WorkFeeDetail> map = new HashMap<>();
        if (LongUtil.isZero(assortId) || CollectionUtil.isEmpty(workIdList)) {
            return map;
        }
        List<WorkFeeDetail> list = this.list(new QueryWrapper<WorkFeeDetail>().eq("fee_type", FeeTypeEnum.ASSORT.getCode())
                .eq("fee_id", assortId).in("work_id", workIdList));
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(workFeeDetail -> {
                map.put(workFeeDetail.getWorkId(), workFeeDetail);
            });
        }
        return map;
    }

    /**
     * 修改分类费用定义时，自动初始化未审核且满足条件的工单
     *
     * @param workFeeAssortDto
     * @param userId
     */
    @Override
    public void updateDetailByAssort(WorkFeeAssortDto workFeeAssortDto, Long userId) {
        // 由于工单表里没有保存设备大类，所以这里做特殊处理，利用设备大类查询设备小类列表，从而根据设备小类列表去做匹配
        if (LongUtil.isNotZero(workFeeAssortDto.getLargeClassId())) {
            Result<List<Long>> smallClassIdListResult = this.deviceFeignService.listSmallClassIdByLargeClassId(workFeeAssortDto.getLargeClassId());
            if (smallClassIdListResult != null && Result.SUCCESS == smallClassIdListResult.getCode().intValue()) {
                workFeeAssortDto.setSmallClassIdList(smallClassIdListResult.getData());
            }
        }
        // 获取满足条件的未审核工单
        List<WorkDto> workDtoList = this.workFeeAssortDefineService.selectMatchWork(workFeeAssortDto);
        if (CollectionUtil.isNotEmpty(workDtoList)) {
            List<Long> workIdList = new ArrayList<>();
            workDtoList.forEach((workDto -> {
                workIdList.add(workDto.getWorkId());
            }));
            Map<Long, WorkFee> workFeeMap = this.workFeeService.mapWorkFee(workIdList);
            Map<Long, WorkFeeDetail> workFeeDetailMap = this.mapWorkIdAndFeeDetailByAssortId(workFeeAssortDto.getAssortId(), workIdList);

            List<WorkFeeDetail> detailList = new ArrayList<>();
            List<WorkFee> workFeeList = new ArrayList<>();
            for (WorkDto workDto: workDtoList) {
                WorkFeeDetail workFeeDetail = workFeeDetailMap.get(workDto.getWorkId());
                if (workFeeDetail == null) {
                    workFeeDetail = new WorkFeeDetail();
                    workFeeDetail.setDetailId(KeyUtil.getId());
                }
                BigDecimal oldAssortFee = workFeeDetail.getAmount() == null ? BigDecimal.ZERO : workFeeDetail.getAmount();
                workFeeDetail.setWorkId(workDto.getWorkId());

                // 是否用于协同工程师单独判断
                if (EnabledEnum.YES.getCode().equals(workFeeAssortDto.getTogether())) {
                    if (StrUtil.isBlank(workDto.getTogetherEngineers()) && StrUtil.isBlank(workDto.getHelpNames())) {
                        continue;
                    }
                    int togetherNum = 0;
                    if (StrUtil.isNotEmpty(workDto.getTogetherEngineers())) {
                        String[] togetherEngineers = workDto.getTogetherEngineers().split(",");
                        togetherNum += togetherEngineers.length;
                    }
                    if (StrUtil.isNotEmpty(workDto.getHelpNames())) {
                        String[] helpNames = workDto.getHelpNames().split(",");
                        togetherNum += helpNames.length;
                    }
                    workFeeDetail.setAmount(workFeeAssortDto.getAmount().multiply(BigDecimal.valueOf(togetherNum)));
                } else {
                    workFeeDetail.setAmount(workFeeAssortDto.getAmount());
                }
                workFeeDetail.setFeeType(FeeTypeEnum.ASSORT.getCode());
                workFeeDetail.setFeeId(workFeeAssortDto.getAssortId());
                // 备注置为规则定义备注
                workFeeDetail.setNote(workFeeAssortDto.getNote());
                workFeeDetail.setOperator(userId);
                workFeeDetail.setOperateTime(DateTime.now());
                detailList.add(workFeeDetail);
                WorkFee workFee = workFeeMap.get(workDto.getWorkId());
                workFee.setAssortFee(workFee.getAssortFee().subtract(oldAssortFee).add(workFeeDetail.getAmount()));
                workFee.setTotalFee(workFee.getTotalFee().subtract(oldAssortFee).add(workFeeDetail.getAmount()));
                workFeeList.add(workFee);
            }
            // 增加工单费用明细
            this.saveOrUpdateBatch(detailList);
            // 修改对应工单费用
            this.workFeeService.updateBatchById(workFeeList);
        }
    }

    /**
     * 删除未审核工单的指定分类费用
     *
     * @param assortId
     */
    @Override
    public void delUncheckDetailByAssort(Long assortId) {
        if (LongUtil.isZero(assortId)) {
            return;
        }
        List<WorkFeeDetail> list = this.baseMapper.listUncheckedByAssortId(assortId);
        if (CollectionUtil.isNotEmpty(list)) {
            List<Long> detailIdList = list.stream().map(workFeeDetail -> workFeeDetail.getDetailId()).collect(Collectors.toList());
            List<Long> workIdList = new ArrayList<>();
            for (WorkFeeDetail workFeeDetail : list) {
                workIdList.add(workFeeDetail.getWorkId());
            }
            // 删除费用明细后，需更新费用信息
            Map<Long, WorkFee> workFeeMap = this.workFeeService.mapWorkFee(workIdList);
            List<WorkFee> workFeeList = new ArrayList<>();
            for (WorkFeeDetail workFeeDetail : list) {
                WorkFee workFee = workFeeMap.get(workFeeDetail.getWorkId());
                workFee.setAssortFee(workFee.getAssortFee().subtract(workFeeDetail.getAmount()));
                workFee.setTotalFee(this.workFeeService.getTotalFee(workFee));
                workFeeList.add(workFee);
            }
            this.removeByIds(detailIdList);
            this.workFeeService.updateBatchById(workFeeList);
        }
    }

    /**
     * 根据workIdList获取WorkFeeDetailDto
     *
     * @param workIdList
     * @return
     */
    @Override
    public List<WorkFeeDetailDto> listDtoByWorkIdList(List<Long> workIdList) {
        if (CollectionUtil.isEmpty(workIdList)) {
            return null;
        }
        return this.baseMapper.listByTypeAndWorkId(workIdList);
    }

    /**
     * 根据工单编号查询实施发生费用明细
     *
     * @param workId
     * @return
     */
    @Override
    public List<WorkFeeDetailDto> listImplementByWorkId(Long workId) {
        List<WorkFeeDetailDto> list = new ArrayList<>();
        if (LongUtil.isZero(workId)) {
            return list;
        }
        List<WorkFeeDetailDto> dtoList = this.baseMapper.listDtoByWorkId(workId);
        List<WorkFeeDetailDto> implementList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(dtoList)) {
            implementList = dtoList.stream().filter(workFeeDetailDto ->
                    FeeTypeEnum.IMPLEMENT.getCode().equals(workFeeDetailDto.getFeeType())).collect(Collectors.toList());
        }
        return implementList;
    }

    /**
     * 根据工单编号删除实施发生费用明细
     *
     * @param workId
     */
    @Override
    public void delImplementById(Long workId) {
        if (LongUtil.isNotZero(workId)) {
            QueryWrapper<WorkFeeDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("work_id", workId).eq("fee_type", FeeTypeEnum.IMPLEMENT.getCode());
            this.remove(queryWrapper);
        }
    }

}
