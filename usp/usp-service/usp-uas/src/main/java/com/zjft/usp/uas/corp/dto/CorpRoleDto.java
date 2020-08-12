package com.zjft.usp.uas.corp.dto;

import com.zjft.usp.uas.right.model.SysRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CorpRoleDto {
    @ApiModelProperty("企业编号")
    private Long corpId;

    @ApiModelProperty("企业名称")
    private String corpName;

    @ApiModelProperty("企业简称")
    private String shortName;

    @ApiModelProperty("省编号")
    private String province;

    @ApiModelProperty("市编号")
    private String city;

    @ApiModelProperty("区县编号")
    private String district;

    @ApiModelProperty("省市区名称")
    private String region;

    @ApiModelProperty("LOGO文件编号")
    private String logoImg;

    @ApiModelProperty("公司详细地址")
    private String address;

    @ApiModelProperty("公司电话")
    private String telephone;

    private List<SysRole> sysRoleList;
}
