package com.zjft.usp.common.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 分页查询条件
 *
 * @author CK
 * @date 2019-09-30 10:07
 */
@Setter
@Getter
public class Page implements Serializable {

    /**
     * 页码，从1开始
     */
    private int pageNum = 1;

    /**
     * 页面大小
     */
    private int pageSize = 50;
}
