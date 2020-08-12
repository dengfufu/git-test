package com.zjft.usp.wms.business.stock.controller;


import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.stock.dto.StockCommonResultDto;
import com.zjft.usp.wms.business.stock.filter.StockCommonFilter;
import com.zjft.usp.wms.business.stock.service.StockCommonService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 库存实时总账共用表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@RestController
@RequestMapping("/stock-common")
public class StockCommonController {

    @Autowired
    private StockCommonService stockCommonService;

    @ApiOperation(value = "分页查询库存实时数据")
    @PostMapping(value = "/pageBy")
    public Result<ListWrapper<StockCommonResultDto>> pageBy(@RequestBody StockCommonFilter stockCommonFilter,
                                                            @LoginUser UserInfo userInfo,
                                                            @CommonReqParam ReqParam reqParam) {
        stockCommonFilter.setCorpId(reqParam.getCorpId());
        return Result.succeed(stockCommonService.pageBy(stockCommonFilter, userInfo));
    }
}
