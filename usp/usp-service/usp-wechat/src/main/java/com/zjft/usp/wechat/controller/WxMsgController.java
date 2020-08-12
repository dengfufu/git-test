package com.zjft.usp.wechat.controller;

import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.WxTemplateMessage;
import com.zjft.usp.wechat.config.WxUriConfig;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("wx/msg")
public class WxMsgController {

    @Autowired
    private WxMpService wxMpService;

    private  static final String  COLOR = "#173177";

    @ApiOperation(value = "远程调用：发送微信模板消息")
    @PostMapping(value = "/feign/sendWxMsg")
    public Result sendWorkMsg(@RequestBody WxTemplateMessage templateMessage) throws WxErrorException {

        WxMpTemplateMessage message = new WxMpTemplateMessage();
        final WxMpConfigStorage config = wxMpService.getWxMpConfigStorage();

        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+config.getAppId()
                + "&redirect_uri=" + WxUriConfig.uri + templateMessage.getRedirectUrl()
                + "&response_type=code&scope=snsapi_userinfo#wechat_redirect";

        message.setTemplateId(templateMessage.getTemplateId());
        message.setUrl(url);
        message.setToUser(templateMessage.getToUser());


        List<WxMpTemplateData> dataList = new ArrayList<>();

        WxMpTemplateData wxMpTemplateData = new WxMpTemplateData();

        StringBuilder titleValue = new StringBuilder(templateMessage.getFirst());
        // uat环境
        if (WxUriConfig.uri.indexOf("dev") > -1) {
            titleValue.append("（UAT测试）");
        }
        wxMpTemplateData.setName("first");
        wxMpTemplateData.setValue(titleValue.toString());
        wxMpTemplateData.setColor(WxMsgController.COLOR);
        dataList.add(wxMpTemplateData);

        for (int i = 0; i < templateMessage.getKeyWordList().size(); i++) {
            WxMpTemplateData keyworkData = new WxMpTemplateData();
            keyworkData.setName("keyword" + (i + 1));
            keyworkData.setValue(templateMessage.getKeyWordList().get(i).toString());
            keyworkData.setColor(WxMsgController.COLOR);
            dataList.add(keyworkData);

        }
        message.setData(dataList);
        wxMpService.getTemplateMsgService().sendTemplateMsg(message);

        return Result.succeed();
    }


}
