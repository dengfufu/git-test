package com.zjft.usp.uas.right.controller;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.right.composite.SysUserRightCompoService;
import com.zjft.usp.uas.right.dto.SysUserFullRightDto;
import com.zjft.usp.uas.right.filter.SysRightFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户权限控制器
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/12/6 09:19
 */
@Api(tags = "用户权限")
@RestController
@RequestMapping("/user-right")
public class SysUserRightController {

    @Autowired
    private SysUserRightCompoService sysUserRightCompoService;

    @ApiOperation(value = "获取当前用户所有权限编号列表")
    @GetMapping("/list")
    public Result<List<Long>> listUserRightId(@LoginUser UserInfo userInfo,
                                              @CommonReqParam ReqParam reqParam) {
        return Result.succeed(sysUserRightCompoService.listUserRightId(userInfo.getUserId(), reqParam.getCorpId()));
    }

    @ApiOperation(value = "获取某用户在某企业中的所有权限编号列表")
    @GetMapping("/list/{userId}/{corpId}")
    public Result<List<Long>> listUserRightId(@PathVariable("userId") Long userId,
                                              @PathVariable("corpId") Long corpId) {
        return Result.succeed(sysUserRightCompoService.listUserRightId(userId, corpId));
    }

    @ApiOperation(value = "查询当前用户某个企业的所有权限(包含范围权限)")
    @PostMapping("/listByApp")
    public Result<List<SysUserFullRightDto>> listByApp(@RequestBody SysRightFilter sysRightFilter,
                                                       @LoginUser UserInfo userInfo) {
        sysRightFilter.setUserId(userInfo.getUserId());
        List<SysUserFullRightDto> list = sysUserRightCompoService.listFullUserRight(sysRightFilter);
        return Result.succeed(list);
    }

    @ApiOperation(value = "远程调用：获得具有某权限的人员列表")
    @GetMapping(value = "/feign/listUserByRightId")
    public Result<List<Long>> listUserByRightIdFeign(@RequestParam("corpId") Long corpId,
                                                     @RequestParam("rightId") Long rightId) {
        return Result.succeed(sysUserRightCompoService.listUserByRightId(rightId, corpId));
    }

    @ApiOperation(value = "远程调用：获得具有某权限的人员列表，排除系统管理员用户")
    @GetMapping(value = "/feign/listUserByRightIdNoSysUser")
    public Result<List<Long>> listUserByRightIdNoSysUserFeign(@RequestParam("corpId") Long corpId,
                                                              @RequestParam("rightId") Long rightId) {
        return Result.succeed(sysUserRightCompoService.listUserByRightIdNoSysUser(rightId, corpId));
    }

    @ApiOperation(value = "远程调用：获得人员的权限列表")
    @GetMapping(value = "/feign/list/{userId}/{corpId}")
    public Result<List<Long>> listUserRightIdFeign(@PathVariable("userId") Long userId,
                                                   @PathVariable("corpId") Long corpId) {
        return Result.succeed(sysUserRightCompoService.listUserRightId(userId, corpId));
    }
}
