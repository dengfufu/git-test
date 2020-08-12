package com.zjft.usp.anyfix.corp.user.controller;


import com.zjft.usp.anyfix.corp.user.dto.CorpUserDto;
import com.zjft.usp.anyfix.corp.user.dto.DeviceBranchUserDto;
import com.zjft.usp.anyfix.corp.user.filter.DeviceBranchUserFilter;
import com.zjft.usp.anyfix.corp.user.service.DeviceBranchUserService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 设备网点人员表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2019-09-26
 */
@Api(tags = "设备网点人员")
@RestController
@RequestMapping("/device-branch-user")
public class DeviceBranchUserController {

    @Autowired
    private DeviceBranchUserService deviceBranchUserService;

    @ApiOperation("分页查询人员")
    @PostMapping(value = "/query")
    public Result<ListWrapper<DeviceBranchUserDto>> query(@RequestBody DeviceBranchUserFilter deviceBranchUserFilter,
                                                          @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(deviceBranchUserFilter.getCorpId())) {
            deviceBranchUserFilter.setCorpId(reqParam.getCorpId());
        }
        return Result.succeed(deviceBranchUserService.query(deviceBranchUserFilter));
    }

    @ApiOperation("列表展示可选择的人员")
    @PostMapping(value = "/query/available")
    public Result<ListWrapper<CorpUserDto>> queryAvailable(@RequestBody DeviceBranchUserFilter serviceBranchUserFilter,
                                                           @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(serviceBranchUserFilter.getCorpId())) {
            serviceBranchUserFilter.setCorpId(reqParam.getCorpId());
        }
        return Result.succeed(this.deviceBranchUserService.queryAvailable(serviceBranchUserFilter));
    }

    @ApiOperation("查询可选择的人员")
    @PostMapping(value = "/match/available")
    public Result<List<CorpUserDto>> matchAvailable(@RequestBody DeviceBranchUserFilter serviceBranchUserFilter,
                                                    @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(serviceBranchUserFilter.getCorpId())) {
            serviceBranchUserFilter.setCorpId(reqParam.getCorpId());
        }
        return Result.succeed(this.deviceBranchUserService.matchAvailable(serviceBranchUserFilter));
    }


    @ApiOperation("添加网点人员")
    @PostMapping(value = "/add")
    public Result add(@RequestBody DeviceBranchUserDto deviceBranchUserDto) {
        deviceBranchUserService.addBranchUser(deviceBranchUserDto);
        return Result.succeed();
    }

    @ApiOperation("删除网点人员")
    @DeleteMapping(value = "/{branchId}/{userId}")
    public Result delete(@PathVariable("branchId") Long branchId,
                         @PathVariable("userId") Long userId) {
        deviceBranchUserService.delBranchUser(userId, branchId);
        return Result.succeed();
    }
}
