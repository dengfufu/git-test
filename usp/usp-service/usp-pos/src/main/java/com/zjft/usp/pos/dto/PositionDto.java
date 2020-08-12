package com.zjft.usp.pos.dto;

import lombok.Data;

import java.util.Date;

/**
 * 定位信息Dto
 *
 * @author Qiugm
 * @date 2019-08-13 16:41
 * @version 1.0.0
 **/
@Data
public class PositionDto {
    /** 用户ID */
    private long userId;
    /** 定位时间 */
    private Date posTime;
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
    /** 小时区间：0~23 */
    private int posHour;
    /** 定位开始时间 */
    private String posStartTime;
    /** 定位结束时间 */
    private String posEndTime;
    /** 定位轨迹表序号 */
    private String posTrackTableName;
    /** 开始小时区间 */
    private String posHourStart;
    /** 结束小时区间 */
    private String posHourEnd;

}
