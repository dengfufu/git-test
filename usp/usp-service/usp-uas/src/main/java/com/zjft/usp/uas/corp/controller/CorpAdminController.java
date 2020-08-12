package com.zjft.usp.uas.corp.controller;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.corp.dto.CorpAdminDto;
import com.zjft.usp.uas.corp.dto.CorpUserDto;
import com.zjft.usp.uas.corp.service.CorpAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 企业管理员 控制器
 *
 * @author canlei
 * @version 1.0
 * @date 2019-09-29 10:42
 **/
@Api(tags = "企业管理员")
@RestController
@Slf4j
//@RequestMapping(value = "/corp-admin")
public class CorpAdminController {
//
//    @Autowired
//    private CorpAdminService corpAdminService;
//
//    @ApiOperation("设置企业管理员")
//    @PostMapping(value = "/add")
//    public Result addCorpAdmin(@RequestBody CorpAdminDto corpAdminDto,
//                                       @CommonReqParam ReqParam reqParam,
//                                       @LoginUser UserInfo user) {
//        corpAdminService.setCorpManager(corpAdminDto, reqParam, user.getUserId());
//        return Result.succeed();
//    }
//
//    @ApiOperation("根据企业查询管理员列表")
//    @GetMapping(value = "/list/{corpId}")
//    public Result<List<CorpUserDto>> searchCorpUser(@PathVariable("corpId") Long corpId) {
//        return Result.succeed(this.corpAdminService.listCorpAdmin(corpId));
//    }

}
