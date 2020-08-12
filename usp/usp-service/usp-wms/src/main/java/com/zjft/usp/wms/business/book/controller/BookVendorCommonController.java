package com.zjft.usp.wms.business.book.controller;


import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.baseinfo.filter.WareStatusFilter;
import com.zjft.usp.wms.baseinfo.model.WareStatus;
import com.zjft.usp.wms.business.book.dto.BookVendorCommonDto;
import com.zjft.usp.wms.business.book.filter.BookVendorFilter;
import com.zjft.usp.wms.business.book.model.BookVendorCommon;
import com.zjft.usp.wms.business.book.service.BookVendorCommonService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 厂商账共用表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@RestController
@RequestMapping("/book-vendor-common")
public class BookVendorCommonController {

    @Autowired
    private BookVendorCommonService bookVendorCommonService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/query")
    public Result<ListWrapper<BookVendorCommonDto>> query(@RequestBody BookVendorFilter bookVendorFilter, @CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        bookVendorFilter.setCorpId(reqParam.getCorpId());
        return Result.succeed(this.bookVendorCommonService.queryBookVendor(bookVendorFilter,reqParam));
    }

}
