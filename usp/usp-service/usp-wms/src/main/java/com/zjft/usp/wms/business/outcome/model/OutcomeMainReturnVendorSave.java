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
 * 出库基本信息归还厂商专属暂存表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("outcome_m_return_vendor_save")
@ApiModel(value = "OutcomeMainReturnVendorSave对象", description = "出库基本信息归还厂商专属暂存表")
public class OutcomeMainReturnVendorSave implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "出库单ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "供应商ID")
    private Long supplierId;


}
