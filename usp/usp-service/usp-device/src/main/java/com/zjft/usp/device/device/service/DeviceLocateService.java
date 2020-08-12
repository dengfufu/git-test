package com.zjft.usp.device.device.service;

import com.zjft.usp.device.device.dto.DeviceLocateDto;
import com.zjft.usp.device.device.model.DeviceLocate;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 设备位置表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-10-16
 */
public interface DeviceLocateService extends IService<DeviceLocate> {

    /**
     * 更新
     * @param deviceLocateDto
     * @param curUserId
     */
    void update(DeviceLocateDto deviceLocateDto, Long curUserId);

    /**
     * 清除位置信息
     * @param deviceId
     */
    void clearLocate(Long deviceId, Long curUserId);

}
