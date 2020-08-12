package com.zjft.usp.anyfix.corp.branch.dto;

import com.zjft.usp.common.model.Page;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zrlin
 * @date 2019-10-11 09:17
 */
@Setter
@Getter
public class DeviceBranchSelect extends Page{
    private Long customCorp;
    private String branchName;
    private List<String> area;
}
