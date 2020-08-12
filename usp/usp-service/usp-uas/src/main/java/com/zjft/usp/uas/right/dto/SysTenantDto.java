package com.zjft.usp.uas.right.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统租户
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/11/26 19:39
 */
@ApiModel("系统租户")
@Data
public class SysTenantDto {

    @ApiModelProperty(value = "企业ID")
    private Long corpId;

    @ApiModelProperty(value = "企业名称")
    private String corpName;

    @ApiModelProperty(value = "服务委托方 Y=是 N=否")
    private String serviceDemander;

    @ApiModelProperty(value = "服务提供商 Y=是 N=否")
    private String serviceProvider;

    @ApiModelProperty(value = "设备使用商 Y=是 N=否")
    private String deviceUser;

    @ApiModelProperty(value = "平台管理 Y=是 N=否")
    private String cloudManager;

    @ApiModelProperty(value = "是否需要绑定企业私有账号")
    private boolean needAccount;

    @ApiModelProperty(value = "用户校验API")
    private String applyCheckApi;

    @ApiModelProperty(value = "委托商级别")
    private Integer demanderLevel;

    @ApiModelProperty(value = "服务商级别")
    private Integer serviceLevel;
}
