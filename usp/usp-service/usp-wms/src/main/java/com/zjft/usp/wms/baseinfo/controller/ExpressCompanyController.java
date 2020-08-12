package com.zjft.usp.wms.baseinfo.controller;


import cn.hutool.core.date.DateUtil;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.baseinfo.filter.ExpressCompanyFilter;
import com.zjft.usp.wms.baseinfo.model.ExpressCompany;
import com.zjft.usp.wms.baseinfo.service.ExpressCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 快递公司表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@RestController
@RequestMapping("/express-company")
public class ExpressCompanyController {

    @Autowired
    ExpressCompanyService expressCompanyService;

    @GetMapping("/list")
    public Result<List<ExpressCompany>> list(){
        return Result.succeed(expressCompanyService.list());
    }

    @GetMapping("/{companyId}")
    public Result<ExpressCompany> find(@PathVariable Long companyId){
        return Result.succeed(expressCompanyService.getById(companyId));
    }

    @PostMapping("/query")
    public Result<ListWrapper<ExpressCompany>> query(@RequestBody ExpressCompanyFilter expressCompanyFilter, @CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        expressCompanyFilter.setCorpId(reqParam.getCorpId());
        return Result.succeed(expressCompanyService.queryExpressCompany(expressCompanyFilter));
    }

    @PostMapping("/insert")
    public Result insert(@RequestBody ExpressCompany expressCompany, @LoginUser UserInfo user, @CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        expressCompany.setCorpId(reqParam.getCorpId());
        expressCompany.setCreateBy(user.getUserId());
        expressCompany.setCreateTime(DateUtil.date());
        expressCompanyService.insertExpressCompany(expressCompany);
        return Result.succeed();
    }

    @PostMapping("/update")
    public Result update(@RequestBody ExpressCompany expressCompany, @LoginUser UserInfo user, @CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        expressCompany.setUpdateBy(user.getUserId());
        expressCompany.setUpdateTime(DateUtil.date());
        expressCompanyService.updateExpressCompany(expressCompany);
        return Result.succeed();
    }

    @DeleteMapping("/{companyId}")
    public Result delete(@PathVariable Long companyId){
        expressCompanyService.deleteExpressCompany(companyId);
        return Result.succeed();
    }
}
