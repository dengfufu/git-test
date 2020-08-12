package com.zjft.usp.auth.authentication;

import com.zjft.usp.auth.exception.ValidateCodeException;
import com.zjft.usp.auth.business.service.ValidateCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 校验短信验证码
 *
 * @author CK
 * @date 2019-08-23 15:45
 */
@Slf4j
@Component
public class MobileSmsCodeFilter extends OncePerRequestFilter {

    @Autowired
    ValidateCodeService validateCodeService;

    /**
     * 验证码校验失败处理器
     */
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    /**
     * 验证请求url与配置的url是否匹配的工具类
     */
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 返回true代表不执行过滤器，false代表执行
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 登录提交的时候验证验证码
        if (pathMatcher.match("/oauth/sms/token", request.getRequestURI())) {
            return false;
        }
        return true;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            validateCodeService.validate(request);
        } catch (ValidateCodeException e) {
            authenticationFailureHandler.onAuthenticationFailure(request, response, e);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
