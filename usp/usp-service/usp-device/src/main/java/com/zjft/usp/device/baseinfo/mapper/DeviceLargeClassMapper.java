package com.zjft.usp.device.baseinfo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.device.baseinfo.filter.DeviceLargeClassFilter;
import com.zjft.usp.device.baseinfo.model.DeviceLargeClass;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 设备大类表 Mapper 接口
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
public interface DeviceLargeClassMapper extends BaseMapper<DeviceLargeClass> {

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
     * 模糊查询设备大类
     *
     * @param deviceLargeClassFilter
     * @return
     * @author zgpi
     * @date 2019/12/11 10:18
     **/
    List<DeviceLargeClass> matchDeviceLargeClass(@Param("deviceLargeClassFilter") DeviceLargeClassFilter deviceLargeClassFilter);
}
