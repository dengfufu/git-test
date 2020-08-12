package com.zjft.usp.pos.service;

import com.zjft.usp.common.model.Result;
import com.zjft.usp.pos.service.fallback.PosFeignServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 定位feign接口服务类
 *
 * @author hcf
 * @version 1.0
 * @date 2019/11/12 7:51 下午
 **/
@FeignClient(name = "usp-pos", fallbackFactory = PosFeignServiceFallbackFactory.class)
public interface PosFeignService {

     /**
     * 根据定位信息查找附近的用户
     * @param distance
     * @param lon
     * @param lat
     * @return
     */
    @RequestMapping(value = "/position/feign/findNearUsers", method = RequestMethod.POST)
    Result<List<Long>> findNearEngineers(@RequestParam("distance") Integer distance,
                                           @RequestParam("lon") Double lon,
                                           @RequestParam("lat") Double lat);

}
