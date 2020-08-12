package com.zjft.usp.msg.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.jpush.api.push.PushResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.msg.dto.MsgFilter;
import com.zjft.usp.msg.enums.MessageTypeEnum;
import com.zjft.usp.msg.mapper.PushMsgMapper;
import com.zjft.usp.msg.model.*;
import com.zjft.usp.msg.service.MessageService;
import com.zjft.usp.msg.service.PushMessageService;
import com.zjft.usp.msg.utils.PushUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.annotation.Resource;
import java.util.*;


/**
 * @Author : zrlin
 * @Date : 2019年8月 23日 15:20:30
 * @Desc : 消息发送服务类
 */
@Slf4j
@Service
public class PushMessageServiceImpl extends ServiceImpl<PushMsgMapper, PushMsg> implements PushMessageService {

    @Resource
    private PushTemplateServiceImpl pushTemplateServiceImpl;

    @Resource
    private MessageService messageService;

    @Resource
    private PushMsgMapper pushMsgMapper;

    @Resource
    private PushUtil pushUtil;

    /**
     * 发送普通消息
     *
     * @param message
     * @return
     * @author zgpi
     * @date 2019/11/20 09:05
     **/
    @Override
    public void pushMessage(Message message) {
        message = this.convertToMessage(message);
        PushResult result = pushUtil.pushMessage(message);
        insertMessage(message, result != null ? Long.toString(result.msg_id) : "");
    }

    /**
     * 发送模板消息
     *
     * @param tplMessage
     * @return
     * @author zgpi
     * @date 2019/11/20 09:05
     **/
    @Override
    public void pushTplMessage(TemplateMessage tplMessage) {
        Message message = this.convertTemplateToMessage(tplMessage);
        PushResult result = pushUtil.pushMessage(message);
        insertMessage(message, result != null ? Long.toString(result.msg_id) : "");
    }

    /**
     * 发送通知
     *
     * @param message
     * @return
     * @author zgpi
     * @date 2019/11/20 10:45
     **/
    @Override
    public void pushNotice(Message message) {
        message = this.convertMessageToNotice(message);
        PushResult result = pushUtil.pushMessage(message);
        insertMessage(message, result != null ? Long.toString(result.msg_id) : "");
    }

    /**
     * 发送公告
     *
     * @param message
     * @return
     * @author zgpi
     * @date 2019/11/20 10:45
     **/
    @Override
    public void pushBulletin(Message message) {
        message = this.convertMessageToBulletin(message);
        PushResult result = pushUtil.pushMessage(message);
        insertMessage(message, result != null ? Long.toString(result.msg_id) : "");
    }

    /**
     * 设置别名
     *
     * @param registrationId
     * @param alias
     * @return
     * @author zgpi
     * @date 2019/11/20 09:05
     **/
    @Override
    public void setAlias(String registrationId, String alias) {
        pushUtil.setAlias(registrationId, alias);
    }

    /**
     * 分页查询消息
     *
     * @param msgFilter
     * @return
     * @author zgpi
     * @date 2019/11/20 09:06
     **/
    @Override
    public ListWrapper<PushMsg> query(MsgFilter msgFilter) {
        List<PushMsg> pushMsgList;
        try {
            pushMsgList = this.pushMsgMapper.queryList(msgFilter);
        } catch (Exception e) {
            if (e.getMessage().contains("push_msg_") && e.getMessage().contains("doesn't exist")) {
                this.createPushMsgTable();
                return ListWrapper.<PushMsg>builder()
                        .list(new ArrayList<>())
                        .total(0L)
                        .build();
            } else {
                throw e;
            }
        }
        Long total = this.pushMsgMapper.queryCount(msgFilter);
        return ListWrapper.<PushMsg>builder()
                .list(pushMsgList)
                .total(total)
                .build();
    }

    @Override
    public List<PushMsg> listUnSend(Long userId) {
        List<String> yearMonthList = new ArrayList<>();
        String thisMonth = DateUtil.format(DateUtil.date(), "yyMM");
        DateTime preMonthTime = DateUtil.offsetMonth(DateUtil.date(), -1);
        String preMonth = DateUtil.format(preMonthTime, "yyMM");
        DateTime preMonthTime2 = DateUtil.offsetMonth(DateUtil.date(), -2);
        String preMonth2 = DateUtil.format(preMonthTime2, "yyMM");
        yearMonthList.add(thisMonth);
        yearMonthList.add(preMonth);
        yearMonthList.add(preMonth2);
        try {
            return this.pushMsgMapper.listUnSend(userId, yearMonthList);
        } catch (Exception e) {
            if (e.getMessage().contains("push_msg_") && e.getMessage().contains("doesn't exist")) {
                this.createPushMsgTable();
                return new ArrayList<>();
            } else {
                throw e;
            }
        }
    }

