package com.zjft.usp.wechat.service.fallback;

import com.zjft.usp.common.model.LoginAppUser;
import com.zjft.usp.common.model.WxTemplateMessage;
import com.zjft.usp.wechat.service.WechatFeignService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author: CK
 * @create: 2019-12-25 15:58
 */
@Slf4j
@Component
public class WechatFeignServiceFallbackFactory implements FallbackFactory<WechatFeignService> {

    @Override
    public WechatFeignService create(Throwable throwable) {
        return new WechatFeignService() {
            @Override
            public String logonByWxCode(String code) {
                return null;
            }

            @Override
            public String sendWorkMsg(WxTemplateMessage templateMessage) {
                return null;
            }


        };
    }
}
