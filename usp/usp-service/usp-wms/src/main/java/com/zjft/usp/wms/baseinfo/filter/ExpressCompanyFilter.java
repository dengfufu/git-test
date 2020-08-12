package com.zjft.usp.wms.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author : dcyu
 * @Date : 2019/11/21 19:23
 * @Desc : 快递公司过滤器
 * @Version 1.0.0
 */
@Setter
@Getter
public class ExpressCompanyFilter extends Page {

    private String name;

    private Long corpId;
}
