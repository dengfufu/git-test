package com.zjft.usp.common.feign.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 权限映射表
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
@Getter
@Setter
public class SysRightUrlDto {

    /**
     * 主键
     */
    private Long id;

    /**
     * 权限类型，1=公共权限
     */
    private Integer rightType;

    /**
     * 权限对应的请求URI
     */
    private String uri;

    /**
     * 请求方法
     */
    private String pathMethod;

    /**
     * 权限ID
     */
    private Long rightId;

    /**
     * 描述
     */
    private String description;


}
