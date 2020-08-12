package com.zjft.usp.auth.authentication;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.zjft.usp.auth.consts.SecurityConstants;
import com.zjft.usp.auth.util.AuthUtils;
import com.zjft.usp.common.oauth2.token.MobileSmsCodeAuthenticationToken;
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
 * 手机+短信登录
 *
 * @author CK
 * @date 2019-08-08 14:40
 */
public class MobileSmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    // ~ Static fields/initializers
    // =====================================================================================

    public static final String SPRING_SECURITY_FORM_MOBILE_KEY = "mobile";
    public static final String SPRING_SECURITY_FORM_SMSCODE_KEY = "smsCode";

    private String mobileParameter = SPRING_SECURITY_FORM_MOBILE_KEY;
    private String smsCodeParameter = SPRING_SECURITY_FORM_SMSCODE_KEY;
    private boolean postOnly = true;

    // ~ Constructors
    // ===================================================================================================

    public MobileSmsCodeAuthenticationFilter() {
        super(new AntPathRequestMatcher(SecurityConstants.MOBILE_SMSCODE_LOGIN_URL, "POST"));
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
        String smsCode = obtainSmsCode(request);

        String commonParamJson = request.getHeader("Common-Params");
        String clientId = "";
        String[] clientInfos = AuthUtils.extractClient(request);
        if(clientInfos != null && clientInfos.length > 0){
            clientId = StrUtil.trimToEmpty(clientInfos[0]);
        }

        ReqParam reqParam = JsonUtil.parseObject(commonParamJson, ReqParam.class);
        JSONObject jsonObject = new JSONObject(reqParam);
        jsonObject.put("clientId", clientId);
        String json = JSON.toJSONString(jsonObject);

        if (mobile == null) {
            mobile = "";
        }

        if (smsCode == null) {
            smsCode = "";
        }

        mobile = mobile.trim();

        MobileSmsCodeAuthenticationToken authRequest = new MobileSmsCodeAuthenticationToken(
                mobile, smsCode, json);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * Enables subclasses to override the composition of the smsCode, such as by
     * including additional values and a separator.
     * <p>
     * This might be used for example if a postcode/zipcode was required in addition to
     * the smsCode. A delimiter such as a pipe (|) should be used to separate the
     * smsCode and extended value(s). The <code>AuthenticationDao</code> will need to
     * generate the expected smsCode in a corresponding manner.
     * </p>
     *
     * @param request so that request attributes can be retrieved
     * @return the smsCode that will be presented in the <code>Authentication</code>
     * request token to the <code>AuthenticationManager</code>
     */
    protected String obtainSmsCode(HttpServletRequest request) {
        return request.getParameter(smsCodeParameter);
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
                              MobileSmsCodeAuthenticationToken authRequest) {
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
     * Sets the parameter name which will be used to obtain the smsCode from the login
     * request..
     *
     * @param smsCodeParameter the parameter name. Defaults to "smsCode".
     */
    public void setsmsCodeParameter(String smsCodeParameter) {
        Assert.hasText(smsCodeParameter, "smsCode parameter must not be empty or null");
        this.smsCodeParameter = smsCodeParameter;
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

    public final String getSmsCodeParameter() {
        return smsCodeParameter;
    }

}
