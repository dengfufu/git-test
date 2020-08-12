package com.zjft.usp.uas.wallet.controller;

import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.uas.wallet.dto.WalDetailDto;
import com.zjft.usp.uas.wallet.service.WalDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @description: 前端控制器
 * @author chenxiaod
 * @date 2019/8/6 17:12
 */
@RestController
@RequestMapping("/wallet/detail")
public class WalDetailController {

    @Autowired
    private WalDetailService walDetailService;

    @PostMapping(value="/walDetailQuery")
    public Result<List<WalDetailDto>>  walDetailQuery(@LoginUser UserInfo userInfo) {
        return Result.succeed(walDetailService.walDetailQuery(userInfo.getUserId()));
    }
}
