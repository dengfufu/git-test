package com.zjft.usp.uas.corp.dto;

import cn.hutool.core.util.StrUtil;
import com.zjft.usp.uas.corp.model.CorpRegistry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author canlei
 * @date 2019-08-04
 */
@Getter
@Setter
public class CorpDto extends CorpRegistry {

    @ApiModelProperty("省名称")
    private String provinceName;

    @ApiModelProperty("市名称")
    private String cityName;

    @ApiModelProperty("区县名称")
    private String districtName;

    @ApiModelProperty("LOGO文件路径")
    private String logoImgUrl;

    @ApiModelProperty("省市区名称")
    private String region;

    @ApiModelProperty("省市名称")
    private String shortRegion;

    @ApiModelProperty("统一社会信用编码")
    private String creditCode;

    @ApiModelProperty(value = "服务委托方 Y=是 N=否")
    private String serviceDemander;

    @ApiModelProperty(value = "服务提供商 Y=是 N=否")
    private String serviceProvider;

    @ApiModelProperty(value = "设备使用商 Y=是 N=否")
    private String deviceUser;

    @ApiModelProperty("是否已认证")
    private boolean verifyStatus;

    @ApiModelProperty("是否已申请认证")
    private boolean verifyAppStatus;

    @ApiModelProperty("当前用户是否为企业员工")
    private boolean inCorp;

    @ApiModelProperty("当前用户是否为管理员")
    private boolean admin;

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
        this.region = this.provinceName + this.cityName + this.districtName;
    }

    @ApiModelProperty("注册人姓名")
    private String regUserName;

}
