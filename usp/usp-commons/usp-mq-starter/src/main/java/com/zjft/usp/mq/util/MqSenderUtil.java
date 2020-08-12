package com.zjft.usp.mq.util;

import com.alibaba.fastjson.JSONObject;
import com.zjft.usp.mq.listener.KafkaSendResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * MQ发送工具类
 *
 * @author Qiugm
 * @date 2019-08-12 17:10
 * @version 1.0.0
 **/
public class MqSenderUtil {
    private static Logger log = LoggerFactory.getLogger(MqSenderUtil.class);
    /** 发送模板 */
    private KafkaTemplate kafkaTemplate;
    /** 发送监听器  */
    private KafkaSendResultHandler sendResultHandler;

    public MqSenderUtil(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public MqSenderUtil(KafkaTemplate kafkaTemplate, KafkaSendResultHandler sendResultHandler) {
        this.kafkaTemplate = kafkaTemplate;
        this.sendResultHandler = sendResultHandler;
    }


    /**
     * 同步发送消息
     *
     * @author Qiugm
     * @date 2019-08-06
     * @param topicName
     * @param jsonMessage
     * @return void
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void sendSyncMessage(String topicName, String jsonMessage) throws InterruptedException, ExecutionException {
        log.debug("发送信息");
        try {
            kafkaTemplate.send(topicName, jsonMessage).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("消费成功" + System.currentTimeMillis());
    }

    /**
     * 异步发送消息
     *
     * @author Qiugm
     * @date 2019-08-06
     * @param topicName
     * @param jsonMessage
     * @return void
     */
    public void sendAsyncMessage(String topicName, String jsonMessage) {
        log.debug("发送信息");
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, jsonMessage);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("消费成功" + System.currentTimeMillis());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.debug("消费失败");
                ex.getStackTrace();
            }
        });
    }

    /**
     * 传入kafka约定的topicName,json格式字符串，发送给kafka集群
     *
     * @author Qiugm
     * @date 2019-08-06
     * @param topicName
     * @param jsonMessage
     * @return void
     */
    public void sendMessage(String topicName, String jsonMessage) {
        kafkaTemplate.setProducerListener(sendResultHandler);
        kafkaTemplate.send(topicName, jsonMessage);
    }

    /**
     * 传入kafka约定的topicName,json格式字符串数组，发送给kafka集群
     *
     * @author Qiugm
     * @date 2019-08-06
     * @param topicName
     * @param jsonMessages
     * @return void
     */
    public void sendMessage(String topicName, String... jsonMessages) {
        for (String jsonMessage : jsonMessages) {
            ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate.send(topicName, jsonMessage);
            this.sendCallBack(listenableFuture);
        }
    }

    /**
     * 传入kafka约定的topicName,Map，内部转为json发送给kafka集群
     *
     * @author Qiugm
     * @date 2019-08-06
     * @param topicName
     * @param mapMessageToJson
     * @return void
     */
    public void sendMessage(String topicName, Map<Object, Object> mapMessageToJson) {
        String array = JSONObject.toJSONString(mapMessageToJson);
        ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate.send(topicName, array);
        this.sendCallBack(listenableFuture);
    }


    /**
     * 消息发送回调方法
     *
     * @author Qiugm
     * @date 2019-08-06
     * @param listenableFuture
     * @return void
     */
    private void sendCallBack(ListenableFuture<SendResult<String, String>> listenableFuture) {
        try {
            SendResult<String, String> sendResult = listenableFuture.get();
            listenableFuture.addCallback(successCallBack -> {
                        log.info("kafka Producer发送消息成功！topic=" + sendResult.getRecordMetadata().topic() + ",partition" + sendResult.getRecordMetadata().partition() + ",offset=" + sendResult.getRecordMetadata().offset());
                    },
                    failureCallBack -> log.error("kafka Producer发送消息失败！sendResult=" + JSONObject.toJSONString(sendResult.getProducerRecord())));
        } catch (Exception e) {
            log.error("获取producer返回值失败", e);
        }
    }
}
