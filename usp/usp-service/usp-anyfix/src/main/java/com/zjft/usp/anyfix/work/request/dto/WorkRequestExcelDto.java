package com.zjft.usp.anyfix.work.request.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 工单导入信息
 * @author ljzhu
 * @since 2020-02-25
 */

@ApiModel(value = "工单导入信息")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@HeadRowHeight(value = 40)
public class WorkRequestExcelDto {

    @ColumnWidth(20)
    @ExcelProperty(value = "*委托商", index = 0)
    private String demanderCorpName;

    @ColumnWidth(20)
    @ExcelProperty(value = "委托单号", index = 1)
    private String checkWorkCode;

    @ColumnWidth(20)
    @ExcelProperty(value = "*客户名称", index = 2)
    private String customCorpName;
    
    @ColumnWidth(20)
    @ExcelProperty(value = "设备网点", index = 3)
    private String deviceBranchName;

    @ExcelProperty(value = "*联系人", index = 4)
    private String contactName;

    @ExcelProperty(value = "*联系电话", index = 5)
    private String contactPhone;

    @ExcelProperty(value = "*省", index = 6)
    private String provinceName;

    @ExcelProperty(value = "*市", index = 7)
    private String cityName;

    @ExcelProperty(value = "区", index = 8)
    private String districtName;

    @ColumnWidth(20)
    @ExcelProperty(value = "*详细地址", index = 9)
    private String address;

    @ExcelProperty(value = "分布", index = 10)
    private String zoneName;

    @ColumnWidth(25)
    @ExcelProperty(value = "设备类型", index = 11)
    private String smallClassName;


    @ExcelProperty(value = "设备规格", index = 12)
    private String specificationName;

    @ColumnWidth(20)
    @ExcelProperty(value = "设备品牌", index = 13)
    private String brandName;


    @ExcelProperty(value = "设备型号", index = 14)
    private String modelName;

    @ExcelProperty(value = "*设备数量", index = 15)
    private String deviceNumStr;

    @ColumnWidth(20)
    @ExcelProperty(value = "出厂序列号", index = 16)
    private String serials;

    @ExcelProperty(value = "维保方式", index = 17)
    private String warrantyModeName;

    @ExcelProperty(value = "*工单类型", index = 18)
    private String workTypeName;


    @ExcelProperty(value = "故障代码", index = 19)
    private String faultCode;

    @ColumnWidth(20)
    @ExcelProperty(value = "故障时间(yyyy-MM-dd HH:mm)", index = 20)
    private String faultTimeStr;

    @ColumnWidth(20)
    @ExcelProperty(value = "*故障现象", index = 21)
    private String faultTypes;

    @ColumnWidth(20)
    @ExcelProperty(value = "*服务请求", index = 22)
    private String serviceRequest;

    @ColumnWidth(25)
    @ExcelProperty(value = "预约时间(yyyy-MM-dd HH:mm)", index = 23)
    private String bookTimeEndStr;

    @ExcelProperty(value = "*工单来源", index = 24)
    private String sourceName;

    @ColumnWidth(15)
    @ExcelProperty(value = "每台设备费用报价", index = 25)
    private String basicServiceFee;

}
