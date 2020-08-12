package com.zjft.usp.anyfix.corp.manage.dto;

import com.zjft.usp.anyfix.corp.manage.model.DemanderCustom;
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
public class DemanderCustomDto extends DemanderCustom {

    @ApiModelProperty(value = "服务委托方企业名称")
    private String demanderCorpName;

    @ApiModelProperty(value = "地区")
    private String region;

    @ApiModelProperty(value = "联系电话")
    private String telephone;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "详细地址")
    private boolean demander;
}
