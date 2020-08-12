package com.zjft.usp.anyfix.corp.manage.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * 服务委托方与用户企业关系表
 * </p>
 *
 * @author canlei
 * @since 2019-10-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("demander_custom")
@ApiModel(value="DemanderCustom对象", description="服务委托方与用户企业关系表")
public class DemanderCustom implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键编号")
    @TableId(value = "custom_id")
    private Long customId;

    @ApiModelProperty(value = "服务委托方企业编号")
    private Long demanderCorp;

    @ApiModelProperty(value = "用户企业编号")
    private Long customCorp;

    @ApiModelProperty(value = "用户企业名称")
    private String customCorpName;

    @ApiModelProperty(value = "是否可用，Y=可用，N=不可用")
    private String enabled;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    private Date operateTime;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DemanderCustom that = (DemanderCustom) o;
        return customCorp.equals(that.customCorp) &&
                customCorpName.equals(that.customCorpName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customCorp, customCorpName);
    }
}
