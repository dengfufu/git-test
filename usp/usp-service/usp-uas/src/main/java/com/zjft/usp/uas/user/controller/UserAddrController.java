package com.zjft.usp.uas.user.controller;

import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.uas.user.dto.UserAddrDto;
import com.zjft.usp.uas.user.service.UserAddrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户地址控制层
 *
 * @author zgpi
 * @version 1.0
 * @date 2019-08-20 10:38
 **/
@Api(tags = "用户地址")
@RestController
@RequestMapping("/address")
public class UserAddrController {

    @Autowired
    private UserAddrService userAddrService;

    @ApiOperation(value = "当前登录用户地址列表")
    @GetMapping(value = "/list")
    public Result<List<UserAddrDto>> listUserAddr(@LoginUser UserInfo userInfo) {
        List<UserAddrDto> list = userAddrService.listUserAddr(userInfo.getUserId());
        return Result.succeed(list);
    }

    @ApiOperation(value = "获得用户地址")
    @GetMapping(value = "/{addrId}")
    public Result<UserAddrDto> findUserAddr(@PathVariable("addrId") Long addrId) {
        UserAddrDto dto = userAddrService.findUserAddr(addrId);
        return Result.succeed(dto);
    }

    @ApiOperation(value = "添加当前登录用户地址")
    @PostMapping(value = "/add")
    public Result<Object> addUserAddr(@RequestBody UserAddrDto userAddrDto,
                                      @LoginUser UserInfo userInfo) {
        userAddrDto.setUserId(userInfo.getUserId());
        userAddrService.addUserAddr(userAddrDto);
        return Result.succeed();
    }

    @ApiOperation(value = "修改当前登录用户地址")
    @PostMapping(value = "/update")
    public Result<Object> updateUserAddr(@RequestBody UserAddrDto userAddrDto,
                                         @LoginUser UserInfo userInfo) {
        userAddrDto.setUserId(userInfo.getUserId());
        userAddrService.updateUserAddr(userAddrDto);
        return Result.succeed();
    }

    @ApiOperation(value = "删除用户地址")
    @DeleteMapping(value = "/{addrId}")
    public Result<Object> deleteUseAddr(@PathVariable("addrId") Long addrId) {
        userAddrService.deleteUserAddr(addrId);
        return Result.succeed();
    }

}
