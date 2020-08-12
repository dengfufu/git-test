package com.zjft.usp.uas.user.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.feign.service.AuthFeignService;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.RsaUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.redis.template.RedisRepository;
import com.zjft.usp.uas.protocol.service.ProtocolDefService;
import com.zjft.usp.uas.protocol.service.ProtocolSignService;
import com.zjft.usp.uas.user.dto.UserInfoDto;
import com.zjft.usp.uas.user.dto.UserWxDto;
import com.zjft.usp.uas.user.enums.UserOperEnum;
import com.zjft.usp.uas.user.model.UserMobile;
import com.zjft.usp.uas.user.model.UserReal;
import com.zjft.usp.uas.user.model.UserSafe;
import com.zjft.usp.uas.user.model.UserWx;
import com.zjft.usp.uas.user.service.*;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zphu
 * @date 2019/8/16 10:14
 * @Version 1.0
 **/
@Transactional(rollbackFor = Exception.class)
@Service
public class UserRegisterServiceImpl implements UserRegisterService {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserRealService userRealService;
    @Autowired
    private UserMobileService userMobileService;
    @Autowired
    private UserAppService userAppService;
    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private UserOperService userOperService;
    @Autowired
    private UserDeviceService userDeviceService;
    @Autowired
    private UserSafeService userSafeService;
    @Autowired
    private UserLogonIdService userLogonIdService;
    @Autowired
    private AuthFeignService authFeignService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    ProtocolSignService protocolSignService;

    @Autowired
    ProtocolDefService protocolDefService;

    @Autowired
    private UserWxService userWxService;

    @Override
    public UserInfo registerUserByMobile(UserInfoDto userInfoDto, ReqParam reqParam, String clientId) {
        // 验证验证码
        String result = authFeignService.verifySmsCode(userInfoDto.getMobile(), userInfoDto.getSmsCode());
        JSONObject jsonObject = JSONObject.parseObject(result);
        String code = jsonObject.getString("code");
        if (!"0".equals(code)) {
            throw new AppException(jsonObject.getString("msg"));
        }

        if (this.checkIsRegister(userInfoDto.getMobile())) {
            throw new AppException("该账号已经注册，请进行登录！");
        }

        // 注册手机号
        UserInfo userInfo = userInfoService.registerUserByMobile(userInfoDto.getMobile());

        // 插入手机号表记录
        userMobileService.insert(userInfo.getMobile(), userInfo.getUserId());

        // 设置用户登录信息
        userSafeService.setUserSafeInfo(userInfoDto, userInfo.getUserId());
        UserSafe userSafe = userSafeService.getOne(new QueryWrapper<UserSafe>().eq("userid", userInfo.getUserId()), true);
        if (StringUtils.isNotEmpty(userSafe.getLogonId())) {
            userLogonIdService.saveOrUpdate(userSafe.getLogonId(), userSafe.getUserId());
        }
        if (!StringUtils.isEmpty(clientId)) {
            //uas_user_app插入用户应用关联表信息
            userAppService.insert(clientId, userInfo.getUserId());
            userDeviceService.addUserDeviceInfo(reqParam, userInfo.getUserId());
            userOperService.insert(JsonUtil.toJson(userInfoDto), "", UserOperEnum.UO_REGISTER_USER, userInfo.getUserId(), reqParam, clientId);
        }

        // 查询默认需要签约的协议
        List<Integer> protocolIds = this.protocolDefService.listByModule("").stream().map(e -> e.getId()).collect(Collectors.toList());

        protocolSignService.signToC(userInfo.getUserId(), protocolIds);
        return userInfo;
    }

    @Override
    public String getPublicKey() {
        //获取随机RSA秘钥对
        RsaUtil.KeyPairInfo keyPairInfo = RsaUtil.getKeyPair();
        //将公钥保存为key，私钥缓存为value,有效期为60s
        redisRepository.setExpire(keyPairInfo.getPublicKey(), keyPairInfo.getPrivateKey(), 60L);
        //返回公钥给前端加密密码，注意公钥返回给前端的json中含有'\n'字符，使用公钥加密前需要删除该字符
        return keyPairInfo.getPublicKey();
    }

    @Override
    public Boolean checkIsRegister(String mobile) {
        Assert.notEmpty(mobile, "手机号不能为空");
        UserMobile userMobile = userMobileService.getById(mobile);
        return userMobile != null;
    }

