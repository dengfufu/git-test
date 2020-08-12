package com.zjft.usp.uas.corp.controller;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginClient;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.OauthClient;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.utils.RestTemplateUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.corp.composite.CorpUserCompoService;
import com.zjft.usp.uas.corp.dto.CorpDto;
import com.zjft.usp.uas.corp.dto.CorpUserAppDto;
import com.zjft.usp.uas.corp.dto.CorpUserDto;
import com.zjft.usp.uas.corp.dto.CorpUserInfoDto;
import com.zjft.usp.uas.corp.filter.CorpUserAppFilter;
import com.zjft.usp.uas.corp.filter.CorpUserFilter;
import com.zjft.usp.uas.corp.model.CorpUser;
import com.zjft.usp.uas.corp.service.CorpUserAppService;
import com.zjft.usp.uas.corp.service.CorpUserService;
import com.zjft.usp.uas.right.model.SysTenant;
import com.zjft.usp.uas.right.service.SysTenantService;
import com.zjft.usp.uas.user.service.UserInfoService;
import com.zjft.usp.uas.user.service.UserRealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 企业用户 控制器
 *
 * @author canlei
 * 2019-08-04
 */
@Api(tags = "企业人员")
@RestController
@Slf4j
@RequestMapping(value = "/corp-user")
public class CorpUserController {

    @Autowired
    private CorpUserService corpUserService;
    @Autowired
    private CorpUserAppService corpUserAppService;
    @Autowired
    private CorpUserCompoService corpUserCompoService;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    UserRealService userRealService;

    @Autowired
    SysTenantService sysTenantService;

