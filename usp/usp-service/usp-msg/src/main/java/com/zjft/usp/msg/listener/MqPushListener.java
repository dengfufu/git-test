package com.zjft.usp.msg.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zjft.usp.msg.enums.MessageTypeEnum;
import com.zjft.usp.msg.model.Message;
import com.zjft.usp.msg.model.TemplateMessage;
import com.zjft.usp.msg.service.PushMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 短信消息监听
 *
 * @author z
 * @version 1.0.0
 * @date 2019-08-12 20:18
 **/
@Slf4j
@Component
public class MqPushListener implements MqListener {
    private static final String PUSH_TOPIC = "push";

    @Resource
    private PushMessageService pushMessageService;

    /**
     * 监听主题为push-topic的消息
     *
     * @param record
     * @param consumer
     * @return void
     * @author zrlin
     * @date 2019-08-13
     */
    @Override
    @KafkaListener(topics = {PUSH_TOPIC})
    public void receive(ConsumerRecord<String, String> record, Consumer<?, ?> consumer) {
        //主题名称
        String topic = record.topic();
        //消息内容
        String message = record.value();
        try {
            log.info("MQ消费消息: " + message);
            JSONObject jsonObject = JSONObject.parseObject(message);
            int type = jsonObject.getIntValue("type");
            if (type == MessageTypeEnum.MESSAGE_TEMPLATE.getCode()) {
                log.info("MQ消费消息: 模板消息" );
                TemplateMessage tplMessage = JSON.toJavaObject(jsonObject, TemplateMessage.class);
                pushMessageService.pushTplMessage(tplMessage);
            } else {
                log.info("MQ消费消息: 非模板消息" );
                Message pushMessage = JSON.toJavaObject(jsonObject, Message.class);
                pushMessageService.pushMessage(pushMessage);
            }
            //提交 offset
            consumer.commitAsync();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("消息推送服务消费异常", e);
        }
    }
}
