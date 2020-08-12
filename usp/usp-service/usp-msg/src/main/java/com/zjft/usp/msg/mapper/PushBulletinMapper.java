package com.zjft.usp.msg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.msg.model.PushBulletin;
import com.zjft.usp.msg.model.PushTemplate;
import org.apache.ibatis.annotations.Param;

/**
 * 公告表
 * @author cxd
 * */
public interface PushBulletinMapper extends BaseMapper<PushTemplate> {

    void insertPushBulletin(PushBulletin pushBulletin);

    /***
     * 添加公告表
     * @date 2020/1/19
     * @param tableName 表名
     * @return void
     */
    void createTable(@Param("tableName") String tableName);

    /***
     * 判断表是否存在
     * @date 2020/1/19
     * @param tableName 表名
     * @return int
     */
    int existTable(@Param("tableName") String tableName);
}
