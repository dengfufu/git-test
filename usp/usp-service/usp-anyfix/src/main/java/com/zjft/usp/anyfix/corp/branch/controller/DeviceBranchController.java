package com.zjft.usp.anyfix.corp.branch.controller;


import cn.hutool.json.JSONUtil;
import com.zjft.usp.anyfix.corp.branch.dto.DeviceBranchDto;
import com.zjft.usp.anyfix.corp.branch.filter.DeviceBranchFilter;
import com.zjft.usp.anyfix.corp.branch.model.DeviceBranch;
import com.zjft.usp.anyfix.corp.branch.service.DeviceBranchService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
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
 * 设备网点表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2019-09-24
 */
@RestController
@RequestMapping("/device-branch")
@Api(tags = "设备网点")
public class DeviceBranchController {

    @Autowired
    private DeviceBranchService deviceBranchService;

    @ApiOperation(value = "分页查询设备网点")
    @PostMapping("/query")
    public Result<ListWrapper<DeviceBranchDto>> queryDeviceBranch(@RequestBody DeviceBranchFilter deviceBranchFilter,
                                                                  @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(deviceBranchFilter.getCorpId())) {
            deviceBranchFilter.setCorpId(reqParam.getCorpId());
        }
        ListWrapper<DeviceBranchDto> list = deviceBranchService.queryDeviceBranch(deviceBranchFilter);
        return Result.succeed(list);
    }

    @ApiOperation(value = "分页查询客户设备网点列表")
    @PostMapping("/custom/query")
    public Result<ListWrapper<DeviceBranchDto>> queryDeviceBranchByCustom(@RequestBody DeviceBranchFilter deviceBranchFilter) {
        ListWrapper<DeviceBranchDto> list = deviceBranchService.queryDeviceBranchByCustom(deviceBranchFilter);
        return Result.succeed(list);
    }

    @ApiOperation(value = "设备网点列表")
    @PostMapping("/list")
    public Result<List<DeviceBranch>> listDeviceBranch(@RequestBody DeviceBranchFilter deviceBranchFilter,
                                                       @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(deviceBranchFilter.getCustomCorp())) {
            deviceBranchFilter.setCustomCorp(reqParam.getCorpId());
        }
        List<DeviceBranch> list = deviceBranchService.listDeviceBranch(deviceBranchFilter);
        return Result.succeed(list);
    }

    @ApiOperation(value = "添加设备网点")
    @PostMapping("/add")
    public Result add(@RequestBody DeviceBranchDto deviceBranchDto,
                      @LoginUser UserInfo userInfo,
                      @CommonReqParam ReqParam reqParam) {
        deviceBranchService.addDeviceBranch(deviceBranchDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "修改设备网点")
    @PostMapping("/update")
    public Result update(@RequestBody DeviceBranchDto deviceBranchDto,
                         @LoginUser UserInfo userInfo) {
        deviceBranchDto.setOperator(userInfo.getUserId());
        deviceBranchService.updateDeviceBranch(deviceBranchDto);
        return Result.succeed();
    }

    @ApiOperation(value = "获得下级设备网点数量")
    @GetMapping("/count/{upperBranchId}")
    public Result<Integer> countByUpperId(@PathVariable("upperBranchId") Long upperBranchId) {
        return Result.succeed(deviceBranchService.countByUpperId(upperBranchId));
    }

    @ApiOperation(value = "获得设备网点")
    @GetMapping("/{branchId}")
    public Result<DeviceBranchDto> findDtoById(@PathVariable("branchId") Long branchId) {
        return Result.succeed(deviceBranchService.findDtoById(branchId));
    }

    @ApiOperation(value = "删除设备网点")
    @DeleteMapping("/{branchId}")
    public Result delete(@PathVariable("branchId") Long branchId) {
        deviceBranchService.delDeviceBranch(branchId);
        return Result.succeed();
    }

    @ApiOperation("模糊查询客户企业的设备网点")
    @PostMapping(value = "/match")
    public Result<List<DeviceBranchDto>> matchDeviceBranch(@RequestBody DeviceBranchFilter deviceBranchFilter,
                                                           @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(deviceBranchFilter.getCustomId()) &&
                LongUtil.isZero(deviceBranchFilter.getCustomCorp())) {
            deviceBranchFilter.setCustomCorp(reqParam.getCorpId());
        }
        return Result.succeed(this.deviceBranchService.matchDeviceBranch(deviceBranchFilter));
    }

    @ApiOperation("模糊查询相关企业的设备网点")
    @PostMapping(value = "/relate/match")
    public Result<List<DeviceBranchDto>> matchRelateDeviceBranch(@RequestBody DeviceBranchFilter deviceBranchFilter,
                                                                 @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(deviceBranchFilter.getCorpId())) {
            deviceBranchFilter.setCorpId(reqParam.getCorpId());
        }
        return Result.succeed(this.deviceBranchService.matchRelateDeviceBranch(deviceBranchFilter));
    }

    @ApiOperation("模糊查询相关企业的设备网点")
    @PostMapping(value = "/upper-branch")
    public Result<List<DeviceBranch>> getUpperBranchList(@RequestBody DeviceBranchFilter deviceBranchFilter,
                                                                 @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(deviceBranchFilter.getCorpId())) {
            deviceBranchFilter.setCorpId(reqParam.getCorpId());
        }
        return Result.succeed(this.deviceBranchService.selectUpperBranchList(deviceBranchFilter));
    }

    @ApiOperation(value = "远程调用：获得单个设备网点信息")
    @GetMapping("/feign/{branchId}")
    public Result findDtoByIdFeign(@PathVariable("branchId") Long branchId) {
        return Result.succeed(deviceBranchService.findDtoById(branchId));
    }

    @ApiOperation(value = "远程调用：根据客户企业编号列表获取设备网点映射")
    @PostMapping("/feign/mapByCustomIdList")
    public Result<Map<Long, String>> mapByCustomIdList(@RequestParam("customIdList") List<Long> customIdList) {
        Map<Long, String> map = this.deviceBranchService.mapCustomDeviceBranchByCustomIdList(customIdList);
        return Result.succeed(map);
    }

    @ApiOperation(value = "远程调用：查询网带你")
    @PostMapping("/feign/mapIdAndName")
    public Result<Map<Long, String>> mapIdAndNameByIdList(@RequestParam("branchIdList") List<Long> branchIdList) {
        Map<Long, String> map = this.deviceBranchService.mapIdAndNameByIdList(branchIdList);
        return Result.succeed(map);
    }

    @ApiOperation(value = "远程调用：批量增加设备网点")
    @PostMapping("/feign/batchAddDeviceBranch")
    public Result<Map<String,Long>> batchAddDeviceBranch(@RequestBody String json) {
        List<DeviceBranch> deviceBranches = JSONUtil.parseArray(json).toList(DeviceBranch.class);
        Map<String,Long> map = this.deviceBranchService.batchAddDeviceBranch(deviceBranches);
        return Result.succeed(map);
    }
}
