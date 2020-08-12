package com.zjft.usp.wms.business.book.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 销售借用待还账表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("book_sale_borrow")
@ApiModel(value = "BookSaleBorrow对象", description = "销售借用待还账表")
public class BookSaleBorrow implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "销售出库明细ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "企业ID")
    private Long corpId;

    @ApiModelProperty(value = "销售单ID")
    private Long saleFormId;

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

    @ApiModelProperty(value = "销售出库数量(全部置为1)")
    private Integer saleQty;

    @ApiModelProperty(value = "创建人")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "是否销账(Y=是,N=否)")
    private String reversed;

    @ApiModelProperty(value = "销账表单明细ID")
    private Long reverseDetailId;

    @ApiModelProperty(value = "销账人")
    private Long reverseBy;

    @ApiModelProperty(value = "销账时间")
    private Date reverseTime;


}
