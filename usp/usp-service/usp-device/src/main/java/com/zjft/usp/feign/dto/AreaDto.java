package com.zjft.usp.feign.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 地区
 * @author zgpi
 * @version 1.0
 * @date 2019-11-06 09:30
 **/
@Getter
@Setter
public class AreaDto {

    /** 省份代码 **/
    private String provinceCode;
    /** 省份名称 **/
    private String provinceName;
    /** 地市代码 **/
    private String cityCode;
    /** 地市名称 **/
    private String cityName;
    /** 区/县代码 **/
    private String districtCode;
    /** 区/县名称 **/
    private String districtName;
    /** 地区代码 **/
    private String areaCode;
    /** 地区名称 **/
    private String areaName;
}
