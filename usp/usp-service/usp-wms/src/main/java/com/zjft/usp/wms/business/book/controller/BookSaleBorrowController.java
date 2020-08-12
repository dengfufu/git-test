package com.zjft.usp.wms.business.book.controller;


import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.book.dto.BookSaleBorrowResultDto;
import com.zjft.usp.wms.business.book.filter.BookSaleBorrowFilter;
import com.zjft.usp.wms.business.book.model.BookSaleBorrow;
import com.zjft.usp.wms.business.book.service.BookSaleBorrowService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 销售借用待还账表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@RestController
@RequestMapping("/book-sale-borrow")
public class BookSaleBorrowController {

    @Autowired
    private BookSaleBorrowService bookSaleBorrowService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/query")
    public Result<ListWrapper<BookSaleBorrowResultDto>> query(@RequestBody BookSaleBorrowFilter bookSaleBorrowFilter,
                                                              @CommonReqParam ReqParam reqParam){
        bookSaleBorrowFilter.setCorpId(reqParam.getCorpId());
        return Result.succeed(bookSaleBorrowService.pageBy(bookSaleBorrowFilter));
    }

}
