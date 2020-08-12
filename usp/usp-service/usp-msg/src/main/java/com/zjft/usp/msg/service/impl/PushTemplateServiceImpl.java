package com.zjft.usp.msg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.msg.mapper.PushTemplateMapper;
import com.zjft.usp.msg.model.PushTemplate;
import com.zjft.usp.msg.service.PushTemplateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PushTemplateServiceImpl extends ServiceImpl<PushTemplateMapper, PushTemplate> implements PushTemplateService {

    @Override
    public PushTemplate findPushTemplate(Integer appId, String tplName) {
        return this.baseMapper.selectOne(new QueryWrapper<PushTemplate>().eq("appid", appId)
                .eq("tplname", tplName));
    }
}
