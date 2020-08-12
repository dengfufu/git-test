package com.zjft.usp.anyfix.baseinfo.service;

import com.zjft.usp.anyfix.baseinfo.dto.CustomFieldDto;
import com.zjft.usp.anyfix.baseinfo.model.CustomFieldDataSource;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 自定义字段数据源表 服务类
 * </p>
 *
 * @author cxd
 * @since 2020-01-06
 */
public interface CustomFieldDataSourceService extends IService<CustomFieldDataSource> {

    /**
     *  查询自定义字段数据源列表
     * @param fieldId 自定义字段id
     * @return
     */
     List<CustomFieldDataSource> listDataSource(Long fieldId);

    /**
     *  添加自定义字段数据源列表
     * @param customFieldDto  数据源列表
     */
    void insertDataSourceList(CustomFieldDto customFieldDto);

    /**
     *  删除自定义字段数据源列表
     * @param fieldId  自定义字段id
     */
    void deleteDataSource(Long fieldId);
}
