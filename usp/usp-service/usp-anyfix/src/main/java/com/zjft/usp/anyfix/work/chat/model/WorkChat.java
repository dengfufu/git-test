package com.zjft.usp.anyfix.work.chat.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author linzerun
 * @since 2020-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_chat")
@ApiModel(value="WorkChat对象", description="")
public class WorkChat implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "工单编号")
    private Long workId;

    @ApiModelProperty(value = "顺序号")
    private Integer msgOrder;

    @ApiModelProperty(value = "消息类型，1文本、2图片、3视频、4音频、5文件")
    private Short msgType;

    @ApiModelProperty(value = "发送内容")
    private String content;

    @ApiModelProperty(value = "文件编号")
    private Long fileId;

    @ApiModelProperty(value = "发送人")
    private Long userId;

    @ApiModelProperty(value = "发送时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date createTime;

    @ApiModelProperty(value = "操作类型，1撤回、2屏蔽、3删除")
    private Integer operateType;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    private Date operateTime;

    @ApiModelProperty(value = "缩略图")
    private Long thumbnail;

    @ApiModelProperty(value = "发送人公司")
    private Long corpId;

}
