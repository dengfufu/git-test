package com.zjft.usp.msg.listener;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zjft.usp.msg.config.WebSocketServer;
import com.zjft.usp.msg.enums.MessageTypeEnum;
import com.zjft.usp.msg.model.Message;
import com.zjft.usp.msg.model.TemplateMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author CK
 * @date 2019-11-12 15:13
 */
@Slf4j
@Component
public class MqWebSocketListener implements MqListener {

    // 工单流程提示信息
    private static final String PUSH_TOPIC = "push";

    @Value("${websocket.instance-id}")
    private String instanceId;

    @Override
    @KafkaListener(topics = {PUSH_TOPIC}, groupId = "#{'${websocket.instance-id}'}")
    public void receive(ConsumerRecord<String, String> record, Consumer<?, ?> consumer) {
        String message = record.value();
        String userIds;
        JSONObject jsonObject = JSONObject.parseObject(message);
        int type = jsonObject.getIntValue("type");
        if (type == MessageTypeEnum.MESSAGE_TEMPLATE.getCode()) {
            TemplateMessage templateMessage = JSONObject.parseObject(message, TemplateMessage.class);
            userIds = templateMessage.getUserIds();
        } else {
            Message pushMessage = JSON.toJavaObject(jsonObject, Message.class);
            userIds = pushMessage.getUserIds();
        }
        jsonObject.remove("userIds");
        message = JSONObject.toJSONString(jsonObject);
        if (StrUtil.isNotBlank(userIds)) {
            String[] userIdArray = StrUtil.split(userIds, ",");
            for (String userId : userIdArray) {
                WebSocketServer.sendMessageToOne(Long.parseLong(userId), message);
            }
        }
        //提交 offset
        consumer.commitAsync();
    }
}
