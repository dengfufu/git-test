package com.zjft.usp.msg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.msg.dto.MsgFilter;
import com.zjft.usp.msg.model.PushMsg;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 单点消息记录表
 * @author cxd
 * */
public interface PushMsgMapper extends BaseMapper<PushMsg> {

    void insertPushMsg(PushMsg msg);

    void updatePushMsg(PushMsg msg);

    List<PushMsg> queryList(MsgFilter msgFilter);

    Long queryCount(MsgFilter msgFilter);

    List<PushMsg> listUnSend(@Param("userId") Long userId,
                             @Param("yearMonthList") List<String> yearMonthList);

    /***
     * 添加单点消息记录表
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
