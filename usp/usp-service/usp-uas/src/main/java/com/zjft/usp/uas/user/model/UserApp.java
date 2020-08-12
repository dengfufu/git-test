package com.zjft.usp.uas.user.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zphu
 * @Description
 * @date 2019/8/8 13:53
 * @Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("uas_user_app")
public class UserApp extends Model<UserApp> {

        @TableId("userid")
        /** 用户ID **/
        private Long userId;

        @Deprecated
        @TableField("appid")
        /** 废弃应用ID **/
        private Integer appId;

        @TableField("clientid")
        /** 客户端应用ID **/
        private String clientId;

        @TableField("regtime")
        /** 注册时间 **/
        private String regTime;


}
