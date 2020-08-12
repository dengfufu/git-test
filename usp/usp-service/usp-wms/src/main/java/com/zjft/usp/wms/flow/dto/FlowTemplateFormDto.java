package com.zjft.usp.wms.flow.dto;

import com.zjft.usp.wms.flow.model.FlowTemplate;
import com.zjft.usp.wms.form.model.FormTemplate;
import com.zjft.usp.wms.form.model.FormTemplateField;
import lombok.Data;

import java.util.List;

/**
 * 封装当前节点所需要的流程模板、表单模板
 *
 * @Author: JFZOU
 * @Date: 2019-11-12 10:28
 */

@Data
public class FlowTemplateFormDto {
    /**
     * 流程模板
     */
    private FlowTemplate flowTemplate;

    /**
     * 表单模板-业务主表
     */
    private FormTemplate mainFormTemplate;

    /**
     * 表单模板-业务主表字段列表
     */
    private List<FormTemplateField> mainFormTemplateFieldList;

    /**
     * 表单模板-业务明细表
     */
    private FormTemplate detailFormTemplate;

    /**
     * 表单模板-业务明细表字段列表
     */
    private List<FormTemplateField> detailFormTemplateFieldList;

}
