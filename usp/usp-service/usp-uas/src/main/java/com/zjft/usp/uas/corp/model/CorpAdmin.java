package com.zjft.usp.uas.corp.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author canlei
 * @date 2019-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uas_corp_admin")
@ApiModel("企业管理员表")
public class CorpAdmin extends Model<CorpAdmin> {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "userid")
    @ApiModelProperty("用户编号")
    private Long userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "corpid")
    @ApiModelProperty("企业编号")
    private Long corpId;

    @ApiModelProperty("授权时间")
    private Date grantTime;

}