package com.zjft.usp.anyfix.work.listener;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.zjft.usp.anyfix.common.constant.RightConstants;
import com.zjft.usp.anyfix.common.feign.dto.DeviceSmallClassDto;
import com.zjft.usp.anyfix.corp.manage.service.DemanderCustomService;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.request.composite.WorkRequestCompoService;
import com.zjft.usp.anyfix.work.request.model.WorkRequest;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.auto.service.WorkDispatchServiceCorpService;
import com.zjft.usp.anyfix.work.request.service.WorkRequestService;
import com.zjft.usp.common.feign.service.RightFeignService;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.device.service.DeviceFeignService;
import com.zjft.usp.mq.util.MqSenderUtil;
import com.zjft.usp.msg.model.Message;
import com.zjft.usp.msg.model.TemplateMessage;
import com.zjft.usp.common.model.WxTemplateMessage;
import com.zjft.usp.msg.service.PushService;
import com.zjft.usp.wechat.service.WechatFeignService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监听建单消息
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/11/14 15:33
 */
@Slf4j
@Component
public class WorkCreateListener implements MqListener {

    @Autowired
    MqSenderUtil mqSenderUtil;
    @Autowired
    PushService pushService;
    @Autowired
    WorkRequestService workRequestService;
    @Autowired
    WorkDealService workDealService;
    @Autowired
    WorkDispatchServiceCorpService workDispatchServiceCorpService;
    @Autowired
    WorkRequestCompoService workRequestCompoService;

    @Resource
    private DeviceFeignService deviceFeignService;
    @Resource
    private RightFeignService rightFeignService;
    @Resource
    private DemanderCustomService demanderCustomService;
    @Resource
    private WechatFeignService wechatFeignService;


    @Override
    @KafkaListener(topics = {WorkMqTopic.CREATE_WORK, WorkMqTopic.SCAN_CREATE_WORK, WorkMqTopic.MOD_WORK})
    public void receive(ConsumerRecord<String, String> record, Consumer<?, ?> consumer) {
        // 消息内容
        String message = record.value();

        if(record.topic().equalsIgnoreCase(WorkMqTopic.SCAN_CREATE_WORK)) {
            // TODO
        } else {
            this.autoDispatch(message);
        }
        JSONObject jsonObject = JSONObject.parseObject(message);
        String workIdStr = StrUtil.trimToEmpty(jsonObject.getString("workId"));
        this.sendWxMessage(workIdStr);

        consumer.commitAsync();
    }

    /**
     * 发送微信提醒消息
     * @param workId
     */
    private void sendWxMessage(String workId){
        WxTemplateMessage message = workRequestCompoService.buildWxMessage(workId);
        // 为null代表没有绑定微信用户不需要发送消息
        if (message != null) {
            wechatFeignService.sendWorkMsg(message);
        }
    }
    /**
     * 建单成功后，自动提交服务商
     *
     * @param message
     * @return
     * @author zgpi
     * @date 2019/11/14 15:40
     **/
    private void autoDispatch(String message) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(message);
            String workIdStr = StrUtil.trimToEmpty(jsonObject.getString("workId"));
            if (StrUtil.isBlank(workIdStr) || "0".equals(workIdStr)) {
                log.error("自动提交服务商失败，workId参数有误:{}", workIdStr);
                return;
            }
            Long workId = Long.parseLong(workIdStr);
            WorkRequest workRequest = workRequestService.getById(workId);
            WorkDeal workDeal = workDealService.getById(workId);
            if (workRequest == null || workDeal == null) {
                log.error("自动提交服务商失败，工单数据不存在");
                return;
            }
            Long serviceCorp = workDispatchServiceCorpService.autoGetServiceCorp(workRequest, workDeal);
            if (LongUtil.isNotZero(serviceCorp)) {
                // 自动分配服务商成功，进入自动分配服务网点
                Map<String, Object> msg = new HashMap<>(1);
                msg.put("workId", workRequest.getWorkId());
                mqSenderUtil.sendMessage(WorkMqTopic.AUTO_DISPATCH_WORK, JsonUtil.toJson(msg));
            } else {
                // 没有找到服务商，提醒客户方客服提交服务商
                this.sendMessage(workRequest, workDeal);
            }
        } catch (Exception e) {
            log.error("自动提交服务商异常", e);
        }
    }

    /**
     * 自动分配服务商失败，发送通知给客户，手动分配
     *
     * @param workRequest
     * @param workDeal
     * @return
     * @author zgpi
     * @date 2019/11/14 15:03
     **/
    private void sendMessage(WorkRequest workRequest, WorkDeal workDeal) {
        // 消息接收人为创建人
        Message message = new Message();
        message.setAppId(RightConstants.WORK_APPID);
        message.setUserIds(String.valueOf(workRequest.getCreator()));
        message.setContent("您好！你的服务请求【" + workDeal.getWorkCode() + "】已登记。");
        message.setTitle("你的服务请求已登记");
        pushService.pushMessage(message);

        TemplateMessage templateMessage = new TemplateMessage();
        // 消息接收人为供应商报修客服
        Result<List<Long>> authResult = rightFeignService.listUserByRightId(
                workDeal.getDemanderCorp(), RightConstants.WORK_DISPATCH);
        if (authResult != null && authResult.getCode() == Result.SUCCESS) {
            List<Long> list = authResult.getData();
            if (CollectionUtil.isEmpty(list)) {
                return;
            }
            templateMessage.setUserIds(StringUtils.collectionToDelimitedString(list, ","));
        } else {
            return;
        }
        templateMessage.setTplName("分配工单");
        templateMessage.setAppId(RightConstants.WORK_APPID);
        Map<String, Object> contentMap = new HashMap<>(1);
        StringBuilder msg = new StringBuilder(32);
        msg.append("您有一个工单[").append(workDeal.getWorkCode()).append("]需要提交服务商，请及时处理！");
        contentMap.put("msg", msg.toString());
        this.setContentMap(contentMap, workRequest, workDeal);
        templateMessage.setDataMap(contentMap);
        pushService.pushTemplateMessage(templateMessage);
    }

    /**
     * 填充工单内容
     * @param contentMap
     * @param workRequest
     * @param workDeal
     */
    private void setContentMap(Map<String, Object> contentMap, WorkRequest workRequest, WorkDeal workDeal) {
        contentMap.put("workCode", workDeal.getWorkCode());
        contentMap.put("customCorp", workRequest.getCustomCorp());
        // 获得客户名称
        if (workRequest.getCustomId() != 0) {
            contentMap.put("customCorpName", demanderCustomService.getById(workRequest.getCustomId()).getCustomCorpName());
        } else {
            contentMap.put("customCorpName", "");
        }
        // 远程调用获得设备小类名称
        Result smallClassResult = deviceFeignService.findDeviceSmallClass(workRequest.getSmallClass());
        DeviceSmallClassDto deviceSmallClassDto = null;
        if (smallClassResult.getCode() == Result.SUCCESS) {
            deviceSmallClassDto = JsonUtil.parseObject(JsonUtil.toJson(smallClassResult.getData()), DeviceSmallClassDto.class);
        }
        if (deviceSmallClassDto != null) {
            contentMap.put("smallClassName", StrUtil.trimToEmpty(deviceSmallClassDto.getName()));
        } else {
            contentMap.put("smallClassName", "");
        }
    }
}
