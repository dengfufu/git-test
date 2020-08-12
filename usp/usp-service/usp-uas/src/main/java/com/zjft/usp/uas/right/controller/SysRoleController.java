package com.zjft.usp.uas.right.controller;


import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.right.composite.RightRedisCompoService;
import com.zjft.usp.uas.right.composite.SysRoleCompoService;
import com.zjft.usp.uas.right.dto.SysRoleDto;
import com.zjft.usp.uas.right.filter.SysRoleFilter;
import com.zjft.usp.uas.right.model.SysRole;
import com.zjft.usp.uas.right.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
@Api(tags = "系统角色")
@RestController
@RequestMapping("/sys-role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRoleCompoService sysRoleCompoService;
    @Autowired
    private RightRedisCompoService rightRedisCompoService;

    @ApiOperation(value = "分页查询角色")
    @PostMapping(value = "/query")
    public Result<ListWrapper<SysRoleDto>> query(@RequestBody SysRoleFilter sysRoleFilter,
                                                 @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(sysRoleFilter.getCorpId())) {
            sysRoleFilter.setCorpId(reqParam.getCorpId());
        }
        return Result.succeed(sysRoleCompoService.query(sysRoleFilter, reqParam));
    }

    @ApiOperation(value = "根据id获取角色")
    @GetMapping(value = "/{roleId}")
    public Result<SysRoleDto> findByRoleId(@PathVariable("roleId") Long roleId) {
        return Result.succeed(sysRoleCompoService.findByRoleId(roleId));
    }

    @ApiOperation(value = "添加角色")
    @PostMapping(value = "/add")
    public Result add(@RequestBody SysRoleDto sysRoleDto,
                      @LoginUser UserInfo userInfo,
                      @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(sysRoleDto.getCorpId())) {
            sysRoleDto.setCorpId(reqParam.getCorpId());
        }
        sysRoleCompoService.addRole(sysRoleDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "修改角色")
    @PostMapping(value = "/update")
    public Result update(@RequestBody SysRoleDto sysRoleDto,
                         @LoginUser UserInfo userInfo,
                         @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(sysRoleDto.getCorpId())) {
            sysRoleDto.setCorpId(reqParam.getCorpId());
        }
        SysRole sysRole = sysRoleCompoService.updateRole(sysRoleDto, userInfo, reqParam);
        // 异步删除redis中角色用户的范围权限key
        rightRedisCompoService.delRoleUserScopeRightKey(sysRole.getCorpId(), sysRole.getRoleId());
        return Result.succeed();
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping(value = "/{roleId}")
    public Result delete(@PathVariable("roleId") Long roleId) {
        sysRoleCompoService.delRole(roleId);
        return Result.succeed();
    }

    @ApiOperation(value = "根据条件查询角色列表")
    @GetMapping(value = "/list")
    public Result<List<SysRole>> list(@RequestBody SysRoleFilter sysRoleFilter,
                                      @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(sysRoleFilter.getCorpId())) {
            sysRoleFilter.setCorpId(reqParam.getCorpId());
        }
        return Result.succeed(sysRoleService.list(sysRoleFilter));
    }

    @ApiOperation(value = "模糊查询角色")
    @PostMapping(value = "/match")
    public Result<List<SysRole>> matchRole(@RequestBody SysRoleFilter sysRoleFilter) {
        return Result.succeed(sysRoleService.matchRole(sysRoleFilter));
    }

    @ApiOperation("远程调用：获得用户的角色列表")
    @GetMapping(value = "/list/user/{userId}")
    public Result<List<SysRole>> listUserRole(@PathVariable("userId") Long userId) {
        return Result.succeed(sysRoleCompoService.listUserRole(userId));
    }

    @ApiOperation(value = "远程调用：添加系统角色")
    @PostMapping(value = "/feign/sys/add")
    public Result addSysRole(@RequestParam("corpId") Long corpId,
                             @RequestParam("userId") Long userId) {
        sysRoleCompoService.addSysRole(corpId, userId);
        return Result.succeed();
    }

    @ApiOperation(value = "设置系统管理员")
    @PostMapping(value = "/set/admin")
    public Result setAdmin(@RequestBody SysRoleDto sysRoleDto) {
        int code = sysRoleCompoService.setAdmin(sysRoleDto);
        return Result.succeed(code, "设置成功");
    }
}
