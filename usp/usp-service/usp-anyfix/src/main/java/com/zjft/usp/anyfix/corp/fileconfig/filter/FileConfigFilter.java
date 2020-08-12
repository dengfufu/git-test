package com.zjft.usp.anyfix.corp.fileconfig.filter;

import com.zjft.usp.common.model.Page;
import lombok.Data;

@Data
public class FileConfigFilter extends Page {
    private Long serviceCorp;
    private Long demanderCorp;
    private Integer workType;
    private Integer formType;
    private Long refId;
    private Long workId;
}
