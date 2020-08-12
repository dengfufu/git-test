package com.zjft.usp.zj.work.cases.atmcase.dto.partreplace;

import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.CaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 备件更换列表DTO类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-31 16:42
 **/
@ApiModel("备件更换列表DTO类")
@Data
public class PartReplaceListDto {

    @ApiModelProperty(value = "备件更换列表")
    private List<PartReplaceDto> partReplaceList;

    @ApiModelProperty(value = "ATM机CASE")
    private CaseDto caseDto;
}
