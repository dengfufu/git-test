package com.zjft.usp.anyfix.goods.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 物品寄送单导出信息
 *
 * @author zgpi
 * @date 2020-4-27 16:30
 */
@ApiModel(value = "物品寄送单导出信息")
@Getter
@Setter
public class GoodsPostExportDto implements Serializable {

    @ColumnWidth(20)
    @ExcelProperty(value = "寄送单号", index = 0)
    private String postNo;

    @ColumnWidth(10)
    @ExcelProperty(value = "签收状态", index = 1)
    private String signStatusName;

    @ColumnWidth(20)
    @ExcelProperty(value = "发货企业", index = 2)
    private String consignCorpName;

    @ColumnWidth(20)
    @ExcelProperty(value = "发货网点", index = 3)
    private String consignBranchName;

    @ColumnWidth(20)
    @ExcelProperty(value = "发货人", index = 4)
    private String consignUserName;

    @ColumnWidth(20)
    @ExcelProperty(value = "发货人联系电话", index = 5)
    private String consignUserPhone;

    @ColumnWidth(20)
    @ExcelProperty(value = "发货时间(yyyy-MM-dd)", index = 6)
    private String consignTimeStr;

    @ColumnWidth(10)
    @ExcelProperty(value = "省", index = 7)
    private String consignProvinceName;

    @ColumnWidth(10)
    @ExcelProperty(value = "市", index = 8)
    private String consignCityName;

    @ColumnWidth(10)
    @ExcelProperty(value = "区", index = 9)
    private String consignDistrictName;

    @ColumnWidth(20)
    @ExcelProperty(value = "发货详细地址", index = 10)
    private String consignAddress;

    @ColumnWidth(20)
    @ExcelProperty(value = "发货备注", index = 11)
    private String consignNote;

    @ColumnWidth(20)
    @ExcelProperty(value = "收货企业", index = 12)
    private String receiveCorpName;

    @ColumnWidth(20)
    @ExcelProperty(value = "收货网点", index = 13)
    private String receiveBranchName;

    @ColumnWidth(10)
    @ExcelProperty(value = "省", index = 14)
    private String receiveProvinceName;

    @ColumnWidth(10)
    @ExcelProperty(value = "市", index = 15)
    private String receiveCityName;

    @ColumnWidth(10)
    @ExcelProperty(value = "区", index = 16)
    private String receiveDistrictName;

    @ColumnWidth(20)
    @ExcelProperty(value = "收货详细地址", index = 17)
    private String receiveAddress;

    @ColumnWidth(20)
    @ExcelProperty(value = "收货人", index = 18)
    private String receiverName;

    @ColumnWidth(20)
    @ExcelProperty(value = "收货人联系电话", index = 19)
    private String receiverPhone;

    @ColumnWidth(20)
    @ExcelProperty(value = "收货备注", index = 20)
    private String receiveNote;

    @ColumnWidth(10)
    @ExcelProperty(value = "运输方式", index = 21)
    private String transportTypeName;

    @ColumnWidth(10)
    @ExcelProperty(value = "托运方式", index = 22)
    private String consignTypeName;

    @ColumnWidth(20)
    @ExcelProperty(value = "快递公司", index = 23)
    private String expressCorpName;

    @ColumnWidth(20)
    @ExcelProperty(value = "快递单号", index = 24)
    private String expressNo;

    @ColumnWidth(10)
    @ExcelProperty(value = "快递类型", index = 25)
    private String expressTypeName;

    @ColumnWidth(10)
    @ExcelProperty(value = "总箱数", index = 26)
    private Integer boxNum;

    @ColumnWidth(10)
    @ExcelProperty(value = "付费方式", index = 27)
    private String payWayName;

    @ColumnWidth(20)
    @ExcelProperty(value = "邮寄费", index = 28)
    private BigDecimal postFee;

    @ColumnWidth(20)
    @ExcelProperty(value = "签收人", index = 29)
    private String signerName;

    @ColumnWidth(20)
    @ExcelProperty(value = "签收时间(yyyy-MM-dd HH:mm)", index = 30)
    private String signTimeStr;

    @ColumnWidth(20)
    @ExcelProperty(value = "创建人", index = 31)
    private String creatorName;

    @ColumnWidth(20)
    @ExcelProperty(value = "创建时间(yyyy-MM-dd HH:mm)", index = 32)
    private String createTimeStr;

}
