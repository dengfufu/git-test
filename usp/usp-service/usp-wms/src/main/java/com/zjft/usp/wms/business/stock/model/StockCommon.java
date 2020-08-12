package com.zjft.usp.wms.business.stock.model;

import java.math.BigDecimal;

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
 * 库存实时总账共用表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("stock_common")
@ApiModel(value="StockCommon对象", description="库存实时总账共用表")
public class StockCommon implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "库存明细ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "企业ID")
    private Long corpId;

    @ApiModelProperty(value = "库存类型(10=库房库存，20=个人库存)")
    private Integer stockType;

    @ApiModelProperty(value = "新生成stock_common时所依赖的业务明细ID，ID可以是常规入库单明细ID，调拨单明细ID，出库单明细ID，工单明细ID，还可以是调拨单明细ID、出库单明细ID等。")
    private Long inFormDetailId;

    @ApiModelProperty(value = "业务大类ID")
    private Integer inLargeClassId;

    @ApiModelProperty(value = "业务小类ID")
    private Integer inSmallClassId;

    @ApiModelProperty(value = "库房ID")
    private Long depotId;

    @ApiModelProperty(value = "型号ID")
    private Long modelId;

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

    @ApiModelProperty(value = "入库数量(入库数量是入库时的记录，保持不变，当变动时需要全量拆分)")
    private Integer incomeQty;

    @ApiModelProperty(value = "直接库存明细ID，即新库存明细ID是由哪条库存明细ID生成的")
    private Long directSourceStockId;

    @ApiModelProperty(value = "根库存明细ID,即源头库存明细ID，可以为0，如果为0表示当前库存明细就是源头")
    private Long rootSourceStockId;

    @ApiModelProperty(value = "维保到期日")
    private String serviceEndDate;

    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "采购类型(10=公司采购 20=物品采购)")
    private Integer purchaseType;

    @ApiModelProperty(value = "创建人")
    private Long inCreateBy;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date inCreateTime;

    @ApiModelProperty(value = "是否可用 (Y=是)")
    private String enabled;

    @ApiModelProperty(value = "出库对应的业务单号")
    private Long outFormDetailId;

    @ApiModelProperty(value = "出库业务大类ID")
    private Integer outLargeClassId;

    @ApiModelProperty(value = "出库业务小类ID")
    private Integer outSmallClassId;

    @ApiModelProperty(value = "出库创建人")
    private Long outCreateBy;

    @ApiModelProperty(value = "出库创建时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date outCreateTime;


    public boolean isExistsSN() {
        if (this.getSn() != null && this.getSn().length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isExistsBarcode() {
        if (this.getBarcode() != null && this.getBarcode().length() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
