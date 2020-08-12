package com.zjft.usp.zj.work.baseinfo.controller;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.device.atm.dto.DeviceDto;
import com.zjft.usp.zj.device.atm.filter.DeviceFilter;
import com.zjft.usp.zj.work.baseinfo.composite.BaseInfoCompoService;
import com.zjft.usp.zj.work.baseinfo.dto.*;
import com.zjft.usp.zj.work.baseinfo.filter.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 基础数据 前端控制器
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-13 16:29
 **/
@Api(tags = "老平台基础数据")
@RestController
@RequestMapping("/zj/work-baseinfo")
public class BaseInfoController {

    @Autowired
    private BaseInfoCompoService baseInfoCompoService;

    @ApiOperation(value = "获得CASE基本数据")
    @GetMapping("/findCaseBase")
    public Result<CaseBaseDto> findCaseBase(@LoginUser UserInfo userInfo,
                                            @CommonReqParam ReqParam reqParam) {
        return Result.succeed(baseInfoCompoService.findCaseBase(userInfo, reqParam));
    }

    @ApiOperation(value = "获得CASE查询的基本数据")
    @PostMapping("/findCaseQueryBase")
    public Result<CaseBaseDto> findCaseQueryBase(@RequestParam("serviceBranch") String serviceBranch,
                                                 @LoginUser UserInfo userInfo,
                                                 @CommonReqParam ReqParam reqParam) {
        return Result.succeed(baseInfoCompoService.findCaseQueryBase(serviceBranch, userInfo, reqParam));
    }

    @ApiOperation(value = "获得工程师")
    @PostMapping("/findEngineer")
    public Result<CaseBaseDto> findEngineer(@RequestParam("serviceBranchName") String serviceBranchName,
                                            @LoginUser UserInfo userInfo,
                                            @CommonReqParam ReqParam reqParam) {
        return Result.succeed(baseInfoCompoService.findEngineer(serviceBranchName, userInfo, reqParam));
    }

    @ApiOperation(value = "获得CASE类型列表")
    @GetMapping("/listCaseType")
    public Result<List<String>> listCaseType(@LoginUser UserInfo userInfo,
                                             @CommonReqParam ReqParam reqParam) {
        return Result.succeed(baseInfoCompoService.listCaseType(userInfo, reqParam));
    }

    @ApiOperation(value = "获得CASE子类型列表")
    @PostMapping("/listCaseSubType")
    public Result<Map<Integer, String>> listCaseBase(@RequestParam("workType") String workType,
                                                     @LoginUser UserInfo userInfo,
                                                     @CommonReqParam ReqParam reqParam) {
        return Result.succeed(baseInfoCompoService.listCaseSubType(workType, userInfo, reqParam));
    }

