package com.zjft.usp.uas.corp.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.RestTemplateUtil;
import com.zjft.usp.uas.corp.dto.CorpAppDto;
import com.zjft.usp.uas.corp.dto.CorpMenu;
import com.zjft.usp.uas.corp.model.CorpUser;
import com.zjft.usp.uas.corp.service.CorpAppService;
import com.zjft.usp.uas.corp.service.CorpUserService;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RefreshScope
/**
 * 企业外部应用管理实现类
 */
public class CorpAppServiceImpl implements CorpAppService {

    @Value("#{${corp.apps}}")
    Map<String,String> apis;


    @Autowired
    CorpUserService corpUserService;

    @Override public CorpAppDto getExternalApp(long corpId, long userId) {
        String api = apis.get(String.valueOf(corpId));

        if(StrUtil.isEmpty(api)){
            // 没有企业外部应用配置
            return null;
        }
        CorpUser corpUser = corpUserService.getOne(new QueryWrapper<CorpUser>().eq("corpid", corpId).eq("userid", userId));
        if(StrUtil.isEmpty(corpUser.getAccount())) {
            // 未配置企业方用户 ID
            return null;
        }

        MultiValueMap param = new LinkedMultiValueMap();
        param.add("account", corpUser.getAccount());
        try {
            ResponseEntity<String> response = RestTemplateUtil.postForm(api, param, null);
            Result result = JsonUtil.parseObject(response.getBody(), Result.class);
            if(result.getCode() == 1) {
                Map data = (Map) result.getData();
                return CorpAppDto.builder()
                    .cookie((String) data.get("cookie"))
                    .menus((List<CorpMenu>) data.get("menus"))
                    .domain((String) data.get("domain"))
                    .build();
            }
        } catch (Exception e) {
            log.error("请求["+api+"]异常", e);
        }

        return null;
    }
}
