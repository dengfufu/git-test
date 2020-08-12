package com.zjft.usp.uas.right.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zgpi
 * @version 1.0
 * @date 2019/11/27 11:12
 */
@ApiModel(value = "系统权限设置")
@Getter
@Setter
public class SysRightUrlFilter extends Page {

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

    @ApiModelProperty(value = "企业编号")
    private Long corpId;

}
