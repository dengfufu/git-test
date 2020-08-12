package com.zjft.usp.uas.user.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author cxd
 * @version 1.0
 * @date 2019/12/18 15:08
 */
@ApiModel("用户操作filter")
@Getter
@Setter
public class UserOperFilter extends Page {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "当前企业编号")
    private Long corpId;

    @ApiModelProperty(value = "操作类型，1=登录 2=自动登录 3=登出")
    private Long operType;

    @ApiModelProperty(value = "操作时间开始")
    private Date operateTimeStart;

    @ApiModelProperty(value = "操作时间结束")
    private Date operateTimeEnd;
}
