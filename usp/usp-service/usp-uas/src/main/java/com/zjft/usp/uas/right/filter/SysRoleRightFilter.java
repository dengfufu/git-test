package com.zjft.usp.uas.right.filter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zgpi
 * @version 1.0
 * @date 2019/11/28 15:57
 */
@ApiModel("角色权限filter")
@Data
public class SysRoleRightFilter {

    @ApiModelProperty(value = "角色编号", notes = "多个用逗号,隔开")
    private String roleIds;

    @ApiModelProperty(value = "企业编号")
    private Long corpId;
}