    /**
     * 将普通消息添加到extra中重新转换成普通消息
     *
     * @param message
     * @return
     * @author zgpi
     * @date 2019/11/20 09:05
     **/
    private Message convertToMessage(Message message) {
        message.setMsgId(KeyUtil.getId());
        message.setType(MessageTypeEnum.MESSAGE_ORDINARY.getCode());
        Map<String, String> extraMap ;
        if(message.getExtraMap() != null) {
            extraMap = message.getExtraMap();
        } else {
            extraMap = new HashMap<>(5);
        }
        extraMap.put("msgid", String.valueOf(message.getMsgId()));
        extraMap.put("msgtype", String.valueOf(message.getType()));
        extraMap.put("title", message.getTitle());
        extraMap.put("content", message.getContent());
        extraMap.put("sendtime", DateUtil.now());
        message.setExtraMap(extraMap);
        return message;
    }

    /**
     * 将普通消息添加到extra中重新转换成通知消息
     *
     * @param message
     * @return
     * @author zgpi
     * @date 2019/11/20 09:05
     **/
    private Message convertMessageToNotice(Message message) {
        message.setMsgId(KeyUtil.getId());
        message.setType(MessageTypeEnum.MESSAGE_NOTICE.getCode());
        Map<String, String> extraMap = new HashMap<>(5);
        extraMap.put("msgid", String.valueOf(message.getMsgId()));
        extraMap.put("msgtype", String.valueOf(message.getType()));
        extraMap.put("title", message.getTitle());
        extraMap.put("content", message.getContent());
        extraMap.put("sendtime", DateUtil.now());
        message.setExtraMap(extraMap);
        return message;
    }

    /**
     * 将普通消息添加到extra中重新转换成公告消息
     *
     * @param message
     * @return
     * @author zgpi
     * @date 2019/11/20 09:05
     **/
    private Message convertMessageToBulletin(Message message) {
        message.setMsgId(KeyUtil.getId());
        message.setType(MessageTypeEnum.MESSAGE_BULLETIN.getCode());
        Map<String, String> extraMap = new HashMap<>(5);
        extraMap.put("msgid", String.valueOf(message.getMsgId()));
        extraMap.put("msgtype", String.valueOf(message.getType()));
        extraMap.put("title", message.getTitle());
        extraMap.put("content", message.getContent());
        extraMap.put("sendtime", DateUtil.now());
        message.setExtraMap(extraMap);
        return message;
    }

    /**
     * 将模板消息添加到extra中重新转换成普通消息
     *
     * @param tplMessage
     * @return
     * @author zgpi
     * @date 2019/11/20 09:05
     **/
    private Message convertTemplateToMessage(TemplateMessage tplMessage) {
        PushTemplate template = pushTemplateServiceImpl.
                findPushTemplate(tplMessage.getAppId(), tplMessage.getTplName());
        Assert.isTrue(template != null, "找不到消息模板");

        Map<String, Object> contentMap = tplMessage.getDataMap();
        contentMap = contentMap == null ? new HashMap<>(0) : contentMap;
        // 3. 使用Iterator遍历
        Iterator<Map.Entry<String, Object>> it = contentMap.entrySet().iterator();
        String content = template.getContent();
        String title = template.getTitle();
        log.info("模板消息添加到extra中重新转换成普通消息");
        log.info("contentMap: {}", contentMap);
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            log.info("entry key{}、value:{} ", entry.getKey(), entry.getValue());
            content = content.replace("${" + entry.getKey() + "}", String.valueOf(entry.getValue()));
            title = title.replace("${" + entry.getKey() + "}", String.valueOf(entry.getValue()));
            log.info("content: " + content);
            log.info("title: " + title);
        }
        Message message = new Message();
        message.setAppId(tplMessage.getAppId());
        message.setTitle(title);
        message.setContent(content);
        message.setUserIds(tplMessage.getUserIds());
        message.setMsgId(KeyUtil.getId());
        message.setType(MessageTypeEnum.MESSAGE_TEMPLATE.getCode());

