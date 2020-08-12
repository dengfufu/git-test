package com.zjft.usp.anyfix.work.listener;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.zjft.usp.anyfix.common.constant.RightConstants;
import com.zjft.usp.anyfix.common.feign.dto.DeviceSmallClassDto;
import com.zjft.usp.anyfix.corp.manage.service.DemanderCustomService;
import com.zjft.usp.anyfix.corp.user.service.ServiceBranchUserService;
import com.zjft.usp.anyfix.work.assign.service.WorkAssignService;
import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.request.model.WorkRequest;
import com.zjft.usp.anyfix.work.auto.service.WorkAssignModeService;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.request.service.WorkRequestService;
import com.zjft.usp.common.feign.service.RightFeignService;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.device.dto.DeviceInfoDto;
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
 * 监听自动分配服务网点消息
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/11/14 16:44
 */
@Slf4j
@Component
public class AutoWorkDispatchServiceBranchListener implements MqListener {

    @Autowired
    private PushService pushService;
    @Autowired
    private WorkAssignModeService workAssignModeService;
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
    @Resource
    private ServiceBranchUserService serviceBranchUserService;
    @Resource
    private WorkAssignService workAssignService;


    @Override
    @KafkaListener(topics = {WorkMqTopic.AUTO_DISPATCH_SERVICE_BRANCH, WorkMqTopic.SERVICE_HANDLE_WORK,
            WorkMqTopic.SERVICE_TURN_HANDLE_WORK})
    public void receive(ConsumerRecord<String, String> record, Consumer<?, ?> consumer) {
        // 消息内容
        String message = record.value();
        this.autoAssign(message);
        consumer.commitAsync();
    }

    /**
     * 自动分配服务网点后，若是自动受理，则进入自动派单
     *
     * @param message
     * @return
     * @author zgpi
     * @date 2019/11/14 15:39
     **/
    private void autoAssign(String message) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(message);
            String workIdStr = StrUtil.trimToEmpty(jsonObject.getString("workId"));
//            String handleTypeStr = StrUtil.trimToEmpty(jsonObject.getString("handleType"));
//            if (StrUtil.isBlank(workIdStr) || "0".equals(workIdStr)
//                    || StrUtil.isBlank(handleTypeStr) || "0".equals(handleTypeStr)) {
//                log.error("自动派单失败，workId参数有误，workId:{}, handleType:{}", workIdStr, handleTypeStr);
//                return;
//            }
            Long workId = Long.parseLong(workIdStr);
//            Integer handleType = Integer.parseInt(handleTypeStr);
//            if (handleType != WorkHandleTypeEnum.AUTO.getCode()) {
//                return;
//            }
            WorkRequest workRequest = workRequestService.getById(workId);
            WorkDeal workDeal = workDealService.getById(workId);
            if (workRequest == null || workDeal == null) {
                log.error("自动派单失败，工单数据不存在");
                return;
            }
            if (workDeal.getWorkStatus() != WorkStatusEnum.TO_ASSIGN.getCode()) {
//                log.error("自动派单失败，工单状态不正确");
                return;
            }

