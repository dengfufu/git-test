package com.zjft.usp.auth.business.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zjft.usp.auth.business.dto.WeiXinDto;
import com.zjft.usp.auth.business.service.UspUserDetailsService;
import com.zjft.usp.common.feign.service.RightFeignService;
import com.zjft.usp.common.feign.service.UserFeignService;
import com.zjft.usp.common.model.LoginAppUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.redis.template.RedisRepository;
import com.zjft.usp.wechat.service.WechatFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 基于数据库的认证
 * spring security 规范
 * 远程调用 UserService
 *
 * @author CK
 * @date 2019-08-01 11:07
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UspUserDetailsService {

    @Resource
    private UserFeignService userFeignService;
    @Resource
    private RightFeignService rightFeignService;

    @Resource
    private WechatFeignService wechatFeignService;

    @Resource
    private RedisRepository redisRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // mobile是唯一登录用户名
        LoginAppUser loginAppUser = userFeignService.logonByMobile(username);
        return loginAppUser;
    }

    /**
     * 手机号登录
     *
     * @param mobile
     * @return
     * @author zgpi
     * @date 2019/12/13 11:32
     **/
    @Override
    public UserDetails loadUserByMobile(String mobile) {
        LoginAppUser loginAppUser = userFeignService.logonByMobile(mobile);
        long end = System.currentTimeMillis();
        if (loginAppUser != null && LongUtil.isNotZero(loginAppUser.getUserId())) {
            long start_user = System.currentTimeMillis();
            // 初始化用户角色
            this.initUserRoleRedis(loginAppUser.getUserId());
            end = System.currentTimeMillis();
            log.info("初始化用户ID:{}，角色权限耗时：{}毫秒", loginAppUser.getUserId(), (end - start_user));
        }
        return loginAppUser;
    }

    @Override
    public UserDetails loadUserByWxCode(String code, String status) {
        //todo
//        String openId = this.wechatFeignService.logonByWxCode(code);
        String openId = "";
        LoginAppUser loginAppUser = this.userFeignService.logonByWxId(openId);
        log.info(JSON.toJSONString(loginAppUser));
        return loginAppUser;
    }

    /**
     * 根据微信用户openid 获取用户
     * @param openId
     * @return
     */
    @Override
    public UserDetails loadUserByWxOpenId(String openId) {
        long start = System.currentTimeMillis();
        LoginAppUser loginAppUser = this.userFeignService.logonByWxId(openId);
        long end = System.currentTimeMillis();
        log.info("微信登录耗时：{}毫秒", (end - start));
        return loginAppUser;
    }

    /**
     * 获取微信用户
     * @param code
     * @return
     */
    @Override
    public WeiXinDto getWXInfo(String code) {

        String wxUserJson = wechatFeignService.logonByWxCode(code);
        if(StringUtils.isEmpty(wxUserJson)){
            return null;
        }else{
            return JSONObject.parseObject(wxUserJson,WeiXinDto.class);
        }

    }

    /**
     * 初始化人员对应的角色列表
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2020/5/28 21:02
     **/
    private void initUserRoleRedis(Long userId) {
        Result result = rightFeignService.initUserRoleRedis(userId);
        if (result == null) {
            log.error("初始化用户角色失败，用户ID:{}", userId);
        }
        if (!Result.isSucceed(result)) {
            log.error("初始化用户角色失败，用户ID:{}，错误信息:{}", userId, result);
        }
    }

}
