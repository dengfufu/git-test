package com.zjft.usp.uas.corp.dto;

import com.zjft.usp.uas.corp.model.CorpUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 * @author canlei
 * @version 1.0
 * @date 2019-08-22 13:44
 **/
@Getter
@Setter
public class CorpUserDto extends CorpUser {

    @ApiModelProperty("是否为管理员")
    private boolean isAdmin;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("用户头像")
    private String faceImg;

    @ApiModelProperty("角色编号列表")
    private List<Long> roleIdList;

    @ApiModelProperty("角色名称")
    private String roleNames;

    @ApiModelProperty("用户状态，1:新加入、2: 离职")
    private int type;

    @ApiModelProperty("省份名称")
    private String provinceName;

    @ApiModelProperty("服务网点名称")
    private String serviceBranchNames;

}
