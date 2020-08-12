package com.zjft.usp.wms.business.outcome.model;

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
 * 出库信息共用表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("outcome_ware_common")
@ApiModel(value = "OutcomeWareCommon对象", description = "出库信息共用表")
public class OutcomeWareCommon implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "出库明细ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "可显示的出库单编号")
    private String outcomeCode;

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

    @ApiModelProperty(value = "申请日期")
    private String applyDate;

    @ApiModelProperty(value = "出库单状态(10=待出库20=已出库)")
    private Integer outcomeStatus;

    @ApiModelProperty(value = "出库描述")
    private String description;

    @ApiModelProperty(value = "创建人")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private Long updateBy;

    @ApiModelProperty(value = "修改时间(先默认当前时间，修改再刷新)")
    private Date updateTime;

    @ApiModelProperty(value = "库存明细ID")
    private Long stockId;

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

    @ApiModelProperty(value = "存储状态")
    private Integer situation;

    @ApiModelProperty(value = "物料产权")
    private Long propertyRight;

}
