package com.zjft.usp.pos.service.fallback;

import com.zjft.usp.common.model.Result;
import com.zjft.usp.pos.service.PosFeignService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author hcf
 * @version 1.0
 * @date 2019/11/12 7:51 下午
 **/
@Slf4j
@Component
public class PosFeignServiceFallbackFactory implements FallbackFactory<PosFeignService> {
    @Override
    public PosFeignService create(Throwable cause) {
        return new PosFeignService() {

            @Override
            public Result<List<Long>> findNearEngineers(Integer distance, Double lon, Double lat) {
                return null;
            }
        };
    }
}
