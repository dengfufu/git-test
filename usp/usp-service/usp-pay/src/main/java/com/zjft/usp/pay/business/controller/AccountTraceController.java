package com.zjft.usp.pay.business.controller;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.pay.business.filter.AccountTraceFilter;
import com.zjft.usp.pay.business.model.AccountTrace;
import com.zjft.usp.pay.business.service.AccountTraceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 账户流水表  前端控制器
 * </p>
 *
 * @author CK
 * @since 2020-05-26
 */
@RestController
@RequestMapping("/account-trace")
public class AccountTraceController {

    @Autowired
    AccountTraceService accountTraceService;

    @PostMapping(value = "/query")
    public Result<ListWrapper<AccountTrace>> query(@RequestBody AccountTraceFilter AccountTraceFilter){
        ListWrapper<AccountTrace> listWrapper = this.accountTraceService.query(AccountTraceFilter);
        return Result.succeed(listWrapper);
    }

}
