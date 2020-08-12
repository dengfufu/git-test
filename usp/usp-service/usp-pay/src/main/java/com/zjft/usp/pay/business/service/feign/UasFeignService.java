package com.zjft.usp.pay.business.service.feign;


import com.zjft.usp.common.model.Result;
import com.zjft.usp.pay.business.service.feign.dto.CorpRegistry;
import com.zjft.usp.pay.business.service.feign.fallback.UasFeignServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author CK
 * @date 2020/06/24 13:30
 * @Version 1.0
 **/
@FeignClient(name = "usp-uas", fallbackFactory = UasFeignServiceFallbackFactory.class)
public interface UasFeignService {

    @GetMapping(value = "/corp-registry/feign/{corpId}")
    Result findCorpById(@PathVariable("corpId") Long corpId);

    @PostMapping(value = "/feign/corp-registry/corp-map")
    Map<Long, CorpRegistry> corpIdAndCorpRegistryMap(@RequestBody List<Long> corpIdList);

    /**
     * 远程服务：签订协议
     * @param param
     * @return
     */
    @PostMapping(value = "/protocol/feign/sign-tob")
    Result signFeignToB(@RequestBody Map<String, Object> param);
}
