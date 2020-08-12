package com.zjft.usp.uas.right.controller;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.uas.right.composite.SysRightUrlCompoService;
import com.zjft.usp.uas.right.dto.SysRightUrlDto;
import com.zjft.usp.uas.right.filter.SysRightUrlFilter;
import com.zjft.usp.uas.right.model.SysRightUrl;
import com.zjft.usp.uas.right.service.SysRightUrlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 权限映射表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
@Api(tags = "鉴权设置")
@RestController
@RequestMapping("/sys-right-url")
public class SysRightUrlController {

    @Autowired
    private SysRightUrlService sysRightUrlService;
    @Autowired
    private SysRightUrlCompoService sysRightUrlCompoService;

    @ApiOperation(value = "分页查询鉴权设置")
    @PostMapping(value = "/query")
    public Result<ListWrapper<SysRightUrlDto>> query(@RequestBody SysRightUrlFilter sysRightUrlFilter) {
        return Result.succeed(sysRightUrlService.query(sysRightUrlFilter));
    }

    @ApiOperation(value = "添加鉴权设置")
    @PostMapping(value = "/add")
    public Result addSysRightUrl(@RequestBody SysRightUrl sysRightUrl) {
        sysRightUrlCompoService.addSysRightUrl(sysRightUrl);
        return Result.succeed();
    }

    @ApiOperation(value = "修改鉴权设置")
    @PostMapping(value = "/update")
    public Result updateSysRightUrl(@RequestBody SysRightUrl sysRightUrl) {
        sysRightUrlCompoService.updateSysRightUrl(sysRightUrl);
        return Result.succeed();
    }

    @ApiOperation(value = "删除鉴权设置")
    @DeleteMapping(value = "/{id}")
    public Result delSysAuthRight(@PathVariable("id") Long id) {
        sysRightUrlCompoService.delSysRightUrl(id);
        return Result.succeed();
    }

    @ApiOperation(value = "公共权限列表")
    @GetMapping(value = "/list/common")
    public Result<List<SysRightUrl>> listCommon() {
        return Result.succeed(sysRightUrlService.listCommon());
    }

    @ApiOperation(value = "权限列表")
    @GetMapping(value = "/list")
    public Result<List<SysRightUrl>> listAuthRight() {
        return Result.succeed(sysRightUrlService.listAuthRight());
    }


    @ApiOperation(value = "远程调用：权限列表")
    @GetMapping(value = "/feign/list")
    public Result<List<SysRightUrl>> listAuthRightFeign() {
        return Result.succeed(sysRightUrlService.listAuthRight());
    }
}
