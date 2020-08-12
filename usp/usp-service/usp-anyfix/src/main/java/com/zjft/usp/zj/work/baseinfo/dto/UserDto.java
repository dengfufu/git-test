package com.zjft.usp.zj.work.baseinfo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户DTO类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-29 17:51
 **/

@ApiModel(value = "用户DTO类")
@Data
public class UserDto implements Serializable {

    private static final long serialVersionUID = -7936312658182507299L;

    @ApiModelProperty(value = "用户编号")
    private String userId;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

}
