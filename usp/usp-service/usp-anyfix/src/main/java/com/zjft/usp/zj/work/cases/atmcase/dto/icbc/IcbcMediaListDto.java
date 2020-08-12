package com.zjft.usp.zj.work.cases.atmcase.dto.icbc;

import com.zjft.usp.zj.work.cases.atmcase.dto.icbc.IcbcMediaDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: JFZOU
 * @Date: 2020-04-01 10:05
 * @Version 1.0
 */
@Data
public class IcbcMediaListDto {

    @ApiModelProperty(value = "存储介质对象列表")
    private List<IcbcMediaDto> icbcMediaDtoList;
}
