package com.zjft.usp.uas.corp.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 企业人员filter
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-12 08:55
 **/
@Getter
@Setter
public class CorpUserFilter extends Page {

    @ApiModelProperty(value = "企业编号")
    private Long corpId;

    @ApiModelProperty(value = "用户编号")
    private String userId;

    @ApiModelProperty("是否隐藏(0=显示，1=隐藏)")
    private Long hidden;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "用户编号列表")
    private List<Long> userIdList;

    @ApiModelProperty(value = "移动端搜索栏")
    private String mobileFilter;

    @ApiModelProperty(value = "模糊查询搜索")
    private String matchFilter;

    @ApiModelProperty(value = "企业编号")
    private List<Long> excludeUserIdList;

    @ApiModelProperty(value = "角色编号")
    private String roleId;

    @ApiModelProperty(value = "用户状态")
    private int type;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "服务网点编号")
    private Long serviceBranch;

    @ApiModelProperty(value = "第三方企业员工账号列表")
    private List<String> accountList;
}
