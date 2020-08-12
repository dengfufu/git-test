package com.zjft.usp.zj.device.atm.controller;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.device.atm.composite.DeviceInfoCompoService;
import com.zjft.usp.zj.device.atm.dto.*;
import com.zjft.usp.zj.device.atm.filter.DeviceFilter;
import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.CaseDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.partreplace.PartReplaceDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * ATM设备信息 前端控制器
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-13 17:10
 **/
@Api(tags = "ATM设备信息请求")
@RestController
@RequestMapping("/zj/atm-device")
public class DeviceInfoController {
    @Resource
    private DeviceInfoCompoService deviceInfoCompoService;

    @ApiOperation(value = "查询设备列表")
    @PostMapping("/queryDevice")
    public Result<ListWrapper<DeviceDto>> queryDevice(@RequestBody DeviceFilter deviceFilter,
            @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        return Result.succeed(deviceInfoCompoService.queryDevice(deviceFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "获得设备详情")
    @PostMapping("/findDevice")
    public Result<DeviceDto> findDeviceDetail(@RequestBody DeviceFilter deviceFilter,
            @LoginUser UserInfo userInfo,
            @CommonReqParam ReqParam reqParam) {
        return Result.succeed(deviceInfoCompoService.findDeviceDetail(deviceFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "获得设备档案")
    @PostMapping("/findDeviceArchive")
    public Result<DeviceDto> findDeviceArchive(@RequestBody DeviceFilter deviceFilter,
            @LoginUser UserInfo userInfo,
            @CommonReqParam ReqParam reqParam) {
        return Result.succeed(deviceInfoCompoService.findDeviceArchive(deviceFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "查询安装记录")
    @PostMapping("/listInstallRecord")
    public Result<ListWrapper<InstallRecordDto>> listInstallRecord(@RequestBody DeviceFilter deviceFilter,
            @LoginUser UserInfo userInfo,
            @CommonReqParam ReqParam reqParam) {
        return Result.succeed(deviceInfoCompoService.listInstallRecord(deviceFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "查询升级记录")
    @PostMapping("/listUpdateRecord")
    public Result<ListWrapper<UpdateRecordDto>> listUpdateRecord(@RequestBody DeviceFilter deviceFilter,
            @LoginUser UserInfo userInfo,
            @CommonReqParam ReqParam reqParam) {
        return Result.succeed(deviceInfoCompoService.listUpdateRecord(deviceFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "查询维护PM记录")
    @PostMapping("/listMaintainPm")
    public Result<ListWrapper<MaintainPmDto>> listMaintainPm(@RequestBody DeviceFilter deviceFilter,
            @LoginUser UserInfo userInfo,
            @CommonReqParam ReqParam reqParam) {
        return Result.succeed(deviceInfoCompoService.listMaintainPm(deviceFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "查看维护详情")
    @PostMapping("/viewMaintainDetail")
    public Result<MaintainDto> viewMaintainDetail(@RequestBody DeviceFilter deviceFilter,
            @LoginUser UserInfo userInfo,
            @CommonReqParam ReqParam reqParam) {
        return Result.succeed(deviceInfoCompoService.viewMaintainDetail(deviceFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "查看PM详情")
    @PostMapping("/viewPmDetail")
    public Result<PmDto> viewPmDetail(@RequestBody DeviceFilter deviceFilter,
            @LoginUser UserInfo userInfo,
            @CommonReqParam ReqParam reqParam) {
        return Result.succeed(deviceInfoCompoService.viewPmDetail(deviceFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "查询维护PM更换备件记录")
    @PostMapping("/listPartReplace")
    public Result<ListWrapper<PartDto>> listPartReplace(@RequestBody DeviceFilter deviceFilter,
            @LoginUser UserInfo userInfo,
            @CommonReqParam ReqParam reqParam) {
        return Result.succeed(deviceInfoCompoService.listPartReplace(deviceFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "查找机器最近CASE情况")
    @PostMapping("/listRecentCase")
    public Result<ListWrapper<CaseDto>> listRecentCase(@RequestBody DeviceFilter deviceFilter,
            @LoginUser UserInfo userInfo,
            @CommonReqParam ReqParam reqParam) {
        return Result.succeed(deviceInfoCompoService.listRecentCase(deviceFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "查找机器最近更换备件情况")
    @PostMapping("/listRecentPartReplace")
    public Result<ListWrapper<PartReplaceDto>> listRecentPartReplace(@RequestBody DeviceFilter deviceFilter,
            @LoginUser UserInfo userInfo,
            @CommonReqParam ReqParam reqParam) {
        return Result.succeed(deviceInfoCompoService.listRecentPartReplace(deviceFilter, userInfo, reqParam));
    }

}
