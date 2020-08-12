package com.zjft.usp.pay.business.service.feign.fallback;

import com.zjft.usp.common.model.Result;
import com.zjft.usp.pay.business.service.feign.UasFeignService;
import com.zjft.usp.pay.business.service.feign.dto.CorpRegistry;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * @author zphu
 * @date 2019/9/27 14:17
 * @Version 1.0
 **/
@Slf4j
@Component
public class UasFeignServiceFallbackFactory implements FallbackFactory<UasFeignService> {

    @Override
    public UasFeignService create(Throwable cause) {
        return new UasFeignService() {

            @Override
            public Result findCorpById(Long corpId) {
                log.error("usp-uas:feign:findCorpById调用失败");
                return null;
            }

            @Override
            public Map<Long, CorpRegistry> corpIdAndCorpRegistryMap(List<Long> corpIdList) {
                log.error("usp-uas:feign:corpIdAndCorpRegistryMap 调用失败");
                return null;
            }

            /**
             * 远程服务：签订协议
             *
             * @param param
             * @return
             */
            @Override public Result signFeignToB(Map<String, Object> param) {
                log.error("usp-uas:feign:signFeignToB 调用失败");
                return null;
            }

        };
    }
}
