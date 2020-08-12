package com.zjft.usp.auth.business.service;

import com.zjft.usp.auth.business.dto.WeiXinDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 拓展UserDetailsService以支持其他登录认证方式
 *
 * @author CK
 * @date 2019-08-01 11:05
 */
public interface UspUserDetailsService extends UserDetailsService {

    /**
     * 根据电话号码查询用户
     *
     * @param mobile
     * @return
     */
    UserDetails loadUserByMobile(String mobile);

    /**
     * 根据WxCode查询用户
     *
     * @param code
     * @param status
     * @return
     */
    UserDetails loadUserByWxCode(String code, String status);

    /**
     * 根据微信openid获取用户
     * @param openId
     * @return
     */
    UserDetails loadUserByWxOpenId(String openId);

    /**
     * 获取微信用户openid
     * @param code
     */
    WeiXinDto getWXInfo(String code);
}
