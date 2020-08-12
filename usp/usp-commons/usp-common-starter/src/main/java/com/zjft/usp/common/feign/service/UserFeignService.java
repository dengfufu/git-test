package com.zjft.usp.common.feign.service;

import com.zjft.usp.common.feign.service.fallback.UserFeignServiceFallbackFactory;
import com.zjft.usp.common.model.LoginAppUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author CK
 * @date 2019-08-20 08:46
 */
@FeignClient(name = "usp-uas", fallbackFactory = UserFeignServiceFallbackFactory.class)
public interface UserFeignService {

    /**
     * 通过手机号登录
     *
     * @param mobile
     * @return
     * @author zgpi
     * @date 2019/11/28 11:00
     **/
    @RequestMapping(value = "/account/user/mobile/logon", params = "mobile", method = RequestMethod.GET)
    LoginAppUser logonByMobile(@RequestParam("mobile") String mobile);

    /**
     * 通过logonId查询用户信息
     *
     * @param logonId
     * @return
     */
    @RequestMapping(value = "/account/user/logon", params = "logonId", method = RequestMethod.GET)
    LoginAppUser logonByLogonId(@RequestParam("logonId") String logonId);


    @RequestMapping(value = "/account/user/wx/logon", params = "openId", method = RequestMethod.GET)
    LoginAppUser logonByWxId(@RequestParam("openId") String openId);

    /**
     *
     */
    @GetMapping(value = "/users/name/{userId}")
    UserInfo selectByUserId(@PathVariable("userId") String userId);

    /**
     * 根据idList获得企业ID与名称映射
     *
     * @param corpIdList
     * @return
     */
    @RequestMapping(value = "/corp-registry/feign/mapByCorpIdList", method = RequestMethod.POST)
    Result<Map<Long, String>> mapCorpIdAndNameByCorpIdList(@RequestParam("corpIdList") List<Long> corpIdList);

    /**
     * 根据idList获得企业ID与名称映射
     *
     * @param corpIdList
     * @return
     */
    @RequestMapping(value = "/corp-registry/feign/mapByJsonCorpIds", method = RequestMethod.POST)
    Result<Map<Long, String>> mapCorpIdAndNameByJsonCorpIds(@RequestParam("corpIdList") List<Long> corpIdList);

    /**
     * 保存登录成功日志
     *
     * @param userLog
     * @return
     * @author zgpi
     * @date 2020/5/25 14:43
     **/
    @RequestMapping(value = "/user-logon-log/feign/success/save", method = RequestMethod.POST, headers = {"content-type=application/json"})
    Result saveLogonSuccessLog(@RequestBody String userLog);


    @GetMapping(value = "/wx/feign/openid/{userId}")
    String getOpenidByUserid(@PathVariable("userId") Long userId);
    /**
     * 保存登录成功日志
     *
     * @param userLog
     * @return
     * @author zgpi
     * @date 2020/5/25 14:43
     **/
    @RequestMapping(value = "/user-logon-log/feign/auto-success/save", method = RequestMethod.POST, headers = {"content-type=application/json"})
    Result saveAutoLogonSuccessLog(@RequestBody String userLog);

    /**
     * 保存登录密码错误日志
     *
     * @param userLog
     * @return
     * @author zgpi
     * @date 2020/5/25 14:43
     **/
    @RequestMapping(value = "/user-logon-log/feign/pwd-fail/save", method = RequestMethod.POST, headers = {"content-type=application/json"})
    Result saveLogonPwdFailLog(@RequestBody String userLog);

    /**
     * 保存登出日志
     *
     * @param userLog
     * @return
     * @author zgpi
     * @date 2020/5/25 19:59
     **/
    @RequestMapping(value = "/user-logon-log/feign/logout/save", method = RequestMethod.POST, headers = {"content-type=application/json"})
    Result saveLogoutLog(@RequestBody String userLog);
}
