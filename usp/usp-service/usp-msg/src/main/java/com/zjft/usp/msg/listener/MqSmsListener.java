package com.zjft.usp.msg.listener;

import com.alibaba.fastjson.JSONObject;
import com.zjft.usp.msg.model.Sms;
import com.zjft.usp.msg.service.SmsSendService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 短信消息监听
 *
 * @author Qiugm
 * @date 2019-08-12 20:18
 * @version 1.0.0
 **/
@Component
public class MqSmsListener implements MqListener {
    private static final Logger log = LoggerFactory.getLogger(MqSmsListener.class);
    public static final String SMS_TOPIC = "sms-topic";

    @Autowired
    private SmsSendService smsSendService;
    /**
     * 监听主题为sms-topic的消息
     *
     * @author Qiugm
     * @date 2019-08-13
     * @param record
     * @param consumer
     * @return void
     */
    @Override
    @KafkaListener(topics = {SMS_TOPIC})
    public void receive(ConsumerRecord<String, String> record, Consumer<?, ?> consumer) {
        log.info("topic:{},key: {},partition:{}, value: {}, record: {}", record.topic(), record.key(), record.partition(), record.value(), record);
        //主题名称
        String topic = record.topic();
        //消息内容
        String message = record.value();
        //
        try{
            Sms sms = JSONObject.parseObject(message, Sms.class);
            log.info("短信发送服务消费开始");
            smsSendService.consumeSmsFromQueue(sms);
            log.info("短信发送服务消费结束");
        }catch(Exception e){
            e.printStackTrace();
            log.error("短信发送服务消费异常", e);
        }
        //提交 offset
        consumer.commitAsync();
    }

}
