package com.zjft.usp.uas.right.controller;


import com.zjft.usp.common.dto.RightScopeDto;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.uas.right.composite.SysUserRightScopeCompoService;
import com.zjft.usp.uas.right.dto.SysUserDto;
import com.zjft.usp.uas.right.dto.SysUserRightScopeDto;
import com.zjft.usp.uas.right.filter.SysUserScopeRightFilter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 人员范围权限表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2020-06-04
 */
@RestController
@RequestMapping("/sys-user-right-scope")
public class SysUserRightScopeController {

    @Autowired
    private SysUserRightScopeCompoService sysUserRightScopeCompoService;

    @ApiOperation(value = "人员范围权限列表")
    @GetMapping("/list/{userId}/{corpId}")
    public Result<List<SysUserRightScopeDto>> listUserRightScope(@PathVariable("userId") Long userId,
                                                                 @PathVariable("corpId") Long corpId) {
        return Result.succeed(sysUserRightScopeCompoService.listUserRightScope(userId, corpId));
    }

    @ApiOperation(value = "设置人员范围权限")
    @PostMapping("/set")
    public Result setUserRightScope(@RequestBody SysUserDto sysUserDto) {
        sysUserRightScopeCompoService.setUserRightScope(sysUserDto);
        return Result.succeed();
    }

    @ApiOperation(value = "远程调用：删除人员范围权限")
    @PostMapping(value = "/feign/delete")
    public Result delUserRightScope(@RequestBody SysUserScopeRightFilter sysUserScopeRightFilter) {
        sysUserRightScopeCompoService.delUserRightScope(sysUserScopeRightFilter.getUserIdList(),
                sysUserScopeRightFilter.getCorpId());
        return Result.succeed();
    }

    @ApiOperation(value = "远程调用：获得人员范围权限")
    @PostMapping("/feign/list")
    public Result<List<RightScopeDto>> listUserRightScopeFeign(@RequestBody SysUserScopeRightFilter sysUserScopeRightFilter) {
        return Result.succeed(sysUserRightScopeCompoService.listUserRightScope(sysUserScopeRightFilter));
    }
}
