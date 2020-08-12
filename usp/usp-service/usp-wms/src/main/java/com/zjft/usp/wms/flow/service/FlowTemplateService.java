package com.zjft.usp.wms.flow.service;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.wms.flow.dto.FlowTemplateDto;
import com.zjft.usp.wms.flow.filter.FlowTemplateFilter;
import com.zjft.usp.wms.flow.model.FlowTemplate;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 流程模板表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface FlowTemplateService extends IService<FlowTemplate> {

    /**
     * 分页查询流程模板
     *
     * @param flowTemplateFilter
     * @return
     */
    ListWrapper<FlowTemplateDto> pageBy(FlowTemplateFilter flowTemplateFilter);

    /**
     * 添加流程模板主表
     *
     * @param flowTemplate
     * @param userInfo
     */
    void add(FlowTemplate flowTemplate, UserInfo userInfo);

    /**
     * 修改流程模板主表
     *
     * @param flowTemplate
     * @param userInfo
     * @throws Exception
     */
    void mod(FlowTemplate flowTemplate, UserInfo userInfo);

    /**
     * 获得最新的流程模板
     *
     * @param corpId       企业ID
     * @param largeClassId 业务大类
     * @param smallClassId 业务小类
     * @return
     */
    FlowTemplate findLatestEnabled(Long corpId, Integer largeClassId, Integer smallClassId);


}
