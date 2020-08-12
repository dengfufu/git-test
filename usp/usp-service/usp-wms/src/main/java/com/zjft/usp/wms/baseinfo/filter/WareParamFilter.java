package com.zjft.usp.wms.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author : dcyu
 * @Date : 2019/11/21 16:51
 * @Desc : 物料系统参数过滤器
 * @Version 1.0.0
 */
@Setter
@Getter
public class WareParamFilter extends Page {

    @ApiModelProperty("参数编码")
    private String paramCode;

    @ApiModelProperty("参数名称")
    private String paramName;

    @ApiModelProperty("企业ID")
    private Long corpId;
}
