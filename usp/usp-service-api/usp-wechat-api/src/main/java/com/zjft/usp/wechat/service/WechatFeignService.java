package com.zjft.usp.wechat.service;

import com.zjft.usp.common.model.WxTemplateMessage;
import com.zjft.usp.wechat.service.fallback.WechatFeignServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


/**
 * @author: CK
 * @create: 2019-12-25 15:58
 */
@FeignClient(name = "usp-wechat", fallbackFactory = WechatFeignServiceFallbackFactory.class)
public interface WechatFeignService {

    @RequestMapping(value = "/wx/auto/logon", params = "code", method = RequestMethod.GET)
    String logonByWxCode(@RequestParam("code") String code);

    @RequestMapping(value = "/wx/msg/feign/sendWxMsg", method = RequestMethod.POST)
    String sendWorkMsg(@RequestBody WxTemplateMessage templateMessage);
}
