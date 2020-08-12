package com.zjft.usp.uas.user.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
/**
 *
 *用户操作
 * @Author zphu
 * @Date 2019/8/13 13:44
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("uas_user_oper")
public class UserOper extends Model<UserOper> {

    @TableId("userid")
    /** 用户ID **/
    private Long userId;

    @TableId("txid")
    /** 交易号 **/
    private Long txId;

    @TableField("clientid")
    /** 客户端应用ID **/
    private String clientId;

    @Deprecated
    @TableField("appid")
    /** 废弃应用ID **/
    private Integer appId;

    @TableField("opertype")
    /** 操作类型 **/
    private Short operType;

    /** 操作详情 **/
    private String details;

    @TableField("opertime")
    /** 操作时间 **/
    private Date operTime;

    /** 经度 **/
    private BigDecimal lon;

    /** 纬度 **/
    private BigDecimal lat;

}