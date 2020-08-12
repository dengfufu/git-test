package com.zjft.usp.anyfix.settle.filter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 网点结算单filter
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-11 09:25
 **/
@Getter
@Setter
public class SettleBranchFilter extends Page {

    @ApiModelProperty(value = "服务商编号")
    private Long serviceCorp;

    @ApiModelProperty(value = "网点名称")
    private String branchName;

    @ApiModelProperty(value = "网点编号")
    private Long branchId;

}
