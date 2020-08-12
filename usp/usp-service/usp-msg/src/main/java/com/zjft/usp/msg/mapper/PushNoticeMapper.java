package com.zjft.usp.msg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.msg.model.PushNotice;
import org.apache.ibatis.annotations.Param;

/**
 * 群发通知表
 * @author cxd
 **/
public interface PushNoticeMapper extends BaseMapper<PushNotice> {

    void insertPushNotice(PushNotice pushNotice);

    /***
     * 添加群发通知表
     * @date 2020/1/19
     * @param tableName
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
