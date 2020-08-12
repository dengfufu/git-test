package com.zjft.usp.wechat.controller;

import com.alibaba.fastjson.JSON;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.wechat.config.WxUriConfig;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMenuService;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author: CK
 * @create: 2019-12-06 14:27
 */
@Slf4j
@RestController
@RequestMapping("/wx/menu")
public class WxMenuController {

    @Autowired
    private WxMpMenuService wxMpMenuService;

    @Autowired
    private WxMpService wxMpService;

    /**
     * 自定义菜单创建接口
     *
     * @return 如果是个性化菜单，则返回menuId，否则返回null
     */
    @PostMapping("/create")
    public Result menuCreate(@RequestBody WxMenu menu) throws WxErrorException {
        this.wxMpMenuService.menuCreate(menu);
        return Result.succeed("菜单创建成功");
    }

    @GetMapping("/create")
    public Result menuCreateSample(HttpServletRequest request) throws WxErrorException, MalformedURLException, UnsupportedEncodingException {
        URL requestURL = new URL(request.getRequestURL().toString());

        WxMenu menu = new WxMenu();


        WxMenuButton button10 = new WxMenuButton();
        button10.setName("建单");
        
        
        WxMenuButton button11 = new WxMenuButton();
        button11.setType(WxConsts.MenuButtonType.VIEW);
        button11.setName("扫一扫建单");
        String url11 = this.wxMpService.oauth2buildAuthorizationUrl(WxUriConfig.uri + "scan",
                WxConsts.OAuth2Scope.SNSAPI_USERINFO, null);
        button11.setUrl(url11);
        
        WxMenuButton button12 = new WxMenuButton();
        button12.setName("自主建单");
        button12.setType(WxConsts.MenuButtonType.VIEW);
        String url12 = this.wxMpService.oauth2buildAuthorizationUrl(WxUriConfig.uri + "demander-add",
                WxConsts.OAuth2Scope.SNSAPI_USERINFO, null);
        button12.setUrl(url12);

        button10.getSubButtons().add(button11);
        button10.getSubButtons().add(button12);


        WxMenuButton button20 = new WxMenuButton();
        button20.setType(WxConsts.MenuButtonType.VIEW);
        button20.setName("我的工单");
        String url20 = this.wxMpService.oauth2buildAuthorizationUrl(
                WxUriConfig.uri + "work-list",
                WxConsts.OAuth2Scope.SNSAPI_USERINFO, null);
        button20.setUrl(url20);


        WxMenuButton button30 = new WxMenuButton();

        button30.setType(WxConsts.MenuButtonType.VIEW);
        button30.setName("账号");
        String url31 = this.wxMpService.oauth2buildAuthorizationUrl(
                WxUriConfig.uri + "myinfo",
                WxConsts.OAuth2Scope.SNSAPI_USERINFO, null);
        button30.setUrl(url31);


        menu.getButtons().add(button10);
        menu.getButtons().add(button20);
        menu.getButtons().add(button30);

        this.wxMpMenuService.menuCreate(menu);
        return Result.succeed(menu, "菜单创建成功");
    }
}
