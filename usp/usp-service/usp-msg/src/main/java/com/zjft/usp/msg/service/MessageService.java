package com.zjft.usp.msg.service;


import com.zjft.usp.msg.model.PushBulletin;
import com.zjft.usp.msg.model.PushMsg;
import com.zjft.usp.msg.model.PushNotice;

/**
 * @Author : zrLin
 * @Date : 2019年8月14日
 * @Desc : 消息数据库服务类
 */
public interface MessageService {

    void addPushBulletin(PushBulletin bulletin);

    void addPushNotice(PushNotice notice);

    void addPushMsg(PushMsg msg);

    void signPushMsg(PushMsg msg);

}
