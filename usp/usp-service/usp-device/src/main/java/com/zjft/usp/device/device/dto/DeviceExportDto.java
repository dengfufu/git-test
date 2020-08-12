package com.zjft.usp.device.device.dto;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "设备导出信息")
public class DeviceExportDto {

    @ExcelProperty(index = 0 , value = "客户名称")
    @ColumnWidth(20)
    private String customCorpName;

    @ExcelProperty(index = 1 , value = "设备类型")
    @ColumnWidth(20)
    private String smallClassName;

    @ExcelProperty(index = 2 , value = "设备品牌")
    @ColumnWidth(20)
    private String brandName;

    @ExcelProperty(index = 3 , value = "设备型号")
    @ColumnWidth(20)
    private String modelName;

    @ExcelProperty(index = 4 , value = "出厂序列号")
    @ColumnWidth(20)
    private String serial;

    @ExcelProperty(index = 5 , value = "设备大类")
    @ColumnWidth(20)
    @ApiModelProperty(value = "设备大类")
    private String largeClassName;

    @ExcelProperty(index = 6 , value = "设备规格")
    @ColumnWidth(20)
    @ApiModelProperty(value = "设备规格")
    private String specificationName;

    @ExcelProperty(index = 7 , value = "省")
    @ColumnWidth(20)
    @ApiModelProperty(value = "省")
    private String provinceName;

    @ExcelProperty(index = 8 , value = "市")
    @ColumnWidth(20)
    @ApiModelProperty(value = "市")
    private String cityName;

    @ExcelProperty(index = 9 , value = "区县")
    @ColumnWidth(20)
    @ApiModelProperty(value = "区县")
    private String districtName;

    @ExcelProperty(index = 10 , value = "设备网点")
    @ColumnWidth(20)
    @ApiModelProperty(value = "设备网点")
    private String branchName;

    @ExcelProperty(index = 11 , value = "市郊")
    @ColumnWidth(20)
    @ApiModelProperty(value = "市郊")
    private String zoneName;

    @ExcelProperty(index = 12 , value = "联系人")
    @ColumnWidth(20)
    @ApiModelProperty(value = "联系人")
    private String contactName;

    @ExcelProperty(index = 13 , value = "联系电话")
    @ColumnWidth(20)
    @ApiModelProperty(value = "联系电话")
    private String contactPhone;

    @ExcelProperty(index = 14 , value = "详细地址")
    @ColumnWidth(20)
    @ApiModelProperty(value = "详细地址")
    private String address;

    @ExcelProperty(index = 15 , value = "设备说明")
    @ColumnWidth(20)
    @ApiModelProperty(value = "设备说明")
    private String description;

    @ExcelProperty(index = 16 , value = "委托商")
    @ColumnWidth(20)
    @ApiModelProperty(value = "委托商")
    private String demanderCorpName;

    @ExcelProperty(index = 17 , value = "服务网点")
    @ColumnWidth(20)
    @ApiModelProperty(value = "服务网点")
    private String serviceBranchName;


    @ExcelProperty(index = 18 , value = "服务商")
    @ColumnWidth(20)
    @ApiModelProperty(value = "服务商")
    private String serviceCorpName;

    @ExcelProperty(index = 19 , value = "派单主管")
    @ColumnWidth(20)
    @ApiModelProperty(value = "派单主管")
    private String workManagerName;


    @ExcelProperty(index = 20 , value = "工程师")
    @ColumnWidth(20)
    @ApiModelProperty(value = "工程师")
    private String engineerName;

    @ExcelProperty(index = 21 , value = "安装日期")
    @ColumnWidth(20)
    @ApiModelProperty(value = "安装日期")
    private String installDate;

    @ExcelProperty(index = 22 , value = "保修状态")
    @ColumnWidth(20)
    @ApiModelProperty(value = "保修状态")
    private String warrantyStatusName;

    @ExcelProperty(index = 23 , value = "设备状态")
    @ColumnWidth(20)
    @ApiModelProperty(value = "设备状态")
    private String statusName;

    @ExcelProperty(index = 24 , value = "出厂日期")
    @ColumnWidth(20)
    @ApiModelProperty(value = "出厂日期")
    private String factoryDate;

    @ExcelProperty(index = 25 , value = "购买日期")
    @ColumnWidth(20)
    @ApiModelProperty(value = "购买日期")
    private String purchaseDate;

    @ExcelProperty(index = 26 , value = "维保方式")
    @ColumnWidth(20)
    @ApiModelProperty(value = "维保方式")
    private String warrantyModeName;

    @ExcelProperty(index = 27 , value = "维保合同号")
    @ColumnWidth(20)
    @ApiModelProperty(value = "维保合同号")
    private String contNo;

    @ExcelProperty(index = 28 , value = "保修起始日期")
    @ColumnWidth(20)
    @ApiModelProperty(value = "保修起始日期")
    private String warrantyStartDate;

    @ExcelProperty(index = 29 , value = "保修结束日期")
    @ColumnWidth(20)
    @ApiModelProperty(value = "保修结束日期")
    private String warrantyEndDate;

    @ExcelProperty(index = 30 , value = "保修说明")
    @ColumnWidth(20)
    @ApiModelProperty(value = "保修说明")
    private String warrantyNote;

    @ExcelProperty(index = 31 , value = "自定义设备编号")
    @ColumnWidth(20)
    @ApiModelProperty(value = "自定义设备编号")
    private String deviceCode;

}
