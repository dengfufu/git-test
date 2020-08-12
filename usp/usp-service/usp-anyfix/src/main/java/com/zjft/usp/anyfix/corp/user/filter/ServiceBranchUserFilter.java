package com.zjft.usp.anyfix.corp.user.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 服务网点人员filter
 *
 * @author canlei
 * @version 1.0ServiceBranchUserFilter
 * @date 2019-10-14 09:17
 **/
@ApiModel("服务网点人员")
@Getter
@Setter
public class ServiceBranchUserFilter extends Page {

    @ApiModelProperty(value = "网点编号")
    private Long branchId;

    @ApiModelProperty(value = "企业编号")
    private Long corpId;

    @ApiModelProperty(value = "人员编号")
    private Long userId;

    @ApiModelProperty(value = "人员姓名")
    private String userName;

    @ApiModelProperty(value = "人员编号列表")
    private List<Long> userIdList;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "模糊查询")
    private String matchFilter;
}
