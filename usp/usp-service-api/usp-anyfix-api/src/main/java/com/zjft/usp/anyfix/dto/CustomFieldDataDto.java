package com.zjft.usp.anyfix.dto;

import lombok.Data;

/**
 * <p>
 * 自定义字段数据表
 * </p>
 *
 * @author cxd
 * @since 2020-01-08
 */
@Data
public class CustomFieldDataDto {
    /** 数据ID **/
    private Long dataId;

    /** 业务表单类型 **/
    private Integer formType;

    /** 业务表单ID **/
    private Long formId;

    /** 字段ID **/
    private Long fieldId;

    /** 字段名称 **/
    private String fieldName;

    /** 字段类型 **/
    private Integer fieldType;

    /** 字段值 **/
    private String fieldValue;


}
