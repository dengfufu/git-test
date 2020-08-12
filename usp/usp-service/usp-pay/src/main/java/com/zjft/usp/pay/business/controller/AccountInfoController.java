package com.zjft.usp.pay.business.controller;

import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.pay.business.dto.CorpAccountDto;
import com.zjft.usp.pay.business.model.AccountInfo;
import com.zjft.usp.pay.business.service.AccountInfoService;
import com.zjft.usp.pay.business.service.feign.UasFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 账户表 前端控制器
 * </p>
 *
 * @author CK
 * @since 2020-05-26
 */
@Api(tags = "钱包管理")
@RestController
@RequestMapping("/account-info")
public class AccountInfoController {

    @Autowired
    AccountInfoService accountInfoService;

    @Autowired
    UasFeignService uasFeignService;

    @ApiOperation("开通企业钱包")
    @PostMapping(value = "/corp-init")
    public Result corpInitWallet(@RequestBody CorpAccountDto param, @LoginUser UserInfo userInfo) {
        // 远程调用，签订协议
        Map<String, Object> params = new HashMap<>();
        params.put("corpId", param.getCorpId());
        params.put("operator", userInfo.getUserId());
        params.put("protocolIds", param.getProtocolIds());

        Result feignResult = this.uasFeignService.signFeignToB(params);
        if (feignResult != null && feignResult.getCode() == Result.SUCCESS) {
            Long accountInfoId = accountInfoService.corpInitWallet(param.getCorpId());
            return Result.succeed(accountInfoId, "企业开通钱包成功");
        } else {
            return Result.failed("企业开通钱包失败: " + feignResult.getMsg());
        }

    }

    @ApiOperation("企业查看钱包信息")
    @PostMapping(value = "/corp-view")
    public Result<AccountInfo> accountInfoView(@RequestBody Map<String, String> param) {
        Long corpId = Long.valueOf(param.get("corpId"));
        AccountInfo accountInfo = accountInfoService.corpViewWallet(corpId);
        return Result.succeed(accountInfo);
    }
}
