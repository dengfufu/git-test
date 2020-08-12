package com.zjft.usp.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author zgpi
 * @version 1.0
 * @date 2019/11/26 11:04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Right implements Serializable {

    private static final long serialVersionUID = 749360940290141180L;

    private Long rightId;
    private String pathMethod;
    private Integer rightType;
}
