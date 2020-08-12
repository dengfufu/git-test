package com.zjft.usp.auth.business.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.auth.business.model.Token;
import com.zjft.usp.auth.business.service.TokensService;
import com.zjft.usp.common.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author CK
 * @date 2019-09-19 09:04
 */
@RestController
@RequestMapping("/tokens")
public class TokenController {

    @Autowired
    private TokensService tokensService;

    @PostMapping("/list")
    public Result<IPage<Token>> list(@RequestBody Map<String, String> params, @RequestBody Page page) {
        String clientId = params.get("clientId");
        String logonId = params.get("logonId");
        IPage<Token> tokenPageList = tokensService.listTokens(page, logonId, clientId);
        return Result.succeed(tokenPageList);
    }
}
