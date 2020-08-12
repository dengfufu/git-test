package com.zjft.usp.wms.form.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.wms.form.enums.FieldTypeEnum;
import com.zjft.usp.wms.form.model.FormTemplate;
import com.zjft.usp.wms.form.model.FormTemplateField;
import com.zjft.usp.wms.form.mapper.FormTemplateFieldMapper;
import com.zjft.usp.wms.form.service.FormTemplateFieldService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.wms.form.service.FormTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 表单模板字段表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FormTemplateFieldServiceImpl extends ServiceImpl<FormTemplateFieldMapper, FormTemplateField> implements FormTemplateFieldService {

    @Autowired
    private FormTemplateService formTemplateService;

    @Override
    public void addByCopy(Long oldFormTemplateId, Long newFormTemplateId) {
        List<FormTemplateField> findList = this.listBy(oldFormTemplateId);
        if (findList != null && !findList.isEmpty()) {
            for (FormTemplateField findEntity : findList) {
                /** 创建新的ID */
                findEntity.setId(KeyUtil.getId());
                /** 使用新的表单模板ID */
                findEntity.setFormTemplateId(newFormTemplateId);
            }
        }
        super.saveBatch(findList);
    }

    @Override
    public boolean addField(FormTemplateField formTemplateField) {
        if (formTemplateField != null && formTemplateField.getId() <= 0) {
            formTemplateField.setId(KeyUtil.getId());
        }
        this.checkCommon(formTemplateField);
        return super.save(formTemplateField);
    }

    @Override
    public boolean modField(FormTemplateField formTemplateField, UserInfo userInfo) {
        FormTemplate formTemplate = new FormTemplate();
        formTemplate.setId(formTemplateField.getFormTemplateId());
        formTemplate.setUpdateBy(userInfo.getUserId());
        formTemplate.setUpdateTime(DateUtil.date().toTimestamp());
        this.formTemplateService.updateById(formTemplate);
        return super.updateById(formTemplateField);
    }

    @Override
    public void addList(List<FormTemplateField> formTemplateFieldList) {
        if (formTemplateFieldList == null || formTemplateFieldList.isEmpty()) {
            return;
        }

        /**先删除全部*/
        FormTemplateField oneFormTemplateField = formTemplateFieldList.get(0);
        if (oneFormTemplateField != null) {
            this.deleteByTemplateId(oneFormTemplateField.getFormTemplateId());
        }
        /**再全部插入，原来的field_id不能变*/
        this.saveBatch(formTemplateFieldList);
    }


    @Override
    public List<FormTemplateField> listAllBy(Long templateId) {
        return this.listBy(templateId);
    }


    /**
     * 校验必填项
     *
     * @param formTemplateField
     */
    private void checkCommon(FormTemplateField formTemplateField) {
        if (formTemplateField != null) {
            if (LongUtil.isZero(formTemplateField.getId())) {
                throw new AppException("字段编号不能为空！");
            }
            if (LongUtil.isZero(formTemplateField.getFormTemplateId())) {
                throw new AppException("表单模板编号不能为空！");
            }
            if (StringUtils.isEmpty(formTemplateField.getTableName())) {
                throw new AppException("数据库表名不能为空！");
            }
            if (StringUtils.isEmpty(formTemplateField.getFieldCode())) {
                throw new AppException("字段编码不能为空！");
            }
            if (formTemplateField.getFieldClass() == null || formTemplateField.getFieldClass().intValue() <= 0) {
                throw new AppException("字段分类不能为空！");
            }
            if (StringUtils.isEmpty(formTemplateField.getFieldName())) {
                throw new AppException("字段名称不能为空！");
            }

            if (formTemplateField.getFieldType() == null || formTemplateField.getFieldType().intValue() <= 0) {
                throw new AppException("字段类型不能为空！");
            }

            /**如果是自定义列表，则custom_list_m_id不能为空*/
            if (formTemplateField.getFieldType().intValue() == FieldTypeEnum.CUSTOM_LIST.getCode()) {
                if (LongUtil.isZero(formTemplateField.getCustomListMainId())) {
                    throw new AppException("自定义列表不能为空！");
                }
            }
        }
    }


    @Override
    public void deleteByTemplateId(Long templateId) {
        if (LongUtil.isZero(templateId)) {
            throw new AppException("表单模板传值错误，无法删除！");
        }
        this.remove(new QueryWrapper<FormTemplateField>().eq("form_template_id", templateId));
    }


    /**
     * 根据表单模板ID获得列表
     *
     * @param formTemplateId
     * @return
     */
    private List<FormTemplateField> listBy(Long formTemplateId) {
        QueryWrapper<FormTemplateField> queryWrapper = new QueryWrapper<>();
        //根据表单模板ID获得表单字段列表
        if (LongUtil.isNotZero(formTemplateId)) {
            queryWrapper.eq("form_template_id", formTemplateId);
        }
        queryWrapper.orderByAsc("sort_no");
        return this.list(queryWrapper);
    }
}
