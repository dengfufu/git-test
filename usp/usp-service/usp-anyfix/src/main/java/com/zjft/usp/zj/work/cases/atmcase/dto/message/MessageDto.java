package com.zjft.usp.zj.work.cases.atmcase.dto.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * 消息dto类
 *
 * @author zgpi
 * @date 2020/4/7 20:39
 */
@ApiModel(value = "消息dto类")
@Data
public class MessageDto {

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "标题内容")
    private String content;

    @ApiModelProperty(value = "消息")
    private Map<String,String> extraMap;

    @ApiModelProperty(value = "消息类型")
    private int type;

    @ApiModelProperty(value = "接收用户列表")
    private String userIds;

}
