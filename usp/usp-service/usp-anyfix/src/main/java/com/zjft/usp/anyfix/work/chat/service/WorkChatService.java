package com.zjft.usp.anyfix.work.chat.service;

import com.zjft.usp.anyfix.work.chat.dto.WorkChatDto;
import com.zjft.usp.anyfix.work.chat.filter.WorkChatFilter;
import com.zjft.usp.anyfix.work.chat.model.WorkChat;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.vo.ReqParam;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author linzerun
 * @since 2020-03-09
 */
public interface WorkChatService extends IService<WorkChat> {

    /**
     * 发送消息
     * @param workChat
     */
    void sendMsg(WorkChat workChat);

    /**
     * 查询该工单所有评论
     * @param workChatFilter
     * @param reqParam
     * @return
     */
    List<WorkChatDto> selectWorkChatList(WorkChatFilter workChatFilter, ReqParam reqParam);


    /**
     * 更新消息
     * @param workId
     * @param status
     */
    void operateMsg(Long workId, Integer status);

    /**
     * 消息提醒
     * @param workId
     */
    void notify(Long workId,Long excludeUserId);


}
