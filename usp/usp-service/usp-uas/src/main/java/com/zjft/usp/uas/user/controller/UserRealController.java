package com.zjft.usp.uas.user.controller;

import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.uas.user.dto.UserRealDto;
import com.zjft.usp.uas.user.filter.UserRealFilter;
import com.zjft.usp.uas.user.service.UserRealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author zphu
 * @date 2019/9/27 14:30
 * @Version 1.0
 **/
@Api(tags = "实名认证")
@RequestMapping("/user-real")
@RestController
public class UserRealController {

    @Autowired
    private UserRealService userRealService;

    @ApiOperation(value = "获得当前登录用户实名认证信息")
    @GetMapping(value = "/get")
    public Result<UserRealDto> findUserReal(@LoginUser UserInfo userInfo) {
        return Result.succeed(userRealService.findUserReal(userInfo.getUserId()));
    }

    @ApiOperation(value = "远程调用：获得用户实名认证信息")
    @GetMapping(value = "/feign/{userId}")
    public Result<UserRealDto> findUserRealDtoById(@PathVariable("userId") Long userId) {
        return Result.succeed(userRealService.findUserRealDtoById(userId));
    }

    @ApiOperation(value = "远程调用：获得用户编号与姓名映射")
    @PostMapping(value = "/feign/mapByUserIdList")
    public Result<Map<Long, String>> mapByUserIdList(@RequestBody List<Long> userIdList) {
        return Result.succeed(userRealService.mapIdAndName(userIdList));
    }
}
