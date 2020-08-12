package com.zjft.usp.uas.user.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author : dcyu
 * @Date : 2019年8月13日
 * @Desc : 地区信息传输类
 */
@NotNull(message = "regionDto 不能为空")
@Data
public class UserRegionDto {

    /** 用户ID **/
    private String userId;
    /** 国家 **/
    private String state;
    /** 省 **/
    private String provinceCode;
    /** 市 **/
    private String cityCode;
    /** 区 **/
    private String districtCode;
    /** 省 **/
    private String provinceName;
    /** 市 **/
    private String cityName;
    /** 区 **/
    private String districtName;
    /** 地区名称 **/
    private String areaNames;
    /** 地区代码 **/
    private String areaCode;
    /** 选择类型，1=定位；2=手工选择 **/
    private int selectType;
}
