package com.zjft.usp.pos.dto;

import lombok.Data;

import java.util.Date;

/**
 * 位置表定时任务Dto
 *
 * @author cxd
 * @date 2019-1-19 16:41
 * @version 1.0.0
 **/
@Data
public class PosJobDto {

    /** 开始时间 */
    private String startDateTime;
    /** 建表天数 */
    private int days;
}
