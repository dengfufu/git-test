package com.zjft.usp.device.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.device.baseinfo.dto.DeviceModelDto;
import com.zjft.usp.device.baseinfo.dto.DeviceSmallClassDto;
import com.zjft.usp.device.device.dto.DeviceInfoDto;
import com.zjft.usp.device.device.filter.DeviceInfoFilter;
import com.zjft.usp.device.device.model.DeviceInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 设备档案基本表 Mapper 接口
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
public interface DeviceInfoMapper extends BaseMapper<DeviceInfo> {

    /**
     * 分页查询设备档案
     *
     * @author zgpi
     * @date 2019/10/16 6:57 下午
     * @param page
     * @param deviceInfoFilter
     * @return
     **/
    List<DeviceInfoDto> queryDeviceInfo(Page page, @Param("deviceInfoFilter") DeviceInfoFilter deviceInfoFilter);

    /**
     * 查询设备档案列表
     *
     * @param deviceInfoFilter
     * @return
     * @author zgpi
     * @date 2020/1/15 16:08
     **/
    List<DeviceInfoDto> listDeviceInfo(@Param("deviceInfoFilter") DeviceInfoFilter deviceInfoFilter);

    /**
     * 查询设备档案
     *
     * @param deviceInfoFilter
     * @return
     * @author zgpi
     * @date 2020/1/15 16:08
     **/
    List<DeviceInfoDto> findDeviceInfo(@Param("deviceInfoFilter") DeviceInfoFilter deviceInfoFilter);

    /**
     * 根据条件获得设备分类列表
     *
     * @author zgpi
     * @date 2019/10/22 2:11 下午
     * @param deviceInfoFilter
     * @return
     **/
    List<DeviceSmallClassDto> listDeviceClass(DeviceInfoFilter deviceInfoFilter);

    /**
     * 设备型号列表
     *
     * @author zgpi
     * @date 2019/10/22 2:11 下午
     * @param deviceInfoFilter
     * @return
     **/
    List<DeviceModelDto> listDeviceModel(DeviceInfoFilter deviceInfoFilter);
}
