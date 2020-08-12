package com.zjft.usp.auth.authentication;

import com.zjft.usp.auth.consts.SecurityConstants;
import com.zjft.usp.common.oauth2.token.WxCodeStatusAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信登录
 *
 * @author CK
 * @date 2019-08-08 14:40
 */
public class WxCodeStatusAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    // ~ Static fields/initializers
    // =====================================================================================

    public static final String SPRING_SECURITY_FORM_CODE_KEY = "code";
    public static final String SPRING_SECURITY_FORM_OPENID_KEY = "openid";

    private String codeParameter = SPRING_SECURITY_FORM_CODE_KEY;
    private String openidParameter = SPRING_SECURITY_FORM_OPENID_KEY;
    private boolean postOnly = true;

    // ~ Constructors
    // ===================================================================================================

    public WxCodeStatusAuthenticationFilter() {
        super(new AntPathRequestMatcher(SecurityConstants.WEIXIN_LOGIN_URL, "POST"));
    }

    // ~ Methods
    // ========================================================================================================

    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String code = obtainCode(request);
        String openid = obtainOpenid(request);

        if (code == null) {
            code = "";
        }

        if (openid == null) {
            openid = "";
        }

        code = code.trim();

        WxCodeStatusAuthenticationToken authRequest = new WxCodeStatusAuthenticationToken(
                code, openid);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * Enables subclasses to override the composition of the status, such as by
     * including additional values and a separator.
     * <p>
     * This might be used for example if a postcode/zipcode was required in addition to
     * the status. A delimiter such as a pipe (|) should be used to separate the
     * status and extended value(s). The <code>AuthenticationDao</code> will need to
     * generate the expected status in a corresponding manner.
     * </p>
     *
     * @param request so that request attributes can be retrieved
     * @return the status that will be presented in the <code>Authentication</code>
     * request token to the <code>AuthenticationManager</code>
     */
    protected String obtainOpenid(HttpServletRequest request) {
        return request.getParameter(openidParameter);
    }

    /**
     * Enables subclasses to override the composition of the code, such as by
     * including additional values and a separator.
     *
     * @param request so that request attributes can be retrieved
     * @return the code that will be presented in the <code>Authentication</code>
     * request token to the <code>AuthenticationManager</code>
     */
    protected String obtainCode(HttpServletRequest request) {
        return request.getParameter(codeParameter);
    }

    /**
     * Provided so that subclasses may configure what is put into the authentication
     * request's details property.
     *
     * @param request     that an authentication request is being created for
     * @param authRequest the authentication request object that should have its details
     *                    set
     */
    protected void setDetails(HttpServletRequest request,
                              WxCodeStatusAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    /**
     * Sets the parameter name which will be used to obtain the code from the login
     * request.
     *
     * @param codeParameter the parameter name. Defaults to "code".
     */
    public void setCodeParameter(String codeParameter) {
        Assert.hasText(codeParameter, "Code parameter must not be empty or null");
        this.codeParameter = codeParameter;
    }

    /**
     * Sets the parameter name which will be used to obtain the status from the login
     * request..
     *
     * @param openidParameter the parameter name. Defaults to "status".
     */
    public void setOpenidParameter(String openidParameter) {
        Assert.hasText(openidParameter, "openid parameter must not be empty or null");
        this.openidParameter = openidParameter;
    }

    /**
     * Defines whether only HTTP POST requests will be allowed by this filter. If set to
     * true, and an authentication request is received which is not a POST request, an
     * exception will be raised immediately and authentication will not be attempted. The
     * <tt>unsuccessfulAuthentication()</tt> method will be called as if handling a failed
     * authentication.
     * <p>
     * Defaults to <tt>true</tt> but may be overridden by subclasses.
     */
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getCodeParameter() {
        return codeParameter;
    }

    public final String getOpenidParameter() {
        return openidParameter;
    }

}
