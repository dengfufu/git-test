package com.zjft.usp.uas.right.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色filter
 *
 * @author zgpi
 * @version 1.0
 * @date 2019-10-12 09:51
 **/
@Getter
@Setter
public class SysRoleFilter extends Page {

    @ApiModelProperty("企业编号")
    private Long corpId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("是否启用")
    private String enabled;

    @ApiModelProperty("模糊查询")
    private String matchFilter;

}
