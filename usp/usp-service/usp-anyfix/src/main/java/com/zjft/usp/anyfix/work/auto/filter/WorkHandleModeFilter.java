package com.zjft.usp.anyfix.work.auto.filter;

import com.zjft.usp.common.model.Page;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ljzhu
 * @Package com.zjft.anyfix.work.filter
 * @date 2019-10-17 09:53
 * @note
 */
@Getter
@Setter
public class WorkHandleModeFilter extends Page {


    private Long customCorp;

    private Long demanderCorp;

    private Long serviceCorp;

    private Long serviceBranch;

    private Integer serviceMode;



}
