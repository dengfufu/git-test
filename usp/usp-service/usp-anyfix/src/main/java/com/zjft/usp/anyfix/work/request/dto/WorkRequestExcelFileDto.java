package com.zjft.usp.anyfix.work.request.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * 工单导入信息
 * @author ljzhu
 * @since 2020-02-25
 */

@Getter
@Setter
public class WorkRequestExcelFileDto {

    @ApiModelProperty(value = "上传excel")
    private MultipartFile file;

    @ApiModelProperty(value = "是否需要自动提单")
    private String autoHandel;
}
