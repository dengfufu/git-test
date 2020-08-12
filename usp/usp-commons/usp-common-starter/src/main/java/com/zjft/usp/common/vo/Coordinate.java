package com.zjft.usp.common.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 定位坐标实体类
 *
 * @author Qiugm
 * @date 2019-08-15 13:53
 * @version 1.0.0
 **/
public class Coordinate {
    /** 经度 */
    @Setter
    @Getter
    private double lon;

    /** 纬度 */
    @Setter
    @Getter
    private double lat;
}
