package com.zjft.usp.device.baseinfo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.device.baseinfo.filter.DeviceBrandFilter;
import com.zjft.usp.device.baseinfo.model.DeviceBrand;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 设备品牌表 Mapper 接口
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
public interface DeviceBrandMapper extends BaseMapper<DeviceBrand> {


    /**
     * 模糊查询设备品牌
     *
     * @param deviceBrandFilter
     * @return
     * @author zgpi
     * @date 2019/12/11 10:40
     **/
    List<DeviceBrand> matchDeviceBrand(@Param("deviceBrandFilter") DeviceBrandFilter deviceBrandFilter);

    /**
     * 根据设备类型查询设备品牌
     *
     * @param deviceBrandFilter
     * @return
     **/
    List<DeviceBrand> listDeviceBrand(@Param("deviceBrandDto") DeviceBrandFilter deviceBrandFilter);
}
