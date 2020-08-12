package com.zjft.usp.anyfix.work.fee.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对账单明细导出信息
 *
 * @author user
 * @version 1.0
 * @date 2020-06-02 10:11
 **/
@ApiModel(value = "对账单明细导出信息")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@HeadRowHeight(value = 40)
public class WorkFeeVerifyDetailExcelDto {

    @ColumnWidth(20)
    @ExcelProperty(value = "工单系统编号", index = 0)
    private String workId;

    @ColumnWidth(20)
    @ExcelProperty(value = "工单编号", index = 1)
    private String workCode;

    @ColumnWidth(20)
    @ExcelProperty(value = "委托单号", index = 2)
    private String checkWorkCode;

    @ColumnWidth(20)
    @ExcelProperty(value = "费用确认", index = 3)
    private String feeConfirmStatusName;

    @ColumnWidth(20)
    @ExcelProperty(value = "基础维护费", index = 4)
    private String assortBasicFee;

    @ColumnWidth(20)
    @ExcelProperty(value = "辅助人员费", index = 5)
    private String assortSupportFee;

    @ColumnWidth(20)
    @ExcelProperty(value = "郊区交通费", index = 6)
    private String suburbTrafficExpense;

    @ColumnWidth(20)
    @ExcelProperty(value = "长途交通费", index = 7)
    private String longTrafficExpense;

    @ColumnWidth(20)
    @ExcelProperty(value = "住宿费", index = 8)
    private String hotelExpense;

    @ColumnWidth(20)
    @ExcelProperty(value = "出差补助", index = 9)
    private String travelExpense;

    @ColumnWidth(20)
    @ExcelProperty(value = "邮寄费", index = 10)
    private String postExpense;

    @ColumnWidth(20)
    @ExcelProperty(value = "其他费用", index = 11)
    private String otherFee;

    @ColumnWidth(10)
    @ExcelProperty(value = "省", index = 12)
    private String provinceName;

    @ColumnWidth(10)
    @ExcelProperty(value = "市", index = 13)
    private String cityName;

    @ColumnWidth(20)
    @ExcelProperty(value = "客户", index = 14)
    private String customCorpName;

    @ColumnWidth(20)
    @ExcelProperty(value = "设备类型", index = 15)
    private String smallClassName;

    @ColumnWidth(20)
    @ExcelProperty(value = "品牌", index = 16)
    private String brandName;

    @ColumnWidth(20)
    @ExcelProperty(value = "型号", index = 17)
    private String modelName;

    @ColumnWidth(20)
    @ExcelProperty(value = "规格", index = 18)
    private String specName;

    @ColumnWidth(20)
    @ExcelProperty(value = "工单类型", index = 19)
    private String workTypeName;

    @ColumnWidth(20)
    @ExcelProperty(value = "建单人", index = 20)
    private String creatorName;

    @ColumnWidth(20)
    @ExcelProperty(value = "创建时间", index = 21)
    private String createTime;

    @ColumnWidth(20)
    @ExcelProperty(value = "工单费用(元)", index = 22)
    private String totalFee;

    @ColumnWidth(20)
    @ExcelProperty(value = "对账后费用(元)", index = 23)
    private String verifyAmount;

    @ColumnWidth(25)
    @ExcelProperty(value = "对账说明", index = 24)
    private String note;

}
