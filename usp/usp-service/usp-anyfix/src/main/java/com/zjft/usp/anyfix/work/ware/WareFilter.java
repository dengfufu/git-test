package com.zjft.usp.anyfix.work.ware;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 部件filter
 *
 * @author canlei
 * @version 1.0
 * @date 2020-02-10 10:15
 **/
@ApiModel("部件filter")
@Data
public class WareFilter extends Page {

    @ApiModelProperty("工单编号")
    private Long workId;

    @ApiModelProperty("分类名称")
    private String catalogName;

    @ApiModelProperty("品牌名称")
    private String brandName;

    @ApiModelProperty("型号名称")
    private String modelName;

    @ApiModelProperty("公司编号")
    private Long corpId;

}
