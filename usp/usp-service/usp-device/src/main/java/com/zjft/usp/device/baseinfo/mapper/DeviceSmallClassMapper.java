package com.zjft.usp.device.baseinfo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.device.baseinfo.dto.DeviceClassCompoDto;
import com.zjft.usp.device.baseinfo.dto.DeviceSmallClassDto;
import com.zjft.usp.device.baseinfo.filter.DeviceSmallClassFilter;
import com.zjft.usp.device.baseinfo.model.DeviceSmallClass;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 设备小类表 Mapper 接口
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
public interface DeviceSmallClassMapper extends BaseMapper<DeviceSmallClass> {

    /**
     * 分页查询设备类型
     *
     * @param page
     * @param deviceSmallClassFilter
     * @return
     * @author zgpi
     * @date 2020/6/10 14:54
     **/
    List<DeviceSmallClassDto> queryDeviceSmallClass(Page page,
                                                    @Param("deviceSmallClassFilter") DeviceSmallClassFilter deviceSmallClassFilter);

    /**
     * 设备分类列表
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/9/25 1:45 下午
     **/
    List<DeviceSmallClassDto> listDeviceClass(Long corpId);

    /**
     * 获得最大顺序号
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/11/15 16:52
     **/
    Integer findMaxSortNo(@Param("corpId") Long corpId);

    /**
     * 模糊查询设备小类
     *
     * @param deviceSmallClassFilter
     * @return
     * @author zgpi
     * @date 2019/12/11 10:19
     **/
    List<DeviceSmallClass> matchDeviceSmallClass(@Param("deviceSmallClassFilter") DeviceSmallClassFilter deviceSmallClassFilter);

    /**
     * 查询设备小类与设备分类组合对象列表
     *
     * @param deviceSmallClassFilter
     * @return
     */
    List<DeviceClassCompoDto> listDeviceClassCompoBy(@Param("deviceSmallClassFilter") DeviceSmallClassFilter deviceSmallClassFilter);

}
