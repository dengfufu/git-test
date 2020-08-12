package com.zjft.usp.wms.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 物品分类filter
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/14 1:17 下午
 **/
@ApiModel(value = "物品分类filter")
@Getter
@Setter
public class WareClassFilter extends Page {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "样图：图片文件ID")
    private Long wareImg;

    @ApiModelProperty(value = "是否通用")
    private Integer isCommon;

    @ApiModelProperty(value = "是否可用，1=可用，2=不可用")
    private String enabled;

    @ApiModelProperty(value = "服务商")
    private Long corpId;
}
