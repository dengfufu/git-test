package com.zjft.usp.device.device.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.device.device.dto.DeviceServiceDto;
import com.zjft.usp.device.device.mapper.DeviceServiceMapper;
import com.zjft.usp.device.device.model.DeviceInfo;
import com.zjft.usp.device.device.model.DeviceService;
import com.zjft.usp.device.device.service.DeviceInfoService;
import com.zjft.usp.device.device.service.DeviceServiceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 设备服务信息表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-10-16
 */
@Service
public class DeviceServiceServiceImpl extends ServiceImpl<DeviceServiceMapper, DeviceService> implements DeviceServiceService {

    @Autowired
    DeviceInfoService deviceInfoService;

    @Override
    public void update(DeviceServiceDto deviceServiceDto, Long curUserId) {
        if (deviceServiceDto == null || LongUtil.isZero(deviceServiceDto.getDeviceId())) {
            throw new AppException("设备编号不能为空！");
        }
        DeviceService deviceService = this.getById(deviceServiceDto.getDeviceId());
        if (deviceService == null) {
            deviceService = new DeviceService();
        }
        BeanUtils.copyProperties(deviceServiceDto, deviceService);
        deviceService.setOperator(curUserId);
        deviceService.setOperateTime(DateUtil.date());
        this.saveOrUpdate(deviceService);
    }

    @Override
    public void delDeviceServiceByCorp(Long userId, Long corpId, Long currentUserId, String clientId) {
        List<DeviceInfo> deviceInfoList = deviceInfoService.list(new QueryWrapper<DeviceInfo>()
                .eq("service_corp", corpId)
                .or()
                .eq("demander_corp", corpId));
        List<Long> deviceIdList = deviceInfoList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(deviceIdList)) {
            this.remove(new UpdateWrapper<DeviceService>()
                    .in("device_id", deviceIdList)
                    .and((i -> i.eq("work_manager", userId)
                            .or()
                            .eq("engineer", userId)))
            );
        }
    }
}
