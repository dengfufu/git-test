package com.zjft.usp.uas.user.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Author : dcyu
 * @Date : 2019年8月13日
 * @Desc : 用户地址类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("uas_user_addr")
public class UserAddr extends Model<UserAddr> {

    /** 地址ID **/
    @TableId("addrid")
    private Long addrId;

    /** 用户ID **/
    @TableId("userid")
    private Long userId;

    /** 添加时间 **/
    @TableField("addtime")
    private Date addTime;

    /** 是否默认地址 1=是 **/
    @TableField("isdefault")
    private String isDefault;

    /** 省份代码 **/
    private String province;

    /** 地市代码 **/
    private String city;

    /** 区县代码 **/
    private String district;

    /** 详细地址 **/
    private String detail;
}
