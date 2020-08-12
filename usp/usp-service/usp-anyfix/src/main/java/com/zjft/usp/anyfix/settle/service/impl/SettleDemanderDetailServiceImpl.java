package com.zjft.usp.anyfix.settle.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.baseinfo.service.WorkTypeService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderCustomService;
import com.zjft.usp.anyfix.settle.dto.SettleDemanderDetailDto;
import com.zjft.usp.anyfix.settle.filter.SettleDemanderDetailFilter;
import com.zjft.usp.anyfix.settle.model.SettleDemander;
import com.zjft.usp.anyfix.settle.model.SettleDemanderDetail;
import com.zjft.usp.anyfix.settle.mapper.SettleDemanderDetailMapper;
import com.zjft.usp.anyfix.settle.service.SettleDemanderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.settle.service.SettleDemanderService;
import com.zjft.usp.anyfix.work.check.enums.FeeCheckStatusEnum;
import com.zjft.usp.anyfix.work.check.enums.FeeConfirmStatusEnum;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeVerifyDetailDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeVerifyDto;
import com.zjft.usp.anyfix.work.fee.enums.WorkFeeVerifyStatusEnum;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeVerify;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeVerifyDetail;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeService;
import com.zjft.usp.anyfix.work.request.enums.WarrantyEnum;
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
import java.util.*;

/**
 * <p>
 * 供应商结算单明细 服务实现类
 * </p>
 *
 * @author canlei
 * @since 2020-01-13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SettleDemanderDetailServiceImpl extends ServiceImpl<SettleDemanderDetailMapper, SettleDemanderDetail> implements SettleDemanderDetailService {

    @Autowired
    private SettleDemanderService settleDemanderService;
    @Autowired
    private DemanderCustomService demanderCustomService;
    @Autowired
    private WorkTypeService workTypeService;
    @Autowired
    private WorkFeeService workFeeService;

    @Resource
    private UasFeignService uasFeignService;
    @Resource
    private DeviceFeignService deviceFeignService;

    /**
     * 查询结算对账单
     *
     * @author canlei
     * @param settleDemanderDetailFilter
     * @return
     */
    @Override
    public List<WorkFeeVerifyDto> listWorkFeeVerifyByFilter(SettleDemanderDetailFilter settleDemanderDetailFilter) {
        List<WorkFeeVerifyDto> dtoList = new ArrayList<>();
        if (settleDemanderDetailFilter == null || LongUtil.isZero(settleDemanderDetailFilter.getSettleId())) {
            return dtoList;
        }
        SettleDemander settleDemander = this.settleDemanderService.getById(settleDemanderDetailFilter.getSettleId());
        if (settleDemander == null) {
            return dtoList;
        }
        dtoList = this.baseMapper.listWorkFeeVerify(settleDemanderDetailFilter);
        this.addWorkFeeVerifyExtraAttributes(dtoList, settleDemander);
        return dtoList;
    }

    /**
     * 分页查询结算对账单
     *
     * @author canlei
     * @param settleDemanderDetailFilter
     * @return
     */
    @Override
    public ListWrapper<WorkFeeVerifyDto> queryWorkFeeVerifyByFilter(SettleDemanderDetailFilter settleDemanderDetailFilter) {
        ListWrapper<WorkFeeVerifyDto> listWrapper = new ListWrapper<>();
        if (settleDemanderDetailFilter == null || LongUtil.isZero(settleDemanderDetailFilter.getSettleId())) {
            return listWrapper;
        }
        SettleDemander settleDemander = this.settleDemanderService.getById(settleDemanderDetailFilter.getSettleId());
        if (settleDemander == null) {
            return listWrapper;
        }
        Page page = new Page(settleDemanderDetailFilter.getPageNum(), settleDemanderDetailFilter.getPageSize());
        List<WorkFeeVerifyDto> detailList = this.baseMapper.queryWorkFeeVerify(page, settleDemanderDetailFilter);
        this.addWorkFeeVerifyExtraAttributes(detailList, settleDemander);
        listWrapper.setList(detailList);
        listWrapper.setTotal(page.getTotal());
        return listWrapper;
    }

    /**
     * 查询结算工单
     *
     * @param settleDemanderDetailFilter
     * @return
     */
    @Override
    public List<WorkFeeDto> listWorkByFilter(SettleDemanderDetailFilter settleDemanderDetailFilter) {
        List<WorkFeeDto> dtoList = new ArrayList<>();
        if (settleDemanderDetailFilter == null || LongUtil.isZero(settleDemanderDetailFilter.getSettleId())) {
            return dtoList;
        }
        SettleDemander settleDemander = this.settleDemanderService.getById(settleDemanderDetailFilter.getSettleId());
        if (settleDemander == null) {
            return dtoList;
        }
        dtoList = this.baseMapper.listWork(settleDemanderDetailFilter);
        this.addWorkExtraAttributes(dtoList, settleDemander);
        return dtoList;
    }

    /**
     * 分页查询结算工单
     *
     * @param settleDemanderDetailFilter
     * @return
     */
    @Override
    public ListWrapper<WorkFeeDto> queryWorkByFilter(SettleDemanderDetailFilter settleDemanderDetailFilter) {
        ListWrapper<WorkFeeDto> listWrapper = new ListWrapper<>();
        if (settleDemanderDetailFilter == null || LongUtil.isZero(settleDemanderDetailFilter.getSettleId())) {
            return listWrapper;
        }
        SettleDemander settleDemander = this.settleDemanderService.getById(settleDemanderDetailFilter.getSettleId());
        if (settleDemander == null) {
            return listWrapper;
        }
        Page page = new Page(settleDemanderDetailFilter.getPageNum(), settleDemanderDetailFilter.getPageSize());
        List<WorkFeeDto> detailList = this.baseMapper.queryWork(page, settleDemanderDetailFilter);
        this.addWorkExtraAttributes(detailList, settleDemander);
        listWrapper.setList(detailList);
        listWrapper.setTotal(page.getTotal());
        return listWrapper;
    }

    /**
     * 删除结算单
     *
     * @author canlei
     * @param settleId
     */
    @Override
    public void deleteBySettleId(Long settleId) {
        UpdateWrapper<SettleDemanderDetail> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("settle_id", settleId);
        this.remove(updateWrapper);
    }

    /**
     * 根据结算单号获取明细
     *
     * @param settleId
     * @return
     */
    @Override
    public List<SettleDemanderDetail> listBySettleId(Long settleId) {
        if (LongUtil.isZero(settleId)) {
            return null;
        }
        List<SettleDemanderDetail> list = this.list(new QueryWrapper<SettleDemanderDetail>().eq("settle_id", settleId));
        return list;
    }

    /**
     * 根据结算单编号查询工单处理列表
     *
     * @param detailList
     * @param settleDemander
     * @return
     */
