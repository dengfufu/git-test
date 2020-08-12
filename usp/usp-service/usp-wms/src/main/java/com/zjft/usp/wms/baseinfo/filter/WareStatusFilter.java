package com.zjft.usp.wms.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author : dcyu
 * @Date : 2019/11/26 10:19
 * @Desc : 物料状态过滤器
 * @Version 1.0.0
 */
@Setter
@Getter
public class WareStatusFilter extends Page {

    @ApiModelProperty("物料状态名称")
    private String name;

    @ApiModelProperty("企业ID")
    private Long corpId;
}
