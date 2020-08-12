package com.zjft.usp.wms.form.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: JFZOU
 * @Date: 2019-11-20 14:34
 */

@Data
public class FormTemplateFilter extends Page {


    @ApiModelProperty(value = "企业ID")
    private Long corpId;

    @ApiModelProperty(value = "表单模板名称")
    private String name;

    @ApiModelProperty(value = "业务大类ID")
    private Integer largeClassId;

    @ApiModelProperty(value = "业务小类ID")
    private Integer smallClassId;

    @ApiModelProperty(value = "是否系统内置 (Y=是,N=否)")
    private String sysBuildIn;

    @ApiModelProperty(value = "数据库表分类(10=业务主表20=业务明细表)")
    private Integer tableClass;

    @ApiModelProperty(value = "顺序号")
    private Integer sortNo;

    @ApiModelProperty(value = "是否可用 (Y=是,N=否)")
    private String enabled;
}
