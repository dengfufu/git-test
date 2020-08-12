package com.zjft.usp.uas.baseinfo.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author : dcyu
 * @Date : 2019年8月13日
 * @Desc : 行政区划代码类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("uas_cfg_area")
public class CfgArea extends Model<CfgArea> {

    /**编码**/
    @TableId("code")
    private String code;
    /**城市名**/
    private String name;
}
