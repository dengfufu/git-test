package com.zjft.usp.auth.authentication;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.zjft.usp.auth.consts.SecurityConstants;
import com.zjft.usp.auth.util.AuthUtils;
import com.zjft.usp.common.oauth2.token.MobilePasswordAuthenticationToken;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.vo.ReqParam;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 手机+密码登录
 *
 * @author CK
 * @date 2019-08-08 14:40
 */
public class MobilePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    // ~ Static fields/initializers
    // =====================================================================================

    public static final String SPRING_SECURITY_FORM_MOBILE_KEY = "mobile";
    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";

    private String mobileParameter = SPRING_SECURITY_FORM_MOBILE_KEY;
    private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;
    private boolean postOnly = true;

    // ~ Constructors
    // ===================================================================================================

    public MobilePasswordAuthenticationFilter() {
        super(new AntPathRequestMatcher(SecurityConstants.MOBILE_PASSWORD_LOGIN_URL, "POST"));
    }

    // ~ Methods
    // ========================================================================================================

    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String mobile = obtainMobile(request);
        String password = obtainPassword(request);

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

        if (mobile == null) {
            mobile = "";
        }

        if (password == null) {
            password = "";
        }

        mobile = mobile.trim();

        MobilePasswordAuthenticationToken authRequest = new MobilePasswordAuthenticationToken(
                mobile, password, json);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * Enables subclasses to override the composition of the password, such as by
     * including additional values and a separator.
     * <p>
     * This might be used for example if a postcode/zipcode was required in addition to
     * the password. A delimiter such as a pipe (|) should be used to separate the
     * password and extended value(s). The <code>AuthenticationDao</code> will need to
     * generate the expected password in a corresponding manner.
     * </p>
     *
     * @param request so that request attributes can be retrieved
     * @return the password that will be presented in the <code>Authentication</code>
     * request token to the <code>AuthenticationManager</code>
     */
    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(passwordParameter);
    }

    /**
     * Enables subclasses to override the composition of the mobile, such as by
     * including additional values and a separator.
     *
     * @param request so that request attributes can be retrieved
     * @return the mobile that will be presented in the <code>Authentication</code>
     * request token to the <code>AuthenticationManager</code>
     */
    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter(mobileParameter);
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
                              MobilePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    /**
     * Sets the parameter name which will be used to obtain the mobile from the login
     * request.
     *
     * @param mobileParameter the parameter name. Defaults to "mobile".
     */
    public void setMobileParameter(String mobileParameter) {
        Assert.hasText(mobileParameter, "Mobile parameter must not be empty or null");
        this.mobileParameter = mobileParameter;
    }

    /**
     * Sets the parameter name which will be used to obtain the password from the login
     * request..
     *
     * @param passwordParameter the parameter name. Defaults to "password".
     */
    public void setPasswordParameter(String passwordParameter) {
        Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
        this.passwordParameter = passwordParameter;
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

    public final String getMobileParameter() {
        return mobileParameter;
    }

    public final String getPasswordParameter() {
        return passwordParameter;
    }

}
