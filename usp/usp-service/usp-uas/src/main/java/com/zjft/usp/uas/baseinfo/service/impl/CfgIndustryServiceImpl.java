package com.zjft.usp.uas.baseinfo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.uas.baseinfo.mapper.CfgIndustryMapper;
import com.zjft.usp.uas.baseinfo.model.CfgIndustry;
import com.zjft.usp.uas.baseinfo.service.CfgIndustryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础数据服务实现
 *
 * @author canlei
 * @version 1.0
 * @date 2019-08-19 15:19
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class CfgIndustryServiceImpl extends ServiceImpl<CfgIndustryMapper, CfgIndustry> implements CfgIndustryService {

    @Override
    public List<CfgIndustry> listIndustry(){
        CfgIndustry industry = new CfgIndustry();
        return industry.selectAll();
    }

    @Override
    public Map<String, String> industryMap(){
        List<CfgIndustry> industryList = list();
        Map<String, String> map = new HashMap<>();
        if(industryList != null && industryList.size() > 0){
            for(CfgIndustry cfgIndustry:industryList){
                map.put(cfgIndustry.getCode(), cfgIndustry.getName());
            }
        }
        return map;
    }

}
