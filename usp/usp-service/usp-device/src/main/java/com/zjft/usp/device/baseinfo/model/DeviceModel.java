package com.zjft.usp.device.baseinfo.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.device.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 设备型号表
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@ApiModel(value="DeviceModel对象", description="设备型号表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("device_model")
public class DeviceModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "型号ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "型号名称")
    private String name;

    @ApiModelProperty(value = "设备品牌")
    private Long brandId;

    @ApiModelProperty(value = "设备小类")
    private Long smallClassId;

    @ApiModelProperty(value = "是否可用")
    private String enabled;

    @ApiModelProperty(value = "企业ID")
    private Long corp;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (this.getClass() != obj.getClass())
            return false;
        DeviceModel brand = (DeviceModel) obj;
        return this.brandId == brand.brandId
                && this.name.equals(brand.name)
                && this.corp == brand.corp
                && this.smallClassId == brand.smallClassId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((corp == null) ? 0 : corp.hashCode());
        result = prime * result + ((smallClassId == null) ? 0 : smallClassId.hashCode());
        result = prime * result + ((brandId == null) ? 0 : brandId.hashCode());
        return result;
    }

}
