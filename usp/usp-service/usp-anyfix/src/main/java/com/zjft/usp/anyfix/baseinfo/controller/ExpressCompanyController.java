package com.zjft.usp.anyfix.baseinfo.controller;


import com.zjft.usp.anyfix.baseinfo.filter.ExpressCompanyFilter;
import com.zjft.usp.anyfix.baseinfo.model.ExpressCompany;
import com.zjft.usp.anyfix.baseinfo.service.ExpressCompanyService;
import com.zjft.usp.common.model.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 快递公司 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2020-04-16
 */
@RestController
@RequestMapping("/express-company")
public class ExpressCompanyController {

    @Autowired
    private ExpressCompanyService expressCompanyService;

    @ApiOperation("模糊查询快递公司")
    @PostMapping(value = "/match")
    public Result<List<ExpressCompany>> matchExpressCorp(@RequestBody ExpressCompanyFilter expressCompanyFilter) {
        return Result.succeed(expressCompanyService.matchExpressCorp(expressCompanyFilter));
    }
}
