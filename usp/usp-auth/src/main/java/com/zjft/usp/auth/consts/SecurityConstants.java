package com.zjft.usp.auth.consts;

public interface SecurityConstants {

    /**
     * 手机号+密码登录
     */
    String MOBILE_PASSWORD_LOGIN_URL = "/oauth/mobile/token";

    /**
     * 手机号+短信验证码登录
     */
    String MOBILE_SMSCODE_LOGIN_URL = "/oauth/sms/token";

    /**
     * 手机号+短信验证码登录
     */
    String WEIXIN_LOGIN_URL = "/oauth/wx/token";

    /**
     * 自动登录URL
     */
    String AUTO_LOGIN_URL = "/oauth/token";

    /**
     * 登出URL
     */
    String LOGOUT_URL = "/oauth/remove/token";

    /**
     * 缓存client的redis key，这里是hash结构存储
     */
    String CACHE_CLIENT_KEY = "oauth_client_details";
}
