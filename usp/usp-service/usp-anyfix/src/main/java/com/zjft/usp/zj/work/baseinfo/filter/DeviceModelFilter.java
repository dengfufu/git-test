package com.zjft.usp.zj.work.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 机器型号filter类
 *
 * @author zgpi
 * @date 2020/3/24 13:53
 */
@ApiModel(value = "机器型号filter类")
@Data
public class DeviceModelFilter extends Page {

    @ApiModelProperty(value = "型号名称")
    private String modelName;
}
