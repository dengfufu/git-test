package com.zjft.usp.anyfix.work.auto.filter;

import com.zjft.usp.common.model.Page;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ljzhu
 * @date 2019-10-17 09:53
 * @note
 */
@Getter
@Setter
public class WorkAssignModeFilter extends Page {

    private Long demanderCorp;

    private Long customCorp;

    private Long serviceCorp;

    private Long serviceBranch;

    private String district;
}
