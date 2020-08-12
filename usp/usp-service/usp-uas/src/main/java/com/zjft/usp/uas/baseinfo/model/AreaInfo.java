package com.zjft.usp.uas.baseinfo.model;

import lombok.Data;

/**
 * 地区数据
 * @author zgpi
 * @version 1.0
 * @date 2019-08-19 11:12
 **/
@Data
public class AreaInfo {
    private String provinceCode;
    private String provinceName;
    private String cityCode;
    private String cityName;
    private String districtCode;
    private String districtName;

    private Integer count;
}
