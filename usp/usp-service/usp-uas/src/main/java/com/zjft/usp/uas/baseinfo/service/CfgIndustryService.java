package com.zjft.usp.uas.baseinfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.uas.baseinfo.model.CfgIndustry;

import java.util.List;
import java.util.Map;

/**
 * 基础数据服务
 *
 * @author canlei
 * @version 1.0
 * @date 2019-08-19 15:18
 **/
public interface CfgIndustryService extends IService<CfgIndustry> {

    /**
     * 获取行业列表
     * @return
     */
    List<CfgIndustry> listIndustry();

    /**
     * 获取行业门类编号和名称的Map
     * @return
     */
    Map<String, String> industryMap();

}
