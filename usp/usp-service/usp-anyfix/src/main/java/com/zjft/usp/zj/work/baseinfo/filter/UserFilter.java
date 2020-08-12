package com.zjft.usp.zj.work.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户filter类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-29 17:52
 **/

@ApiModel(value = "用户filter类")
@Data
public class UserFilter extends Page {

    @ApiModelProperty(value = "用户姓名")
    private String userName;

}
