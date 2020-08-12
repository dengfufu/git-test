package com.zjft.usp.anyfix.corp.manage.dto;

import com.zjft.usp.anyfix.corp.manage.model.DemanderCustom;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zrlin
 * @date 2020-01-06 14:51
 */
@ApiModel(value = "客户详情dto")
@Getter
@Setter
public class DemanderCustomDetailDto extends DemanderCustom {

    @ApiModelProperty(value = "地区")
    private String region;

    @ApiModelProperty(value = "联系电话")
    private String telephone;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "客户标签")
    private String tag;


    @ApiModelProperty(value = "工单记录")
    private List<WorkDto> workDtoList;

}
