package com.zjft.usp.zj.work.cases.atmcase.composite;

import com.zjft.usp.common.model.OauthClient;
import com.zjft.usp.zj.work.cases.atmcase.dto.message.MessageDto;

/**
 * 消息聚合接口
 *
 * @author zgpi
 * @date 2020-4-7 20:41
 **/
public interface MessageCompoService {

    /**
     * 发送普通消息
     *
     * @param messageDto
     * @param oauthClient
     * @return
     * @author zgpi
     * @date 2020/4/7 20:41
     */
    void sendOrdinaryMessage(MessageDto messageDto, OauthClient oauthClient);
}
