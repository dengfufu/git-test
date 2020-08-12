package com.zjft.usp.device.device.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 设备导入Excel模板
 *
 * @author canlei
 * @version 1.0
 * @date 2020-02-27 14:20
 **/
@ApiModel("设备导入Excel模板")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@HeadRowHeight(value = 20)
public class DeviceExcelDto {

    @ExcelProperty(index = 0 , value = "* 客户名称")
    @ColumnWidth(20)
    private String customCorpName;

    @ExcelProperty(index = 1 , value = "客户联系人")
    @ColumnWidth(20)
    private String contactName;

    @ExcelProperty(index = 2 , value = "联系人电话")
    @ColumnWidth(20)
    private String contactPhone;

    @ExcelProperty(index = 3, value = "设备网点")
    @ColumnWidth(20)
    private String deviceBranchName;

    @ExcelProperty(index = 4, value = "* 省")
    @ColumnWidth(10)
    private String provinceName;

    @ExcelProperty(index = 5, value = "市")
    @ColumnWidth(20)
    private String cityName;

    @ExcelProperty(index = 6, value = "区")
    @ColumnWidth(20)
    private String districtName;

    @ExcelProperty(index = 7, value = "详细地址")
    @ColumnWidth(20)
    private String address;

    @ExcelProperty(index = 8, value = "分布")
    @ColumnWidth(20)
    private String zoneName;

    @ExcelProperty(index = 9 , value = "服务商名称")
    @ColumnWidth(20)
    private String serviceCorpName;

    @ExcelProperty(index = 10 , value = "所属大类")
    @ColumnWidth(20)
    private String largeClassName;

    @ExcelProperty(index = 11 , value = "* 设备类型")
    @ColumnWidth(20)
    private String smallClassName;

    @ExcelProperty(index = 12 , value = "设备规格")
    @ColumnWidth(20)
    private String specificationName;

    @ExcelProperty(index = 13 , value = "* 品牌")
    @ColumnWidth(20)
    private String brandName;

    @ExcelProperty(index = 14 , value = "* 型号")
    @ColumnWidth(20)
    private String modelName;

    @ExcelProperty(index = 15 , value = "出厂序列号")
    @ColumnWidth(20)
    private String serial;

    @ExcelProperty(index = 16 , value = "自定义唯一编号")
    @ColumnWidth(20)
    private String deviceCode;

    @ExcelProperty(index = 17 , value = "设备状态")
    @ColumnWidth(20)
    private String statusName;

    @ExcelProperty(index = 18 , value = "出厂日期")
    @ColumnWidth(20)
    private String factoryDate;

    @ExcelProperty(index = 19 , value = "购买日期")
    @ColumnWidth(20)
    private String purchaseDate;

    @ExcelProperty(index = 20 , value = "安装日期")
    @ColumnWidth(20)
    private String installDate;

    @ExcelProperty(index = 21 , value = "设备描述")
    @ColumnWidth(30)
    private String description;

    @ExcelProperty(index = 22, value = "维保方式")
    @ColumnWidth(20)
    private String warrantyModeName;

    @ExcelProperty(index = 23, value = "维保合同号")
    @ColumnWidth(20)
    private String contNo;

    @ExcelProperty(index = 24 , value = "保修日期开始")
    @ColumnWidth(20)
    private String warrantyStartDate;

    @ExcelProperty(index = 25 , value = "保修日期结束")
    @ColumnWidth(20)
    private String warrantyEndDate;

    @ExcelProperty(index = 26 , value = "保修状态")
    @ColumnWidth(20)
    private String warrantyStatusName;

    @ExcelProperty(index = 27 , value = "保修说明")
    @ColumnWidth(30)
    private String warrantyNote;

    @ExcelIgnore
    @ApiModelProperty(value = "客户关系编号")
    private Long customId;

    @ExcelIgnore
    @ApiModelProperty(value = "服务商编号")
    private Long serviceCorp;

    @ExcelIgnore
    @ApiModelProperty(value = "大类编号")
    private Long largeClassId;

    @ExcelIgnore
    @ApiModelProperty(value = "设备类型编号")
    private Long smallClassId;

    @ExcelIgnore
    @ApiModelProperty(value = "设备规格编号")
    private Long specificationId;

    @ExcelIgnore
    @ApiModelProperty(value = "品牌编号")
    private Long brandId;

    @ExcelIgnore
    @ApiModelProperty(value = "型号编号")
    private Long modelId;

    @ExcelIgnore
    @ApiModelProperty(value = "维保方式")
    private Long warrantyMode;

    @ExcelIgnore
    @ApiModelProperty(value = "保修状态")
    private Integer warrantyStatus;

}
