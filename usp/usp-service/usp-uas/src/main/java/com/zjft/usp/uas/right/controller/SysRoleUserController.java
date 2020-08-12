package com.zjft.usp.uas.right.controller;

import com.zjft.usp.common.model.Result;
import com.zjft.usp.uas.right.composite.SysRoleCompoService;
import com.zjft.usp.uas.right.composite.SysRoleUserCompoService;
import com.zjft.usp.uas.right.filter.SysRoleUserFilter;
import com.zjft.usp.uas.right.model.SysRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色人员表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
@Api(tags = "角色人员")
@RestController
@RequestMapping("/sys-role-user")
public class SysRoleUserController {

    @Autowired
    private SysRoleUserCompoService sysRoleUserCompoService;
    @Autowired
    private SysRoleCompoService sysRoleCompoService;

    @ApiOperation(value = "远程调用：初始化用户所有角色的redis")
    @PostMapping(value = "/feign/redis-init/all")
    public Result initUserRoleRedis(@RequestParam("userId") Long userId) {
        sysRoleUserCompoService.initUserRoleRedis(userId);
        return Result.succeed();
    }

    @ApiOperation(value = "远程调用：初始化用户企业角色的redis")
    @PostMapping(value = "/feign/redis-init/corp-role")
    public Result<List<Long>> initUserCorpRoleRedis(@RequestParam("userId") Long userId,
                                                    @RequestParam("corpId") Long corpId) {
        return Result.succeed(sysRoleUserCompoService.initUserCorpRoleRedis(userId, corpId));
    }

    @ApiOperation(value = "远程调用：修改角色人员")
    @PostMapping(value = "/feign/update")
    public Result updateRoleUser(@RequestParam("userId") Long userId,
                                 @RequestParam("corpId") Long corpId,
                                 @RequestParam("roleIdList") List<Long> roleIdList) {
        sysRoleUserCompoService.updateRoleUser(userId, corpId, roleIdList);
        return Result.succeed();
    }

    @ApiOperation(value = "远程调用：删除角色人员")
    @PostMapping(value = "/feign/delete")
    public Result delRoleUser(@RequestParam("userId") Long userId,
                              @RequestParam("corpId") Long corpId) {
        sysRoleUserCompoService.delRoleUser(userId, corpId);
        return Result.succeed();
    }

    @ApiOperation(value = "远程调用：获得用户角色列表")
    @GetMapping(value = "/feign/list/user/{userId}")
    public Result<List<SysRole>> listUserRole(@PathVariable("userId") Long userId) {
        List<SysRole> sysRoleList = sysRoleCompoService.listUserRole(userId);
        return Result.succeed(sysRoleList);
    }

    @ApiOperation(value = "远程调用：获得人员与角色列表映射")
    @GetMapping(value = "/feign/map/{corpId}")
    public Result mapUserIdAndRoleList(@PathVariable("corpId") Long corpId) {
        return Result.succeed(sysRoleCompoService.mapUserIdAndRoleList(corpId, null));
    }

    @ApiOperation(value = "远程调用：获得人员与角色名称映射")
    @PostMapping(value = "/feign/map/users")
    public Result mapUserIdAndRoleNames(@RequestBody SysRoleUserFilter sysRoleUserFilter) {
        return Result.succeed(sysRoleCompoService.mapUserIdAndRoleNames(sysRoleUserFilter.getCorpId(),
                sysRoleUserFilter.getUserIdList()));
    }
}
