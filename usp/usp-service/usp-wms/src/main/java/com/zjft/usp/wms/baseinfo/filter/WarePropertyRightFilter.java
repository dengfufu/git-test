package com.zjft.usp.wms.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author : dcyu
 * @Date : 2019/11/26 10:18
 * @Desc : 物料产权过滤器
 * @Version 1.0.0
 */
@Setter
@Getter
public class WarePropertyRightFilter extends Page {

    @ApiModelProperty("物料产权名称")
    private String name;

    @ApiModelProperty("企业ID")
    private Long corpId;
}
