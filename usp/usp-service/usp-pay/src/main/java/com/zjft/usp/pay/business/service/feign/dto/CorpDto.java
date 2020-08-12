package com.zjft.usp.pay.business.service.feign.dto;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author canlei
 * @date 2019-08-04
 */
@Data
public class CorpDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long corpId;

    private String corpName;

    private String shortName;

    private String province;

    private String provinceName;

    private String city;

    private String cityName;

    private String district;

    private String districtName;

    private String address;

    private String telephone;

    private String industry;

    private String business;

    private Integer staffQty;

    private String website;

    private String logoImg;

    private String logoImgUrl;

    /**
     * 省+市+区
     */
    private String region;

    /**
     * 省+市
     */
    private String shortRegion;

    private String creditCode;

    /**
     * 是否认证
     */
    private boolean verifyStatus;

    /**
     * 是否有已申请未审核的认证
     */
    private boolean verifyAppStatus;

    /**
     * 当前用户是否为企业用户
     */
    private boolean inCorp;

    /**
     * 当前用户是否为管理员
     */
    private boolean admin;

    private Long regUserId;

    private Long txId;

    /**
     * 设置省、市、区以及完整地址
     * @param provinceName
     * @param cityName
     * @param districtName
     */
    public void areaName(String provinceName, String cityName, String districtName){
        this.provinceName = StrUtil.isBlank(provinceName) ? "" : provinceName;
        this.cityName = StrUtil.isBlank(cityName) ? "" : cityName;
        this.districtName = StrUtil.isBlank(districtName) ? "" : districtName;
        this.region = this.getProvinceName() + this.cityName + this.districtName;
    }

    @ApiModelProperty(value = "注册人姓名")
    private String regUserName;


    @ApiModelProperty(value = "企业编号")
    private String corpCode;

}
