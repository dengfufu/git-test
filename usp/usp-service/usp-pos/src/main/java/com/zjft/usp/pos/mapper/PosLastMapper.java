package com.zjft.usp.pos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.pos.dto.PositionDto;
import com.zjft.usp.pos.model.PosLast;

/**
 * 用户最新定位信息
 *
 * @author Qiugm
 * @date 2019-08-13 17:11
 * @version 1.0.0
 **/
public interface PosLastMapper extends BaseMapper<PosLast> {
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
     * 根据用户Id查询最新位置信息
     *
     * @author Qiugm
     * @date 2019-08-15
     * @param userId
     * @return com.zjft.usp.pos.model.PosLast
     */
    PosLast findPosByUserId(Long userId);
}