            Integer type = workAssignModeService.autoAssign(workRequest, workDeal);
            if (type == null) {
                // 自动派单没设置，通知相关人员
                this.sendMessage(workRequest, workDeal);
            } else {
                if (type == 0) {
                    //派单成功，通知工程师
                    workAssignService.assignWorkListener(workRequest.getWorkId());
                } else if (type == 1) {
                    // 通知客服派单
                    this.sendMessage(workRequest, workDeal);
                } else if (type == 2) {
                    // 通知服务主管派单
                    this.sendManagerMessage(workRequest, workDeal);
                } else if (type == 3) {
                    // 通知服务网点客服派单
                    this.sendBranchMessage(workRequest, workDeal);
                } else if (type == -1) {
                    // 自动派单失败
                    this.sendMessage(workRequest, workDeal);
                }
            }
        } catch (Exception e) {
            log.error("自动派单异常", e);
        }
    }

    /**
     * 自动派单失败，发送通知给相关人员，手动派单
     *
     * @param workRequest
     * @param workDeal
     * @return
     * @author zgpi
     * @date 2019/11/14 15:03
     **/
    private void sendMessage(WorkRequest workRequest, WorkDeal workDeal) {
        TemplateMessage templateMessage = new TemplateMessage();
        // 消息接收人为服务商派单客服
        Result<List<Long>> authResult = rightFeignService.listUserByRightIdNoSysUser(
                workDeal.getServiceCorp(), RightConstants.WORK_ASSIGN);
        if (authResult != null && authResult.getCode() == Result.SUCCESS) {
            List<Long> list = authResult.getData();
            if (CollectionUtil.isEmpty(list)) {
                return;
            }
            list.retainAll(serviceBranchUserService.listUserIdsByBranchId(workDeal.getServiceBranch()));
            if (CollectionUtil.isEmpty(list)) {
                return;
            }
            templateMessage.setUserIds(StringUtils.collectionToDelimitedString(list, ","));
        } else {
            return;
        }
        templateMessage.setTplName("工单派工");
        templateMessage.setAppId(RightConstants.WORK_APPID);
        Map<String, Object> contentMap = new HashMap<>(1);
        StringBuilder msg = new StringBuilder(32);
        msg.append("您有一个工单[").append(workDeal.getWorkCode()).append("]需要派单，请及时处理！");
        contentMap.put("msg", msg.toString());
        this.setContentMap(contentMap, workRequest, workDeal);
        templateMessage.setDataMap(contentMap);
        pushService.pushTemplateMessage(templateMessage);
    }

    /**
     * 自动派单失败，发送通知给相关人员，手动派单
     *
     * @param workRequest
     * @param workDeal
     * @return
     **/
    private void sendBranchMessage(WorkRequest workRequest, WorkDeal workDeal) {
        if (LongUtil.isZero(workDeal.getServiceBranch())) {
            return;
        }
        TemplateMessage templateMessage = new TemplateMessage();
        // 消息接收人为服务网点派单客服
        Result<List<Long>> authResult = rightFeignService.listUserByRightIdNoSysUser(
                workDeal.getServiceCorp(), RightConstants.WORK_ASSIGN);
        if (authResult != null && authResult.getCode() == Result.SUCCESS) {
            List<Long> list = authResult.getData();
            if (CollectionUtil.isEmpty(list)) {
                return;
            }
            list.retainAll(serviceBranchUserService.listUserIdsByBranchId(workDeal.getServiceBranch()));
            if (CollectionUtil.isEmpty(list)) {
                return;
            }
            templateMessage.setUserIds(StringUtils.collectionToDelimitedString(list, ","));
        } else {
            return;
        }

        templateMessage.setTplName("工单派工");
        templateMessage.setAppId(RightConstants.WORK_APPID);
        Map<String, Object> contentMap = new HashMap<>(1);
        StringBuilder msg = new StringBuilder(32);
        msg.append("您有一个工单[").append(workDeal.getWorkCode()).append("]需要派单，请及时处理！");
        contentMap.put("msg", msg.toString());
        this.setContentMap(contentMap, workRequest, workDeal);
        templateMessage.setDataMap(contentMap);
        pushService.pushTemplateMessage(templateMessage);
    }

    /**
     * 自动派单失败，发送通知给设备服务主管，手动派单
     *
     * @param workDeal
     * @return
     * @author zgpi
     * @date 2019/11/14 15:03
     **/
    private void sendManagerMessage(WorkRequest workRequest, WorkDeal workDeal) {
        Result result = deviceFeignService.findDeviceInfo(workDeal.getDeviceId());
        DeviceInfoDto deviceInfoDto = JsonUtil.parseObject(JsonUtil.toJson(result.getData()), DeviceInfoDto.class);
        if (deviceInfoDto != null && LongUtil.isNotZero(deviceInfoDto.getWorkManager())) {
            TemplateMessage templateMessage = new TemplateMessage();
            // 消息接收人为服务商设备服务主管
            templateMessage.setUserIds(String.valueOf(deviceInfoDto.getWorkManager()));

            templateMessage.setTplName("工单派工");
            templateMessage.setAppId(RightConstants.WORK_APPID);
            Map<String, Object> contentMap = new HashMap<>(1);
            StringBuilder msg = new StringBuilder(32);
            msg.append("您有一个工单[").append(workDeal.getWorkCode()).append("]需要派单，请及时处理！");
            contentMap.put("msg", msg.toString());
            this.setContentMap(contentMap, workRequest, workDeal);
            templateMessage.setDataMap(contentMap);
            pushService.pushTemplateMessage(templateMessage);
        }
    }

    /**
     * 填充工单内容
     *
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
