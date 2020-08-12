package com.zjft.usp.uas.user.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 用户基本filter
 *
 * @author cxd
 * @version 1.0
 * @date 2020/5/26 3:02 下午
 **/

@ApiModel(value = "用户基本filter")
@Data
public class UserInfoFilter extends Page {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户名称")
    private String customCorpName;

    @ApiModelProperty(value = "当前企业编号")
    private Long corpId;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "1=男 2=女 空=不知道")
    private String sex;

    @ApiModelProperty(value = "国家")
    private String country;

    @ApiModelProperty(value = "省份代码")
    private String province;

    @ApiModelProperty(value = "地市代码")
    private String city;

    @ApiModelProperty(value = "区县代码")
    private String district;

    @ApiModelProperty(value = "1=正常 2=失效")
    private String status;

    @ApiModelProperty(value = "注册时间开始")
    private Date regTimeStart;

    @ApiModelProperty(value = "注册时间结束")
    private Date regTimeTimeEnd;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "模糊查询")
    private String matchFilter;
}
