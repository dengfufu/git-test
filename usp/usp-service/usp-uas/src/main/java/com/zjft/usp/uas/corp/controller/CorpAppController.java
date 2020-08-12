package com.zjft.usp.uas.corp.controller;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.corp.service.CorpAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "企业应用管理")
@RefreshScope
@RestController
@Slf4j
@RequestMapping(value = "/corp-app")
public class CorpAppController {

    @Autowired
    CorpAppService corpAppService;

    @ApiOperation("获取企业应用")
    @GetMapping(value = "/getExternalApps")
    public Result getExternalApps(@CommonReqParam ReqParam reqParam, @LoginUser UserInfo user) {
        return Result.succeed(corpAppService.getExternalApp(reqParam.getCorpId(), user.getUserId()));
    }

}
