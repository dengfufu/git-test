package com.zjft.usp.file.dto;

import lombok.Data;
import java.util.Date;

/**
 * description: FileTemporary
 * date: 2019/9/24 14:57
 * author: cxd
 * version: 1.0
 */
@Data
public class FileTemporaryDto {

    /** 文件ID **/
    private String fileId;
    /** 上传时间 **/
    private Date addTime;
}
