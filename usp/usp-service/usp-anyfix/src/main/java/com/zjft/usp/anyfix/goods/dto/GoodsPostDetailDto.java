package com.zjft.usp.anyfix.goods.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.common.feign.dto.FileInfoDto;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 物品寄送明细DTO
 *
 * @author zgpi
 * @since 2020-04-21
 */
@ApiModel("物品寄送明细DTO")
@Data
public class GoodsPostDetailDto implements Serializable {

    private static final long serialVersionUID = -7861001820322286579L;

    @ApiModelProperty(value = "明细ID")
    private Long id;

    @ApiModelProperty(value = "物品寄送申请单号")
    private Long postId;

    @ApiModelProperty(value = "物品名称")
    private String goodsName;

    @ApiModelProperty(value = "数量")
    private Integer quantity;

    @ApiModelProperty(value = "序列号")
    private String sn;

    @ApiModelProperty(value = "分箱号")
    private Integer subBoxNum;

    @ApiModelProperty(value = "是否签收， Y=是 N=否")
    private String signed;

    @ApiModelProperty(value = "签收人")
    private Long signer;

    @ApiModelProperty(value = "签收人姓名")
    private String signerName;

    @ApiModelProperty(value = "签收时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date signTime;

    @ApiModelProperty(value = "文件编号列表")
    private List<Long> fileIdList;

    @ApiModelProperty(value = "文件列表")
    private List<FileInfoDto> fileList;
}
