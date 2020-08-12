package com.zjft.usp.msg.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class PushTemplate {

    @TableId("tplid")
    private int tplId;

    @TableField("appid")
    private int appId;

    @TableField("tplname")
    private String tplName;

    @TableField("title")
    private String title;

    @TableField("content")
    private String content;

    @TableField("adduserid")
    private long addUserId;

    @TableField("addtime")
    private Date addTime;

    @TableField("enabled")
    private String enabled;
}
