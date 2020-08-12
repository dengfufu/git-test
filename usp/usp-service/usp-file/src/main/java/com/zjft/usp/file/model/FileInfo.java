package com.zjft.usp.file.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @description: 文件信息实体类
 * @author chenxiaod
 * @date 2019/8/9 16:05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("file_info")
public class FileInfo {

    @TableId(value = "fileid")
    private Long fileId;

    @TableField("appid")
    private int appId;

    @TableField("biztype")
    private int bizType;

    @TableField("filename")
    private String fileName;

    private int format;

    private String path;

    private Long size;

    private int width;

    private int height;

    private Long duration;

    @TableField("adduserid")
    private Long addUserId;

    @TableField("addtime")
    private Date addTime;
}