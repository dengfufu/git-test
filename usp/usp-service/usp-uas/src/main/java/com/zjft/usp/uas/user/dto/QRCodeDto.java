package com.zjft.usp.uas.user.dto;

import lombok.Data;

/**
 * @Author : dcyu
 * @Date : 2019/8/20 9:42
 * @Desc : 二维码信息类
 * @Version 1.0.0
 */
@Data
public class QRCodeDto {

    /** 用户ID */
    private Long userId;

    /** 小头像 */
    private String faceImg;

    /** 昵称 */
    private String nickName;

    /** 省份编码 */
    private String province;

    /** 省份中文名 */
    private String provinceName;

    /** 城市编码 */
    private String city;

    /** 城市中文名 */
    private String cityName;

}
