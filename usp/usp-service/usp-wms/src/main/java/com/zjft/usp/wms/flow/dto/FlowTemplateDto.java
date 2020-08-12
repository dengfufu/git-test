package com.zjft.usp.wms.flow.dto;

import com.zjft.usp.wms.flow.model.FlowTemplate;
import lombok.Data;

import java.util.List;

/**流程模板定义全局DTO类，用于封装FlowTemplate、FlowTemplateNode、FlowTemplateReverseDef
 * @Author: JFZOU
 * @Date: 2019-11-07 16:43
 */
@Data
public class FlowTemplateDto extends FlowTemplate {
    /**
     * 业务大类名称
     */
    private String largeClassName;
    /**
     * 业务小类名称
     */
    private String smallClassName;


    /**
     * 获得业务类型节点列表（含节点基本信息+处理人列表）
     */
    private List<FlowTemplateNodeDto> flowTemplateNodeDtoList;



}
