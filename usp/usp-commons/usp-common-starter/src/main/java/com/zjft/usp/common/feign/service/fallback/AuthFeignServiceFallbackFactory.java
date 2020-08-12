package com.zjft.usp.common.feign.service.fallback;

import com.zjft.usp.common.feign.service.AuthFeignService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zphu
 * @date 2019/9/9 19:12
 * @throws
 **/
@Slf4j
@Component
public class AuthFeignServiceFallbackFactory implements FallbackFactory<AuthFeignService> {
    @Override
    public AuthFeignService create(Throwable cause) {
        return new AuthFeignService() {

            /**
             * 验证验证码短信
             *
             * @param mobile
             * @param smsCode
             * @return java.lang.String
             * @datetime 2019/9/2 10:16
             * @version
             */
            @Override
            public String verifySmsCode(String mobile, String smsCode) {
                log.error("verifySmsCode: 远程调用失败");
                return null;
            }

            @Override
            public String getByClientId(String clientId) {
                log.error("getByClientId: 远程调用失败");
                return null;
            }

            @Override
            public String lockAccountReset(String mobile) {
                log.error("lockAccountReset: 远程调用失败");
                return null;
            }
        };
    }
}
