package com.zjft.usp.anyfix.corp.user.controller;


import com.zjft.usp.anyfix.corp.user.dto.CorpUserDto;
import com.zjft.usp.anyfix.corp.user.dto.ServiceBranchUserDto;
import com.zjft.usp.anyfix.corp.user.filter.ServiceBranchUserFilter;
import com.zjft.usp.anyfix.corp.user.service.ServiceBranchUserService;
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
import java.util.Map;

/**
 * <p>
 * 服务网点人员表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2019-09-26
 */
@Api(tags = "服务网点人员")
@RestController
@RequestMapping("/service-branch-user")
public class ServiceBranchUserController {

    @Autowired
    private ServiceBranchUserService serviceBranchUserService;

    @ApiOperation("分页查询人员")
    @PostMapping(value = "/query")
    public Result<ListWrapper<ServiceBranchUserDto>> query(@RequestBody ServiceBranchUserFilter serviceBranchUserFilter,
                                                           @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(serviceBranchUserFilter.getCorpId())) {
            serviceBranchUserFilter.setCorpId(reqParam.getCorpId());
        }
        return Result.succeed(this.serviceBranchUserService.query(serviceBranchUserFilter));
    }

    @ApiOperation("查询可选择的人员")
    @PostMapping(value = "/match/available")
    public Result<List<CorpUserDto>> matchAvailable(@RequestBody ServiceBranchUserFilter serviceBranchUserFilter,
                                                    @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(serviceBranchUserFilter.getCorpId())) {
            serviceBranchUserFilter.setCorpId(reqParam.getCorpId());
        }
        return Result.succeed(this.serviceBranchUserService.matchAvailable(serviceBranchUserFilter));
    }

    @ApiOperation("列表展示可选择的人员")
    @PostMapping(value = "/query/available")
    public Result<ListWrapper<CorpUserDto>> queryAvailable(@RequestBody ServiceBranchUserFilter serviceBranchUserFilter,
                                                           @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(serviceBranchUserFilter.getCorpId())) {
            serviceBranchUserFilter.setCorpId(reqParam.getCorpId());
        }
        return Result.succeed(this.serviceBranchUserService.queryAvailable(serviceBranchUserFilter));
    }

    @ApiOperation("添加网点人员")
    @PostMapping(value = "/add")
    public Result add(@RequestBody ServiceBranchUserDto serviceBranchUserDto,
                      @CommonReqParam ReqParam reqParam) {
        this.serviceBranchUserService.addBranchUser(serviceBranchUserDto, reqParam);
        return Result.succeed();
    }

    @ApiOperation("删除网点人员")
    @DeleteMapping(value = "/{branchId}/{userId}")
    public Result delete(@PathVariable("branchId") Long branchId,
                         @PathVariable("userId") Long userId,
                         @CommonReqParam ReqParam reqParam) {
        this.serviceBranchUserService.delBranchUser(userId, branchId, reqParam);
        return Result.succeed();
    }

    @ApiOperation("网点人员列表")
    @PostMapping(value = "/list")
    public Result<List<ServiceBranchUserDto>> listByBranchId(@RequestBody ServiceBranchUserFilter serviceBranchUserFilter,
                                                             @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(serviceBranchUserFilter.getCorpId())) {
            serviceBranchUserFilter.setCorpId(reqParam.getCorpId());
        }
        return Result.succeed(serviceBranchUserService.listDtoBy(serviceBranchUserFilter));
    }

    @ApiOperation("模糊查询网点人员")
    @PostMapping(value = "/match")
    public Result<List<CorpUserDto>> matchUser(@RequestBody ServiceBranchUserFilter serviceBranchUserFilter) {
        return Result.succeed(serviceBranchUserService.matchCorpUser(serviceBranchUserFilter));
    }

    @ApiOperation("远程调用：人员编号与服务网点名称(带省份)映射")
    @PostMapping(value = "/feign/mapUserIdAndServiceBranchNames")
    public Result<Map<Long, String>> mapUserIdAndServiceBranchNames(@RequestBody List<Long> userIdList) {
        return Result.succeed(serviceBranchUserService.mapUserIdAndServiceBranchNames(userIdList));
    }

    @ApiOperation("远程调用：根据条件获得人员列表")
    @PostMapping(value = "/feign/user/list")
    public Result<List<Long>> listUserIdByFeign(@RequestBody ServiceBranchUserFilter serviceBranchUserFilter) {
        return Result.succeed(serviceBranchUserService.listUserIdByFeign(serviceBranchUserFilter));
    }

    @ApiOperation("远程调用：查询某个人员的网点名称")
    @GetMapping(value = "/feign/user/branchName/{userId}")
    public Result<String> listUserIdByFeign(@PathVariable("userId") Long userId) {
        return Result.succeed(serviceBranchUserService.getBranchNamesByUserId(userId),null);
    }

}
