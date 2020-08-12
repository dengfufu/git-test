package com.zjft.usp.zj.work.cases.atmcase.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.zjft.usp.anyfix.common.constant.RightConstants;
import com.zjft.usp.common.model.OauthClient;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.msg.model.TemplateMessage;
import com.zjft.usp.msg.service.PushService;
import com.zjft.usp.uas.service.UasFeignService;
import com.zjft.usp.zj.work.cases.atmcase.composite.MessageCompoService;
import com.zjft.usp.zj.work.cases.atmcase.dto.message.MessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息聚合实现类
 *
 * @author zgpi
 * @date 2020-4-7 20:41
 **/
@Slf4j
@Service
public class MessageCompoServiceImpl implements MessageCompoService {

    @Autowired
    private PushService pushService;
    @Resource
    private UasFeignService uasFeignService;

    /**
     * 发送普通消息
     *
     * @param messageDto
     * @param oauthClient
     * @return
     * @author zgpi
     * @date 2020/4/7 20:41
     */
    @Override
    public void sendOrdinaryMessage(MessageDto messageDto, OauthClient oauthClient) {
        if (StrUtil.isBlank(messageDto.getUserIds())) {
            return;
        }
        Long corpId = oauthClient.getCorpId();
        if (LongUtil.isZero(corpId)) {
            log.error("企业编号为空：{}", JsonUtil.toJson(oauthClient));
            return;
        }
        List<Long> userIdList = new ArrayList<>();
        List<String> accountList = StrUtil.splitTrim(StrUtil.trimToEmpty(messageDto.getUserIds()), ",");
        if (CollectionUtil.isNotEmpty(accountList)) {
            String json = JsonUtil.toJsonString("accountList", accountList, "corpId", corpId);
            Result<List<Long>> userIdListResult = uasFeignService.listUserIdByAccountList(json);
            if (userIdListResult != null && userIdListResult.getCode() == Result.SUCCESS) {
                if (userIdListResult.getCode() == Result.SUCCESS) {
                    userIdList = userIdListResult.getData();
                } else {
                    log.error("查询企业员工账号失败：{}", userIdListResult.getMsg());
                }
            } else {
                log.error("查询企业员工账号失败");
            }
        }
        if (CollectionUtil.isEmpty(userIdList)) {
            log.error("企业员工账号为空，accountList：{}", accountList);
            return;
        }
        if (userIdList.size() < accountList.size()) {
            log.error("部分企业员工账号在账户系统中不存在，accountList：{}, userId：{}", accountList, userIdList);
            return;
        }
        TemplateMessage templateMessage = new TemplateMessage();
        String userIds = CollectionUtil.join(userIdList, ",");
        templateMessage.setUserIds(userIds);
        templateMessage.setAppId(RightConstants.WORK_APPID);
        templateMessage.setTplName("普通模板");
        templateMessage.setTitle(StrUtil.trimToEmpty(messageDto.getTitle()));
        Map<String, Object> contentMap = new HashMap<>(2);
        contentMap.put("title", StrUtil.trimToEmpty(messageDto.getTitle()));
        contentMap.put("content", StrUtil.trimToEmpty(messageDto.getContent()));
        templateMessage.setDataMap(contentMap);
        pushService.pushTemplateMessage(templateMessage);
    }
}
