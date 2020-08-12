package com.zjft.usp.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 范围权限
 *
 * @author zgpi
 * @date 2020/6/4 19:43
 */
@Getter
@Setter
public class RightScopeDto implements Serializable {

    /**
     * 范围类型
     **/
    private Integer scopeType;

    /**
     * 是否有全部权限
     **/
    private String hasAllScope;

    /**
     * 是否有所在组织权限
     **/
    private String hasOwnScope;

    /**
     * 是否有所在组织的下级权限
     **/
    private String hasOwnLowerScope;

    /**
     * 指定组织ID列表
     **/
    private List<String> orgIdList;

    /**
     * 是否包含下级， Y=是 N=否
     **/
    private String containLower;

    /**
     * 范围权限类型， 1=全部，2=有部分范围，3=无范围
     **/
    private Integer scopeRightType;
}
