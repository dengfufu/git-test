package com.zjft.usp.device.baseinfo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.device.baseinfo.dto.DeviceModelDto;
import com.zjft.usp.device.baseinfo.filter.DeviceModelFilter;
import com.zjft.usp.device.baseinfo.model.DeviceModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 设备型号表 Mapper 接口
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
public interface DeviceModelMapper extends BaseMapper<DeviceModel> {

    /**
     * 分页查询设备型号
     *
     * @param page
     * @param deviceModelFilter
     * @return
     * @author zgpi
     * @date 2020/6/10 14:06
     **/
    List<DeviceModelDto> queryDeviceModel(Page page, @Param("deviceModelFilter") DeviceModelFilter deviceModelFilter);

    /**
     * 设备型号列表
     *
     * @param corpId
     * @param smallClassId
     * @return
     * @author zgpi
     * @date 2019/9/25 2:44 下午
     **/
    List<DeviceModelDto> listDeviceModelDto(@Param("corpId") Long corpId,
                                            @Param("smallClassId") Long smallClassId);

    /**
     * 模糊查询设备型号
     *
     * @param deviceModelFilter
     * @return
     * @author zgpi
     * @date 2019/12/11 10:40
     **/
    List<DeviceModel> matchDeviceModel(@Param("deviceModelFilter") DeviceModelFilter deviceModelFilter);
}
