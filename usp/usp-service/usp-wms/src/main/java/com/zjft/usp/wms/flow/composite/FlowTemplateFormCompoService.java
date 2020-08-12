package com.zjft.usp.wms.flow.composite;

import com.zjft.usp.wms.flow.dto.FlowTemplateFormAllDto;
import com.zjft.usp.wms.flow.dto.FlowTemplateFormDto;

/**
 * <p>
 * 流程模板与表单模板表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-12
 */
public interface FlowTemplateFormCompoService {


    /**
     * 获得开始节点所需要的表单模板定义
     *
     * @param corpId
     * @param largeClassId
     * @param smallClassId
     * @return
     */
    FlowTemplateFormDto findStartNodeFormDto(Long corpId, Integer largeClassId, Integer smallClassId);


    /**
     * 获得流程模板所有对象（主表+明细表）+表单模板对象（主表+明细表）
     *
     * @param corpId
     * @param largeClassId
     * @param smallClassId
     * @return
     */
    FlowTemplateFormAllDto findFlowTemplateFormAllDto(Long corpId, Integer largeClassId, Integer smallClassId);
}
