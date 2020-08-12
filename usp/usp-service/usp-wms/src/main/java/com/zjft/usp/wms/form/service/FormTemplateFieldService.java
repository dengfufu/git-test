package com.zjft.usp.wms.form.service;

import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.wms.form.model.FormTemplateField;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 表单模板字段表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface FormTemplateFieldService extends IService<FormTemplateField> {

    /**
     * 根据源表单模板ID进行复制(推荐使用，实际业务中调用此方法创建新的表单模板)
     *
     * @param oldFormTemplateId 源表单模板ID
     * @param newFormTemplateId 目标表单模板ID
     * @throws Exception
     */
    void addByCopy(Long oldFormTemplateId, Long newFormTemplateId);

    /**
     * 添加表单模板字段(备用接口，自定义字段时使用)
     *
     * @param formTemplateField
     * @return
     * @throws Exception
     */
    boolean addField(FormTemplateField formTemplateField);

    /**
     * 修改表单模板单个字段
     *
     * @param formTemplateField
     * @param userInfo
     * @return
     */
    boolean modField(FormTemplateField formTemplateField, @LoginUser UserInfo userInfo);

    /**
     * 批量添加表单模板字段
     *
     * @param formTemplateFieldList
     */
    public void addList(List<FormTemplateField> formTemplateFieldList);

    /**
     * 根据模板ID获得所有模板字段列表
     *
     * @param templateId
     * @return
     */
    List<FormTemplateField> listAllBy(Long templateId);

    /**
     * 删除表单模板字段
     *
     * @param templateId
     */
    void deleteByTemplateId(Long templateId);
}
