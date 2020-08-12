package com.zjft.usp.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 角色
 *
 * @author CK
 * @date 2019-08-05 15:48
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Role extends SuperEntity implements Serializable {
    private static final long serialVersionUID = 4497149010220586111L;
    private String roleId;
    private String roleName;
    private Long userId;
    private Integer sysType;

    private List<Right> rightList;
}
