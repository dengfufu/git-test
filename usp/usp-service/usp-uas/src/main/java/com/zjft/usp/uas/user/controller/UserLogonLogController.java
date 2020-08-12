package com.zjft.usp.uas.user.controller;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.user.dto.UserLogonLogDto;
import com.zjft.usp.uas.user.filter.UserLogonLogFilter;
import com.zjft.usp.uas.user.service.UserLogonLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户登录日志表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2020-05-25
 */
@Api(tags = "用户登录日志")
@RestController
@RequestMapping("/user-logon-log")
public class UserLogonLogController {

    @Autowired
    private UserLogonLogService userLogonLogService;


    @ApiOperation(value = "根据条件分页查询用户登录日志")
    @PostMapping(value = "/query")
    public Result<ListWrapper<UserLogonLogDto>> queryUserLogonLog(@RequestBody UserLogonLogFilter userLogonLogFilter,
                                                                  @LoginUser UserInfo userInfo,
                                                                  @CommonReqParam ReqParam reqParam) {
        return Result.succeed(userLogonLogService.queryUserLogonLog(userLogonLogFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "远程调用：保存登录成功日志")
    @PostMapping("/feign/success/save")
    public Result saveLogonSuccessLog(@RequestBody UserLogonLogDto userLogonLogDto){
        userLogonLogService.saveLogonSuccessLog(userLogonLogDto);
        return Result.succeed();
    }

    @ApiOperation(value = "远程调用：保存自动登录成功日志")
    @PostMapping("/feign/auto-success/save")
    public Result saveAutoLogonSuccessLog(@RequestBody UserLogonLogDto userLogonLogDto){
        userLogonLogService.saveAutoLogonSuccessLog(userLogonLogDto);
        return Result.succeed();
    }

    @ApiOperation(value = "远程调用：保存密码失败日志")
    @PostMapping("/feign/pwd-fail/save")
    public Result saveLogonPwdFailLog(@RequestBody UserLogonLogDto userLogonLogDto){
        userLogonLogService.saveLogonPwdFailLog(userLogonLogDto);
        return Result.succeed();
    }

    @ApiOperation(value = "远程调用：保存登出日志")
    @PostMapping("/feign/logout/save")
    public Result saveLogoutLog(@RequestBody UserLogonLogDto userLogonLogDto){
        userLogonLogService.saveLogoutLog(userLogonLogDto);
        return Result.succeed();
    }

}
