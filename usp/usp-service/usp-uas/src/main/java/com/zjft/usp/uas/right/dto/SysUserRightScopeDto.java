package com.zjft.usp.uas.right.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 人员范围权限
 *
 * @author zgpi
 * @date 2020/6/3 16:17
 */
@Getter
@Setter
public class SysUserRightScopeDto implements Serializable {

    private static final long serialVersionUID = 6806890016205970654L;

    @ApiModelProperty(value = "角色编号")
    private Long userId;

    @ApiModelProperty(value = "企业编号")
    private Long corpId;

    @ApiModelProperty(value = "权限编号")
    private Long rightId;

    @ApiModelProperty(value = "范围类型，10=服务网点")
    private Integer scopeType;

    @ApiModelProperty(value = "是否有全部权限")
    private String hasAllScope;

    @ApiModelProperty(value = "是否有所在组织权限")
    private String hasOwnScope;

    @ApiModelProperty(value = "是否有所在组织的下级权限")
    private String hasOwnLowerScope;

    @ApiModelProperty(value = "指定组织ID列表")
    private List<String> orgIdList;

    @ApiModelProperty(value = "指定组织名称")
    private String orgNames;

    @ApiModelProperty(value = "是否包含下级， Y=是 N=否")
    private String containLower;

    @ApiModelProperty(value = "范围内的组织ID列表", notes = "数据平台使用")
    private List<String> idList;

    @ApiModelProperty(value = "范围权限类型，数据平台使用", notes = "1=全部，2=有部分范围，3=无范围")
    private Integer scopeRightType;
}