        Map<String, String> extraMap = new HashMap<>(7);
        extraMap.put("appid", String.valueOf(tplMessage.getAppId()));
        extraMap.put("msgid", String.valueOf(message.getMsgId()));
        extraMap.put("msgtype", String.valueOf(message.getType()));
        extraMap.put("title", title);
        extraMap.put("content", content);
        extraMap.put("sendtime", DateUtil.now());
        extraMap.put("weburl", tplMessage.getWebUrl());
        extraMap.put("appurl", tplMessage.getAppUrl());
        extraMap.put("wechaturl", tplMessage.getWechatUrl());
        message.setExtraMap(extraMap);
        return message;
    }

    /**
     * 保存消息到数据库
     *
     * @param message
     * @param receiptId
     * @return
     * @author zgpi
     * @date 2019/11/20 09:05
     **/
    private void insertMessage(Message message, String receiptId) {
        // 发送普通消息/模板消息
        if (message.getType() == MessageTypeEnum.MESSAGE_ORDINARY.getCode()
                || message.getType() == MessageTypeEnum.MESSAGE_TEMPLATE.getCode()) {
            List<PushMsg> list = convertToPushMsg(message, receiptId);
            for (PushMsg pushMsg : list) {
                try {
                    this.messageService.addPushMsg(pushMsg);
                } catch (Exception e) {
                    if (e.getMessage().contains("push_msg_") && e.getMessage().contains("doesn't exist")) {
                        this.createPushMsgTable();
                        this.messageService.addPushMsg(pushMsg);
                    } else {
                        throw e;
                    }
                }
            }
        }
        // 发给通知消息
        else if (message.getType() == MessageTypeEnum.MESSAGE_NOTICE.getCode()) {
            PushNotice pushMsg = convertToNotice(message, receiptId);
            this.messageService.addPushNotice(pushMsg);
        }
        // 发送公告
        else if (message.getType() == MessageTypeEnum.MESSAGE_BULLETIN.getCode()) {
            PushBulletin pushMsg = convertToBulletin(message, receiptId);
            this.messageService.addPushBulletin(pushMsg);
        }
    }

    /**
     * 将消息转换为发送消息类
     *
     * @param message
     * @param receiptId
     * @return
     * @author zgpi
     * @date 2019/11/20 09:23
     **/
    private List<PushMsg> convertToPushMsg(Message message, String receiptId) {
        List<PushMsg> list = new ArrayList<>();
        String[] userIdArray = StrUtil.split(message.getUserIds(), ",");
        for (String userId : userIdArray) {
            if (!StrUtil.isEmpty(userId)) {
                PushMsg pushMsg = new PushMsg();
                pushMsg.setTouser(Long.parseLong(userId));
                pushMsg.setAppId(message.getAppId());
                pushMsg.setMsgId(message.getMsgId());
                pushMsg.setTitle(message.getTitle());
                pushMsg.setContent(message.getContent());
                pushMsg.setSentTime(DateUtil.date().toTimestamp());
                pushMsg.setSendStatus(PushConstants.SEND);
                pushMsg.setReceiptId(receiptId);
                list.add(pushMsg);
            }
        }
        return list;
    }

    /**
     * 将消息转换为发送通知类
     *
     * @param message
     * @param receiptId
     * @return
     * @author zgpi
     * @date 2019/11/20 09:23
     **/
    private PushNotice convertToNotice(Message message, String receiptId) {
        PushNotice pushNotice = new PushNotice();
        pushNotice.setAppId(message.getAppId());
        pushNotice.setMsgId(message.getMsgId());
        pushNotice.setTitle(message.getTitle());
        pushNotice.setContent(message.getContent());
        pushNotice.setSentTime(DateUtil.date().toTimestamp());
        pushNotice.setTousers(message.getUserIds());
        pushNotice.setSendStatus(PushConstants.SEND);
        pushNotice.setReceiptId(receiptId);
        return pushNotice;
    }

    /**
     * 将消息转换为发送公告类
     *
     * @param message
     * @param receiptId
     * @return
     * @author zgpi
     * @date 2019/11/20 09:23
     **/
    private PushBulletin convertToBulletin(Message message, String receiptId) {
        PushBulletin bulletin = new PushBulletin();
        bulletin.setMsgId(message.getMsgId());
        bulletin.setAppId(message.getAppId());
        bulletin.setTitle(message.getTitle());
        bulletin.setContent(message.getContent());
        bulletin.setSentTime(DateUtil.date().toTimestamp());
        bulletin.setSendStatus(PushConstants.SEND);
        bulletin.setReceiptId(receiptId);
        return bulletin;
    }

    /***
     * 定时创建单点消息记录表
     * @return void
     */
    @Override
    public void createPushMsgTable() {
        Date startDate = new Date();
        int months = 3;
        for (int i = 0; i < months; i++) {
            String sortNo = DateUtil.format(DateUtils.addMonths(startDate, i), "yyMM");
            String tableName = "push_msg_" + sortNo;
            if (!this.existTable(tableName)) {
                this.pushMsgMapper.createTable(tableName);
            }
        }
    }

    /***
     * 判断表是否存在
     *
     * @author Qiugm
     * @date 2019/9/2
     * @param tableName
     * @return boolean
     */
    private boolean existTable(String tableName) {
        if (this.pushMsgMapper.existTable(tableName) > 0) {
            return true;
        }
        return false;
    }
}
