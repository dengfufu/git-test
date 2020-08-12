package com.zjft.usp.pay.business.service.feign;

import com.zjft.usp.common.model.Result;
import com.zjft.usp.pay.business.service.feign.dto.SettleDemanderOnlinePaymentDto;
import com.zjft.usp.pay.business.service.feign.fallback.AnyfixFeignServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "usp-anyfix", fallbackFactory = AnyfixFeignServiceFallbackFactory.class)
public interface AnyfixFeignService {

    @PostMapping(value = "/feign/settle-demander-payment/pay-online")
    Result payOnline(@RequestBody SettleDemanderOnlinePaymentDto settleDemanderOnlinePaymentDto);

}


