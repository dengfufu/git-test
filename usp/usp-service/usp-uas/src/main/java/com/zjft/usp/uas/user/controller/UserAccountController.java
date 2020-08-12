package com.zjft.usp.uas.user.controller;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginClient;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.LoginAppUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.user.composite.UserCompoService;
import com.zjft.usp.uas.user.dto.UserInfoDto;
import com.zjft.usp.uas.user.dto.UserRealDto;
import com.zjft.usp.uas.user.dto.UserRegionDto;
import com.zjft.usp.uas.user.dto.VerifyMessageDto;
import com.zjft.usp.uas.user.filter.UserFilter;
import com.zjft.usp.uas.user.service.UserAccountService;
import com.zjft.usp.uas.user.service.UserInfoService;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author : dcyu
 * @Date : 2019年8月8日18:47:26
 * @Desc : 个人信息数据控制类
 */
@Api(tags = "账户信息")
@RequestMapping("/account")
@RestController
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserCompoService userCompoService;

    @ApiOperation(value = "手机号登录")
    @GetMapping(value = "/user/mobile/logon", params = "mobile")
    public LoginAppUser logonByMobile(String mobile) {
        return userAccountService.logonByMobile(mobile);
    }

    @ApiOperation(value = "登录名登录")
    @GetMapping(value = "/user/logon", params = "logonId")
    public LoginAppUser logonByLogonId(String logonId) {
        return userAccountService.logonByLogonId(logonId);
    }

    @ApiOperation(value = "微信公众号登录,logonId")
    @GetMapping(value = "/user/wx/logon", params = "openId")
    public LoginAppUser logonByWxId(String openId) {
        return userAccountService.logonByWx(openId);
    }

    @ApiOperation(value = "分页查询用户")
    @PostMapping(value = "/query")
    public Result<ListWrapper<UserInfo>> query(@RequestBody UserFilter userFilter) {
        return Result.succeed(userInfoService.query(userFilter));
    }

    @ApiOperation(value = "修改签名")
    @PostMapping(value = "/updateSignature")
    public Result setSignature(@RequestBody Map<String, String> requestMap,
                               @LoginUser UserInfo userInfo,
                               @CommonReqParam ReqParam reqParam,
                               @LoginClient String clientId) {
        String signature = requestMap.get("signature");
        userAccountService.setSignature(signature, userInfo, reqParam, clientId);
        return Result.succeed();
    }

    @ApiOperation(value = "修改邮箱")
    @PostMapping(value = "/updateMail")
    public Result setMailAddress(@RequestBody Map<String, String> requestMap,
                                 @LoginUser UserInfo userInfo,
                                 @CommonReqParam ReqParam reqParam,
                                 @LoginClient String clientId) {
        String emailAddress = requestMap.get("emailAddress");
        userAccountService.setEmailAddress(emailAddress, userInfo, reqParam, clientId);
        return Result.succeed();
    }

    @ApiOperation(value = "获得二维码")
    @GetMapping(value = "/qrcode")
    public Result<String> showQRCode(@LoginUser UserInfo userInfo) {
        long userId = userInfo.getUserId();
        String qrCode = userAccountService.getQRCodeInfo(userId);
        return Result.succeed(qrCode, "");
    }

    @ApiOperation(value = "获得所在地区")
    @GetMapping(value = "/region")
    public Result<UserRegionDto> getUserRegion(@LoginUser UserInfo userInfo) {
        UserRegionDto userRegionDto = userInfoService.getUserRegion(userInfo.getUserId());
        return Result.succeed(userRegionDto);
    }

    @ApiOperation(value = "修改所在地区")
    @PostMapping(value = "/updateRegion")
    public Result<UserRegionDto> setUserRegion(@RequestBody UserRegionDto userRegionDto,
                                               @LoginUser UserInfo userInfo,
                                               @CommonReqParam ReqParam reqParam,
                                               @LoginClient String clientId) {
        UserRegionDto dto = userAccountService.setUserRegion(userRegionDto, userInfo, reqParam, clientId);
        return Result.succeed(dto);
    }

    @ApiOperation(value = "修改昵称")
    @PostMapping(value = "/updateNickname")
    public Result updateNickname(@RequestBody UserInfoDto userInfoDto,
                                 @LoginUser UserInfo userInfo,
                                 @CommonReqParam ReqParam reqParam,
                                 @LoginClient String clientId) {
        userAccountService.updateNickname(userInfoDto, userInfo.getUserId(), reqParam, clientId);
        return Result.succeed();
    }

    @ApiOperation(value = "修改登录名")
    @PostMapping(value = "/updateLoginId")
    public Result updateLoginId(@RequestBody UserInfoDto userInfoDto,
                                @LoginUser UserInfo userInfo,
                                @CommonReqParam ReqParam reqParam,
                                @LoginClient String clientId) {
        userAccountService.updateLoginId(userInfoDto, userInfo.getUserId(), reqParam, clientId);
        return Result.succeed();
    }

    @ApiOperation(value = "修改性别")
    @PostMapping(value = "/updateSex")
    public Result updateSex(@RequestBody UserInfoDto userInfoDto,
                            @LoginUser UserInfo userInfo,
                            @CommonReqParam ReqParam reqParam,
                            @LoginClient String clientId) {
        userAccountService.updateSex(userInfoDto, userInfo.getUserId(), reqParam, clientId);
        return Result.succeed();
    }

    @ApiOperation(value = "修改手机号")
    @PostMapping(value = "/updateMobile")
    public Result updateMobile(@Valid @RequestBody VerifyMessageDto verifyMessageDto,
                               @LoginUser UserInfo userInfo,
                               @CommonReqParam ReqParam reqParam,
                               @LoginClient String clientId) {
        userAccountService.updateMobile(verifyMessageDto, userInfo, reqParam, clientId);
        return Result.succeed();
    }

    @ApiOperation(value = "修改用户信息")
    @PostMapping(value = "/update")
    public Result<String> updateUserInfo(@RequestBody UserInfoDto userInfoDto) {
        userAccountService.updateUserInfo(userInfoDto);
        return Result.succeed();
    }

    @ApiOperation(value = "上传头像")
    @PostMapping(value = "/face")
    public Result uploadFaceImage(@RequestBody UserInfoDto userInfoDto,
                                  @LoginUser UserInfo userInfo) {
        userAccountService.uploadFaceImage(userInfoDto, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation(value = "获得当前登录用户信息")
    @GetMapping(value = "/info")
    public Result<UserInfoDto> getUserInfoDtoById(@LoginUser UserInfo userInfo) {
        return Result.succeed(userAccountService.getUserInfoDtoById(userInfo.getUserId()));
    }

    @ApiOperation(value = "获得当前登录用户信息")
    @GetMapping(value = "/info/{userId}")
    public Result<UserInfoDto> getUserInfoDtoById(@PathVariable("userId") Long userId) {
        return Result.succeed(userAccountService.getUserInfoDtoById(userId));
    }

    @ApiOperation(value = "获得当前登录用户大头像")
    @GetMapping(value = "/face/big")
    public Result<UserInfoDto> queryFaceImgBig(@LoginUser UserInfo userInfo) {
        return Result.succeed(userAccountService.queryFaceImgBig(userInfo));
    }

    @ApiOperation(value = "修改密码")
    @PostMapping(value = "/password/change")
    public Result<Object> changePassword(@RequestBody Map<String, String> param,
                                         @CommonReqParam ReqParam reqParam,
                                         @LoginUser UserInfo user,
                                         @LoginClient String clientId) {
        String oldPassword = param.get("oldPassword");
        String newPassword = param.get("newPassword");
        userAccountService.changePassword(oldPassword, newPassword, reqParam, user.getUserId(), clientId);
        return Result.succeed();
    }

    @ApiOperation(value = "忘记密码-通过短信模式修改")
    @PostMapping(value = "/password/changeBySms")
    public Result<Object> changePasswordBySms(@RequestBody Map<String, String> param,
                                              @CommonReqParam ReqParam reqParam,
                                              @LoginClient String clientId) {
        String mobile = param.get("mobile");
        String smsCode = param.get("smsCode");
        String newPassword = param.get("newPassword");
        userAccountService.changePasswordBySms(mobile, smsCode, newPassword, reqParam, clientId);
        return Result.succeed("密码修改成功");
    }

    @ApiOperation(value = "身份证认证")
    @PostMapping(value = "/certify/id-card")
    public Result idCardCertify(@RequestBody UserRealDto userRealDto,
                                @LoginUser UserInfo userInfo,
                                @CommonReqParam ReqParam reqParam,
                                @LoginClient String clientId) {
        userAccountService.idCardCertify(userRealDto, userInfo, reqParam, clientId);
        return Result.succeed();
    }

    @ApiOperation("远程调用：根据手机号查询用户编号")
    @GetMapping(value = "/feign/getUserId/{mobile}")
    public Result<Map<String, Long>> getUserIdByMobile(@PathVariable("mobile") String mobile) {
        if (StringUtil.isNullOrEmpty(mobile)) {
            throw new AppException("手机号不能为空！");
        }
        Long userId = userAccountService.findUserIdByMobile(mobile);
        Assert.notNull(userId, "查询的用户在云服务平台中不存在！");
        Map<String, Long> map = new HashMap<>();
        map.put("userId", userId);
        return Result.succeed(map);
    }

    @ApiOperation("远程调用：根据用户编号查询用户信息")
    @RequestMapping(value = "/feign/{userId}", method = RequestMethod.GET)
    public Result<UserInfoDto> findUserInfoDtoById(@PathVariable("userId") Long userId) {
        return Result.succeed(userAccountService.getUserInfoDtoById(userId));
    }

    @ApiOperation(value = "远程调用：获得用户编号与手机号映射")
    @PostMapping(value = "/feign/mapUserIdAndMobile")
    public Result<Map<Long, String>> mapUserIdAndMobile(@RequestBody List<Long> userIdList) {
        return Result.succeed(userCompoService.mapUserIdAndMobile(userIdList));
    }
}
