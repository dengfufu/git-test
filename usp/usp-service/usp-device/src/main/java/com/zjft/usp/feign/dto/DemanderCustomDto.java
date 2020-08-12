package com.zjft.usp.feign.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 服务委托方与用户企业
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-24 09:26
 **/
@ApiModel(value = "服务委托方与用户企业Dto")
@Getter
@Setter
public class DemanderCustomDto {

    @ApiModelProperty(value = "主键编号")
    private Long customId;

    @ApiModelProperty(value = "服务委托方企业编号")
    private Long demanderCorp;

    @ApiModelProperty(value = "服务委托方企业名称")
    private String demanderCorpName;

    @ApiModelProperty(value = "用户企业编号")
    private Long customCorp;

    @ApiModelProperty(value = "用户企业名称")
    private String customCorpName;

    @ApiModelProperty(value = "是否可用，Y=可用，N=不可用")
    private String enabled;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "地区")
    private String region;

    @ApiModelProperty(value = "联系电话")
    private String telephone;

    @ApiModelProperty(value = "详细地址")
    private String address;

}
