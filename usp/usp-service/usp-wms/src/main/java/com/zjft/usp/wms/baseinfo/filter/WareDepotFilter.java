package com.zjft.usp.wms.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author : dcyu
 * @Date : 2019/11/20 14:38
 * @Desc : 物料库房过滤器
 * @Version 1.0.0
 */
@Setter
@Getter
public class WareDepotFilter extends Page {

    @ApiModelProperty("物料库房名称")
    private String name;

    private Long corpId;
}