//    @Override
//    public List<WorkDeal> listWorkDealBySettleId(Long settleId) {
//        List<WorkDeal> list = new ArrayList<>();
//        if (LongUtil.isZero(settleId)) {
//            return list;
//        }
//        list = this.baseMapper.listWorkDealBySettleId(settleId);
//        return list;
//    }

    private void addWorkFeeVerifyExtraAttributes(List<WorkFeeVerifyDto> detailList, SettleDemander settleDemander) {
        if (CollectionUtil.isNotEmpty(detailList)) {
            Result<Map<String, String>> districtMapResult = this.uasFeignService.mapAreaCodeAndName();
            Map<String, String> areaMap = new HashMap<>();
            if (districtMapResult != null && Result.SUCCESS == districtMapResult.getCode().intValue()) {
                areaMap = districtMapResult.getData() == null ? new HashMap<>() : districtMapResult.getData();
            }
            for (WorkFeeVerifyDto detailDto : detailList) {
                detailDto.setStatusName(StrUtil.trimToEmpty(WorkFeeVerifyStatusEnum.lookup(detailDto.getStatus())));
                String provinceName = "";
                String cityName = "";
                String districtName = "";
                if (StrUtil.isNotBlank(detailDto.getDistrict()) && detailDto.getDistrict().length() >= 2) {
                    provinceName = StrUtil.trimToEmpty(areaMap.get(detailDto.getDistrict().substring(0, 2)));
                }
                if (StrUtil.isNotBlank(detailDto.getDistrict()) && detailDto.getDistrict().length() >= 4) {
                    cityName = StrUtil.trimToEmpty(areaMap.get(detailDto.getDistrict().substring(0, 4)));
                }
                if (StrUtil.isNotBlank(detailDto.getDistrict()) && detailDto.getDistrict().length() >= 6) {
                    provinceName = StrUtil.trimToEmpty(areaMap.get(detailDto.getDistrict()));
                }
                detailDto.setDistrictName(provinceName + cityName + districtName);
            }
        }
    }

    /**
     * 结算工单增加额外属性
     *
     * @param detailList
     * @param settleDemander
     */
    private void addWorkExtraAttributes(List<WorkFeeDto> detailList, SettleDemander settleDemander) {
        if (CollectionUtil.isNotEmpty(detailList)) {
            HashSet<Long> corpIdSet = new HashSet<>();
            HashSet<Long> customIdSet = new HashSet<>();
            HashSet<Long> userIdSet = new HashSet<>();
            HashSet<Long> specIdSet = new HashSet<>();
            corpIdSet.add(settleDemander.getDemanderCorp());
            List<Long> workIdList = new ArrayList<>();
            detailList.forEach(dto -> {
                customIdSet.add(dto.getCustomId());
                workIdList.add(dto.getWorkId());
                specIdSet.add(dto.getSpecification());
            });
            List<Long> corpIdList = new ArrayList<>(corpIdSet);
            List<Long> customIdList = new ArrayList<>(customIdSet);
            List<Long> userIdList = new ArrayList<>(userIdSet);
            List<Long> specIdList = new ArrayList<>(specIdSet);
            Map<Long, String> customMap = this.demanderCustomService.mapIdAndCustomNameByIdList(customIdList);
            Map<Integer, String> workTypeMap = this.workTypeService.mapWorkType(settleDemander.getDemanderCorp());
            Result<Map<Long, String>> smallClassMapResult = this.deviceFeignService.mapSmallClassByCorp(settleDemander.getDemanderCorp());
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
            Result<Map<Long, String>> brandMapResult = this.deviceFeignService.mapDeviceBrandByCorp(settleDemander.getDemanderCorp());
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
//            String userIdListJson = JsonUtil.toJson(userIdList);
//            Result<Map<Long, String>> userMapResult = this.uasFeignService.mapUserIdAndNameByUserIdList(userIdListJson);
//            Map<Long, String> userMap = new HashMap<>();
//            if (userMapResult != null && Result.SUCCESS == userMapResult.getCode().intValue()) {
//                userMap = userMapResult.getData() == null ? new HashMap<>() : userMapResult.getData();
//            }
            Map<Long, WorkFeeDto> workFeeMap = this.workFeeService.mapWorkIdAndFeeDto(workIdList);
            for (WorkFeeDto workFeeDto : detailList) {
                workFeeDto.setCustomCorpName(customMap.get(workFeeDto.getCustomId()));
                workFeeDto.setWorkTypeName(workTypeMap.get(workFeeDto.getWorkType()));
                workFeeDto.setSmallClassName(smallClassMap.get(workFeeDto.getSmallClassId()));
                workFeeDto.setSpecificationName(specMap.get(workFeeDto.getSpecification()));
                workFeeDto.setBrandName(brandMap.get(workFeeDto.getBrand()));
                workFeeDto.setModelName(modelMap.get(workFeeDto.getModel()));
                workFeeDto.setWarrantyName(WarrantyEnum.getNameByCode(workFeeDto.getWarranty()));
                workFeeDto.setFeeConfirmStatusName(FeeConfirmStatusEnum.getNameByCode(workFeeDto.getFeeConfirmStatus()));
                workFeeDto.setFeeCheckStatusName(FeeCheckStatusEnum.getNameByCode(workFeeDto.getFeeCheckStatus()));
//                workFeeDto.setCreatorName(StrUtil.trimToEmpty(userMap.get(workFeeDto.getCreator())));
                String provinceName = "", cityName = "", districtName = "";
                if (StrUtil.isNotEmpty(workFeeDto.getDistrict()) && workFeeDto.getDistrict().length() >= 2) {
                    provinceName = StrUtil.trimToEmpty(areaMap.get(workFeeDto.getDistrict().substring(0, 2)));
                }
                if (StrUtil.isNotEmpty(workFeeDto.getDistrict()) && workFeeDto.getDistrict().length() >= 4) {
                    cityName = StrUtil.trimToEmpty(areaMap.get(workFeeDto.getDistrict().substring(0, 4)));
                }
                if (StrUtil.isNotEmpty(workFeeDto.getDistrict()) && workFeeDto.getDistrict().length() >= 6) {
                    districtName = StrUtil.trimToEmpty(areaMap.get(workFeeDto.getDistrict()));
                }
                workFeeDto.setDistrictName(provinceName + cityName + districtName);
                WorkFeeDto workFeeDtoForFee = workFeeMap.get(workFeeDto.getWorkId());
                if (workFeeDtoForFee != null) {
                    BeanUtil.copyProperties(workFeeDtoForFee, workFeeDto, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
                }
            }
        }
    }

}