package com.zjft.usp.common.feign.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
@Getter
@Setter
public class SysRoleDto {

    /**
     * 主键
     */
    private Long roleId;

    /**
     * 企业编号
     */
    private Long corpId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 系统类型，1=系统角色
     */
    private Integer sysType;

    /**
     * 是否可用
     */
    private String enabled;

    /**
     * 描述
     */
    private String description;

}
