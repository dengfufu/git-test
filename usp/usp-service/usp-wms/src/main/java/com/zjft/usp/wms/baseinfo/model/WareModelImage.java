package com.zjft.usp.wms.baseinfo.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 型号图像表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ware_model_image")
@ApiModel(value="WareModelImage对象", description="型号图像表")
public class WareModelImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "型号ID")
    @TableId("model_id")
    private Long modelId;

    @ApiModelProperty(value = "图像ID")
    @TableId("image_id")
    private Long imageId;

    @ApiModelProperty(value = "顺序号")
    private Integer sortNo;


}
