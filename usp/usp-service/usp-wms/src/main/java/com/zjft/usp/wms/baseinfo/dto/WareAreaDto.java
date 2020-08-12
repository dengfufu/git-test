package com.zjft.usp.wms.baseinfo.dto;

import com.zjft.usp.wms.baseinfo.model.WareArea;
import com.zjft.usp.wms.baseinfo.model.WareAreaUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author : dcyu
 * @Date : 2019/11/18 18:52
 * @Desc : 物料区域信息数据传输对象
 * @Version 1.0.0
 */
@Data
public class WareAreaDto extends WareArea {

    @ApiModelProperty("用户信息数组")
    Long[] users;

    @ApiModelProperty("用户名")
    String userNames;

    @ApiModelProperty("用户信息列表")
    List<WareAreaUser> wareAreaUserList;
}
