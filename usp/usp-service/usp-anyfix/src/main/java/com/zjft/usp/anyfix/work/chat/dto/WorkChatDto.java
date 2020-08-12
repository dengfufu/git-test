package com.zjft.usp.anyfix.work.chat.dto;

import com.zjft.usp.anyfix.work.chat.model.WorkChat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zrlin
 * @date 2020-03-09 13:39
 */
@Data
public class WorkChatDto extends WorkChat{

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "公司名")
    private String corpName;

    @ApiModelProperty(value = "头像")
    private String faceImg;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "拓展名")
    private String extension;

    @ApiModelProperty(value = "高清文件图片")
    private String imgBigId;

    @ApiModelProperty(value = "是否为图片")
    private boolean img;

    @ApiModelProperty(value = "文件大小")
    private Long size;


}
