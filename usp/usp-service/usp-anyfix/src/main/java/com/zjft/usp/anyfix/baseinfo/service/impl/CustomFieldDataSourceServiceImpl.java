package com.zjft.usp.anyfix.baseinfo.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.anyfix.baseinfo.dto.CustomFieldDto;
import com.zjft.usp.anyfix.baseinfo.model.CustomFieldDataSource;
import com.zjft.usp.anyfix.baseinfo.mapper.CustomFieldDataSourceMapper;
import com.zjft.usp.anyfix.baseinfo.service.CustomFieldDataSourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 自定义字段数据源表 服务实现类
 * </p>
 *
 * @author cxd
 * @since 2020-01-06
 */
@Service
public class CustomFieldDataSourceServiceImpl extends ServiceImpl<CustomFieldDataSourceMapper, CustomFieldDataSource> implements CustomFieldDataSourceService {

    /**
     *  查询自定义字段数据源列表
     * @param fieldId 自定义字段id
     * @return
     */
    @Override
    public List<CustomFieldDataSource> listDataSource(Long fieldId) {
        Assert.notNull(fieldId, "fieldId 不能为 NULL");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("field_id", fieldId);
        CustomFieldDataSource dataSource = new CustomFieldDataSource();
        return dataSource.selectList(queryWrapper);
    }

    /**
     *  添加自定义字段数据源列表
     * @param customFieldDto  自定义字段dto
     */
    @Override
    public void insertDataSourceList(CustomFieldDto customFieldDto) {
        Assert.notNull(customFieldDto.getCustomFieldDataSourceList(), "dataSourceList 不能为 NULL");
        CustomFieldDataSource customFieldDataSource;
        for(CustomFieldDataSource customFieldDataSourceEntry : customFieldDto.getCustomFieldDataSourceList()){
            customFieldDataSource = new CustomFieldDataSource();
            BeanUtils.copyProperties(customFieldDataSourceEntry, customFieldDataSource);
            customFieldDataSource.setSourceId(KeyUtil.getId());
            customFieldDataSource.setFieldId(customFieldDto.getFieldId());
            customFieldDataSource.setOperateTime(DateUtil.date().toTimestamp());
            customFieldDataSource.setOperator(customFieldDto.getOperator());
            customFieldDataSource.insert();
        }
    }

    /**
     *  删除自定义字段数据源列表
     * @param fieldId  自定义字段id
     */
    @Override
    public void deleteDataSource(Long fieldId) {
        Assert.notNull(fieldId, "fieldId 不能为 NULL");
        CustomFieldDataSource customFieldDataSource = new CustomFieldDataSource();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("field_id", fieldId);
        customFieldDataSource.delete(queryWrapper);
    }
}
