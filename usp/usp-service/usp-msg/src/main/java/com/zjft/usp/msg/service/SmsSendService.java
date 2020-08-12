package com.zjft.usp.msg.service;

import com.zjft.usp.msg.model.Sms;
import com.zjft.usp.msg.model.SmsResult;

/**
 * 短信发送服务接口类
 * @datetime 2019/8/27 14:20
 * @version
 * @author dcyu
 */
public interface SmsSendService {

    /**
     * 生产验证码短信消息
     * @datetime 2019/8/27 14:26
     * @version
     * @author dcyu
     * @param appId
     * @param phoneNumbers
     * @param verifyCode
     * @return java.lang.String
     */
    void productVerifySmsToQueue(int appId, String phoneNumbers, String verifyCode);

    /**
     * 生产短信消息
     * @datetime 2019/8/27 13:48
     * @version
     * @author dcyu
     * @param sms
     * @return java.lang.String
     */
    void productSmsToQueue(Sms sms);

    /**
     * 消费消息队列 发送短信
     * @param sms
     */
    void consumeSmsFromQueue(Sms sms);

    /**
     * @description
     * @datetime 2019/8/19 14:44
     * @version
     * @author dcyu
     * @param sms 短信体
     * @param result 短信发送结果
     * @return void
     */
    void insertSmsSendRecord(Sms sms, SmsResult result);

    /***
     * 定时创建短信发送记录表
     * @return void
     */
    void createSmsTable();

    /***
     * 定时创建邮件发送记录表
     * @return void
     */
    void createMailTable();
}
