package com.zjft.usp.anyfix.baseinfo.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.zjft.usp.anyfix.baseinfo.dto.CustomFieldDto;
import com.zjft.usp.anyfix.baseinfo.filter.CustomFieldDataFilter;
import com.zjft.usp.anyfix.baseinfo.model.CustomField;
import com.zjft.usp.anyfix.baseinfo.model.CustomFieldData;
import com.zjft.usp.anyfix.baseinfo.mapper.CustomFieldDataMapper;
import com.zjft.usp.anyfix.baseinfo.service.CustomFieldDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.service.CustomFieldService;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 自定义字段数据表 服务实现类
 * </p>
 *
 * @author cxd
 * @since 2020-01-08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomFieldDataServiceImpl extends ServiceImpl<CustomFieldDataMapper, CustomFieldData> implements CustomFieldDataService {

    @Autowired
    private CustomFieldService customFieldService;

    /**
     * 批量添加自定义字段数据
     *
     * @param customFieldDataList 自定义字段数据列表
     */
    @Override
    public void addCustomFieldDataList(List<CustomFieldData> customFieldDataList) {
        Assert.notNull(customFieldDataList, "customFieldDataList 不能为 NULL");
        for (CustomFieldData customFieldData : customFieldDataList) {
            CustomFieldDto customFieldDto = customFieldService.findCustomFieldById(customFieldData.getFieldId());
            customFieldData.setDataId(KeyUtil.getId());
            customFieldData.setFormType(customFieldDto.getFormType());
            customFieldData.setFieldName(customFieldDto.getFieldName());
            customFieldData.setFieldType(customFieldDto.getFieldType());
            customFieldData.insert();
        }
    }

    /**
     * 批量修改自定义字段数据
     *
     * @param formId
     * @param customFieldDataList
     * @return
     * @author zgpi
     * @date 2020/2/26 20:06
     */
    @Override
    public void modCustomFieldDataList(Long formId, List<CustomFieldData> customFieldDataList) {
        if (CollectionUtil.isNotEmpty(customFieldDataList)) {
            for (CustomFieldData customFieldData : customFieldDataList) {
                this.remove(new QueryWrapper<CustomFieldData>().eq("form_id", formId)
                        .eq("field_id", customFieldData.getFieldId()));
                customFieldData.setDataId(KeyUtil.getId());
                CustomField entity = customFieldService.getById(customFieldData.getFieldId());
                if (entity != null) {
                    customFieldData.setFormId(formId);
                    customFieldData.setFormType(entity.getFormType());
                    customFieldData.setFieldName(entity.getFieldName());
                    customFieldData.setFieldType(entity.getFieldType());
                    customFieldData.insert();
                }
            }
        }
    }

    /**
     * 根据条件查询自定义字段数据
     *
     * @param customFieldDataFilter 自定义字段数据条件
     * @return
     */
    @Override
    public List<CustomFieldData> queryCustomFieldData(CustomFieldDataFilter customFieldDataFilter) {
        QueryWrapper<CustomFieldData> queryWrapper = new QueryWrapper();
        if (LongUtil.isNotZero(customFieldDataFilter.getFormId())) {
            queryWrapper.eq("form_id", customFieldDataFilter.getFormId());
        }
        return this.list(queryWrapper);
    }

    /**
     * 根据设备档案id删除自定义字段数据
     *
     * @param deviceId 设备档案id
     */
    @Override
    public void deleteCustomFieldData(Long deviceId) {
        Assert.notNull(deviceId, "deviceId 不能为 NULL");
        CustomFieldData customFieldData = new CustomFieldData();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("form_id", deviceId);
        customFieldData.delete(queryWrapper);
    }
}
