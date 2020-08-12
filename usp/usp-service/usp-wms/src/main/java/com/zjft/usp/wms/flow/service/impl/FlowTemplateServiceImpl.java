package com.zjft.usp.wms.flow.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.wms.baseinfo.enums.EnabledEnum;
import com.zjft.usp.wms.baseinfo.service.LargeClassService;
import com.zjft.usp.wms.baseinfo.service.SmallClassService;
import com.zjft.usp.wms.flow.dto.FlowTemplateDto;
import com.zjft.usp.wms.flow.filter.FlowTemplateFilter;
import com.zjft.usp.wms.flow.model.FlowTemplate;
import com.zjft.usp.wms.flow.mapper.FlowTemplateMapper;
import com.zjft.usp.wms.flow.service.FlowTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 流程模板表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@SuppressWarnings("ALL")
@Service
public class FlowTemplateServiceImpl extends ServiceImpl<FlowTemplateMapper, FlowTemplate> implements FlowTemplateService {

    @Autowired
    private LargeClassService largeClassService;

    @Autowired
    private SmallClassService smallClassService;


    @Override
    public ListWrapper<FlowTemplateDto> pageBy(FlowTemplateFilter flowTemplateFilter) {
        IPage<FlowTemplate> page = new Page(flowTemplateFilter.getPageNum(), flowTemplateFilter.getPageSize());
        QueryWrapper<FlowTemplate> queryWrapper = new QueryWrapper();
        if (flowTemplateFilter != null) {
            /**查询条件后面陆续加*/
            if (!StringUtils.isEmpty(flowTemplateFilter.getName())) {
                queryWrapper.like("name", flowTemplateFilter.getName());
            }

            if (!StringUtils.isEmpty(flowTemplateFilter.getLargeClassId())) {
                queryWrapper.eq("large_class_id", flowTemplateFilter.getLargeClassId());
            }

            if (!StringUtils.isEmpty(flowTemplateFilter.getSmallClassId())) {
                queryWrapper.eq("small_class_id", flowTemplateFilter.getSmallClassId());
            }

            if (!StringUtils.isEmpty(flowTemplateFilter.getSortNo())) {
                queryWrapper.eq("sort_no", flowTemplateFilter.getSortNo());
            }

            if (!StringUtils.isEmpty(flowTemplateFilter.getEnabled())) {
                queryWrapper.eq("enabled", flowTemplateFilter.getEnabled());
            }

            if (LongUtil.isNotZero(flowTemplateFilter.getCorpId())) {
                queryWrapper.eq("corp_id", flowTemplateFilter.getCorpId());
            }

            queryWrapper.orderByAsc("sort_no");

        }
        IPage<FlowTemplate> iPage = this.page(page, queryWrapper);
        List<FlowTemplate> flowTemplateList = iPage.getRecords();
        List<FlowTemplateDto> flowTemplateDtoList = getFlowTemplateDtoList(flowTemplateFilter.getCorpId(), flowTemplateList);

        return ListWrapper.<FlowTemplateDto>builder()
                .list(flowTemplateDtoList)
                .total(iPage.getTotal())
                .build();
    }

    /**
     * 内部通用方法（将model转换成dto）
     *
     * @param corpId
     * @param flowTemplateList
     * @return
     */
    private List<FlowTemplateDto> getFlowTemplateDtoList(Long corpId, List<FlowTemplate> flowTemplateList) {
        List<FlowTemplateDto> flowTemplateDtoList = new ArrayList<>();
        if (flowTemplateList != null && flowTemplateList.size() > 0) {

            Map<Integer, String> largeClassIdAndName = largeClassService.mapClassIdAndName(corpId);
            Map<Integer, String> smallClassIdAndName = smallClassService.mapClassIdAndName(corpId);
            for (FlowTemplate entity : flowTemplateList) {
                /** Dto转换 */
                FlowTemplateDto tmpDto = new FlowTemplateDto();
                BeanUtils.copyProperties(entity, tmpDto);

                /** id与name转换 */
                if (entity.getLargeClassId() > 0) {
                    tmpDto.setLargeClassName(largeClassIdAndName.get(entity.getLargeClassId()));
                }
                if (entity.getSmallClassId() > 0) {
                    tmpDto.setSmallClassName(smallClassIdAndName.get(entity.getSmallClassId()));

                }
                flowTemplateDtoList.add(tmpDto);
            }
        }
        return flowTemplateDtoList;
    }

    @Override
    public void add(FlowTemplate flowTemplate, UserInfo userInfo) {
        flowTemplate.setCreateBy(userInfo.getUserId());
        flowTemplate.setCreateTime(DateUtil.date().toTimestamp());
        flowTemplate.setEnabled(EnabledEnum.YES.getCode());
        flowTemplate.setUpdateBy(0L);
        if (flowTemplate.getId() == null || flowTemplate.getId() <= 0) {
            flowTemplate.setId(KeyUtil.getId());
        }
        super.save(flowTemplate);
    }

    @Override
    public void mod(FlowTemplate requestFlowTemplate, UserInfo userInfo) {
        FlowTemplate newFlowTemplate = new FlowTemplate();
        newFlowTemplate.setId(requestFlowTemplate.getId());
        newFlowTemplate.setEnabled(requestFlowTemplate.getEnabled());
        newFlowTemplate.setDescription(requestFlowTemplate.getDescription());
        newFlowTemplate.setSortNo(requestFlowTemplate.getSortNo());
        newFlowTemplate.setName(requestFlowTemplate.getName());
        newFlowTemplate.setUpdateBy(userInfo.getUserId());
        newFlowTemplate.setUpdateTime(DateUtil.date().toTimestamp());
        super.updateById(newFlowTemplate);
    }

    @Override
    public FlowTemplate findLatestEnabled(Long corpId, Integer largeClassId, Integer smallClassId) {
        QueryWrapper<FlowTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("corp_id", corpId);
        queryWrapper.eq("large_class_id", largeClassId);
        queryWrapper.eq("small_class_id", smallClassId);
        queryWrapper.eq("enabled", EnabledEnum.YES.getCode());
        queryWrapper.orderByDesc("create_time");
        queryWrapper.last("LIMIT 1");
        return super.getOne(queryWrapper);
    }


}
