package com.zjft.usp.uas.corp.listener;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * 消息队列消费者监听器接口类
 *
 * @author Qiugm
 * @date 2019-08-13 15:44
 * @version 1.0.0
 **/
public interface MqListener {
    /**
     * 监听消息
     *
     * @author Qiugm
     * @date 2019-08-13
     * @param record
     * @param consumer
     * @return void
     */
    void receive(ConsumerRecord<String, String> record, Consumer<?, ?> consumer);
}
