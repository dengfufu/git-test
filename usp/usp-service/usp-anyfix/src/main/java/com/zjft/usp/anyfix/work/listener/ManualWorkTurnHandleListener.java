//package com.zjft.usp.anyfix.work.listener;
//
//import cn.hutool.core.util.StrUtil;
//import com.alibaba.fastjson.JSONObject;
//import com.zjft.usp.anyfix.work.transfer.enums.WorkHandleTypeEnum;
//import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
//import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
//import com.zjft.usp.anyfix.work.request.model.WorkRequest;
//import com.zjft.usp.anyfix.work.auto.service.WorkAssignModeService;
//import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
//import com.zjft.usp.anyfix.work.request.service.WorkRequestService;
//import com.zjft.usp.common.model.Result;
//import com.zjft.usp.common.utils.JsonUtil;
//import com.zjft.usp.common.utils.LongUtil;
//import com.zjft.usp.device.dto.DeviceInfoDto;
//import com.zjft.usp.device.service.DeviceFeignService;
//import com.zjft.usp.mq.util.MqSenderUtil;
//import com.zjft.usp.msg.model.TemplateMessage;
//import com.zjft.usp.msg.service.PushService;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.Consumer;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author zgpi
// * @version 1.0
// * @date 2019/11/15 13:38
// */
//@Slf4j
//@Component
//public class ManualWorkTurnHandleListener implements MqListener {
//
//    @Autowired
//    MqSenderUtil mqSenderUtil;
//    @Autowired
//    PushService pushService;
//    @Autowired
//    WorkAssignModeService workAssignModeService;
//    @Autowired
//    WorkRequestService workRequestService;
//    @Autowired
//    WorkDealService workDealService;
//    @Resource
//    DeviceFeignService deviceFeignService;
//
//    @Override
//    @KafkaListener(topics = {WorkMqTopic.SERVICE_TURN_HANDLE_WORK})
//    public void receive(ConsumerRecord<String, String> record, Consumer<?, ?> consumer) {
//        // 消息内容
//        String message = record.value();
//        this.autoAssign(message);
//        consumer.commitAsync();
//    }
//
//    /**
//     * 手动受理后，则进入自动派单
//     *
//     * @param message
//     * @return
//     * @author zgpi
//     * @date 2019/11/14 15:39
//     **/
//    private void autoAssign(String message) {
//        try {
//            JSONObject jsonObject = JSONObject.parseObject(message);
//            String workIdStr = StrUtil.trimToEmpty(jsonObject.getString("workId"));
//            String handleTypeStr = StrUtil.trimToEmpty(jsonObject.getString("handleType"));
//            if (StrUtil.isBlank(workIdStr) || "0".equals(workIdStr)
//                    || StrUtil.isBlank(handleTypeStr) || "0".equals(handleTypeStr)) {
//                log.error("自动派单失败，workId参数有误，workId:{}, handleType:{}", workIdStr, handleTypeStr);
//                return;
//            }
//            Long workId = Long.parseLong(workIdStr);
//            Integer handleType = Integer.parseInt(handleTypeStr);
//            if (handleType != WorkHandleTypeEnum.AUTO.getCode()) {
//                return;
//            }
//            WorkRequest workRequest = workRequestService.getById(workId);
//            WorkDeal workDeal = workDealService.getById(workId);
//            if (workRequest == null || workDeal == null) {
//                log.error("自动派单失败，工单数据不存在");
//                return;
//            }
//            if (workDeal.getWorkStatus() == WorkStatusEnum.TO_ASSIGN.getCode()) {
//                log.error("自动派单失败，工单状态不正确");
//                return;
//            }
//
//            Integer type = workAssignModeService.autoAssign(workRequest, workDeal);
//            // 自动派单失败
//            if (type == null) {
//                // 通知相关人员
//                this.sendMessage(workRequest, workDeal);
//            } else {
//                // 客服派单
//                if (type == 1) {
//                    // 通知相关人员
//                    this.sendMessage(workRequest, workDeal);
//                }
//                // 服务主管派单
//                else if (type == 2) {
//                    this.sendManagerMessage(workDeal);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("自动派单异常", e);
//        }
//    }
//
//    /**
//     * 自动派单失败，发送通知给相关人员，手动派单
//     *
//     * @param workRequest
//     * @param workDeal
//     * @return
//     * @author zgpi
//     * @date 2019/11/14 15:03
//     **/
//    private void sendMessage(WorkRequest workRequest, WorkDeal workDeal) {
//        TemplateMessage templateMessage = new TemplateMessage();
//        //TODO 消息接收人需要改为服务商客服
//        templateMessage.setUserIds(String.valueOf(workRequest.getCreator()));
//        templateMessage.setTplName("工单通知");
//        Map<String, Object> contentMap = new HashMap<>(1);
//        StringBuilder msg = new StringBuilder(32);
//        msg.append("您有一个工单[" + workDeal.getWorkCode() + "]需要分配服务网点，请及时处理！");
//        contentMap.put("msg", msg.toString());
//        templateMessage.setDataMap(contentMap);
//        pushService.pushMessage(JsonUtil.toJson(templateMessage));
//    }
//
//    /**
//     * 自动派单失败，发送通知给设备服务主管，手动派单
//     *
//     * @param workDeal
//     * @return
//     * @author zgpi
//     * @date 2019/11/14 15:03
//     **/
//    private void sendManagerMessage(WorkDeal workDeal) {
//        TemplateMessage templateMessage = new TemplateMessage();
//        Result result = deviceFeignService.findDeviceInfo(workDeal.getDeviceId());
//        DeviceInfoDto deviceInfoDto = JsonUtil.parseObject(JsonUtil.toJson(result.getData()), DeviceInfoDto.class);
//        if(deviceInfoDto != null && LongUtil.isNotZero(deviceInfoDto.getWorkManager())){
//            templateMessage.setUserIds(String.valueOf(deviceInfoDto.getWorkManager()));
//            templateMessage.setTplName("工单通知");
//            Map<String, Object> contentMap = new HashMap<>(1);
//            StringBuilder msg = new StringBuilder(32);
//            msg.append("您有一个工单[" + workDeal.getWorkCode() + "]需要派单，请及时处理！");
//            contentMap.put("msg", msg.toString());
//            templateMessage.setDataMap(contentMap);
//            pushService.pushMessage(JsonUtil.toJson(templateMessage));
//        }
//    }
//}
