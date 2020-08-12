package com.zjft.usp.anyfix.corp.manage.dto;

import com.zjft.usp.anyfix.corp.manage.model.DemanderServiceManager;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 委托商与服务商客户经理关系
 *
 * @author zgpi
 * @date 2020/6/15 13:34
 */
@Getter
@Setter
public class DemanderServiceManagerDto implements Serializable {

    @ApiModelProperty(value = "关系ID")
    private Long referId;

    @ApiModelProperty(value = "委托商")
    private Long demanderCorp;

    @ApiModelProperty(value = "服务商")
    private Long serviceCorp;

    @ApiModelProperty(value = "客户经理列表")
    private List<DemanderServiceManager> demanderServiceManagerList;

    @ApiModelProperty(value = "客户经理编号列表")
    private List<Long> managerList;

    @ApiModelProperty(value = "客户经理姓名列表")
    private List<String> managerNameList;
}
