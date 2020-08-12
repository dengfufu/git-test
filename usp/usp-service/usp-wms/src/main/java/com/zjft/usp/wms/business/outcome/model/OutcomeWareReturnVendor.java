package com.zjft.usp.wms.business.outcome.model;

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
 * 出库信息归还厂商专用表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("outcome_ware_return_vendor")
@ApiModel(value = "OutcomeWareReturnVendor对象", description = "出库信息归还厂商专用表")
public class OutcomeWareReturnVendor implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "出库明细ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "供应商ID")
    private Long supplierId;

    @ApiModelProperty(value = "工单ID")
    private Long workId;

    @ApiModelProperty(value = "厂商应还入库单号ID")
    private Long incomeId;

    @ApiModelProperty(value = "设备型号ID")
    private Long deviceModelId;

    @ApiModelProperty(value = "设备出厂序列号")
    private String deviceSn;


}
