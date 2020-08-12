package com.zjft.usp.uas.user.dto;

import com.zjft.usp.uas.user.model.UserOper;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 用户操作日志DTO
 *
 * @author zgpi
 * @date 2020/5/25 14:25
 */
@Getter
@Setter
public class UserOperDto extends UserOper implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;

    private String operTypeName;

    /** 所属公司ID列表 **/
    private List<Long> corpIdList;

    /** 所属公司名称列表 **/
    private String corpName;
}
