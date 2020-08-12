package com.zjft.usp.wms.business.book.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 厂商应还销账表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("book_vendor_vendor_return")
@ApiModel(value="BookVendorReturn对象", description="厂商应还销账表")
public class BookVendorReturn implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "厂商账明细ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "是否销账(Y=是,N=否)")
    private String reversed;

    @ApiModelProperty(value = "销账表单明细ID")
    private Long reverseDetailId;

    @ApiModelProperty(value = "销账人")
    private Long reverseBy;

    @ApiModelProperty(value = "销账时间")
    private Date reverseTime;


}
