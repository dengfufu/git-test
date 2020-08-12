package com.zjft.usp.gateway.service;

import com.zjft.usp.common.model.Result;
import com.zjft.usp.gateway.service.fallback.UserRightFeignServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "usp-uas", fallbackFactory = UserRightFeignServiceFallbackFactory.class)
public interface UserRightFeignService {

    /**
     * 初始化用户企业角色的redis
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/8 11:15
     **/
    @RequestMapping(value = "/sys-role-user/feign/redis-init/corp-role", method = RequestMethod.POST)
    Result initUserCorpRoleRedis(@RequestParam("userId") Long userId,
                                 @RequestParam("corpId") Long corpId);

}
