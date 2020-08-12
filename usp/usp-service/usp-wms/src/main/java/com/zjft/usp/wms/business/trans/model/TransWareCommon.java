package com.zjft.usp.wms.business.trans.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.sql.Date;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.wms.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 调拨信息共用表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("trans_ware_common")
@ApiModel(value = "TransWareCommon对象", description = "调拨信息共用表")
public class TransWareCommon implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "调拨明细ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "可显示的调拨单编号")
    private String transCode;

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

    @ApiModelProperty(value = "出库库房ID")
    private Long fromDepotId;

    @ApiModelProperty(value = "收货库房ID")
    private Long toDepotId;

    @ApiModelProperty(value = "申请日期")
    private String applyDate;

    @ApiModelProperty(value = "调拨单状态(10=待调拨20=调拨中30=已调拨)")
    private Integer transStatus;

    @ApiModelProperty(value = "优先级别")
    private Long priorityLevel;

    @ApiModelProperty(value = "出库描述")
    private String description;

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

    @ApiModelProperty(value = "出厂序列号")
    private String sn;

    @ApiModelProperty(value = "条形码")
    private String barcode;

    @ApiModelProperty(value = "物料状态")
    private Integer status;

    @ApiModelProperty(value = "申请数量")
    private Integer applyQuantity;

    @ApiModelProperty(value = "审批通过数量")
    private Integer passedQuantity;

    @ApiModelProperty(value = "已发货数量")
    private Integer consignedQuantity;
}
