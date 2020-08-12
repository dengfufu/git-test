package com.zjft.usp.anyfix.corp.user.model;

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
 * 服务网点人员表
 * </p>
 *
 * @author zgpi
 * @since 2019-09-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("service_branch_user")
public class ServiceBranchUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 网点编号
     */
    private Long branchId;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 添加时间
     */
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date addTime;


}
