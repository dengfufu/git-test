package com.zjft.usp.wms.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 物料品牌DTO
 *
 * @author cxd
 * @version 1.0
 * @date 2019/11/15 2:37 下午
 **/
@ApiModel(value = "设备品牌filter")
@Data
public class WareBrandFilter extends Page {

    @ApiModelProperty(value = "品牌名称")
    private String name;

    @ApiModelProperty(value = "企业ID")
    private Long corpId;

    @ApiModelProperty(value = "是否可用")
    private String enabled;

}
