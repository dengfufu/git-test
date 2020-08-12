package com.zjft.usp.msg.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;


@Data
@TableName("push_msg")
public class PushMsg {

    /** 表名 */
    @TableField(exist = false)
    private String tableName = "";

    @TableId("msgid")
    private long msgId;

    @TableField("touser")
    private long touser;

    @TableField("appid")
    private int appId;

    private String title;

    private String content;

    @TableField("fixedtime")
    private Date fixedTime;

    @TableField("senttime")
    private Date sentTime;

    @TableField("sendstatus")
    private int sendStatus;

    @TableField("receiptid")
    private String receiptId;

    @TableField("receivestatus")
    private int receiveStatus;

    @TableField("receivetime")
    private Date receiveTime;

    @TableField("weburl")
    private String webUrl;

    @TableField("appurl")
    private String appUrl;

    @TableField("wechaturl")
    private String wechatUrl;

    /** 按日期分表 */
    public String getTableName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String suffix = sdf.format(new Date()).substring(2);
        return "push_msg_" + suffix;
    }

}
