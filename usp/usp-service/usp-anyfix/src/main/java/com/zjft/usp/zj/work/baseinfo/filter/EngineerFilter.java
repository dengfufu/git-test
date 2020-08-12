package com.zjft.usp.zj.work.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工程师filter类
 *
 * @author zgpi
 * @date 2020/3/27 11:27
 */
@ApiModel(value = "工程师filter类")
@Data
public class EngineerFilter extends Page {

    @ApiModelProperty(value = "服务站")
    private String serviceBranch;

    @ApiModelProperty(value = "工程师姓名")
    private String engineerName;
}
