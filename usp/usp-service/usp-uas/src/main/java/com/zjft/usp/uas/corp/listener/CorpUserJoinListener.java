package com.zjft.usp.uas.corp.listener;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.zjft.usp.msg.model.TemplateMessage;
import com.zjft.usp.msg.service.PushService;
import com.zjft.usp.uas.common.constant.RightConstants;
import com.zjft.usp.uas.corp.dto.CorpDto;
import com.zjft.usp.uas.corp.model.CorpUserApp;
import com.zjft.usp.uas.corp.service.CorpRegistryService;
import com.zjft.usp.uas.corp.service.CorpUserAppService;
import com.zjft.usp.uas.right.service.SysRoleRightService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监听人员加入企业申请提醒审核人处理
 * @author ljzhu
 * @version 1.0
 */
@Slf4j
@Component
public class CorpUserJoinListener implements MqListener {

    @Autowired
    private PushService pushService;
    @Autowired
    private CorpUserAppService corpUserAppService;
    @Autowired
    private CorpRegistryService corpRegistryService;
    @Autowired
    private SysRoleRightService sysRoleRightService;
    @Override
    @KafkaListener(topics = {CorpMqTopic.CORP_USER_JOIN})
    public void receive(ConsumerRecord<String, String> record, Consumer<?, ?> consumer) {
        // 消息内容
        String message = record.value();
        this.remindCorpUserJoin(message);
        consumer.commitAsync();
    }

    /**
     * 提醒审核人处理
     * @param message
     * @return
     * @author ljzhu
     **/
    private void remindCorpUserJoin(String message) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(message);
            String userIdStr = StrUtil.trimToEmpty(jsonObject.getString("userId"));
            String corpIdStr = StrUtil.trimToEmpty(jsonObject.getString("corpId"));

            Long userId = Long.parseLong(userIdStr);
            Long corpId = Long.parseLong(corpIdStr);

            CorpUserApp corpUserApp = corpUserAppService.getApplyingApp(userId,corpId);

            if (corpUserApp == null) {
                log.error("加入企业申请提醒审核人处理失败，数据不存在");
                return;
            }

            /**
             * 根据权限点和客户编号查询有权限的人员编号
             */
            List<Long> checkList = sysRoleRightService.listUserByRightId(Long.valueOf(RightConstants.CORP_USER_CHECK),corpId);
            if(checkList != null && checkList.size() > 0){
                this.sendAdminMessage(corpUserApp,checkList);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("人员加入企业申请提醒审核人处理异常", e);
        }
    }

    /**
     * 提醒管理员
     * @param corpUserApp
     * @param adminUseridList
     */
    public void sendAdminMessage(CorpUserApp corpUserApp,List<Long> adminUseridList) {
        TemplateMessage templateMessage = new TemplateMessage();
        // 消息接收人为admin
        templateMessage.setUserIds(StringUtils.collectionToDelimitedString(adminUseridList, ","));
        templateMessage.setTplName("加入企业");
        templateMessage.setAppId(RightConstants.UAS_APPID);
        Map<String, Object> contentMap = new HashMap<>(1);
        StringBuilder msg = new StringBuilder(32);

        CorpDto corpInfo = corpRegistryService.getCorpInfoWithAddress(corpUserApp.getCorpId());
        msg.append("您收到一个名为[").append(corpUserApp.getUserName()).append("]的人员加入企业[").
                append(corpInfo.getCorpName()).append("]的申请待接受，请及时处理！");
        contentMap.put("msg", msg.toString());
        this.setContentMap(contentMap, corpUserApp, corpInfo.getCorpName());
        templateMessage.setDataMap(contentMap);
        pushService.pushTemplateMessage(templateMessage);
    }

    /**
     *
     * 填充内容
     * @param contentMap
     * @param corpUserApp
     * @param corpName
     */
    public void setContentMap(Map<String, Object> contentMap,CorpUserApp corpUserApp,String corpName) {
        contentMap.put("userName", StrUtil.trimToEmpty(corpUserApp.getUserName()));
        contentMap.put("corpName", StrUtil.trimToEmpty(corpName));
    }


}
