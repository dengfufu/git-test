package com.zjft.usp.wms.business.consign.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.wms.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 发货明细信息表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("consign_d")
@ApiModel(value = "ConsignDetail对象", description = "发货明细信息表")
public class ConsignDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "发货明细ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "发货单号ID")
    private Long consignMainId;

    @ApiModelProperty(value = "业务大类ID")
    private Integer largeClassId;

    @ApiModelProperty(value = "业务小类ID")
    private Integer smallClassId;

    @ApiModelProperty(value = "对应明细ID(可以是审批明细ID,也可以是调拨申请明细ID、出库申请明细ID，如果是直接发货，如待修返还则可以为0)")
    private Long formDetailId;

    @ApiModelProperty(value = "发货库房ID")
    private Long fromDepotId;

    @ApiModelProperty(value = "收货库房ID")
    private Long toDepotId;

    @ApiModelProperty(value = "型号ID")
    private Long modelId;

    @ApiModelProperty(value = "规格值json格式{“内存”：“4G”,“颜色”：“红色”,“硬盘”：“256G”}")
    private String normsValue;

    @ApiModelProperty(value = "数量")
    private Integer quantity;

    @ApiModelProperty(value = "出厂序列号")
    private String sn;

    @ApiModelProperty(value = "条形码")
    private String barcode;

    @ApiModelProperty(value = "物料状态")
    private Integer status;

    @ApiModelProperty(value = "分箱号")
    private Integer subBoxNum;

    @ApiModelProperty(value = "是否签收 (Y=是,N=否)")
    private String signed;

    @ApiModelProperty(value = "签收人ID")
    private Long signBy;

    @ApiModelProperty(value = "签收时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date signTime;

    @ApiModelProperty(value = "库存明细ID")
    private Long stockId;

}
