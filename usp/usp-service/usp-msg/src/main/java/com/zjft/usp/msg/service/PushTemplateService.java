package com.zjft.usp.msg.service;

import com.zjft.usp.msg.model.PushTemplate;

public interface PushTemplateService {

    PushTemplate findPushTemplate(Integer appId, String tplName);
}
