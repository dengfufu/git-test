package com.zjft.usp.wms.baseinfo.dto;

import com.zjft.usp.wms.baseinfo.model.CustomListDetail;
import com.zjft.usp.wms.baseinfo.model.CustomListMain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 常用列表数据传输类
 *
 * @author dcyu
 * @version 1.0.0
 * @since 2019-12-02 11:05
 */
@Setter
@Getter
public class CustomListMainDto extends CustomListMain {

    private Long userId;

    @ApiModelProperty("常用列表列表项")
    private List<CustomListDetail> customListDetailList;
}
