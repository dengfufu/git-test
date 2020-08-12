package com.zjft.usp.uas.right.filter;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SysRoleUserFilter {

    @ApiModelProperty(value = "企业编号")
    private Long corpId;

    @ApiModelProperty(value = "人员编号列表")
    private List<Long> userIdList;
}
