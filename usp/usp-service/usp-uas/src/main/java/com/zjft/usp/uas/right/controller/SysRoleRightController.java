package com.zjft.usp.uas.right.controller;


import com.zjft.usp.common.model.Result;
import com.zjft.usp.uas.right.composite.SysRoleRightCompoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 角色权限表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
@Api(tags = "角色权限")
@RestController
@RequestMapping("/sys-role-right")
public class SysRoleRightController {

    @Autowired
    private SysRoleRightCompoService sysRoleRightCompoService;

    @ApiOperation(value = "初始化角色权限redis")
    @PostMapping(value = "/redis/init")
    public Result initSysRoleRightRedis() {
        sysRoleRightCompoService.initRoleRightRedis();
        return Result.succeed();
    }

    @ApiOperation(value = "初始化用户对应的角色权限redis")
    @PostMapping(value = "/feign/redis-init/{userId}")
    public Result initUserRoleRightRedis(@PathVariable("userId") Long userId) {
        sysRoleRightCompoService.initUserRoleRightRedis(userId);
        return Result.succeed();
    }

    @ApiOperation(value = "初始化角色权限redis")
    @PostMapping(value = "/feign/redis-init/role")
    public Result initRoleRightRedis(@RequestParam("roleId") Long roleId) {
        sysRoleRightCompoService.initRoleRightRedis(roleId);
        return Result.succeed();
    }
}
