package com.zjft.usp.wms.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author : dcyu
 * @Date : 2019/11/20 14:55
 * @Desc : 物料分类过滤器
 * @Version 1.0.0
 */
@Setter
@Getter
public class WareCatalogFilter extends Page {

    private String name;

    private Long corpId;
}
