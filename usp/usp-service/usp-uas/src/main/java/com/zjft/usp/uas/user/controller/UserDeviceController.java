package com.zjft.usp.uas.user.controller;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.user.service.UserDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户设备控制层
 *
 * @author zgpi
 * @version 1.0
 * @date 2019-11-28 10:38
 **/
@Api(tags = "用户设备")
@RestController
@RequestMapping("/user-device")
public class UserDeviceController {

    @Autowired
    private UserDeviceService userDeviceService;

    @ApiOperation(value = "保存用户设备信息")
    @PostMapping("/add")
    public Result addUserDevice(@RequestParam("userId") Long userId,
                                @CommonReqParam ReqParam reqParam) {
        userDeviceService.addUserDeviceInfo(reqParam, userId);
        return Result.succeed();
    }
}
