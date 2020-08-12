package com.zjft.usp.wms.flow.dto;

import com.zjft.usp.wms.flow.model.FlowTemplateNode;
import com.zjft.usp.wms.flow.model.FlowTemplateNodeHandler;
import com.zjft.usp.wms.form.model.FormTemplate;
import com.zjft.usp.wms.form.model.FormTemplateField;
import lombok.Data;

import java.util.List;

/**
 * 流程模板节点对象DTO类
 *
 * @Author: JFZOU
 * @Date: 2019-11-12 10:09
 */

@Data
public class FlowTemplateNodeDto extends FlowTemplateNode {

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

    /**
     * 处理人列表
     */
    private List<FlowTemplateNodeHandler> handlerList;
}
