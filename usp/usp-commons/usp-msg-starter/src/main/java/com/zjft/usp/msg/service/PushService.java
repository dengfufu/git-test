package com.zjft.usp.msg.service;


import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.zjft.usp.mq.util.MqSenderUtil;
import com.zjft.usp.msg.enums.MessageTypeEnum;
import com.zjft.usp.msg.model.Message;
import com.zjft.usp.msg.model.PushTopic;
import com.zjft.usp.msg.model.TemplateMessage;
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
    private MqSenderUtil mqSenderUtil;

    /**
     * 发送普通消息（要作废）
     *
     * @param message
     * @return
     * @author zgpi
     * @date 2019/11/20 15:09
     * @deprecated
     **/
    @Deprecated
    public void pushMessage(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            jsonObject.put("type", MessageTypeEnum.MESSAGE_ORDINARY.getCode());
            mqSenderUtil.sendMessage(PushTopic.PUSH, JSON.toJSONString(jsonObject));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("推送普通消息异常", e);
        }
    }

    /**
     * 发送普通消息
     *
     * @param message
     * @return
     **/
    public void pushMessage(Message message) {
        try {
            message.setType(MessageTypeEnum.MESSAGE_ORDINARY.getCode());
            mqSenderUtil.sendMessage(PushTopic.PUSH, JSON.toJSONString(message));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("推送普通消息异常", e);
        }
    }

    /**
     * 发送模板消息
     *
     * @param message
     * @return
     * @author zgpi
     * @date 2019/11/20 15:09
     **/
    public void pushTemplateMessage(TemplateMessage message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            jsonObject.put("type", MessageTypeEnum.MESSAGE_TEMPLATE.getCode());
            mqSenderUtil.sendMessage(PushTopic.PUSH, JSON.toJSONString(jsonObject));
        } catch (Exception e) {
            log.error("推送模板消息异常", e);
        }
    }

    /**
     * 发送通知消息
     *
     * @param message
     * @return
     * @author zgpi
     * @date 2019/11/20 15:09
     **/
    public void pushNoticeMessage(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            jsonObject.put("type", MessageTypeEnum.MESSAGE_NOTICE.getCode());
            mqSenderUtil.sendMessage(PushTopic.PUSH, JSON.toJSONString(jsonObject));
        } catch (Exception e) {
            log.error("推送通知消息异常", e);
        }
    }

    /**
     * 发送公告消息
     *
     * @param message
     * @return
     * @author zgpi
     * @date 2019/11/20 15:09
     **/
    public void pushBulletinMessage(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            jsonObject.put("type", MessageTypeEnum.MESSAGE_BULLETIN.getCode());
            mqSenderUtil.sendMessage(PushTopic.PUSH, JSON.toJSONString(jsonObject));
        } catch (Exception e) {
            log.error("推送公告消息异常", e);
        }
    }

}

