package com.zjft.usp.wms.form.dto;

import com.zjft.usp.wms.form.model.FormTemplate;
import com.zjft.usp.wms.form.model.FormTemplateField;
import lombok.Data;

import java.util.List;

/**
 * 表单模板DTO类
 *
 * @Author: JFZOU
 * @Date: 2019-11-07 8:56
 */
@Data
public class FormTemplateDto extends FormTemplate {
    /**
     * 业务大类名称
     */
    private String largeClassName;
    /**
     * 业务小类名称
     */
    private String smallClassName;

    /**
     * 添加时引用的表单模板ID
     */
    private Long referId;

    /**
     * 表单模板字段维护时使用
     */
    private List<FormTemplateField> fieldList;
}
