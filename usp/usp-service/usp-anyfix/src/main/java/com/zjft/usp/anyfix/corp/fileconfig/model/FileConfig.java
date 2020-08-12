package com.zjft.usp.anyfix.corp.fileconfig.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 文件配置定义表
 * </p>
 *
 * @author zrlin
 * @since 2020-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("file_config")
@ApiModel(value="FileConfig对象", description="文件配置定义表")
public class FileConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键编号")
    private Long id;

    @ApiModelProperty(value = "引用客户委托商关系主键")
    private Long refId;

    @ApiModelProperty(value = "分组名")
    private String groupName;

    @ApiModelProperty(value = "用于哪个提交表单 1为服务完成")
    private Integer formType;

    @ApiModelProperty(value = "最少图片数量")
    private Integer minNum;

    @ApiModelProperty(value = "最多图片数量")
    private Integer maxNum;

    @ApiModelProperty(value = "工单类型")
    private Integer workType;

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    private Date operateTime;


}
