package com.zjft.usp.uas.corp.controller;


import com.zjft.usp.common.model.Result;
import com.zjft.usp.uas.corp.dto.CorpBankAccountDto;
import com.zjft.usp.uas.corp.model.CorpBankAccount;
import com.zjft.usp.uas.corp.service.CorpBankAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 企业银行账户表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2020-07-03
 */
@Api(tags = "企业银行账户")
@RestController
@RequestMapping("/corp-bank-account")
public class CorpBankAccountController {

    @Autowired
    private CorpBankAccountService corpBankAccountService;

    @ApiOperation(value = "获得企业默认银行账户")
    @GetMapping("/corp/{corpId}")
    public Result<CorpBankAccount> findDefaultBankAccount(@PathVariable("corpId") Long corpId) {
        return Result.succeed(corpBankAccountService.findDefaultBankAccount(corpId));
    }

    @ApiOperation(value = "远程调用：获取企业默认银行账户")
    @GetMapping("/feign/{corpId}")
    public Result<CorpBankAccountDto> findDefault(@PathVariable("corpId") Long corpId) {
        return Result.succeed(corpBankAccountService.finDefaultDtoByCorpId(corpId));
    }

    @ApiOperation(value = "远程调用：获取企业编号与默认账户的映射")
    @PostMapping("/feign/mapByCorpIdList")
    public Result<Map<Long, CorpBankAccountDto>> mapDefaultByCorpIdList(@RequestBody List<Long> corpIdList) {
        return Result.succeed(corpBankAccountService.mapDefaultByCorpIdList(corpIdList));
    }

}
