package com.zjft.usp.uas.corp.controller;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginClient;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.corp.dto.CorpVerifyAppDto;
import com.zjft.usp.uas.corp.dto.CorpVerifyDto;
import com.zjft.usp.uas.corp.filter.CorpVerifyAppFilter;
import com.zjft.usp.uas.corp.service.CorpVerifyAppService;
import com.zjft.usp.uas.corp.service.CorpVerifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 企业认证 控制器
 *
 * @author canlei
 * @version 1.0
 * @date 2019-09-29 10:03
 **/
@Api(tags = "企业认证")
@RestController
@RequestMapping(value = "/corp-verify")
public class CorpVerifyController {

    @Autowired
    private CorpVerifyService corpVerifyService;
    @Autowired
    private CorpVerifyAppService corpVerifyAppService;

    @ApiOperation("分页查询企业认证申请")
    @PostMapping(value = "/query")
    public Result<ListWrapper<CorpVerifyAppDto>> pageCorpVerifyApp(@RequestBody CorpVerifyAppFilter corpVerifyAppFilter) {
        return Result.succeed(corpVerifyAppService.pageByFilter(corpVerifyAppFilter));
    }

    @ApiOperation("企业认证申请")
    @PostMapping(value = "/apply")
    public Result corpVerifyApply(@RequestBody CorpVerifyAppDto corpVerifyAppDto,
                                  @CommonReqParam ReqParam reqParam,
                                  @LoginClient String clientId,
                                  @LoginUser UserInfo user) {
        corpVerifyAppService.corpVerifyApply(corpVerifyAppDto, reqParam, user.getUserId(), clientId);
        return Result.succeed();
    }

    @ApiOperation("企业认证审核")
    @PostMapping(value = "/audit")
    public Result<Object> corpVerifyCheck(@RequestBody CorpVerifyAppDto corpVerifyAppDto,
                                          @CommonReqParam ReqParam reqParam,
                                          @LoginClient String clientId,
                                          @LoginUser UserInfo user) {
        return corpVerifyAppService.corpVerifyCheck(corpVerifyAppDto, reqParam, user.getUserId(), clientId);
    }

    @ApiOperation("获取企业认证详细信息")
    @GetMapping(value = "/{corpId}")
    public Result<CorpVerifyDto> queryCorpVerify(@PathVariable("corpId") Long corpId,
                                               @LoginUser UserInfo userInfo) {
        return Result.succeed(corpVerifyService.queryCorpVerify(corpId, userInfo));
    }

    @ApiOperation("更改企业认证信息")
    @PostMapping(value = "/update")
    public Result updateCorpVerify(@RequestBody CorpVerifyDto CorpVerifyDto,@LoginUser UserInfo user) {
        corpVerifyService.updateCorpVerify(CorpVerifyDto,user);
        return Result.succeed();
    }

}
