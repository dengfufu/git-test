package com.zjft.usp.wms.business.book.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zphu
 * @date 2019/12/10 10:01
 * @Version 1.0
 **/
@Data
public class BookVendorFilter extends Page {

    @ApiModelProperty(value = "企业ID")
    private Long corpId;

    @ApiModelProperty(value = "工单ID")
    private Long workId;
}
