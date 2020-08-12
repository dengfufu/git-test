package com.zjft.usp.uas.right.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 人员
 *
 * @author zgpi
 * @date 2020/6/4 15:36
 */
@Getter
@Setter
public class SysUserDto implements Serializable {

    private static final long serialVersionUID = 3558833609222435879L;

    @ApiModelProperty(value = "人员编号")
    private Long userId;

    @ApiModelProperty(value = "企业编号")
    private Long corpId;

    @ApiModelProperty(value = "人员范围权限列表")
    private List<SysUserRightScopeDto> userRightScopeList;
}
