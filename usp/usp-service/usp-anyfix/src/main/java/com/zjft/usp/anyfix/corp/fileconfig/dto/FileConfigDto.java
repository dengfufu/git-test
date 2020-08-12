package com.zjft.usp.anyfix.corp.fileconfig.dto;

import com.zjft.usp.anyfix.corp.fileconfig.model.FileConfig;
import lombok.Data;

@Data
public class FileConfigDto extends FileConfig {

    private String workTypeName;
    private String formTypeName;
}
