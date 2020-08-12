package com.zjft.usp.uas.baseinfo.dto;

import lombok.Data;

import java.util.List;

@Data
public class CfgAreaWebDto {

    /** 值 **/
    private String value;
    /** 文本 **/
    private String label;
    /** 是否有子节点 **/
    private boolean isLeaf;
    /** 子节点 **/
    private List<CfgAreaDto> children;
}
