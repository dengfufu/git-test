package com.zjft.usp.push.service;


import com.gexin.fastjson.JSONObject;

import com.zjft.usp.mq.util.MqSenderUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @Author : zrLin
 * @Date : 2019年8月14日
 * @Desc : 消息推送服务
 */
@Slf4j
public class PushService {

    @Autowired
    private MqSenderUtil mqSenderUtil ;

    protected static final String APP_KEY = "862200571184b945899388b2";
    protected static final String MASTER_SECRET = "8a03c49fa4b6fee38ea5ac96";

    public static String appendReminderMessage(String title, String content, String contentDetail) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);
        jsonObject.put("content", content);
        jsonObject.put("contentDetail", contentDetail);
        jsonObject.put("isNeedNotification", true);
        return jsonObject.toJSONString();
    }

    public static String parseToJSON (String transMessage, boolean isNeedNotification) {
        JSONObject jsonObject = new JSONObject();
        if(isNeedNotification == true) {
            jsonObject.put("isNeedNotification", true);
        }
        jsonObject.put("transMessage", transMessage);
        return jsonObject.toJSONString();
    }

    public void sendToMessageQueue(Object object) {
        try {
            log.info("发送推送消息至短信消息队列开始");
            mqSenderUtil.sendMessage("push", JSONObject.toJSONString(object));
            log.info("发送推送消息至短信消息队列结束");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("推送消息发送其他异常", e);
        }
    }

    public void setAlias(String registration, String userId){

    }

    public void pushMessage(Object message){
        sendToMessageQueue(message);
    }

}

