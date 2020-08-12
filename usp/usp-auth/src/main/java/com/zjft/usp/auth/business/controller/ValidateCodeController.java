package com.zjft.usp.auth.business.controller;

import com.zjft.usp.auth.business.service.ValidateCodeService;
import com.zjft.usp.common.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 验证码 TODO
 *
 * @author CK
 * @date 2019-07-30 14:46
 */
@RestController
@Slf4j
public class ValidateCodeController {

    @Autowired
    ValidateCodeService validateCodeService;

    @GetMapping("/validate/imageCaptcha")
    public Result createImageCaptcha() throws IOException {
        return validateCodeService.createImageCaptcha();
    }

    /**
     * 发送手机验证码
     * 后期要加接口限制
     *
     * @param mobile
     * @return
     */
    @PostMapping("/validate/sms-code")
    public Result createCode(@RequestParam(value = "mobile") String mobile) {
        return this.validateCodeService.sendSmsCode(mobile);
    }

    /**
     * 验证手机验证码
     *
     * @param mobile
     * @return
     */
    @PostMapping("/validate/verifySmsCode")
    public Result verifySmsCode(@RequestParam(value = "mobile") String mobile, @RequestParam(value = "smsCode") String smsCode) {
        return this.validateCodeService.validate(mobile, smsCode);
    }
}

