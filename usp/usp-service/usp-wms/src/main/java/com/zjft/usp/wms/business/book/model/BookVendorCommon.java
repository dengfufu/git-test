package com.zjft.usp.wms.business.book.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 厂商账共用表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("book_vendor_common")
@ApiModel(value="BookVendorCommon对象", description="厂商账共用表")
public class BookVendorCommon implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "厂商账明细ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "企业ID")
    private Long corpId;

    @ApiModelProperty(value = "工单ID")
    private Long workId;

    @ApiModelProperty(value = "工单更换ID")
    private Long replaceId;

    @ApiModelProperty(value = "库房ID")
    private Long depotId;

    @ApiModelProperty(value = "型号ID")
    private Long modelId;

    @ApiModelProperty(value = "规格值json格式{“内存”：“4G”,“颜色”：“红色”,“硬盘”：“256G”}")
    private String normsValue;

    @ApiModelProperty(value = "物料状态")
    private Integer status;

    @ApiModelProperty(value = "产权ID")
    private Long propertyRight;

    @ApiModelProperty(value = "出厂序列号")
    private String sn;

    @ApiModelProperty(value = "条形码")
    private String barcode;

    @ApiModelProperty(value = "生成数量(数量多个需要拆分成1条，全部置为1)")
    private Integer qty;

    @ApiModelProperty(value = "创建人")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
