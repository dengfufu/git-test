package com.zjft.usp.anyfix.work.listener;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.zjft.usp.anyfix.common.constant.RightConstants;
import com.zjft.usp.anyfix.common.feign.dto.DeviceSmallClassDto;
import com.zjft.usp.anyfix.corp.manage.service.DemanderCustomService;
import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
import com.zjft.usp.anyfix.work.transfer.enums.WorkHandleTypeEnum;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.request.model.WorkRequest;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.auto.service.WorkDispatchServiceBranchService;
import com.zjft.usp.anyfix.work.request.service.WorkRequestService;
import com.zjft.usp.common.feign.service.RightFeignService;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.device.service.DeviceFeignService;
import com.zjft.usp.mq.util.MqSenderUtil;
import com.zjft.usp.msg.model.TemplateMessage;
import com.zjft.usp.msg.service.PushService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监听自动提交服务商消息
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/11/14 15:35
 */
@Slf4j
@Component
public class AutoWorkDispatchListener implements MqListener {

    @Autowired
    private MqSenderUtil mqSenderUtil;
    @Autowired
    private PushService pushService;
    @Autowired
    private WorkDispatchServiceBranchService workDispatchServiceBranchService;
    @Autowired
    private WorkRequestService workRequestService;
    @Autowired
    private WorkDealService workDealService;
    @Resource
    private DeviceFeignService deviceFeignService;
    @Resource
    private RightFeignService rightFeignService;
    @Resource
    private DemanderCustomService demanderCustomService;

    @Override
    @KafkaListener(topics = {WorkMqTopic.AUTO_DISPATCH_WORK, WorkMqTopic.CUSTOM_DISPATCH_WORK})
    public void receive(ConsumerRecord<String, String> record, Consumer<?, ?> consumer) {
        // 消息内容
        String message = record.value();
        this.autoHandle(message);
        consumer.commitAsync();
    }

    /**
     * 自动分配服务商后，自动受理，分配服务商网点
     *
     * @param message
     * @return
     * @author zgpi
     * @date 2019/11/14 15:39
     **/
    private void autoHandle(String message) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(message);
            String workIdStr = StrUtil.trimToEmpty(jsonObject.getString("workId"));
            if (StrUtil.isBlank(workIdStr) || "0".equals(workIdStr)) {
                log.error("自动分配服务商网点失败，workId参数有误:{}", workIdStr);
                return;
            }
            Long workId = Long.parseLong(workIdStr);
            WorkRequest workRequest = workRequestService.getById(workId);
            WorkDeal workDeal = workDealService.getById(workId);
            if (workRequest == null || workDeal == null) {
                log.error("自动分配服务商网点失败，工单数据不存在");
                return;
            }
            if (workDeal.getWorkStatus() != WorkStatusEnum.TO_HANDLE.getCode()) {
                return;
            }

            Integer handleType = workDispatchServiceBranchService.autoDispatchServiceBranch(workRequest, workDeal);
            if (handleType != null) {
                if (handleType == WorkHandleTypeEnum.AUTO.getCode()) {
                    // 自动分配服务网点成功，进入自动派单
                    Map<String, Object> msg = new HashMap<>(1);
                    msg.put("workId", workRequest.getWorkId());
                    msg.put("handleType", handleType);
                    mqSenderUtil.sendMessage(WorkMqTopic.AUTO_DISPATCH_SERVICE_BRANCH, JsonUtil.toJson(msg));
                }else{
                    // 需要手动受理，提醒服务商客服指定服务网点
                    this.sendHandleMessage(workRequest, workDeal);
                }
            } else {
                // 没有找到服务商网点，提醒服务商客服指定服务网点
                this.sendHandleMessage(workRequest, workDeal);
            }
        } catch (Exception e) {
            log.error("自动分配服务商网点异常", e);
        }
    }

    /**
     * 需要手动受理，发送通知给服务商客服
     *
     * @param workRequest
     * @param workDeal
     * @return
     * @author zgpi
     * @date 2019/11/14 15:03
     **/
    private void sendHandleMessage(WorkRequest workRequest, WorkDeal workDeal) {
        TemplateMessage templateMessage = new TemplateMessage();
        // 消息接收人为服务商受理客服
        Result<List<Long>> authResult = rightFeignService.listUserByRightIdNoSysUser(
                workDeal.getServiceCorp(), RightConstants.WORK_HANDLE);
        if (authResult != null && authResult.getCode() == Result.SUCCESS) {
            List<Long> list = authResult.getData();
            if (CollectionUtil.isEmpty(list)) {
                return;
            }
            templateMessage.setUserIds(StringUtils.collectionToDelimitedString(list, ","));
        } else {
            return;
        }
        templateMessage.setTplName("受理工单");
        templateMessage.setAppId(RightConstants.WORK_APPID);
        Map<String, Object> contentMap = new HashMap<>(1);
        StringBuilder msg = new StringBuilder(32);
        msg.append("您有一个工单[").append(workDeal.getWorkCode()).append("]需要受理，请及时处理！");
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