    @ApiOperation("分页查询加入企业申请")
    @PostMapping(value = "/apply/query")
    public Result<ListWrapper<CorpUserAppDto>> pageCorpUserApp(@RequestBody CorpUserAppFilter corpUserAppFilter,
                                                               @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.corpUserAppService.pageCorpUserApp(corpUserAppFilter, reqParam));
    }

    @ApiOperation("加入企业申请")
    @PostMapping(value = "/join/apply")
    public Result joinCorpApply(@RequestBody CorpUserAppDto corpUserAppDto,
                                @CommonReqParam ReqParam reqParam,
                                @LoginClient String clientId,
                                @LoginUser UserInfo user) {
        if (corpUserAppDto.getAutoJoin()) {
            // 自动审核申请(动态密码模式)
            SysTenant sysTenant = sysTenantService.getById(corpUserAppDto.getCorpId());
            if (!StringUtils.isEmpty(sysTenant.getApplyCheckApi())) {
                if (StringUtils.isEmpty(corpUserAppDto.getCaptcha())) {
                    // 1. 请求验证码
                    String url = sysTenant.getApplyCheckApi() + "?account=" + corpUserAppDto.getAccount();
                    String result = RestTemplateUtil.get(url, null);
                    log.info("请求验证码结果: {}", result);
                    Map<String, Object> resultObj1 = JsonUtil.parseMap(result);
                    Map<String, Object> retMsg1 = (Map<String, Object>) resultObj1.get("retMsg");
                    int code = (int) retMsg1.get("code");
                    String msg = (String) retMsg1.get("msg");
                    if (code == 1) {
                        return Result.succeed(msg);
                    } else {
                        return Result.failed(msg);
                    }
                } else {
                    // 2. 远程校验
                    Map<String, String> paramData = new HashMap<>();
                    paramData.put("account", corpUserAppDto.getAccount());
                    paramData.put("captcha", corpUserAppDto.getCaptcha());
                    String result = RestTemplateUtil.postJson(sysTenant.getApplyCheckApi(), JsonUtil.toJsonString(paramData), null);
                    log.info("远程校验结果: {}", result);
                    Map<String, Object> resultObj2 = JsonUtil.parseMap(result);
                    Map<String, Object> retMsg2 = (Map<String, Object>) resultObj2.get("retMsg");
                    int code = (int) retMsg2.get("code");
                    String msg = (String) retMsg2.get("msg");
                    if (code == 1) {
                        corpUserAppService.joinCorpApply(corpUserAppDto, reqParam, user.getUserId(), clientId);
                        return Result.succeed("自动审核加入企业成功！");
                    } else {
                        return Result.failed(msg);
                    }
                }
            } else {
                return Result.failed("该企业不支持自动审核！");
            }
        } else {
            // 人工审核申请
            corpUserAppService.joinCorpApply(corpUserAppDto, reqParam, user.getUserId(), clientId);
            // 提醒管理员审核
            corpUserAppService.corpUserJoinListener(user.getUserId(), corpUserAppDto.getCorpId());
            return Result.succeed();
        }
    }

    @ApiOperation("加入企业审核")
    @PostMapping(value = "/join/audit")
    public Result joinCorpAudit(@RequestBody CorpUserAppDto corpUserAppDto,
                                @CommonReqParam ReqParam reqParam,
                                @LoginClient String clientId,
                                @LoginUser UserInfo user) {
        corpUserAppService.joinCorpCheck(corpUserAppDto, reqParam, user.getUserId(), clientId);
        return Result.succeed();
    }

    @ApiOperation("分页查询企业人员")
    @PostMapping(value = "/query")
    public Result<ListWrapper<CorpUserDto>> query(@RequestBody CorpUserFilter corpUserFilter,
                                                  @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(corpUserFilter.getCorpId())) {
            corpUserFilter.setCorpId(reqParam.getCorpId());
        }
        return Result.succeed(this.corpUserService.query(corpUserFilter));
    }

    @ApiOperation(value = "企业员工详情")
    @GetMapping(value = "/{userId}/{corpId}")
    public Result<CorpUserDto> findCorpUserDetail(@PathVariable("userId") Long userId,
                                                  @PathVariable("corpId") Long corpId) {
        return Result.succeed(corpUserCompoService.findCorpUserDetail(userId, corpId));
    }

    @ApiOperation("根据企业id分页查询企业所有人员(无过滤)")
    @PostMapping(value = "/queryCorpUser")
    public Result<ListWrapper<CorpUserDto>> queryCorpUser(@RequestBody CorpUserFilter corpUserFilter,
                                                  @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(corpUserFilter.getCorpId())) {
            corpUserFilter.setCorpId(reqParam.getCorpId());
        }
        return Result.succeed(this.corpUserService.queryCorpUser(corpUserFilter));
    }

    @ApiOperation(value = "添加企业员工")
    @PostMapping(value = "/add")
    public Result addCorpUser(@RequestBody CorpUserDto corpUserDto,
                              @LoginClient String clientId,
                              @LoginUser UserInfo userInfo,
                              @CommonReqParam ReqParam reqParam) {
        corpUserCompoService.addCorpUser(corpUserDto, userInfo.getUserId(), clientId, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "隐藏企业员工")
    @PostMapping(value = "/hidden")
    public Result hiddenCorpUser(@RequestBody CorpUserFilter corpUserFilter) {
        corpUserService.hiddenCorpUser(corpUserFilter);
        return Result.succeed();
    }

    @ApiOperation(value = "修改企业员工")
    @PostMapping(value = "/update")
    public Result updateCorpUser(@RequestBody CorpUserDto corpUserDto) {
        corpUserCompoService.updateCorpUser(corpUserDto);
        return Result.succeed();
    }

    @ApiOperation(value = "删除企业员工")
    @DeleteMapping(value = "/{corpId}/{userId}")
    public Result delCorpUser(@PathVariable("corpId") Long corpId,
                              @PathVariable("userId") Long userId,
                              @LoginClient String clientId,
                              @LoginUser UserInfo userInfo) {
        corpUserCompoService.delCorpUser(userId, corpId, userInfo.getUserId(), clientId);
        return Result.succeed();
    }

    @ApiOperation(value = "openApi: 删除企业员工")
    @PostMapping(value = "/change")
    public Result delCorpUser4open(@RequestBody Map<String, Object> reqParam,
                                   @LoginClient(isFull = true) OauthClient oauthClient,
                                   @LoginUser UserInfo currentUserInfo) {
        log.info("open-api: 删除企业员工,{} ", reqParam);
        Boolean test = (Boolean) reqParam.get("test");
        String username = (String) reqParam.get("username");
        String mobile = (String) reqParam.get("mobile");
        Assert.notNull(username, "username 不能为空");
        Assert.notNull(mobile, "mobile 不能为空");

        UserInfo userInfo = userInfoService.getUserInfoByMobile(mobile);
        if (StringUtils.isEmpty(userInfo)) {
            log.info("open-api: 删除企业员工,用户未注册过");
            return Result.succeed("用户未注册过");
        }
        Long userId = userInfo.getUserId();
        String usernameTmp = userRealService.findUserRealDtoById(userId).getUserName();
        if (username.indexOf(usernameTmp) != 0) {
            log.info("open-api: 删除企业员工, 用户真实姓名与手机号不匹配");
            return Result.failed("用户真实姓名与手机号不匹配");
        }
        Long corpId = oauthClient.getCorpId();
        String additionalInformation = oauthClient.getAdditionalInformation();
        log.info("open-api: 删除企业员工:clientId:{},corpId:{},additionalInformation:{}, username:{},mobile:{}",
                oauthClient.getClientId(), corpId, additionalInformation, username, mobile);
        if (!corpUserService.ifUserInCorp(userId, corpId)) {
            return Result.succeed("该用户非该企业员工");
        }
        // 测试用
        if (test) {
            log.info("open-api: 删除企业员工,测试成功");
            return Result.succeed("测试模式: 删除企业成功,系统中存在该用户信息，姓名:" + username + ",手机号:" + mobile);
        } else {
            Long currentUserId = null;
            if (!StringUtils.isEmpty(currentUserInfo)) {
                currentUserId = currentUserInfo.getUserId();
            }
            corpUserCompoService.delCorpUser(userId, corpId, currentUserId, oauthClient.getClientId());
            log.info("open-api: 删除企业员工,删除企业员工成功");
            return Result.succeed("删除企业员工成功");
        }
    }

    @ApiOperation("根据用户获取企业列表")
    @PostMapping(value = "/corp/list")
    public Result<List<CorpDto>> listUserCorp(@RequestBody CorpUserDto corpUserDto,
                                              @LoginUser UserInfo userInfo) {
        if (LongUtil.isZero(corpUserDto.getUserId())) {
            corpUserDto.setUserId(userInfo.getUserId());
        }
        return Result.succeed(corpUserService.listCorpInfo(corpUserDto));
    }

    @ApiOperation("根据企业获取人员列表")
    @PostMapping(value = "/user/list")
    public Result<List<CorpUserDto>> listCorpUser(@RequestBody CorpUserDto corpUserDto,
                                                  @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(corpUserDto.getCorpId())) {
            corpUserDto.setCorpId(reqParam.getCorpId());
        }
        return Result.succeed(corpUserService.getCorpUserByCorpId(corpUserDto, reqParam));
    }

    @ApiOperation("根据企业模糊查询人员姓名列表")
    @PostMapping(value = "/match")
    public Result<List<CorpUserDto>> matchCorpUser(@RequestBody CorpUserFilter corpUserFilter) {
        return Result.succeed(corpUserService.matchCorpUserNames(corpUserFilter));
    }

    @ApiOperation("模糊查询企业人员列表（包含隐藏人员）")
    @PostMapping(value = "/matchCorpUserByCorp")
    public Result<List<CorpUserDto>> matchCorpUserByCorp(@RequestBody CorpUserFilter corpUserFilter) {
        return Result.succeed(corpUserService.matchCorpUserByCorp(corpUserFilter));
    }

    @ApiOperation("远程调用：根据网点模糊查询人员列表")
    @PostMapping(value = "/feign/match")
    public Result<List<CorpUserDto>> matchCorpBranchUser(@RequestBody CorpUserFilter corpUserFilter) {
        return Result.succeed(corpUserService.matchCorpUser(corpUserFilter));
    }

    @ApiOperation("远程调用：根据用户获取企业编号列表")
    @GetMapping(value = "/feign/getCorpIdList/{userId}")
    public Result<List<Long>> listCorpIdByUserId(@PathVariable("userId") Long userId) {
        return Result.succeed(corpUserService.listCorpIdByUserId(userId));
    }

    @ApiOperation("远程调用：根据企业编号和人员编号List获取人员编号和名称映射以及头像信息")
    @PostMapping(value = "/feign/mapCorpUserInfoByUserIdList")
    public Result<Map<Long, CorpUserInfoDto>> mapCorpUserInfoByUserIdList(@RequestParam("userIdList") List<Long> userIdList,
                                                                          @RequestParam("corpId") Long corpId) {
        return Result.succeed(corpUserService.mapCorpUserInfoByUserIdList(userIdList, corpId));
    }

    @ApiOperation("远程调用：根据企业编号List获取人员编号和名称映射")
    @PostMapping(value = "/feign/mapByCorpIdList")
    public Result<Map<Long, String>> mapUserIdAndNameByCorpIdList(@RequestParam("corpIdList") List<Long> corpIdList) {
        return Result.succeed(corpUserService.mapUserIdAndNameByCorpIdList(corpIdList));
    }

    @ApiOperation("远程调用：根据企业编号获得企业人员映射")
    @GetMapping(value = "/feign/map/{corpId}")
    public Result<Map<Long, String>> mapUserIdAndNameByCorpId(@PathVariable("corpId") Long corpId) {
        return Result.succeed(corpUserService.mapUserIdAndName(corpId));
    }

    @ApiOperation("远程调用：根据企业获取人员编号列表")
    @GetMapping(value = "/feign/user/list/{corpId}")
    public Result<List<Long>> listCorpUser(@PathVariable("corpId") Long corpId) {
        return Result.succeed(corpUserService.listUserIdByCorpId(corpId));
    }

    @ApiOperation("远程调用：分页查询企业人员")
    @PostMapping(value = "/feign/query")
    public Result<ListWrapper<CorpUserDto>> query(@RequestBody CorpUserFilter corpUserFilter) {
        return Result.succeed(this.corpUserService.query(corpUserFilter));
    }

    @ApiOperation("远程调用：根据人员编号和企业编号获得企业人员信息")
    @PostMapping(value = "/feign/findCorpUserByUserIdAndCorpId")
    public Result<CorpUser> findCorpUserByUserIdAndCorpId(@RequestParam("userId") Long userId,
                                                          @RequestParam("corpId") Long corpId) {
        return Result.succeed(corpUserService.findCorpUserByUserIdAndCorpId(userId, corpId));
    }

    @ApiOperation("远程调用：根据企业编号和人员编号列表获得企业员工账号列表")
    @PostMapping(value = "/feign/listUserIdByAccountList")
    public Result<List<Long>> listUserIdByAccountList(@RequestBody CorpUserFilter corpUserFilter) {
        return Result.succeed(corpUserService.listUserIdByAccountList(corpUserFilter));
    }
}
