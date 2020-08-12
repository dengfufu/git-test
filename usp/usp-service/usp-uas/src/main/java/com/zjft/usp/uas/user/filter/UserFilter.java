package com.zjft.usp.uas.user.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zgpi
 * @version 1.0
 * @date 2019/12/18 15:08
 */
@ApiModel("用户filter")
@Getter
@Setter
public class UserFilter extends Page {

    @ApiModelProperty(value = "用户编号")
    private Long userId;

    @ApiModelProperty(value = "用户编号列表")
    private List<Long> userIdList;
}
