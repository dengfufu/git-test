package com.zjft.usp.anyfix.work.fee.dto;

import com.zjft.usp.anyfix.work.fee.model.WorkPost;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 工单换下备件邮寄费Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2020-01-07 20:23
 **/
@ApiModel("工单换下备件邮寄费Dto")
@Getter
@Setter
public class WorkPostDto extends WorkPost {

    @ApiModelProperty("邮寄方式名称")
    private String postWayName;

    @ApiModelProperty("企业编号")
    private Long corpId;

}
