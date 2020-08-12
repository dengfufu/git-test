package com.zjft.usp.msg.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author : zrLin
 * @Date : 2019年8月14日
 * @Desc : 公告表实体类
 */
@Data
@TableName("push_bulletin")
public class PushBulletin {

    /** 表名 */
    private String tableName = "";

    @TableId("msgid")
    private long msgId;

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

    public String getTableName() {
        return "push_bulletin";
    }
}
