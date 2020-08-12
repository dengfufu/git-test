package com.zjft.usp.uas.corp.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 企业注册filter
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-11 16:46
 **/
@Getter
@Setter
public class CorpRegistryFilter extends Page {

    @ApiModelProperty("企业名称")
    private String corpName;

    @ApiModelProperty("省份编号")
    private String province;

    @ApiModelProperty("城市编号")
    private String city;

    @ApiModelProperty("区县编号")
    private String district;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("企业人员编号")
    private Long userId;

    @ApiModelProperty(value = "移动端搜索栏")
    private String mobileFilter;

    @ApiModelProperty(value = "模糊查询")
    private String matchFilter;

    @ApiModelProperty(value = "企业编号")
    private Long corpId;

    @ApiModelProperty(value = "不包括的企业编号")
    private List<Long> excludeCorpIdList;

    @ApiModelProperty(value = "包括的企业编号")
    private List<Long> corpIdList;

    @ApiModelProperty("是否认证(0=未认证，1=已认证)")
    private Integer verify;
}
