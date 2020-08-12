package com.zjft.usp.anyfix.work.auto.dto;

import com.zjft.usp.anyfix.work.auto.model.WorkAssignMode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ljzhu
 * @Package com.zjft.anyfix.work.dto
 * @date 2019-09-29 10:43
 * @note
 */
@Getter
@Setter
public class WorkAssignModeDto extends WorkAssignMode {

    /**
     * 设备客户
     */
    private Long customCorp;

    private Long demanderCorp;

    private String demanderCorpName;

    /**
     * 工单类型
     */
    private String workType;

    /**
     * 设备大类
     */
    private Long largeClassId;

    /**
     * 设备小类
     */
    private String smallClassId;

    /**
     * 设备品牌
     */
    private String brandId;

    /**
     * 设备型号
     */
    private String modelId;

    /**
     * 行政区划
     */
    private String district;

    /**
     * 设备网点
     */
    private String deviceBranch;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 人员列表
     */
    private String userList;

    /**
     * 派单半径
     */
    private Integer distance;

    /**
     * 等待认领时间
     */
    private Integer waiting;

    /**
     * 匹配工程师技能
     */
    private Integer skilled;

    /**
     * 设备客户 ID
     */
    private String serviceCorpName;

    /**
     * 设备客户 ID
     */
    private String customCorpName;

    /**
     * 工单类型
     */
    private String workTypeName;


    /**
     * 设备大类
     */
    private String largeClassName;

    /**
     * 设备小类
     */
    private String smallClassName;

    /**
     * 设备品牌
     */
    private String brandName;

    /**
     * 设备型号
     */
    private String modelName;

    /**
     * 行政区划
     */
    private String districtName;

    /**
     * 设备网点
     */
    private String deviceBranchName;

    private String userNameList;
}
