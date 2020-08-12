package com.zjft.usp.uas.user.controller;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.user.composite.UserCompoService;
import com.zjft.usp.uas.user.dto.UserInfoDto;
import com.zjft.usp.uas.user.filter.UserInfoFilter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户基本表 前端控制器
 * </p>
 *
 * @author cxd
 * @since 2020-05-29
 */
@RestController
@RequestMapping("/user-info")
public class UserInfoController {
    @Autowired
    private UserCompoService userCompoService;

    @ApiOperation(value = "根据条件分页查询用户基本信息")
    @PostMapping(value = "/query")
    public Result<ListWrapper<UserInfoDto>> queryUserInfo(@RequestBody UserInfoFilter userInfoFilter,
                                                          @CommonReqParam ReqParam reqParam) {
        return Result.succeed(userCompoService.queryUserInfo(userInfoFilter, reqParam));
    }

    @ApiOperation(value = "模糊查询平台有效用户")
    @PostMapping(value = "/match")
    public Result<List<UserInfoDto>> matchUser(@RequestBody UserInfoFilter userInfoFilter,
                                               @CommonReqParam ReqParam reqParam) {
        return Result.succeed(userCompoService.matchUser(userInfoFilter, reqParam));
    }

    @ApiOperation(value = "获得单个用户基本信息")
    @GetMapping(value = "/detail/{userId}")
    public Result<UserInfoDto> findUserDetail(@PathVariable("userId") Long userId, @CommonReqParam ReqParam reqParam) {
        return Result.succeed(userCompoService.findUserDetail(userId, reqParam));
    }
}
