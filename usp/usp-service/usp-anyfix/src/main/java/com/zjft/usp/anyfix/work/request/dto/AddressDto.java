package com.zjft.usp.anyfix.work.request.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author cxd
 * @date 2020-2-25 14:00
 */
@Data
public class AddressDto {

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区，区县")
    private String county;

    @ApiModelProperty(value = "街道，乡村，镇")
    private String town;

    @ApiModelProperty(value = "详细地址")
    private String village;

    @ApiModelProperty(value = "完整地址")
    private String detailAddress;

    @ApiModelProperty(value = "详细地址")
    private String detailFormat;
}
