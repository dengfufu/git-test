package com.zjft.usp.pay.business.service.feign.fallback;

import com.zjft.usp.common.model.Result;
import com.zjft.usp.pay.business.service.feign.AnyfixFeignService;
import com.zjft.usp.pay.business.service.feign.dto.SettleDemanderOnlinePaymentDto;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AnyfixFeignServiceFallbackFactory implements FallbackFactory<AnyfixFeignService> {
    @Override
    public AnyfixFeignService create(Throwable cause) {
        return new AnyfixFeignService() {

            @Override
            public Result payOnline(SettleDemanderOnlinePaymentDto settleDemanderOnlinePaymentDto) {
                log.error("usp-anyfix:feign:payOnline:failed");
                return null;
            }
        };
    }
}
