package com.zjft.usp.wms.business.consign.dto;

import com.zjft.usp.wms.business.consign.model.ConsignDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author zphu
 * @date 2019/12/4 10:26
 * @Version 1.0
 **/
@Data
public class ConsignDetailDto extends ConsignDetail {

    @ApiModelProperty(value = "可显示的发货单号")
    private String consignCode;

    @ApiModelProperty(value = "发货日期")
    private String consignDate;

    @ApiModelProperty(value = "发货人ID")
    private Long consignBy;

    @ApiModelProperty(value = "发货人姓名")
    private String consignByName;

    @ApiModelProperty(value = "发货人联系手机号")
    private String consignMobile;

    @ApiModelProperty(value = "快递公司ID")
    private Long expressCorpId;

    @ApiModelProperty(value = "快递单号ID")
    private String expressNo;

    @ApiModelProperty(value = "公司收货人ID")
    private Long receiverId;

    @ApiModelProperty(value = "收货人显示名称(出库时，收货方可能不是本公司人员，无法使用ID，此时可使用名称)")
    private String receiveName;

    @ApiModelProperty(value = "收货人联系手机号")
    private String receiverMobile;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "库房ID")
    private Long depotId;

    @ApiModelProperty(value = "企业ID")
    private Long corpId;

    @ApiModelProperty(value = "快递公司名称")
    private String expressCorpName;

    @ApiModelProperty(value = "签收人姓名")
    private String signByName;

    @ApiModelProperty(value = "运输方式自定义列表")
    private Long transportTypeId;

    @ApiModelProperty(value = "运输方式自定义列表name")
    private String transportTypeName;

    @ApiModelProperty(value = "收货地址")
    private String receiveDistrict;

    @ApiModelProperty(value = "收货地址中文")
    private String receiveDistrictAddress;

    @ApiModelProperty(value = "收货详细地址")
    private String receiveAddress;

    @ApiModelProperty(value = "可显示的调拨单编号")
    private String transCode;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "物料名称")
    private String catalogName;

    @ApiModelProperty(value = "型号")
    private String modelName;

    @ApiModelProperty(value = "型号")
    private String fromDepotName;

    @ApiModelProperty(value = "总箱数")
    private Integer totalBoxNum;

    @ApiModelProperty(value = "备件状态")
    private String situationName;

    @ApiModelProperty(value = "单价")
    private Integer unitPrice;

    @ApiModelProperty(value = "在保状态")
    private String statusName;

    @ApiModelProperty(value = "维保到期日")
    private String serviceEndDate ;

   /* @ApiModelProperty(value = "厂商序列号")
    private String sn;*/

    /*@ApiModelProperty(value = "唯一编码")
    private String barcode;*/

}