    @Override
    public Long registryByCorpAdmin(String mobile, String userName) {
        if (StrUtil.isBlank(mobile)) {
            throw new AppException("手机号不能为空！");
        }
        if (mobile.trim().length() < 11) {
            throw new AppException("手机号长度小于11位！");
        }
        Long userId = 0L;
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setMobile(mobile);
        userInfoDto.setUserName(userName);
//        userInfoDto.setPassword(userInfoDto.getMobile().substring(5));
        UserMobile userMobile = userMobileService.getById(mobile);
        // 已注册
        if (userMobile != null) {
            UserReal userReal = userRealService.getOne(new QueryWrapper<UserReal>()
                .eq("userid", userMobile.getUserId()));
            // 已实名认证，姓名不一致
            if (userReal != null && userReal.getVerified() == 1
                && StrUtil.isNotBlank(userReal.getUserName())
                && !StrUtil.trimToEmpty(mobile).equals(StrUtil.trimToEmpty(userReal.getUserName()))) {
                throw new AppException("人员已注册，姓名与实名认证的不一致！");
            }
            userId = userMobile.getUserId();
            userReal = userReal == null ? new UserReal() : userReal;
            userReal.setUserId(userId);
            userReal.setUserName(userName);
            userRealService.saveOrUpdate(userReal);
        } else {
            //注册手机号
            UserInfo userInfo = userInfoService.registerUserByMobile(userInfoDto.getMobile());
            //插入手机号表记录
            userMobileService.insert(userInfoDto.getMobile(), userInfo.getUserId());

            // 添加实名认证信息，未认证
            UserReal userReal = new UserReal();
            userReal.setUserId(userInfo.getUserId());
            userReal.setUserName(userName);
            userRealService.save(userReal);

            //设置用户登录信息
            userSafeService.setUserSafeInfo(userInfoDto, userInfo.getUserId());
            UserSafe userSafe = userSafeService.getOne(new QueryWrapper<UserSafe>().eq("userid", userInfo.getUserId()), true);
            if (StringUtils.isNotEmpty(userSafe.getLogonId())) {
                userLogonIdService.saveOrUpdate(userSafe.getLogonId(), userSafe.getUserId());
            }
            userId = userInfo.getUserId();
        }
        return userId;
    }

    /**
     * 企业管理员注册用户
     *
     * @param userId
     * @param userName
     * @return
     * @author zgpi
     * @date 2019/11/13 16:47
     **/
    @Override
    public void updateByCorpAdmin(Long userId, String userName) {
        UserReal userReal = userRealService.getOne(new QueryWrapper<UserReal>()
            .eq("userid", userId));
        // 已实名认证，姓名不一致
        if (userReal != null && userReal.getVerified() == 1
            && StrUtil.isNotBlank(userReal.getUserName())
            && !StrUtil.trimToEmpty(userName).equals(StrUtil.trimToEmpty(userReal.getUserName()))) {
            throw new AppException("人员已注册，姓名与实名认证的不一致！");
        }
        if (userReal == null) {
            userReal = new UserReal();
            userReal.setUserId(userId);
        }
        userReal.setUserName(userName);
        userRealService.saveOrUpdate(userReal);
    }


    /**
     * 微信注册
     * @param userWxDto
     * @param reqParam
     * @param clientId
     */
    @Override
    public void registerUserByWX(UserWxDto userWxDto, ReqParam reqParam, String clientId) {
        if (userWxDto == null || StringUtils.isEmpty(userWxDto.getOpenId())){
            throw new AppException("微信信息出错");
        }
        UserInfo userInfo = null;

        boolean isRegister = this.checkIsRegister(userWxDto.getMobile());
        // 如果已经存在账号
        if (isRegister) {
            userInfo =  this.userInfoService.getUserInfoByMobile(userWxDto.getMobile());
        } else{
            userInfo = this.registerUserByMobile(userWxDto,reqParam,clientId);
        }

        // 如果是注册更新姓名
        if (!isRegister) {
            UserReal userReal = new UserReal();
            userReal.setUserName(userWxDto.getUserName());
            userReal.setUserId(userInfo.getUserId());
            userRealService.saveOrUpdate(userReal);
        }

        Boolean isBindWx = userWxService.checkIsBindWx(userWxDto.getMobile());

        // 如果已经绑定过微信，先解除原先的绑定
        if (isBindWx) {
            userWxService.delByUserId(userInfo.getUserId());
        }
        UserWx userWx = new UserWx();
        userWx.setOpenId(userWxDto.getOpenId());
        userWx.setUnionId(userWxDto.getUnionId());
        userWx.setUserId(userInfo.getUserId());
        userWx.setAddTime(DateUtil.parse(DateUtil.now()));
        userWxService.saveOrUpdate(userWx);
    }

}
