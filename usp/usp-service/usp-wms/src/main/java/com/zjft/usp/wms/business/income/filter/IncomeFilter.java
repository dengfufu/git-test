package com.zjft.usp.wms.business.income.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 入库Filter
 *
 * @author Qiugm
 * @version 1.0
 * @date 2019-11-11 18:49
 **/
@Setter
@Getter
public class IncomeFilter extends Page {
    @ApiModelProperty(value = "审核状态")
    private int auditStatus;

    @ApiModelProperty(value = "企业ID")
    private Long corpId;

    @ApiModelProperty(value = "业务大类ID(为了查询方便，使用冗余字段）")
    private Integer largeClassId;

    @ApiModelProperty(value = "业务小类ID(为了查询方便，使用冗余字段）")
    private Integer smallClassId;

    @ApiModelProperty(value = "库房ID")
    private Long depotId;

    @ApiModelProperty(value = "入库开始日期")
    private String incomeDateStart;

    @ApiModelProperty(value = "入库结束日期")
    private String incomeDateEnd;

    @ApiModelProperty(value = "入库单状态(10=待入库20=已入库)")
    private List<Integer> incomeStatuses;

    @ApiModelProperty(value = "物料产权")
    private Long propertyRight;

    @ApiModelProperty(value = "出厂序列号")
    private String sn;

    @ApiModelProperty(value = "条形码")
    private String barcode;

    @ApiModelProperty(value = "物料分类编号")
    private Long catalogId;

    @ApiModelProperty(value = "物料品牌编号")
    private Long brandId;

    @ApiModelProperty(value = "物料型号")
    private Long modelId;

    @ApiModelProperty(value = "当前审批人")
    private Long curAuditUserId;

}
