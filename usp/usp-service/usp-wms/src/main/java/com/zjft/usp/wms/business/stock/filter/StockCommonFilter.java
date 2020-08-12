package com.zjft.usp.wms.business.stock.filter;

import cn.hutool.db.DaoTemplate;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.common.model.Page;
import com.zjft.usp.wms.config.DateToLongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @Author: JFZOU
 * @Date: 2019-11-20 14:25
 */

@Data
public class StockCommonFilter extends Page {

    @ApiModelProperty(value = "企业ID")
    private Long corpId;

    @ApiModelProperty(value = "业务大类ID")
    private Integer largeClassId;

    @ApiModelProperty(value = "业务小类ID")
    private Integer smallClassId;

    @ApiModelProperty(value = "库房ID")
    private Long depotId;

    @ApiModelProperty(value = "分类Id")
    private Long catalogId;

    @ApiModelProperty(value = "型号ID")
    private Long modelId;

    @ApiModelProperty(value = "品牌ID")
    private Long brandId;

    @ApiModelProperty(value = "规格值json格式{“内存”：“4G”,“颜色”：“红色”,“硬盘”：“256G”}")
    private String normsValue;

    @ApiModelProperty(value = "存储状态")
    private Integer situation;

    @ApiModelProperty(value = "物料状态")
    private Integer status;

    @ApiModelProperty(value = "产权ID")
    private Long propertyRight;

    @ApiModelProperty(value = "出厂序列号")
    private String sn;

    @ApiModelProperty(value = "条形码")
    private String barcode;

    @ApiModelProperty(value = "id集合")
    private Collection<Long> stockIdList;

    @ApiModelProperty(value = "创建时间区间集合")
    @JsonSerialize(using = DateToLongSerializer.class)
    private List<Date> createTimeList;
}
