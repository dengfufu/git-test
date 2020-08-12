package com.zjft.usp.uas.corp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zjft.usp.uas.corp.model.CorpAdmin;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * TODO
 *
 * @author canlei
 * @version 1.0
 * @date 2019-09-29 10:44
 **/
@Getter
@Setter
public class CorpAdminDto extends CorpAdmin {

    @ApiModelProperty("姓名")
    private String userName;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("头像文件编号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String faceImg;

}
