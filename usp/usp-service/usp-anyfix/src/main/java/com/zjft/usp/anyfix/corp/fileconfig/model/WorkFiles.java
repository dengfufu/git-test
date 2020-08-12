package com.zjft.usp.anyfix.corp.fileconfig.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 分组文件表
 * </p>
 *
 * @author zrlin
 * @since 2020-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_files")
@ApiModel(value="WorkFiles对象", description="分组文件表")
public class WorkFiles implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键编号")
    @TableId
    private Long id;

    @ApiModelProperty(value = "配置编号")
    private Long configId;

    @ApiModelProperty(value = "工单编号")
    private Long workId;

    @ApiModelProperty(value = "该分组下的文件列表")
    private String fileIds;


}
