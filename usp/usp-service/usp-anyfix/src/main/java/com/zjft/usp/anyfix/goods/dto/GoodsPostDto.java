package com.zjft.usp.anyfix.goods.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.common.feign.dto.FileInfoDto;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import com.zjft.usp.anyfix.goods.model.GoodsPostFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 物品寄送信息DTO
 *
 * @author zgpi
 * @since 2020-04-16
 */
@ApiModel("物品寄送信息DTO")
@Data
public class GoodsPostDto implements Serializable {

    private static final long serialVersionUID = 254748646680627978L;

    @ApiModelProperty(value = "寄送单ID")
    private Long id;

    @ApiModelProperty(value = "寄送单号")
    private String postNo;

    @ApiModelProperty(value = "发货企业")
    private Long consignCorp;

    @ApiModelProperty(value = "发货企业名称")
    private String consignCorpName;

    @ApiModelProperty(value = "发货网点")
    private Long consignBranch;

    @ApiModelProperty(value = "发货网点名称")
    private String consignBranchName;

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

    @ApiModelProperty(value = "发货行政区划名称")
    private String consignAreaName;

    @ApiModelProperty(value = "发货省份名称")
    private String consignProvinceName;

    @ApiModelProperty(value = "发货城市名称")
    private String consignCityName;

    @ApiModelProperty(value = "发货区县名称")
    private String consignDistrictName;

    @ApiModelProperty(value = "发货行政区划列表")
    private List<String> consignAreaList;

    @ApiModelProperty(value = "发货详细地址")
    private String consignAddress;

    @ApiModelProperty(value = "发货备注")
    private String consignNote;

    @ApiModelProperty(value = "收货企业")
    private Long receiveCorp;

    @ApiModelProperty(value = "收货企业名称")
    private String receiveCorpName;

    @ApiModelProperty(value = "收货网点")
    private Long receiveBranch;

    @ApiModelProperty(value = "收货网点名称")
    private String receiveBranchName;

    @ApiModelProperty(value = "收货行政区划")
    private String receiveArea;

    @ApiModelProperty(value = "收货行政区划")
    private String receiveAreaName;

    @ApiModelProperty(value = "收货省份名称")
    private String receiveProvinceName;

    @ApiModelProperty(value = "收货城市名称")
    private String receiveCityName;

    @ApiModelProperty(value = "收货区县名称")
    private String receiveDistrictName;

    @ApiModelProperty(value = "收货行政区划列表")
    private List<String> receiveAreaList;

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

    @ApiModelProperty(value = "运输方式名称")
    private String transportTypeName;

    @ApiModelProperty(value = "托运方式，10=汽车 20=火车 30=轮船 40=飞机 50=其他")
    private Integer consignType;

    @ApiModelProperty(value = "托运方式名称")
    private String consignTypeName;

    @ApiModelProperty(value = "快递公司名称")
    private String expressCorpName;

    @ApiModelProperty(value = "快递单号")
    private String expressNo;

    @ApiModelProperty(value = "快递类型，1=物流 2=快件 3=慢件")
    private Integer expressType;

    @ApiModelProperty(value = "快递类型名称")
    private String expressTypeName;

    @ApiModelProperty(value = "总箱数")
    private Integer boxNum;

    @ApiModelProperty(value = "付费方式")
    private Integer payWay;

    @ApiModelProperty(value = "付费方式名称")
    private String payWayName;

    @ApiModelProperty(value = "邮寄费")
    private BigDecimal postFee;

    @ApiModelProperty(value = "签收状态")
    private Integer signStatus;

    @ApiModelProperty(value = "签收状态名称")
    private String signStatusName;

    @ApiModelProperty(value = "签收人")
    private Long signer;

    @ApiModelProperty(value = "签收人姓名")
    private String signerName;

    @ApiModelProperty(value = "签收时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date signTime;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "创建人姓名")
    private String creatorName;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private Long editor;

    @ApiModelProperty(value = "更新人姓名")
    private String editorName;

    @ApiModelProperty(value = "更新时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date editTime;

    @ApiModelProperty(value = "工单信息列表")
    private List<GoodsPostWorkDto> goodsPostWorkDtoList;

    @ApiModelProperty(value = "文件信息列表")
    private List<GoodsPostFile> goodsPostFileList;

    @ApiModelProperty(value = "文件编号列表")
    private List<Long> fileIdList;

    @ApiModelProperty(value = "文件列表")
    private List<FileInfoDto> fileList;

    @ApiModelProperty(value = "物品明细列表")
    private List<GoodsPostDetailDto> goodsPostDetailDtoList;

    @ApiModelProperty(value = "是否属于发货企业人员")
    private String ifConsignCorpUser;

    @ApiModelProperty(value = "是否属于收货企业人员")
    private String ifReceiveCorpUser;

}
