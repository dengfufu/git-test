package com.zjft.usp.anyfix.corp.user.dto;

import com.zjft.usp.anyfix.corp.user.model.DeviceBranchUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zgpi
 * @version 1.0
 * @date 2019/12/18 09:54
 */
@ApiModel("设备网点人员")
@Getter
@Setter
public class DeviceBranchUserDto extends DeviceBranchUser {

    @ApiModelProperty("网点人员姓名")
    private String userName;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("角色")
    private String roleNames;

    @ApiModelProperty("人员列表")
    private List<Long> userIdList;
}
