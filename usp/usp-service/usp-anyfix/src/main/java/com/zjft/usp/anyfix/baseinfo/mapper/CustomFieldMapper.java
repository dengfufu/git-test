package com.zjft.usp.anyfix.baseinfo.mapper;

import com.zjft.usp.anyfix.baseinfo.filter.CustomFieldFilter;
import com.zjft.usp.anyfix.baseinfo.model.CustomField;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;


/**
 * <p>
 * 自定义字段配置表 Mapper 接口
 * </p>
 *
 * @author chenxiaod
 * @since 2019-10-08
 */
public interface CustomFieldMapper extends BaseMapper<CustomField> {

    /**
     * 根据表单类型和企业来查询自定义字段
     * @param customFieldFilter
     * @return
     */
    List<CustomField> selectCustomField(CustomFieldFilter customFieldFilter);

}
