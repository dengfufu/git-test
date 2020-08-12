package com.zjft.usp.device.device.service.impl;

import cn.hutool.core.date.DateTime;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.device.device.dto.DeviceLocateDto;
import com.zjft.usp.device.device.mapper.DeviceLocateMapper;
import com.zjft.usp.device.device.model.DeviceLocate;
import com.zjft.usp.device.device.service.DeviceLocateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;

/**
 * <p>
 * 设备位置表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-10-16
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceLocateServiceImpl extends ServiceImpl<DeviceLocateMapper, DeviceLocate> implements DeviceLocateService {

    @Override
    public void update(DeviceLocateDto deviceLocateDto, Long curUserId) {
        if (deviceLocateDto == null || LongUtil.isZero(deviceLocateDto.getDeviceId())) {
            throw new AppException("设备编号不能为空！");
        }
        DeviceLocate deviceLocate = this.getById(deviceLocateDto.getDeviceId());
        if (deviceLocate == null) {
            deviceLocate = new DeviceLocate();
        }
        BeanUtils.copyProperties(deviceLocateDto, deviceLocate);
        deviceLocate.setOperator(curUserId);
        deviceLocate.setOperateTime(DateTime.now().toTimestamp());
        this.saveOrUpdate(deviceLocate);
    }

    @Override
    public void clearLocate(Long deviceId, Long curUserId) {
        if (LongUtil.isZero(deviceId)) {
            throw new AppException("设备编号不能为空！");
        }
        DeviceLocate deviceLocate = this.getById(deviceId);
        Assert.notNull(deviceLocate, "该设备位置信息不存在！");
        //只保留客户信息
        deviceLocate.setAddress("");
        deviceLocate.setDistrict("");
        deviceLocate.setBranchId(0L);
        deviceLocate.setStatus(0);
        deviceLocate.setDescription("");
        deviceLocate.setDeviceCode("");
        deviceLocate.setInstallDate(null);
        deviceLocate.setLat(BigDecimal.valueOf(0L));
        deviceLocate.setLon(BigDecimal.valueOf(0L));
        deviceLocate.setOperator(curUserId);
        deviceLocate.setOperateTime(DateTime.now().toTimestamp());
        this.updateById(deviceLocate);
    }
}
