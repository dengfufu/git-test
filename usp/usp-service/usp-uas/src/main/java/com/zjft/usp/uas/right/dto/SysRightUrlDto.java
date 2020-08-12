package com.zjft.usp.uas.right.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * @author zgpi
 * @version 1.0
 * @date 2019/11/27 14:07
 */
@ApiModel("系统权限树")
@Getter
@Setter
public class SysRightUrlDto {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "权限ID")
    private Long rightId;

    @ApiModelProperty(value = "权限名称")
    private String rightName;

    @ApiModelProperty(value = "权限描述")
    private String description;

    @ApiModelProperty(value = "权限类型 1=公共权限，关联的菜单及操作不进行拦截；可以设置范围，默认只能看自己")
    private Integer rightType;

    @ApiModelProperty(value = "权限对应的请求URI")
    private String uri;

    @ApiModelProperty(value = "权限对应的请求方法")
    private String pathMethod;

}
