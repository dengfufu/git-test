package com.zjft.usp.anyfix.goods.model;

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
 * 物品寄送基本信息表
 * </p>
 *
 * @author zgpi
 * @since 2020-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("goods_post_m")
@ApiModel(value="GoodsPost对象", description="物品寄送基本信息表")
public class GoodsPost implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "寄送单ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "寄送单号")
    private String postNo;

    @ApiModelProperty(value = "发货企业")
    private Long consignCorp;

    @ApiModelProperty(value = "发货网点")
    private Long consignBranch;

    @ApiModelProperty(value = "发货人")
    private Long consignUser;

    @ApiModelProperty(value = "发货人姓名")
    private String consignUserName;

    @ApiModelProperty(value = "发货人联系电话")
    private String consignUserPhone;

    @ApiModelProperty(value = "发货时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date consignTime;

    @ApiModelProperty(value = "发货行政区划")
    private String consignArea;

    @ApiModelProperty(value = "发货详细地址")
    private String consignAddress;

    @ApiModelProperty(value = "发货备注")
    private String consignNote;

    @ApiModelProperty(value = "收货企业")
    private Long receiveCorp;

    @ApiModelProperty(value = "收货网点")
    private Long receiveBranch;

    @ApiModelProperty(value = "收货行政区划")
    private String receiveArea;

    @ApiModelProperty(value = "收货详细地址")
    private String receiveAddress;

    @ApiModelProperty(value = "收货人")
    private Long receiver;

    @ApiModelProperty(value = "收货人姓名")
    private String receiverName;

    @ApiModelProperty(value = "收货人联系电话")
    private String receiverPhone;

    @ApiModelProperty(value = "收货备注")
    private String receiveNote;

    @ApiModelProperty(value = "运输方式，1=自提 2=托运 3=快递")
    private Integer transportType;

    @ApiModelProperty(value = "托运方式，10=汽车 20=火车 30=轮船 40=飞机 50=其他")
    private Integer consignType;

    @ApiModelProperty(value = "快递公司名称")
    private String expressCorpName;

    @ApiModelProperty(value = "快递单号")
    private String expressNo;

    @ApiModelProperty(value = "快递类型，1=物流 2=快件 3=慢件")
    private Integer expressType;

    @ApiModelProperty(value = "总箱数")
    private Integer boxNum;

    @ApiModelProperty(value = "付费方式")
    private Integer payWay;

    @ApiModelProperty(value = "邮寄费")
    private BigDecimal postFee;

    @ApiModelProperty(value = "签收状态")
    private Integer signStatus;

    @ApiModelProperty(value = "签收人")
    private Long signer;

    @ApiModelProperty(value = "签收时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date signTime;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private Long editor;

    @ApiModelProperty(value = "更新时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date editTime;

}
