package com.zjft.usp.uas.baseinfo.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 地区
 * @author zgpi
 * @version 1.0
 * @date 2019-08-19 10:04
 **/
@Data
public class CfgAreaDto implements Serializable {

    /** 值 **/
    private String value;
    /** 文本 **/
    private String label;
    /** 是否有子节点 **/
    private boolean hasChildren;
    /** 子节点 **/
    private List<CfgAreaDto> children;
    /** 是否为单独一个 **/
    private boolean isIsLeaf;
}
