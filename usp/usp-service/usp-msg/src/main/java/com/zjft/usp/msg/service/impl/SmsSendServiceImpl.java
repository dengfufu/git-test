package com.zjft.usp.msg.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.mq.util.MqSenderUtil;
import com.zjft.usp.msg.helper.SmsSendHelper;
import com.zjft.usp.msg.listener.MqSmsListener;
import com.zjft.usp.msg.mapper.SmsSendRecordMapper;
import com.zjft.usp.msg.model.Sms;
import com.zjft.usp.msg.model.SmsConfig;
import com.zjft.usp.msg.model.SmsResult;
import com.zjft.usp.msg.model.SmsSendRecord;
import com.zjft.usp.msg.service.SmsSendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author : dcyu
 * @Date : 2019年8月9日15:20:30
 * @Desc : 短信发送服务类
 */

@Slf4j
@Service
public class SmsSendServiceImpl implements SmsSendService {

    @Value("${msg.sms-type_verify}")
    private String msgTypeVerify;

    @Resource
    private SmsSendRecordMapper smsSendRecordMapper;

    @Autowired
    MqSenderUtil mqSenderUtil;

    @Autowired
    private SmsConfig smsConfig;

    /**TODO 临时变量 短信类型*/
    public static final int SMS_TYPE_VERIFY = 1;

    @Override
    public void productVerifySmsToQueue(int appId, String phoneNumbers, String verifyCode) {
        Assert.notEmpty(phoneNumbers, "phoneNumbers 不能为空");
        Assert.notEmpty(verifyCode, "verifyCode 不能为空");
        /*TODO 组装短信体*/
        Sms sms = new Sms();
        sms.setAppId(appId);
        /*TODO 根据APPID获取对应验证码短信模板码*/
        sms.setSmsType(SMS_TYPE_VERIFY);
        sms.setPhoneNumbers(phoneNumbers);

        /* 短信模板参数 */
        JSONObject paramJson = new JSONObject();
        paramJson.put("code", verifyCode);

        sms.setParam(paramJson.toJSONString());
        this.productSmsToQueue(sms);
    }

    @Override
    public void productSmsToQueue(Sms sms) {
        try {
            String msg = SmsSendHelper.checkSms(sms);
            if(!StringUtils.isEmpty(msg)){
                throw new AppException(msg);
            }

            /*TODO 订阅主题名 是否需要统一定义*/
            log.info("发送消息至短信消息队列开始");
            mqSenderUtil.sendMessage(MqSmsListener.SMS_TOPIC, JSONObject.toJSONString(sms));
            log.info("发送消息至短信消息队列结束");
        }catch(Exception e){
            e.printStackTrace();
            log.error("短信发送其他异常", e);
        }
    }

    @Override
    public void consumeSmsFromQueue(Sms sms) {
        /*TODO 获取配置数据 从数据库获取*/


        /*TODO 设置短信类型映射数据源 从数据库获取*/
        //根据appId找签名
        Map<Integer, String> appTypeMap = new HashMap<>();
        appTypeMap.put(10001,"紫金技术");
        //根据appId + smsType 找模板编号
        Map<Integer, String> smsTypeMap = new HashMap<>();
        smsTypeMap.put(SMS_TYPE_VERIFY,msgTypeVerify);

        SmsSendHelper sender = new SmsSendHelper(smsConfig, appTypeMap, smsTypeMap);
        SmsResult result = sender.sendSms(sms);

        /*插入短信发送记录*/
        this.insertSmsSendRecord(sms, result);
    }

    @Override
    public void insertSmsSendRecord(Sms sms, SmsResult result){
        SmsSendRecord smsSendRecord = new SmsSendRecord();
        //通过雪花算法生成Id
        smsSendRecord.setSmsId(KeyUtil.getId());
        smsSendRecord.setTouser(Long.parseLong(sms.getPhoneNumbers()));
        smsSendRecord.setAppId(sms.getAppId());
        smsSendRecord.setSmsType(sms.getSmsType());
        smsSendRecord.setFixedTime(sms.getSendDate());
        smsSendRecord.setContent(sms.getParam());
        smsSendRecord.setSentTime(new Date());
        smsSendRecord.setReceiptId(result.getBizId());
        smsSendRecord.setReturnCode(result.getCode());
        smsSendRecord.setReturnMsg(result.getMessage());

        try {
            smsSendRecordMapper.insertSmsSendRecord(smsSendRecord);
        } catch (Exception e) {
            if (e.getMessage().contains("sms_send_") && e.getMessage().contains("doesn't exist")) {
                this.createSmsTable();
                smsSendRecordMapper.insertSmsSendRecord(smsSendRecord);
            } else {
                throw e;
            }
        }
    }

    /***
     * 定时创建短信发送记录表
     * @return void
     */
    @Override
    public void createSmsTable() {
        Date startDate = new Date();
        int months = 3;
        for (int i = 0; i < months; i++) {
            String sortNo = DateUtil.format(DateUtils.addMonths(startDate, i), "yyMM");
            String tableName = "sms_send_" + sortNo;
            if (!this.existTable(tableName)) {
                this.smsSendRecordMapper.createTableBySms(tableName);
            }
        }
    }

    /***
     * 定时创建邮件发送记录表
     * @return void
     */
    @Override
    public void createMailTable() {
        Date startDate = new Date();
        int months = 3;
        for (int i = 0; i < months; i++) {
            String sortNo = DateUtil.format(DateUtils.addMonths(startDate, i), "yyMM");
            String tableName = "mail_send_" + sortNo;
            if (!this.existTable(tableName)) {
                this.smsSendRecordMapper.createTableByMai(tableName);
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
        if (this.smsSendRecordMapper.existTable(tableName) > 0) {
            return true;
        }
        return false;
    }
}
