package com.zjft.usp.uas.baseinfo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.uas.baseinfo.model.AreaInfo;
import com.zjft.usp.uas.baseinfo.model.CfgArea;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CfgAreaMapper extends BaseMapper<CfgArea> {

    List<CfgArea> listCfgArea();

    /**
     * 获得地区数据
     * @author zgpi
     * @date 2019-08-19 13:46
     * @param 
     * @return
     **/
    List<AreaInfo> listAreaInfo();

    /**
     * 地区映射
     * @author zgpi
     * @date 2019-08-21 09:48
     * @param
     * @return
     **/
    List<Map<String, String>> selectAreaMap();

    /**
     * 省份编码与地市数量映射
     * @author zgpi
     * @date 2019-08-29 08:53
     * @param 
     * @return
     **/
    List<Map<String, Integer>> getProvinceCityCountMap();
    
    /**
     * 地市编码与区县数量映射
     * @author zgpi
     * @date 2019-08-29 09:06
     * @param province
     * @return
     **/
    List<Map<String, Integer>> getCityDistrictCountMap(String province);

    /**
     * 根据省份列出地市
     *
     * @param province
     * @return
     */
    List<CfgArea> listCityByProvince(@Param("province") String province);

    /**
     * 根据城市列出地区
     *
     * @param city
     * @return
     */
    List<CfgArea> listDistrictByCity(@Param("city") String city);
}
