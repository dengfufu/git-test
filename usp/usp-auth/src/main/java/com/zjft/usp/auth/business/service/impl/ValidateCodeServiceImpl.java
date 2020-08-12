package com.zjft.usp.auth.business.service.impl;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import com.google.code.kaptcha.Producer;
import com.zjft.usp.auth.business.model.Captcha;
import com.zjft.usp.auth.business.service.ValidateCodeService;
import com.zjft.usp.auth.exception.ValidateCodeException;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.redis.template.RedisRepository;
import com.zjft.usp.sms.service.SmsFeignService;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

/**
 * @author CK
 * @date 2019-08-08 16:41
 */
@Slf4j
@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {

    private static String DEFAULT_CODE_KEY = "usp_auth_captcha_key"; // 默认生成短信验证码过期时间
    private static int DEFAULT_IMAGE_EXPIRE = 600; // 单位秒，10分钟
    private static int DEFAULT_SMS_TIME_EXPIRE = 60; // 单位秒，1分钟

    private static String DEFAULT_IMAGE_CAPTCHA_KEY = "usp_auth_image_captcha_key"; // 图形验证码

    private static int CLIENT_ID = 10001; // TODO

    @Autowired
    private RedisRepository redisRepository;

    @Resource
    SmsFeignService smsFeignService;

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Override
    public Result sendSmsCode(String mobile) {
        Assert.notEmpty(mobile, "手机号不能为空");
        Object tempCodeValue = redisRepository.get(buildKey(mobile));
        if (tempCodeValue != null) {
            Captcha tempSms = (Captcha) tempCodeValue;
            long tempCreateTime = tempSms.getCreateTime();
            long tempCurrentTime = new Date().getTime();
            if (tempCurrentTime - tempCreateTime < DEFAULT_SMS_TIME_EXPIRE * 1000) {
                log.error("用户:{}验证码未失效{}", mobile, tempSms.getCode());
                return Result.failed("验证码发送过于频繁，请60秒后重试");
            }
        }

        String code = RandomUtil.randomNumbers(6);
        long createTime = new Date().getTime();
        Captcha captcha = new Captcha();
        captcha.setCode(code);
        captcha.setCreateTime(createTime);
        log.info("短信发送请求消息中心 -> 手机号:{} -> 验证码：{} -> 创建时间: {}", mobile, code, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        redisRepository.setExpire(buildKey(mobile), captcha, DEFAULT_IMAGE_EXPIRE);
        String result = smsFeignService.sendVerifySms(CLIENT_ID, mobile, code);
        log.info(result);
        return Result.succeed("验证码发送成功");
    }


    @Override
    public void validate(HttpServletRequest request) {
        String deviceId = request.getParameter("mobile");
        if (StringUtils.isBlank(deviceId)) {
            throw new ValidateCodeException("请在请求参数中携带mobile参数");
        }
        String code = this.getCode(deviceId);
        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request, "smsCode");

        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }
        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("请填写验证码");
        }

        if (code == null) {
            throw new ValidateCodeException("验证码不存在或已过期");
        }

        if (!StringUtils.equals(code, codeInRequest.toLowerCase())) {
            throw new ValidateCodeException("验证码不正确");
        }

        this.remove(deviceId);
    }

    @Override
    public Result validate(String mobile, String smsCode) {
        Assert.notEmpty(mobile, "手机号不能为空");
        Assert.notEmpty(smsCode, "验证码不能为空");
        String deviceId = mobile;
        if (StringUtils.isBlank(deviceId)) {
            return Result.failed("请在请求参数中携带mobile参数");
        }
        String code = this.getCode(deviceId);

        if (StringUtils.isBlank(smsCode)) {
            return Result.failed("请填写验证码");
        }

        if (code == null) {
            return Result.failed("验证码不存在或已过期");
        }
        if (!StringUtils.equals(code, smsCode.toLowerCase())) {
            return Result.failed("验证码不正确");
        }
        this.remove(deviceId);
        return Result.succeed();
    }

    @Override
    public String getCode(String deviceId) {
        Object tempCodeValue = redisRepository.get(buildKey(deviceId));
        if (tempCodeValue != null) {
            return ((Captcha) tempCodeValue).getCode();
        }
        return null;
    }

    @Override
    public void remove(String deviceId) {
        redisRepository.del(buildKey(deviceId));
    }

    private String buildKey(String deviceId) {
        return DEFAULT_CODE_KEY + ":" + deviceId;
    }


    //============= 图形验证码
    @Override
    public Result createImageCaptcha() throws IOException {
        String capText = captchaProducer.createText();
        // 前端需要回传
        String captchaId = UUID.randomUUID().toString();
        // 保存到redis
        redisRepository.setExpire(buildImageCaptchaKey(captchaId), capText, DEFAULT_IMAGE_EXPIRE);
        // 生成base64图片
        BufferedImage bufferedImage = captchaProducer.createImage(capText);

        String captchaBase64 = "data:image/jpeg;base64," + ImgUtil.toBase64(bufferedImage,"jpg");

        HashMap<String, Object> temp = new HashMap<>();
        temp.put("captchaId", captchaId);
        temp.put("captchaBase64", captchaBase64);
        return Result.succeed(temp);
    }


    @Override
    public void validateImageCaptcha(HttpServletRequest request) {
        String imageCaptchaId = request.getParameter("captchaId");
        if (StringUtils.isBlank(imageCaptchaId)) {
            log.info("无需验证码登录");
            return;
        }
        // captchaId存在则需要校验验证码
        String captchaCode = this.getImageCaptchaCode(imageCaptchaId);
        String captchaCodeInRequest;
        try {
            captchaCodeInRequest = ServletRequestUtils.getStringParameter(request, "captchaCode");
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }
        if (StringUtils.isBlank(captchaCodeInRequest)) {
            throw new ValidateCodeException("请填写验证码");
        }
        if (captchaCodeInRequest == null) {
            throw new ValidateCodeException("验证码不存在或已过期");
        }
        if (!StringUtils.equals(captchaCode, captchaCodeInRequest.toLowerCase())) {
            throw new ValidateCodeException("验证码不正确");
        }

        redisRepository.del(buildKey(imageCaptchaId));
    }

    private String getImageCaptchaCode(String captchaId) {
        Object tempCodeValue = redisRepository.get(buildImageCaptchaKey(captchaId));
        if (tempCodeValue != null) {
            return (String) tempCodeValue;
        }
        return null;
    }

    private String buildImageCaptchaKey(String captchaId) {
        return DEFAULT_IMAGE_CAPTCHA_KEY + ":" + captchaId;
    }

    public static void main(String[] args) {
        for (int i = 0; i <= 20; i++) {
            System.out.println(UUID.randomUUID().toString());
        }
    }

}
