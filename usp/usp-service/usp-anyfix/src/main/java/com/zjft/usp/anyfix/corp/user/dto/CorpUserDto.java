package com.zjft.usp.anyfix.corp.user.dto;

import com.zjft.usp.anyfix.corp.branch.model.DeviceBranch;
import com.zjft.usp.anyfix.corp.branch.model.ServiceBranch;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 企业人员
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-14 18:53
 **/
@Data
public class CorpUserDto {

    @ApiModelProperty("企业编号")
    private Long corpId;

    @ApiModelProperty("用户编号")
    private Long userId;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("添加时间")
    private Date addTime;

    @ApiModelProperty("是否为管理员")
    private boolean isAdmin;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("服务网点列表")
    private List<ServiceBranch> serviceBranchList;

    @ApiModelProperty("设备网点列表")
    private List<DeviceBranch> deviceBranchList;

    @ApiModelProperty("服务网点列表")
    private List<Long> serviceBranchIdList;

    @ApiModelProperty("设备网点编号列表")
    private List<Long> deviceBranchIdList;

    @ApiModelProperty("角色编号列表")
    private List<Long> roleIdList;

    @ApiModelProperty("角色权限名称")
    private String roleNames;

    @ApiModelProperty(value = "网点编号", notes = "包括服务网点和设备网点")
    private String branchIds;

    @ApiModelProperty("服务网点名称")
    private String serviceBranchNames;

    @ApiModelProperty("设备网点名称")
    private String deviceBranchNames;

}
