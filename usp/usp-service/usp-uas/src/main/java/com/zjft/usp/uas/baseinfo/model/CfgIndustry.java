package com.zjft.usp.uas.baseinfo.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;

/**
 * 行业门类
 *
 * @author canlei
 * @version 1.0
 * @date 2019-08-15 14:17
 **/
@Getter
@Setter
@TableName("uas_cfg_industry")
public class CfgIndustry extends Model<CfgIndustry> {

    private String code;

    private String name;

}
