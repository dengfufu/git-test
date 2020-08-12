package com.zjft.usp.file.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * description: FileTemporary
 * date: 2019/9/24 14:57
 * author: cxd
 * version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("file_temporary")
public class FileTemporary {

    @TableId(value = "fileid")
    private Long fileId;

    @TableField("addtime")
    private Date addTime;
}
