package com.zjft.usp.uas.corp.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zrlin
 * @date 2019-11-04 16:44
 */
@Data
public class CorpUserInfoDto {

    @ApiModelProperty("企业编号")
    private Long corpId;

    @ApiModelProperty("用户编号")
    private Long userId;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("用户头像")
    private String faceImg;

}
