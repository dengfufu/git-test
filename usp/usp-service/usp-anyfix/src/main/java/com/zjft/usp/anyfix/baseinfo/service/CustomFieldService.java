package com.zjft.usp.anyfix.baseinfo.service;

import com.zjft.usp.anyfix.baseinfo.dto.CustomFieldDto;
import com.zjft.usp.anyfix.baseinfo.filter.CustomFieldFilter;
import com.zjft.usp.anyfix.baseinfo.model.CustomField;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;

import java.util.List;

/**
 * <p>
 * 自定义字段配置表 服务类
 * </p>
 *
 * @author chenxiaod
 * @since 2019-10-08
 */
public interface CustomFieldService extends IService<CustomField> {

    /**
     * 分页查询所有的自定义字段
     * @date 2019/9/29
     * @return
     */
    ListWrapper<CustomFieldDto> query(CustomFieldFilter customFieldFilter);

    /**
     *  根据条件查询自定义配置字段列表(包含数据源)
     * @param customFieldFilter 查询条件
     * @return
     */
    List<CustomFieldDto> customFieldList(CustomFieldFilter customFieldFilter);
    /**
     * 根据id自定义字段
     * @param fieldId 自定义字段dto
     * @return
     */
    CustomFieldDto findCustomFieldById(Long fieldId);

    /**
     *  修改自定义字段
     * @param customFieldDto 自定义字段dto
     */
    void updateCustomField(CustomFieldDto customFieldDto);

    /**
     * 删除自定义字段
     * @param fieldId  自定义字段id
     */
    void deleteCustomField(Long fieldId);

    /**
     * 添加自定义字段
     * @param customFieldDto
     * @param userInfo
     * @return
     */
    void save(CustomFieldDto customFieldDto, UserInfo userInfo);


    /**
     * 更新自定义字段
     * @param customFieldDto
     * @param userInfo
     * @return
     */
    void update(CustomFieldDto customFieldDto, UserInfo userInfo);
}
