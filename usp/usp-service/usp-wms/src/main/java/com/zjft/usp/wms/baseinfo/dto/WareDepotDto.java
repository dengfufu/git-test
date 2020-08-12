package com.zjft.usp.wms.baseinfo.dto;

import com.zjft.usp.wms.baseinfo.model.WareDepot;
import com.zjft.usp.wms.baseinfo.model.WareDepotUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author : dcyu
 * @Date : 2019/11/18 19:04
 * @Desc : 物料库房数据传输对象
 * @Version 1.0.0
 */
@Data
public class WareDepotDto extends WareDepot {

    @ApiModelProperty("库房负责人数组")
    Long[] users;

    @ApiModelProperty("用户名")
    String userNames;

    @ApiModelProperty("上级库房名称")
    String parentName;

    @ApiModelProperty("库房负责人列表")
    List<WareDepotUser> wareDepotUserList;

    @ApiModelProperty("物料库房子列表")
    List<WareDepotDto> children;
}
