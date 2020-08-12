package com.zjft.usp.uas.right.controller;

import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.uas.right.composite.RightRedisCompoService;
import com.zjft.usp.uas.right.composite.SysTenantCompoService;
import com.zjft.usp.uas.right.dto.SysTenantDto;
import com.zjft.usp.uas.right.filter.SysTenantFilter;
import com.zjft.usp.uas.right.model.SysTenant;
import com.zjft.usp.uas.right.service.SysTenantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 租户设置表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
@Api(tags = "租户设置")
@RestController
@RequestMapping("/sys-tenant")
public class SysTenantController {

    @Autowired
    private SysTenantService sysTenantService;
    @Autowired
    private SysTenantCompoService sysTenantCompoService;
    @Autowired
    private RightRedisCompoService rightRedisCompoService;

    @ApiOperation(value = "分页查询系统租户")
    @PostMapping(value = "/query")
    public Result<ListWrapper<SysTenantDto>> query(@RequestBody SysTenantFilter sysTenantFilter) {
        return Result.succeed(sysTenantService.query(sysTenantFilter));
    }

    @ApiOperation(value = "查询系统租户列表")
    @PostMapping(value = "/list")
    public Result<List<SysTenantDto>> list(@RequestBody SysTenantFilter sysTenantFilter) {
        return Result.succeed(sysTenantService.list(sysTenantFilter));
    }

    @ApiOperation(value = "添加系统租户")
    @PostMapping(value = "/add")
    public Result addSysTenant(@RequestBody SysTenant sysTenant,
                               @LoginUser UserInfo userInfo) {
        sysTenantService.addSysTenant(sysTenant, userInfo);
        sysTenantCompoService.addSysTenantRedis(sysTenant.getCorpId());
        // 异步初始化
        rightRedisCompoService.initTenantRightRedis();
        return Result.succeed();
    }

    @ApiOperation(value = "修改系统租户")
    @PostMapping(value = "/update")
    public Result updateSysTenant(@RequestBody SysTenant sysTenant,
                                  @LoginUser UserInfo userInfo) {
        sysTenantService.updateSysTenant(sysTenant, userInfo);
        sysTenantCompoService.updateSysTenantRedis(sysTenant.getCorpId());
        // 异步初始化
        rightRedisCompoService.initTenantRightRedis();
        return Result.succeed();
    }

    @ApiOperation(value = "删除系统租户")
    @DeleteMapping(value = "/{corpId}")
    public Result delSysTenant(@PathVariable("corpId") Long corpId) {
        sysTenantService.delSysTenant(corpId);
        sysTenantCompoService.delSysTenantRedis(corpId);
        return Result.succeed();
    }

    @ApiOperation(value = "获得系统租户")
    @GetMapping(value = "/{corpId}")
    public Result<SysTenant> findSysTenant(@PathVariable("corpId") Long corpId) {
        return Result.succeed(sysTenantService.getById(corpId));
    }

    @ApiOperation(value = "根据idList获得系统租户")
    @PostMapping(value = "/listByIds")
    public Result<Collection<SysTenant>> listByIds(@RequestBody SysTenantFilter sysTenantFilter) {
        return Result.succeed(sysTenantService.listByIds(sysTenantFilter.getExcludeCorpIdList()));
    }

    @ApiOperation(value = "初始化租户类型Redis")
    @PostMapping(value = "/redis/init")
    public Result initSysTenantRedis() {
        sysTenantCompoService.initSysTenantRedis();
        return Result.succeed();
    }

    @ApiOperation(value = "初始化租户权限Redis")
    @PostMapping(value = "/right/redis/init")
    public Result initTenantRightRedis() {
        sysTenantCompoService.initTenantRightRedis();
        return Result.succeed();
    }

    @ApiOperation(value = "远程调用：获得系统租户")
    @GetMapping(value = "/feign/{corpId}")
    public Result<SysTenant> findSysTenantFeign(@PathVariable("corpId") Long corpId) {
        return Result.succeed(sysTenantService.getById(corpId));
    }

    @ApiOperation(value = "远程调用：添加系统租户")
    @PostMapping(value = "/feign/addSysTenantDemander")
    public Result setDemander(@RequestParam("userId") Long userId,
                              @RequestParam("corpId") Long corpId) {
        sysTenantService.setDemander(userId, corpId);
        // 异步初始化
        rightRedisCompoService.initSysTenantRedis();
        rightRedisCompoService.initTenantRightRedis();
        return Result.succeed();
    }
}
