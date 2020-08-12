package com.zjft.usp.device.device.service;

import com.zjft.usp.device.device.dto.DeviceServiceDto;
import com.zjft.usp.device.device.model.DeviceService;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 设备服务信息表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-10-16
 */
public interface DeviceServiceService extends IService<DeviceService> {

    /**
     * 更新
     *
     * @param deviceServiceDto
     * @param curUserId
     */
    void update(DeviceServiceDto deviceServiceDto, Long curUserId);

    void delDeviceServiceByCorp(Long userId, Long corpId, Long currentUserId, String clientId);

}
