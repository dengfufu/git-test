package com.zjft.usp.anyfix.baseinfo.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 快递公司
 * </p>
 *
 * @author zgpi
 * @since 2020-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("express_company")
@ApiModel(value="ExpressCompany对象", description="快递公司")
public class ExpressCompany implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "快递公司ID")
    @TableId(value = "id")
    private Long id;

    @ApiModelProperty(value = "快递公司名称")
    private String name;

    @ApiModelProperty(value = "委托商或服务商企业编号")
    private Long corpId;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
