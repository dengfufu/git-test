package com.zjft.usp.anyfix.goods.filter;

import com.zjft.usp.common.dto.RightScopeDto;
import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 物品寄送信息FILTER
 * </p>
 *
 * @author zgpi
 * @since 2020-04-16
 */
@ApiModel("物品寄送信息FILTER")
@Data
public class GoodsPostFilter extends Page implements Serializable {

    private static final long serialVersionUID = -478272034269781347L;

    @ApiModelProperty(value = "寄送单号")
    private String postNo;

    @ApiModelProperty(value = "签收状态")
    private List<Integer> signStatusList;

    @ApiModelProperty(value = "发货企业")
    private Long consignCorp;

    @ApiModelProperty(value = "发货网点")
    private Long consignBranch;

    @ApiModelProperty(value = "发货人姓名")
    private String consignUserName;

    @ApiModelProperty(value = "发货行政区划")
    private List<String> consignAreaList;

    @ApiModelProperty(value = "发货行政区划")
    private String consignArea;

    @ApiModelProperty(value = "运输方式")
    private List<Integer> transportTypeList;

    @ApiModelProperty(value = "托运方式")
    private List<Integer> consignTypeList;

    @ApiModelProperty(value = "快递类型")
    private List<Integer> expressTypeList;

    @ApiModelProperty(value = "快递公司")
    private String expressCorpName;

    @ApiModelProperty(value = "快递单号")
    private String expressNo;

    @ApiModelProperty(value = "发货开始时间")
    private Date consignStartDate;

    @ApiModelProperty(value = "发货结束时间")
    private Date consignEndDate;

    @ApiModelProperty(value = "收货企业")
    private Long receiveCorp;

    @ApiModelProperty(value = "收货网点")
    private Long receiveBranch;

    @ApiModelProperty(value = "收货行政区划")
    private List<String> receiveAreaList;

    @ApiModelProperty(value = "收货行政区划")
    private String receiveArea;

    @ApiModelProperty(value = "收货人姓名")
    private String receiverName;

    @ApiModelProperty(value = "付费方式")
    private List<Integer> payWayList;

    @ApiModelProperty(value = "创建开始时间")
    private Date createStartDate;

    @ApiModelProperty(value = "创建结束时间")
    private Date createEndDate;

    @ApiModelProperty(value = "企业编号")
    private Long corpId;

    @ApiModelProperty(value = "用户编号")
    private Long userId;

    @ApiModelProperty(value = "权限类型")
    private Integer rightType;

    @ApiModelProperty(value = "网点编号权限列表")
    private List<Long> branchIdRightList;

    @ApiModelProperty(value = "地址查询类型", notes = "1=发货，2=收货")
    private Integer addressQueryType;

    @ApiModelProperty(value = "范围权限列表")
    List<RightScopeDto> rightScopeList;
}
