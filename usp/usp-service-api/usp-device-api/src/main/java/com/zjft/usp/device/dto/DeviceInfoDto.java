package com.zjft.usp.device.dto;

import lombok.Data;

import java.util.Date;

/**
 * 设备信息
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/15 9:07 上午
 **/
@Data
public class DeviceInfoDto {

    /**
     * 委托商
     */
    private Long demanderCorp;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 设备小类ID
     */
    private Long smallClassId;

    /**
     * 设备小名称
     */
    private String smallClassName;

    /**
     * 设备规格
     */
    private Long specificationId;

    /**
     * 设备规格名称
     **/
    private String specificationName;

    /**
     * 设备品牌ID
     */
    private Long brandId;

    /**
     * 设备型号ID
     */
    private Long modelId;

    /**
     * 设备型号名称
     */
    private String modelName;

    /**
     * 出厂序列号
     */
    private String serial;

    /**
     * 设备序列号
     */
    private String deviceCode;

    /**
     * 委托商与客户关系编号
     */
    private Long customId;

    /**
     * 设备所属ID
     */
    private Long customCorp;

    /**
     * 设备网点
     */
    private Long branchId;

    /**
     * 行政区划
     */
    private String district;

    /**
     * 联系人
     */
    private String contactName;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 服务商
     */
    private Long serviceCorp;

    /**
     * 设备状态
     */
    private Integer status;

    /**
     * 安装日期
     */
    private Date installationDate;

    /**
     * 保修开始日期
     */
    private Date warrantyStartDate;

    /**
     * 保修结束日期
     */
    private Date warrantyEndDate;

    /**
     * 描述
     */
    private String description;

    /**
     * 服务网点
     */
    private Long serviceBranch;

    /**
     * 服务主管
     */
    private Long workManager;

    /**
     * 服务工程师
     */
    private Long engineer;

    /**
     * 出厂日期
     */
    private Date factoryDate;

    /**
     * 购买日期
     */
    private Date purchaseDate;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 操作人
     */
    private Long operator;

    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * 工单ID
     */
    private Long workId;


    /**
     * 描述
     */
    private String deviceDescription;


}
