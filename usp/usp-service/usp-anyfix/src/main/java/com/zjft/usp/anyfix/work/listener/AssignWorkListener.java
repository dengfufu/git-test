package com.zjft.usp.anyfix.work.listener;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.zjft.usp.anyfix.common.constant.RightConstants;
import com.zjft.usp.anyfix.common.feign.dto.DeviceSmallClassDto;
import com.zjft.usp.anyfix.corp.manage.service.DemanderCustomService;
import com.zjft.usp.anyfix.work.assign.service.WorkAssignEngineerService;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.request.model.WorkRequest;
import com.zjft.usp.anyfix.work.request.service.WorkRequestService;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.device.service.DeviceFeignService;
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
 * 监听派单给工程师成功提醒工程师接单
 *
 * @author ljzhu
 * @version 1.0
 */
@Slf4j
@Component
public class AssignWorkListener implements MqListener {

    @Autowired
    private PushService pushService;
    @Autowired
    private WorkRequestService workRequestService;
    @Autowired
    private WorkDealService workDealService;
    @Resource
    private DeviceFeignService deviceFeignService;
    @Resource
    private DemanderCustomService demanderCustomService;
    @Resource
    private WorkAssignEngineerService workAssignEngineerService;

    @Override
    @KafkaListener(topics = {WorkMqTopic.ASSIGN_WORK})
    public void receive(ConsumerRecord<String, String> record, Consumer<?, ?> consumer) {
        // 消息内容
        String message = record.value();
        this.autoAssign(message);
        consumer.commitAsync();
    }

    /**
     * 提醒工程师接单
     *
     * @param message
     * @return
     * @author ljzhu
     **/
    private void autoAssign(String message) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(message);
            String workIdStr = StrUtil.trimToEmpty(jsonObject.getString("workId"));

            Long workId = Long.parseLong(workIdStr);

            WorkRequest workRequest = workRequestService.getById(workId);
            WorkDeal workDeal = workDealService.getById(workId);
            if (workRequest == null || workDeal == null) {
                log.error("提醒工程师接单失败，工单数据不存在");
                return;
            }
            //派单成功，通知工程师
            List<Long> list = workAssignEngineerService.listEngineerByWorkId(workRequest.getWorkId());
            this.sendEngineerMessage(workRequest, workDeal,list);

        } catch (Exception e) {
            log.error("自动派单异常", e);
        }
    }

    /**
     * 提醒工程师
     * @param workRequest
     * @param workDeal
     * @param engineerList
     */
    public void sendEngineerMessage(WorkRequest workRequest, WorkDeal workDeal,List<Long> engineerList) {
        TemplateMessage templateMessage = new TemplateMessage();
        // 消息接收人为工程师
        templateMessage.setUserIds(StringUtils.collectionToDelimitedString(engineerList, ","));
        templateMessage.setTplName("工单接单");
        templateMessage.setAppId(RightConstants.WORK_APPID);
        Map<String, Object> contentMap = new HashMap<>(1);
        StringBuilder msg = new StringBuilder(32);
        msg.append("您收到一个工单[").append(workDeal.getWorkCode()).append("]待接受，请及时处理！");
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
    public void setContentMap(Map<String, Object> contentMap, WorkRequest workRequest, WorkDeal workDeal) {
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
