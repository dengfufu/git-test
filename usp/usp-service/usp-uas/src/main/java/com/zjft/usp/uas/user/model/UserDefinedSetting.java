package com.zjft.usp.uas.user.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户自定义配置
 * </p>
 *
 * @author minji
 * @since 2020-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uas_user_defined_setting")
@ApiModel(value="UserDefinedSetting对象", description="用户自定义配置")
public class UserDefinedSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    @TableId("userid")
    private Long userid;

    @ApiModelProperty(value = "自定义配置Key")
    @TableField("setting_key")
    private String settingKey;

    @ApiModelProperty(value = "自定义配置项值")
    @TableField("setting_value")
    private String settingValue;


}
