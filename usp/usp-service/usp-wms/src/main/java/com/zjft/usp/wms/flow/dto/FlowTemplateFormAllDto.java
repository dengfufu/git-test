package com.zjft.usp.wms.flow.dto;

import com.zjft.usp.wms.flow.model.FlowTemplate;
import com.zjft.usp.wms.flow.model.FlowTemplateNode;
import com.zjft.usp.wms.form.model.FormTemplate;
import com.zjft.usp.wms.form.model.FormTemplateField;
import lombok.Data;

import java.util.List;

/**
 * 封装流程模板所有数据、表单模板所有数据
 *
 * @Author: JFZOU
 * @Date: 2019-11-12 10:28
 */

@Data
public class FlowTemplateFormAllDto {
    /**
     * 流程模板
     */
    private FlowTemplate flowTemplate;

    /**
     * 流程模板节点列表
     */
    private List<FlowTemplateNodeDto> flowTemplateNodeDtoList;

}
