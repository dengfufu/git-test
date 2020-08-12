package com.zjft.usp.wms.flow.composite.impl;

import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.wms.flow.composite.FlowTemplateFormCompoService;
import com.zjft.usp.wms.flow.dto.FlowTemplateFormAllDto;
import com.zjft.usp.wms.flow.dto.FlowTemplateFormDto;
import com.zjft.usp.wms.flow.dto.FlowTemplateNodeDto;
import com.zjft.usp.wms.flow.model.FlowTemplate;
import com.zjft.usp.wms.flow.model.FlowTemplateNode;
import com.zjft.usp.wms.flow.service.FlowTemplateNodeService;
import com.zjft.usp.wms.flow.service.FlowTemplateService;
import com.zjft.usp.wms.form.model.FormTemplate;
import com.zjft.usp.wms.form.model.FormTemplateField;
import com.zjft.usp.wms.form.service.FormTemplateFieldService;
import com.zjft.usp.wms.form.service.FormTemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: JFZOU
 * @Date: 2019-11-12 10:40
 */
public class FlowTemplateFormCompoServiceImpl implements FlowTemplateFormCompoService {

    @Autowired
    public FlowTemplateService flowTemplateService;

    @Autowired
    public FlowTemplateNodeService flowTemplateNodeService;

    @Autowired
    public FormTemplateService formTemplateService;

    @Autowired
    public FormTemplateFieldService formTemplateFieldService;

    @Override
    public FlowTemplateFormDto findStartNodeFormDto(Long corpId, Integer largeClassId, Integer smallClassId) {

        FlowTemplateFormAllDto findFlowTemplateFormAllDto = this.findFlowTemplateFormAllDto(corpId, largeClassId, smallClassId);
        FlowTemplateFormDto flowTemplateFormDto = new FlowTemplateFormDto();
        if (findFlowTemplateFormAllDto != null) {
            flowTemplateFormDto.setFlowTemplate(findFlowTemplateFormAllDto.getFlowTemplate());

            List<FlowTemplateNodeDto> flowTemplateNodeDtoList = findFlowTemplateFormAllDto.getFlowTemplateNodeDtoList();
            if (flowTemplateNodeDtoList != null && !flowTemplateNodeDtoList.isEmpty()) {
                FlowTemplateNodeDto flowTemplateNodeDto = flowTemplateNodeDtoList.get(0);
                flowTemplateFormDto.setMainFormTemplate(flowTemplateNodeDto.getMainFormTemplate());
                flowTemplateFormDto.setMainFormTemplateFieldList(flowTemplateNodeDto.getMainFormTemplateFieldList());
                flowTemplateFormDto.setDetailFormTemplate(flowTemplateNodeDto.getDetailFormTemplate());
                flowTemplateFormDto.setDetailFormTemplateFieldList(flowTemplateNodeDto.getDetailFormTemplateFieldList());
            }
        }
        return flowTemplateFormDto;
    }

    @Override
    public FlowTemplateFormAllDto findFlowTemplateFormAllDto(Long corpId, Integer largeClassId, Integer smallClassId) {
        FlowTemplateFormAllDto flowTemplateFormAllDto = new FlowTemplateFormAllDto();


        /**1、根据业务类型查找最新启用的流程模板对象*/
        FlowTemplate flowTemplate = flowTemplateService.findLatestEnabled(corpId, largeClassId, smallClassId);
        if (flowTemplate == null) {
            throw new AppException("获得最新的可用的流程模板错误，请检查！");
        }

        /**2、根据流程模板查找最新的流程模板节点列表*/
        List<FlowTemplateNode> findEnabledNodeListBy = flowTemplateNodeService.listEnabledBy(flowTemplate.getId());
        if (findEnabledNodeListBy == null || findEnabledNodeListBy.isEmpty()) {
            throw new AppException("获得最新的可用的流程模板节点对象列表错误，请检查！");
        }

        List<FlowTemplateNodeDto> flowTemplateNodeDtoList = new ArrayList<>();
        for (FlowTemplateNode flowTemplateNode : findEnabledNodeListBy) {
            FlowTemplateNodeDto tmpDto = new FlowTemplateNodeDto();
            /**model与dto转换*/
            BeanUtils.copyProperties(flowTemplateNode, tmpDto);

            /**获得表单基本信息模板对象及相应的字段列表*/
            if (LongUtil.isNotZero(flowTemplateNode.getFormMainId())) {
                FormTemplate mainFormTemplate = formTemplateService.getById(flowTemplateNode.getFormMainId());
                tmpDto.setMainFormTemplate(mainFormTemplate);
                List<FormTemplateField> queryMainFieldBy = this.formTemplateFieldService.listAllBy(flowTemplateNode.getFormMainId());
                tmpDto.setMainFormTemplateFieldList(queryMainFieldBy);
            }

            /**获得表单明细信息模板对象及相应的字段列表*/
            if (LongUtil.isNotZero(flowTemplateNode.getFormDetailId())) {
                FormTemplate detailFormTemplate = formTemplateService.getById(flowTemplateNode.getFormDetailId());
                tmpDto.setDetailFormTemplate(detailFormTemplate);
                List<FormTemplateField> queryDetailFieldBy = this.formTemplateFieldService.listAllBy(flowTemplateNode.getFormDetailId());
                tmpDto.setDetailFormTemplateFieldList(queryDetailFieldBy);
            }

            flowTemplateNodeDtoList.add(tmpDto);
        }


        flowTemplateFormAllDto.setFlowTemplate(flowTemplate);
        flowTemplateFormAllDto.setFlowTemplateNodeDtoList(flowTemplateNodeDtoList);
        return flowTemplateFormAllDto;
    }
}
