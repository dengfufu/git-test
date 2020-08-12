package com.zjft.usp.anyfix.settle.filter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 客户结算单filter
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-11 08:45
 **/
@Getter
@Setter
public class SettleCustomFilter extends Page {

    @ApiModelProperty(value = "服务商企业编号")
    private Long serviceCorp;

    @ApiModelProperty(value = "客户企业编号")
    private Long customCorp;

    @ApiModelProperty(value = "客户企业名称")
    private String customName;

}
