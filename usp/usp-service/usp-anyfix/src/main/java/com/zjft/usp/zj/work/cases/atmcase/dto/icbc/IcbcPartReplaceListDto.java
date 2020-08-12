package com.zjft.usp.zj.work.cases.atmcase.dto.icbc;

import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.OldCaseDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.icbc.IcbcPartReplaceDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: JFZOU
 * @Date: 2020-03-30 15:48
 * @Version 1.0
 */
@Data
public class IcbcPartReplaceListDto {

    @ApiModelProperty(value = "原CASE属性，不转换")
    private OldCaseDto oldCaseDto;
    @ApiModelProperty(value = "工行维修登记列表")
    private List<IcbcPartReplaceDto> icbcPartReplaceDtoList;
}
