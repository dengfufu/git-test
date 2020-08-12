package com.zjft.usp.anyfix.work.request.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author zrlin
 * @date 2020-03-05 12:02
 */
@ApiModel(value = "工单导出信息")
@Getter
@Setter
public class WorkRequestExportDto {

    @ColumnWidth(20)
    @ExcelProperty(value = "工单状态", index = 0)
    private String workStatusName;

    @ColumnWidth(20)
    @ExcelProperty(value = "工单编号", index = 1)
    private String workCode;

    @ColumnWidth(20)
    @ExcelProperty(value = "委托单号", index = 2)
    private String checkWorkCode;

    @ColumnWidth(10)
    @ExcelProperty(value = "省", index = 3)
    private String provinceName;

    @ColumnWidth(10)
    @ExcelProperty(value = "市", index = 4)
    private String cityName;

    @ColumnWidth(10)
    @ExcelProperty(value = "区", index = 5)
    private String districtName;

    @ColumnWidth(30)
    @ExcelProperty(value = "客户名称", index = 6)
    private String customCorpName;

    @ColumnWidth(20)
    @ExcelProperty(value = "设备类型", index = 7)
    private String smallClassName;

    @ColumnWidth(20)
    @ExcelProperty(value = "出厂序列号", index = 8)
    private String serial;

    @ColumnWidth(20)
    @ExcelProperty(value = "品牌", index = 9)
    private String brandName;

    @ColumnWidth(20)
    @ExcelProperty(value = "型号", index = 10)
    private String modelName;

    @ColumnWidth(20)
    @ExcelProperty(value = "设备规格", index = 11)
    private String specificationName;

    @ColumnWidth(20)
    @ExcelProperty(value = "工单来源", index = 12)
    private String sourceName;

    @ColumnWidth(20)
    @ExcelProperty(value = "工单类型", index = 13)
    private String workTypeName;

    @ColumnWidth(20)
    @ExcelProperty(value = "建单人", index = 14)
    private String creatorName;

    @ColumnWidth(20)
    @ExcelProperty(value = "创建日期", index = 15)
    private String createTime;

    @ColumnWidth(20)
    @ExcelProperty(value = "委托商", index = 16)
    private String demanderCorpName;

    @ColumnWidth(20)
    @ExcelProperty(value = "设备网点", index = 17)
    private String deviceBranchName;

    @ColumnWidth(20)
    @ExcelProperty(value = "服务商", index = 18)
    private String serviceCorpName;

    @ColumnWidth(20)
    @ExcelProperty(value = "服务网点", index = 19)
    private String serviceBranchName;

    @ColumnWidth(20)
    @ExcelProperty(value = "服务工程师", index = 20)
    private String engineerName;


    @ColumnWidth(20)
    @ExcelProperty(value = "协同工程师", index = 21)
    private String togetherEngineers;


    @ColumnWidth(20)
    @ExcelProperty(value = "外部协同人员", index = 22)
    private String helpNames;

    @ColumnWidth(20)
    @ExcelProperty(value = "提单日期", index = 23)
    private String dispatchTime;

    @ColumnWidth(20)
    @ExcelProperty(value = "出发时间", index = 24)
    private String goTime;

    @ColumnWidth(20)
    @ExcelProperty(value = "到达时间", index = 25)
    private String signTime;

    @ColumnWidth(20)
    @ExcelProperty(value = "完单时间", index = 26)
    private String endTime;

    @ColumnWidth(20)
    @ExcelProperty(value = "服务方式", index = 27)
    private String serviceModeName;

    @ColumnWidth(20)
    @ExcelProperty(value = "服务请求", index = 28)
    private String serviceRequest;

    @ColumnWidth(20)
    @ExcelProperty(value = "联系人名字", index = 29)
    private String contactName;

    @ColumnWidth(20)
    @ExcelProperty(value = "联系人电话", index = 30)
    private String contactPhone;

    @ColumnWidth(20)
    @ExcelProperty(value = "详细地址", index = 31)
    private String address;

    @ColumnWidth(20)
    @ExcelProperty(value = "维保方式", index = 32)
    private String warrantyModeName;

    @ColumnWidth(20)
    @ExcelProperty(value = "故障现象", index = 33)
    private String faultTypeName;

    @ColumnWidth(20)
    @ExcelProperty(value = "预约时间", index = 34)
    private Date bookTimeEnd;

    @ColumnWidth(20)
    @ExcelProperty(value = "接单时间", index = 35)
    private String acceptTime;

}
