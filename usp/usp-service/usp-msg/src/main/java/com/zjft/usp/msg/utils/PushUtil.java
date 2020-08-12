package com.zjft.usp.msg.utils;

import cn.hutool.core.util.StrUtil;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.DefaultResult;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.zjft.usp.msg.enums.MessageTypeEnum;
import com.zjft.usp.msg.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


import java.util.*;


/**
 * @Author : zrlin
 * @Date : 2019年9月10日
 * @Desc : 推送工具类
 */

@Slf4j
@Service
public class PushUtil {

    /**
     * 环境: dev、test、prod
     */
    private static String environmentTag;

    @Value("${jgpush.environment-tag}")
    public void setEnvironmentTag(String tag) {
        environmentTag = tag;
        log.debug("environment-tag: " + environmentTag);
    }

    @Value("${jgpush.app-key}")
    private String appKey;

    @Value("${jgpush.master-secret}")
    private String masterSecret;

    public PushResult pushMessage(Message message) {
        JPushClient jpushClient = getClient();
        PushPayload payload = buildPushPayload(message);
        PushResult result = null;
        try {
            result = jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            log.error("Connection error. Should retry later. ", e);
            log.error("Sendno: " + payload.getSendno());
        } catch (APIRequestException e) {
            if (e.getErrorCode() == 1011) {
                log.info("Error Code: " + e.getErrorCode() + " Error Message: " + e.getErrorMessage());
            } else {
                log.error("Error response from JPush server. Should review and fix it. ", e);
                log.error("HTTP Status: " + e.getStatus());
                log.error("Error Code: " + e.getErrorCode() + " Error Message: " + e.getErrorMessage());
                log.info("Msg ID: " + e.getMsgId() + " Sendno: " + payload.getSendno());
            }
        }
        return result;
    }

    public JPushClient getClient() {
        return new JPushClient(masterSecret, appKey);
    }

    public static PushPayload buildPushPayload(Message message) {
        String userIds = message.getUserIds();
        Assert.isTrue(message.getType() > 0, "消息类型不能为空");
        Assert.isTrue(StrUtil.isNotBlank(userIds), "接收人不能为空");

        PushPayload.Builder builder = PushPayload.newBuilder();

        // 通过tag和alias来确定推送接收者
        Audience.Builder audience = Audience.newBuilder();
        // 设置tag来区分生产测试开环境
        log.info("极光推送当前环境Tag为:" + environmentTag);
        audience.addAudienceTarget(AudienceTarget.tag(Arrays.asList(environmentTag)));
        // 设置alias来设置接收者
        if (message.getType() == MessageTypeEnum.MESSAGE_ORDINARY.getCode() || message.getType() == MessageTypeEnum.MESSAGE_TEMPLATE.getCode()) {
            log.info("发送普通消息(1)或者发送模板消息(2)");
            String[] userIdArray = StrUtil.split(userIds, ",");
            if (userIdArray.length > 1) {
                audience.addAudienceTarget(AudienceTarget.alias(Arrays.asList(userIdArray)));
            } else {
                audience.addAudienceTarget(AudienceTarget.alias(userIds));
            }
        } else if (message.getType() == MessageTypeEnum.MESSAGE_NOTICE.getCode()) {
            log.info("发送通知(3)");
            String[] userIdArray = StrUtil.split(userIds, ",");
            audience.addAudienceTarget(AudienceTarget.alias(Arrays.asList(userIdArray)));
        } else if (message.getType() == MessageTypeEnum.MESSAGE_BULLETIN.getCode()) {
            log.info("发送公告(4)，发送给符合Tag的所有人");
        }

        // 构建通知和自定义消息
        builder
                .setPlatform(Platform.all()) // 全平台
                .setAudience(audience.build())
                .setNotification(
                        Notification.newBuilder()
                                .addPlatformNotification(
                                        AndroidNotification
                                                .newBuilder()
                                                //.setTitle(message.getTitle()) //设置标题
                                                .setAlert(message.getTitle()) //设置通知内容,不能用content，content包含html
                                                .setChannelId("com.zjft.anyfix")
                                                .build())
                                .addPlatformNotification(
                                        IosNotification
                                                .newBuilder()
                                                .setAlert(message.getTitle())
                                                .setContentAvailable(true)
                                                .setSound("sound.caf")
                                                .incrBadge(1)
                                                .build())
                                .build())
                .setMessage(
                        cn.jpush.api.push.model.Message.newBuilder()
                                .setTitle(message.getTitle())
                                .setMsgContent(message.getContent())
                                .addExtras(message.getExtraMap()) // 自定义消息的额外字段
                                .build())
                .setOptions(
                        Options.newBuilder()
                                .setApnsProduction(true)
                                .build());
        return builder.build();
    }

    public void setAlias(String registrationId, String alias) {
        JPushClient jPushClient = getClient();
        try {
            DefaultResult result = jPushClient.updateDeviceTagAlias(registrationId, alias, null, null);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        PushUtil pushUtil = new PushUtil();
        Message message = new Message();
        message.setMsgId(10000001L);
        message.setAppId(10001);
        message.setType(2);
        message.setContent("您好！你的服务请求【20200218000001】已登记。");
        Map map = new HashMap();
        map.put("msgid", "11111198822234443");
        map.put("appid", "10001");
        map.put("sendername", "sendername");
        map.put("sendtime", "2020-02-19 15:01:27");
        map.put("title", "您有一个工单需要派工");
//        map.put("content", "工单号1111111 客户名称南京银行 设备大额机");
        map.put("content", "<div style='line-height: 30px;margin-top: 10px;'><span style='display:inline-block;width:25%;color:#999999;font-size:14px;'>工单号</span><span style='font-size:15px;color:#444444;font-weight:500;'>20200218000001</span></div><div style='line-height: 30px;margin-top: 10px;'><span style='display:inline-block;width: 25%;color:#999999;font-size:14px;'>客户名称</span><span style='font-size:15px;color:#444444;font-weight:500;'></span></div><div style='line-height: 30px;margin-top: 10px;'><span style='display:inline-block;width: 25%;color:#999999;font-size:14px;'>设备</span><span style='font-size:15px;color:#444444;font-weight:500;'>${smallClassName}</span></div> ");
        map.put("msgtype", "2");
        map.put("weburl", "weburl");
        map.put("appurl", "appurl");
        map.put("wechaturl", "wechaturl");

        message.setExtraMap(map);
        message.setTitle("您有一个工单需要派工");
        message.setUserIds("1227481848385376257");
        pushUtil.pushMessage(message);

    }
}
