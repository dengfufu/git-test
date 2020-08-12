package com.zjft.usp.anyfix.work.listener;

import com.alibaba.fastjson.JSONObject;
import com.zjft.usp.anyfix.work.remind.model.WorkRemindDeal;
import com.zjft.usp.anyfix.work.remind.service.WorkRemindDealService;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.LongUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 监听工单各个节点处理后更新预警处理记录
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-05-15 14:16
 **/

@Slf4j
@Component
public class WorkRemindDealListener implements MqListener {

    @Autowired
    private WorkRemindDealService workRemindDealService;

    @Override
    @KafkaListener(topics = {WorkMqTopic.WORK_REMIND_DEAL})
    public void receive(ConsumerRecord<String, String> record, Consumer<?, ?> consumer) {
        // 消息内容
        String message = record.value();
        this.handle(message);
        consumer.commitAsync();
    }

    /**
     * 更新预警处理记录
     *
     * @param message
     * @return
     * @author Qiugm
     * @date 2020-05-15
     */
    private void handle(String message) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(message);
            Long workId = jsonObject.getLong("workId");
            Integer remindType = jsonObject.getIntValue("remindType");
            Date dealTime = jsonObject.getDate("dealTime");
            if (LongUtil.isNotZero(workId) && IntUtil.isNotZero(remindType)) {
                WorkRemindDeal workRemindDeal = workRemindDealService.findByWorkIdAndType(workId, remindType);
                if (workRemindDeal != null && workRemindDeal.getRemindTime() != null) {
                    if (dealTime.before(workRemindDeal.getRemindTime())) {
                        workRemindDealService.updateEnabled(workRemindDeal.getId(), "N");
                    }
                }
            }
        } catch (Exception e) {
            log.error("更新预警处理记录异常", e);
        }
    }

}
