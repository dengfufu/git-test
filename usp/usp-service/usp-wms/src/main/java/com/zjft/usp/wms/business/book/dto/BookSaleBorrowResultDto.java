package com.zjft.usp.wms.business.book.dto;

import com.zjft.usp.wms.business.book.model.BookSaleBorrow;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: JFZOU
 * @Date: 2019-11-20 15:53
 */
@Getter
@Setter
public class BookSaleBorrowResultDto extends BookSaleBorrow {

    @ApiModelProperty(value = "分类名称")
    private String catalogName;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "型号名称")
    private String modelName;

    @ApiModelProperty(value = "库房名称")
    private String depotName;

    @ApiModelProperty(value = "物料状态名称")
    private String statusName;

    @ApiModelProperty(value = "产权名称")
    private String propertyRightName;

    @ApiModelProperty(value = "创建人姓名")
    private String createByName;

    @ApiModelProperty(value = "销账人姓名")
    private String reverseByName;

    @ApiModelProperty(value = "入库保存明细编号")
    private Long incomeDetailSaveId;

}
