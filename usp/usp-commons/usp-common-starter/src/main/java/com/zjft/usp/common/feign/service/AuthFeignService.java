package com.zjft.usp.common.feign.service;

import com.zjft.usp.common.feign.service.fallback.AuthFeignServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "usp-auth", fallbackFactory = AuthFeignServiceFallbackFactory.class)
public interface AuthFeignService {

    /**
     * 验证验证码短信
     *
     * @param mobile
     * @param smsCode
     * @return java.lang.String
     * @datetime 2019/9/2 10:16
     * @version
     */
    @PostMapping(value = "/validate/verifySmsCode", params = {"mobile", "smsCode"})
    String verifySmsCode(@RequestParam(value = "mobile") String mobile, @RequestParam(value = "smsCode") String smsCode);

    /**
     * 通过clientId查找客户端信息
     *
     * @param clientId
     * @return
     */
    @RequestMapping(value = "/clients/feign/findByClientId", params = "clientId", method = RequestMethod.GET)
    String getByClientId(@RequestParam("clientId") String clientId);

    /**
     * 重置账号锁定信息
     *
     * @param mobile
     * @return
     */
    @PostMapping(value = "/feign/lock-account/reset", params = "mobile")
    String lockAccountReset(@RequestParam("mobile") String mobile);
}
