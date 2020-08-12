package com.zjft.usp.anyfix.work.listener;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.zjft.usp.anyfix.common.constant.RightConstants;
import com.zjft.usp.msg.enums.MessageTypeEnum;
import com.zjft.usp.msg.model.Message;
import com.zjft.usp.msg.service.PushService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 监听沟通记录主题信息
 *
 * @author zrlin
 * @version 1.0
 */
@Slf4j
@Component
public class WorkChatListener implements MqListener {

    @Autowired
    private PushService pushService;


    @Override
    @KafkaListener(topics = {WorkMqTopic.WORK_CHAT})
    public void receive(ConsumerRecord<String, String> record, Consumer<?, ?> consumer) {
        // 消息内容
        String message = record.value();
        this.notify(message);
        consumer.commitAsync();
    }

    /**
     * 通知该沟通记录有关人员
     * @param message
     * @return
     * @author zrlin
     **/
    private void notify(String message) {
        JSONObject jsonObject = JSONObject.parseObject(message);
        String workIdStr = StrUtil.trimToEmpty(jsonObject.getString("workId"));
        String workCode = StrUtil.trimToEmpty(jsonObject.getString("workCode"));
        String userIds = StrUtil.trimToEmpty(jsonObject.getString("userIds"));
        if (StrUtil.isBlank(workIdStr) || "0".equals(workIdStr)) {
            log.error("沟通记录提醒，workId参数有误:{}", workIdStr);
            return;
        }
        this.sendMessage(workCode,workIdStr,userIds);
    }

    /**
     * 发送通知
     */
    public void sendMessage(String workCode ,String workId,String userIds) {
        Message message = new Message();
        message.setTitle("工单" + workCode + "沟通记录有更新");
        message.setContent("工单" + workCode+ "沟通记录有更新");
        Map<String,String> extraMap = new HashMap<>();
        extraMap.put("type","CHAT");
        extraMap.put("workId", workId + "");
        extraMap.put("workCode",workCode);
        message.setUserIds(userIds);
        message.setExtraMap(extraMap);
        message.setType(MessageTypeEnum.MESSAGE_ORDINARY.getCode());
        message.setAppId(RightConstants.WORK_APPID);
        pushService.pushMessage(message);
    }
}
