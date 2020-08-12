package com.zjft.usp.anyfix.baseinfo.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.baseinfo.dto.CustomFieldDto;
import com.zjft.usp.anyfix.baseinfo.filter.CustomFieldFilter;
import com.zjft.usp.anyfix.baseinfo.model.CustomField;
import com.zjft.usp.anyfix.baseinfo.mapper.CustomFieldMapper;
import com.zjft.usp.anyfix.baseinfo.model.CustomFieldDataSource;
import com.zjft.usp.anyfix.baseinfo.service.CustomFieldDataSourceService;
import com.zjft.usp.anyfix.baseinfo.service.CustomFieldService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 自定义字段配置表 服务实现类
 * </p>
 *
 * @author chenxiaod
 * @since 2019-10-08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomFieldServiceImpl extends ServiceImpl<CustomFieldMapper, CustomField> implements CustomFieldService {

    @Autowired
    CustomFieldDataSourceService customFieldDataSourceService;

    /**
     * 分页查询所有的自定义字段
     *
     * @return
     * @date 2019/9/29
     */
    @Override
    public ListWrapper<CustomFieldDto> query(CustomFieldFilter customFieldFilter) {
        IPage<CustomField> page = new Page(customFieldFilter.getPageNum(), customFieldFilter.getPageSize());
        QueryWrapper<CustomField> queryWrapper = new QueryWrapper();
        List<CustomFieldDto> customFieldDtoList = new ArrayList<>();
        if (StrUtil.isNotBlank(customFieldFilter.getFieldName())) {
            queryWrapper.like("field_name", customFieldFilter.getFieldName());
        }
        if (customFieldFilter.getFieldType() > 0) {
            queryWrapper.eq("field_type", customFieldFilter.getFieldType());
        }
        /*queryWrapper.eq("service_corp", customFieldFilter.getCorpId()).or()
                .eq("custom_corp", customFieldFilter.getCorpId());*/
        if (LongUtil.isNotZero(customFieldFilter.getCorpId())) {
            queryWrapper.eq("corp_id", customFieldFilter.getCorpId());
        }
        IPage<CustomField> iPage = this.page(page, queryWrapper);

        CustomFieldDto customFieldDto;
        for (CustomField customField : iPage.getRecords()) {
            customFieldDto = new CustomFieldDto();
            BeanUtils.copyProperties(customField, customFieldDto);

            // 获取自定义字段数据源列表
            List<CustomFieldDataSource> dataSourceList = customFieldDataSourceService.listDataSource(customField.getFieldId());
            customFieldDto.setCustomFieldDataSourceList(dataSourceList);
            customFieldDtoList.add(customFieldDto);
        }
        return ListWrapper.<CustomFieldDto>builder()
                .list(customFieldDtoList)
                .total(iPage.getTotal())
                .build();
    }

    /**
     * 根据条件查询自定义配置字段列表(包含数据源)
     * @param customFieldFilter 查询条件
     * @return
     */
    @Override
    public List<CustomFieldDto> customFieldList(CustomFieldFilter customFieldFilter) {
        Assert.notNull(customFieldFilter.getFormType(), "表单类型不能为NULL");
        Assert.notNull(customFieldFilter.getCorpId(), "公司编号不能为NULL");

        List<CustomFieldDto> customFieldDtoList = new ArrayList<>();
        List<CustomField> customFieldList = this.baseMapper.selectCustomField(customFieldFilter);
        if(CollectionUtil.isNotEmpty(customFieldList)) {
            CustomFieldDto customFieldDto;
            for (CustomField customField : customFieldList) {
                customFieldDto = new CustomFieldDto();
                BeanUtils.copyProperties(customField, customFieldDto);

                // 获取自定义字段数据源列表
                List<CustomFieldDataSource> dataSourceList = customFieldDataSourceService.listDataSource(customField.getFieldId());
                customFieldDto.setCustomFieldDataSourceList(dataSourceList);
                customFieldDtoList.add(customFieldDto);
            }
        }
        return customFieldDtoList;
    }

    /**
     *  根据id查询自定义字段
     * @param fieldId 自定义字段dto
     * @return
     */
    @Override
    public CustomFieldDto findCustomFieldById(Long fieldId) {
        CustomField customField = new CustomField().selectById(fieldId);
        CustomFieldDto customFieldDto = new CustomFieldDto();
        BeanUtils.copyProperties(customField, customFieldDto);
        /* TODO 装入自定义字段数据源列表 */
        customFieldDto.setCustomFieldDataSourceList(customFieldDataSourceService.listDataSource(fieldId));
        return customFieldDto;
    }

    /**
     *  修改自定义字段
     * @param customFieldDto 自定义字段dto
     */
    @Override
    public void updateCustomField(CustomFieldDto customFieldDto) {
        Assert.notNull(customFieldDto, "customFieldDto 不能为 NULL");
        CustomField customField = new CustomField();
        BeanUtils.copyProperties(customFieldDto, customField);
        customField.updateById();
        /* TODO 更新策略 删除再添加 */
        customFieldDataSourceService.deleteDataSource(customField.getFieldId());
        customFieldDataSourceService.insertDataSourceList(customFieldDto);
    }

    /**
     *  删除自定义字段
     * @param fieldId  自定义字段id
     */
    @Override
    public void deleteCustomField(Long fieldId) {
        CustomField customField = new CustomField();
        customField.deleteById(fieldId);
        // 删除自定义字段数据源列表
        customFieldDataSourceService.deleteDataSource(fieldId);
    }

    @Override
    public void save(CustomFieldDto customFieldDto, UserInfo userInfo) {
        StringBuilder builder = new StringBuilder();
        if(StringUtils.isEmpty(customFieldDto.getFieldName())){
            builder.append("字段名称不能为空");
        }
        if(LongUtil.isZero(customFieldDto.getCorpId())) {
            builder.append("企业不能为空");
        }
        if(customFieldDto.getFieldType() == 0 ){
            builder.append("字段类型不能为空");
        }
        if(StringUtils.isEmpty(customFieldDto.getCommon())) {
            builder.append("需要选择是否为公用字段");
        }
        if(builder.length() > 0) {
            throw new AppException(builder.toString());
        }
        CustomField customField = new CustomField();
        BeanUtils.copyProperties(customFieldDto, customField);
        QueryWrapper<CustomField> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("field_name",customField.getFieldName());
        if("Y".equals(customField.getCommon())) {
            queryWrapper.eq("common",customField.getCommon());
        } else {
            queryWrapper.eq("form_type",customField.getFormType());
        }
        queryWrapper.eq("field_type",customField.getFieldType());
        queryWrapper.eq("corp_id",customField.getCorpId());
        List<CustomField> list = this.baseMapper.selectList(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)) {
            throw new AppException("该字段已经存在");
        }
        customField.setFieldId(KeyUtil.getId());
        customField.setOperator(userInfo.getUserId());
        customField.setOperateTime(DateUtil.date().toTimestamp());
        customField.insert();
        //添加数据源
        customFieldDto.setOperator(customField.getOperator());
        customFieldDto.setFieldId(customField.getFieldId());
        customFieldDataSourceService.insertDataSourceList(customFieldDto);
    }

    @Override
    public void update(CustomFieldDto customFieldDto, UserInfo userInfo) {
        StringBuilder builder = new StringBuilder();
        if(StringUtils.isEmpty(customFieldDto.getFieldName())){
            builder.append("字段名称不能为空");
        }
        if(LongUtil.isZero(customFieldDto.getCorpId())) {
            builder.append("企业不能为空");
        }
        if(LongUtil.isZero(customFieldDto.getFieldId())) {
            builder.append("字段编号不能为空");
        }
        if(customFieldDto.getFieldType() == 0 ){
            builder.append("字段类型不能为空");
        }
        if(StringUtils.isEmpty(customFieldDto.getCommon())) {
            builder.append("是否公用字段不能为空");
        }
        if(builder.length() > 0) {
            throw new AppException(builder.toString());
        }
        CustomField customField = new CustomField();
        BeanUtils.copyProperties(customFieldDto, customField);
        QueryWrapper<CustomField> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("field_name",customField.getFieldName());
        queryWrapper.eq("field_type",customField.getFieldType());
        if("Y".equals(customField.getCommon())) {
            queryWrapper.eq("common",customField.getCommon());
        } else {
            queryWrapper.eq("form_type",customField.getFormType());
        }
        queryWrapper.eq("corp_id",customField.getCorpId());
        queryWrapper.ne("field_id",customField.getFieldId());
        List<CustomField> list = this.baseMapper.selectList(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)) {
            throw new AppException("该字段已经存在");
        }
        customFieldDto.setOperator(userInfo.getUserId());
        customFieldDto.setOperateTime(DateUtil.date().toTimestamp());
        this.updateCustomField(customFieldDto);
    }
}
