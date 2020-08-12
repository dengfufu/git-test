package com.zjft.usp.pos.service;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.pos.dto.PositionDto;

/**
 * 定位服务接口
 *
 * @author Qiugm
 * @date 2019-08-15 15:42
 * @version 1.0.0
 **/
public interface PosService {
    /**
     * 添加用户定位信息
     *
     * @author Qiugm
     * @date 2019-08-13
     * @param positionDto
     * @param userInfo
     * @return int
     * @throws Exception
     */
    int uploadLocation(PositionDto positionDto, UserInfo userInfo) throws Exception;
}
