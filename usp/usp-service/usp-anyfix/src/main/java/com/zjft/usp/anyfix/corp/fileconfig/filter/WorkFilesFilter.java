package com.zjft.usp.anyfix.corp.fileconfig.filter;

import lombok.Data;

import java.util.List;

@Data
public class WorkFilesFilter {
    private List<Long> newFileIdList;
    private List<Long> deletedFileIdList;
    private Long id;
    private List<Long> nowFileIdList;
    private Long configId;
    private Long workId;
    private Long demanderCorp;
    private Long serviceCorp;
    private Integer workType;
}
