package com.zjft.usp.wms.form.service;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.wms.form.dto.FormTemplateDto;
import com.zjft.usp.wms.form.filter.FormTemplateFilter;
import com.zjft.usp.wms.form.model.FormTemplate;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 表单模板主表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface FormTemplateService extends IService<FormTemplate> {

    /**
     * 查询表单模板不分页(推荐使用)
     *
     * @param formTemplateDto 表单模板Dto对象
     * @return List<FormTemplateDto>
     */
    List<FormTemplateDto> listBy(FormTemplateDto formTemplateDto);

    /**
     * 查询表单模板分页(推荐使用)
     *
     * @param formTemplateFilter
     * @return
     */
    public ListWrapper<FormTemplateDto> pageBy(FormTemplateFilter formTemplateFilter);


    /**
     * 查询表单模板（用于作为选择列表进行选择）
     *
     * @param formTemplateDto
     * @return
     */
    List<FormTemplate> listForSelectBy(FormTemplateDto formTemplateDto);

    /**
     * 通过复制添加表单模板(推荐使用)
     *
     * @param formTemplateDto
     * @param userInfo
     * @return
     * @throws Exception
     */
    boolean addByCopy(FormTemplateDto formTemplateDto, UserInfo userInfo);

    /**
     * 修改表单模板基本信息(推荐使用)
     *
     * @param formTemplateDto
     * @param userInfo
     */
    void modBaseInfo(FormTemplateDto formTemplateDto, UserInfo userInfo);

    /**
     * 修改表单模板字段配置(推荐使用)
     *
     * @param formTemplateDto
     * @param userInfo
     */
    void modFieldList(FormTemplateDto formTemplateDto, UserInfo userInfo);

    /**
     * 删除表单模板(推荐使用)
     *
     * @param id
     */
    void deleteById(long id);

}
