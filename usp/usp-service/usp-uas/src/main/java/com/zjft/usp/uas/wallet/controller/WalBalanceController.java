package com.zjft.usp.uas.wallet.controller;

import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.uas.wallet.service.WalBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @description: 前端控制器
 * @author chenxiaod
 * @date 2019/8/6 17:01
 */
@RestController
@RequestMapping("/wallet/balance")
public class WalBalanceController {

    @Autowired
    private WalBalanceService walBalanceService;

    @PostMapping(value="/walBalanceQuery")
    public Result walBalanceQuery(@LoginUser UserInfo userInfo) {
        BigDecimal balance  = walBalanceService.walBalanceQuery(userInfo.getUserId());
        return Result.succeed(balance);
    }
}
