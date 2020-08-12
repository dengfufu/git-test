package com.zjft.usp.common.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

/**
 * 权限点
 *
 * @author CK
 * @date 2019-08-05 16:28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("uas_permission")
public class Permission extends SuperEntity {

    private static final long serialVersionUID = 749360940290141180L;

    private Long parentId;
    private String name;
    private String css;
    private String url;
    private String path;
    private Integer sort;
    private Integer type;
    private Boolean hidden;
    /**
     * 请求的类型
     */
    private String pathMethod;

    @TableField(exist = false)
    private List<Permission> subPermission;
    @TableField(exist = false)
    private Long roleId;
    @TableField(exist = false)
    private Set<Long> permissionIds;
}
