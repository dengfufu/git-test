package com.zjft.usp.wms.business.trans.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: JFZOU
 * @Date: 2019-11-14 15:28
 */
@ApiModel(value = "调拨单filter")
@Data
public class TransFilter extends Page {

    @ApiModelProperty(value = "业务大类ID")
    private Integer largeClassId;

    @ApiModelProperty(value = "业务小类ID")
    private Integer smallClassId;

    @ApiModelProperty(value = "企业编号")
    private Long corpId;

    @ApiModelProperty(value = "调拨主表Id")
    private Long transId;

    @ApiModelProperty(value = "可显示的调度单编号")
    private String transCode;

    @ApiModelProperty(value = "可显示的分组/批次号")
    private String groupCode;

    @ApiModelProperty(value = "出库库房编号")
    private Long fromDepotId;

    @ApiModelProperty(value = "收货库房编号")
    private Long toDepotId;

    @ApiModelProperty(value = "调拨单状态list")
    private List<Integer> transStatusList;

    @ApiModelProperty(value = "创建人用户编号")
    private Long createBy;

    @ApiModelProperty(value = "物料分类编号")
    private Long catalogId;

    @ApiModelProperty(value = "物料品牌编号")
    private Long brandId;

    @ApiModelProperty(value = "物料型号编号")
    private Long modelId;

    @ApiModelProperty(value = "出厂序列号")
    private String sn;

    @ApiModelProperty(value = "条形码")
    private String barcode;

    @ApiModelProperty(value = "流程节点类型,节点类型(10=填写节点20=普通审批节点30=会签审批节点40=发货节点50=收货节点60=确认节点)")
    private List<Integer> flowNodeTypeList;
}
