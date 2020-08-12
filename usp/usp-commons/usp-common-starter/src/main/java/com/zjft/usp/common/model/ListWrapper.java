package com.zjft.usp.common.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果
 *
 * @author CK
 * @date 2019-09-30 09:55
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListWrapper<T> implements Serializable {

    /**
     * 总数
     */
    private Long total;

    /**
     * 列表
     */
    private List<T> list;
}
