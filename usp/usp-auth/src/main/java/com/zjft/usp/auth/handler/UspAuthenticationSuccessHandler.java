package com.zjft.usp.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zjft.usp.auth.util.AuthUtils;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.oauth2.token.WxCodeStatusAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证成功钩子
 *
 * @author CK
 * @date 2019-08-08 15:27
 */
@Component
@Slf4j
public class UspAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Resource
    private ObjectMapper objectMapper;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource
    private AuthorizationServerTokenServices authorizationServerTokenServices; // 创建oath2Token

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        final String[] clientInfos = AuthUtils.extractClient(request);
        String clientId = clientInfos[0];
        String clientSecret = clientInfos[1];

        // 校验 clientDetails 信息
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
        if (clientDetails == null) {
            throw new UnapprovedClientAuthenticationException("clientId对应的信息不存在");
        } else if (!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
            throw new UnapprovedClientAuthenticationException("clientSecret不匹配");
        }

        // 自定义的 grantType
        TokenRequest tokenRequest = new TokenRequest(Collections.EMPTY_MAP, clientId, clientDetails.getScope(), "mobile_smsCode");
        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
        OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);


        response.setContentType("application/json;charset=UTF-8");
        if (authentication instanceof WxCodeStatusAuthenticationToken){
            Map<String,Object> result = new HashMap<>();

            result.put("token",token);
            result.put("openid",((WxCodeStatusAuthenticationToken) authentication).getOpenid());
            result.put("unionid",((WxCodeStatusAuthenticationToken) authentication).getUnionid());
            result.put("hasuser",((WxCodeStatusAuthenticationToken) authentication).isHasUser());
            response.getWriter().write(objectMapper.writeValueAsString(Result.succeed(result)));
        } else {
            response.getWriter().write(objectMapper.writeValueAsString(Result.succeed(token)));
        }
    }
}
