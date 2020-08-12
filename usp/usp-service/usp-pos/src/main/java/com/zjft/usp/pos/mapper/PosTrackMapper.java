package com.zjft.usp.pos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.pos.dto.PositionDto;
import com.zjft.usp.pos.model.PosTrack;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户定位信息
 *
 * @author Qiugm
 * @date 2019-08-13 17:11
 * @version 1.0.0
 **/
public interface PosTrackMapper extends BaseMapper<PosTrack> {
    /**
     * 添加用户定位信息
     *
     * @author Qiugm
     * @date 2019-08-13
     * @param positionDto
     * @return void
     */
    int insertPosInfo(PositionDto positionDto);

    /**
     * 查询用户定位信息
     *
     * @author Qiugm
     * @date 2019-08-15
     * @param positionDto
     * @return java.util.List<com.zjft.usp.pos.model.PosTrack>
     */
    List<PosTrack> listPosTrack(PositionDto positionDto);

    /***
     * 判断表是否存在
     *
     * @author Qiugm
     * @date 2019/9/2
     * @param tableName
     * @return boolean
     */
    int existTable(@Param("tableName") String tableName);

    /***
     * 创建表
     *
     * @author Qiugm
     * @date 2019/9/2
     * @param tableName
     * @param userIdIdx
     * @param postTimeIdx
     * @return void
     */
    void createTable(@Param("tableName") String tableName, @Param("userIdIdx") String userIdIdx,
                     @Param("postTimeIdx") String postTimeIdx);

    /***
     * 删除表
     *
     * @author Qiugm
     * @date 2019/9/2
     * @param tableName
     * @return void
     */
    void dropTable(@Param("tableName") String tableName);
}