    @ApiOperation(value = "分页查询服务站列表")
    @PostMapping("/queryServiceBranch")
    public Result<ListWrapper<ServiceBranchDto>> queryServiceBranch(@RequestBody ServiceBranchFilter serviceBranchFilter,
                                                                    @LoginUser UserInfo userInfo,
                                                                    @CommonReqParam ReqParam reqParam) {
        return Result.succeed(baseInfoCompoService.queryServiceBranch(serviceBranchFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "分页查询设备网点列表")
    @PostMapping("/queryDeviceBranch")
    public Result<ListWrapper<DeviceBranchDto>> queryDeviceBranch(@RequestBody DeviceBranchFilter deviceBranchFilter,
                                                                  @LoginUser UserInfo userInfo,
                                                                  @CommonReqParam ReqParam reqParam) {
        return Result.succeed(baseInfoCompoService.queryDeviceBranch(deviceBranchFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "分页查询总行列表")
    @PostMapping("/queryHeadBank")
    public Result<ListWrapper<BankDto>> queryHeadBank(@RequestBody BankFilter bankFilter,
                                                      @LoginUser UserInfo userInfo,
                                                      @CommonReqParam ReqParam reqParam) {
        return Result.succeed(baseInfoCompoService.queryHeadBank(bankFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "分页查询分行列表")
    @PostMapping("/querySubBank")
    public Result<ListWrapper<BankDto>> querySubBank(@RequestBody BankFilter bankFilter,
                                                     @LoginUser UserInfo userInfo,
                                                     @CommonReqParam ReqParam reqParam) {
        return Result.succeed(baseInfoCompoService.querySubBank(bankFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "分页查询机器型号列表")
    @PostMapping("/queryDeviceModel")
    public Result<ListWrapper<DeviceModelDto>> queryDeviceModel(@RequestBody DeviceModelFilter deviceModelFilter,
                                                                @LoginUser UserInfo userInfo,
                                                                @CommonReqParam ReqParam reqParam) {
        return Result.succeed(baseInfoCompoService.queryDeviceModel(deviceModelFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "查询机器型号列表")
    @GetMapping("/listDeviceModel")
    public Result<List<DeviceModelDto>> listDeviceModel(@LoginUser UserInfo userInfo,
                                                        @CommonReqParam ReqParam reqParam) {
        return Result.succeed(baseInfoCompoService.listDeviceModel(userInfo, reqParam));
    }

    @ApiOperation(value = "查询机器列表")
    @PostMapping("/listDevice")
    public Result<List<DeviceDto>> listDevice(@RequestBody DeviceFilter deviceFilter,
                                              @LoginUser UserInfo userInfo,
                                              @CommonReqParam ReqParam reqParam) {
        return Result.succeed(baseInfoCompoService.listDevice(deviceFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "获得机器详情")
    @PostMapping("/findDevice")
    public Result<DeviceDto> findDeviceDetail(@RequestBody DeviceFilter deviceFilter,
                                              @LoginUser UserInfo userInfo,
                                              @CommonReqParam ReqParam reqParam) {
        return Result.succeed(baseInfoCompoService.findDeviceDetail(deviceFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "获得保修状态列表")
    @PostMapping("/listWarrantyStatus")
    public Result<List<WarrantyStatusDto>> listWarrantyStatus(@RequestParam("serviceBranchName") String serviceBranchName,
                                                              @LoginUser UserInfo userInfo,
                                                              @CommonReqParam ReqParam reqParam) {
        return Result.succeed(baseInfoCompoService.listWarrantyStatus(serviceBranchName, userInfo, reqParam));
    }

    @ApiOperation(value = "根据制造号查找终端号")
    @PostMapping("/findDeviceCodesBySerials")
    public Result<String> findDeviceCodesBySerials(@RequestParam("modelId") String modelId,
                                                   @RequestParam("serials") String serials,
                                                   @LoginUser UserInfo userInfo,
                                                   @CommonReqParam ReqParam reqParam) {
        return Result.succeedStr(baseInfoCompoService.findDeviceCodesBySerials(modelId, serials,
                userInfo, reqParam));
    }

    @ApiOperation(value = "根据终端号查找制造号")
    @PostMapping("/findSerialsByDeviceCodes")
    public Result<String> findSerialsByDeviceCodes(@RequestParam("deviceCodes") String deviceCodes,
                                                   @LoginUser UserInfo userInfo,
                                                   @CommonReqParam ReqParam reqParam) {
        return Result.succeedStr(baseInfoCompoService.findSerialsByDeviceCodes(deviceCodes,
                userInfo, reqParam));
    }

    @ApiOperation(value = "检查机器状态")
    @PostMapping("/checkDeviceStatus")
    public Result<DeviceCheckDto> checkDeviceStatus(@RequestBody DeviceFilter deviceFilter,
                                                    @LoginUser UserInfo userInfo,
                                                    @CommonReqParam ReqParam reqParam) {
        return Result.succeed(baseInfoCompoService.checkDeviceStatus(deviceFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "分页查询工程师列表")
    @PostMapping("/queryEngineer")
    public Result<ListWrapper<EngineerDto>> queryEngineer(@RequestBody EngineerFilter engineerFilter,
                                                          @LoginUser UserInfo userInfo,
                                                          @CommonReqParam ReqParam reqParam) {
        return Result.succeed(baseInfoCompoService.queryEngineer(engineerFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "交通工具列表")
    @GetMapping("/listTraffic")
    public Result<Map<String, String>> listTraffic(@LoginUser UserInfo userInfo,
                                                   @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.baseInfoCompoService.listTraffic(userInfo, reqParam));
    }

    @ApiOperation(value = "获取软件版本")
    @PostMapping("/listSoftVersion")
    public Result<Map<String, List<SoftVersionDto>>> listSoftVersion(@RequestParam("customId") String customId,
                                                                     @RequestParam("modelId") String modelId,
                                                                     @LoginUser UserInfo userInfo,
                                                                     @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.baseInfoCompoService.listSoftVersion(customId, modelId, userInfo, reqParam));
    }

    @ApiOperation(value = "查询是否已存在CASE")
    @PostMapping("/ifExistCase")
    public Result<Boolean> ifExistCase(@RequestParam("bankCode") String bankCode,
                                       @RequestParam("branchName") String branchName,
                                       @LoginUser UserInfo userInfo,
                                       @CommonReqParam ReqParam reqParam) {
        return Result.succeed(baseInfoCompoService.ifExistCase(bankCode, branchName, userInfo, reqParam));
    }

    @ApiOperation(value = "获取跟单规则")
    @PostMapping("/listTraceRule")
    public Result<List<TraceRuleDto>> listTraceRule(@RequestParam("workTypeName") String workTypeName,
                                                    @RequestParam("traceRequired") String traceRequired,
                                                    @LoginUser UserInfo userInfo,
                                                    @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.baseInfoCompoService.listTraceRule(workTypeName, traceRequired, userInfo, reqParam));
    }

    @ApiOperation(value = "分页查询紫金公司服务主管列表(含400)")
    @PostMapping("/queryManager")
    public Result<ListWrapper<UserDto>> queryManager(@RequestBody UserFilter userFilter,
                                                     @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        return Result.succeed(baseInfoCompoService.queryManager(userFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "获取是否人为损坏映射Map")
    @GetMapping("/listManMade")
    public Result<Map<Integer, String>> listManMade(@LoginUser UserInfo userInfo,
                                                    @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.baseInfoCompoService.listManMade(userInfo, reqParam));
    }

    @ApiOperation(value = "获取巡检处理方式映射Map")
    @GetMapping("/listDealWay")
    public Result<Map<Integer, String>> listDealWay(@LoginUser UserInfo userInfo,
                                                    @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.baseInfoCompoService.listDealWay(userInfo, reqParam));
    }

}
