package com.zjft.usp.wms.business.income.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.wms.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 入库信息共用表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("income_ware_common")
@ApiModel(value = "IncomeCommon对象", description = "入库信息共用表")
public class IncomeWareCommon implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "入库明细ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "可显示的入库单编号")
    private String incomeCode;

    @ApiModelProperty(value = "可显示的分组号/批次号(拆单后的分组号，如果本单继续拆单也是使用同一个分组号)")
    private String groupCode;

    @ApiModelProperty(value = "企业ID")
    private Long corpId;

    @ApiModelProperty(value = "流程实例ID")
    private Long flowInstanceId;

    @ApiModelProperty(value = "业务大类ID(为了查询方便，使用冗余字段）")
    private Integer largeClassId;

    @ApiModelProperty(value = "业务小类ID(为了查询方便，使用冗余字段）")
    private Integer smallClassId;

    @ApiModelProperty(value = "库房ID")
    private Long depotId;

    @ApiModelProperty(value = "入库日期")
    private String incomeDate;

    @ApiModelProperty(value = "入库单状态(10=待入库20=已入库)")
    private Integer incomeStatus;

    @ApiModelProperty(value = "物料产权")
    private Long propertyRight;

    @ApiModelProperty(value = "创建人")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private Long updateBy;

    @ApiModelProperty(value = "修改时间(先默认当前时间，修改再刷新)")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date updateTime;

    @ApiModelProperty(value = "型号ID")
    private Long modelId;

    @ApiModelProperty(value = "规格值json格式{“内存”：“4G”,“颜色”：“红色”,“硬盘”：“256G”}")
    private String normsValue;

    @ApiModelProperty(value = "数量")
    private Integer quantity;

    @ApiModelProperty(value = "维保到期日")
    private String serviceEndDate;

    @ApiModelProperty(value = "出厂序列号")
    private String sn;

    @ApiModelProperty(value = "条形码")
    private String barcode;

    @ApiModelProperty(value = "物料状态")
    private Integer status;

    @ApiModelProperty(value = "存储状态")
    private Integer situation;

    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "入库备注")
    private String description;
}
