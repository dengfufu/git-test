package com.zjft.usp.sms.service.fallback;

import com.zjft.usp.sms.service.SmsFeignService;
import com.zjft.usp.sms.vo.Sms;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author : dcyu
 * @Date : 2019/9/2 10:19
 * @Desc : 短信发送Feign接口实现类
 * @Version 1.0.0
 */
@Slf4j
@Component
public class SmsFeignServiceFallbackFactory implements FallbackFactory<SmsFeignService> {
    @Override
    public SmsFeignService create(Throwable cause) {
        return new SmsFeignService() {
            @Override
            public String sendSms(Sms sms) {
                log.error("发送短信失败");
                return null;
            }

            @Override
            public String sendVerifySms(int appId, String phoneNumbers, String verifyCode) {
                log.error("发送验证码短信失败");
                return null;
            }
        };
    }
}
