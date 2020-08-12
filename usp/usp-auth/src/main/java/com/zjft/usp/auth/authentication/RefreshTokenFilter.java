package com.zjft.usp.auth.authentication;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.zjft.usp.auth.business.service.UserLogService;
import com.zjft.usp.auth.consts.SecurityConstants;
import com.zjft.usp.auth.util.ResponseWrapperUtil;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.vo.ReqParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自动刷新token filter
 *
 * @author zgpi
 * @date 2020/5/26 09:18
 */
@Slf4j
@Component
public class RefreshTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserLogService userLogService;
    @Autowired
    private TokenStore tokenStore;

    /**
     * 验证请求url与配置的url是否匹配的工具类
     */
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 返回true代表不执行过滤器，false代表执行
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if (pathMatcher.match(SecurityConstants.AUTO_LOGIN_URL, request.getRequestURI())) {
            return false;
        }
        return true;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ResponseWrapperUtil responseWrapperUtil = new ResponseWrapperUtil(response);
        filterChain.doFilter(request, responseWrapperUtil);

        this.saveAutoLogonSuccess(request, response, responseWrapperUtil);
    }

    private void saveAutoLogonSuccess(HttpServletRequest request, HttpServletResponse response,
                                      ResponseWrapperUtil responseWrapperUtil) {
        try {
            String commonParamJson = request.getHeader("Common-Params");
            String clientId = StrUtil.trimToEmpty(request.getParameter("client_id"));
            ReqParam reqParam = JsonUtil.parseObject(commonParamJson, ReqParam.class);
            JSONObject jsonObject = new JSONObject(reqParam);
            jsonObject.put("clientId", clientId);
            String json = JSON.toJSONString(jsonObject);

            String content = responseWrapperUtil.getResponseData(response.getCharacterEncoding());
            // 重新写入response
            response.getOutputStream().write(content.getBytes());
            Result result = JsonUtil.parseObject(content, Result.class);
            // 自动登录成功
            if (result != null && result.getCode() == Result.SUCCESS) {
                String token = JsonUtil.parseString(JsonUtil.toJson(result.getData()), "access_token");
                OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(token);
                UserInfo userInfo = (UserInfo) oAuth2Authentication.getUserAuthentication().getPrincipal();
                log.info("保存自动登录成功日志，用户ID:{}，手机号:{}，附加信息:{}",
                        userInfo.getUserId(), userInfo.getMobile(), json);
                userLogService.saveAutoLogonSuccess(userInfo.getUserId(), userInfo.getMobile(), json);
            }
        } catch (Exception e) {
            log.error("保存自动登录成功日志发生错误:{}", e);
        }
    }
}
