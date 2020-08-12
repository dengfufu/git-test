package com.zjft.usp.auth.business.service;

import com.zjft.usp.common.model.Result;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author CK
 * @date 2019-08-08 16:40
 */
public interface ValidateCodeService {


    /**
     * 发送短信验证码
     *
     * @param mobile
     * @return
     */
    Result sendSmsCode(String mobile);

    /**
     * 验证验证码
     *
     * @param request
     */
    void validate(HttpServletRequest request);

    /**
     * 验证验证码
     *
     * @param mobile
     * @param smsCode
     * @return java.lang.Boolean
     * @throws
     * @author zphu
     * @date 2019/9/5 15:22
     **/
    Result validate(String mobile, String smsCode);

    /**
     * 获取验证码
     *
     * @param deviceId 前端唯一标识/手机号
     * @return
     */
    String getCode(String deviceId);

    /**
     * 删除验证码
     *
     * @param deviceId 前端唯一标识/手机号
     */
    void remove(String deviceId);

    /**
     * 生成图形验证码
     *
     * @return
     */
    Result createImageCaptcha() throws IOException;

    /**
     * 校验图形验证码
     *
     * @param request
     */
    void validateImageCaptcha(HttpServletRequest request);

}
