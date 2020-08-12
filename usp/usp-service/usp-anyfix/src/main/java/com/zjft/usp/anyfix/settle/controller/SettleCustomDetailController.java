package com.zjft.usp.anyfix.settle.controller;

import com.zjft.usp.anyfix.settle.dto.SettleCustomDetailDto;
import com.zjft.usp.anyfix.settle.filter.SettleCustomDetailFilter;
import com.zjft.usp.anyfix.settle.service.SettleCustomDetailService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 客户结算单明细 前端控制器
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
@Api(tags = "客户结算单明细")
@RestController
@RequestMapping("/settle-custom-detail")
public class SettleCustomDetailController {

    @Autowired
    private SettleCustomDetailService customSettleDetailService;

    /**
     * 根据结算单号查询明细
     * @param settleCustomDetailFilter
     * @return
     */
    @PostMapping(value = "/query")
    public Result<ListWrapper<SettleCustomDetailDto>> pageDetailBySettleId(@RequestBody SettleCustomDetailFilter settleCustomDetailFilter){
        ListWrapper<SettleCustomDetailDto> list = this.customSettleDetailService.pageByFilter(settleCustomDetailFilter);
        return Result.succeed(list);
    }

}
