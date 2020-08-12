package com.zjft.usp.anyfix.baseinfo.dto;

import com.zjft.usp.anyfix.baseinfo.model.CustomField;
import com.zjft.usp.anyfix.baseinfo.model.CustomFieldDataSource;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 *  自定义字段dto
 *
 * @date: 2019/10/9 15:31
 * @author: cxd
 * @version: 1.0
 */
@ApiModel("自定义字段dto")
@Data
public class CustomFieldDto extends CustomField {

    /**
     * 获得自定义字段数据源列表
     */
    private List<CustomFieldDataSource> customFieldDataSourceList;
}




