package com.zjft.usp.sms.service;

import com.zjft.usp.sms.service.fallback.SmsFeignServiceFallbackFactory;
import com.zjft.usp.sms.vo.Sms;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 短信Feign接口
 * @datetime 2019/9/2 10:29
 * @version
 * @author dcyu
 */
@FeignClient(name = "usp-msg", path = "/sms", fallbackFactory = SmsFeignServiceFallbackFactory.class)
public interface SmsFeignService {

    /**
     * 发送短信（需指定应用类型及短信类型）
     * @datetime 2019/9/2 10:16
     * @version
     * @author dcyu
     * @param sms
     * @return java.lang.String
     */
    @PostMapping(value = "/sendSms")
    String sendSms(@RequestBody Sms sms);

    /**
     * 发送验证码短信
     * @datetime 2019/9/2 10:16
     * @version
     * @author dcyu
     * @param appId
     * @param phoneNumbers
     * @param verifyCode
     * @return java.lang.String
     */
    @PostMapping(value = "/sendVerifySms", params = {"appId","phoneNumbers","verifyCode"})
    String sendVerifySms(@RequestParam(value = "appId") int appId, @RequestParam(value = "phoneNumbers") String phoneNumbers, @RequestParam(value = "verifyCode") String verifyCode);
}
