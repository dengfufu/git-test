package com.zjft.usp.wms.baseinfo.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.wms.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 快递公司表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("express_company")
@ApiModel(value="ExpressCompany对象", description="快递公司表")
public class ExpressCompany implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "快递公司ID")
    @TableId("id")
    private Long id;


    @ApiModelProperty(value = "企业ID")
    private Long corpId;

    @ApiModelProperty(value = "快递公司名称")
    private String name;

    @ApiModelProperty(value = "顺序号")
    private Integer sortNo;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "是否可用 (Y=是,N=否)")
    private String enabled;

    @ApiModelProperty(value = "创建人")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private Long updateBy;

    @ApiModelProperty(value = "修改时间(先默认当前时间，修改再刷新)")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date updateTime;


}
