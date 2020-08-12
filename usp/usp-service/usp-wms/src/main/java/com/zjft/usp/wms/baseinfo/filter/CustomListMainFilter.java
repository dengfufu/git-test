package com.zjft.usp.wms.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: JFZOU
 * @Date: 2019-11-20 14:34
 */

@Data
public class CustomListMainFilter extends Page {

    @ApiModelProperty(value = "企业ID")
    private Long corpId;

    @ApiModelProperty(value = "列表名称")
    private String name;

    @ApiModelProperty(value = "列表分类(10=系统自定义列表20=用户自定义列表)")
    private Integer listClass;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "顺序号")
    private Integer sortNo;

    @ApiModelProperty(value = "是否可用 (Y=是,N=否)")
    private String enabled;
}
