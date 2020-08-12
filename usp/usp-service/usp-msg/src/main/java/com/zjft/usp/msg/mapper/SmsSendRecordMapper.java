package com.zjft.usp.msg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.msg.model.SmsSendRecord;
import org.apache.ibatis.annotations.Param;

/**
 * 用户短信发送记录
 * @author cxd
 **/
public interface SmsSendRecordMapper extends BaseMapper<SmsSendRecord>  {

    void insertSmsSendRecord(SmsSendRecord smsSendRecord);

    /***
     * 添加短信发送记录表
     * @date 2020/1/19
     * @param tableName 表名
     * @return void
     */
    void createTableBySms(@Param("tableName") String tableName);

    /***
     * 添加邮件发送记录表
     * @date 2020/1/19
     * @param tableName
     * @return void
     */
    void createTableByMai(@Param("tableName") String tableName);

    /***
     * 判断表是否存在
     * @date 2020/1/19
     * @param tableName 表名
     * @return int
     */
    int existTable(@Param("tableName") String tableName);
}
