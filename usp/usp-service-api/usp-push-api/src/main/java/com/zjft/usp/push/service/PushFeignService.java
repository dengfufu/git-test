package com.zjft.usp.push.service;


import com.zjft.usp.common.model.Result;
import com.zjft.usp.push.service.fallback.PushFeignServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "usp-push", path = "/alias", fallbackFactory = PushFeignServiceFallbackFactory.class)
public interface PushFeignService {

    /**
     */
    @PostMapping(value = "/setAlias", params = {"registrationId","alias"})
    Result setAlias(@RequestParam(value = "registrationId") String registrationId, @RequestParam(value = "alias") String alias);
}
