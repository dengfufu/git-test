package com.zjft.usp.anyfix.corp.fileconfig.dto;

import com.zjft.usp.anyfix.common.feign.dto.FileInfoDto;
import lombok.Data;

import java.util.List;

@Data
public class WorkFilesDto {
    private Long id;
    private String groupName;
    private String fileIds;
    private List<Long> fileList;
    private List<FileInfoDto> fileInfoDtosList;
    private Long configId;
    private String description;
    private Integer minNum;
    private Integer maxNum;
    private boolean isPass;
}
