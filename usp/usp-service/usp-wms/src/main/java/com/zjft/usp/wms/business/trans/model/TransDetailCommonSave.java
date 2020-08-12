package com.zjft.usp.wms.business.trans.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 调拨明细信息共用暂存表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("trans_d_common_save")
@ApiModel(value = "TransDetailCommonSave对象", description = "调拨明细信息共用暂存表")
public class TransDetailCommonSave implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "调拨明细ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "调拨单ID")
    private Long transId;

    @ApiModelProperty(value = "型号ID")
    private Long modelId;

    @ApiModelProperty(value = "规格值json格式{“内存”：“4G”,“颜色”：“红色”,“硬盘”：“256G”}")
    private String normsValue;

    @ApiModelProperty(value = "申请数量")
    private Integer applyQuantity;

    @ApiModelProperty(value = "出厂序列号")
    private String sn;

    @ApiModelProperty(value = "条形码")
    private String barcode;

    @ApiModelProperty(value = "物料状态")
    private Integer status;

}
