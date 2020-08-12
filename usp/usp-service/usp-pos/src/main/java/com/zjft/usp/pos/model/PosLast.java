package com.zjft.usp.pos.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * 用户轨迹信息实体类
 *
 * @author Qiugm
 * @date 2019-08-13 17:30
 * @version 1.0.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pos_last")
public class PosLast {
    /** 用户ID */
    @TableId("userid")
    private long userId;
    /** 定位时间 */
    private Timestamp posTime;
    /** GPS经度 */
    private double lon;
    /** GPS纬度 */
    private double lat;
    /** 地址 */
    private String addr;
    /** 百度坐标系经度 */
    private double bdLon;
    /** 百度坐标系纬度 */
    private double bdLat;
    /** 定位精度 */
    private double radius;
    /** 终端类型：1=Android，2=IOS，3=微信 */
    private int client;
}
