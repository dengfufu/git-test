package com.zjft.usp.uas.right.filter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 人员范围权限
 *
 * @author zgpi
 * @version 1.0
 * @date 2020/3/13 17:10
 */
@ApiModel(value = "人员范围权限")
@Getter
@Setter
public class SysUserScopeRightFilter {

    @ApiModelProperty(value = "权限编号")
    private Long rightId;

    @ApiModelProperty(value = "人员编号")
    private Long userId;

    @ApiModelProperty(value = "人员编号列表")
    private List<Long> userIdList;

    @ApiModelProperty(value = "企业编号")
    private Long corpId;

    @ApiModelProperty(value = "范围类型")
    private Integer scopeType;

}
