package com.zjft.usp.mq.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;

/**
 * Kafka消息发送结果处理类
 *
 * @author Qiugm
 * @date 2019-08-06 20:04
 * @version 1.0.0
 **/
@Slf4j
public class KafkaSendResultHandler implements ProducerListener {

    /**
     * 发送成功回调方法
     *
     * @author Qiugm
     * @date 2019-08-13
     * @param producerRecord
     * @param recordMetadata
     * @return void
     */
    @Override
    public void onSuccess(ProducerRecord producerRecord, RecordMetadata recordMetadata) {
        log.info("Message send success : " + producerRecord.toString());
    }

    /**
     * 发送失败回调方法
     *
     * @author Qiugm
     * @date 2019-08-13
     * @param producerRecord
     * @param exception
     * @return void
     */
    @Override
    public void onError(ProducerRecord producerRecord, Exception exception) {
        log.info("Message send error : " + producerRecord.toString());
    }

}
