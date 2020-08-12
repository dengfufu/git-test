package com.zjft.usp.device.baseinfo.common.service.impl;

import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.device.baseinfo.common.service.CorpNameService;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CorpNameServiceImpl implements CorpNameService {

    @Resource
    private UasFeignService uasFeignService;

    @Override
    public Map<Long, String> corpIdNameMap(List<Long> corpIdList) {
        // 增加委托商名称显示
        Result corpResult = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList));
        Map<Long, String> corpMap;
        if(corpResult !=null && corpResult.getCode() == Result.SUCCESS) {
            corpMap = JsonUtil.parseObject(JsonUtil.toJson(corpResult.getData()), Map.class);
        } else {
            corpMap = new HashMap<>();
        }
        return corpMap;
    }
}
