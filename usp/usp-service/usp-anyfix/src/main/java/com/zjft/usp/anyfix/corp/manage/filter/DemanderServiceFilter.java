package com.zjft.usp.anyfix.corp.manage.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 服务委托方管理服务商filter
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-24 08:53
 **/
@Getter
@Setter
public class DemanderServiceFilter extends Page {

    @ApiModelProperty(value = "委托商企业编号")
    private Long demanderCorp;

    @ApiModelProperty(value = "委托商企业名称")
    private String demanderCorpName;

    @ApiModelProperty(value = "服务商企业编号")
    private Long serviceCorp;

    @ApiModelProperty(value = "服务商企业名称")
    private String serviceCorpName;

    @ApiModelProperty("是否可用")
    private String enabled;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区县")
    private String district;

    @ApiModelProperty("客户名称，方便传往uas时使用，相当于serviceCorpName")
    private String corpName;

    @ApiModelProperty(value = "移动端搜索栏")
    private String mobileFilter;

    @ApiModelProperty(value = "模糊查询")
    private String matchFilter;

    @ApiModelProperty(value = "用于是否分页查询")
    private boolean forDemander;
}
