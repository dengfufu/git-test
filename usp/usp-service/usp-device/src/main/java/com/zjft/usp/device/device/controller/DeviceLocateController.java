package com.zjft.usp.device.device.controller;

import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.device.device.dto.DeviceLocateDto;
import com.zjft.usp.device.device.service.DeviceLocateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 设备位置信息
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-23 15:01
 **/
@Api(tags = "设备位置信息")
@RestController
@RequestMapping("/device-locate")
public class DeviceLocateController {

    @Autowired
    private DeviceLocateService deviceLocateService;

    @ApiOperation(value = "更新设备位置信息")
    @PostMapping(value = "/update")
    public Result update(@RequestBody DeviceLocateDto deviceLocateDto, @LoginUser UserInfo userInfo) {
        this.deviceLocateService.update(deviceLocateDto, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation(value = "清除位置信息，但是保留客户信息")
    @DeleteMapping(value = "/{deviceId}")
    public Result clearLocate(@PathVariable Long deviceId, @LoginUser UserInfo userInfo) {
        this.deviceLocateService.clearLocate(deviceId, userInfo.getUserId());
        return Result.succeed();
    }

}
