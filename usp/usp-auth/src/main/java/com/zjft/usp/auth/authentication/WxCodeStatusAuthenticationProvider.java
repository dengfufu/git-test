package com.zjft.usp.auth.authentication;

import com.zjft.usp.auth.business.dto.WeiXinDto;
import com.zjft.usp.auth.business.service.UspUserDetailsService;
import com.zjft.usp.common.oauth2.token.WxCodeStatusAuthenticationToken;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

/**
 * 自定义手机认证,手机号登录校验逻辑
 * 拓展spring security 用户名密码模式
 *
 * @author CK
 * @date 2019-08-02 14:10
 */
@Setter
public class WxCodeStatusAuthenticationProvider implements AuthenticationProvider {

    private UspUserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WxCodeStatusAuthenticationToken authenticationToken = (WxCodeStatusAuthenticationToken) authentication;
        String code = (String) authenticationToken.getPrincipal();
        String openid = (String) authenticationToken.getCredentials();

        UserDetails user = null;
        String unionid = null;
        // 传过来openid 直接尝试用openid 查找用户尝试登陆
        if (!StringUtils.isEmpty(openid)){
            user = userDetailsService.loadUserByWxOpenId(openid);
        }else {
            // 尝试通过code 与微信请求交换获取openid
            try {
                WeiXinDto weiXinDto = userDetailsService.getWXInfo(code);
                if( weiXinDto != null && !StringUtils.isEmpty(weiXinDto.getOpenid())){
                    openid = weiXinDto.getOpenid();
                }
                if( weiXinDto != null && !StringUtils.isEmpty(weiXinDto.getUnionid())){
                    unionid = weiXinDto.getUnionid();
                }
                user = userDetailsService.loadUserByWxOpenId(openid);
            } catch (Exception e) {
                throw new InternalAuthenticationServiceException("获取微信用户信息失败:" + e.getMessage());
            }
        }

//        JSONObject json = new JSONObject();
//        json.put("openid",openid);
//        json.put("unionid",unionid);
//        json.put("type","无法获取用户信息");
//        if (user == null) {
//            throw new InternalAuthenticationServiceException(json.toJSONString());
//        }

        WxCodeStatusAuthenticationToken authenticationResult = new WxCodeStatusAuthenticationToken(user, openid,  null);
        authenticationResult.setDetails(authenticationToken.getDetails());
        authenticationResult.setOpenid(openid);
        authenticationResult.setUnionid(unionid);
        authenticationResult.setHasUser(user != null);
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WxCodeStatusAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
