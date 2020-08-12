package com.zjft.usp.uas.right.controller;


import com.zjft.usp.common.model.Result;
import com.zjft.usp.uas.right.composite.SysRoleRightScopeCompoService;
import com.zjft.usp.uas.right.dto.SysRoleRightScopeDto;
import com.zjft.usp.uas.right.model.SysRoleRightScope;
import com.zjft.usp.uas.right.service.SysRoleRightScopeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色范围权限表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2020-03-12
 */
@Api(tags = "角色范围权限")
@RestController
@RequestMapping("/sys-role-right-scope")
public class SysRoleRightScopeController {

    @Autowired
    private SysRoleRightScopeService sysRoleRightScopeService;
    @Autowired
    private SysRoleRightScopeCompoService sysRoleRightScopeCompoService;

    @ApiOperation(value = "角色范围权限列表")
    @GetMapping("/list/{roleId}")
    public Result<List<SysRoleRightScopeDto>> listRoleRightScope(@PathVariable("roleId") Long roleId) {
        return Result.succeed(sysRoleRightScopeCompoService.listRoleRightScope(roleId));
    }

    @ApiOperation(value = "添加角色范围权限")
    @PostMapping("/add")
    public Result addRoleRightScope(@RequestBody SysRoleRightScope sysRoleRightScope) {
        sysRoleRightScopeService.addRoleRightScope(sysRoleRightScope);
        return Result.succeed();
    }

    @ApiOperation(value = "远程调用：根据角色编号列表获得对应权限编号列表")
    @PostMapping("/feign/right/list")
    public Result<List<Long>> listRightIdByRoleIdList(@RequestBody List<Long> roleIdList) {
        return Result.succeed(sysRoleRightScopeService.listRightIdByRoleIdList(roleIdList));
    }
}
