package com.zjft.usp.pos.service;


import com.zjft.usp.pos.dto.PositionDto;
import com.zjft.usp.pos.model.PosLast;

import java.util.List;

/**
 * 用户最新定位服务接口
 *
 * @author Qiugm
 * @date 2019-08-13 16:39
 * @version 1.0.0
 **/
public interface PosLastService {
    /**
     * 根据用户Id查询最新位置信息
     *
     * @author Qiugm
     * @date 2019-08-15
     * @param userId
     * @return com.zjft.usp.pos.model.PosLast
     */
    PosLast findPosByUserId(Long userId);

    /**
     * 添加用户定位信息
     *
     * @author Qiugm
     * @date 2019-08-13
     * @param positionDto
     * @return int
     */
    int insertPosInfo(PositionDto positionDto);

    /***
     * 根据用户Id删除记录
     *
     * @author Qiugm
     * @date 2019-08-22
     * @param userId
     * @return void
     */
    void deleteById(Long userId);

    /***
     * 更新用户信息
     *
     * @author Qiugm
     * @date 2019/8/26
     * @param positionDto
     * @return int
     */
    int updateById(PositionDto positionDto);

    /**
     * 根据定位信息查找附近的用户
     * @param distance
     * @param lon
     * @param lat
     * @return
     */
    List<Long> findNearUsers(Integer distance, Double lon, Double lat);
}
