package com.zjft.usp.gateway.service.fallback;

import com.zjft.usp.common.model.Result;
import com.zjft.usp.gateway.service.UserRightFeignService;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class UserRightFeignServiceFallbackFactory implements FallbackFactory<UserRightFeignService> {
    @Override
    public UserRightFeignService create(Throwable throwable) {

        return new UserRightFeignService() {

            /**
             * 初始化用户企业角色的redis
             *
             * @param userId
             * @param corpId
             * @return
             * @author zgpi
             * @date 2020/6/8 11:15
             **/
            @Override
            public Result initUserCorpRoleRedis(Long userId, Long corpId) {
                return null;
            }

        };
    }
}
