package com.zjft.usp.wms.flow.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 流程节点处理结果DTO对象
 *
 * @Author: JFZOU
 * @Date: 2019-11-11 10:35
 */
@Data
public class FlowInstanceNodeDealResultDto implements Serializable {

    /**
     * 流程实例节点处理人ID
     */
    private Long nodeHandlerId;

    /**
     * 节点结束类型ID
     */
    private Integer nodeEndTypeId;

    /**
     * 节点结束类型名称
     */
    private String nodeEndTypeName;

    /**
     * 是否提交
     */
    private String isSubmit;

    /**
     * 节点结束处理意见
     */
    private String doDescription;

    /**
     * 退回的节点ID(退回指定节点时使用，如果没有，可不填，可选)
     */
    private Long assignNodeId;

    /**
     * 会签结论数据库实际保存值(普通会签人员的界面有此选项，可选)
     */
    private String countersignPassed;

    /**
     * 会签结论前端显示值(普通会签人员的界面有此选项，可选)
     */
    private String countersignPassedName;

}
