package com.zjft.usp.uas.baseinfo.mapper;

import com.zjft.usp.uas.baseinfo.model.CfgApp;

public interface CfgAppMapper {
    int deleteByPrimaryKey(Integer appId);

    int insert(CfgApp record);

    int insertSelective(CfgApp record);

    CfgApp selectByPrimaryKey(Integer appId);

    int updateByPrimaryKeySelective(CfgApp record);

    int updateByPrimaryKey(CfgApp record);
}