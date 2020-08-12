package com.zjft.usp.device.baseinfo.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.service.AnyfixFeignService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.device.baseinfo.common.service.CorpNameService;
import com.zjft.usp.device.baseinfo.dto.DeviceModelDto;
import com.zjft.usp.device.baseinfo.enums.EnabledEnum;
import com.zjft.usp.device.baseinfo.filter.DeviceModelFilter;
import com.zjft.usp.device.baseinfo.mapper.DeviceModelMapper;
import com.zjft.usp.device.baseinfo.model.DeviceModel;
import com.zjft.usp.device.baseinfo.model.DeviceSmallClass;
import com.zjft.usp.device.baseinfo.service.DeviceBrandService;
import com.zjft.usp.device.baseinfo.service.DeviceLargeClassService;
import com.zjft.usp.device.baseinfo.service.DeviceModelService;
import com.zjft.usp.device.baseinfo.service.DeviceSmallClassService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 设备型号表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceModelServiceImpl extends ServiceImpl<DeviceModelMapper, DeviceModel> implements DeviceModelService {

    @Autowired
    private DeviceSmallClassService deviceSmallClassService;
    @Autowired
    private DeviceBrandService deviceBrandService;
    @Autowired
    private DeviceLargeClassService deviceLargeClassService;
    @Resource
    private DeviceModelMapper deviceModelMapper;
    @Resource
    private CorpNameService corpNameService;

    @Resource
    private AnyfixFeignService anyfixFeignService;

    @Override
    public List<DeviceModelDto> listDeviceModel(DeviceModelFilter deviceModelFilter) {
        QueryWrapper<DeviceModel> queryWrapper = new QueryWrapper<>();
        if (deviceModelFilter != null) {
            if (StrUtil.isNotBlank(deviceModelFilter.getName())) {
                queryWrapper.like("name", deviceModelFilter.getName());
            }
            if (StrUtil.isNotBlank(deviceModelFilter.getEnabled())) {
                queryWrapper.eq("enabled", deviceModelFilter.getEnabled().toUpperCase());
            }
            if (LongUtil.isNotZero(deviceModelFilter.getCorp())) {
                queryWrapper.eq("corp", deviceModelFilter.getCorp());
            }
            if (LongUtil.isNotZero(deviceModelFilter.getBrandId())) {
                queryWrapper.eq("brand_id", deviceModelFilter.getBrandId());
            }
            /*TODO 需要根据传递过来的largeClassId查找范围内的smallClassId*/
            /*if (LongUtil.isNotZero(deviceModelDto.getLargeClassId())) {
                queryWrapper.eq("largeclassid", deviceModelDto.getLargeClassId());
            }*/
            if (LongUtil.isNotZero(deviceModelFilter.getSmallClassId())) {
                queryWrapper.eq("small_class_id", deviceModelFilter.getSmallClassId());
            }
        }
        List<DeviceModel> deviceModelList = this.list(queryWrapper);
        List<DeviceModelDto> deviceModelDtoList = new ArrayList<>();
        if (deviceModelList != null && deviceModelList.size() > 0) {
            /** 映射数据Map */
            Map<Long, DeviceSmallClass> smallClassMap = this.deviceSmallClassService.mapSmallClassByCorpId(deviceModelFilter.getCorp());
            Map<Long, String> brandMap = this.deviceBrandService.mapIdAndNameByCorpId(deviceModelFilter.getCorp());
            Map<Long, String> largeClassMap = this.deviceLargeClassService.mapClassIdAndNameByCorp(deviceModelFilter.getCorp());
            for (DeviceModel deviceModel : deviceModelList) {
                /** Mapper转换 */
                DeviceModelDto tmpDto = new DeviceModelDto();
                BeanUtils.copyProperties(deviceModel, tmpDto);
                tmpDto.setBrandName(brandMap.get(deviceModel.getBrandId()));
                DeviceSmallClass deviceSmallClass = smallClassMap.get(deviceModel.getSmallClassId());
                if (deviceSmallClass != null) {
                    tmpDto.setSmallClassName(deviceSmallClass.getName());
                    tmpDto.setLargeClassId(deviceSmallClass.getLargeClassId());
                    if (LongUtil.isNotZero(tmpDto.getLargeClassId())) {
                        tmpDto.setLargeClassName(largeClassMap.get(tmpDto.getLargeClassId()));
                    }
                }
                tmpDto.setEnabledName(EnabledEnum.getValueByCode(deviceModel.getEnabled().toUpperCase()));
                deviceModelDtoList.add(tmpDto);
            }
        }
        return deviceModelDtoList;
    }

    @Override
    public Map<Long, String> mapIdAndNameByCorpIdList(List<Long> corpIdList) {
        Map<Long, String> map = new HashMap<>();
        if (corpIdList == null || corpIdList.size() == 0) {
            return map;
        }
        List<DeviceModel> list = this.list(new QueryWrapper<DeviceModel>().in("corp", corpIdList));
        if (list != null && list.size() > 0) {
            for (DeviceModel deviceModel : list) {
                map.put(deviceModel.getId(), deviceModel.getName());
            }
        }
        return map;
    }

    /**
     * 根据委托商编号获取型号编号与名称映射
     *
     * @param corpId
     * @return
     */
    @Override
    public Map<Long, String> mapIdAndNameByCorp(Long corpId) {
        Map<Long, String> map = new HashMap<>();
        if (LongUtil.isZero(corpId)) {
            return map;
        }
        List<DeviceModel> list = this.list(new QueryWrapper<DeviceModel>().eq("corp", corpId));
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(deviceModel -> {
                map.put(deviceModel.getId(), deviceModel.getName());
            });
        }
        return map;
    }

    /**
     * 获得编号与名称映射
     *
     * @param
     * @return
     * @author zgpi
     * @date 2019/10/16 7:51 下午
     **/
    @Override
    public Map<Long, String> mapIdAndName() {
        Map<Long, String> map = new HashMap<>();
        List<DeviceModel> list = this.list();
        if (list != null && list.size() > 0) {
            for (DeviceModel deviceModel : list) {
                map.put(deviceModel.getId(), deviceModel.getName());
            }
        }
        return map;
    }

    @Override
    public ListWrapper<DeviceModelDto> query(DeviceModelFilter deviceModelFilter) {
        Page<DeviceModelDto> page = new Page(deviceModelFilter.getPageNum(), deviceModelFilter.getPageSize());
        List<Long> demanderCorpList = new ArrayList<>();

        if (LongUtil.isZero(deviceModelFilter.getCorp())) {
            Result<List<Long>> result = this.anyfixFeignService
                    .listDemanderCorpId(deviceModelFilter.getCorpIdForDemander());
            if (Result.isSucceed(result)) {
                demanderCorpList = result.getData();
            }
            if (CollectionUtil.isEmpty(demanderCorpList)) {
                return ListWrapper.<DeviceModelDto>builder()
                        .list(new ArrayList<>())
                        .total(0L)
                        .build();
            }
        } else {
            demanderCorpList.add(deviceModelFilter.getCorp());
        }
        deviceModelFilter.setDemanderCorpList(demanderCorpList);

        List<DeviceModelDto> deviceModelDtoList = this.baseMapper.queryDeviceModel(page, deviceModelFilter);
        if (CollectionUtil.isNotEmpty(deviceModelDtoList)) {
            // 增加委托商名称显示
            List<Long> corpIdList = deviceModelDtoList.stream().map(e -> e.getCorp()).distinct().collect(Collectors.toList());
            Map<Long, String> corpMap = corpNameService.corpIdNameMap(corpIdList);
            for (DeviceModelDto deviceModelDto : deviceModelDtoList) {
                deviceModelDto.setEnabledName(EnabledEnum.getValueByCode(deviceModelDto.getEnabled()));
                deviceModelDto.setCorpName(corpMap.get(deviceModelDto.getCorp()));
            }
        }
        return ListWrapper.<DeviceModelDto>builder()
                .list(deviceModelDtoList)
                .total(page.getTotal())
                .build();
    }

    /**
     * 模糊查询设备型号
     *
     * @param deviceModelFilter
     * @return
     * @author zgpi
     * @date 2019/12/11 10:42
     **/
    @Override
    public List<DeviceModel> matchDeviceModel(DeviceModelFilter deviceModelFilter) {
        return this.baseMapper.matchDeviceModel(deviceModelFilter);
    }

    @Override
    public void save(DeviceModel deviceModel, UserInfo userInfo, ReqParam reqParam) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isEmpty(deviceModel.getName())) {
            builder.append("设备型号名称不能为空");
        }
        if (LongUtil.isZero(deviceModel.getBrandId())) {
            builder.append("品牌编号不能为空");
        }
        if (LongUtil.isZero(deviceModel.getSmallClassId())) {
            builder.append("设备小类编号不能为空");
        }
        if (builder.length() > 0) {
            throw new AppException(builder.toString());
        }
        QueryWrapper<DeviceModel> queryWrapper = new QueryWrapper<>();
        // 为空时设置当前公司
        if (LongUtil.isZero(deviceModel.getCorp())) {
            deviceModel.setCorp(reqParam.getCorpId());
        }
        queryWrapper.eq("name", deviceModel.getName());
        queryWrapper.eq("corp", deviceModel.getCorp());
        queryWrapper.eq("small_class_id", deviceModel.getSmallClassId());
        queryWrapper.eq("brand_id", deviceModel.getBrandId());
        List<DeviceModel> list = this.baseMapper.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            throw new AppException("该设备型号已经存在");
        }
        deviceModel.setId(KeyUtil.getId());
        deviceModel.setOperator(userInfo.getUserId());
        deviceModel.setOperateTime(DateUtil.date().toTimestamp());
        this.save(deviceModel);
    }

    @Override
    public void update(DeviceModel deviceModel, UserInfo userInfo) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isEmpty(deviceModel.getName())) {
            builder.append("设备型号名称不能为空");
        }
        if (LongUtil.isZero(deviceModel.getBrandId())) {
            builder.append("品牌编号不能为空");
        }
        if (LongUtil.isZero(deviceModel.getSmallClassId())) {
            builder.append("设备小类编号不能为空");
        }
        if (LongUtil.isZero(deviceModel.getId())) {
            builder.append("设备型号编号不能为空");
        }

        if (builder.length() > 0) {
            throw new AppException(builder.toString());
        }
        QueryWrapper<DeviceModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", deviceModel.getName());
        queryWrapper.eq("corp", deviceModel.getCorp());
        queryWrapper.eq("brand_id", deviceModel.getBrandId());
        queryWrapper.eq("small_class_id", deviceModel.getSmallClassId());
        queryWrapper.ne("id", deviceModel.getId());
        List<DeviceModel> list = this.baseMapper.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            throw new AppException("该设备型号已经存在");
        }
        deviceModel.setOperator(userInfo.getUserId());
        deviceModel.setOperateTime(DateUtil.date().toTimestamp());
        this.updateById(deviceModel);
    }

    /**
     * 获取产品型号id和model的映射
     *
     * @param corpId
     * @return
     */
    @Override
    public Map<Long, DeviceModel> mapIdAndModelByCorp(Long corpId) {
        Map<Long, DeviceModel> modelMap = new HashMap<>();
        if (LongUtil.isZero(corpId)) {
            return modelMap;
        }
        List<DeviceModel> list = this.list(new QueryWrapper<DeviceModel>().eq("corp", corpId));
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(deviceModel -> modelMap.put(deviceModel.getId(), deviceModel));
        }
        return modelMap;
    }

    @Override
    public Map<DeviceModel, Long> batchAddDeviceModel(List<DeviceModel> deviceModelList, Long userId) {
        if (CollectionUtil.isEmpty(deviceModelList)) {
            throw new AppException("设备型号列表不能为空");
        }
        for (DeviceModel deviceModel : deviceModelList) {
            if (StringUtils.isEmpty(deviceModel.getName())
                    || LongUtil.isZero(deviceModel.getCorp())
                    || LongUtil.isZero(deviceModel.getBrandId())
                    || LongUtil.isZero(deviceModel.getSmallClassId())) {
                throw new AppException("设备小类编号不能为空，品牌编号，型号名称都称必填");
            }
        }
        // 去重
        deviceModelList = deviceModelList.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() ->
                                new TreeSet<>(Comparator.comparing(o -> o.getName() + ";"
                                        + o.getSmallClassId() + ";" + o.getCorp() + ";" + o.getBrandId()))), ArrayList::new));
        QueryWrapper<DeviceModel> queryWrapper = new QueryWrapper<>();
        for (DeviceModel deviceModel : deviceModelList) {
            queryWrapper.or(wrapper -> wrapper
                    .eq("name", deviceModel.getName())
                    .eq("corp", deviceModel.getCorp())
                    .eq("small_class_id", deviceModel.getSmallClassId())
                    .eq("brand_id", deviceModel.getBrandId()));
        }
        queryWrapper.select("id");
        List<DeviceModel> queryModelList = this.list(queryWrapper);
        Map<DeviceModel, Long> queryMap = new HashMap<>();
        for (DeviceModel deviceModel : queryModelList) {
            queryMap.put(deviceModel, deviceModel.getId());
        }
        // 去除掉已经存在的列表
        deviceModelList.removeIf(s -> queryMap.get(s) != null);
        Map<DeviceModel, Long> returnMap = new HashMap<>();
        for (DeviceModel deviceModel : deviceModelList) {
            Long keyId = KeyUtil.getId();
            deviceModel.setId(keyId);
            deviceModel.setOperator(userId);
            deviceModel.setEnabled(EnabledEnum.YES.getCode());
            deviceModel.setOperateTime(DateUtil.date().toTimestamp());
            returnMap.put(deviceModel, keyId);
        }
        this.saveBatch(deviceModelList);
        return returnMap;
    }
}
