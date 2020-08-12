package com.zjft.usp.anyfix.work.operate.dto;

import com.zjft.usp.anyfix.work.operate.model.WorkOperate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 处理过程
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/18 10:58 上午
 **/
@ApiModel("处理过程")
@Getter
@Setter
public class WorkOperateDto extends WorkOperate {

    @ApiModelProperty(value = "工单状态名称")
    private String workStatusName;

    @ApiModelProperty(value = "操作类型名称")
    private String operateTypeName;

    @ApiModelProperty(value = "操作人企业")
    private Long operatorCorp;
}
