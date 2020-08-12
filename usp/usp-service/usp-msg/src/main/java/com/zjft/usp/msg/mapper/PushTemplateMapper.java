package com.zjft.usp.msg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.msg.model.PushTemplate;


public interface PushTemplateMapper extends BaseMapper<PushTemplate> {

    PushTemplate selectByTplName(String tplName);

}
