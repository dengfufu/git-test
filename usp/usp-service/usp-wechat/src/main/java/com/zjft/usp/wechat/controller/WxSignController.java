package com.zjft.usp.wechat.controller;

import com.alibaba.fastjson.JSON;
import com.zjft.usp.common.model.Result;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;


/**
 * @author: CK
 * @create: 2019-12-10 11:13
 */
@Slf4j
@RestController
@RequestMapping("wx")
public class WxSignController {

    @Autowired
    private WxMpService wxMpService;

    @RequestMapping(value = "/sign/url", method = RequestMethod.GET)
    public Result getSign(@RequestParam String url) {
        try {
            String uri = java.net.URLDecoder.decode(url,"UTF-8");
            return Result.succeed(wxMpService.createJsapiSignature(uri));
        } catch (WxErrorException | UnsupportedEncodingException e) {
            log.error("错误信息：" + e.toString());
            return Result.failed(e.getMessage());
        }
    }

    @RequestMapping(value = "/auto/logon", method = RequestMethod.GET)
    public String login(@RequestParam String code) throws WxErrorException {
        WxMpOAuth2AccessToken accessToken = wxMpService.oauth2getAccessToken(code);
        log.info(JSON.toJSONString(accessToken));
        return JSON.toJSONString(accessToken);
    }

}
