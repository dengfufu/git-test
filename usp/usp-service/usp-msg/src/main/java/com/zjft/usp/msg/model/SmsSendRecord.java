package com.zjft.usp.msg.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author : dcyu
 * @Date : 2019/8/19 10:25
 * @Desc : 短信发送记录
 * @Version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "sms_send")
public class SmsSendRecord extends Model<SmsSendRecord> {

    /** 表名 */
    private String tableName = "";

    /** 短信ID */
    @TableId("smsid")
    private Long smsId;

    /** 目标用户 */
    private Long touser;

    /** 应用ID */
    @TableField("appid")
    private int appId;

    /** 短信类型 */
    @TableField("smstype")
    private int smsType;

    /** 通知内容 */
    private String content = "";

    /** 指定推送时间 */
    @TableField("fixedtime")
    private Date fixedTime;

    /** 已发送时间 */
    @TableField("senttime")
    private Date sentTime;

    /** 发送状态 */
    private int status;

    /** 回执ID */
    @TableField("receiptid")
    private String receiptId = "";

    /** 返回码 */
    @TableField("returncode")
    private String returnCode = "";

    /** 返回消息 */
    @TableField("returnmsg")
    private String returnMsg = "";

    /** 按日期分表 */
    public String getTableName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String suffix = sdf.format(new Date()).substring(2);
        return "sms_send_" + suffix;
    }
}
