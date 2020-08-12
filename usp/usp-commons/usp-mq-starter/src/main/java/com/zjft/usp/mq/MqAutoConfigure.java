package com.zjft.usp.mq;

import com.zjft.usp.mq.listener.KafkaSendResultHandler;
import com.zjft.usp.mq.util.MqSenderUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

/**
 * MQ配置类
 *
 * @author Qiugm
 * @date 2019-08-12 14:49
 * @version 1.0.0
 **/
@EnableConfigurationProperties({KafkaProperties.class})
@EnableCaching
public class MqAutoConfigure {
    @Bean
    public KafkaTemplate kafkaTemplate(ProducerFactory producerFactory) {
        KafkaTemplate kafkaTemplate = new KafkaTemplate(producerFactory);
        return kafkaTemplate;
    }

    @Bean
    public KafkaSendResultHandler kafkaSendResultHandler() {
        KafkaSendResultHandler kafkaSendResultHandler = new KafkaSendResultHandler();
        return kafkaSendResultHandler;
    }

    @Bean
    @ConditionalOnMissingBean
    public MqSenderUtil mqSenderUtil(KafkaTemplate kafkaTemplate, KafkaSendResultHandler kafkaSendResultHandler) {
        return new MqSenderUtil(kafkaTemplate, kafkaSendResultHandler);
    }
}
