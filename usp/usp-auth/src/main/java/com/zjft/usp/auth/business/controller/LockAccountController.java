package com.zjft.usp.auth.business.controller;

import com.zjft.usp.auth.business.service.LockAccountService;
import com.zjft.usp.common.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 账户锁定
 *
 * @author: CK
 * @create: 2020-04-01 14:39
 */
@Slf4j
@RestController
public class LockAccountController {

    @Autowired
    LockAccountService lockAccountService;

    @PostMapping("/feign/lock-account/reset")
    public Result lockAccountReset(@RequestParam(name = "mobile") String mobile) {
        lockAccountService.resetPasswordErrorNumber(mobile);
        return Result.succeed();
    }

}
