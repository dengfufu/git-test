package com.zjft.usp.wms.baseinfo.model;

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
 * 业务小类定义抵销账务表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("business_small_class_reverse")
@ApiModel(value="SmallClassReverse对象", description="业务小类定义抵销账务表")
public class SmallClassReverse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "业务小类ID(固定编码)")
    @TableId("id")
    private Integer smallClassId;

    @ApiModelProperty(value = "是否需要抵销销售借用账(Y=是,N=否)")
    private String reverseSaleBorrow;

    @ApiModelProperty(value = "是否需要抵销厂商应还账 (Y=是,N=否)")
    private String reverseVendorReturn;

    @ApiModelProperty(value = "是否需要抵销应还厂商账(Y=是,N=否)")
    private String reverseReturnVendor;


}
