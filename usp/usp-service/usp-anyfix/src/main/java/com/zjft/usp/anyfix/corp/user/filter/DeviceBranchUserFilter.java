package com.zjft.usp.anyfix.corp.user.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zgpi
 * @version 1.0
 * @date 2019/12/18 09:56
 */
@ApiModel("设备网点人员filter")
@Getter
@Setter
public class DeviceBranchUserFilter extends Page {

    @ApiModelProperty("网点编号")
    private Long branchId;

    @ApiModelProperty("企业编号")
    private Long corpId;

    @ApiModelProperty("人员名字")
    private String userName;
}
