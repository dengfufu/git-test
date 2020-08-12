package com.zjft.usp.device.device.controller;


import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.device.baseinfo.dto.DeviceBrandDto;
import com.zjft.usp.device.baseinfo.dto.DeviceLargeClassDto;
import com.zjft.usp.device.device.dto.DeviceInfoDto;
import com.zjft.usp.device.device.filter.DeviceInfoFilter;
import com.zjft.usp.device.device.service.DeviceInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 设备档案基本表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Api(tags = "设备档案")
@RestController
@RequestMapping("/device-info")
public class DeviceInfoController {

    @Autowired
    private DeviceInfoService deviceInfoService;

    @ApiOperation(value = "分页查询设备档案")
    @PostMapping(value = "/query")
    public Result<ListWrapper<DeviceInfoDto>> queryDeviceInfo(@RequestBody DeviceInfoFilter deviceInfoFilter,
                                                              @LoginUser UserInfo userInfo,
                                                              @CommonReqParam ReqParam reqParam) {
        ListWrapper<DeviceInfoDto> list = deviceInfoService.queryDeviceInfo(deviceInfoFilter, userInfo, reqParam);
        return Result.succeed(list);
    }

    @ApiOperation(value = "获得单个设备档案")
    @GetMapping(value = "/{deviceId}")
    public Result<DeviceInfoDto> findDeviceInfo(@PathVariable("deviceId") Long deviceId) {
        DeviceInfoDto deviceInfoDto = deviceInfoService.getByDeviceId(deviceId);
        return Result.succeed(deviceInfoDto);
    }

    @ApiOperation(value = "添加设备档案")
    @PostMapping(value = "/add")
    public Result<Long> addDeviceInfo(@RequestBody DeviceInfoDto deviceInfoDto,
                                      @LoginUser UserInfo userInfo,
                                      @CommonReqParam ReqParam reqParam) {
        Long deviceId = deviceInfoService.addDeviceInfo(deviceInfoDto, userInfo, reqParam);
        return Result.succeed(deviceId);
    }

    @ApiOperation(value = "修改设备档案")
    @PostMapping(value = "/update")
    public Result<String> updateDeviceInfo(@RequestBody DeviceInfoDto deviceInfoDto,
                                           @LoginUser UserInfo userInfo,
                                           @CommonReqParam ReqParam reqParam) {
        deviceInfoService.updateDeviceInfo(deviceInfoDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "删除设备档案")
    @DeleteMapping(value = "/{deviceId}")
    public Result<String> deleteDeviceInfo(@PathVariable("deviceId") Long deviceId) {
        deviceInfoService.deleteDeviceInfo(deviceId);
        return Result.succeed();
    }

    @ApiOperation(value = "根据设备信息条件获得设备档案详情")
    @PostMapping(value = "/findByWhere")
    public Result<DeviceInfoDto> findDeviceInfoDto(@RequestBody DeviceInfoFilter deviceInfoFilter) {
        return Result.succeed(deviceInfoService.findDeviceInfoDto(deviceInfoFilter));
    }

    @ApiOperation(value = "根据设备信息条件获得设备分类列表")
    @PostMapping(value = "/deviceClass/list")
    public Result<List<DeviceLargeClassDto>> listDeviceClass(@RequestBody DeviceInfoFilter deviceInfoFilter) {
        List<DeviceLargeClassDto> list = deviceInfoService.listDeviceClass(deviceInfoFilter);
        return Result.succeed(list);
    }

    @ApiOperation(value = "根据设备信息条件获得设备分类列表")
    @PostMapping(value = "/deviceModel/list")
    public Result<List<DeviceBrandDto>> listDeviceModel(@RequestBody DeviceInfoFilter deviceInfoFilter) {
        List<DeviceBrandDto> list = deviceInfoService.listDeviceModel(deviceInfoFilter);
        return Result.succeed(list);
    }

    @ApiOperation(value = "远程调用：获得单个设备档案")
    @GetMapping(value = "/feign/{deviceId}")
    public Result<DeviceInfoDto> findByIdFeign(@PathVariable("deviceId") Long deviceId) {
        DeviceInfoDto deviceInfoDto = deviceInfoService.getByDeviceId(deviceId);
        return Result.succeed(deviceInfoDto);
    }

    @ApiOperation(value = "远程调用：添加/修改设备档案")
    @PostMapping(value = "/feign/edit")
    public Result<DeviceInfoDto> editDeviceInfo(@RequestBody DeviceInfoDto deviceInfoDto) {
        DeviceInfoDto entity = deviceInfoService.editDeviceInfo(deviceInfoDto);
        return Result.succeed(entity);
    }

    @ApiOperation(value = "远程调用：根据出厂序列号、设备编号、设备型号获得设备档案")
    @PostMapping(value = "/feign/find")
    public Result<DeviceInfoDto> findDeviceInfoBy(@RequestBody DeviceInfoFilter deviceInfoFilter) {
        return Result.succeed(deviceInfoService.findDeviceInfoBy(deviceInfoFilter));
    }

    @ApiOperation(value = "远程调用：根据出厂序列号、设备编号、设备型号获得设备档案列表")
    @PostMapping(value = "/feign/findList")
    public Result<List<DeviceInfoDto>> findDeviceInfoListBy(@RequestBody DeviceInfoFilter deviceInfoFilter) {
        return Result.succeed(deviceInfoService.findDeviceInfoListBy(deviceInfoFilter));
    }

    @ApiOperation(value = "远程调用：根据设备信息条件获取到负责工程师列表")
    @PostMapping(value = "/feign/findDeviceEngineers")
    public Result<List<Long>> findDeviceEngineers(@RequestParam("deviceCode") String deviceCode,
                                                  @RequestParam("smallClassId") Long smallClassId,
                                                  @RequestParam("brandId") Long brandId,
                                                  @RequestParam("modelId") Long modelId,
                                                  @RequestParam("serial") String serial,
                                                  @RequestParam("demanderCorp") Long demanderCorp,
                                                  @RequestParam("serviceCorp") Long serviceCorp) {
        List<Long> engineers = deviceInfoService.findDeviceEngineers(deviceCode, smallClassId, brandId, modelId, serial, demanderCorp, serviceCorp);
        return Result.succeed(engineers);
    }

}
