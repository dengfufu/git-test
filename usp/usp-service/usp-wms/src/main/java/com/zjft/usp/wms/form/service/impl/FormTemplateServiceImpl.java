package com.zjft.usp.wms.form.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.wms.baseinfo.enums.EnabledEnum;
import com.zjft.usp.wms.baseinfo.service.LargeClassService;
import com.zjft.usp.wms.baseinfo.service.SmallClassService;
import com.zjft.usp.wms.flow.service.FlowTemplateNodeService;
import com.zjft.usp.wms.form.dto.FormTemplateDto;
import com.zjft.usp.wms.form.enums.SysBuildInEnum;
import com.zjft.usp.wms.form.filter.FormTemplateFilter;
import com.zjft.usp.wms.form.model.FormTemplate;
import com.zjft.usp.wms.form.mapper.FormTemplateMapper;
import com.zjft.usp.wms.form.service.FormTemplateFieldService;
import com.zjft.usp.wms.form.service.FormTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 表单模板主表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FormTemplateServiceImpl extends ServiceImpl<FormTemplateMapper, FormTemplate> implements FormTemplateService {

    @Autowired
    private LargeClassService largeClassService;

    @Autowired
    private SmallClassService smallClassService;

    @Autowired
    private FormTemplateFieldService formTemplateFieldService;

    @Autowired
    private FlowTemplateNodeService flowTemplateNodeService;

    @Override
    public List<FormTemplateDto> listBy(FormTemplateDto formTemplateDto) {

        QueryWrapper<FormTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("corp_id", formTemplateDto.getCorpId());
        List<FormTemplate> formTemplateList = this.list(queryWrapper);
        List<FormTemplateDto> formTemplateDtoList = getFormTemplateDtoList(formTemplateDto.getCorpId(), formTemplateList);
        return formTemplateDtoList;
    }

    @Override
    public List<FormTemplate> listForSelectBy(FormTemplateDto formTemplateDto) {
        QueryWrapper<FormTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("corp_id", formTemplateDto.getCorpId());
        return this.list(queryWrapper);
    }

    /**
     * 内部通用方法（将model转换成dto）
     *
     * @param corpId
     * @param formTemplateList
     * @return
     */
    private List<FormTemplateDto> getFormTemplateDtoList(Long corpId, List<FormTemplate> formTemplateList) {
        List<FormTemplateDto> formTemplateDtoList = new ArrayList<>();
        if (formTemplateList != null && formTemplateList.size() > 0) {

            Map<Integer, String> largeClassIdAndName = largeClassService.mapClassIdAndName(corpId);
            Map<Integer, String> smallClassIdAndName = smallClassService.mapClassIdAndName(corpId);
            for (FormTemplate entity : formTemplateList) {
                /** Dto转换 */
                FormTemplateDto tmpDto = new FormTemplateDto();
                BeanUtils.copyProperties(entity, tmpDto);

                /** id与name转换 */
                if (entity.getLargeClassId() > 0) {
                    tmpDto.setLargeClassName(largeClassIdAndName.get(entity.getLargeClassId()));
                }
                if (entity.getSmallClassId() > 0) {
                    tmpDto.setSmallClassName(smallClassIdAndName.get(entity.getSmallClassId()));

                }
                formTemplateDtoList.add(tmpDto);
            }
        }
        return formTemplateDtoList;
    }

    @Override
    public ListWrapper<FormTemplateDto> pageBy(FormTemplateFilter formTemplateFilter) {
        IPage<FormTemplate> page = new Page(formTemplateFilter.getPageNum(), formTemplateFilter.getPageSize());
        QueryWrapper<FormTemplate> queryWrapper = new QueryWrapper();
        if (formTemplateFilter != null) {
            /**查询条件后面陆续加*/
            if (!StringUtils.isEmpty(formTemplateFilter.getLargeClassId())) {
                queryWrapper.eq("large_class_id", formTemplateFilter.getLargeClassId());
            }
            if (!StringUtils.isEmpty(formTemplateFilter.getSmallClassId())) {
                queryWrapper.eq("small_class_id", formTemplateFilter.getSmallClassId());
            }
            if (!StringUtils.isEmpty(formTemplateFilter.getName())) {
                queryWrapper.like("name", formTemplateFilter.getName());
            }
            if (!StringUtils.isEmpty(formTemplateFilter.getSortNo())) {
                queryWrapper.eq("sort_no", formTemplateFilter.getSortNo());
            }
            if (!StringUtils.isEmpty(formTemplateFilter.getSysBuildIn())) {
                queryWrapper.eq("sys_build_in", formTemplateFilter.getSysBuildIn());
            }
            if (!StringUtils.isEmpty(formTemplateFilter.getTableClass())) {
                queryWrapper.eq("table_class", formTemplateFilter.getTableClass());
            }
            if (!StringUtils.isEmpty(formTemplateFilter.getEnabled())) {
                queryWrapper.eq("enabled", formTemplateFilter.getEnabled());
            }
            if (LongUtil.isNotZero(formTemplateFilter.getCorpId())) {
                queryWrapper.eq("corp_id", formTemplateFilter.getCorpId());
            }
            queryWrapper.orderByAsc("sort_no");
        }
        IPage<FormTemplate> iPage = this.page(page, queryWrapper);
        List<FormTemplate> formTemplateList = iPage.getRecords();
        List<FormTemplateDto> formTemplateDtoList = getFormTemplateDtoList(formTemplateFilter.getCorpId(), formTemplateList);

        return ListWrapper.<FormTemplateDto>builder()
                .list(formTemplateDtoList)
                .total(iPage.getTotal())
                .build();
    }

    @Override
    public boolean addByCopy(FormTemplateDto formTemplateDto, UserInfo userInfo) {
        if (formTemplateDto == null) {
            throw new AppException("数据传输发生内部错误，请重试！");
        }

        if (StringUtils.isEmpty(formTemplateDto.getName())) {
            throw new AppException("表单模板名称不能为空！");
        }

        if (LongUtil.isZero(formTemplateDto.getReferId())) {
            throw new AppException("引用模板不能为空！");
        }

        /**查找原有模板基本信息*/
        FormTemplate oldFormTemplate = this.getById(formTemplateDto.getReferId());
        if (oldFormTemplate == null) {
            throw new AppException("引用模板已不存在，请检查！");
        }

        FormTemplate newFormTemplate = new FormTemplate();
        /**利用工具把原表单模板属性复制到新对象中*/
        BeanUtils.copyProperties(oldFormTemplate, newFormTemplate);
        /**新表单模板ID*/
        newFormTemplate.setId(KeyUtil.getId());
        /**新表单模板名称*/
        newFormTemplate.setName(formTemplateDto.getName());
        /**新表单模板是否系统内置为否*/
        newFormTemplate.setSysBuildIn(SysBuildInEnum.NO.getCode());
        /**置为已启用*/
        newFormTemplate.setEnabled(EnabledEnum.YES.getCode());

        this.add(newFormTemplate, userInfo);

        /**将原来的表单模板自动置为不可用。*/
        this.updateDisable(oldFormTemplate.getId(), userInfo.getUserId());
        /**查找原有模板字段信息，复制原有模板字段。*/
        formTemplateFieldService.addByCopy(oldFormTemplate.getId(), newFormTemplate.getId());

        return true;
    }

    @Override
    public void modBaseInfo(FormTemplateDto formTemplateDto, UserInfo userInfo) {
        /**新创建一个对象，仅保存可以修改值，不能修改的值不要设置属性，增强安全性*/
        FormTemplate newFormTemplate = new FormTemplate();
        /**主键*/
        newFormTemplate.setId(formTemplateDto.getId());
        /** 可修改项 */
        newFormTemplate.setName(formTemplateDto.getName());
        newFormTemplate.setSortNo(formTemplateDto.getSortNo());
        newFormTemplate.setEnabled(formTemplateDto.getEnabled());
        newFormTemplate.setDescription(formTemplateDto.getDescription());

        /** 系统自动设置修改人、修改时间 */
        newFormTemplate.setUpdateBy(userInfo.getUserId());
        newFormTemplate.setUpdateTime(DateUtil.date().toTimestamp());

        super.updateById(newFormTemplate);
    }

    @Override
    public void modFieldList(FormTemplateDto formTemplateDto, UserInfo userInfo) {
        /**新创建一个对象，仅保存可以修改值，不能修改的值不要设置属性，增强安全性*/
        FormTemplate newFormTemplate = new FormTemplate();
        /**主键*/
        newFormTemplate.setId(formTemplateDto.getId());
        /** 系统自动设置修改人、修改时间 */
        newFormTemplate.setUpdateBy(userInfo.getUserId());
        newFormTemplate.setUpdateTime(DateUtil.date().toTimestamp());
        super.updateById(newFormTemplate);

        /** 修改字段配置 */
        formTemplateFieldService.addList(formTemplateDto.getFieldList());
    }

    @Override
    public void deleteById(long id) {

        FormTemplate findFormTemplate = this.getById(id);
        if (findFormTemplate == null) {
            throw new AppException("表单模板已不存在，无法删除，请检查！");
        }

        /**系统内置模板不能删除*/
        if (SysBuildInEnum.YES.getCode().equalsIgnoreCase(findFormTemplate.getSysBuildIn())) {
            throw new AppException("该表单模板为系统内置模板，不允许删除，请检查！");
        }

        /**流程模板节点表中有form_main_id引用不能删除*/
        boolean isUsed = flowTemplateNodeService.isExistByFormMainId(findFormTemplate.getId());
        if (isUsed) {
            throw new AppException("该表单模板已被流程模板引用，不允许删除，只能置为禁用，请检查！");
        }
        /**删除表单模板*/
        super.removeById(id);
        /**删除表单模板字段*/
        formTemplateFieldService.deleteByTemplateId(id);
    }

    /**
     * 置为可用
     *
     * @param formTemplateId 表单模板ID
     */
    public void updateEnable(long formTemplateId, long userId) {

        this.updateEnabled(formTemplateId, userId, EnabledEnum.YES.getCode());
    }

    /**
     * 置为禁用
     *
     * @param formTemplateId 表单模板ID
     * @param userId         用户ID
     */
    public void updateDisable(long formTemplateId, long userId) {
        this.updateEnabled(formTemplateId, userId, EnabledEnum.NO.getCode());
    }

    /**
     * 置为可用/禁用
     *
     * @param formTemplateId 表单模板ID
     * @param userId         用户ID
     * @param enabled        参数值
     */
    private void updateEnabled(long formTemplateId, long userId, String enabled) {
        FormTemplate tmpFormTemplate = new FormTemplate();
        tmpFormTemplate.setId(formTemplateId);
        tmpFormTemplate.setEnabled(enabled);
        tmpFormTemplate.setUpdateBy(userId);
        tmpFormTemplate.setUpdateTime(DateUtil.date().toTimestamp());
        super.updateById(tmpFormTemplate);
    }

    /**
     * 添加新的表单模板
     *
     * @param formTemplate 表单模板对象
     * @param userInfo     登录用户对象
     * @return
     * @throws Exception
     */
    private boolean add(FormTemplate formTemplate, UserInfo userInfo) {
        formTemplate.setCreateBy(userInfo.getUserId());
        formTemplate.setCreateTime(DateUtil.date().toTimestamp());
        formTemplate.setEnabled(EnabledEnum.YES.getCode());
        formTemplate.setUpdateBy(0L);
        formTemplate.setUpdateTime(null);
        if (formTemplate.getId() == null || formTemplate.getId() <= 0) {
            formTemplate.setId(KeyUtil.getId());
        }
        return super.save(formTemplate);
    }

}
