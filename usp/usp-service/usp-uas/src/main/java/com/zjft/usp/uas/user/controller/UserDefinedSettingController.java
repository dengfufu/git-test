package com.zjft.usp.uas.user.controller;

import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.uas.user.dto.UserDefinedSettingDto;
import com.zjft.usp.uas.user.service.UserDefinedSettingService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户自定义配置 前端控制器
 * </p>
 *
 * @author minji
 * @since 2020-05-13
 */
@RestController
@RequestMapping("/user-defined-setting")
public class UserDefinedSettingController {

    @Autowired
    UserDefinedSettingService userDefinedSettingService;

    @ApiOperation(value = "获取用户所有自定义设置")
    @GetMapping(value = "/list")
    public Result<List<UserDefinedSettingDto>> listUserAddr(@LoginUser UserInfo userInfo) {
        List<UserDefinedSettingDto> list = userDefinedSettingService.listUserDefinedSetting(userInfo.getUserId());
        return Result.succeed(list);
    }

    @ApiOperation(value = "合并用户所有自定义设置")
    @PostMapping(value = "/merge")
    public Result merge(@RequestBody UserDefinedSettingDto userDefinedSettingDto,
        @LoginUser UserInfo userInfo) {
        userDefinedSettingDto.setUserid(userInfo.getUserId());
        userDefinedSettingService.merge(userDefinedSettingDto);
        return Result.succeed();
    }

}
