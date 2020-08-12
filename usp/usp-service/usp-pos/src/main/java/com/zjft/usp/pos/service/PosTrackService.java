package com.zjft.usp.pos.service;


import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.pos.dto.PositionDto;
import com.zjft.usp.pos.model.PosTrack;

import java.util.List;

/**
 * 用户定位信息服务接口
 *
 * @author Qiugm
 * @date 2019-08-13 16:39
 * @version 1.0.0
 **/
public interface PosTrackService {
    /**
     * 添加用户定位信息
     *
     * @author Qiugm
     * @date 2019-08-13
     * @param positionDto
     * @return int
     */
    int insertPosInfo(PositionDto positionDto);

    /**
     * 查询用户定位信息
     *
     * @author Qiugm
     * @date 2019-08-15
     * @param positionDto
     * @return
     */
    List<PosTrack> listPosTrack(PositionDto positionDto);

    /***
     * 定时创建表
     *
     * @author Qiugm
     * @date 2019/9/2
     * @param startDateTime
     * @param days
     * @return void
     */
    void createTableByTask(String startDateTime, int days);

    /***
     * 定时删除表
     *
     * @author Qiugm
     * @date 2019/9/2
     * @param startDateTime
     * @param days
     * @param retenDays
     * @return void
     */
    void dropTableByTask(String startDateTime, int days, int retenDays);
}
