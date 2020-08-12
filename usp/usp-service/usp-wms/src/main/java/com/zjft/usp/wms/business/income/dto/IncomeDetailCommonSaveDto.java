package com.zjft.usp.wms.business.income.dto;

import com.zjft.usp.wms.business.book.dto.BookSaleBorrowResultDto;
import com.zjft.usp.wms.business.book.model.BookSaleBorrow;
import com.zjft.usp.wms.business.income.model.IncomeDetailCommonSave;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 入库明细Dto
 *
 * @author Qiugm
 * @version 1.0
 * @date 2019-11-19 19:45
 **/
@ApiModel(value = "入库明细Dto")
@Getter
@Setter
public class IncomeDetailCommonSaveDto extends IncomeDetailCommonSave {

    @ApiModelProperty(value = "物料状态名称")
    private String statusName;

    @ApiModelProperty(value = "库存状态名称")
    private String situationName;

    @ApiModelProperty(value = "物料型号名称")
    private String modelName;

    @ApiModelProperty(value = "物料分类编号")
    private Long catalogId;

    @ApiModelProperty(value = "物料分类名称")
    private String catalogName;

    @ApiModelProperty(value = "物料品牌编号")
    private Long brandId;

    @ApiModelProperty(value = "物料品牌名称")
    private String brandName;

    @ApiModelProperty(value = "销账明细编号list")
    List<Long> bookIdList;

    @ApiModelProperty(value = "销售借用待还账list")
    List<BookSaleBorrowResultDto> bookSaleBorrowResultDtoList;

}
