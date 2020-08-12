package com.zjft.usp.msg.service.impl;


import cn.hutool.core.date.DateUtil;
import com.zjft.usp.msg.mapper.PushBulletinMapper;
import com.zjft.usp.msg.mapper.PushMsgMapper;
import com.zjft.usp.msg.mapper.PushNoticeMapper;
import com.zjft.usp.msg.model.PushBulletin;
import com.zjft.usp.msg.model.PushMsg;
import com.zjft.usp.msg.model.PushNotice;
import com.zjft.usp.msg.service.MessageService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author : zrLin
 * @Date : 2019年8月14日
 * @Desc : 消息数据库实现类
 */
@Service
public class MessageServiceImpl implements MessageService{

    @Resource
    private PushBulletinMapper bulletinMapper;

    @Resource
    private PushMsgMapper msgMapper;

    @Resource
    private PushNoticeMapper noticeMapper;
    
    @Override
    public void addPushBulletin(PushBulletin bulletin) {
        bulletinMapper.insertPushBulletin(bulletin);
    }

    @Override
    public void addPushNotice(PushNotice notice) {
        noticeMapper.insertPushNotice(notice);
    }

    @Override
    public void addPushMsg(PushMsg msg) {
        msgMapper.insertPushMsg(msg);
    }

    @Override
    public void signPushMsg(PushMsg msg) {
        msg.setReceiveTime(DateUtil.date().toTimestamp());
        msgMapper.updatePushMsg(msg);
    }

}
