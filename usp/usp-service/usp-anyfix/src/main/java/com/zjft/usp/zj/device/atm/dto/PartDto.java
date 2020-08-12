package com.zjft.usp.zj.device.atm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 维护PM更换备件记录DTO类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-28 17:07
 **/
@ApiModel(value = "维护PM更换备件记录DTO类")
@Data
public class PartDto implements Serializable {

    private static final long serialVersionUID = 5071985703153671380L;

    @ApiModelProperty(value = "备件更换记录ID")
    private String id;

    @ApiModelProperty(value = "产品系列")
    private String brandName;

    @ApiModelProperty(value = "备件模块名称")
    private String zcodeName;

    @ApiModelProperty(value = "换下备件编号")
    private String oldPartId;

    @ApiModelProperty(value = "换下备件条形码")
    private String oldBarcode;

    @ApiModelProperty(value = "换上备件编号")
    private String newPartId;

    @ApiModelProperty(value = "换上备件条形码")
    private String newBarcode;

    @ApiModelProperty(value = "更换备件数量,默认为1")
    private int quantity;

}
