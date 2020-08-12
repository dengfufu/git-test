package com.zjft.usp.device.baseinfo.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.service.AnyfixFeignService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.device.baseinfo.common.service.CorpNameService;
import com.zjft.usp.device.baseinfo.composite.DeviceClassCompoService;
import com.zjft.usp.device.baseinfo.dto.DeviceSmallClassDto;
import com.zjft.usp.device.baseinfo.filter.DeviceSmallClassFilter;
import com.zjft.usp.device.baseinfo.model.DeviceLargeClass;
import com.zjft.usp.device.baseinfo.model.DeviceSmallClass;
import com.zjft.usp.device.baseinfo.model.DeviceSpecification;
import com.zjft.usp.device.baseinfo.service.DeviceLargeClassService;
import com.zjft.usp.device.baseinfo.service.DeviceSmallClassService;
import com.zjft.usp.device.baseinfo.service.DeviceSpecificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 设备规格 聚合服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2020-01-20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceClassCompoServiceImpl implements DeviceClassCompoService {

    @Autowired
    private DeviceLargeClassService deviceLargeClassService;
    @Autowired
    private DeviceSmallClassService deviceSmallClassService;
    @Autowired
    private DeviceSpecificationService deviceSpecificationService;

    @Autowired
    private AnyfixFeignService anyfixFeignService;
    @Autowired
    private CorpNameService corpNameService;

    /**
     * 分页查询设备小类列表
     *
     * @param deviceSmallClassFilter
     * @return
     * @author zgpi
     * @date 2020/1/20 10:56
     **/
    @Override
    public ListWrapper<DeviceSmallClassDto> queryDeviceSmallClass(DeviceSmallClassFilter deviceSmallClassFilter) {
        Page<DeviceSmallClassDto> page = new Page(deviceSmallClassFilter.getPageNum(), deviceSmallClassFilter.getPageSize());
        List<Long> demanderCorpList = new ArrayList<>();

        if (LongUtil.isZero(deviceSmallClassFilter.getCorp())) {
            Result<List<Long>> result = this.anyfixFeignService
                    .listDemanderCorpId(deviceSmallClassFilter.getCorpIdForDemander());
            if (Result.isSucceed(result)) {
                demanderCorpList = result.getData();
            }
            if (CollectionUtil.isEmpty(demanderCorpList)) {
                return ListWrapper.<DeviceSmallClassDto>builder()
                        .list(new ArrayList<>())
                        .total(0L)
                        .build();
            }
        } else {
            demanderCorpList.add(deviceSmallClassFilter.getCorp());
        }
        deviceSmallClassFilter.setDemanderCorpList(demanderCorpList);

        List<DeviceSmallClassDto> deviceSmallClassDtoList = deviceSmallClassService.queryDeviceSmallClass(page, deviceSmallClassFilter);

        if (CollectionUtil.isNotEmpty(deviceSmallClassDtoList)) {
            // 增加委托商名称显示
            List<Long> corpIdList = deviceSmallClassDtoList.stream().map(e -> e.getCorp()).distinct().collect(Collectors.toList());
            Map<Long, String> corpMap = corpNameService.corpIdNameMap(corpIdList);
            for (DeviceSmallClassDto deviceSmallClassDto : deviceSmallClassDtoList) {
                deviceSmallClassDto.setCorpName(corpMap.get(deviceSmallClassDto.getCorp()));
            }
        }
        return ListWrapper.<DeviceSmallClassDto>builder()
                .list(deviceSmallClassDtoList)
                .total(page.getTotal())
                .build();
    }

    /**
     * 添加设备小类
     *
     * @param deviceSmallClassDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/1/20 10:56
     **/
    @Override
    public void addDeviceSmallClass(DeviceSmallClassDto deviceSmallClassDto, UserInfo userInfo, ReqParam reqParam) {
        Long smallClassId = deviceSmallClassService.save(deviceSmallClassDto, userInfo, reqParam);
        List<DeviceSpecification> deviceSpecificationList = deviceSmallClassDto.getDeviceSpecificationList();
        if (CollectionUtil.isNotEmpty(deviceSpecificationList)) {
            for (DeviceSpecification deviceSpecification : deviceSpecificationList) {
                deviceSpecification.setSmallClassId(smallClassId);
                deviceSpecification.setCorp(deviceSmallClassDto.getCorp());
                deviceSpecification.setId(KeyUtil.getId());
                deviceSpecification.setOperator(userInfo.getUserId());
                deviceSpecification.setOperateTime(DateUtil.date());
            }
            deviceSpecificationService.saveBatch(deviceSpecificationList);
        }
    }

    /**
     * 修改设备小类
     *
     * @param deviceSmallClassDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/1/20 10:56
     **/
    @Override
    public void updateDeviceSmallClass(DeviceSmallClassDto deviceSmallClassDto, UserInfo userInfo, ReqParam
            reqParam) {
        this.deviceSmallClassService.update(deviceSmallClassDto, userInfo);
        this.deviceSpecificationService.delDeviceSpecification(deviceSmallClassDto.getId());
        List<DeviceSpecification> deviceSpecificationList = deviceSmallClassDto.getDeviceSpecificationList();
        if (CollectionUtil.isNotEmpty(deviceSpecificationList)) {
            for (DeviceSpecification deviceSpecification : deviceSpecificationList) {
                deviceSpecification.setSmallClassId(deviceSmallClassDto.getId());
                deviceSpecification.setCorp(deviceSmallClassDto.getCorp());
                deviceSpecification.setId(KeyUtil.getId());
                deviceSpecification.setOperator(userInfo.getUserId());
                deviceSpecification.setOperateTime(DateUtil.date());
            }
            deviceSpecificationService.saveBatch(deviceSpecificationList);
        }
    }

    /**
     * 获得设备小类
     *
     * @param smallClassId
     * @return
     * @author zgpi
     * @date 2020/1/20 13:47
     **/
    @Override
    public DeviceSmallClassDto findDeviceSmallClass(Long smallClassId) {
        DeviceSmallClass deviceSmallClass = this.deviceSmallClassService.getById(smallClassId);
        DeviceSmallClassDto deviceSmallClassDto = null;
        if (deviceSmallClass != null) {
            deviceSmallClassDto = new DeviceSmallClassDto();
            BeanUtils.copyProperties(deviceSmallClass, deviceSmallClassDto);
            DeviceLargeClass deviceLargeClass = this.deviceLargeClassService.getById(deviceSmallClass.getLargeClassId());
            if (deviceLargeClass != null) {
                deviceSmallClassDto.setLargeClassName(deviceLargeClass.getName());
            }
            QueryWrapper<DeviceSpecification> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("small_class_id", smallClassId);
            List<DeviceSpecification> deviceSpecificationList = this.deviceSpecificationService.list(queryWrapper);
            deviceSmallClassDto.setDeviceSpecificationList(deviceSpecificationList);
        }
        return deviceSmallClassDto;
    }

    /**
     * 删除设备小类
     *
     * @param smallClassId
     * @return
     * @author zgpi
     * @date 2020/1/20 10:56
     **/
    @Override
    public void delDeviceSmallClass(Long smallClassId) {
        this.deviceSmallClassService.delete(smallClassId);
        this.deviceSpecificationService.delDeviceSpecification(smallClassId);
    }
}
