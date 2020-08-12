package com.zjft.usp.uas.right.controller;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.right.composite.SysRightCompoService;
import com.zjft.usp.uas.right.dto.SysRightDto;
import com.zjft.usp.uas.right.dto.SysRightTreeDto;
import com.zjft.usp.uas.right.filter.SysRightFilter;
import com.zjft.usp.uas.right.model.SysRight;
import com.zjft.usp.uas.right.service.SysRightService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
@Api(tags = "系统权限")
@RestController
@RequestMapping("/sys-right")
public class SysRightController {

    @Autowired
    private SysRightService sysRightService;
    @Autowired
    private SysRightCompoService sysRightCompoService;

    @ApiOperation(value = "分页查询系统权限")
    @PostMapping(value = "/query")
    public Result<ListWrapper<SysRight>> query(@RequestBody SysRightFilter sysRightFilter) {
        return Result.succeed(sysRightService.query(sysRightFilter));
    }

    @ApiOperation(value = "模糊查询系统权限")
    @PostMapping(value = "/match")
    public Result<List<SysRight>> matchSysRight(@RequestBody SysRightFilter sysRightFilter) {
        return Result.succeed(sysRightService.matchSysRight(sysRightFilter));
    }

    @ApiOperation(value = "添加系统权限")
    @PostMapping(value = "/add")
    public Result addSysRight(@RequestBody SysRightDto sysRightDto) {
        sysRightCompoService.addSysRight(sysRightDto);
        return Result.succeed();
    }

    @ApiOperation(value = "修改系统权限")
    @PostMapping(value = "/update")
    public Result updateSysRight(@RequestBody SysRightDto sysRightDto) {
        sysRightCompoService.updateSysRight(sysRightDto);
        return Result.succeed();
    }

    @ApiOperation(value = "删除系统权限")
    @DeleteMapping(value = "/{rightId}")
    public Result delSysRight(@PathVariable("rightId") Long rightId) {
        sysRightCompoService.delSysRight(rightId);
        return Result.succeed();
    }

    @ApiOperation(value = "获得最大权限编号")
    @GetMapping(value = "/id/max")
    public Result<Long> findMaxRightId() {
        return Result.succeed(sysRightService.findMaxRightId());
    }

    @ApiOperation(value = "查询系统权限列表")
    @PostMapping(value = "/list")
    public Result<List<SysRight>> listByCorpId(@RequestBody SysRightFilter sysRightFilter,
                                               @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(sysRightFilter.getCorpId())) {
            sysRightFilter.setCorpId(reqParam.getCorpId());
        }
        return Result.succeed(sysRightService.listByCorpId(sysRightFilter));
    }

    @ApiOperation(value = "查询系统权限树")
    @PostMapping(value = "/tree")
    public Result<List<SysRightTreeDto>> listSysRightTree(@RequestBody SysRightFilter sysRightFilter) {
        return Result.succeed(sysRightService.listSysRightTree(sysRightFilter));
    }

    @ApiOperation(value = "查询系统权限树", notes = "授权时使用")
    @PostMapping("/auth/tree")
    public Result<List<SysRightTreeDto>> listSysAuthRightTree(@RequestBody SysRightFilter sysRightFilter,
                                                              @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(sysRightFilter.getCorpId())) {
            sysRightFilter.setCorpId(reqParam.getCorpId());
        }
        return Result.succeed(sysRightService.listSysAuthRightTree(sysRightFilter));
    }

    @ApiOperation(value = "初始化系统公共权限")
    @PostMapping("/common-right/redis/init")
    public Result initSysCommonRightRedis() {
        sysRightCompoService.initSysCommonRightRedis();
        return Result.succeed();
    }
}
