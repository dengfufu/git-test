package com.zjft.usp.uas.baseinfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.uas.baseinfo.dto.AreaDto;
import com.zjft.usp.uas.baseinfo.dto.CfgAreaDto;
import com.zjft.usp.uas.baseinfo.model.CfgArea;

import java.util.List;
import java.util.Map;

/**
 * 地区服务接口类
 * @author zgpi
 * @version 1.0
 * @date 2019-08-19 10:14
 **/
public interface CfgAreaService extends IService<CfgArea> {

    /**
     * 省市区数据
     * @author zgpi
     * @date 2019-08-19 13:47
     * @param 
     * @return
     **/
    List<CfgAreaDto> listAreaDto();


    /**
     * 省市数据
     * @author ljzhu
     * @return
     */
    List<CfgAreaDto> listProvinceAndCity();

    /**
     * 省份列表
     * @author zgpi
     * @date 2019-08-19 13:47
     * @param
     * @return
     **/
    List<CfgAreaDto> listProvince(boolean needChild,boolean needDistrictChild);
    
    /**
     * 地市列表
     * @author zgpi
     * @date 2019-08-29 08:35
     * @param province
     * @return
     **/
    List<CfgAreaDto> listCity(String province,boolean needChild);
    
    /**
     * 区县列表
     * @author zgpi
     * @date 2019-08-29 08:36
     * @param city
     * @return
     **/
    List<CfgAreaDto> listDistrict(String city);

    /**
     * 获得区域
     * @author zgpi
     * @date 2019-08-22 17:36
     * @param areaDto
     * @return
     **/
    AreaDto getAreaDto(AreaDto areaDto);
    
    /**
     * 获得区域
     * @author zgpi
     * @date 2019-09-02 11:08
     * @param code
     * @return
     **/
    AreaDto findAreaByCode(String code);

    /**
     * 解析省份代码
     * @author zgpi
     * @date 2019-08-22 15:27
     * @param name
     * @return
     **/
    String parseProvince(String name);

    /**
     * 解析地市代码
     * @author zgpi
     * @date 2019-08-22 16:05
     * @param province
     * @param name
     * @return
     **/
    String parseCity(String province, String name);

    /**
     * 解析区/县代码
     * @author zgpi
     * @date 2019-08-22 16:05
     * @param city
     * @param name
     * @return
     **/
    String parseDistrict(String city, String name);

    /**
     * 地区映射
     * @author zgpi
     * @date 2019-08-21 10:13
     * @param 
     * @return
     **/
    Map<String, String> selectAreaMap();

    /**
     * 获得地区映射（包含下级）
     * @author zgpi
     * @date 2019-08-26 14:51
     * @param code
     * @return
     **/
    Map<String, String> mapAreaCodeAndName(String code);

    /**
     * 省份编码与地市数量映射
     * @author zgpi
     * @date 2019-08-29 08:54
     * @param 
     * @return
     **/
    Map<String, Integer> getProvinceCityCountMap();

    /**
     * 地市编码与区县数量映射
     * @author zgpi
     * @date 2019-08-29 09:05
     * @param province
     * @return
     **/
    Map<String, Integer> getCityDistrictCountMap(String province);

    /**
     * 根据区域编号列表获得映射
     *
     * @param codeList
     * @return
     * @author zgpi
     * @date 2020/2/22 13:14
     */
    Map<String, String> mapCodeAndNameByCodeList(List<String> codeList);

    /**
     * 地区信息列表，包含所有children信息
     * @author ljzhu
     * @date 2019-10-17
     * @param
     * @return
     **/
    List<CfgAreaDto> listAreaInfo();

}
