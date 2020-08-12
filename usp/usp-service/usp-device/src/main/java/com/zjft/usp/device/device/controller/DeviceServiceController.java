package com.zjft.usp.device.device.controller;

import cn.hutool.core.lang.Assert;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.device.device.dto.DeviceServiceDto;
import com.zjft.usp.device.device.service.DeviceServiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 设备服务信息
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-23 15:16
 **/
@Api(tags = "设备服务信息")
@RestController
@RequestMapping("/device-service")
public class DeviceServiceController {

    @Autowired
    private DeviceServiceService deviceServiceService;

    @ApiOperation(value = "更新设备服务信息")
    @PostMapping(value = "/update")
    public Result update(@RequestBody DeviceServiceDto deviceServiceDto, @LoginUser UserInfo userInfo) {
        this.deviceServiceService.update(deviceServiceDto, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation(value = "删除设备服务信息")
    @DeleteMapping(value = "/{deviceId}")
    public Result deleteById(@PathVariable("deviceId") Long deviceId, @LoginUser UserInfo userInfo) {
        Assert.notNull(deviceId, "主键不能为空！");
        this.deviceServiceService.removeById(deviceId);
        return Result.succeed();
    }


    @ApiOperation("远程调用：根据用户ID和企业ID删除设备服务信息")
    @PostMapping(value = "/feign/deleteDeviceService")
    public Result deleteBranchUser(@RequestParam("userId") Long userId,
                                   @RequestParam("corpId") Long corpId,
                                   @RequestParam("currentUserId") Long currentUserId,
                                   @RequestParam("clientId") String clientId) {
        this.deviceServiceService.delDeviceServiceByCorp(userId, corpId, currentUserId, clientId);
        return Result.succeed();
    }

}
