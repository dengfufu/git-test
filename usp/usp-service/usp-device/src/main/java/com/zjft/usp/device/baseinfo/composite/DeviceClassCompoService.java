package com.zjft.usp.device.baseinfo.composite;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.device.baseinfo.dto.DeviceSmallClassDto;
import com.zjft.usp.device.baseinfo.filter.DeviceSmallClassFilter;

/**
 * <p>
 * 设备规格 聚合服务类
 * </p>
 *
 * @author zgpi
 * @since 2020-01-20
 */
public interface DeviceClassCompoService {

    /**
     * 分页查询设备小类列表
     *
     * @param deviceSmallClassFilter
     * @return
     * @author zgpi
     * @date 2020/1/20 10:56
     **/
    ListWrapper<DeviceSmallClassDto> queryDeviceSmallClass(DeviceSmallClassFilter deviceSmallClassFilter);

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
    void addDeviceSmallClass(DeviceSmallClassDto deviceSmallClassDto, UserInfo userInfo, ReqParam reqParam);

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
    void updateDeviceSmallClass(DeviceSmallClassDto deviceSmallClassDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 获得设备小类
     *
     * @param smallClassId
     * @return
     * @author zgpi
     * @date 2020/1/20 13:47
     **/
    DeviceSmallClassDto findDeviceSmallClass(Long smallClassId);

    /**
     * 删除设备小类
     *
     * @param smallClassId
     * @return
     * @author zgpi
     * @date 2020/1/20 10:56
     **/
    void delDeviceSmallClass(Long smallClassId);
}
