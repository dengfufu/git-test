package com.zjft.usp.wechat.controller;

import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;

/**
 * @author: CK
 * @create: 2019-12-09 10:46
 */
@Controller
@RequestMapping("/wx/redirect")
public class WxRedirectController {

    @Autowired
    private WxMpService wxMpService;

    @RequestMapping("/sdk")
    public String login(HttpServletRequest request, Model model) throws WxErrorException, MalformedURLException {
        String fullURL = request.getRequestURL().toString();
        WxJsapiSignature wxJsapiSignature = wxMpService.createJsapiSignature(fullURL);
        model.addAttribute("jsapi", wxJsapiSignature);
        return "js_sdk";
    }

    @RequestMapping("/greet")
    public String greetUser(@RequestParam String code, ModelMap map) {
        try {
            WxMpOAuth2AccessToken accessToken = wxMpService.oauth2getAccessToken(code);
            WxMpUser user = wxMpService.oauth2getUserInfo(accessToken, null);
            map.put("user", user);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        return "greet_user";
    }
}
