package com.zjft.usp.auth.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.zjft.usp.auth.business.service.UserLogService;
import com.zjft.usp.auth.util.AuthUtils;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.vo.ReqParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 退出登录
 *
 * @author CK
 * @date 2019-08-08 09:44
 */
@Slf4j
@Component
public class OauthLogoutHandler implements LogoutHandler {

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private UserLogService userLogService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Assert.notNull(tokenStore, "tokenStore must be set");
        String token = request.getParameter("token");
        if (StrUtil.isBlank(token)) {
            token = AuthUtils.extractToken(request);
        }
        if (StrUtil.isNotBlank(token)) {
            OAuth2AccessToken existingAccessToken = tokenStore.readAccessToken(token);
            OAuth2RefreshToken refreshToken;
            if (existingAccessToken != null) {
                if (existingAccessToken.getRefreshToken() != null) {
                    log.info("remove refreshToken!", existingAccessToken.getRefreshToken());
                    refreshToken = existingAccessToken.getRefreshToken();
                    tokenStore.removeRefreshToken(refreshToken);
                }
                this.saveLogonOut(request, token);
                log.info("remove existingAccessToken!", existingAccessToken);
                tokenStore.removeAccessToken(existingAccessToken);
            }
        }
    }

    private void saveLogonOut(HttpServletRequest request, String token) {
        try {
            OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(token);
            UserInfo userInfo = (UserInfo) oAuth2Authentication.getUserAuthentication().getPrincipal();

            String commonParamJson = request.getHeader("Common-Params");
            String clientId = "";
            String[] clientInfos = AuthUtils.extractClient(request);
            if (clientInfos != null && clientInfos.length > 0) {
                clientId = StrUtil.trimToEmpty(clientInfos[0]);
            }
            ReqParam reqParam = JsonUtil.parseObject(commonParamJson, ReqParam.class);
            JSONObject jsonObject = new JSONObject(reqParam);
            jsonObject.put("clientId", clientId);
            String json = JSON.toJSONString(jsonObject);
            log.info("保存登出日志，用户ID:{}，手机号:{}，附加信息:{}",
                    userInfo.getUserId(), userInfo.getMobile(), json);
            userLogService.saveLogoutLog(userInfo.getUserId(), userInfo.getMobile(), json);
        } catch (Exception e) {
            log.error("保存登出日志发生错误:{}", e);
        }
    }
}
