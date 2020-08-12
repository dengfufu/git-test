package com.zjft.usp.wms.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author : dcyu
 * @Date : 2019/11/20 17:27
 * @Desc : 物料型号过滤器
 * @Version 1.0.0
 */
@Setter
@Getter
public class WareModelFilter extends Page {

    @ApiModelProperty("物料型号名称")
    private String name;

    @ApiModelProperty("企业编号")
    private Long corpId;

    @ApiModelProperty("物料分类编号")
    private Long catalogId;

    @ApiModelProperty("品牌编号")
    private Long brandId;

    @ApiModelProperty("是否可用")
    private String enabled;

}
