package com.zjft.usp.wms.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author : dcyu
 * @Date : 2019/11/20 10:23
 * @Desc : 物料区域过滤器
 * @Version 1.0.0
 */
@Setter
@Getter
public class WareAreaFilter extends Page {

    @ApiModelProperty("物料区域名称")
    private String name;

    private Long corpId;
}
