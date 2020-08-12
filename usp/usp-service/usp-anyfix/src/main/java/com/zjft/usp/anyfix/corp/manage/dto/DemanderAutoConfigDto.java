package com.zjft.usp.anyfix.corp.manage.dto;

import com.zjft.usp.anyfix.corp.manage.model.DemanderAutoConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 委托商自动化配置Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2020-07-20 19:42
 **/
@ApiModel(value = "委托商自动化配置Dto")
@Getter
@Setter
public class DemanderAutoConfigDto extends DemanderAutoConfig {

    @ApiModelProperty(value = "委托商编号")
    private Long demanderCorp;

    @ApiModelProperty(value = "服务商编号")
    private Long serviceCorp;

    @ApiModelProperty(value = "结算方式名称")
    private String settleTypeName;

}
