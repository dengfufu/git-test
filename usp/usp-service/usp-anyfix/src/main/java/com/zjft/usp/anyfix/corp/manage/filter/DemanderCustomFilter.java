package com.zjft.usp.anyfix.corp.manage.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 服务委托方与用户企业filter
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-24 09:29
 **/
@Getter
@Setter
public class DemanderCustomFilter extends Page {

    @ApiModelProperty(value = "客户档案主键")
    private Long customId;

    @ApiModelProperty(value = "服务委托方企业编号")
    private Long demanderCorp;

    @ApiModelProperty(value = "客户企业编号")
    private Long customCorp;

    @ApiModelProperty(value = "客户企业名称")
    private String customCorpName;

    @ApiModelProperty(value = "是否可用")
    private String enabled;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区县")
    private String district;

    @ApiModelProperty(value = "客户名称，方便传往uas时使用，相当于serviceCorpName")
    private String corpName;

    @ApiModelProperty(value = "服务商企业")
    private Long serviceCorp;

    @ApiModelProperty(value = "企业编号")
    private Long corpId;
}
