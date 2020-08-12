package com.zjft.usp.anyfix.work.operate.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 工单操作过程表
 * </p>
 *
 * @author zgpi
 * @since 2019-09-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_operate")
public class WorkOperate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("id")
    private Long id;

    /**
     * 工单ID
     */
    private Long workId;

    /**
     * 关联ID
     */
    private Long referId;

    /**
     * 工单状态
     */
    private Integer workStatus;

    /**
     * 处理类型
     */
    private Integer operateType;

    /**
     * 处理组织
     */
    private Long corp;

    /**
     * 处理人
     */
    private Long operator;

    /**
     * 处理时间
     */
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;

    /**
     * 处理描述
     */
    private String summary;


}
