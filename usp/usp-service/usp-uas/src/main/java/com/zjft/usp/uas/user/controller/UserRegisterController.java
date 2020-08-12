package com.zjft.usp.uas.user.controller;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.user.dto.*;
import com.zjft.usp.uas.user.service.UserRegisterService;
import com.zjft.usp.uas.user.service.UserWxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


/**
 * @author zphu
 * @Descprition 用户基础表控制类
 * @date 2019/8/6 9:00
 * @Vesion 1.0
 **/
@Api(tags = "用户注册")
@RequestMapping("/register")
@RestController
public class UserRegisterController {

    @Autowired
    UserRegisterService userRegisterService;

    @Autowired
    UserWxService userWxService;

    @ApiOperation(value = "手机号注册")
    @PostMapping(value = "/mobile")
    public Result registerByMobile(@RequestBody UserInfoDto userInfoDto,
                                   @CommonReqParam ReqParam reqParam) {
        userRegisterService.registerUserByMobile(userInfoDto, reqParam,userInfoDto.getClientId());
        return Result.succeed();
    }

    @ApiOperation(value = "检查是否已注册")
    @PostMapping(value = "/checkIsRegister")
    public Result<Boolean> checkIsRegister(@Valid @RequestBody VerifyMessageDto verifyMessageDto) {
        Boolean result = userRegisterService.checkIsRegister(verifyMessageDto.getMobile());
        return Result.succeed(result);
    }

    @ApiOperation(value = "获得公钥")
    @GetMapping(value = "/getPublicKey")
    public Result<Map<String, String>> getPublicKey() {
        //返回公钥给前端加密密码，注意公钥返回给前端的json中含有'\n'字符，使用公钥加密前需要删除该字符
        Map<String, String> publicKey = new HashMap<>();
        publicKey.put("publicKey", userRegisterService.getPublicKey());
        return Result.succeed(publicKey);
    }

    @ApiOperation(value = "微信手机号注册")
    @PostMapping(value = "/wx/mobile")
    public Result registerByWXMobile(@RequestBody UserWxDto userWxDto,
                                   @CommonReqParam ReqParam reqParam) {
        userRegisterService.registerUserByWX(userWxDto, reqParam,userWxDto.getClientId());
        return Result.succeed();
    }

    @ApiOperation(value = "检查手机是否已注册和是否已经绑定微信")
    @PostMapping(value = "/checkMobile")
    public Result<Map<String, Boolean>> checkMobile(@Valid @RequestBody VerifyMessageDto verifyMessageDto) {
        Boolean isRegister = userRegisterService.checkIsRegister(verifyMessageDto.getMobile());
        Boolean isBindWx = false;
        // 已经存在账号
        if (isRegister) {
            isBindWx = userWxService.checkIsBindWx(verifyMessageDto.getMobile());
        }
        Map<String, Boolean> register = new HashMap<>();
        register.put("isBindWx", isBindWx);
        register.put("isRegister", isRegister);
        return Result.succeed(register);
    }

}
