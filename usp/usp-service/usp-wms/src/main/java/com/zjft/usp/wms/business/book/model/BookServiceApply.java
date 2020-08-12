package com.zjft.usp.wms.business.book.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 厂商物料保内申请表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("book_vendor_service_apply")
@ApiModel(value="BookServiceApply对象", description="厂商物料保内申请表")
public class BookServiceApply implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "厂商账明细ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "是否申请(Y=是,N=否)")
    private String isApply;

    @ApiModelProperty(value = "申请表单ID")
    private Long applyFormId;

    @ApiModelProperty(value = "申请表单明细ID")
    private Long applyDetailId;

    @ApiModelProperty(value = "申请人")
    private Long applyBy;

    @ApiModelProperty(value = "申请时间")
    private LocalDateTime applyTime;


}
