package com.zjft.usp.wms.flow.dto;

import com.zjft.usp.wms.flow.model.FlowInstanceNode;
import com.zjft.usp.wms.flow.model.FlowTemplateNode;
import com.zjft.usp.wms.form.model.FormTemplate;
import lombok.Data;

/**
 * @Author: JFZOU
 * @Date: 2019-11-11 9:11
 */
@Data
public class FlowInstanceNodeDto extends FlowInstanceNode {

    /**
     * 流程模板节点对象
     */
    private FlowTemplateNode flowTemplateNode;

    /**
     * 表单模板对象
     */
    private FormTemplate formTemplate;
}
