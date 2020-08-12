package com.zjft.usp.msg.service;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.msg.dto.MsgFilter;
import com.zjft.usp.msg.model.*;

import java.util.List;

/**
 * 消息推送 接口类
 *
 * @author zgpi
 * @since 2019-11-20
 */
public interface PushMessageService {

    /**
     * 发送普通消息
     *
     * @param message 消息
     * @return
     * @author zgpi
     * @date 2019/11/20 09:02
     **/
    void pushMessage(Message message);

    /**
     * 发送模板消息
     *
     * @param tplMessage 模板消息
     * @return
     * @author zgpi
     * @date 2019/11/20 09:02
     **/
    void pushTplMessage(TemplateMessage tplMessage);

    /**
     * 发送通知
     *
     * @param message 消息
     * @return
     * @author zgpi
     * @date 2019/11/20 09:02
     **/
    void pushNotice(Message message);

    /**
     * 发送公告
     *
     * @param message 消息
     * @return
     * @author zgpi
     * @date 2019/11/20 09:02
     **/
    void pushBulletin(Message message);

    /**
     * 设置别名
     *
     * @param registrationId 注册ID
     * @param alias 别名
     * @return
     * @author zgpi
     * @date 2019/11/20 09:02
     **/
    void setAlias(String registrationId, String alias);

    /**
     * 分页查询消息
     *
     * @param msgFilter 消息过滤条件
     * @return
     * @author zgpi
     * @date 2019/11/20 09:02
     **/
    ListWrapper<PushMsg> query(MsgFilter msgFilter);

    /**
     * 未发送的消息列表
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/11/22 14:18
     **/
    List<PushMsg> listUnSend(Long userId);

    /***
     * 定时创建单点消息记录表
     * @return void
     */
    void createPushMsgTable();

}
