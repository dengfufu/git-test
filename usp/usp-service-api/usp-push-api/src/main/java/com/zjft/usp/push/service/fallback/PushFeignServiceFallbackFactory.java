package com.zjft.usp.push.service.fallback;


import com.zjft.usp.common.model.Result;
import com.zjft.usp.push.service.PushFeignService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author : zrLin
 * @Date : 2019/9/11 10:19
 * @Desc : 推送服务Feign接口实现类
 * @Version 1.0.0
 */
@Slf4j
@Component
public class PushFeignServiceFallbackFactory implements FallbackFactory<PushFeignService> {

    @Override
    public PushFeignService create(Throwable throwable) {
        return new PushFeignService() {
            @Override
            public Result setAlias(String registrationId, String alias) {
                return null;
            }
        };
    }
}
