package com.zjft.usp.uas.corp.dto;

import com.zjft.usp.uas.corp.model.CorpVerify;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * TODO
 *
 * @author user
 * @version 1.0
 * @date 2019-09-27 17:29
 **/
@Getter
@Setter
public class CorpVerifyDto extends CorpVerify {

    @ApiModelProperty(value = "判断参数（是否认证）")
    private String key;
}
