package com.zjft.usp.wms.flow.dto;


import lombok.Data;

/**
 * @Author: JFZOU
 * @Date: 2019-11-15 14:06
 */
@Data
public class FlowTemplateCopyDto {

    /**
     * 被复制的流程模板ID
     */
    private Long sourceFlowTemplateId;
    /**
     * 被复制的流程模板是否置为可用
     */
    private String sourceEnabled;
    /**
     * 新流程模板名称
     */
    private String newFlowTemplateName;
}
