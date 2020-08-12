package com.zjft.usp.anyfix.work.request.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 工单服务请求表
 * </p>
 *
 * @author zgpi
 * @since 2019-09-23
 */
@ApiModel(value="WorkRequest对象", description="工单服务请求表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_request")
public class WorkRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单ID")
    @TableId("work_id")
    private Long workId;

    @ApiModelProperty(value = "关联工单ID", notes = "用于标记拆单")
    private Long relateWorkId;

    @ApiModelProperty(value = "工单号")
    private String workCode;

    @ApiModelProperty(value = "委托单号")
    private String checkWorkCode;

    @ApiModelProperty(value = "工单类型")
    private Integer workType;

    @ApiModelProperty(value = "请求来源 1=APP，2=微信，3=PC")
    private Integer source;

    @ApiModelProperty(value = "服务请求")
    private String serviceRequest;

    @ApiModelProperty(value = "委托商")
    private Long demanderCorp;

    @ApiModelProperty(value = "设备客户")
    private Long customCorp;

    @ApiModelProperty(value = "客户ID")
    private Long customId;

    @ApiModelProperty(value = "设备网点")
    private Long deviceBranch;

    @ApiModelProperty(value = "行政区划")
    private String district;

    @ApiModelProperty(value = "城郊，1=市区 2=郊县")
    private Integer zone;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "联系人姓名")
    private String contactName;

    @ApiModelProperty(value = "联系人电话")
    private String contactPhone;

    @ApiModelProperty(value = "预约时间开始")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date bookTimeBegin;

    @ApiModelProperty(value = "预约时间结束")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date bookTimeEnd;

    @ApiModelProperty(value = "优先级")
    private Integer priority;

    @ApiModelProperty(value = "设备小类")
    private Long smallClass;

    @ApiModelProperty(value = "设备规格ID")
    private Long specification;

    @ApiModelProperty(value = "设备规格名称")
    private String specificationName;

    @ApiModelProperty(value = "设备编号")
    private String deviceCode;

    @ApiModelProperty(value = "出厂序列号")
    private String serial;

    @ApiModelProperty(value = "设备品牌")
    private Long brand;

    @ApiModelProperty(value = "设备型号")
    private Long model;

    @ApiModelProperty(value = "设备型号名称")
    private String modelName;

    @ApiModelProperty(value = "保修状态")
    private Integer warranty;

    @ApiModelProperty(value = "维保方式，10=整机保 20=单次保")
    private Integer warrantyMode;

    @ApiModelProperty(value = "故障时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date faultTime;

    @ApiModelProperty(value = "故障现象")
    private String faultType;

    @ApiModelProperty(value = "故障代码")
    private String faultCode;

    @ApiModelProperty(value = "工单附件")
    private String files;

    @ApiModelProperty(value = "经度")
    private BigDecimal lon;

    @ApiModelProperty(value = "维度")
    private BigDecimal lat;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date createTime;

    @ApiModelProperty(value = "重新提单ID")
    private Long resubmitWorkId;

    @ApiModelProperty(value = "是否补单")
    private String isSupplement;

    @ApiModelProperty(value = "设备描述")
    private String deviceDescription;

    @ApiModelProperty(value = "创建人企业编号")
    private Long creatorCorpId;
}
